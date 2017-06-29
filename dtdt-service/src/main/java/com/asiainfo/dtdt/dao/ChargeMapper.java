package com.asiainfo.dtdt.dao;

import com.asiainfo.dtdt.entity.Charge;

public interface ChargeMapper {
    int deleteByPrimaryKey(String chargeId);

    int insert(Charge record);

    int insertSelective(Charge record);

    Charge selectByPrimaryKey(String chargeId);

    int updateByPrimaryKeySelective(Charge record);

    int updateByPrimaryKey(Charge record);
}