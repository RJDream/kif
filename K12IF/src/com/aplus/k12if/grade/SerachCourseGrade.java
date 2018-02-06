/**
 * Id: SerachCourseGrade.java, v1.0 2015-8-26 下午3:27:10 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.grade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aplus.utils.PagerUtils;
import com.aplus.utils.StringUtils;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.db.DbTable;
import com.aplus.v1.framework.interf.NoneTransactionInterfaceBase;
import com.aplus.v1.framework.protocol.common.IFPager;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@SerachCourseGrade.java </p>
 * <p>Description: 
 *    查询学生指定科目的考试信息
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-26
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF00040002", versions = "V1000", des = "查询学生指定科目的考试信息")
public class SerachCourseGrade extends NoneTransactionInterfaceBase {

	// 查询制定科目考试信息
//	final static String QUERY_COURSE_GRADE = "select " +
//											 "  (select d.`name` from k12_dictionary_detail d where d.`code` = A.exam_type) as type," +
//											 "  a.scoure,a.exam_date " +
//											 "from " +
//											 "  k12_achievement a " +
//											 "where  " +
//											 "  a.student_id = '{0}' and a.course = '{1}'  and a.grade_name = '{2}' and class_name = '{3}' order by a.exam_date";
//	

	final static String QUERY_COURSE_GRADE = "SELECT " +
			 "  t.id, t.score, t.`code`, t1.exam_date, t2.alias,t2.batch,t2.semester," +
			 "  ( SELECT d.`name` FROM k12_dictionary_detail d WHERE d.`code` = t2.type_code ) AS type, " +
			 "  ( SELECT d.`name` FROM k12_dictionary_detail d WHERE d.`code` = t1.code_course ) AS courseName " +
			 "from " +
			 "  k12_exam_score t " +
			 "LEFT JOIN k12_exam_task t1 ON t.exam_task_id = t1.id " +
			 "LEFT JOIN k12_exam_batch t2 ON t1.exam_batch_id = t2.id " +
			 "where  " +
			 "  t.student_id = '{0}' and t1.code_course = '{1}' order by t1.exam_date desc";

	
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

		//	AND t2.semester = '{2}'
		
		// V2 参数   学生ID/科目/学期/考试年份
		String studentId = busData.get("studentId").toString();
		String course = busData.get("course").toString();
		//String semester = busData.get("semester").toString();
		Object[] params = new Object[] {studentId, course};
		try {
			DbTable table = db.executeQuery(StringUtils.format(QUERY_COURSE_GRADE,
					params) + PagerUtils.pagingSQL(getPager(tmb)));
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			Map<String, String> map = null;
			
			while (table.next()) {
				map = new HashMap<String, String>();
				map.put("id", table.getValue("id"));
				map.put("type", table.getValue("type"));
				map.put("scoure", table.getValue("score"));
				map.put("examDate", table.getValue("exam_date"));
				map.put("alias", table.getValue("alias"));
				map.put("batch", table.getValue("batch"));
				map.put("courseName", table.getValue("courseName"));
				map.put("semester", table.getValue("semester"));
				list.add(map);
			}
			ifdata.put("result", list);
			head.setRet(Constants.RET_S);
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrMsg("查询科目考试成绩信息失败： " + e.getMessage());
		}
		return ret();
	}


}
