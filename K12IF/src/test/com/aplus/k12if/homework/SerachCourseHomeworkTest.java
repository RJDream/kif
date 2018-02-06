/**
 * Id: SerachTodayAllHomeworkTest.java, v1.0 2015-8-19 下午2:59:15 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package test.com.aplus.k12if.homework;

import java.util.HashMap;
import java.util.Map;

import test.BaseInterfaceTest;

import com.aplus.v1.framework.code.Coder;
import com.aplus.v1.framework.code.DESCoder;
import com.aplus.v1.framework.code.RSACoder;
import com.aplus.v1.framework.http.UrlConnUtil;
import com.aplus.v1.framework.utils.IFMessageBuilderUtil;

/**
 * ------------------------------------------------------------
 * <p>Title:@SerachCourseHomeworkTest.java </p>
 * <p>Description: 
 *    TODO 类功能描述
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-19
 * ------------------------------------------------------------
 */
public class SerachCourseHomeworkTest extends BaseInterfaceTest {

	/** 
	 * <p>方法描述: TODO(这里用一句话描述这个方法的作用) </p>
	 * @param args
	 * @return void
	 * 2015-8-19 下午2:59:15
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		SerachCourseHomeworkTest t = new SerachCourseHomeworkTest();
		// 准备加密业务数据
		String data = "{header:{osType:'ios'},data:{schoolId:'402981e84f1aac8c014f1aba4f1a0000',grade:'gr_01',gradeClass:'gc_03',course:'c_01'}}";
		
		// 加密KEY
		String desKey = DESCoder.initKey();
		String encData = DESCoder.encryptBASE64(DESCoder.encrypt(data.getBytes(), desKey));
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("rid", "APP_Test");
		map.put("clientOSType", "ANDROID");
		map.put("rver", "V1000"); // 系统版本
		map.put("tid", "IF00050002"); // 接口编号
		map.put("tver", "V1000"); // 接口版本
		map.put("enyData", encData);
		// 用户ID
		map.put("useraccount", "1");
		byte[] encodedKey = RSACoder.encryptByPublicKeyForAndroid(desKey.getBytes(), pubRSAKey);
		map.put("signature", Coder.encryptBASE64(encodedKey));
		
		// 请求
		String result = UrlConnUtil.post(interfUrl, IFMessageBuilderUtil.convertObject2Json(map));
		System.out.println("系统返回数据 = " + result);

	}

}
