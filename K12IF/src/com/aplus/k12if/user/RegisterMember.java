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
import com.aplus.utils.UUIDGenerator;
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
 *    用户注册接口
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-14
 * ------------------------------------------------------------
 */
@AutoInterface(id="IF00010003",versions="V1000",des="用户注册接口！")
public class RegisterMember extends TransactionInterfaceBase {
	
	// 异常配置文件
	Config config = Config.getInstance("k12_ex");
	
	// 注册SQL
	final static String REGISTER_MEMBER = "INSERT INTO k12_member VALUES('{0}',NOW(),NOW(),NULL,NULL, '{1}','{1}','{2}',NULL)";
	
	// 检查唯一性SQL
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
		String password = ifData.get("password").toString();

		
		String id = UUIDGenerator.getUUID();
		
		// 不使用第三方验证工具时，使用此段代码进行验证码验证
		String validCode = ifData.get("code").toString();		
		if (!RegisterValidCode.validCodes.containsKey(tel)) {
			head.setRet(Constants.RET_E);
			head.setErrMsg(config.get("Ex010304"));
			return ret();
		} else {
			Phone phone = RegisterValidCode.validCodes.get(tel);
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
		Object[] params = new Object[]{id, tel, MD5Util.string2MD5(password) , tel};
		
		try {
			// 注册唯一性检查
			if (this.checkMemberPhoneOnly(tel)) {
				db.executeNonQuery(StringUtils.format(REGISTER_MEMBER, params));
				// 设置返回业务数据
				ifdata.put("id", id);
				head.setRet(Constants.RET_S);
				// 业务完成后，清理短信防轰炸工具类的记录数
				PhoneUtils.clearSendNumber(tel);
			} else {
				head.setRet(Constants.RET_E);
				head.setErrCode("Ex010302");
				head.setErrMsg(config.get("Ex010302"));
			}
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			if (org.apache.commons.lang.StringUtils.isEmpty(head.getErrCode()) ) {
				head.setErrCode("Ex010303");
			}
			head.setErrMsg(config.get("Ex010303") + "__" + e.getMessage());
		}
		return ret();
	}
	
	
	/**
	 * <p>方法描述: 检查注册用户手机号是否唯一 </p>
	 * @param tel
	 * @return
	 * @return boolean
	 * 2015-9-7 上午10:51:03
	 */
	protected boolean checkMemberPhoneOnly(String tel){
		Object[] params = new Object[]{tel};
		try {
			DbTable dbTable = db.executeQuery(StringUtils.format(CHECK_PHONE_ONLY, params));
			if (dbTable.getRowCollection().size() == 0) {
				return true;
			}
		} catch (Exception e) {
			head.setErrCode("Ex010301");
			throw new MessageException(config.get("Ex010301") + "__" + e.getMessage());
		}
		return false;
	}

}
