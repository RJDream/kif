/**
 * Id: SerachSingleGradeRanking.java, v1.0 2015-8-26 上午11:53:26 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.grade;

import java.util.HashMap;
import java.util.Map;

import com.aplus.utils.StringUtils;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.db.DbTable;
import com.aplus.v1.framework.interf.NoneTransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@SerachSingleGradeRanking.java </p>
 * <p>Description: 
 *    单科成绩班级/年纪排名
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-26
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF00040003", versions = "V1000", des = "查询单科成绩班级/年纪排名情况")
public class SerachSingleGradeRanking extends NoneTransactionInterfaceBase {

	// 单科目成绩班级/年纪排名查询SQL 老版本
//	final static String QUERY_RANKING = "select " +
//										"b.ranking as gradeRanking," +
//										"c.ranking as classRanking " +
//										"from" +
//										"	(select n.ranking from (select a.id,a.scoure,(@rowno:=@rowno+1) as ranking from k12_achievement a,(select (@rowno:=0)) b " +
//										"	where a.grade_name = '{0}' AND a.course = '{2}' AND a.exam_type = '{3}' ORDER BY a.scoure desc)  as n where n.id = '{4}') as b," +
//										"	(select n.ranking from (select a.id,a.scoure,(@rowno:=@rowno+1) as ranking from k12_achievement a,(select (@rowno:=0)) b " +
//										"	where a.grade_name = '{0}' and a.class_name='{1}' AND a.course = '{2}' AND a.exam_type = '{3}' ORDER BY a.scoure desc)  as n where n.id = '{4}') as c";
	
	
	// 单科目成绩班级/年纪排名查询SQL
	final static String QUERY_RANKING = "SELECT " +
										"	b.ranking AS classRanking," +
										"	(SELECT n.ranking FROM ( SELECT id, score, (@rowno :=@rowno + 1) AS ranking FROM k12_exam_score t, (SELECT(@rowno := 0)) b " + 
										" 	WHERE t.`code` LIKE ( SELECT REPLACE ( t2.`code`, SUBSTR( t2.`code`, locate('gc', t2.`code`), 5 ), '%%' ) AS c FROM k12_exam_score t2 WHERE t2.id = '{0}' ) "+
										"	AND t.exam_task_code_id LIKE ( SELECT CONCAT(SUBSTR(t2.exam_task_code_id,1,22),'%%') FROM k12_exam_score t2 WHERE t2.id = '{0}' ) ORDER BY T.score DESC ) AS n WHERE n.id = '{0}' ) AS gradeRanking " +
										"from" +
										"	(SELECT n.ranking FROM ( SELECT id, (@rowno :=@rowno + 1) AS ranking FROM k12_exam_score t, (SELECT(@rowno := 0)) b " +
										"	WHERE t.`code` IN ( SELECT t1.`code` FROM k12_exam_score t1 WHERE t1.id = '{0}') AND t.exam_task_code_id " +
										"	IN (SELECT t2.exam_task_code_id FROM k12_exam_score t2 WHERE t2.id = '{0}') ORDER BY T.score DESC) AS n WHERE n.id = '{0}') AS b";
	
	// 单科考试人数统计
	final static String QUERY_COUNT =  "SELECT " +
										"	b.classCount," +
										"	( SELECT count(*) AS gradeCount FROM k12_exam_score t WHERE t.`code` LIKE ( SELECT REPLACE ( t2.`code`, SUBSTR( t2.`code`, locate('gc', t2.`code`), 5 ), '%%' ) AS c " +
										"FROM k12_exam_score t2 WHERE t2.id = '{0}' ) AND t.exam_task_code_id LIKE ( SELECT CONCAT(SUBSTR(t2.exam_task_code_id,1,22),'%%') FROM k12_exam_score t2 WHERE t2.id = '{0}' )) AS gradeCount " + 
										"FROM ( SELECT count(*) AS classCount FROM k12_exam_score t WHERE t.`code` IN ( SELECT t1.`code` FROM k12_exam_score t1 WHERE t1.id = '{0}' ) " +
										"AND t.exam_task_code_id IN ( SELECT t2.exam_task_code_id FROM k12_exam_score t2 WHERE t2.id = '{0}' ) ORDER BY T.score DESC ) AS b";
										
	
	
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

		// 学生当前考试记录ID
		String id = busData.get("id").toString();
		Object[] params = new Object[] {id};
		try {
			// 排名查询
			DbTable table = db.executeQuery(StringUtils.format(QUERY_RANKING,
					params));
			Map<String, String> map = new HashMap<String, String>();
			while (table.next()) {
				map.put("gradeRanking", table.getValue("gradeRanking"));
				map.put("classRanking", table.getValue("classRanking"));
			}
			// 考试人数统计
			table = db.executeQuery(StringUtils.format(QUERY_COUNT,
					params));
			while (table.next()) {
				map.put("gradeCount", table.getValue("gradeCount"));
				map.put("classCount", table.getValue("classCount"));
			}
			ifdata.put("result", map);
			head.setRet(Constants.RET_S);
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrMsg("查询学生排名信息失败： " + e.getMessage());
		}
		return ret();
	}

}
