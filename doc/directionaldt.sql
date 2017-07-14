﻿/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/7/12 12:31:47                           */
/*==============================================================*/

DROP database directionaldt;
CREATE DATABASE IF NOT EXISTS directionaldt DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
use directionaldt;

drop table if exists t_s_app;

drop table if exists t_s_batch_order;

drop table if exists t_s_charge;

drop table if exists t_s_his_charge;

drop table if exists t_s_his_order;

drop table if exists t_s_his_order_record;

drop table if exists t_s_his_payorder;

drop table if exists t_s_order;

drop table if exists t_s_order_record;

drop table if exists t_s_param;

drop table if exists t_s_partner;

drop table if exists t_s_partner_his_order_resources;

drop table if exists t_s_partner_order_resources;

drop table if exists t_s_partner_sms_config;

drop table if exists t_s_payorder;

drop table if exists t_s_product;

drop table if exists t_s_product_price;

drop table if exists t_s_vcode;

drop table if exists t_s_woplat_order;

/*==============================================================*/
/* Table: t_s_app                                               */
/*==============================================================*/
create table t_s_app
(
   app_id               bigint(20) not null comment '应用ID',
   app_key              varchar(36) not null comment '应用key',
   secret               varchar(32) comment '秘钥',
   partner_id           bigint(20) comment '合作方ID',
   app_name             varchar(50) comment '应用名称',
   state                tinyint(4) comment '状态（有效：0，失效：1）',
   is_need_charge       tinyint(4) comment '是否需要返充话费（0：需要 1：不需要）',
   create_time          timestamp comment '创建时间',
   notice_url           varchar(100) comment '通知回调地址',
   primary key (app_id)
);

/*==============================================================*/
/* Table: t_s_batch_order                                       */
/*==============================================================*/
create table t_s_batch_order
(
   batch_id             varchar(32) not null comment '批量订单ID',
   partner_code         varchar(12) comment '合作方编码',
   app_key              varchar(36) comment '合作伙伴产品',
   partner_order_id     varchar(36) comment '合作伙伴订单ID',
   product_code         varchar(6),
   oper_type            tinyint(4) default 0 comment '订购：1，退订：2',
   state                varchar(2) comment '初始状态0 处理结束1',
   create_time          timestamp comment '订购时间',
   update_time          timestamp null comment '更新时间',
   price                int(11) comment '订购产品价格',
   redirect_url         varchar(100) comment '支付成功跳转Url',
   mobilephones         varchar(1000) comment '批量开的手机号码',
   mobilephones_count   int(11) comment '手机号码个数',
   remark               varchar(100),
   primary key (batch_id)
);

alter table t_s_batch_order comment '批量订单表';

/*==============================================================*/
/* Table: t_s_charge                                            */
/*==============================================================*/
create table t_s_charge
(
   charge_id            varchar(32) not null comment '充值ID',
   order_id             varchar(32),
   recharge_parent_id   varchar(32) comment '充值拆分父ID',
   recharge_phone_num   varchar(11) comment '充值手机号',
   recharge_money       int(11) comment '充值金额',
   rechage_time         timestamp  comment '充值时间',
   rechage_num          int(11) comment '充值次数',
   state                varchar(2) comment '状态（1：充值中 2：成功 3：失败 4：超时）',
   result               varchar(500) comment '结果',
   return_time          timestamp null comment '返回时间',
   charge_sys_username  varchar(20) comment '企业用户名',
   charge_sys_pwd       varchar(100) comment '企业用户密码',
   remark               varchar(500) comment '备注',
   primary key (charge_id)
);

/*==============================================================*/
/* Table: t_s_his_charge                                        */
/*==============================================================*/
create table t_s_his_charge
(
   charge_id            varchar(32) comment '充值ID',
   order_id             varchar(32),
   recharge_parent_id   varchar(32) comment '充值拆分父ID',
   recharge_phone_num   varchar(11) comment '充值手机号',
   recharge_money       int(11) comment '充值金额',
   copy_time            timestamp comment '移入时间',
   rechage_time         timestamp null comment '充值时间',
   rechage_num          int(11) comment '充值次数',
   state                varchar(2) comment '状态（1：充值中 2：成功 3：失败 4：超时）',
   result               varchar(500) comment '结果',
   return_time          timestamp null comment '返回时间',
   charge_sys_username  varchar(20) comment '企业用户名',
   charge_sys_pwd       varchar(100) comment '企业用户密码',
   remark               varchar(500) comment '备注',
   copy_remark          varchar(500)
);

