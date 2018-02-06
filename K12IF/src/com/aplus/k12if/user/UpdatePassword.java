/**
 * Id: UpdatePassword.java, v1.0 2015-8-21 下午5:07:37 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.user;

import java.util.Map;

import com.aplus.utils.MD5Util;
import com.aplus.utils.StringUtils;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.db.DbTable;
import com.aplus.v1.framework.exception.MessageException;
import com.aplus.v1.framework.interf.TransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@UpdatePassword.java </p>
 * <p>Description: 
 *     密码修改接口
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-21
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF00010005", versions = "V1000", des = "密码修改接口")
public class UpdatePassword extends TransactionInterfaceBase {
	
	// 修改用户秘密SQL
	final static String UPDATE_PASSWORD = "update k12_member m set m.`password` = '{0}' where m.id = '{1}'";
	
	// 根据原密码查询用户SQL
	final static String QUERY_USER = "select * from k12_member t where t.`password` = '{0}' and t.id = '{1}'";
	

	/* （非 Javadoc）
	 * <p>Title: execute</p>
	 * <p>Description: </p>
	 * @param tmb
	 * @return
	 * @see com.aplus.v1.framework.interf.TransactionInterfaceBase#execute(com.aplus.v1.framework.protocol.request.IFRequestMessage)
	 */
	@Override
	public String execute(IFRequestMessage tmb) {
		// 业务数据
		Map<String, Object> busData = getBusData(tmb);
		
		// 学生ID/获取记录数
		String oldPW = busData.get("oldPassword") == null ? null : busData.get("oldPassword").toString();
		String newPW = busData.get("newPassword").toString();
		String id	= busData.get("id").toString();
		try {
			// 用户原密码检查
			if (oldPW != null && this.checkPassword(oldPW, id)) {
				Object[] params = new Object[]{MD5Util.string2MD5(newPW) , id};
				db.executeNonQuery(StringUtils.format(UPDATE_PASSWORD, params));
				head.setRet(Constants.RET_S);
				head.setErrMsg("修改密码成功！ ");
			} else {
				// 走"找回密码"功能所调用
				Object[] params = new Object[]{MD5Util.string2MD5(newPW) , id};
				db.executeNonQuery(StringUtils.format(UPDATE_PASSWORD, params));
				head.setRet(Constants.RET_S);
				head.setErrMsg("修改密码成功！ ");
			}
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrMsg("修改密码失败： " + e.getMessage());
		}
		return ret();
	}
	
	/***
	 * <p>方法描述: 检测用户密码是否正确 </p>
	 * @return
	 * @return boolean
	 * 2015-8-21 下午5:16:55
	 */
	private boolean checkPassword(String oldPass, String id)throws MessageException{
		Object[] params = new Object[]{MD5Util.string2MD5(oldPass) , id};
		try {
			DbTable table =	db.executeQuery(StringUtils.format(QUERY_USER, params));
			if (table.getRowCollection().size() == 0) {
				throw new MessageException("请输入正确的原密码");
			} else {
				return true;
			}
		} catch (Exception e) {
			throw new MessageException("原密码检查失败： " + e.getMessage());
		}
	}

}
