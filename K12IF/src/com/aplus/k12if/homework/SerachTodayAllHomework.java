/**
 * Id: SerachAllHomework.java, v1.0 2015-8-19 下午12:38:05 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.homework;

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
 * <p>Title:@SerachAllHomework.java </p>
 * <p>Description: 
 *    查询当天所有科目的家庭作业
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-19
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF00050001", versions = "V1000",des = "查询当天所有科目家庭作业接口")
public class SerachTodayAllHomework extends NoneTransactionInterfaceBase {
	
	// 查询所有家庭作业
	final static String QUERY_ALL_HOMEWORK = "SELECT h.id,h.title,h.content,h.course,h.finish_date FROM k12_home_work h" +
			" where TO_DAYS(h.begin_date) = TO_DAYS(NOW()) and h.school_id = '{0}' and h.grade = '{1}' and h.grade_class = '{2}'" +
			" order by course,finish_date desc";

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
		// 学校/年纪/班级
		String schoolId = busData.get("schoolId").toString();
		String grade = busData.get("grade").toString();
		String gradeClass = busData.get("gradeClass").toString();
		
		Object[] params = new Object[]{schoolId, grade, gradeClass};
		try {
			DbTable table =	db.executeQuery(StringUtils.format(QUERY_ALL_HOMEWORK, params));
			List<Map<String, String>> list = new ArrayList<Map<String,String>>();
			Map<String, String> map = new HashMap<String, String>();
			while (table.next()) {
				map = new HashMap<String, String>();
				map.put("id", table.getValue("id"));
				map.put("title", table.getValue("title"));
				map.put("content", table.getValue("content"));
				map.put("course", table.getValue("course"));
				map.put("createData", table.getValue("create_date"));
				list.add(map);
			}
			ifdata.put("result", list);
			head.setRet(Constants.RET_S);
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrMsg("查询当天作业信息失败： " + e.getMessage());
		}
		return ret();
	}

}
