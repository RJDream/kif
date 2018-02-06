/**
 * Id: RelevanceStudent.java, v1.0 2015-8-18 下午3:42:51 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.user;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.aplus.utils.StringUtils;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.db.DbTable;
import com.aplus.v1.framework.exception.MessageException;
import com.aplus.v1.framework.interf.TransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestData;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@RelevanceStudent.java </p>
 * <p>Description: 
 *    用户关联学生信息接口
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-18
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF00010004", versions = "V1000",des = "用户关联学生信息接口")
public class RelevanceStudent extends TransactionInterfaceBase {
	
	// 学生信息查询SQL
	final static String QUERY_STUDENT = "SELECT id FROM k12_student s where s.school_id = '{0}' AND s.student_id = '{1}'";
	
	// 用户学生关联SQL
	final static String RELEVANCE_STUDENT = "INSERT INTO k12_student_member VALUES('{0}','{1}')";

	/* （非 Javadoc）
	 * <p>Title: execute</p>
	 * <p>Description: </p>
	 * @param tmb
	 * @return
	 * @see com.aplus.v1.framework.interf.TransactionInterfaceBase#execute(com.aplus.v1.framework.protocol.request.IFRequestMessage)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String execute(IFRequestMessage tmb) {
		IFRequestData data = getBody(tmb);
		Map<String, Object> busData = ((HashMap<String, Object>) data.get("data"));
		// 业务数据
		String schoolId = busData.get("schoolId").toString();
		String no = busData.get("number").toString();
		String memberId = busData.get("memberId").toString();
		try {
			String studentId = this.serachStudentIdBySchoolIdOrNo(schoolId, no);
			this.relStudent(studentId, memberId);
			head.setRet(Constants.RET_S);
			head.setErrMsg("成功关联学生信息！");
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrMsg(e.getMessage());
		}
		return ret();
	}
	
	/**
	 * <p>方法描述: 根据学校ID和学生学号获取学生信息 </p>
	 * @param schoolId	学校ID
	 * @param no		学生编号
	 * @return
	 * @return String
	 * 2015-8-18 下午3:56:04
	 * @throws Exception 
	 */
	protected String serachStudentIdBySchoolIdOrNo(String schoolId,String no) throws Exception{
		Object[] params = new Object[]{schoolId, no};
		DbTable dt = db.executeQuery(StringUtils.format(QUERY_STUDENT, params));
		if (dt.getRowCollection().size() == 0) {
			throw new MessageException("未找到该学校中所对应学号的学生，请核对！");
		}
		String studentId = null;
		while (dt.next()) {
			studentId = dt.getValue("id");
		}
		return studentId;
	}
	
	/**
	 * <p>方法描述: 关联学生 </p>
	 * @param studentId	关联学生ID
	 * @param memberId	用户ID
	 * @return void
	 * 2015-8-18 下午4:10:30
	 */
	protected void relStudent(String studentId,String memberId){
		Object[] params = new Object[]{studentId, memberId};
		try {
			db.executeNonQuery(StringUtils.format(RELEVANCE_STUDENT, params));
		} catch (SQLException e) {
			throw new MessageException("关联学生失败！" + e.getMessage());
		}
	}

}
