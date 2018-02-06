/**
 * Id: SerachNewGrade.java, v1.0 2015-8-26 上午11:19:58 Sunshine Exp
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
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@SerachNewGrade.java </p>
 * <p>Description: 
 *    查询最新的考试情况
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-26
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF00040001", versions = "V1000", des = "查询最新的考试情况")
public class SerachNewGrade extends NoneTransactionInterfaceBase {

	// 查询最近考试成绩情况
//	final static String QUERY_NEW_GRADE = "select a.id,(select d.`name` from k12_dictionary_detail d where d.`code` = a.course) as  courseName," +
//			" (select d.`name` from k12_dictionary_detail d where d.`code` = A.exam_type) as type,a.scoure,a.exam_date from k12_achievement a " +
//			" where a.student_id = '{0}' ORDER BY a.create_date limit 5";

	
	final static String QUERY_NEW_GRADE = "SELECT t.id, t.score, t.`code`, t1.exam_date,t2.semester,( SELECT d.`name` FROM k12_dictionary_detail d WHERE" +
									" d.`code` = t1.code_course ) AS courseName, t2.alias, ( SELECT d.`name` FROM k12_dictionary_detail d WHERE " +
									" d.`code` = t2.type_code ) AS type FROM k12_exam_score t LEFT JOIN k12_exam_task t1 ON t.exam_task_id = t1.id " +
									"LEFT JOIN k12_exam_batch t2 ON t1.exam_batch_id = t2.id WHERE t.student_id = '{0}' order by t1.exam_date desc";
	
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

		// 学生ID/获取记录数
		String studentId = busData.get("studentId").toString();
		Object[] params = new Object[] { studentId };
		try {
			DbTable table = db.executeQuery(StringUtils.format(QUERY_NEW_GRADE,
					params)  + PagerUtils.pagingSQL(getPager(tmb)));
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			Map<String, String> map = null;
			while (table.next()) {
				map = new HashMap<String, String>();
				map.put("id", table.getValue("id"));
				map.put("courseName", table.getValue("courseName"));
				map.put("type", table.getValue("type"));
				map.put("scoure", table.getValue("score"));
				map.put("examDate", table.getValue("exam_date"));
				map.put("code", table.getValue("code"));
				map.put("alias", table.getValue("alias"));
				map.put("semester", table.getValue("semester"));
				list.add(map);
			}
			ifdata.put("result", list);
			head.setRet(Constants.RET_S);
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrMsg("查询最新考试成绩信息失败： " + e.getMessage());
		}
		return ret();
	}

}
