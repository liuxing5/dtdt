package com.asiainfo.dtdt.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;


/**
* @ClassName: DataUtil 
* @Description: 拼手气算法
 */
public class DataUtil {

	/**
	 * Description: 获取随机金额
	 * @param redPacketTotalMoney
	 * @param redPacketCount
	 * @param redPacketLotteryCount
	 * @param set
	 * @return
	 */
	public static int getRandMoney(int redPacketTotalMoney, int redPacketCount, int redPacketLotteryCount, Set<Integer> set)
	{
		int random_money = 0;
		double pre_money = redPacketTotalMoney / (redPacketCount - redPacketLotteryCount);
		if (1 < pre_money)
		{
			random_money = DataUtil.getRandomNum((int) pre_money, set);
			if (redPacketLotteryCount == (redPacketCount - 2))
			{
				Iterator<Integer> itx = set.iterator();
				while (itx.hasNext())
				{
					Integer x = itx.next();
					Iterator<Integer> ity = set.iterator();
					while (ity.hasNext())
					{
						Integer y = ity.next();
						if (redPacketTotalMoney == x + y)
							random_money = y;
					}
				}
			}
			if (0 >= (redPacketTotalMoney - random_money - (redPacketCount - redPacketLotteryCount)))
			{
				random_money = 1;
			}
			if (redPacketLotteryCount == (redPacketCount - 1))
			{
				random_money = redPacketTotalMoney;
			}
		} else
		{
			random_money = 1;
		}
		return random_money;
	}
	
	/**
	 * Description:获取随机数
	 * @param preNum
	 * @param set
	 * @return	
	 * 
	 */
	public static int getRandomNum(int preNum, Set<Integer> set)
	{
		int randomNum = 1;
		if (preNum > 1)
		{
			Random r = new Random();
			randomNum = r.nextInt(preNum * 2) + 1;
		}
		randomNum = getNumberNearSet(randomNum, set);
		return randomNum;
	}

	/**
	 * Description: 把sum拆分成targetSetDES中的组合
	 * @param sum
	 * @param targetSetDES
	 * @param resultList
	 */
	public static void splitNumber(int sum, int targetSetDES[],
			List<Integer> resultList)
	{
		if (0 != sum)
		{
			for (int i : targetSetDES)
			{
				if (i <= sum)
				{
					resultList.add(i);
					sum = sum - i;
					splitNumber(sum, targetSetDES, resultList);
					break;
				}
			}
		}
	}
	
	/**
	 * Description: 找出set中离给定数最近的值
	 * @param number
	 * @param set
	 * @return
	 */
	public static int getNumberNearSet(int number, Set<Integer> set)
	{
		int result = 1;
		int absoluteValue = 0;
		int compareNum = 0;
		Iterator<Integer> it = set.iterator();
		while (it.hasNext())
		{
			Integer current = it.next();
			absoluteValue = number - current;
			absoluteValue = absoluteValue * (1 - ((absoluteValue >>> 31) << 1));
			if (absoluteValue == 0)
			{
				result = current;
			} else
			{
				if (absoluteValue < compareNum)
					result = current;
			}
			compareNum = absoluteValue;
		}
		return result;
	}

	/**
	 * Description:set倒序
	 * @param targetSet
	 * @return
	 */
	public static int[] setToArrayDes(Set<Integer> targetSet)
	{
		int size = targetSet.size();
		int targetSetDES[] = new int[size];
		Iterator<Integer> itx = targetSet.iterator();
		while (itx.hasNext())
		{
			targetSetDES[size - 1] = itx.next();
			size--;
		}
		//从大到小排序
		int temp;
		for (int i = 0; i < targetSetDES.length; i++) {   
	        for (int j = targetSetDES.length - 1; j >i; j--)  {   
	        	if (targetSetDES[i] < targetSetDES[j]) { // 交换两数的位置   
	                temp = targetSetDES[i];   
	                targetSetDES[i] = targetSetDES[j];   
	                targetSetDES[j] = temp;   
	            }  
	        }   
	        
	    } 
		return targetSetDES;
	}
	
	public static Set<Integer> str2Set(String integerStr,Integer max,Integer min) throws Exception
	{
		Set<Integer> targetSet = new TreeSet<Integer>();
		String[] setStrs = integerStr.split(",");
		for (int i = 0; i < setStrs.length-1; i++)
		{
		int  maxTest = (null != max)?(Integer.valueOf(setStrs[i]) < max ? 1:0):1;
		int  minTest = (null != min)?(Integer.valueOf(setStrs[i]) > min ? 1:0):1;
		if(1 == maxTest?true:false)
			if(1 == minTest?true:false)
				targetSet.add(Integer.valueOf(setStrs[i]));
		}
		return targetSet;
	}
	
	public static String set2Str(Set<Integer> integerSet) throws Exception
	{
		Iterator<Integer> it = integerSet.iterator();
		String content = "";
		while (it.hasNext())
		{
			content += it.next() + ",";
		}
		return content;
	}
	public static void main(String[] args) throws Exception
	{
//		int redPacketTotalMoney = 100;//红包金额
//		int redPacketCount = 10;
//		int redPacketLotteryCount = 0;
//		int sum = 0;
//		List<Integer> redPacketList = new ArrayList<Integer>();
		/*for (int i = 0; i < redPacketCount; i++)
		{
			int random_money = getRandMoney(redPacketTotalMoney, redPacketCount,
					redPacketLotteryCount, rechargeService.getRechargeDenomination(0,0));
			System.out.println("第" +  i + "个红包，包" + random_money +"元");
			//红包保存
			redPacketList.add(random_money);
			redPacketTotalMoney = redPacketTotalMoney - random_money;
			redPacketLotteryCount++;
		}*/
//		for (int i = 0; i < redPacketList.size(); i++)
//		{
//			sum += redPacketList.get(i);
//		}
//			System.out.println("sum=" + sum);
		Set<Integer> set = new HashSet<Integer>();
		set.add(1);
		set.add(20);
		set.add(5);
		set.add(100);
		set.add(10);
		set.add(50);
		set.add(30);
		
		//a.从库中取面额值
		//b.直接设置面额值，从数据库中取值
		//保存拆分后的金额
		List<Integer> resultList = new ArrayList<Integer>();	
		//进行拆分
		DataUtil.splitNumber(159,
			DataUtil.setToArrayDes(set), resultList);
		System.out.println(resultList);
	}
}
