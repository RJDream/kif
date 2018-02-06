/**
 * Id: DeleteTrends.java, v1.0 2015-12-9 下午4:01:40 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12teacher.circle;

import java.util.Map;

import com.aplus.utils.StringUtils;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.config.Config;
import com.aplus.v1.framework.interf.TransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@DeleteTrends.java </p>
 * <p>Description: 
 *    删除班级动态
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-12-9
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF01050004", versions = "V1000", des = "删除班级动态接口")
public class DeleteTrends extends TransactionInterfaceBase {
	

	// 异常配置文件
	Config config = Config.getInstance("k12_ex");
	
	// 删除动态SQL
	final static String DELETE_TRENDS = "UPDATE k12_msg_circle SET is_delete = 1 WHERE id = '{0}'";

	/* （非 Javadoc）
	 * <p>Title: execute</p>
	 * <p>Description: </p>
	 * @param tmb
	 * @return
	 * @throws Exception
	 * @see com.aplus.v1.framework.interf.TransactionInterfaceBase#execute(com.aplus.v1.framework.protocol.request.IFRequestMessage)
	 */
	@Override
	public String execute(IFRequestMessage tmb) throws Exception {
		// 业务数据
		Map<String, Object> busData = getBusData(tmb);
		// id
		String id = busData.get("id").toString();
		try {
			db.executeNonQuery(StringUtils.format(DELETE_TRENDS, new Object[]{id}));
			head.setRet(Constants.RET_S);
			head.setErrCode("Ex1050401");
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrCode("Ex1050402");
			head.setErrMsg(config.get("Ex1050402") + ":" + e.getMessage());
		}
		return ret();
	}

}
