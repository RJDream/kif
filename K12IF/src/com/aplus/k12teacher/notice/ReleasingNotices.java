/**
 * Id: ReleasingNotices.java, v1.0 2015-9-21 下午3:08:55 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12teacher.notice;

import java.util.Map;
import java.util.UUID;

import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;

import com.aplus.bo.Notice;
import com.aplus.utils.JPushUtils;
import com.aplus.utils.StringUtils;
import com.aplus.utils.UUIDGenerator;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.config.Config;
import com.aplus.v1.framework.db.DbTable;
import com.aplus.v1.framework.exception.MessageException;
import com.aplus.v1.framework.interf.TransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@ReleasingNotices.java </p>
 * <p>Description: 
 *     教师部分班级通知
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-9-21
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF01030001", versions = "V1000", des = "教师部分班级通知")
public class ReleasingNotices extends TransactionInterfaceBase {
	
	
	// 异常配置文件
	Config config = Config.getInstance("k12_ex");
	
	// 新增通知记录
	final static String ADD_NOTICES = "INSERT INTO k12_notice VALUES('{0}',NOW(),NOW(),'{1}','{2}','{3}','{4}',NULL, 0,'{5}',null,'{6}','{7}','{8}','{9}')";

	
	final static String SERACH_TAG = "SELECT s.school_code,t1.sclass_id,t2.alias, t2.grade_code, t2.class_code FROM k12_teacher t LEFT JOIN k12_school s ON s.id = " +
			"t.school_id LEFT JOIN k12_teaching_info t1 ON t.id = t1.teacher_id LEFT JOIN k12_school_class t2 ON t2.id = t1.sclass_id WHERE t.id = '{0}' AND t1.is_header = '1'";
	
	// 发布状态：已发布
	final static String IS_RELEASE = "msg_02";
	
	// 班级通知
	final static String NOTICE_TYPE_CODE = "not_01";
	
	// 推送状态：推送
	final static String IS_PUSH_CODE01 = "np_01";
	
	// 推送状态：不推送
	final static String IS_PUSH_CODE02 = "np_02";
	
	// 分割符号
	final static String PARAM_NOTICE_OPERATOR = "_";
	
	/* （非 Javadoc）
	 * <p>Title: execute</p>
	 * <p>Description: </p>
	 * @param tmb
	 * @return
	 * @see com.aplus.v1.framework.interf.TransactionInterfaceBase#execute(com.aplus.v1.framework.protocol.request.IFRequestMessage)
	 */
	@Override
	public String execute(IFRequestMessage tmb) throws Exception {
		
		// 业务数据
		Map<String, Object> busData = getBusData(tmb);

		// 学校ID/教师ID/标题/内容/是否推送/是否发布？/
		String schoolId = busData.get("schoolId").toString();
		String teacherId = busData.get("teacherId").toString();
		String title = busData.get("title").toString();
		String content = busData.get("content").toString();
		String isPush = busData.get("isPush").toString();
		
		String id = UUIDGenerator.getUUID();
		String pushCode = null;
		Notice notice = null;
		if (isPush.equals("1")) {
			// 推送
			pushCode = IS_PUSH_CODE01;
			notice = new Notice();
			notice.setContent(content);
			notice.setTitle(title);
			notice.setNoticeTypeCode(NOTICE_TYPE_CODE);
		} else {
			pushCode = IS_PUSH_CODE02;
		}
		
		try {
			// 查询当前老师的学校编号 和 班级CODE
			DbTable table = db.executeQuery(StringUtils.format(SERACH_TAG, new Object[]{teacherId}));
			if (table.getRowCollection().size() == 0) {
				head.setRet(Constants.RET_E);
				head.setErrCode("Ex1030103");
				head.setErrMsg(config.get("Ex1030103"));
				
			}

			String classId = null;
			String classAlias = null;
			String jpushTag = "";
			while (table.next()) {
				// 学校编号 + "_" + 年级CODE + "_" + 班级CODE 组成 激光推送 tag
				jpushTag = table.getValue("school_code") 
						+ PARAM_NOTICE_OPERATOR 
						+ table.getValue("grade_code") 
						+ PARAM_NOTICE_OPERATOR 
						+ table.getValue("class_code");
				
				classId = table.getValue("sclass_id");
				classAlias = table.getValue("alias");
			}
			Object[] params = new Object[]{id, content,IS_RELEASE,NOTICE_TYPE_CODE,pushCode,title,teacherId,schoolId,classAlias,classId};
			
			// 提交
			db.executeNonQuery(StringUtils.format(ADD_NOTICES, params));
			
			// 激光推送(调用第三方插件并返回的情况下，还是要考虑事物回滚，必须得抽离出一个service层)
			if (isPush.equals("1")) { 
				try {
					//根据消息标签推送消息
					JPushUtils.sendNotice(notice, jpushTag);
				} catch (APIConnectionException e) {
					head.setErrCode("Ex1030104");
					throw new MessageException(config.get("Ex1030104"));
				} catch (APIRequestException e) {
					head.setErrCode("Ex1030105");
					throw new MessageException(config.get("Ex1030105"));
				}
			}
			head.setRet(Constants.RET_S);
			head.setErrCode("Ex1030101");
			head.setErrMsg(config.get("Ex1030101"));
		} catch (MessageException e) {
			throw e;
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrCode("Ex1030102");
			head.setErrMsg(config.get("Ex1030102") + ":" + e.getMessage());
		}
		return ret();
	}

}
