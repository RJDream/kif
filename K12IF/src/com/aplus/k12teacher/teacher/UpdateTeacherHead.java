/***
 * Copyright (c) 2011 aplus. All rights reserved
 *
 */
package com.aplus.k12teacher.teacher;

import java.util.Map;
import com.aplus.utils.StringUtils;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.config.Config;
import com.aplus.v1.framework.interf.TransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * <p>Title:@UpdateTeacherHead.java </p>
 * <p>Description: 
 *    教师端头像修改
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12IF</p>
 * @author 邹华杉<248446746@qq.com>
 * @version 1.0
 * @date 2015-12-9  上午10:53:40
 */
@AutoInterface(id="IF01020003",versions="V1000",des="教师端头像修改")
public class UpdateTeacherHead extends TransactionInterfaceBase {
	
	// 异常配置文件
	Config config = Config.getInstance("k12_ex");
	
	final String UPDATE_HEAD="update k12_teacher t set t.head = '{0}' where t.id= '{1}' ";
	
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
		// 会员ID
		String teacherId = busData.get("teacherId").toString();
		
		String headadr = busData.get("head").toString();
		
		try {
			Object[] params = new Object[] {headadr, teacherId};
			db.executeNonQuery(StringUtils.format(UPDATE_HEAD,params));
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
