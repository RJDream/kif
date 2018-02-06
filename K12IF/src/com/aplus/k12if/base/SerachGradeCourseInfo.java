/**
 * Id: SerachGradeCourseInfo.java, v1.0 2015-10-26 下午5:34:27 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aplus.utils.StringUtils;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.db.DbTable;
import com.aplus.v1.framework.interf.NoneTransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@SerachGradeCourseInfo.java </p>
 * <p>Description: 
 *    查询学校年级配置的科目信息
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-10-26
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF00000003", versions = "V1000", des = "查询学校年级配置的科目信息")
public class SerachGradeCourseInfo extends NoneTransactionInterfaceBase {
	
	
	// 查询学习年级配置的科目信息
	final static String SERACH_GRADE_COURSE_INFO =  "SELECT												  " +
													"	t1.grade_code,                                    " +
													"	t2.course_code                                    " +
													"FROM                                                 " +
													"	k12_grade_course t1                               " +
													"LEFT JOIN k12_course t2 ON t1.id = t2.gradecourse_id " +
													"WHERE                                                " +
													"	t1.school_id = '{0}'                              ";

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
		String schoolId = busData.get("schoolId").toString();
		try {
			Object[] params = new Object[] {schoolId};
			// 查询字典数据
			DbTable table = db.executeQuery(StringUtils.format(SERACH_GRADE_COURSE_INFO,
					params));
			// 年级列表
			Map<String, List<String>> gradeList = new HashMap<String, List<String>>();
			
			// 年级对应班级列表
			List<String> gradeCourseList = null;
			
			Map<String, String> map = null;
			while (table.next()) {
				if (!gradeList.containsKey(table.getValue("grade_code"))) {
					gradeCourseList = new ArrayList<String>();
					gradeList.put(table.getValue("grade_code"), gradeCourseList);
				}
				gradeCourseList.add(table.getValue("course_code"));
			}
			ifdata.put("result", gradeList);
			head.setRet(Constants.RET_S);
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrMsg("查询学校年级科目信息异常：" + e.getMessage());
		}
		return ret();
	}

}
