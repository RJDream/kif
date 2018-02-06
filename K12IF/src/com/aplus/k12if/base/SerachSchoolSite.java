package com.aplus.k12if.base;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import com.aplus.utils.StringUtils;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.code.Coder;
import com.aplus.v1.framework.code.DESCoder;
import com.aplus.v1.framework.code.RSACoder;
import com.aplus.v1.framework.db.DbTable;
import com.aplus.v1.framework.http.UrlConnUtil;
import com.aplus.v1.framework.interf.NoneTransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestData;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;
import com.aplus.v1.framework.protocol.response.IFResponseMessage;
import com.aplus.v1.framework.utils.IFMessageBuilderUtil;
/**
 * 
 * <p>Title:@SerachSchoolSite.java </p>
 * <p>Description: 
 *    校园风采展示图片
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12IF</p>
 * @author 邹华杉<248446746@qq.com>
 * @version 1.0
 * @date 2015-12-7  下午2:14:17
 */
@AutoInterface(id="IF00000006",versions="V1000",des="校园风采展示图片")
public class SerachSchoolSite extends NoneTransactionInterfaceBase {
	
	final static String QUERY_SCHOOLSITE="select figure_show from k12_school_site AS ssite where ssite.school_id='{0}'";
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute(IFRequestMessage tmb) {
		// 业务数据
		IFRequestData data = getBody(tmb);
		Map<String, Object> ifData = ((HashMap<String, Object>) data.get("data"));
		String schoolId = ifData.get("schoolId").toString();
		Object[] params = new Object[]{schoolId};
		try {
			DbTable table =	db.executeQuery(StringUtils.format(QUERY_SCHOOLSITE, params));
			if (table.getRowCollection().size() == 0) {
				head.setRet(Constants.RET_E);
				head.setErrMsg("该学校未设置校园风采！");
			} else {
				ifdata.put("figure_show", table.getRowCollection().get(0).get("figure_show"));
				head.setRet(Constants.RET_S);
				head.setErrMsg("成功！");
			}
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrMsg(e.getMessage());
		}
		return ret();
	}

}
