/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/7/5 17:23:41                            */
/*==============================================================*/

DROP database directionaldt;
CREATE DATABASE IF NOT EXISTS directionaldt DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
use directionaldt;

drop table if exists t_s_app;

drop table if exists t_s_charge;

drop table if exists t_s_his_charge;

drop table if exists t_s_his_order;

drop table if exists t_s_his_order_record;

drop table if exists t_s_his_payorder;

drop table if exists t_s_order;

drop table if exists t_s_order_record;

drop table if exists t_s_param;

drop table if exists t_s_partner;

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
   app_id               bigint not null comment 'Ӧ��ID',
   app_key              varchar(36) not null comment 'Ӧ��key',
   secret               varchar(32) comment '��Կ',
   partner_id           bigint comment '������ID',
   app_name             varchar(50) comment 'Ӧ������',
   state                tinyint comment '״̬����Ч��0��ʧЧ��1��',
   is_need_charge       tinyint comment '�Ƿ���Ҫ���仰�ѣ�0����Ҫ 1������Ҫ��',
   create_time          timestamp comment '����ʱ��',
   notice_url           varchar(100) comment '֪ͨ�ص���ַ',
   primary key (app_id)
);

/*==============================================================*/
/* Table: t_s_charge                                            */
/*==============================================================*/
create table t_s_charge
(
   charge_id            varchar(22) not null comment '��ֵID',
   order_id             varchar(22),
   recharge_parent_id   varchar(22) comment '��ֵ��ָ�ID',
   recharge_phone_num   varchar(11) comment '��ֵ�ֻ���',
   recharge_money       int(11) comment '��ֵ���',
   rechage_time         timestamp comment '��ֵʱ��',
   rechage_num          int comment '��ֵ����',
   state                varchar(2) comment '״̬��1����ֵ�� 2���ɹ� 3��ʧ�� 4����ʱ��',
   result               varchar(500) comment '���',
   return_time          timestamp comment '����ʱ��',
   charge_sys_username  varchar(20) comment '��ҵ�û���',
   charge_sys_pwd       varchar(100) comment '��ҵ�û�����',
   remark               varchar(500) comment '��ע',
   primary key (charge_id)
);

/*==============================================================*/
/* Table: t_s_his_charge                                        */
/*==============================================================*/
create table t_s_his_charge
(
   charge_id            varchar(22) comment '��ֵID',
   order_id             varchar(22),
   recharge_parent_id   varchar(22) comment '��ֵ��ָ�ID',
   recharge_phone_num   varchar(11) comment '��ֵ�ֻ���',
   recharge_money       int(11) comment '��ֵ���',
   rechage_time         timestamp comment '��ֵʱ��',
   rechage_num          int comment '��ֵ����',
   state                varchar(2) comment '״̬��1����ֵ�� 2���ɹ� 3��ʧ�� 4����ʱ��',
   result               varchar(500) comment '���',
   return_time          timestamp comment '����ʱ��',
   charge_sys_username  varchar(20) comment '��ҵ�û���',
   charge_sys_pwd       varchar(100) comment '��ҵ�û�����',
   remark               varchar(500) comment '��ע',
   copy_time            timestamp comment '����ʱ��',
   copy_remark          varchar(100)
);

alter table t_s_his_charge comment '���仰����ʷ��¼������Ǩ�ƣ�';

