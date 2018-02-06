/**
 * Id: MemberHeadUpload.java, v1.0 2015-9-16 下午12:29:15 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.user;

import java.util.Map;
import java.util.Set;

import com.aplus.utils.StringUtils;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.config.Config;
import com.aplus.v1.framework.interf.TransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestFile;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@MemberHeadUpload.java </p>
 * <p>Description: 
 *    会员头像上传
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-9-16
 * ------------------------------------------------------------
 */
@AutoInterface(id="IF00010007",versions="V1000",des="修改用户头像接口！")
public class MemberHeadUpload extends TransactionInterfaceBase {
	
	
	// 异常配置文件
	Config config = Config.getInstance("k12_ex");
	
	// 修用户头像信息
	final static String UPDATE_HEAD = "update k12_member m set m.head = '{0}' where m.id = '{1}'";
		
	

	/* （非 Javadoc）
	 * <p>Title: execute</p>
	 * <p>Description: </p>
	 * @param tmb
	 * @return
	 * @see com.aplus.v1.framework.interf.TransactionInterfaceBase#execute(com.aplus.v1.framework.protocol.request.IFRequestMessage)
	 */
	@Override
	public String execute(IFRequestMessage tmb) {
		// 业务数据
		Map<String, Object> busData = getBusData(tmb);

		// 会员ID
		String memberId = busData.get("memberId").toString();

		// 获取上传的文件信息
		Map<String, IFRequestFile>  files =  tmb.getData().getFiles();
		Set<String> keys = files.keySet();
		for (String key : keys) {
			IFRequestFile file = files.get(key);
			System.out.println("------------ 上传文件信息 ----------");
			System.out.println("文件表单名称：" + key);
			System.out.println("文件名称：" + file.getFileName());
			System.out.println("文件类型：" + file.getContentType());
			System.out.println("文件地址：" + file.getHttpPath());
		}
		try {
			Object[] params = new Object[] {files.get("head").getHttpPath(), memberId};
			db.executeNonQuery(StringUtils.format(UPDATE_HEAD,
					params));
			head.setRet(Constants.RET_S);
			head.setErrCode("Ex010801");
			head.setErrMsg(config.get("Ex010801"));
		} catch (Exception e) {
			head.setErrCode("Ex010802");
			head.setRet(Constants.RET_E);
			head.setErrMsg(config.get("Ex010802") + e.getMessage());
		}
		return ret();
	}

}
