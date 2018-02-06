/**
 * Id: TeacherLogin.java, v1.0 2015-9-18 下午2:36:40 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12teacher.user;

import java.util.Map;

import com.aplus.utils.MD5Util;
import com.aplus.utils.StringUtils;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.config.Config;
import com.aplus.v1.framework.db.DbTable;
import com.aplus.v1.framework.interf.NoneTransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@TeacherLogin.java </p>
 * <p>Description: 
 *    教师登陆接口
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-9-18
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF01010001", versions = "V1000", des = "教师登陆接口")
public class TeacherLogin extends NoneTransactionInterfaceBase {
	
	// 查询教师
	final static String QUERY_TEACHER = "SELECT t.id, t.school_id, t.`name`,t.head,t1.is_header FROM k12_teacher t LEFT JOIN k12_teaching_info t1 ON t1.teacher_id = t.id WHERE  t.username = '{0}' and t.`password` = '{1}' ORDER BY t1.is_header DESC LIMIT 0,1";
	

	// 异常配置文件
	Config config = Config.getInstance("k12_ex");
	
	/* （非 Javadoc）
	 * <p>Title: execute</p>
	 * <p>Description: </p>
	 * @param tmb
	 * @return
	 * @see com.aplus.v1.framework.interf.NoneTransactionInterfaceBase#execute(com.aplus.v1.framework.protocol.request.IFRequestMessage)
	 */
	@Override
	public String execute(IFRequestMessage tmb) {
		// 业务数据
		Map<String, Object> ifData = getBusData(tmb);
		String username = ifData.get("username").toString();
		String password = ifData.get("password").toString();
		
		// password 转码
		password = MD5Util.string2MD5(password);
		Object[] params = new Object[]{username, password};
		try {
			DbTable table =	db.executeQuery(StringUtils.format(QUERY_TEACHER, params));
			if (table.getRowCollection().size() == 0) {
				head.setRet(Constants.RET_E);
				head.setErrCode("Ex1010101");
				head.setErrMsg(config.get("Ex1010101"));
			} else {
				System.out.println( table.getRowCollection().get(0).get("is_header"));
				ifdata.put("id", table.getRowCollection().get(0).get("id"));
				ifdata.put("schoolId", table.getRowCollection().get(0).get("school_id"));
				ifdata.put("name", table.getRowCollection().get(0).get("name"));
				ifdata.put("isMain", table.getRowCollection().get(0).get("is_header"));
				ifdata.put("head", table.getRowCollection().get(0).get("head"));
				head.setRet(Constants.RET_S);
				head.setErrCode("Ex1010102");
				head.setErrMsg(config.get("Ex1010102"));
			}
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrCode("Ex1010103");
			head.setErrMsg(config.get("Ex1010103") + ":" + e.getMessage());
		}
		return ret();
	}

}
