/**
 * Id: SerachSchoolTeacher.java, v1.0 2015-11-4 下午3:29:43 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12teacher.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aplus.utils.StringUtils;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.config.Config;
import com.aplus.v1.framework.db.DbTable;
import com.aplus.v1.framework.interf.NoneTransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@SerachSchoolTeacher.java </p>
 * <p>Description: 
 *    查询学校所有教师信息
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-11-4
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF01020002", versions = "V1000", des = "根据学校ID查询学校教师信息")
public class SerachSchoolTeacher extends NoneTransactionInterfaceBase {

	// 异常配置文件
	Config config = Config.getInstance("k12_ex");
	
	// 查询学校教师信息
	final static String QUERY_SCHOOL_TEACHER = "SELECT t.id,t.`name`,t.mobile,t.age,t.gender,t.head_teacher, t.address,t.cornet FROM k12_teacher t where t.`status` <> '1'  and t.school_id = '{0}' ";
	
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
		// schoolId
		String schoolId = busData.get("schoolId").toString();
		try {
			Object[] params = new Object[] {schoolId};
			DbTable table = db.executeQuery(StringUtils.format(QUERY_SCHOOL_TEACHER,
					params));
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = null;
			while (table.next()) {
				map = new HashMap<String, Object>();
				map.put("id", table.getValue("id"));
				map.put("name", table.getValue("name"));
				map.put("mobile", table.getValue("mobile"));
				map.put("age", table.getValue("age"));
				map.put("gender", table.getValue("gender"));
				map.put("head_teacher", table.getValue("head_teacher"));
				map.put("address", table.getValue("address"));
				map.put("cornet", table.getValue("cornet"));
				list.add(map);
			}
			ifdata.put("result", list);
			head.setRet(Constants.RET_S);
			head.setErrCode("Ex1020201");
			head.setErrMsg(config.get("Ex1020201"));
		} catch (Exception e) {
			head.setErrCode("Ex1020202");
			head.setRet(Constants.RET_E);
			head.setErrMsg(config.get("Ex1020202") + e.getMessage());
		}
		return ret();
	}

}
