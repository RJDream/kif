/**
 * Id: ClassNotice.java, v1.0 2015-8-19 上午11:20:16 Sunshine Exp
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
 * <p>Title:@ClassNotice.java </p>
 * <p>Description: 
 *    班级通知查询接口
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-19
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF00060001", versions = "V1000", des = "班级通知查询接口")
public class ClassNotice extends NoneTransactionInterfaceBase {

	
	
	// 查询班级通知
	final static String QUERY_CLASS_NOTICE = "SELECT n.title,n.content,n.create_date FROM k12_notice n where n.notice_publisher_id = '{0}'" +
			" and n.notice_type_code = 'not_01' and n.isrelease = 'msg_02' order by create_date desc";
	
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
		String teacherId = busData.get("teacherId").toString();
		Object[] params = new Object[]{teacherId};
		try {
			DbTable table =	db.executeQuery(StringUtils.format(QUERY_CLASS_NOTICE, params) 
					+  PagerUtils.pagingSQL(getPager(tmb)));
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
