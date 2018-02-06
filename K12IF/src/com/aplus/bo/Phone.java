/**
 * Id: Phone.java, v1.0 2015-9-28 下午4:50:46 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.bo;

/**
 * ------------------------------------------------------------
 * <p>Title:@Phone.java </p>
 * <p>Description: 
 *    手机号信息，注册专用
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-9-28
 * ------------------------------------------------------------
 */
public class Phone {
	
	// 验证码有效时间
	final static Long VALID_TIME = 90 * 1000L;
	
	// 手机号码
	private String tel;
	
	// 注册验证码发送时间
	private Long sendTime;
	
	// 验证码
	private String code;
	
	
	/**
	 * <p>方法描述: 检查是否在有效时间内</p>
	 * @return void
	 * 2015-9-28 下午5:02:36
	 */
	public boolean checkValidTime(){
		Long currentTime = System.currentTimeMillis();
		Long time = currentTime - sendTime;
		if (time < VALID_TIME) {
			return true;
		}
		return false;
	}
	

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Long getSendTime() {
		return sendTime;
	}

	public void setSendTime(Long sendTime) {
		this.sendTime = sendTime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
