package com.aplus.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aplus.v1.framework.log.Logs;
import com.aplus.v1.framework.protocol.decode.DecodeRequestMessage;


/**
 * ------------------------------------------------------------
 * <p>Title:@StringUtils.java </p>
 * <p>Description: 
 *    字符串处理工具
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-8-13
 * ------------------------------------------------------------
 */
public class StringUtils {
	
	static Logs logs = new Logs(StringUtils.class);

	private static final Pattern fsPattern = Pattern.compile("(\\{\\d+\\})");
	private static final Pattern inPattern = Pattern.compile("(\\d+)");
	
	/**
	 * <p>方法描述: 字符串参数转换工具 </p>
	 * @param s
	 * @param params
	 * @return
	 * @return String
	 * 2015-8-13 下午4:56:09
	 */
	public static String format(String s, Object[] params) {
		StringBuffer sb = new StringBuffer();
		String str = s;
		int start = 0;
		int end = 0;
		Matcher m = fsPattern.matcher(str);
		while (m.find()) {
			int group = m.groupCount();
			for (int i = 1; i <= group; i++) {
				String g = m.group();
				Matcher m1 = inPattern.matcher(g);
				m1.find();
				String _index = m1.group(1);
				int index = Integer.valueOf(_index).intValue();
				if (index < params.length) {
					end = m.start(i);
					sb.append(str.substring(start, end));
					sb.append(params[index]);
					start = m.end(i);
				}
			}
	    }
	    sb.append(str.substring(start));
	    logs.outInfo("SQL:" + sb.toString() + "\n");
	    return sb.toString();
	}
	
	
	/**
	 * <p>方法描述: 如果是对象是NULL,则转换为空串，否则反正字符串本身 </p>
	 * @param str
	 * @return
	 * @return String
	 * 2015-12-11 上午11:34:08
	 */
	public static String isNullToEmptyStr(Object str) {
		if (str == null) {
			return "";
		}
		return str.toString();
	}
	
	public static void main(String[] args){
		Object[] params = new Object[]{1,2};
		StringUtils.format("这是 {0},这是{1}", params);
		
		System.out.println("空：" + StringUtils.isNullToEmptyStr("1"));
		
	}
	
}
