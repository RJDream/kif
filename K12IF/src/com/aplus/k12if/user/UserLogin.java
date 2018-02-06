/**
 * Id: UserLogin.java, v1.0 2015-8-13 下午4:41:06 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.user;

import java.util.HashMap;
import java.util.Map;

import com.aplus.utils.MD5Util;
import com.aplus.utils.StringUtils;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.db.DbTable;
import com.aplus.v1.framework.interf.NoneTransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestData;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@UserLogin.java </p>
 * <p>Description: 
 *    用户登录接口
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-13
 * ------------------------------------------------------------
 */
@AutoInterface(id="IF00010001",versions="V1000",des="用户登录接口")
public class UserLogin extends NoneTransactionInterfaceBase {
	
	final static String QUERY_USER = "select id,username,head from k12_member t where t.mobile = '{0}' and t.password = '{1}'";

	/* （非 Javadoc）
	 * <p>Title: execute</p>
	 * <p>Description: </p>
	 * @param tmb
	 * @return
	 * @see com.aplus.v1.framework.interf.NoneTransactionInterfaceBase#execute(com.aplus.v1.framework.protocol.request.IFRequestMessage)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String execute(IFRequestMessage tmb) {
		// 业务数据
		IFRequestData data = getBody(tmb);
		Map<String, Object> ifData = ((HashMap<String, Object>) data.get("data"));
		String phoneNumber = ifData.get("phone").toString();
		String password = ifData.get("password").toString();
		Object[] params = new Object[]{phoneNumber, MD5Util.string2MD5(password) };
		try {
			DbTable table =	db.executeQuery(StringUtils.format(QUERY_USER, params));
			if (table.getRowCollection().size() == 0) {
				head.setRet(Constants.RET_E);
				head.setErrMsg("用户名或者密码错误！");
			} else {
				ifdata.put("id", table.getRowCollection().get(0).get("id"));
				ifdata.put("username", table.getRowCollection().get(0).get("username"));
				ifdata.put("head", table.getRowCollection().get(0).get("head"));
				head.setRet(Constants.RET_S);
				head.setErrMsg("登陆成功！");
			}
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrMsg(e.getMessage());
		}
		return ret();
	}

}
