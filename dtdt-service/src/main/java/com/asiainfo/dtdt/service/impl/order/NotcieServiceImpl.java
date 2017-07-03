package com.asiainfo.dtdt.service.impl.order;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.interfaces.order.INoticeService;
import com.asiainfo.dtdt.interfaces.order.IOrderService;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年7月3日 上午11:31:25 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
@Service
public class NotcieServiceImpl implements INoticeService {

	private static final Logger logger = Logger.getLogger(NotcieServiceImpl.class);
	
	@Resource
	private IOrderService orderService;
	
	public void optNoticeOrder(String resultCode, String orderId) {
		logger.info("begin NoticeService optNoticeOrder param:{resultCode="+resultCode+",orderId="+orderId+"}");
		try {
			if(resultCode.equals("2")){//订购成功
				logger.info("NoticeService optNoticeOrder wojia return resultCode 2-订购成功");
				//沃家总管异步回调返回订购成功，我们需要生成订购关系，并且反冲话费
				/**调用反冲话费 start**/
				
				/**调用反冲话费 end**/
				orderService.updateOrder(orderId, null, "11", Constant.IS_NEED_CHARGE_0,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_0);
				orderService.orderPayBak(orderId, Constant.HISORDER_TYPE_0, "邮箱侧订购成功&沃家总管侧存在有效订购关系&返充话费成功");
				
			}else if(resultCode.equals("5")){//退订成功（可再订购）
				logger.info("NoticeService optNoticeOrder wojia return resultCode 5-退订成功（可再订购）");
				//退订成功将订单关系数据转移到备份表中
				orderService.updateOrder(orderId, null, "19", Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_0);
				orderService.orderPayBak(orderId, Constant.HISORDER_TYPE_0, "邮箱侧退订成功");
				
			}else if(resultCode.equals("6")){//订购失败
				logger.info("NoticeService optNoticeOrder wojia return resultCode 6-订购失败");
				//订购失败更新在途表状态5-订购失败待原路退款
				orderService.updateOrder(orderId, null, "5", Constant.IS_NEED_CHARGE_0,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_0);
			}else if(resultCode.equals("7")){//退订失败
				logger.info("NoticeService optNoticeOrder wojia return resultCode 7-退订失败");
				//一般情况下不会出现退订失败，退订失败不更新任何信息
				
			}
		} catch (Exception e) {
			logger.error("NoticeService optNoticeOrder fail:"+e.getMessage(),e);
			e.printStackTrace();
		}
	}

}
