package com.aplus.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.omg.CORBA.NameValuePair;

import com.aplus.v1.framework.config.Config;


/**
 * ------------------------------------------------------------
 * <p>Title:@SendSms.java </p>
 * <p>Description: 
 *    对象验证码发送接口
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-9-23
 * ------------------------------------------------------------
 */
public class SendSms {

	
	static Config config = Config.getInstance("sendsms");

	/**
	 * URL
	 */
	private static String URL = "";
	
	/**
	 * SP CODE
	 */
	private static String SPCODE = "";
	
	/**
	 * 用户名
	 */
	private static String LOGINNAME = "";
	
	/**
	 * 密码
	 */
	private static String PASSWD = "";
	
	// 返回状态
	public final static String RESULT = "result";
	
	// 返回描述
	public final static String DESCRIPTION = "description";
	
	// 任务编号 （新版）
	public final static String TASKID = "taskid";

	// 失败手机号列表
	public final static String FAIL_LIST = "faillist";
	
	// 任务编号老版本
	public final static String TASK_ID = "task_id";
	
	
	
	
	
	
	/**
	 * 读取配置文件
	 */
	static {
        try {
			URL = config.get("url");
			SPCODE = config.get("spCode");
			LOGINNAME=config.get("loginName");
			PASSWD = config.get("password");
        } catch(Exception e) {
        	e.printStackTrace();
        }
    }
	
	/**
	 * 一信通发送短信
	 * 
	 * @param msgContent
	 *            发送的内容
	 * @param phoneNumber
	 *            接受短信的电话号码数组
	 * @return 发送短信后返回的结果（result=0 & description=发送短信成功 &
	 *         taskid=22928221933&faillist=失败号码列表）
	 */
	public final static String sendMessage(String msgContent, String[] phoneNumber) {
		String messageContent = msgContent;
		StringBuffer strbuf = new StringBuffer();
		for (int i = 0; i < phoneNumber.length; i++) {
			if (phoneNumber[i] != null && phoneNumber[i].length() > 0) {
				strbuf.append(",").append(phoneNumber[i]);
			}
		}
		String userNumber = strbuf.deleteCharAt(0).toString();
		String content = "";
		HttpClient httpclient = new HttpClient();
		PostMethod post = new PostMethod(URL);//
		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "gbk");
		post.addParameter("SpCode", SPCODE);
		post.addParameter("LoginName", LOGINNAME);
		post.addParameter("Password", PASSWD);
		post.addParameter("MessageContent", messageContent);
		post.addParameter("UserNumber", userNumber);
		post.addParameter("SerialNumber", "");
		post.addParameter("ScheduleTime", "");
		post.addParameter("ExtendAccessNum", "");
		post.addParameter("f", "2");
		try {
			httpclient.executeMethod(post);
			content = new String(post.getResponseBody(),"gbk");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	/**
	 * <p>方法描述: 解析运营商返回数据 ,以map信息返回</p>
	 * @param result
	 * @return
	 * @return Map<String,String>
	 * 2015-9-24 下午2:10:54
	 */
	public static Map<String, String> parserResult(String result){
		Map<String, String> map = new HashMap<String, String>();
		String[] arr = result.split("&");
		String[] params = null;
		for (int i = 0; i < arr.length; i++) {
			params = arr[i].split("=");
			if (params.length == 1) {
				map.put(params[0], "");
				continue;
			}
			map.put(params[0], params[1]);
		}
		return map;
	}
	
}
