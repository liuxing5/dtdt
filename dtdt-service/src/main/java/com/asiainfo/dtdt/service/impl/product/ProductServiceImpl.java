package com.asiainfo.dtdt.service.impl.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.common.ReturnUtil;
import com.asiainfo.dtdt.entity.App;
import com.asiainfo.dtdt.entity.Partner;
import com.asiainfo.dtdt.entity.Product;
import com.asiainfo.dtdt.interfaces.IProductService;
import com.asiainfo.dtdt.service.mapper.AppMapper;
import com.asiainfo.dtdt.service.mapper.PartnerMapper;
import com.asiainfo.dtdt.service.mapper.ProductMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ProductServiceImpl implements IProductService {
	
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private AppMapper appMapper;
	
	@Autowired
	private PartnerMapper partnerMapper;
	
	/**
	* @Title: getProductList 
	* @Description: 查询可订购产品列表服务
	* @return List
	* @throws
	 */
	public String getProductList(String appkey, String partnerCode) {
		log.info("ProductServiceImpl getProductList() appkey=" + appkey + " partnerCode=" + partnerCode);
		
		try {
			//校验合作方信息
			if (checkPartner(appkey, partnerCode)) {
				return ReturnUtil.returnJsonList(Constant.PARTNER_ERROR_CODE, Constant.PARTNER_ERROR_MSG, null);
			}
			
			App app = appMapper.queryAppInfo(appkey);
			List<Product> list = productMapper.getProductList(app.getAppId());
			if (list.size() == 0) {
				return ReturnUtil.returnJsonList(Constant.NO_PRODUCT_CODE, Constant.NO_PRODUCT_MSG, null);
			}
			log.info("ProductServiceImpl getProductList() list=" + list);
			return ReturnUtil.returnJsonList(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, list);
		} catch (Exception e) {
			log.info("ProductServiceImpl getProductList() Exception e" + e);
			return ReturnUtil.returnJsonList(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
	}
	
	/**
	* @Title: checkPartner 
	* @Description: 校验合作方信息
	* @param appkey
	* @param partnerCode
	* @return boolean
	* @throws
	 */
	private boolean checkPartner(String appkey, String partnerCode) {
		App app = appMapper.queryAppInfo(appkey);
		Partner partner = partnerMapper.selectByPrimaryKey(app.getPartnerId());
		if (!partner.getPartnerCode().equals(partnerCode)) {
			return false;
		}
		return true;
	}

	/**
	* @Title: queryProduct 
	* @Description: 根据产品 编码查询产品
	* @return String
	* @throws
	 */
	public String queryProduct(String productCode) {
		log.info("ProductServiceImpl queryProduct param:productCode="+productCode);
		
		try {
			Product product = productMapper.queryProduct(productCode);
			if(product == null){
				return null;
			}
			return JSONObject.toJSONString(product);
		} catch (Exception e) {
			log.info("ProductServiceImpl queryProduct() Exception e" + e);
			return ReturnUtil.returnJsonList(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
	}
}
