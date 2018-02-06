/**
 * Id: SerachTeacherInfo.java, v1.0 2015-8-29 下午4:26:28 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.teacher;

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
 * <p>Title:@SerachTeacherInfo.java </p>
 * <p>Description: 
 *    TODO 类功能描述
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-29
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF00070001", versions = "V1000", des = "教师查询接口")
public class SerachTeacherInfo extends NoneTransactionInterfaceBase {

	// 异常配置文件
	Config config = Config.getInstance("k12_ex");
	
	// 根据地区查询学校
	final static String QUERY_TEACHER_INFO = "select t1.teacher_id,t1.course,t1.is_header ,t2.`name`,t2.mobile from k12_teaching_info t1 " +
			"LEFT JOIN k12_teacher t2 on t2.id = t1.teacher_id LEFT JOIN k12_school_class t3 ON t3.id = t1.sclass_id " +
			"where t2.school_id = '{0}' and t3.grade_code  = '{1}' and t3.class_code  =  '{2}' order by is_header DESC";

	
	
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
		Map<String, Object> busData = getBusData(tmb);

		// 学校ID/年纪/班级
		String studentId = busData.get("schoolId").toString();
		String gradeName = busData.get("grade").toString();
		String className = busData.get("class").toString();
		
		try {
			Object[] params = new Object[] {studentId, gradeName, className};
			DbTable table = db.executeQuery(StringUtils.format(QUERY_TEACHER_INFO,
					params));
			
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			Map<String, Object> map = null;
			while (table.next()) {
				map = new HashMap<String, Object>();
				map.put("id", table.getValue("teacher_id"));
				map.put("subject", table.getValue("course"));
				System.out.println("__" + (table.getValue("is_header").equals("1") ? true : false));
				map.put("isMain", table.getValue("is_header").equals("1") ? true : false);
				map.put("name", table.getValue("name"));
				map.put("phoneNumber", table.getValue("mobile"));
				// 头像占时为null
				map.put("head", "");
				list.add(map);
			}
			ifdata.put("result", list);
			head.setRet(Constants.RET_S);
		} catch (Exception e) {
			head.setErrCode("Ex070101");
			head.setRet(Constants.RET_E);
			head.setErrMsg(config.get("Ex070101") + e.getMessage());
		}
		return ret();
	}

}
