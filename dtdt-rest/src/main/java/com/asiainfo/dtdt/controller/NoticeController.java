package com.asiainfo.dtdt.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.dtdt.interfaces.order.INoticeService;

import lombok.extern.log4j.Log4j2;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月28日 下午9:15:51 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
@Controller
@Log4j2
@RequestMapping(value="/notice",method=RequestMethod.POST)
public class NoticeController {
	
	@Resource
	private INoticeService noticeService;
	
	@RequestMapping("/order")
	@ResponseBody
	public synchronized String notice(HttpServletRequest request, HttpServletResponse response) throws IOException{
		InputStream ins = request.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(ins, "ISO-8859-1"));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = in.readLine()) != null) {
        	sb.append(line);
        }
        String notifyJson = sb.toString(); //接收到通知信息。
        log.info("notice data:"+notifyJson);
		/**处理业务开始*/
		String result = noticeService.optNoticeOrder(notifyJson);
		/**处理业务结束*/
		return result;
	}
}
