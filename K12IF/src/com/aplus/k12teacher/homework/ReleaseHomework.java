/**
 * Id: ReleaseHomework.java, v1.0 2015-10-8 下午12:10:59 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12teacher.homework;

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
 * <p>Title:@ReleaseHomework.java </p>
 * <p>Description: 
 *    教师发布发布
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-10-8
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF01040001", versions = "V1000", des = "发布作业")
public class ReleaseHomework extends TransactionInterfaceBase {
	
	// 异常配置文件
	Config config = Config.getInstance("k12_ex");

	// 作业添加
	final static String INSERT_HOMEWORK = "INSERT INTO k12_home_work VALUES('{0}',NOW(),NOW(),'{1}','{2}','{3}','{4}','{5}','{6}','{7}','hw_02','{8}',null,'{9}','{10}','{11}')";

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

		// 教师ID/班级ID[]/开始时间/结束日期/作业内容
		String schoolId = busData.get("schoolId").toString();
		String teacherId = busData.get("teacherId").toString();
		String classIds = busData.get("classIds").toString();
		String startDate = busData.get("startDate").toString();
		String endDate = busData.get("endDate").toString();
		String content = busData.get("content").toString();
		String subject = busData.get("subject").toString();
		
		// 次要信息: 年纪CODE/班级CODE
		String grade = busData.get("grade").toString();
		String gradeClass = busData.get("gradeClass").toString();
		String classAlias = busData.get("classAlias").toString();
		
		// 班级数组
		String[] classIdArr = classIds.split("#");
		String[] gradeArr = grade.split("#");
		String[] gradeClassArr = gradeClass.split("#");
		String[] classAliasArr = classAlias.split("#");
		
		try {
			for (int i = 0; i < classIdArr.length; i++) {
				Object[] params = new Object[12];
				params[0] = UUIDGenerator.getUUID();
				params[1] = startDate;		// 开始时间
				params[2] = content;		// 作业内容
				params[3] = subject;		// 科目CODE
				params[4] = endDate;		// 结束时间
				params[5] = gradeArr[i];	// 年纪CODE
				params[6] = gradeClassArr[i];// 班级CODE
				params[7] = schoolId;		// 学校ID
				params[8] = "作业";			// 标题
				params[9] = teacherId;		// 发布教师ID
				params[10] = classAliasArr[i];// 班级别名
				params[11] = classIdArr[i];	// 班级ID
				db.executeNonQuery(StringUtils.format(INSERT_HOMEWORK, params));
			}
			head.setRet(Constants.RET_S);
			head.setErrCode("Ex1040101");
			head.setErrMsg(config.get("Ex1040101"));
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrCode("Ex1040102");
			head.setErrMsg(config.get("Ex1040102") + ":" + e.getMessage());
		}
		
		return ret();
	}

}