/*==============================================================*/
/* Table: t_s_his_order                                         */
/*==============================================================*/
create table t_s_his_order
(
   order_id             varchar(22) not null,
   wo_order_id          varchar(36) comment '�ּ��ܹܷ��ص�ID',
   partner_code         varchar(12) comment '����������',
   app_key              varchar(36) comment '��������ƷID',
   partner_order_id     varchar(36) comment '������鶩��ID',
   product_code         varchar(6),
   oper_type            tinyint default 0 comment '������1���˶���2',
   refund_order_id      varchar(22) comment 'oper_typeδ1ʱ��������',
   is_real_request_woplat tinyint comment '�Ƿ���ʵ�����ּ��ܹܣ�0����ʵ���� 1��δ����
            ����ҷ�ͬһ�ֻ����룬�ڶ��app�¶�����ͬһ������Ʒ��
            ��1��ֻ�е�һ�ζ��������ּ��ܹܷ��𶩹�����
            2��ֻ�����һ���˶�ʱ��������ʵ���ּ��ܹܷ����˶�����',
   state                varchar(2),
   mobilephone          varchar(11) comment '�����ֻ�����',
   order_channel        varchar(8) comment '����������APP��WEB��FILE���ļ��ӿڡ�Others��������',
   create_time          timestamp comment '����ʱ��',
   update_time          timestamp comment '����ʱ��',
   valid_time           timestamp comment '��Чʱ��',
   invalid_time         timestamp comment 'ʧЧʱ��',
   price                int comment '������Ʒ�۸�',
   count                int default 1 comment '��������',
   money                bigint comment '�����۸�',
   is_need_charge       tinyint comment '�Ƿ���Ҫ���仰�ѣ�0����Ҫ 1������Ҫ��',
   allow_auto_pay       tinyint comment 'ÿ�¹̶�ʱ�䣺0��һ���ԣ�1',
   redirect_url         varchar(100),
   remark               varchar(100),
   copy_time2           timestamp comment '���ʱ��',
   copy_type            tinyint default 0 comment '���ʽ��0�������깤 1��δ֧��ʧЧ 2���˹�������',
   copy_remark          varchar(100),
   primary key (order_id)
);

alter table t_s_his_order comment '������ʷ���ݱ�����Ǩ�ƣ�';

/*==============================================================*/
/* Table: t_s_his_order_record                                  */
/*==============================================================*/
create table t_s_his_order_record
(
   order_id             varchar(22) not null,
   wo_order_id          varchar(22),
   parent_order_id      varchar(22) comment '������ID���������������������ʱ���ִ��ֶΣ���ʱ�ü�¼δ��ϵͳ�ڲ����ɣ�
            ���û���������',
   partner_code         varchar(12) comment '����������',
   app_key              varchar(36) comment '��������ƷID',
   partner_order_id     varchar(36) comment '������鶩��ID',
   cycle_type2          tinyint default 0 comment '���£�0�������꣺1�����꣺2',
   product_code         varchar(6) comment '����������Ʒ����',
   state                varchar(2),
   mobilephone          varchar(11) comment '�����ֻ�����',
   order_channel        varchar(8) comment '����������APP��WEB��FILE���ļ��ӿڡ�Others��������',
   price                int comment '������Ʒ�۸�',
   count                int default 1 comment '��������',
   money                bigint comment '�����۸�',
   is_need_charge       tinyint comment '�Ƿ���Ҫ���仰�ѣ�0����Ҫ 1������Ҫ��',
   oper_source          tinyint default 0 comment '������Դ��0����������ƽ̨�� 1���ּ��ܹ�ͬ�� ��',
   allow_auto_pay       tinyint default 0 comment '0������ 1��������',
   redirect_url         varchar(100),
   wo_order             tinyint default 0 comment '0���ҷ���ʼ������ 
            1�����������̶��� 
            2�����������̶���ʧЧ�����ڻ��˶����ҷ�����',
   remark               varchar(100),
   refund_valid_time    timestamp comment '�˶���Чʱ��',
   refund_time          timestamp comment '�˶�ʱ��',
   valid_time           timestamp comment '��Чʱ��',
   invalid_time         timestamp comment 'ʧЧʱ��',
   update_time          timestamp comment '����ʱ��',
   create_time          timestamp comment '����ʱ��',
   copy_time            timestamp comment '���ʱ��',
   copy_type            tinyint default 0 comment '���ʽ��0�������˶� 1�������ꡢ���굽��ʧЧ 2���˹�������',
   copy_remark          varchar(100),
   primary key (order_id)
);

alter table t_s_his_order_record comment '��Ʒ������ϵ��ʷ���ݼ�¼��,�˶��İ��¶�����ʧЧ�İ����ꡢ�����Ʒ������ϵ';

