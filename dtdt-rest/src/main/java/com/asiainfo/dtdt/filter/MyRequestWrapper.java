package com.asiainfo.dtdt.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import lombok.Data;

import com.alibaba.fastjson.JSONObject;

@Data
public class MyRequestWrapper extends HttpServletRequestWrapper {

	private final JSONObject body;

	public MyRequestWrapper(HttpServletRequest request, Map newParams)
			throws Exception {
		super(request);
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try
		{
			InputStream inputStream = request.getInputStream();
			if (inputStream != null)
			{
				bufferedReader = new BufferedReader(new InputStreamReader(
						inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0)
				{
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else
			{
				stringBuilder.append("");
			}
		} catch (IOException ex)
		{
			throw ex;
		} finally
		{
			if (bufferedReader != null)
			{
				try
				{
					bufferedReader.close();
				} catch (IOException ex)
				{
					throw ex;
				}
			}
		}
		this.body = JSONObject.parseObject(stringBuilder.toString());;
		this.params = newParams;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException
	{
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				body.toString().getBytes());
		ServletInputStream servletInputStream = new ServletInputStream() {

			public int read() throws IOException
			{
				return byteArrayInputStream.read();
			}

			@Override
			public boolean isFinished()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isReady()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener)
			{
				// TODO Auto-generated method stub

			}
		};
		return servletInputStream;
	}

	@Override
	public BufferedReader getReader() throws IOException
	{
		return new BufferedReader(new InputStreamReader(this.getInputStream()));
	}

	public String DecryptBody(String encryptBody) throws Exception
	{
		String decryptBody = "";
		JSONObject json = JSONObject.parseObject(encryptBody);
		// json =  analysisJsonObject(json);
		// decryptBody = json.toString();
		return json.toString();
	}

	private JSONObject analysisJsonObject( JSONObject json){
    	 Set<Entry<String, Object>> setJson = json.entrySet();
    	 Iterator<Entry<String, Object>> iterator = setJson.iterator();
    	 try
		{
    		 while(iterator.hasNext()){
    			 Entry<String, Object> entryStrJson =  iterator.next();
    			 Object value = entryStrJson.getValue();
    			 String key = entryStrJson.getKey();
    			/* if(value){
    				 json.add(key, analysisJsonObject(value.getAsJsonObject()));
    			 }else if(value.isJsonPrimitive()){
    				 Gson gson = new Gson();
    				 JsonElement newValue = gson.toJsonTree(Des3.decode(value.toString().replace("\"", "")));
    				 json.add(key, newValue);
    				 continue;
    			 }else 
    				 continue;*/
    		 }
    		 return json;
		} catch (Exception e)
		{
			throw e;
		}
     }

	@SuppressWarnings("rawtypes")
	private Map params;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map getParameterMap()
	{
		return params;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Enumeration getParameterNames()
	{
		Vector l = new Vector(params.keySet());
		return l.elements();
	}

	public String[] getParameterValues(String name)
	{
		Object v = params.get(name);
		if (v == null)
		{
			return null;
		} else if (v instanceof String[])
		{
			return (String[]) v;
		} else if (v instanceof String)
		{
			return new String[] { (String) v };
		} else
		{
			return new String[] { v.toString() };
		}
	}

	public String getParameter(String name)
	{
		Object v = params.get(name);
		if (v == null)
		{
			return null;
		} else if (v instanceof String[])
		{
			String[] strArr = (String[]) v;
			if (strArr.length > 0)
			{
				return strArr[0];
			} else
			{
				return null;
			}
		} else if (v instanceof String)
		{
			return (String) v;
		} else
		{
			return v.toString();
		}
	}

}
