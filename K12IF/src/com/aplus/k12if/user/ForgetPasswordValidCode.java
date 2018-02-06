/**
 * Id: RegisterValidCode.java, v1.0 2015-8-14 下午3:15:20 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.aplus.bo.Phone;
import com.aplus.utils.PhoneUtils;
import com.aplus.utils.SendSms;
import com.aplus.utils.StringUtils;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.config.Config;
import com.aplus.v1.framework.interf.NoneTransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestData;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@RegisterValidCode.java </p>
 * <p>Description: 
 *     用户忘记密码找回验证码
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-14
 * ------------------------------------------------------------
 */
@AutoInterface(id="IF00010008",versions="V1000",des="用户忘记密码找回，获取验证码接口")
public class ForgetPasswordValidCode extends NoneTransactionInterfaceBase {

	// 用户注册时，验证码存放Map
	public static Map<String, Phone> validCodes = new HashMap<String, Phone>();
	
	
	// 异常配置文件
	Config config = Config.getInstance("k12_ex");
	
	// TODO 验证码Map 定时清理机制
	static String SMS_TEPMLATE = "欢迎来到文昊校园平台，您的验证码为：{0}，有效时间为90秒！";
	

	/*
	 * （非 Javadoc） <p>Title: execute</p> <p>Description: </p>
	 * 
	 * @param tmb
	 * 
	 * @return
	 * 
	 * @see
	 * com.aplus.v1.framework.interf.NoneTransactionInterfaceBase#execute(com
	 * .aplus.v1.framework.protocol.request.IFRequestMessage)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String execute(IFRequestMessage tmb) {
		IFRequestData data = getBody(tmb);
		Map<String, Object> ifData = ((HashMap<String, Object>) data.get("data"));
		String phoneNumber = ifData.get("phone").toString();
		
		// 防短信轰炸及机器验证拦截过滤（24 小时内不能超过5次）
		if (PhoneUtils.checkSendNumber(phoneNumber)) {
			head.setRet(Constants.RET_E);
			head.setErrMsg("你已重复发送5条验证码，超过发送上限，请24小时后再重新获取验证码！");
			return ret();	
		}
		
		// 防短信轰炸及机器验证拦截过滤
		if (validCodes.containsKey(phoneNumber)) {
			Phone p = validCodes.get(phoneNumber);
			// 在有效期内，不能重复发送短信
			if (p.checkValidTime()) {
				head.setRet(Constants.RET_E);
				head.setErrMsg("请勿重复发送短信！");
				return ret();	
			}
		} 
		
		try {
			RegisterMember member = new RegisterMember();
			// 注册唯一性检查
			if (member.checkMemberPhoneOnly(phoneNumber)) {
				head.setRet(Constants.RET_E);
				head.setErrMsg(config.get("该手机号还未注册，请先注册！"));
				return ret();
			}
			
			// 生成随机码
			String key =  this.create4BitValidCode(); 	
			String[] phone = new String[]{phoneNumber};
			Object[] params = {key};
			String result = SendSms.sendMessage(StringUtils.format(SMS_TEPMLATE, params) , phone);
			Map<String, String> map = SendSms.parserResult(result);
			if (map.get(SendSms.RESULT).equals("0")) {
				Phone p = new Phone();
				p.setTel(phoneNumber);
				p.setSendTime(System.currentTimeMillis());
				p.setCode(key);
				PhoneUtils.recordSendNumber(p);
				validCodes.put(phoneNumber, p);
				head.setRet(Constants.RET_S);
				// 测试用代码
				//ifdata.put("code", key);
			} else {
				head.setRet(Constants.RET_E);
				head.setErrMsg(map.get(SendSms.DESCRIPTION));
			}
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrMsg("获取验证码失败：" + e.getMessage());
		}
		return ret();
	}	
	
	/**
	 * <p>方法描述: 生成4位数验证码 </p>
	 * @return
	 * @return String
	 * 2015-8-14 下午3:48:04
	 */
	private String create4BitValidCode(){
		return this.createValidCode(4);
	}

	/**
	 * <p>方法描述: 生成6位数验证码 </p>
	 * @return
	 * @return String
	 * 2015-8-14 下午3:48:12
	 */
	private String create6BitValidCode() {
		return this.createValidCode(6);
	}
	
	/**
	 * <p>方法描述: 生成指定位数的验证 </p>
	 * @param bit
	 * @return
	 * @return String
	 * 2015-8-14 下午3:44:26
	 */
	private String createValidCode(int bit) {
		String str = "0,1,2,3,4,5,6,7,8,9";
		String str2[] = str.split(",");// 将字符串以,分割
		Random rand = new Random();// 创建Random类的对象rand
		int index = 0;
		String randStr = "";// 创建内容为空字符串对象randStr
		for (int i = 0; i < bit; ++i) {
			index = rand.nextInt(str2.length - 1);// 在0到str2.length-1生成一个伪随机数赋值给index
			randStr += str2[index];// 将对应索引的数组与randStr的变量值相连接
		}
		return randStr;
	}
	
	
	
	
	
	public static void main(String[] args){
//	    RegisterValidCode.create6BitValidCode();
	}

}
