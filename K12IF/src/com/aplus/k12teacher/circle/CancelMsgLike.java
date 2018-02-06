/***
 * Copyright (c) 2011 aplus. All rights reserved
 *
 */
package com.aplus.k12teacher.circle;

import java.util.Map;

import com.aplus.utils.StringUtils;
import com.aplus.utils.UUIDGenerator;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.config.Config;
import com.aplus.v1.framework.interf.TransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * <p>Title:@TrendsMsgLike.java </p>
 * <p>Description: 
 *    取消赞动态接口
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12IF</p>
 * @author 邹华杉<248446746@qq.com>
 * @version 1.0
 * @date 2015-12-10  下午3:18:14
 */
@AutoInterface(id = "IF01050007", versions = "V1000", des = "取消赞动态接口")
public class CancelMsgLike extends TransactionInterfaceBase {

	// 异常配置文件
	Config config = Config.getInstance("k12_ex");
	
	
	final String UPDATE_LIKE="update k12_msg_like ml set ml.status='0'  where ml.user_id = '{0}' and ml.msg_circle_id = '{1}'";
	
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
		String userid = busData.get("userId").toString();
		String circleid = busData.get("circleId").toString();
		try {
			db.executeNonQuery(StringUtils.format(UPDATE_LIKE, new Object[]{userid,circleid}));
			head.setRet(Constants.RET_S);
			head.setErrCode("Ex1050601");
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrCode("Ex1050602");
			head.setErrMsg(config.get("Ex1050602") + ":" + e.getMessage());
		}
		return ret();
	}

}
