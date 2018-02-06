/**
 * Id: SerachDictionaryInfo.java, v1.0 2015-9-1 下午3:51:34 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.base;

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
 * <p>Title:@SerachDictionaryInfo.java </p>
 * <p>Description: 
 *    查询系统字典信息
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-9-1
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF00000001", versions = "V1000", des = "查询系统字典信息接口")
public class SerachDictionaryInfo extends NoneTransactionInterfaceBase {

	
	final static String REGISTER_MEMBER = "select t.`code`,t.`name` from k12_dictionary_detail t";
	
	
	/* （非 Javadoc）
	 * <p>Title: execute</p>
	 * <p>Description: </p>
	 * @param tmb
	 * @return
	 * @see com.aplus.v1.framework.interf.NoneTransactionInterfaceBase#execute(com.aplus.v1.framework.protocol.request.IFRequestMessage)
	 */
	@Override
	public String execute(IFRequestMessage tmb) {
		try {
			// 查询字典数据
			DbTable table = db.executeQuery(REGISTER_MEMBER);
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			Map<String, String> map = null;
			while (table.next()) {
				map = new HashMap<String, String>();
				map.put("code", table.getValue("code"));
				map.put("name", table.getValue("name"));
				list.add(map);
			}
			ifdata.put("result", list);
			head.setRet(Constants.RET_S);
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrMsg("查询字典信息异常：" + e.getMessage());
		}
		return ret();
	}

}
