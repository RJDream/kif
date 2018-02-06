/**
 * Id: SerachTeachInfo.java, v1.0 2015-9-18 下午4:09:03 Sunshine Exp
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
 * 
 * ------------------------------------------------------------
 * <p>Title:@SerachTeachInfo.java </p>
 * <p>Description: 
 *    查询老师任课信息,及其班级下的学生/家长信息
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-9-18
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF01020001", versions = "V1000", des = "查询老师任课信息,及其班级下的学生/家长信息")
public class SerachTeachInfo extends NoneTransactionInterfaceBase {

	// 异常配置文件
	Config config = Config.getInstance("k12_ex");

	// 根据地区查询学校
	final static String QUERY_TEACH_INFO = "SELECT t.id,t.sclass_id,t.course, t.grade, t.grade_class,( SELECT c.alias FROM k12_school_class c WHERE c.id = t.sclass_id ) " +
										"AS class_name, t.is_header, t1.`name`, t2.id AS student_id, t2.`name` AS student_name,t2.crash_contacts,t4.id AS member_id, t4.head " +
										"FROM k12_teaching_info t LEFT JOIN k12_teacher t1 ON t.teacher_id = t1.id LEFT JOIN k12_student t2 ON " +
										"t.sclass_id = t2.sclass_id LEFT JOIN k12_student_member t3 ON t3.students_set_id = t2.id " +
										"LEFT JOIN k12_member t4 ON t4.id = t3.member_set_id WHERE t.teacher_id = '{0}' " +
										"  ";
	// AND t4.id IS NOT NULL
	

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

		// 教师ID
		String teacherId = busData.get("teacherId").toString();
		try {
			Object[] params = new Object[] { teacherId };
			DbTable table = db.executeQuery(StringUtils.format(
					QUERY_TEACH_INFO, params));
			
			// 班级组
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = null;
			
			// 学生组
			List<Map<String, Object>> students = null;
			Map<String, Object> student = null;
			
			// 上一个班级分组ID
			String lastId = null;
			while (table.next()) {
				if (lastId == null || !lastId.equals(table.getValue("id"))) {
					map = new HashMap<String, Object>();
					students = new ArrayList<Map<String, Object>>();
					map.put("id", table.getValue("id"));
					map.put("sclassId", table.getValue("sclass_id"));
					map.put("subject", table.getValue("course"));
					map.put("isMain", table.getValue("is_header"));
					map.put("grade", table.getValue("grade"));
					map.put("gradeClass", table.getValue("grade_class"));
					map.put("className", table.getValue("class_name"));
					map.put("teacherName", table.getValue("name"));
					
					
					// 头像占时为null
					map.put("head", table.getValue("head"));
					
					// 班级下学生组信息
					map.put("students", students);
					
					list.add(map);
					lastId = table.getValue("id");
				} 				
				if (org.apache.commons.lang.StringUtils.isEmpty(table.getValue("student_id"))) {
					continue;
				}
				student = new HashMap<String, Object>();
				student.put("studentId", table.getValue("student_id"));
				student.put("studentName", table.getValue("student_name"));
				// 联系人电话号码
				student.put("contactsTel", table.getValue("crash_contacts"));
				student.put("memberId", table.getValue("member_id"));
				student.put("memberHead", table.getValue("head"));
				students.add(student);
			}
			ifdata.put("result", list);
			head.setRet(Constants.RET_S);
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrCode("Ex1020101");
			head.setErrMsg(config.get("Ex1020101") + e.getMessage());
		}
		return ret();
	}

}