/*==============================================================*/
/* Table: t_s_his_payorder                                      */
/*==============================================================*/
create table t_s_his_payorder
(
   pay_id               varchar(22) not null comment '֧��ID',
   order_id             varchar(22) comment '����ID',
   pay_account          varchar(32) comment '֧���˻�ID',
   pay_money            bigint comment '֧��Ǯ��',
   pay_type             varchar(3) comment '΢��֧����WCP��֧����֧����ALP',
   oper_type            tinyint comment '֧����1  ���˿2',
   state                varchar(2),
   origin_refound_money bigint comment 'ԭ·�˷ѽ��',
   origin_refound_time  timestamp comment 'ԭ·�˷�ʱ��',
   man_made_refound_time timestamp comment '�˹��˷�ʱ��',
   man_made_refound_type varchar(3) comment '�˹��˷ѷ�ʽ��֧������ALP��΢��WCP��ͨѶ�˻���CTP��������OTP��',
   account_day          timestamp comment '����',
   pre_pay_id           varchar(128) comment 'Ԥ֧��ID',
   third_pay_id         varchar(128) comment '������֧��ID',
   update_time          timestamp comment '����ʱ��',
   create_time          timestamp comment '֧��ʱ��',
   copy_time            timestamp comment '����ʱ��',
   copy_remark          varchar(100),
   primary key (pay_id)
);

alter table t_s_his_payorder comment '֧����ʷ����������Ǩ�ƣ�';

/*==============================================================*/
/* Table: t_s_order                                             */
/*==============================================================*/
create table t_s_order
(
   order_id             varchar(22) not null,
   wo_order_id          varchar(36) comment '�ּ��ܹܷ��ص�ID',
   partner_code         varchar(12) comment '����������',
   app_key              varchar(36) comment '��������ƷID',
   partner_order_id     varchar(36) comment '������鶩��ID',
   product_code         varchar(6),
   oper_type            tinyint default 0 comment '������1���˶���2',
   refund_order_id      varchar(22) comment 'oper_typeδ1ʱ��������',
   is_real_request_woplat tinyint comment '�Ƿ���ʵ�����ּ��ܹܣ�0����ʵ���� 1��δ����
            ����ҷ�ͬһ�ֻ����룬�ڶ��app�¶�����ͬһ������Ʒ��
            ��1��ֻ�е�һ�ζ��������ּ��ܹܷ��𶩹�����
            2��ֻ�����һ���˶�ʱ��������ʵ���ּ��ܹܷ����˶�����',
   state                varchar(2),
   mobilephone          varchar(11) comment '�����ֻ�����',
   order_channel        varchar(8) comment '����������APP��WEB��FILE���ļ��ӿڡ�Others��������',
   create_time          timestamp comment '����ʱ��',
   update_time          timestamp comment '����ʱ��',
   valid_time           timestamp comment '��Чʱ��',
   invalid_time         timestamp comment 'ʧЧʱ��',
   price                int comment '������Ʒ�۸�',
   count                int default 1 comment '��������',
   money                bigint comment '�����۸�',
   is_need_charge       tinyint comment '�Ƿ���Ҫ���仰�ѣ�0����Ҫ 1������Ҫ��',
   allow_auto_pay       tinyint comment 'ÿ�¹̶�ʱ�䣺0��һ���ԣ�1',
   redirect_url         varchar(100) comment '֧���ɹ���תUrl',
   remark               varchar(100),
   primary key (order_id)
);

alter table t_s_order comment 'ִ�������еĶ�����¼��';

