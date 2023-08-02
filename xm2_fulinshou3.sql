/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.28 : Database - xm2_fulinshou3
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`xm2_fulinshou3` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `xm2_fulinshou3`;

/*Table structure for table `carousel` */

DROP TABLE IF EXISTS `carousel`;

CREATE TABLE `carousel` (
  `carousel_id` bigint NOT NULL AUTO_INCREMENT COMMENT '首页轮播图id',
  `carousel_photo` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '首页轮播图图片url',
  PRIMARY KEY (`carousel_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='首页轮播图表';

/*Data for the table `carousel` */

insert  into `carousel`(`carousel_id`,`carousel_photo`) values 
(1,'https://pic.imgdb.cn/item/64c63ae61ddac507cca5e491.jpg'),
(2,'https://pic.imgdb.cn/item/64c63ae61ddac507cca5e56a.jpg'),
(3,'https://pic.imgdb.cn/item/64c63ae61ddac507cca5e591.jpg');

/*Table structure for table `goods` */

DROP TABLE IF EXISTS `goods`;

CREATE TABLE `goods` (
  `goods_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `goods_title` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品标题',
  `goods_price` int NOT NULL COMMENT '商品单价，单位分',
  `goods_formula` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品配方',
  `goods_effect` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品功效',
  PRIMARY KEY (`goods_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='商品表';

/*Data for the table `goods` */

insert  into `goods`(`goods_id`,`goods_title`,`goods_price`,`goods_formula`,`goods_effect`) values 
(1,'茯苓人参尊养茶',599,'熟普洱+ 冬虫夏草+ 小罐茯苓茶（人参、陈皮、菊花、红枣、颗枸杞、玛咖片、桑葚、茯神、陈皮）','气血双补，补肾精肾阳'),
(2,'茯苓甜梦茶',999,'金花茯砖茶（安化） +小罐茯苓茶（茯神、酸枣仁、百合干、胎菊、枸杞、20年老陈皮）','养心安神'),
(3,'茯苓清润茶',1599,'金花茯砖茶（安化）+ 小罐茯苓茶（茯苓、胖大海、贡菊、金银花、薄荷叶、20年老陈皮）','润喉护嗓'),
(4,'茯苓养生三高茶',500,'熟普洱+ 小罐茯苓茶（茯神+菊花+山楂+决明子+牛蒡子+20年老陈皮）','降三高'),
(5,'茯苓玫瑰女神茶',666,'生普洱茶+ 小罐茯苓茶(玫瑰花+桂圆干+红枣+枸杞+菊花+茯苓+20年老陈皮)','美容养颜，减肥'),
(6,'茯苓祛湿茶',1299,'金骏眉 + 小罐茯苓茶（茯苓+薏米仁+芡实+赤小豆+山楂+红枣+20年老陈皮）','健脾祛湿，调理肠胃');

/*Table structure for table `home` */

DROP TABLE IF EXISTS `home`;

CREATE TABLE `home` (
  `home_id` bigint NOT NULL AUTO_INCREMENT COMMENT '首页id',
  `home_photo` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '首页图片路径',
  `home_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '首页内容介绍',
  `home_title` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '首页内容标题',
  PRIMARY KEY (`home_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='首页表';

/*Data for the table `home` */

insert  into `home`(`home_id`,`home_photo`,`home_content`,`home_title`) values 
(1,'https://pic.imgdb.cn/item/64c626781ddac507cc836db6.png','是以君子知形恃神以立，神须形以存……故修性以保神，安心以全身……又呼吸吐纳，服食养身，使形神相亲，表里俱济也','《养生论》'),
(2,'https://pic.imgdb.cn/item/64c626891ddac507cc838bca.png','茶茗久服，令人有力、悦志。','《神农本草经》'),
(3,'https://pic.imgdb.cn/item/64c626a11ddac507cc8414b1.png','1.自有茯苓种植基地、原料原产地直供；\r\n2.特优级茶叶+大片20年陈皮+茯苓养生配料；\r\n3.自拥有工厂：不用碎料、小份烘干、六级手工严选，把控品质；\r\n4.小罐便捷、大片原材、料足够效、大气礼品装。','茯临寿特色'),
(4,'https://pic.imgdb.cn/item/64c626a41ddac507cc8418c9.png','①准备好茶具\r\n②洗茶：使用镊子夹取一块陈皮，打开小罐茯苓茶导入茶壶中，倒入热水后将过滤去茶材的水倒掉。\r\n③重新注水，待水开后放入所配茶叶，煎制3～5分钟后即可饮用。\r\n④第二遍及后续煎制只需2-3分钟即可饮用。茶材料实，可煎煮次数为12次以内口感保持较佳。','养生茶饮用方法'),
(5,'https://pic.imgdb.cn/item/64c626a41ddac507cc841936.png','茯临寿养生茶以腹部大、颈口小的陶瓷壶和玻璃壶陶壶耐高温的玻璃壶冲泡为佳，这类壶可以最大限度地留住花草的清香，切勿选择铁壶，因为茶材中的某些带中药成分容易与铁发生化学反应，降低效果。\r\n选择茶杯时，多选择能保温的陶瓷茶杯。茶杯多选用玻璃杯，这样花草茶的汤色在透明的杯中更显好看，增添饮茶的情趣。','茶具挑选'),
(6,'https://pic.imgdb.cn/item/64c626a41ddac507cc84199b.png','煎煮法、调制法、冲泡法\r\n1.煎煮法\r\n煎煮法也是一种较常用的养生茶制作方法。将茶材放入大茶壶或砂锅中，用火熬煮5~8分钟，第一次饮用后可反复煎煮，每次水开后2分钟即可饮用，饮用时滤去茶材，取茶汤饮用。以上所示6款茯苓临寿系列茶饮均适用于本法，可充分发挥茶材的养生效果。\r\n2.冲泡法\r\n冲泡法是最常见也最便捷的制作方法。只需将备好的茶材放入杯中，再用烧开的水冲泡，盖上锅盖焖几分钟，就可以饮用了。本法适用于具有止痛、止泻、明目、发汗等功效，且挥发性强的茶材，如茶叶、花草茶及部分中药材等。茯临寿六款茶饮中，茯苓清润茶和玫瑰女神茶可使用本法。\r\n3.调制法\r\n调制法适用于部分水果及需研磨成粉状服用的茶材。先将茶材泡好，等茶汤放凉后，再加入要泡饮的水果或茶末，这样可以避免水果及茶末的养分流失。','养生茶最常见的制作方法有三种');

/*Table structure for table `order` */

DROP TABLE IF EXISTS `order`;

CREATE TABLE `order` (
  `order_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '生成的订单编号',
  `order_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单描述：商品标题*数量',
  `order_prepay_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单的预支付交易会话标识',
  `order_price` int NOT NULL COMMENT '订单总价，单位分',
  `order_receipt_id` bigint NOT NULL COMMENT '订单收货地址id',
  `order_remarks` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单备注',
  `order_user_id` bigint NOT NULL COMMENT '订单所属用户id',
  `order_status` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '订单修改时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

/*Data for the table `order` */

/*Table structure for table `payment` */

DROP TABLE IF EXISTS `payment`;

CREATE TABLE `payment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
  `transaction_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '微信支付返回的支付id',
  `trade_type` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付类型',
  `trade_status` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付状态',
  `open_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付者标识',
  `payer_total` int NOT NULL COMMENT '用户支付金额，单位分',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '微信支付返回的全部信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '支付单创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '支付单更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

/*Data for the table `payment` */

/*Table structure for table `photo` */

DROP TABLE IF EXISTS `photo`;

CREATE TABLE `photo` (
  `photo_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品图片id',
  `photo_url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品图片url',
  `photo_goods_id` bigint NOT NULL COMMENT '商品图片所属商品id',
  PRIMARY KEY (`photo_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='商品图片表';

/*Data for the table `photo` */

insert  into `photo`(`photo_id`,`photo_url`,`photo_goods_id`) values 
(1,'https://pic.imgdb.cn/item/64c626781ddac507cc836db6.png',1),
(2,'https://pic.imgdb.cn/item/64c626781ddac507cc836db6.png',1),
(3,'https://pic.imgdb.cn/item/64c626781ddac507cc836db6.png',1),
(4,'https://pic.imgdb.cn/item/64c626781ddac507cc836db6.png',1),
(5,'https://pic.imgdb.cn/item/64c626781ddac507cc836db6.png',1),
(6,'https://pic.imgdb.cn/item/64c626891ddac507cc838bca.png',2),
(7,'https://pic.imgdb.cn/item/64c626891ddac507cc838bca.png',2),
(8,'https://pic.imgdb.cn/item/64c626891ddac507cc838bca.png',2),
(9,'https://pic.imgdb.cn/item/64c626891ddac507cc838bca.png',2),
(10,'https://pic.imgdb.cn/item/64c626891ddac507cc838bca.png',2),
(11,'https://pic.imgdb.cn/item/64c626a11ddac507cc8414b1.png',3),
(12,'https://pic.imgdb.cn/item/64c626a11ddac507cc8414b1.png',3),
(13,'https://pic.imgdb.cn/item/64c626a11ddac507cc8414b1.png',3),
(14,'https://pic.imgdb.cn/item/64c626a11ddac507cc8414b1.png',3),
(15,'https://pic.imgdb.cn/item/64c626a11ddac507cc8414b1.png',3),
(16,'https://pic.imgdb.cn/item/64c626a41ddac507cc8418c9.png',4),
(17,'https://pic.imgdb.cn/item/64c626a41ddac507cc8418c9.png',4),
(18,'https://pic.imgdb.cn/item/64c626a41ddac507cc8418c9.png',4),
(19,'https://pic.imgdb.cn/item/64c626a41ddac507cc8418c9.png',4),
(20,'https://pic.imgdb.cn/item/64c626a41ddac507cc8418c9.png',4),
(21,'https://pic.imgdb.cn/item/64c626a41ddac507cc841936.png',5),
(22,'https://pic.imgdb.cn/item/64c626a41ddac507cc841936.png',5),
(23,'https://pic.imgdb.cn/item/64c626a41ddac507cc841936.png',5),
(24,'https://pic.imgdb.cn/item/64c626a41ddac507cc841936.png',5),
(25,'https://pic.imgdb.cn/item/64c626a41ddac507cc841936.png',5),
(26,'https://pic.imgdb.cn/item/64c626a41ddac507cc84199b.png',6),
(27,'https://pic.imgdb.cn/item/64c626a41ddac507cc84199b.png',6),
(28,'https://pic.imgdb.cn/item/64c626a41ddac507cc84199b.png',6),
(29,'https://pic.imgdb.cn/item/64c626a41ddac507cc84199b.png',6),
(30,'https://pic.imgdb.cn/item/64c626a41ddac507cc84199b.png',6);

/*Table structure for table `receipt` */

DROP TABLE IF EXISTS `receipt`;

CREATE TABLE `receipt` (
  `receipt_id` bigint NOT NULL AUTO_INCREMENT COMMENT '收货信息id',
  `receipt_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货姓名',
  `receipt_address` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货地址',
  `receipt_phone` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货手机号',
  `receipt_user_id` bigint NOT NULL COMMENT '收货信息所属用户id',
  PRIMARY KEY (`receipt_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='收货信息表';

/*Data for the table `receipt` */

insert  into `receipt`(`receipt_id`,`receipt_name`,`receipt_address`,`receipt_phone`,`receipt_user_id`) values 
(1,'封心','江西省南昌市江西农业大学东区','1866562659595',1),
(2,'付总','江西农业大学','1515911611891',1),
(3,'鱼儿','南昌','1516149115155',1);

/*Table structure for table `shopping_car` */

DROP TABLE IF EXISTS `shopping_car`;

CREATE TABLE `shopping_car` (
  `shopping_car_id` bigint NOT NULL AUTO_INCREMENT COMMENT '购物车id（自增）',
  `goods_id` bigint DEFAULT NULL COMMENT '商品id',
  `goods_number` int DEFAULT NULL COMMENT '商品数量',
  `user_id` bigint DEFAULT NULL COMMENT '所属用户id',
  `create_time` datetime DEFAULT NULL COMMENT '加入时间',
  PRIMARY KEY (`shopping_car_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3;

/*Data for the table `shopping_car` */

insert  into `shopping_car`(`shopping_car_id`,`goods_id`,`goods_number`,`user_id`,`create_time`) values 
(18,1,4,2,'2023-08-02 16:42:40');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名',
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户昵称',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户密码',
  `gender` enum('男','女') CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '男' COMMENT '用户性别',
  `portrait` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '头像地址',
  `phone_number` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户手机号',
  `open_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户登录唯一标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

/*Data for the table `user` */

insert  into `user`(`id`,`user_name`,`nick_name`,`password`,`gender`,`portrait`,`phone_number`,`open_id`) values 
(2,'','微信用户','','','https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132','','ooP5i5Fgw8evSujcxrm9Ic54EBVI');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
