/**
 * Id: TrendsComment.java, v1.0 2015-12-1 下午3:29:13 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12if.circle;

import java.util.Map;
import java.util.UUID;

import com.aplus.utils.StringUtils;
import com.aplus.utils.UUIDGenerator;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.config.Config;
import com.aplus.v1.framework.db.DbTable;
import com.aplus.v1.framework.interf.TransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@TrendsComment.java </p>
 * <p>Description: 
 *    家长发表评论/回复接口
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-12-1
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF00080001", versions = "V1000", des = "家长发表评论/回复接口")
public class TrendsComment extends TransactionInterfaceBase {

	
	
	// 异常配置文件
	Config config = Config.getInstance("k12_ex");
	
	// 新增动态评论
	final static String INSERT_TRENDS_COMMENT = " INSERT INTO k12_msg_circle_comments	" +
												" VALUES                                " +
												" 	(                                   " +
												" 		'{0}',                          " +
												" 		NOW(),                          " +
												" 		NOW(),                          " +
												" 		'{1}',                          " +
												" 		'{2}',                          " +
												" 		'{3}',                          " +
												" 		'{4}',                          " +
												" 		'{5}',                          " +
												" 		0,                              " +
												" 		'{6}',                          " +
												" 		'{7}'                           " +
												" 	);                                  " ;
	
	
	// 圈子评论查询
	final static String SERACH_COMMENTS = 		" SELECT						" +
												" 	t.id                        " +
												" FROM                          " +
												" 	k12_msg_circle_comments t   " +
												" WHERE                         " +
												" 	t.msg_circle_id = '{0}'     " +
												" AND t.by_reply_id = ''        " +
												" ORDER BY                      " +
												" 	t.create_date DESC          " +
												" LIMIT 0,1			            " ;
	
	// 回复评论查询
	final static String SERACH_REPLY_COMMENTS = " SELECT						" +
												" 	t.id                        " +
												" FROM                          " +
												" 	k12_msg_circle_comments t   " +
												" WHERE                         " +
												" 	t.by_reply_id = '{0}'       " +
												" ORDER BY                      " +
												" 	t.id DESC                   " +
												" LIMIT 0,1                     " ;
								
	// 评论角色为老师：01
	final static String COMMENT_ROLE_TEACHER = "02";
	
	
	
	/* （非 Javadoc）
	 * <p>Title: execute</p>
	 * <p>Description: </p>
	 * @param tmb
	 * @return
	 * @throws Exception
	 * @see com.aplus.v1.framework.interf.TransactionInterfaceBase#execute(com.aplus.v1.framework.protocol.request.IFRequestMessage)
	 */
	@Override
	public String execute(IFRequestMessage tmb) throws Exception {
		// 业务数据
		Map<String, Object> busData = getBusData(tmb);
		// 评论人id/回复ID/圈子ID/班级ID/学校ID/评论内容
		String commentUserId = busData.get("commentUserId").toString();
		String replyId = busData.get("replyId") == null ? "" : busData.get("replyId").toString();
		String circleId = busData.get("circleId").toString();
		String sclassId = busData.get("sclassId").toString();
		String schoolId = busData.get("schoolId").toString();
		String content = busData.get("content").toString();
		
		try {
			String uuid = "";
			DbTable table = null;
			// 生成评论ID
			if (replyId == "") {
				// 评论
				table = db.executeQuery(StringUtils.format(SERACH_COMMENTS, new Object[]{circleId}));
				uuid = circleId;
			} else {
				// 回复
				table = db.executeQuery(StringUtils.format(SERACH_REPLY_COMMENTS, new Object[]{replyId}));
				uuid = replyId;
			}
			if (table.getRowCollection().size() > 0) {
				String lastCommentId = table.getRowCollection().get(0).get("id");
				String[] arr = lastCommentId.split("-");
				int index = Integer.parseInt(arr[arr.length - 1]); 
				uuid += "-" + (index + 1);
			} else {
				uuid += "-1";
			}
			
			Object[] params = new Object[]{uuid ,replyId,sclassId,commentUserId,COMMENT_ROLE_TEACHER,content,circleId,schoolId};
			db.executeNonQuery(StringUtils.format(INSERT_TRENDS_COMMENT, params));
			head.setRet(Constants.RET_S);
			head.setErrCode("Ex1050301");
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrCode("Ex1050302");
			head.setErrMsg(config.get("Ex1050302") + ":" + e.getMessage());
		}
		return ret();
	}

}
