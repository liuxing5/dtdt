package com.asiainfo.dtdt.service.impl.order;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.entity.App;
import com.asiainfo.dtdt.interfaces.IAppService;
import com.asiainfo.dtdt.service.mapper.AppMapper;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月29日 下午7:07:45 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
@Service
public class AppServiceImpl implements IAppService {

	private static final Logger logger = Logger.getLogger(AppServiceImpl.class);
	
	@Autowired
	private AppMapper appMapper;
	public AppServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * (非 Javadoc) 
	* <p>Title: queryAppInfo</p> 
	* <p>Description: </p> 
	* @param appKey
	* @return 
	* @see com.asiainfo.dtdt.interfaces.IAppService#queryAppInfo(java.lang.String)
	 */
	public String queryAppInfo(String appKey) {
		App app = appMapper.queryAppInfo(appKey);
		if(app==null){
			return null;
		}
		return JSONObject.toJSONString(app);
	}

}
