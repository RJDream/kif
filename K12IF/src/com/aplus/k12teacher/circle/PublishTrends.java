/**
 * Id: PublishTrends.java, v1.0 2015-12-1 上午11:32:17 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12teacher.circle;

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
 * <p>Title:@PublishTrends.java </p>
 * <p>Description: 
 *    教师发布班级动态（相册）
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-12-1
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF01050001", versions = "V1000", des = "教师发布班级动态（相册）")
public class PublishTrends extends TransactionInterfaceBase {

	// 异常配置文件
	Config config = Config.getInstance("k12_ex");
	
	// 新增消息圈
	final static String INSERT_MSG_CIRCLE = " INSERT INTO k12_msg_circle	" +
											" VALUES                        " +
											" 	(                           " +
											" 		'{0}',                  " +
											" 		NOW(),                  " +
											" 		NOW(),                  " +
											" 		'{1}',                  " +
											" 		'{2}',                  " +
											" 		0,                      " +
											" 		'{3}',                  " +
											" 		'{4}',                  " +
											" 		'{5}',                  " +
											" 		'{6}',                  " +
											" 		'{7}'                   " +
											" 	)                           " ;;
	
	// 发布角色为老师：01
	final static String PUBLIS_ROLE_TEACHER = "01";
	
	// 圈子类型为班级相册：01
	final static String CIRCLE_TYPE_CLASS_ALBUM = "01";
	
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
		//发布内容/发布图片/发布ID/发布班级/发布学校
		String content = busData.get("content").toString();
		String picUrl = busData.get("picUrl").toString();
		String userId = busData.get("userId").toString();
		String sclassId = busData.get("sclassId").toString();
		String schoolId = busData.get("schoolId").toString();
		
		try {
			Object[] params = new Object[]{UUIDGenerator.getUUID().toString(),sclassId,content,picUrl,PUBLIS_ROLE_TEACHER, CIRCLE_TYPE_CLASS_ALBUM,schoolId,userId};
			db.executeNonQuery(StringUtils.format(INSERT_MSG_CIRCLE, params));
			head.setRet(Constants.RET_S);
			head.setErrCode("Ex1050101");
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrCode("Ex1050102");
			head.setErrMsg(config.get("Ex1050102") + ":" + e.getMessage());
		}
		return ret();
	}

}
