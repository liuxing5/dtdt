package com.asiainfo.dtdt.service.mapper;

import com.asiainfo.dtdt.entity.Payorder;

public interface PayorderMapper {
    int deleteByPrimaryKey(String payId);

    int insertSelective(Payorder record);

    Payorder queryPayOrderByPayId(String payId);

    int updatePayOrder(Payorder record);

}