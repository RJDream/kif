/**
 * Id: SerachVsersionUpdata.java, v1.0 2015-9-23 下午2:00:24 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.base;

import java.util.HashMap;
import java.util.Map;

import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.config.Config;
import com.aplus.v1.framework.interf.NoneTransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@SerachVsersionUpdata.java </p>
 * <p>Description: 
 *    版本更新查询接口
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-9-23
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF00000002", versions = "V1000", des = "版本更新查询接口")
public class SerachVsersionUpdata extends NoneTransactionInterfaceBase {

	
	// 异常配置文件
	Config config = Config.getInstance("k12_ex");
	
	// 版本更新配置文件
	Config appVersionConfig = Config.getInstance("appUpdateCheck");
	
	
	// 更新APPID集合 KEY
	static final String CHECK_IDS = "app.version.ids";
	
	// 更新检查前缀 KEY
	static final String CHECK_PREFIX = "app.info.";
	
	static final String VERSION_CODE = ".versionCode";
	
	static final String VERSION = ".version";
	
	static final String UPDATE_TYPE = ".updateType";
	
	static final String UPDATE_DESCRIPTION = ".updateDescription";
	
	static final String UPDATE_URL = ".updateUrl";
	
	static final String APP_SIZE = ".appSize";
	
		
	
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

		// app 更新ID
		String appId = busData.get("appId").toString();
		try {
			String ids = appVersionConfig.get(CHECK_IDS);
			if (ids.indexOf(appId) == -1) {
				head.setRet(Constants.RET_E);
				head.setErrCode("Ex000202");
				head.setErrMsg(config.get("Ex000202"));
				return ret();
			}
			//Map<String, Object> map = new HashMap<String, Object>();
			ifdata.put("version", appVersionConfig.get(CHECK_PREFIX + appId + VERSION));
			ifdata.put("versionCode", appVersionConfig.get(CHECK_PREFIX + appId + VERSION_CODE));
			ifdata.put("updateType", appVersionConfig.get(CHECK_PREFIX + appId + UPDATE_TYPE));
			ifdata.put("updateDescription", appVersionConfig.get(CHECK_PREFIX + appId + UPDATE_DESCRIPTION));
			ifdata.put("updateUrl", appVersionConfig.get(CHECK_PREFIX + appId + UPDATE_URL));
			ifdata.put("appSize", appVersionConfig.get(CHECK_PREFIX + appId + APP_SIZE));
			//ifdata.put("result", map);
			head.setRet(Constants.RET_S);
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrCode("Ex000201");
			head.setErrMsg(config.get("Ex000201") + "__:" + e.getMessage());
		}
		return ret();
	}

}
