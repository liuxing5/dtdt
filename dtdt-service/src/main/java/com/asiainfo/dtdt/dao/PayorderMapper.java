package com.asiainfo.dtdt.dao;

import com.asiainfo.dtdt.entity.Payorder;

public interface PayorderMapper {
    int deleteByPrimaryKey(String payId);

    int insert(Payorder record);

    int insertSelective(Payorder record);

    Payorder selectByPrimaryKey(String payId);

    int updateByPrimaryKeySelective(Payorder record);

    int updateByPrimaryKey(Payorder record);
}