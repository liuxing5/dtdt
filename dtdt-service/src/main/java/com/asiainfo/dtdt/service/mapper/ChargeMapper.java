package com.asiainfo.dtdt.service.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.dtdt.entity.Charge;

public interface ChargeMapper {
    int deleteByPrimaryKey(String chargeId);

    int insert(Charge record);

    int insertSelective(Charge record);

    Charge selectByPrimaryKey(String chargeId);

    int updateByPrimaryKeySelective(Charge record);

    int updateByPrimaryKey(Charge record);
    
    /**
    * @Title: ChargeMapper 
    * @Description: (查询拆分的充值记录) 
    * @param rechargeParentId
    * @return        
    * @throws
     */
    List<Charge> getChargeByParentId(@Param("rechargeParentId")String rechargeParentId);
}