alter table t_s_his_charge comment '返充话费历史记录表（按月迁移）';

/*==============================================================*/
/* Table: t_s_his_order                                         */
/*==============================================================*/
create table t_s_his_order
(
   order_id             varchar(32) not null,
   wo_order_id          varchar(36) comment '沃家总管返回的ID',
   partner_code         varchar(12) comment '合作方编码',
   app_key              varchar(36) comment '合作伙伴产品ID',
   partner_order_id     varchar(36) comment '合作伙伴订单ID',
   product_code         varchar(6),
   oper_type            tinyint(4) default 0 comment '订购：1，退订：2',
   refund_order_id      varchar(32) comment 'oper_type未1时，必须有',
   is_real_request_woplat tinyint(4) comment '是否真实请求沃家总管（0：真实请求 1：未请求）
            如果我方同一手机号码，在多个app下订购了同一流量产品，
            则1、只有第一次订购会像沃家总管发起订购请求；
            2、只有最后一个退订时，才能真实像沃家总管发起退订请求；',
   state                varchar(2),
   mobilephone          varchar(11) comment '订购手机号码',
   order_channel        varchar(8) comment '订购渠道（APP、WEB、FILE：文件接口、Others：其他）',
   copy_time            timestamp comment '入表时间',
   create_time          timestamp null comment '订购时间',
   update_time          timestamp null comment '更新时间',
   valid_time           timestamp null comment '有效时间',
   invalid_time         timestamp null comment '失效时间',
   price                int(11) comment '订购产品价格',
   count                int(11) default 1 comment '订购数量',
   money                bigint(20) comment '订单价格',
   is_need_charge       tinyint(4) comment '是否需要返充话费（0：需要 1：不需要）',
   allow_auto_pay       tinyint(4) comment '每月固定时间：0，一次性：1',
   redirect_url         varchar(100),
   remark               varchar(500),
   copy_type            tinyint(4) default 0 comment '入表方式（0：订单完工 1：未支付失效 2：人工操作）',
   copy_remark          varchar(500),
   primary key (order_id)
);

alter table t_s_his_order comment '订单历史数据表（按月迁移）';

/*==============================================================*/
/* Table: t_s_his_order_record                                  */
/*==============================================================*/
create table t_s_his_order_record
(
   order_id             varchar(32) not null,
   wo_order_id          varchar(36),
   parent_order_id      varchar(32) comment '父订单ID，当半年包，或者年包拆分时出现此字段，此时该记录未本系统内不生成，
            非用户请求生成',
   partner_code         varchar(12) comment '合作方编码',
   app_key              varchar(36) comment '合作伙伴产品ID',
   partner_order_id     varchar(36) comment '合作伙伴订单ID',
   cycle_type           tinyint(4) default 0 comment '包月：0，包半年：1，包年：2',
   product_code         varchar(6) comment '定向流量产品编码',
   state                varchar(2),
   mobilephone          varchar(11) comment '订购手机号码',
   order_channel        varchar(8) comment '订购渠道（APP、WEB、FILE：文件接口、Others：其他）',
   price                int(11) comment '订购产品价格',
   count                int(11) default 1 comment '订购数量',
   money                bigint(20) comment '订单价格',
   is_need_charge       tinyint(4) comment '是否需要返充话费（0：需要 1：不需要）',
   oper_source          tinyint(4) default 0 comment '操作来源（0：定向流量平台内 1：沃家总管同步 ）',
   allow_auto_pay       tinyint(4) default 0 comment '0：允许 1：不允许',
   redirect_url         varchar(100),
   wo_order             tinyint(4) default 0 comment '0：我方初始化订购 
            1：其他代理商订购 
            2：其他代理商订购失效、到期或退订由我方续订',
   remark               varchar(500),
   copy_time            timestamp comment '入表时间',
   refund_valid_time    timestamp null comment '退订生效时间',
   refund_time          timestamp null comment '退订时间',
   valid_time           timestamp null comment '有效时间',
   invalid_time         timestamp null comment '失效时间',
   update_time          timestamp null comment '更新时间',
   create_time          timestamp null comment '订购时间',
   copy_type            tinyint(4) default 0 comment '入表方式（0：包月退订 1：包半年、包年到期失效 2：人工操作）',
   copy_remark          varchar(500),
   primary key (order_id)
);

alter table t_s_his_order_record comment '产品订购关系历史数据记录表,退订的包月订购，失效的包半年、包年产品订购关系';

