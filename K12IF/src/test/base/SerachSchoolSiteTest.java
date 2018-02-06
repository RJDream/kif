/***
 * Copyright (c) 2011 aplus. All rights reserved
 *
 */
package test.base;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import test.BaseInterfaceTest;

import com.aplus.v1.framework.code.Coder;
import com.aplus.v1.framework.code.DESCoder;
import com.aplus.v1.framework.code.RSACoder;
import com.aplus.v1.framework.http.UrlConnUtil;
import com.aplus.v1.framework.protocol.response.IFResponseMessage;
import com.aplus.v1.framework.utils.IFMessageBuilderUtil;

/**
 * <p>Title:@SerachSchoolSiteTest.java </p>
 * <p>Description: 
 *    TODO 类功能描述
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12IF</p>
 * @author 邹华杉<248446746@qq.com>
 * @version 1.0
 * @date 2015-12-7  下午4:07:00
 */

public class SerachSchoolSiteTest extends BaseInterfaceTest {

	/**
	 * Test method for {@link com.aplus.k12if.base.SerachSchoolSite#execute(com.aplus.v1.framework.protocol.request.IFRequestMessage)}.
	 * @throws Exception 
	 */
	@Test
	public void testExecute() throws Exception {
		// 准备加密业务数据
		String data = "{header:{osType:'ios'},data:{schoolId:'8a22a0f2506a9f7d01506aa1e3ce0000'}}";
		
		// 加密KEY
		String desKey = DESCoder.initKey();
		String encData = DESCoder.encryptBASE64(DESCoder.encrypt(data.getBytes(), desKey));
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("rid", "APP_Test");
		map.put("clientOSType", "ANDROID");
		map.put("rver", "V1000"); // 系统版本
		map.put("tid", "IF00000006"); // 接口编号
		map.put("tver", "V1000"); // 接口版本
		map.put("enyData", encData);
		// 用户ID
		map.put("useraccount", "1");
		byte[] encodedKey = RSACoder.encryptByPublicKeyForAndroid(desKey.getBytes(), pubRSAKey);
		map.put("signature", Coder.encryptBASE64(encodedKey));
		
		System.out.println("request:" + IFMessageBuilderUtil.convertObject2Json(map));
		// 请求
		String result = UrlConnUtil.post(interfUrl, IFMessageBuilderUtil.convertObject2Json(map));
		System.out.println("response:" + result);
		IFResponseMessage responseMessage = IFResponseMessage.buildResponse(result);
		
		assertEquals("1", responseMessage.getHeader().getRet());
	}

}
