package com.asiainfo.dtdt.common;

import java.util.Random;
import java.util.UUID;

import org.springframework.util.StringUtils;

/**
 * Description: 生成ID
 */
public class BaseSeq {
	public static String getSeq()
	{
		StringBuilder strb = new StringBuilder();
		strb.append(DateUtil.getSysdateYYYYMMDDHHMMSS());
		strb.append(UUID.randomUUID().toString().replace("-", "").toUpperCase()
				.subSequence(0, 8));
		return strb.toString();
	}
	
	public static String getLongSeq()
	{
		StringBuilder strb = new StringBuilder();
		strb.append(DateUtil.getSysdateYYYYMMDDHHMMSS());
		int x = (int)(Math.random()*13);
		strb.append(UUID.randomUUID().toString().replace("-", "").toUpperCase()
				.subSequence(x, x+18));
		return strb.toString();
	}
	
	public static String getSeq(String seq)
	{
		if(StringUtils.isEmpty(seq))
		{
			return "";
		}
		
		StringBuilder strb = new StringBuilder();
		strb.append(DateUtil.getSysdateYYYYMMDDHHMMSS());
		if(seq.length() < 8)
		{
			for(int i=0; i<8-seq.length(); i++)
			{
				strb.append("0");
			}
		}
		
		strb.append(seq);
		
		return strb.toString();
	}

	public static String getSeqq(String date)
	{
		StringBuilder strb = new StringBuilder();
		strb.append(date);
		strb.replace(4, 5, "");
		strb.replace(6, 7, "");
		strb.replace(8, 9, "");
		strb.replace(10, 11, "");
		strb.replace(12, 13, "");
		strb.append(UUID.randomUUID().toString().replace("-", "").toUpperCase()
				.subSequence(0, 8));
		return strb.toString();
	}

	public static void main(String[] args)
	{
//		for (int i = 0; i < 20; i++)
//		{
//			System.out.println(getSeq());
//		}
		long unboundedLong = new Random().nextLong();
		System.out.println(unboundedLong);
	}
	
}
