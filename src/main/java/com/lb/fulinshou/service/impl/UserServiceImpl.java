package com.lb.fulinshou.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lb.fulinshou.common.RedisKey;
import com.lb.fulinshou.common.Result;
import com.lb.fulinshou.mapper.UserMapper;
import com.lb.fulinshou.model.WXAuth;
import com.lb.fulinshou.model.WxUserInfo;
import com.lb.fulinshou.pojo.User;
import com.lb.fulinshou.pojo.dto.UserDto;
import com.lb.fulinshou.service.UserService;
import com.lb.fulinshou.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Value("${wxmini.appid}")
    private String appid;

    @Value("${wxmini.secret}")
    private String secret;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 1. 拼接URL，微信登录凭证校验接口
     * 2. 发起一个http的调用，获取微信的返回结果
     * 3. 存入redis中
     * 4. 生成一个sessionId，返回给前端，作为当前需要登录用户的标识
     *    sessionId作用是在用户下次登录时，我们可以通过标识来判断是谁点击的登录，然后可以拿出redis中对应的信息
     * @param code
     */
    public Result getSessionId(String code){
        log.info("code ==> {}", code);
        log.info("appid ==> {}", appid);
        log.info("secret ==> {}", secret);

        String replaceUrl = "https://api.weixin.qq.com/sns/jscode2session?appid="+
                appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String res = HttpUtil.get(replaceUrl);
        log.info("调用微信接口返回的值 ==> {}", res);

        String uuid = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(RedisKey.WX_SESSION_ID + uuid, res, 30, TimeUnit.MINUTES);

        Map<String, String> map = new HashMap<>();
        map.put("sessionId", uuid);
        log.info("sessionId ==> {}", uuid);

        return Result.SUCCESS(map);
    }


    @Autowired
    private WxServiceImpl wxServiceImpl;
    @Resource   //按照名称注入
    private UserMapper userMapper;
    /**
     * 1. 通过WXAuth中获取的值，对它进行解密
     * 2. 解密完成后，会获取到微信用户信息，其中包含 openId, 性别，头像，昵称，头像等信息
     * 3. openId是唯一的，所以可以去User表中查询openId是否存在，存在，就以此身份登录成功
     * 4. 不存在，则是新用户，则需要去注册，然后再进行登录
     * 5. 使用jwt技术， 生成一个token令牌，提供给前端token令牌，用户在下次访问时携带token访问，
     * 6. 那么后端就可以通过对token的验证，知道此用户是否处于登录状态，以及是哪个用户登录
     * @param wxAuth
     * @return
     */
    public Result authLogin(WXAuth wxAuth) {
        try {
            //1. 通过WXAuth中获取的值，对它进行解密，这个json就是微信的用户信息
            String wxRes = wxServiceImpl.wxDecrypt(wxAuth.getEncryptedData(), wxAuth.getSessionId(), wxAuth.getIv());
            log.info("解密后返回的微信用户信息 ==> {}", wxRes);

            //2. 解密完成后，会获取到微信用户信息，其中包含 openId, 性别，头像，昵称，头像等信息，这些信息存在WxUserInfo中
            WxUserInfo wxUserInfo = JSON.parseObject(wxRes, WxUserInfo.class);



            //返回的json中不会有openId，需要我们自己从redis中取出来
            String json =  redisTemplate.opsForValue().get(RedisKey.WX_SESSION_ID + wxAuth.getSessionId());
            log.info("从redis中取出的微信登录返回结果 ==> {}", json);

            //解析JSON格式数据
            JSONObject jsonObject = JSON.parseObject(json);
            String openId = (String) jsonObject.get("openid");
            log.info("openId ==> {}", openId);

            //将openId设置到WxUserInfo中，以便之后存入User中，然后存入user表中
            wxUserInfo.setOpenId(openId);
            log.info("wxUserInfo ==> {}", wxUserInfo);

            //判断openId是否唯一
            User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getOpenId, openId));
            log.info("user ==> {}", user);

            //将WxUserInfo微信用户信息转换成User信息
            UserDto userDto = new UserDto();
            userDto.from(wxUserInfo);

            //判断用户是否存在
            if(user == null){
                //注册
                //4. 不存在，则是新用户，则需要去注册，然后再进行登录
                return this.register(userDto);
            }else{
                //登录
                //5. 使用jwt技术， 生成一个token令牌，提供给前端token令牌，用户在下次访问时携带token访问
                //这里为userDto设置一个id，因为登录的时候需要id形成token
                userDto.setId(user.getId());

                return this.login(userDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回登录失败信息
        return Result.FAIL();
    }

    public Result login(UserDto userDto) {
        log.info("login userDto ==> {}", userDto);

        //5. 使用jwt技术， 生成一个token令牌，提供给前端token令牌，用户在下次访问时携带token访问
        String token = JWTUtils.sign(userDto.getId());
        log.info("token ==> {}", token);
        userDto.setToken(token);//这样用户就会带有一个token

        //这些信息不能给前端展示，所以设置为null
        userDto.setOpenId(null);
        log.info("添加token后的userDto ==> {}", userDto);

        //将token存入redis中，value存为userDto，下次用户访问需要登录资源的时候，可以根据token拿到用户的详细信息
        //类似于缓存了
        //七天有效时间
        redisTemplate.opsForValue().set(RedisKey.TOKEN + token, JSON.toJSONString(userDto), 7, TimeUnit.DAYS);

        return Result.SUCCESS(userDto);
    }

    public Result register(UserDto userDto) {
        log.info("register userDto ==> {}", userDto);

        //注册之前判断用户是否存在--->在调用register方法时已经判断了

        User user = new User();
        //将userDto赋值到user中
        BeanUtils.copyProperties(userDto, user);
        log.info("register user ==> {}", user);

        //然后在表中插入一条数据
        this.userMapper.insert(user);

        //这里为userDto设置一个id，因为登录的时候需要id形成token
        userDto.setId(user.getId());

        //返回userDto的信息
        //但是需要使用jwt技术，生成一个token令牌，提供给前端token令牌，用户在下次访问时携带token访问
        return this.login(userDto);
    }

    /**
     * 1. 根据token，来验证此token是否有效
     * 2. refresh，如果为true，代表刷新，重新生成token并在redis中重新存储--续期
     *             如果为false，则直接返回用户信息-->从redis中取出
     * @param token
     * @param refresh
     * @return
     */
    /*public Result userinfo(String token, Boolean refresh) {
        //首先前端传递过来的token会带有 "Bearer "，先将它去掉
        token.replace("Bearer ", "");
        //1. 根据token，来验证此token是否有效
        boolean verify = JWTUtils.verify(token);

        if(!verify){
            //如果没有验证通过，则返回未登录
            return Result.FAIL("未登录");
        }

        //再从redis中根据token拿出用户信息
        String userJson = redisTemplate.opsForValue().get(RedisKey.TOKEN + token);
        if(StringUtils.isBlank(userJson)){
            //如果用户信息为空，则依然返回未登录
            return Result.FAIL("未登录");
        }

        //首先根据redis中拿到的json来解析为userDto，以便后面根据id重新生成token
        UserDto userDto = JSON.parseObject(userJson, UserDto.class);
        //2. 判断refresh
        if(refresh){
            //2. 如果为true，代表刷新，重新生成token并在redis中重新存储--续期
            token = JWTUtils.sign(userDto.getId());
            userDto.setToken(token);
            //再次存储到redis中
            redisTemplate.opsForValue().set(RedisKey.TOKEN + token, JSON.toJSONString(userDto), 7, TimeUnit.DAYS);
        }

        //2. 如果为false，则直接返回用户信息-->从redis中取出
        return Result.SUCCESS(userDto);
    }*/
}
