/**
 * Id: RegisterMember.java, v1.0 2015-8-14 下午2:27:34 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.user;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.aplus.bo.Phone;
import com.aplus.utils.MD5Util;
import com.aplus.utils.PhoneUtils;
import com.aplus.utils.StringUtils;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.config.Config;
import com.aplus.v1.framework.db.DbTable;
import com.aplus.v1.framework.exception.MessageException;
import com.aplus.v1.framework.interf.TransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestData;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@RegisterMember.java </p>
 * <p>Description: 
 *  	用户找回密码接口
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-14
 * ------------------------------------------------------------
 */
@AutoInterface(id="IF00010009",versions="V1000",des="用户找回密码接口！")
public class ForgetPassword extends TransactionInterfaceBase {
	
	// 异常配置文件
	Config config = Config.getInstance("k12_ex");
	
	// 查询SQL PHONE
	final static String CHECK_PHONE_ONLY = "SELECT id FROM k12_member t WHERE t.mobile = '{0}'";
	
	/* （非 Javadoc）
	 * <p>Title: execute</p>
	 * <p>Description: </p>
	 * @param tmb
	 * @return
	 * @see com.aplus.v1.framework.interf.TransactionInterfaceBase#execute(com.aplus.v1.framework.protocol.request.IFRequestMessage)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String execute(IFRequestMessage tmb) {
		
		// 业务数据
		IFRequestData data = getBody(tmb);
		Map<String, Object> ifData = ((HashMap<String, Object>) data.get("data"));
		String tel = ifData.get("phone").toString();
		
		// 不使用第三方验证工具时，使用此段代码进行验证码验证
		String validCode = ifData.get("code").toString();		
		if (!ForgetPasswordValidCode.validCodes.containsKey(tel)) {
			head.setRet(Constants.RET_E);
			head.setErrMsg(config.get("Ex010304"));
			return ret();
		} else {
			Phone phone = ForgetPasswordValidCode.validCodes.get(tel);
			if (!phone.checkValidTime()) {
				head.setRet(Constants.RET_E);
				head.setErrMsg("验证码已过期，请重新获取验证码！");
				return ret();
			}
			if (!phone.getCode().equals(validCode)) {
				head.setRet(Constants.RET_E);
				head.setErrMsg(config.get("Ex010305"));
				return ret();
			}
			RegisterValidCode.validCodes.remove(tel);
		}
		
		// 构建SQL参数
		Object[] params = new Object[]{tel};
		try {
			// 查询数据
			DbTable table = db.executeQuery(StringUtils.format(CHECK_PHONE_ONLY, params));
			if (table.getRowCollection().size() == 0) {
				head.setRet(Constants.RET_E);
				head.setErrMsg(" BUG 检查，非法请求！");
				return ret();
			}
			while (table.next()) {
				ifdata.put("id", table.getValue("id"));
			}
			head.setRet(Constants.RET_S);
			
			// 业务完成后，清理短信防轰炸工具类的记录数
			PhoneUtils.clearSendNumber(tel);
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			if (org.apache.commons.lang.StringUtils.isEmpty(head.getErrCode()) ) {
				head.setErrCode("Ex010303");
			}
			head.setErrMsg(config.get("Ex010303") + "__" + e.getMessage());
		}
		return ret();
	}
	

}
