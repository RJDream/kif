/**
 * Id: UserFeedback.java, v1.0 2015-11-23 下午3:49:55 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.base;

import java.util.Map;
import java.util.UUID;

import com.aplus.utils.StringUtils;
import com.aplus.utils.UUIDGenerator;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.config.Config;
import com.aplus.v1.framework.interf.TransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@UserFeedback.java </p>
 * <p>Description: 
 *    用户意见反馈接口
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-11-23
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF00000005", versions = "V1000", des = "用户意见反馈接口")
public class UserFeedback extends TransactionInterfaceBase {

	
	// 异常配置文件
	Config config = Config.getInstance("k12_ex");
	
	// 用户反馈新增SQL
	final static String ADD_FEEDBACK = "insert INTO k12_feedback VALUES('{0}',NOW(),NOW(),NULL,'{1}','{2}','{3}','{4}','{5}')";
	
	
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

		// 反馈人ID/反馈标志/反馈内容/反馈图片地址/id地址
		String mid = busData.get("mid").toString();
		String mark = busData.get("mark").toString();
		String content = busData.get("content").toString();
		String picUrl = busData.get("picUrl") != null ? busData.get("picUrl").toString() : "";
		String ipAddress = busData.get("ipAddress") != null ? busData.get("ipAddress").toString() : "";

		try {
			Object[] params = new Object[]{UUIDGenerator.getUUID(),content, ipAddress, mark, mid, picUrl};	
			db.executeNonQuery(StringUtils.format(ADD_FEEDBACK, params));
			head.setRet(Constants.RET_S);
			head.setErrCode("Ex000501");
			head.setErrMsg(config.get("Ex000501"));
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrCode("Ex000502");
			head.setErrMsg(config.get("Ex000502"));
		}
		return ret();
	}

}
