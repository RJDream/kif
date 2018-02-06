package test;

import com.aplus.v1.framework.config.Config;
import com.aplus.v1.framework.config.XmlConfig;

/**
 * ------------------------------------------------------------
 * <p>Title:@BaseInterfaceTest.java </p>
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
public class BaseInterfaceTest {
	static XmlConfig config = (XmlConfig)Config.getInstance(Config.XML + "CommonConfig");
	protected static String pubRSAKey = "";
	protected static String priRSAKey = "";
	
	
	//protected static String interfUrl = "115.29.241.86/K12IF/interfaceCall.if";
	protected static String interfUrl = "localhost:8080/K12IF/interfaceCall.if";
	
	static {
		pubRSAKey = config.get("PubRSAKey");
		pubRSAKey = pubRSAKey.replaceAll("\\\\r", "\r").replaceAll("\\\\n", "\n");
		
		priRSAKey = config.get("PriRSAKey");
		priRSAKey = priRSAKey.replaceAll("\\\\r", "\r").replaceAll("\\\\n", "\n");
		
	}
	
//	public static void main(String[] args){
//		
//		String aa = "zhansan@localhost/web";
//		
//		System.out.println(aa.split("/").length);
//	}
}
