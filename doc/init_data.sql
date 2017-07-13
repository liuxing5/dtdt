USE `directionaldt`;

/*Data for the table `t_s_app` */

insert  into `t_s_app`(`app_id`,`app_key`,`secret`,`partner_id`,`app_name`,`state`,`is_need_charge`,`create_time`,`notice_url`) values (1,'9E739908-4D0F-41D9-B6DA-B61E34037109','E8312BE232#323%233&^%2F46464BF2',1,'xuebing_test',0,0,NULL,NULL),(1002,'22222','22222',10001,'app1',0,0,NULL,'http://120.52.120.162:51012/dtdt-rest/notice/test/partnerNotice'),(121322,'fwerh4356ytrt54','ewer5retyt',1234543245,'测试定向流量包',0,0,NULL,'http://120.52.120.162:51012/dtdt-rest/notice/test/partnerNotice');

/*Data for the table `t_s_partner` */

insert  into `t_s_partner`(`partner_id`,`partner_code`,`partner_name`,`state`,`contacts`,`telephone`,`mobilephone`,`address`,`create_time`,`update_time`) values (1,'asia-xian','王学兵',0,'王学兵','','18909224890','西安','2017-07-11 16:33:00','2017-07-11 16:33:05');

/*Data for the table `t_s_partner_order_resources` */

insert  into `t_s_partner_order_resources`(`batch_id`,`partner_code`,`pre_count`,`use_count`,`charge_count`,`create_user`,`create_time`,`end_time`) values ('1','asia-xian',1000,0,0,NULL,'2017-07-11 16:40:37','2017-07-31 16:40:41');

/*Data for the table `t_s_partner_sms_config` */

insert  into `t_s_partner_sms_config`(`id`,`partner_code`,`contacts`,`mobilephone`,`warn_threshold`,`mail`,`create_user`,`create_time`,`end_time`) values ('1','asia-xian',NULL,'18610728340',0,NULL,NULL,'2017-07-11 16:41:18',NULL);

/*Data for the table `t_s_product_price` */

insert  into `t_s_product_price`(`ID`,`product_id`,`partner_id`,`app_id`,`partner_price`,`app_price`,`state`,`create_time`,`update_time`,`area_code`,`area_name`) values (1,1,1,1,0,6,0,NULL,NULL,NULL,NULL),(2,3,1,1,0,0,0,NULL,NULL,NULL,NULL),(3,5,1,1,0,30,0,NULL,NULL,NULL,NULL),(4,6,1,1,0,50,0,NULL,NULL,NULL,NULL);