/*==============================================================*/
/* Table: t_s_order_record                                      */
/*==============================================================*/
create table t_s_order_record
(
   order_id             varchar(22) not null,
   wo_order_id          varchar(22),
   parent_order_id      varchar(22) comment '������ID���������������������ʱ���ִ��ֶΣ���ʱ�ü�¼δ��ϵͳ�ڲ����ɣ�
            ���û���������',
   partner_code         varchar(12) comment '����������',
   app_key              varchar(36) comment '��������ƷID',
   partner_order_id     varchar(36) comment '������鶩��ID',
   cycle_type2          tinyint default 0 comment '���£�0�������꣺1�����꣺2',
   product_code         varchar(6) comment '����������Ʒ����',
   state                varchar(2),
   mobilephone          varchar(11) comment '�����ֻ�����',
   order_channel        varchar(8) comment '����������APP��WEB��FILE���ļ��ӿڡ�Others��������',
   price                int comment '������Ʒ�۸�',
   count                int default 1 comment '��������',
   money                bigint comment '�����۸�',
   is_need_charge       tinyint comment '�Ƿ���Ҫ���仰�ѣ�0����Ҫ 1������Ҫ��',
   oper_source          tinyint default 0 comment '������Դ��0����������ƽ̨�� 1���ּ��ܹ�ͬ�� ��',
   allow_auto_pay       tinyint default 0 comment '0������ 1��������',
   redirect_url         varchar(100),
   wo_order             tinyint default 0 comment '0���ҷ���ʼ������ 
            1�����������̶��� 
            2�����������̶���ʧЧ�����ڻ��˶����ҷ�����',
   remark               varchar(100),
   refund_valid_time    timestamp comment '�˶���Чʱ��',
   refund_time          timestamp comment '�˶�ʱ��',
   valid_time           timestamp comment '��Чʱ��',
   invalid_time         timestamp comment 'ʧЧʱ��',
   update_time          timestamp comment '����ʱ��',
   create_time          timestamp comment '����ʱ��',
   primary key (order_id)
);

alter table t_s_order_record comment '��Ч�Ĳ�Ʒ������ϵ';

/*==============================================================*/
/* Table: t_s_param                                             */
/*==============================================================*/
create table t_s_param
(
   param_code           varchar(50) comment '��������',
   param_value          varchar(50) comment '����ֵ',
   parent_code          varchar(50) comment '����������',
   status               int(1) comment '״̬ 1������',
   create_time          timestamp comment '����ʱ��',
   remark               varchar(100) comment '��ע'
);

/*==============================================================*/
/* Table: t_s_partner                                           */
/*==============================================================*/
create table t_s_partner
(
   partner_id           bigint not null comment '������ID',
   partner_code         varchar(12) not null comment '����������',
   partner_name         varchar(100) comment '����������',
   state                tinyint default 0 comment '״̬��0����Ч�� 1ʧЧ��',
   contacts             varchar(20) comment '��ϵ��',
   telephone            varchar(13) comment '�̶��绰',
   mobilephone          varchar(11) comment '�ƶ��绰',
   address              varchar(100) comment '��ַ',
   create_time          timestamp comment '����ʱ��',
   update_time          timestamp comment '����ʱ��',
   primary key (partner_id)
);

/*==============================================================*/
/* Table: t_s_payorder                                          */
/*==============================================================*/
create table t_s_payorder
(
   pay_id               varchar(22) not null comment '֧��ID',
   order_id             varchar(22) comment '����ID',
   pay_account          varchar(32) comment '֧���˻�ID',
   pay_money            bigint comment '֧��Ǯ��',
   pay_type             varchar(3) comment '΢��֧����WCP��֧����֧����ALP',
   oper_type            tinyint comment '֧����1  ���˿2',
   state                varchar(2),
   origin_refound_money bigint comment 'ԭ·�˷ѽ��',
   origin_refound_time  timestamp comment 'ԭ·�˷�ʱ��',
   man_made_refound_time timestamp comment '�˹��˷�ʱ��',
   man_made_refound_type varchar(3) comment '�˹��˷ѷ�ʽ��֧������ALP��΢��WCP��ͨѶ�˻���CTP��������OTP��',
   account_day          timestamp comment '����',
   pre_pay_id           varchar(128) comment 'Ԥ֧��ID',
   third_pay_id         varchar(128) comment '������֧��ID',
   update_time          timestamp,
   create_time          timestamp comment '֧��ʱ��',
   primary key (pay_id)
);

