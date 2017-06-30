package com.asiainfo.dtdt.service.mapper;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.dtdt.entity.WoplatOrder;

public interface WoplatOrderMapper {
	/**
	* @Title: WoplatOrderMapper 
	* @Description: (同步插入订购关系信息) 
	* @param record
	* @return        
	* @throws
	 */
    int insertWolatOrder(WoplatOrder record);

    /**
    * @Title: WoplatOrderMapper 
    * @Description: (查询是否存在订购信息) 
    * @param productCode
    * @param mobilephone
    * @return        
    * @throws
     */
    WoplatOrder queryWoplatOrderByParam(@Param("productCode")String productCode,@Param("mobilephone")String mobilephone,@Param("state")String state);

    /**
    * @Title: WoplatOrderMapper 
    * @Description: (更新同步的订购关系信息) 
    * @param record
    * @return        
    * @throws
     */
    int updateWoplatOrder(WoplatOrder record);
}