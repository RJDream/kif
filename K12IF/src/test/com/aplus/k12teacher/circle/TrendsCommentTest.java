package test.com.aplus.k12teacher.circle;

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

public class TrendsCommentTest extends BaseInterfaceTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws Exception {
		// 准备加密业务数据
		String data = "{header:{osType:'ios'}," +
				"data:{" +
					"content:'H：我是嵌套回复的'," +
					"circleId:'297ea6c9515cef7601515cef76c80000'," +
					"commentUserId:'402981e84fdf181a014fdf24202a0032'," +
					"replyId:'297ea6c9515cef7601515cef76c80000-2-1'," +
					"sclassId:'402981e84fdf181a014fdf22a56d0001'," +
					"schoolId:'402981e84fdf1860014fdf2036360000'" +
				"}}"; 
		// 加密KEY
	
		String desKey = DESCoder.initKey();
		String encData = DESCoder.encryptBASE64(DESCoder.encrypt(data.getBytes(), desKey));
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("rid", "APP_Test");
		map.put("clientOSType", "ANDROID");
		map.put("rver", "V1000"); // 系统版本
		map.put("tid", "IF01050003"); // 接口编号
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
