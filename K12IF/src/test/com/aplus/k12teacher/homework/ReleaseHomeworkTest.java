package test.com.aplus.k12teacher.homework;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.aplus.v1.framework.code.Coder;
import com.aplus.v1.framework.code.DESCoder;
import com.aplus.v1.framework.code.RSACoder;
import com.aplus.v1.framework.http.UrlConnUtil;
import com.aplus.v1.framework.protocol.response.IFResponseMessage;
import com.aplus.v1.framework.utils.IFMessageBuilderUtil;

import test.BaseInterfaceTest;

public class ReleaseHomeworkTest extends BaseInterfaceTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws Exception {

//		// 教师ID/班级ID[]/开始时间/结束日期/作业内容
//		String schoolId = busData.get("schoolId").toString();
//		String teacherId = busData.get("teacherId").toString();
//		String classIds = busData.get("classIdS").toString();
//		String startDate = busData.get("startDate").toString(); 
//		String endDate = busData.get("endDate").toString();
//		String content = busData.get("content").toString();
//		String subject = busData.get("subject").toString();
//		
//		// 次要信息: 年纪CODE/班级CODE
//		String grade = busData.get("grade").toString();
//		String gradeClass = busData.get("gradeClass").toString();
//		String classAlias = busData.get("classAlias").toString();
		
		
		// 加#号为同时发布两个班级的作业
		
		// 准备加密业务数据
		String data = "{header:{osType:'ios'}," +
				"data:{" +
					"schoolId:'402981e84fdf1860014fdf2036360000'," +
					"teacherId:'402981e84fdf181a014fdf24202a0032'," +
					"classIds:'402981e84fdf181a014fdf22a56d0001#402981e84fdf181a014fdf22a56d0001'," +
					"startDate:'2015-09-24'," +
					"endDate:'2015-09-25'," +
					"content:'接口工程插入的作业信息'," +
					"subject:'c_01'," +
					"grade:'gr_01#gr_01'," +
					"gradeClass:'gc_01#gc_01'," +
					"classAlias:'一年级一班#一年级一班'" +
				"}}"; 
		// 加密KEY

		String desKey = DESCoder.initKey();
		String encData = DESCoder.encryptBASE64(DESCoder.encrypt(data.getBytes(), desKey));
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("rid", "APP_Test");
		map.put("clientOSType", "ANDROID");
		map.put("rver", "V1000"); // 系统版本
		map.put("tid", "IF01040001"); // 接口编号
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
