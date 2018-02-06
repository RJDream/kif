/**
 * Id: SerachHomeWork.java, v1.0 2015-10-20 下午5:35:04 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12teacher.homework;

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
 * <p>Title:@SerachHomeWork.java </p>
 * <p>Description: 
 *    查询教师已发布的作业信息
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-10-20
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF01040002", versions = "V1000", des = "查询教师已发布的作业信息")
public class SerachHomeWork extends NoneTransactionInterfaceBase {

		// 查询老师自己发布的作业信息
		final static String QUERY_HOMEWORK = "SELECT h.id,h.class_alias,h.title,h.content,h.course,h.create_date, h.begin_date,h.finish_date FROM k12_home_work h" +
				" where h.teacher_id = '{0}' order by h.create_date desc";

		@Override
		public String execute(IFRequestMessage tmb) {
			// 业务数据
			Map<String, Object> busData = getBusData(tmb);
			// 教师Id
			String teacherId = busData.get("teacherId").toString();
			Object[] params = new Object[]{teacherId};
			try {
				DbTable table =	db.executeQuery(StringUtils.format(QUERY_HOMEWORK, params)
						 + PagerUtils.pagingSQL(getPager(tmb)));
				List<Map<String, String>> list = new ArrayList<Map<String,String>>();
				Map<String, String> map = new HashMap<String, String>();
				while (table.next()) {
					map = new HashMap<String, String>();
					map.put("id", table.getValue("id"));
					map.put("title", table.getValue("title"));
					map.put("content", table.getValue("content"));
					map.put("course", table.getValue("course"));
					map.put("createData", table.getValue("create_date"));
					map.put("beginDate", table.getValue("begin_date"));
					map.put("finishDate", table.getValue("finish_date"));
					list.add(map);
				}
				ifdata.put("result", list);
				head.setRet(Constants.RET_S);
			} catch (Exception e) {
				head.setRet(Constants.RET_E);
				head.setErrMsg("查询教师发布作业信息失败： " + e.getMessage());
			}
			return ret();
		}

}
