/**
 * Id: SchoolNotice.java, v1.0 2015-8-19 下午12:14:00 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.notice;

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
 * <p>Title:@SchoolNotice.java </p>
 * <p>Description: 
 *   
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-19
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF00060002", versions = "V1000", des = "学校通知查询接口")
public class SchoolNotice extends NoneTransactionInterfaceBase {
	
	// 查询 学校通知
	final static String QUERY_SCHOOL_NOTICE = "SELECT n.title,n.content,n.create_date FROM k12_notice n where n.notice_school_id = '{0}'" +
				" and n.notice_type_code = 'not_02' and n.isrelease = 'msg_02' order by create_date desc";
	

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
		// 班主任ID
		String schoolId = busData.get("schoolId").toString();
		Object[] params = new Object[]{schoolId};
		try {
			DbTable table =	db.executeQuery(StringUtils.format(QUERY_SCHOOL_NOTICE, params)
					 + PagerUtils.pagingSQL(getPager(tmb)));
			List<Map<String, String>> list = new ArrayList<Map<String,String>>();
			Map<String, String> map = new HashMap<String, String>();
			while (table.next()) {
				map = new HashMap<String, String>();
				map.put("title", table.getValue("title"));
				map.put("content", table.getValue("content"));
				map.put("createData", table.getValue("create_date"));
				list.add(map);
			}
			ifdata.put("result", list);
			head.setRet(Constants.RET_S);
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrMsg("查询学校通知失败： " + e.getMessage());
		}
		return ret();
	}

}
