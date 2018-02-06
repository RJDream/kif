/**
 * Id: SerachHomeworkByDate.java, v1.0 2015-11-30 上午9:53:01 Sunshine Exp
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
 * <p>Title:@SerachHomeworkByDate.java </p>
 * <p>Description: 
 *    根据日期查询家庭作业
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-11-30
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF00050003", versions = "V1000",des = "根据日期查询家庭作业")
public class SerachHomeworkByDate extends NoneTransactionInterfaceBase {

	
	// 查询当天日期的家庭作业
	final static String QUERY_HOMEWORK_BY_DATE = " SELECT								" +
												" 	h.id,                          	 	" +
												" 	h.title,                        	" +
												" 	h.content,                      	" +
												" 	h.course,                       	" +
												" 	date_format(h.create_date,'%Y-%m-%d') as create_date," +
												" 	date_format(h.begin_date,'%Y-%m-%d') as begin_date," +
												" 	date_format(h.finish_date,'%Y-%m-%d') as finish_date" +
												" FROM                              	" +
												" 	k12_home_work h                 	" +
												" WHERE                             	" +
												" 	h.sclass_id = '{0}'             	" +
												" AND '{1}' BETWEEN h.begin_date   	 	" +
												" AND h.finish_date                 	" +
							 					" ORDER BY course, finish_date DESC 	";

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
		// 班级id/查询日期，格式：YYYY-MM-DD
		String sclassId = busData.get("sclassId").toString();
		String currentDate = busData.get("currentDate").toString();
		
		Object[] params = new Object[]{sclassId, currentDate};
		try {
			DbTable table =	db.executeQuery(StringUtils.format(QUERY_HOMEWORK_BY_DATE, params));
			List<Map<String, String>> list = new ArrayList<Map<String,String>>();
			Map<String, String> map = new HashMap<String, String>();
			while (table.next()) {
				map = new HashMap<String, String>();
				map.put("id", table.getValue("id"));
				map.put("title", table.getValue("title"));
				map.put("content", table.getValue("content"));
				map.put("course", table.getValue("course"));
				map.put("createDate", table.getValue("create_date"));
				map.put("beginDate", table.getValue("begin_date"));
				map.put("finishDate", table.getValue("finish_date"));
				list.add(map);
			}
			ifdata.put("result", list);
			head.setRet(Constants.RET_S);
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrMsg("根据日期查询作业信息失败： " + e.getMessage());
		}
		return ret();
	}

}
