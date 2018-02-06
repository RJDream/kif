/**
 * Id: SerachRelStudent.java, v1.0 2015-8-26 下午5:45:35 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.user;

import java.util.HashMap;
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
 * <p>Title:@SerachRelStudent.java </p>
 * <p>Description: 
 *    查询用户所关联的学生信息
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-26
 * ------------------------------------------------------------
 */
@AutoInterface(id="IF00010006",versions="V1000",des="查询用户所关联的学生信息接口！")
public class SerachRelStudent extends NoneTransactionInterfaceBase {
	
	
	// 异常配置文件
	Config config = Config.getInstance("k12_ex");

	// 查询用户所关联学生信息
	final static String RELEVANCE_STUDENT_INFO ="select " +
													"t.id," +
													"t.school_id as school_id," +
													"(select t1.school_name from k12_school t1 where t1.id = t.school_id) as school_name," +
													"(select t4.school_code from k12_school t4 where t4.id = t.school_id) as school_code," +
													"t.student_id,t.`name`,t.sclass_id,c.class_code,c.grade_code,t.address,t.age,t.birthday,t.gender," +
													"(SELECT t2.id FROM k12_teacher t2 LEFT JOIN k12_teaching_info t3 ON t2.id = t3.teacher_id WHERE " +
													"t2.school_id = t.school_id " +
													"AND t3.is_header = '1' AND t3.sclass_id= t.sclass_id) as t_id " +
												"from " +
													"k12_student t " +
												"left join k12_school_class c on c.id = t.sclass_id " + 
												"where " +
													"t.id = (select s.students_set_id as student_id from k12_student_member s where s.member_set_id = '{0}' limit 1)";
	
	
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

		// 会员ID
		String memberId = busData.get("memberId").toString();
		
		try {
			Object[] params = new Object[] {memberId};
			DbTable table = db.executeQuery(StringUtils.format(RELEVANCE_STUDENT_INFO,
					params));
			if (table.getRowCollection().size() == 0) {
				head.setErrCode("Ex010601");
				head.setRet(Constants.RET_E);
				head.setErrMsg(config.get("Ex010601"));
				return ret();
			}
			Map<String, String> map = null;
			while (table.next()) {
				map = new HashMap<String, String>();
				map.put("id", table.getValue("student_id")); 
				map.put("schoolId", table.getValue("school_id"));
				map.put("schoolName", table.getValue("school_name"));
				map.put("schoolCode", table.getValue("school_code"));
				map.put("studentId",table.getValue("id"));
				map.put("name", table.getValue("name"));
				map.put("grade", table.getValue("grade_code"));
				map.put("bclass", table.getValue("class_code"));
				map.put("address", table.getValue("address"));
				map.put("age", table.getValue("age"));
				map.put("birthday", table.getValue("birthday"));
				map.put("gender", table.getValue("gender"));
				map.put("teacherId", table.getValue("t_id"));
				map.put("classId", table.getValue("sclass_id"));
			}
			ifdata.put("result", map);
			head.setRet(Constants.RET_S);
		} catch (Exception e) {
			head.setErrCode("Ex010602");
			head.setRet(Constants.RET_E);
			head.setErrMsg(config.get("Ex010602") + e.getMessage());
		}
		return ret();
	}

}
