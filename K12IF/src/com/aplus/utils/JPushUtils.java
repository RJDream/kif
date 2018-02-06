package com.aplus.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.aplus.bo.Notice;
import com.aplus.v1.framework.config.Config;

/**
 * ------------------------------------------------------------
 * <p>Title:@JPushUtils.java </p>
 * <p>Description: 
 *    激光推送辅助工具
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-10-10
 * ------------------------------------------------------------
 */
public class JPushUtils {
	
	
    
	private static String JPUSH_APP_KEY;
	private static String JPUSH_MASTER_SECRET;
	
	public static JPushClient pushClient;
	
	static Config config = Config.getInstance("K12IF");

	private static void initJpush() {
		
		JPUSH_APP_KEY = config.get(
				"jpush_app_key");
		
		JPUSH_MASTER_SECRET = config.get("jpush_master_secret");
		
		pushClient = new JPushClient(JPUSH_MASTER_SECRET,JPUSH_APP_KEY, 3, null);
	}
	
	/** 
	 * <p>方法描述: TODO 将时间按照指定的格式转换为字符串  ,formar 默认格式 yyyy-MM-dd </p>
	 * @param date 时间
	 * @param format 格式
	 * @return String
	 * 2015-8-17 下午4:26:08
	 */
	public static String convertedStringByDate(Date date, String format){
		try {
			if(StringUtils.isEmpty(format)){
				format = "yyyy-MM-dd";
			}
			return new SimpleDateFormat(format).format(date);
		} catch (Exception ex) {
			return new SimpleDateFormat(format).format(new Date());
		}
	}
	
	/**
	 * <p>方法描述: 通知类型名称转换</p>
	 * @return
	 * @return String
	 * 2015-10-10 下午3:50:40
	 */
	public static String convertedToTypeName(String typeCode){
		if (typeCode == "not_01") {
			return "班级通知";
		} else if(typeCode == "not_02") {
			return "校级通知";
		}
		return "未知类型";
	}

	/** 
	 * <p>方法描述: 为所有的用户发送推送（校级推送）</p>
	 * @param notice 消息
	 * @throws APIConnectionException 
	 * @throws APIRequestException
	 * @return boolean 推送是否发送成功
	 * 2015-8-25 下午3:19:32
	 */
	public static boolean sendNotice(Notice notice) throws APIConnectionException, APIRequestException {
		
		initJpush();
		
		StringBuilder content = new StringBuilder();
		String typeName = convertedToTypeName(notice.getNoticeTypeCode());
		content.append(notice.getTitle());
		content.append("#");
		content.append(notice.getContent());
		content.append("#");
		content.append(convertedStringByDate(new Date(), "yyyy-MM-dd"));
		content.append("#");
		content.append(typeName);
		
		
		PushPayload payload  = buildPayloadForAll(notice.getTitle(), content.toString());

		PushResult result = pushClient.sendPush(payload);

		return result.isResultOK();
	}
	
	
	
	/** 
	 * <p>方法描述: TODO 为拥有特定标签的用户发送推送（班级推送） </p>
	 * @param tag 用户标签
	 * @return boolean 推送是否发送成功
	 * 2015-8-25 下午3:20:38 
	 * @throws APIRequestException 
	 * @throws APIConnectionException 
	 */
	public static boolean sendNotice(Notice notice , String tag) throws APIConnectionException, APIRequestException{
		
		initJpush();
		
		StringBuilder content = new StringBuilder();
		String typeName = convertedToTypeName(notice.getNoticeTypeCode());
		content.append(notice.getTitle());
		content.append("#");
		content.append(notice.getContent());
		content.append("#");
		content.append(convertedStringByDate(new Date(), "yyyy-MM-dd"));
		content.append("#");
		content.append(typeName);

		PushPayload pushPayload = buildPayloadForTag(notice.getTitle(), content.toString(), tag);
		
		PushResult result = pushClient.sendPush(pushPayload);
		
		return result.isResultOK();
	}
	
	
	
	/** 
	 * <p>方法描述: 获取所有用户的 推送负载 </p>
	 * @param title   标题
	 * @param content 警告
	 * @return PushPayload
	 * 2015-8-24 下午6:21:07
	 */
	private static PushPayload buildPayloadForAll(String title, String content) {
		PushPayload payload = PushPayload
				.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(Audience.all())
				.setNotification(
						Notification
								.newBuilder()
								.setAlert(content)
								.addPlatformNotification(
										AndroidNotification.newBuilder()
												.setTitle(title).build())
								.addPlatformNotification(
										IosNotification.newBuilder()
												.incrBadge(1).build()).build())
				.build();
		return payload;
	}
	
	/** 
	 * <p>方法描述: TODO </p>
	 * @param title   标题
	 * @param content 警告
	 * @param tag
	 * @return PushPayload
	 * 2015-8-25 下午3:15:04
	 */
	private static PushPayload buildPayloadForTag(String title, String content,
			String tag) {
		PushPayload payload = PushPayload				.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(Audience.tag(tag))
				.setNotification(
						Notification
								.newBuilder()
								.setAlert(content)
								.addPlatformNotification(
										AndroidNotification.newBuilder()
												.setTitle(title).build())
								.addPlatformNotification(
										IosNotification.newBuilder()
												.incrBadge(1).build()).build())
				.build();
		return payload;
	}
	
	public static void main(String[] args){
		
		initJpush();
	}
}