/*==============================================================*/
/* Table: t_s_his_payorder                                      */
/*==============================================================*/
create table t_s_his_payorder
(
   pay_id               varchar(32) not null comment '支付ID',
   order_id             varchar(32) comment '订单ID',
   pay_account          varchar(32) comment '支付账户ID',
   pay_money            bigint(20) comment '支付钱数',
   pay_type             varchar(3) comment '微信支付：WCP，支付宝支付：ALP',
   oper_type            tinyint(4) comment '支付：1  ，退款：2',
   state                varchar(2),
   copy_time            timestamp comment '移入时间',
   origin_refound_money bigint(20) comment '原路退费金额',
   origin_refound_time  timestamp null comment '原路退费时间',
   man_made_refound_time timestamp null comment '人工退费时间',
   man_made_refound_type varchar(3) comment '人工退费方式（支付宝：ALP，微信WCP，通讯账户：CTP，其他：OTP）',
   account_day          timestamp null comment '账期',
   pre_pay_id           varchar(128) comment '预支付ID',
   third_pay_id         varchar(128) comment '第三方支付ID',
   update_time          timestamp null comment '更新时间',
   create_time          timestamp null comment '支付时间',
   copy_remark          varchar(500),
   primary key (pay_id)
);

alter table t_s_his_payorder comment '支付历史订单表（按月迁移）';

/*==============================================================*/
/* Table: t_s_order                                             */
/*==============================================================*/
create table t_s_order
(
   order_id             varchar(32) not null,
   wo_order_id          varchar(36) comment '沃家总管返回的ID',
   partner_code         varchar(12) comment '合作方编码',
   app_key              varchar(36) comment '合作伙伴产品ID',
   partner_order_id     varchar(36) comment '合作伙伴订单ID',
   product_code         varchar(6),
   oper_type            tinyint(4) default 0 comment '订购：1，退订：2',
   refund_order_id      varchar(32) comment 'oper_type未1时，必须有',
   is_real_request_woplat tinyint(4) comment '是否真实请求沃家总管（0：真实请求 1：未请求）
            如果我方同一手机号码，在多个app下订购了同一流量产品，
            则1、只有第一次订购会像沃家总管发起订购请求；
            2、只有最后一个退订时，才能真实像沃家总管发起退订请求；',
   state                varchar(2),
   mobilephone          varchar(11) comment '订购手机号码',
   order_channel        varchar(8) comment '订购渠道（APP、WEB、FILE：文件接口、Others：其他）',
   create_time          timestamp comment '订购时间',
   update_time          timestamp  null comment '更新时间',
   valid_time           timestamp  null comment '有效时间',
   invalid_time         timestamp  null comment '失效时间',
   price                int(11) comment '订购产品价格',
   count                int(11) default 1 comment '订购数量',
   money                bigint(20) comment '订单价格',
   is_need_charge       tinyint(4) comment '是否需要返充话费（0：需要 1：不需要）',
   allow_auto_pay       tinyint(4) default 1 comment '每月固定时间：0，一次性：1',
   redirect_url         varchar(100) comment '支付成功跳转Url',
   remark               varchar(500),
   primary key (order_id)
);

alter table t_s_order comment '执行流程中的订单记录表';

/*==============================================================*/
/* Table: t_s_order_record                                      */
/*==============================================================*/
create table t_s_order_record
(
   order_id             varchar(32) not null,
   wo_order_id          varchar(36),
   parent_order_id      varchar(32) comment '父订单ID，当半年包，或者年包拆分时出现此字段，此时该记录未本系统内不生成，
            非用户请求生成',
   partner_code         varchar(12) comment '合作方编码',
   app_key              varchar(36) comment '合作伙伴产品ID',
   partner_order_id     varchar(36) comment '合作伙伴订单ID',
   cycle_type           tinyint(4) default 0 comment '包月：0，包半年：1，包年：2',
   product_code         varchar(6) comment '定向流量产品编码',
   state                varchar(2),
   mobilephone          varchar(11) comment '订购手机号码',
   order_channel        varchar(8) comment '订购渠道（APP、WEB、FILE：文件接口、Others：其他）',
   price                int(11) comment '订购产品价格',
   count                int(11) default 1 comment '订购数量',
   money                bigint(20) comment '订单价格',
   is_need_charge       tinyint(4) comment '是否需要返充话费（0：需要 1：不需要）',
   oper_source          tinyint(4) default 0 comment '操作来源（0：定向流量平台内 1：沃家总管同步 ）',
   allow_auto_pay       tinyint(4) default 1 comment '0：允许 1：不允许',
   redirect_url         varchar(100),
   wo_order             tinyint default 0 comment '0：我方初始化订购 
            1：其他代理商订购 
            2：其他代理商订购失效、到期或退订由我方续订',
   remark               varchar(500),
   create_time          timestamp comment '订购时间',
   refund_valid_time    timestamp null comment '退订生效时间',
   refund_time          timestamp null comment '退订时间',
   valid_time           timestamp null comment '有效时间',
   invalid_time         timestamp null comment '失效时间',
   update_time          timestamp null comment '更新时间',
   primary key (order_id)
);