/*==============================================================*/
/* Table: t_s_product                                           */
/*==============================================================*/
create table t_s_product
(
   ID                   bigint not null,
   product_code         varchar(6) not null,
   wo_product_code      varchar(8),
   product_name         varchar(100),
   price                int default 0,
   wo_product_price     int,
   state                tinyint default 0 comment '��Ч��0��ʧЧ��1',
   cycle_type           tinyint default 0 comment '���£�0�������꣺1�����꣺2',
   type                 tinyint default 0 comment '��ǰ�շѣ�0�� ����շѣ�1',
   can_unsubscribe      tinyint default 0 comment '����0�������� 1',
   area_code            varchar(6) comment '��������',
   area_name            varchar(100) comment '��������',
   create_time          timestamp,
   update_time          timestamp,
   primary key (ID)
);

alter table t_s_product comment '������Ʒ�����';

/*==============================================================*/
/* Table: t_s_product_price                                     */
/*==============================================================*/
create table t_s_product_price
(
   ID                   bigint not null,
   product_id           bigint not null comment '��ƷID',
   partner_id           bigint comment '������ID',
   app_id               bigint comment 'Ӧ��ID',
   partner_price        int default 0 comment '��Ʒ��Ժ������ļ۸�',
   app_price            int comment '��Ʒ���Ӧ�õļ۸�',
   state                tinyint default 0 comment '��Ч��0��ʧЧ��1',
   create_time          timestamp,
   update_time          timestamp,
   area_code            varchar(6) comment '��������',
   area_name            varchar(100) comment '��������',
   primary key (ID)
);

alter table t_s_product_price comment '������Ʒ�۸����';

/*==============================================================*/
/* Table: t_s_vcode                                             */
/*==============================================================*/
create table t_s_vcode
(
   ID                   bigint not null,
   order_id             varchar(22) comment '����ID',
   vcode_send_time      varchar(14) comment '���һ����֤���·�ʱ��',
   lvcode               varchar(6) comment '�·�����֤��',
   user_input_vcode     varchar(6) comment '�û�������֤������',
   user_input_time      varchar(14) comment '��֤����дʱ�� ',
   vcode_valid_resut    varchar(4) default '�ɹ�' comment 'У����',
   primary key (ID)
);

alter table t_s_vcode comment '�û����һ����֤����Ϣ������Ǩ��';

/*==============================================================*/
/* Table: t_s_woplat_order                                      */
/*==============================================================*/
create table t_s_woplat_order
(
   order_id             varchar(22) not null,
   wo_order_id          varchar(36) comment '�ּ��ܹܷ��ص�ID',
   partner_code         varchar(12) comment '����������',
   app_key              varchar(36) comment '��������ƷID',
   partner_order_id     varchar(36) comment '������鶩��ID',
   product_code         varchar(6),
   state                varchar(2),
   mobilephone          varchar(11) comment '�����ֻ�����',
   order_channel        varchar(8) comment '����������APP��WEB��FILE���ļ��ӿڡ�Others��������',
   create_time          timestamp comment '����ʱ��',
   update_time          timestamp comment '����ʱ��',
   valid_time           timestamp comment '��Чʱ��',
   invalid_time         timestamp comment 'ʧЧʱ��',
   price                int comment '������Ʒ�۸�',
   count                int default 1 comment '��������',
   money                bigint comment '�����۸�',
   is_need_charge       tinyint comment '�Ƿ���Ҫ���仰�ѣ�0����Ҫ 1������Ҫ��',
   charge_type          tinyint comment 'ÿ�¹̶�ʱ�䣺0��һ���ԣ�1',
   remark               varchar(100),
   primary key (order_id)
);

alter table t_s_woplat_order comment '�ּ��ܹ�ͬ�������ĳ��Լ�����Ķ�����ϵ';

alter table t_s_app add constraint FK_Reference_1 foreign key (partner_id)
      references t_s_partner (partner_id) on delete restrict on update restrict;

