/**
 * Id: SerachAllSafetyInfo.java, v1.0 2015-8-20 上午11:08:10 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.safety;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aplus.utils.StringUtils;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.db.DbTable;
import com.aplus.v1.framework.interf.NoneTransactionInterfaceBase;
import com.aplus.v1.framework.protocol.common.IFPager;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@SerachAllSafetyInfo.java </p>
 * <p>Description: 
 *    查询所有的安全信息
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-20
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF00030001", versions = "V1000", des = "查询学生安全信息接口")
public class SerachAllSafetyInfo extends NoneTransactionInterfaceBase {
	
	
	// 查询安全信息
	final static String QUERY_ALL_SAFETY = "SELECT s.am_in_date,s.am_out_date,s.pm_in_date,s.pm_out_date,s.date FROM k12_safety s " +
			"where s.student_id = '{0}' order by s.date  limit {1},{2}";

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
		IFPager pager = getPager(tmb);
		// 学生ID/获取记录数
		String studentId = busData.get("studentId").toString();
		Object[] params = new Object[]{studentId, pager.getPage() - 1, pager.getPageRecorders()};
		
		try {
			DbTable table =	db.executeQuery(StringUtils.format(QUERY_ALL_SAFETY, params));
			
			List<Map<String, String>> list = new ArrayList<Map<String,String>>();
			Map<String, String> map = new HashMap<String, String>();
			while (table.next()) {
				map = new HashMap<String, String>();
				map.put("amInDate", table.getValue("am_in_date"));
				map.put("amOutDate", table.getValue("am_out_date"));
				map.put("pmInDate", table.getValue("pm_in_date"));
				map.put("pmOutDate", table.getValue("pm_out_date"));
				map.put("date", table.getValue("date"));
				list.add(map);
			}
			ifdata.put("result", list);
			head.setRet(Constants.RET_S);
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrMsg("查询学生安全信息失败： " + e.getMessage());
		}
		return ret();
	}

}
