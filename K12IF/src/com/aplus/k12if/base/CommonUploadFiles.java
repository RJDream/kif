/**
 * Id: CommonUploadFiles.java, v1.0 2015-10-30 下午5:04:23 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.config.Config;
import com.aplus.v1.framework.interf.NoneTransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestFile;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@CommonUploadFiles.java </p>
 * <p>Description: 
 *    通用文件上传接口
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-10-30
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF00000004", versions = "V1000", des = "查询学校年级配置的科目信息")
public class CommonUploadFiles extends NoneTransactionInterfaceBase{

	
	// 异常配置文件
	Config config = Config.getInstance("k12_ex");
	
	/* （非 Javadoc）
	 * <p>Title: execute</p>
	 * <p>Description: </p>
	 * @param tmb
	 * @return
	 * @see com.aplus.v1.framework.interf.NoneTransactionInterfaceBase#execute(com.aplus.v1.framework.protocol.request.IFRequestMessage)
	 */
	@Override
	public String execute(IFRequestMessage tmb) {
		String mark = getBusData(tmb) == null || getBusData(tmb).get("mark") == null ? "" : getBusData(tmb).get("mark").toString();
		try {
			// 获取上传的文件信息
			Map<String, IFRequestFile>  files =  tmb.getData().getFiles();
			Set<String> keys = files.keySet();
			
			Map<String, String> filesPath = new HashMap<String, String>();
			for (String key : keys) {
				IFRequestFile file = files.get(key);
				filesPath.put(key, file.getHttpPath());
			}
			// mark 为功能标示，不同功能模块上传时，文件上传路径规划方式可能不一样 
			// Coding 不同功能的文件规划 TODO
			
			ifdata.put("files", filesPath);
			head.setRet(Constants.RET_S);
			head.setErrCode("Ex000401");
			head.setErrMsg(config.get("Ex000401"));
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrCode("Ex000402");
			head.setErrMsg(config.get("Ex000402") + e.getMessage());
		}
		return ret();
	}

}
