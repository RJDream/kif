/**
 * Id: PhoneUtils.java, v1.0 2015-11-10 上午11:11:02 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.utils;

import java.util.HashMap;

import com.aplus.bo.Phone;

/**
 * ------------------------------------------------------------
 * <p>Title:@PhoneUtils.java </p>
 * <p>Description: 
 *    手机号防轰炸工具
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-11-09
 * ------------------------------------------------------------
 */
public class PhoneUtils {
	
	// 短信发送限制
	final static int MAX_NUMBER = 5;
	
	// 验证时间：24小时
	final static Long VALID_TIME = 1000L * 60 * 60 * 24;
	
	// 记录每个手机号的发送时间，和累计次数
	static HashMap<String, PhoneUtils.SentNumber> record = new HashMap<String, PhoneUtils.SentNumber>();
	
	// 发送记录数对象
	private class SentNumber{
		long startSentTimer; // 开始发送时间
		int number;	// 发送次数
	}
	
	/**
	 * <p>方法描述: 记录消息发送对象 </p>
	 * @param phone
	 * @return void
	 * 2015-11-09  下午3:56:10
	 */
	public static void recordSendNumber(Phone phone){
		PhoneUtils.SentNumber sendNumber;
		if (!record.containsKey(phone.getTel())) {
			// 初次
			sendNumber = new PhoneUtils().new SentNumber();
			sendNumber.startSentTimer = phone.getSendTime();
			sendNumber.number = 1;
			record.put(phone.getTel(), sendNumber);
		} else {
			// 累计
			sendNumber = record.get(phone.getTel());
			sendNumber.number += 1;
		}
	}
	
	/**
	 * <p>方法描述: 注册成功后清理记录数 </p>
	 * @param key
	 * @return void
	 * 2015-11-12 下午5:25:17
	 */
	public static void clearSendNumber(String key){
		record.remove(key);
	}
	
	/**
	 * <p>方法描述: 检查该号码在24小时内发送次数是否超过5次 </p>
	 * @param tel
	 * @return
	 * @return boolean
	 * 2015-11-09 下午3:08:02
	 */
	public static boolean checkSendNumber(String tel){
		if (record.containsKey(tel)) {
			long timeDifference = System.currentTimeMillis() - record.get(tel).startSentTimer;
			if (record.get(tel).number > MAX_NUMBER) {
				// 超过5次
				if (timeDifference > VALID_TIME) {
					// 已经超过验证时间，则重置记录数为1和记录时间
					record.get(tel).startSentTimer = System.currentTimeMillis();
					record.get(tel).number = 1;
					return false;
				} else {
					// 超过5次并在验证时间内则不能再发送了
					return true;
				}
			} else {
				return false;
			}
		}
		return false;
	}
	
	
	
}
