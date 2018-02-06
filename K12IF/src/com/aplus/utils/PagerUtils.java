/**
 * Id: PagerUtils.java, v1.0 2015-10-27 下午3:10:06 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.utils;

import com.aplus.v1.framework.protocol.common.IFPager;

/**
 * ------------------------------------------------------------
 * <p>Title:@PagerUtils.java </p>
 * <p>Description: 
 *    分页字符串拼接工具
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-10-27
 * ------------------------------------------------------------
 */
public class PagerUtils {

	static String MYSQL_PAGER_SQL = " limit {0},{1} "; 
	
	/**
	 * <p>方法描述: 根据传入的分页对象，拼接分页SQL </p>
	 * @param pager
	 * @return
	 * @return String
	 * 2015-10-27 下午3:11:34
	 */
	public static String pagingSQL(IFPager pager) {
		if (pager == null) {
			return "";
		}
		int page =  pager.getPage() - 1;
		Object[] params = new Object[]{page * pager.getPageRecorders(), pager.getPageRecorders()}; 
		String sql = StringUtils.format(MYSQL_PAGER_SQL, params);
		return sql;
	}
	
}