alter table t_s_order_record comment '有效的产品订购关系';

/*==============================================================*/
/* Table: t_s_param                                             */
/*==============================================================*/
create table t_s_param
(
   param_code           varchar(50) comment '参数编码',
   param_value          varchar(50) comment '参数值',
   parent_code          varchar(50) comment '父参数编码',
   status               int(1) comment '状态 1：可用',
   create_time          timestamp comment '创建时间',
   remark               varchar(100) comment '备注'
);

/*==============================================================*/
/* Table: t_s_partner                                           */
/*==============================================================*/
create table t_s_partner
(
   partner_id           bigint(20) not null comment '合作方ID',
   partner_code         varchar(12) not null comment '合作方编码',
   partner_name         varchar(100) comment '合作方名称',
   state                tinyint(4) default 0 comment '状态（0：有效， 1失效）',
   contacts             varchar(20) comment '联系人',
   telephone            varchar(13) comment '固定电话',
   mobilephone          varchar(11) comment '移动电话',
   address              varchar(100) comment '地址',
   create_time          timestamp comment '创建时间',
   update_time          timestamp null comment '更新时间',
   primary key (partner_id)
);

/*==============================================================*/
/* Table: t_s_partner_his_order_resources                       */
/*==============================================================*/
create table t_s_partner_his_order_resources
(
   batch_id             varchar(32) comment '资源批次ID',
   partner_code         varchar(12) comment '合作方编码',
   pre_count            bigint(20) default 0 comment '预存订购次数',
   use_count            bigint(20) comment '失效后，使用了的次数',
   charge_count         bigint(20) comment '充值次数',
   create_time          timestamp comment '创建时间',
   end_time             timestamp null comment '结束时间',
   create_user          varchar(50) comment '创建者'
);

alter table t_s_partner_his_order_resources comment '合作方订购资源批次历史记录表';

/*==============================================================*/
/* Table: t_s_partner_order_resources                           */
/*==============================================================*/
create table t_s_partner_order_resources
(
   batch_id             varchar(32) not null comment '资源批次ID',
   partner_code         varchar(12) comment '合作方编码',
   pre_count            bigint(20) default 0 comment '预存订购次数',
   use_count            bigint(20) default 0 comment '失效后，使用了的次数',
   charge_count         bigint(20) default 0 comment '充值次数',
   create_time          timestamp comment '创建时间',
   end_time             timestamp null comment '结束时间',
   create_user          varchar(50) comment '创建者',
   primary key (batch_id)
);

alter table t_s_partner_order_resources comment '合作方订购资源批次记录表';

/*==============================================================*/
/* Table: t_s_partner_sms_config                                */
/*==============================================================*/
create table t_s_partner_sms_config
(
   id                   varchar(32) not null,
   partner_code         varchar(12) comment '合作方编码',
   contacts             varchar(20) comment '预存订购次数',
   warn_threshold       bigint(20) default 0 comment '告警阈值',
   mobilephone          varchar(200) comment '手机号码，多个用英文逗号间隔（,）',
   mail                 varchar(500) comment '邮箱，多个用英文逗号间隔（,）',
   create_user          varchar(50) comment '创建者',
   create_time          timestamp comment '创建时间',
   end_time             timestamp null comment '结束时间',
   primary key (id)
);

alter table t_s_partner_sms_config comment '合作方短信通知配置表';

