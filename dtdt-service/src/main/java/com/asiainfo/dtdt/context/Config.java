package com.asiainfo.dtdt.context;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Description: 加载配置信息
 */
public class Config {

	public static Properties props = null;
	public static String path = null;
	
	static
	{
		if (System.getProperty("os.name").startsWith("win") || System.getProperty("os.name").startsWith("Win")
				|| System.getProperty("os.name").startsWith("Mac OS X")){
			path = Config.class.getResource("/").getPath();
		}else{
			path = System.getProperty("user.dir") + "/resources";
		}
	}
	
	static
	{
		try
		{
			props = new Properties();
			InputStreamReader inputStream = new InputStreamReader(new FileInputStream(path + "/properties/conf.properties"));
			props.load(inputStream);
			props.keySet();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//短信验证登陆相关
	public static String SMSTIMER = props.getProperty("SMSTimer");
	public static String SMSCOUNTTIMER = props.getProperty("SMSCountTimer");
	public static String SMSCOUNTMAX = props.getProperty("SMSCountMax");
	public static String SESSIONTIMER = props.getProperty("SessionTimer");
	public static String SMSMESAGECODE = props.getProperty("SMSMesageCode");
	public static String PHONENUMMODE = props.getProperty("phoneNumMode");
	
	//沃推信息
	public static String APPKEY = props.getProperty("appKey");
	public static String APPSECRET = props.getProperty("appSecret");
	public static String CENTERURL = props.getProperty("centerUrl");
	public static String KEEPTIME = props.getProperty("keepTime");
	//附件上传路径
	public static String FILE_URL = props.getProperty("file_url");
	public static String FILE_NAME = props.getProperty("file_name");
	
	public static void setProperty(String key, String value)
	{
		props.setProperty(key, value);
	}
	
	public static String getString(String key)
	{
		return props.getProperty(key);
	}
}
