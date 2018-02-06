/**
 * Id: SerachSchoolByArae.java, v1.0 2015-8-17 下午2:42:24 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aplus.utils.StringUtils;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.db.DbTable;
import com.aplus.v1.framework.interf.NoneTransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestData;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@SerachSchoolByArae.java </p>
 * <p>Description: 
 *    根据地区查询学校
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-17
 * ------------------------------------------------------------
 */
@AutoInterface(id="IF00020001", versions = "V1000", des = "根据地区查询学校信息")
public class SerachSchoolByArae extends NoneTransactionInterfaceBase {
	
	// 根据地区查询学校
	final static String QUERY_SCHOOL = "SELECT s.id,s.school_name FROM k12_school s WHERE s.province = '{0}' AND s.city = '{1}' AND s.area = '{2}'";

	
	
	/* （非 Javadoc）
	 * <p>Title: execute</p>
	 * <p>Description: </p>
	 * @param tmb
	 * @return
	 * @see com.aplus.v1.framework.interf.NoneTransactionInterfaceBase#execute(com.aplus.v1.framework.protocol.request.IFRequestMessage)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String execute(IFRequestMessage tmb) {
		// 业务数据
		IFRequestData data = getBody(tmb);
		Map<String, Object> busData = ((HashMap<String, Object>) data.get("data"));
		
		// 前台约定数据格式为： XX省/XX市/XX区
		String addr = busData.get("addr").toString();
		
		Object[] params = (Object[])addr.split("/");
		try {
			DbTable table =	db.executeQuery(StringUtils.format(QUERY_SCHOOL, params));
			List<Map<String, String>> list = new ArrayList<Map<String,String>>();
			
			Map<String, String> map = new HashMap<String, String>();
			while (table.next()) {
				map = new HashMap<String, String>();
				map.put("id", table.getValue("id"));
				map.put("name", table.getValue("school_name"));
				list.add(map);
			}
			ifdata.put("result", list);
			head.setRet(Constants.RET_S);
			head.setErrMsg("查询学校信息成功！");
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrMsg(e.getMessage());
		}
		return ret();
	}

}