/*==============================================================*/
/* Table: t_s_payorder                                          */
/*==============================================================*/
create table t_s_payorder
(
   pay_id               varchar(32) not null comment '支付ID',
   order_id             varchar(32) comment '订单ID',
   pay_account          varchar(32) comment '支付账户ID',
   pay_money            bigint(20) comment '支付钱数',
   pay_type             varchar(3) comment '微信支付：WCP，支付宝支付：ALP',
   oper_type            tinyint(4) comment '支付：1  ，退款：2',
   state                varchar(2),
   origin_refound_money bigint(20) comment '原路退费金额',
   create_time          timestamp comment '支付时间',
   origin_refound_time  timestamp null comment '原路退费时间',
   man_made_refound_time timestamp null comment '人工退费时间',
   man_made_refound_type varchar(3) comment '人工退费方式（支付宝：ALP，微信WCP，通讯账户：CTP，其他：OTP）',
   account_day          timestamp null comment '账期',
   pre_pay_id           varchar(128) comment '预支付ID',
   third_pay_id         varchar(128) comment '第三方支付ID',
   update_time          timestamp null ,
   primary key (pay_id)
);

/*==============================================================*/
/* Table: t_s_product                                           */
/*==============================================================*/
create table t_s_product
(
   ID                   bigint(20) not null,
   product_code         varchar(6) not null,
   wo_product_code      varchar(36),
   product_name         varchar(100),
   price                int(11) default 0,
   wo_product_price     int(11),
   state                tinyint(4) default 0 comment '有效：0，失效：1',
   cycle_type           tinyint(4) default 0 comment '包月：0，包半年：1，包年：2',
   type                 tinyint(4) default 0 comment '向前收费：0， 向后收费：1',
   can_unsubscribe      tinyint(4) default 0 comment '允许：0，不允许： 1',
   area_code            varchar(6) comment '地区编码',
   area_name            varchar(100) comment '地区名称',
   create_time          timestamp,
   update_time          timestamp null ,
   primary key (ID)
);

alter table t_s_product comment '流量产品定义表';

/*==============================================================*/
/* Table: t_s_product_price                                     */
/*==============================================================*/
create table t_s_product_price
(
   ID                   bigint(20) not null,
   product_id           bigint(20) not null comment '产品ID',
   partner_id           bigint(20) comment '合作方ID',
   app_id               bigint(20) comment '应用ID',
   partner_price        int(11) default 0 comment '产品针对合作方的价格',
   app_price            int(11) comment '产品针对应用的价格',
   state                tinyint(4) default 0 comment '有效：0，失效：1',
   create_time          timestamp,
   update_time          timestamp null ,
   area_code            varchar(6) comment '地区编码',
   area_name            varchar(100) comment '地区名称',
   primary key (ID)
);

alter table t_s_product_price comment '流量产品价格定义表';

/*==============================================================*/
/* Table: t_s_vcode                                             */
/*==============================================================*/
create table t_s_vcode
(
   ID                   bigint(20) not null AUTO_INCREMENT,
   order_id             varchar(32) comment '订单ID',
   vcode_send_time      varchar(20) comment '最后一次验证码下发时间',
   lvcode               varchar(6) comment '下发的验证码',
   user_input_vcode     varchar(6) comment '用户输入验证码内容',
   user_input_time      varchar(20) comment '验证码填写时间 ',
   vcode_valid_resut    varchar(4) comment '校验结果',
   primary key (ID)
);

alter table t_s_vcode comment '用户最后一次验证法信息，按月迁移';

/*==============================================================*/
/* Table: t_s_woplat_order                                      */
/*==============================================================*/
create table t_s_woplat_order
(
   order_id             varchar(32) not null,
   wo_order_id          varchar(36) comment '沃家总管返回的ID',
   partner_code         varchar(12) comment '合作方编码',
   app_key              varchar(36) comment '合作伙伴产品ID',
   partner_order_id     varchar(36) comment '合作伙伴订单ID',
   product_code         varchar(6),
   state                varchar(2),
   mobilephone          varchar(11) comment '订购手机号码',
   order_channel        varchar(8) comment '订购渠道（APP、WEB、FILE：文件接口、Others：其他）',
   create_time          timestamp comment '订购时间',
   update_time          timestamp null comment '更新时间',
   valid_time           timestamp null comment '有效时间',
   invalid_time         timestamp null comment '失效时间',
   price                int(11) comment '订购产品价格',
   count                int(11) default 1 comment '订购数量',
   money                bigint(20) comment '订单价格',
   is_need_charge       tinyint(4) comment '是否需要返充话费（0：需要 1：不需要）',
   charge_type          tinyint(4) comment '每月固定时间：0，一次性：1',
   remark               varchar(500),
   primary key (order_id)
);

alter table t_s_woplat_order comment '沃家总管同步过来的出自己以外的订购关系';

alter table t_s_app add constraint FK_Reference_1 foreign key (partner_id)
      references t_s_partner (partner_id) on delete restrict on update restrict;

