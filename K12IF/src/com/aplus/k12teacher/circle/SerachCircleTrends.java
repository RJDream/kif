/**
 * Id: SerachCircleTrends.java, v1.0 2015-12-1 下午2:54:32 Sunshine Exp
 *
 * Copyright (c) 2012 ChinaSoft International, Ltd. All rights reserved
 *
 */
package com.aplus.k12teacher.circle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aplus.utils.PagerUtils;
import com.aplus.utils.StringUtils;
import com.aplus.v1.framework.Constants;
import com.aplus.v1.framework.annotation.AutoInterface;
import com.aplus.v1.framework.db.DbTable;
import com.aplus.v1.framework.interf.NoneTransactionInterfaceBase;
import com.aplus.v1.framework.protocol.request.IFRequestMessage;

/**
 * ------------------------------------------------------------
 * <p>Title:@SerachCircleTrends.java </p>
 * <p>Description: 
 *    查询最新的班级动态
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-12-1
 * ------------------------------------------------------------
 */
@AutoInterface(id = "IF01050002", versions = "V1000", des = "查询最新的班级动态")
public class SerachCircleTrends extends NoneTransactionInterfaceBase {
	
	
	// 查询动态
	final static String SERACHE_CIRCLES = 	" SELECT														" +
											" 	m.id,                                                       " +
											" 	m.content,                                                  " +
											" 	m.user_id,                                                  " +
											" 	m.publish_role,                                             " +
											" 	m.create_date,                                              " +
											" 	m.class_id,                                                 " +
											" 	CASE m.publish_role                                         " +
											" WHEN '01' THEN                                                " +
											" 	(SELECT t.`name` FROM k12_teacher t WHERE t.id = m.user_id) " +
											" WHEN '02' THEN                                                " +
											" 	(                                                           " +
											" 		SELECT                                                  " +
											" 			t2.`name`                                           " +
											" 		FROM                                                    " +
											" 			k12_student_member t1                               " +
											" 		LEFT JOIN k12_member t2 ON t1.students_set_id = t2.id   " +
											" 		WHERE                                                   " +
											" 			t1.member_set_id = m.user_id                        " +
											" 	)                                                           " +
											" END AS user_name,                                             " +
											" 	m.pic_url,                                                  " +
											" 	t1.alias,							                        " +
											" IF (l.id, 1, 0) AS is_like                                    " +
											" FROM                                                          " +
											" 	k12_msg_circle m                                            " +
											" LEFT JOIN k12_msg_like l ON m.id = l.msg_circle_id            " +
											" AND l.user_id = '{1}' 										" +
											" AND l.status = '1'                      						" +
											" LEFT JOIN k12_school_class t1 ON t1.id = m.class_id           " +
											" WHERE                                                         " +
											" 	m.class_id IN {0}                                           " +
											" AND m.is_delete = 0                                           " +
											" AND m.publish_type = '01'                                     " +
											" ORDER BY                                                      " +
											" 	m.create_date DESC                                          " ;
	
	final static String SERACH_TEACHER_INFO=" SELECT								" +
											" 	t.`name`,                       	" +
											" 	t.head,                         	" +
											" 	t.gender,                       	" +
											" 	t.age,                         	 	" +
											" 	t.head_teacher,                 	" +
											" 	GROUP_CONCAT(DISTINCT t1.course) as c" +
											" FROM                              	" +
											" 	k12_teacher t                   	" +
											" LEFT JOIN                        	 	" +
											" 	k12_teaching_info t1           	 	" +
											" ON t.id = t1.teacher_id           	" +
											" WHERE                             	" +
											" 	t.id = '{0}'                    	" +
											"                                   	" +
											" GROUP BY t.id                     	" ;
	
	// 查询动态回复信息
	final static String SERACHE_COMMENTS =  " SELECT														" +
											" 	t.id,                                                       " +
											" 	t.content,                                                  " +
											" 	CASE t.comment_role                                         " +
											" WHEN '01' THEN                                                " +
											" 	(                                                           " +
											" 		SELECT                                                  " +		
											" 			t.`name`                                            " +
											" 		FROM                                                    " +
											" 			k12_teacher t                                       " +
											" 		WHERE                                                   " +
											" 			t.id = t.comment_people_id                          " +
											" 	)                                                           " +
											" WHEN '02' THEN                                                " +
											" 	(                                                           " +
											" 		SELECT                                                  " +
											" 			t2.`name`                                           " +
											" 		FROM                                                    " +
											" 			k12_student_member t1                               " +
											" 		LEFT JOIN k12_student t2 ON t1.students_set_id = t2.id  " +
											" 		WHERE                                                   " +
											" 			t1.member_set_id = t.comment_people_id              " +
											" 	)                                                           " +
											" END AS reply_name,                                            " +
											"  t.comment_people_id AS rp_id,                                " +
											"  t.comment_role AS reply_role,                                " +
											"  CASE r.comment_role                                          " +
											" WHEN '01' THEN                                                " +
											" 	(                                                           " +
											" 		SELECT                                                  " +
											" 			t.`name`                                            " +
											" 		FROM                                                    " +
											" 			k12_teacher t                                       " +
											" 		WHERE                                                   " +
											" 			t.id = r.comment_people_id                          " +
											" 	)                                                           " +
											" WHEN '02' THEN                                                " +
											" 	(                                                           " +
											" 		SELECT                                                  " +
											" 			t2.`name`                                           " +
											" 		FROM                                                    " +
											" 			k12_student_member t1                               " +
											" 		LEFT JOIN k12_student t2 ON t1.students_set_id = t2.id   " +
											" 		WHERE                                                   " +
											" 			t1.member_set_id = r.comment_people_id              " +
											" 	)                                                           " +
											" END AS by_reply_name,                                         " +
											"  r.comment_people_id AS by_rp_id,                             " +
											"  r.comment_role AS by_reply_role,                             " +
											"  t.by_reply_id												" +
											" FROM                                                          " +
											" 	k12_msg_circle_comments t                                   " +
											" LEFT JOIN k12_msg_circle_comments r ON r.id = t.by_reply_id   " +
											" WHERE                                                         " +
											" 	t.msg_circle_id = '{0}'								        " +
											" AND t.is_delete = 0 									        " ;
	
	
	// 用户点赞信息查询
	final static String SERACH_USER_LIKES = " SELECT														" +
											" 	t.user_id,                                                  " +
											" 	t.publish_role,                                             " +
											" 	CASE t.publish_role                                         " +
											" WHEN '01' THEN                                                " +
											" 	(                                                           " +
											" 		SELECT                                                  " +
											" 			t.`name`                                            " +
											" 		FROM                                                    " +
											" 			k12_teacher t                                       " +
											" 		WHERE                                                   " +
											" 			t.id = t.user_id                                    " +
											" 	)                                                           " +
											" WHEN '02' THEN                                                " +
											" 	(                                                           " +
											" 		SELECT                                                  " +
											" 			t2.`name`                                           " +
											" 		FROM                                                    " +
											" 			k12_student_member t1                               " +
											" 		LEFT JOIN k12_student t2 ON t1.students_set_id = t2.id  " +
											" 		WHERE                                                   " +
											" 			t1.member_set_id = t.user_id                        " +
											" 	)                                                           " +
											" END AS like_name,                                             " +
											" 	CASE t.publish_role                                         " +
											" WHEN '01' THEN                                                " +
											" 	(                                                           " +
											" 		SELECT                                                  " +
											" 			t.head        	                                    " +
											" 		FROM                                                    " +
											" 			k12_teacher t                                       " +
											" 		WHERE                                                   " +
											" 			t.id = t.user_id                                    " +
											" 	)                                                           " +
											" WHEN '02' THEN                                                " +
											" 	(                                                           " +
											" 		SELECT                                                  " +
											" 			t1.head 	                                        " +
											" 		FROM                                                    " +
											" 			k12_member t1                               		" +
											" 		WHERE                                                   " +
											" 			t1.id = t.user_id                        			" +
											" 	)                                                           " +
											" END AS head                                		            " +
											" FROM                                                          " +
											" 	k12_msg_like t                                              " +
											" WHERE                                                         " +
											" 	t.msg_circle_id = '{0}'                                     " +
											" AND t.`status` = '1'                                          " ;

	/* （非 Javadoc）
	 * <p>Title: execute</p>
	 * <p>Description: </p>
	 * @param tmb
	 * @return
	 * @see com.aplus.v1.framework.interf.NoneTransactionInterfaceBase#execute(com.aplus.v1.framework.protocol.request.IFRequestMessage)
	 */
	@Override
	public String execute(IFRequestMessage tmb) {
		// 业务数据
		Map<String, Object> busData = getBusData(tmb);
		// 班级id 格式 id#id#id .../查询人ID
		String sclassId = busData.get("sclassId").toString();
		String userId =	StringUtils.isNullToEmptyStr(busData.get("userId"));
		String[] arr = sclassId.split("#");
		
		// 拼接 IN 查询条件
		String sclassIds = "(";
		for (int i = 0; i < arr.length; i++) {
			if (i == arr.length - 1) {
				sclassIds += "'" + arr[i] + "'";
			} else {
				sclassIds += "'" + arr[i] + "',";
			}
		}
		sclassIds += ")";
		
		try {
			Object[] params = new Object[]{sclassIds,userId};
			DbTable table =	db.executeQuery(StringUtils.format(SERACHE_CIRCLES, params) +  PagerUtils.pagingSQL(getPager(tmb)));
			
			// 班级动态
			List<Map<String, Object>> circles = new ArrayList<Map<String, Object>>();
			Map<String, Object> circle = null;
			
			// 动态评论
			DbTable commentsTable = null;
			List<Map<String, String>> comments = null;
			Map<String, String> comment;
			
			// 动态喜欢（赞数）
			DbTable likesTable = null;
			List<Map<String, String>> likes = null;
			Map<String, String> like;
			
			// 教师信息
			DbTable teacherInfoTable = null; 
			Map<String, String> teacerInfo = null;
			
			while (table.next()) {
				
				circle = new HashMap<String, Object>();
				circle.put("circleId", table.getValue("id"));
				circle.put("content", table.getValue("content"));
				circle.put("userId", table.getValue("user_id"));
				circle.put("createDate", table.getValue("create_date"));
				circle.put("sclassId", table.getValue("class_id"));
				circle.put("className", table.getValue("alias"));
				circle.put("userName", table.getValue("user_name"));
				circle.put("picUrl", table.getValue("pic_url"));
				
				// 获取动态发布人附加信息 Object TODO
				// ----任课信息/头像 等等
				if (table.getValue("publish_role").equals(PublishTrends.PUBLIS_ROLE_TEACHER)) {
					// 发布人为老师
					teacherInfoTable = db.executeQuery(StringUtils.format(SERACH_TEACHER_INFO, new Object[]{table.getValue("user_id")}));
					if (teacherInfoTable.getRowCollection().size() == 1) {
						teacerInfo = new HashMap<String, String>();
						teacherInfoTable.next();
						teacerInfo.put("name", teacherInfoTable.getValue("name"));
						teacerInfo.put("head", teacherInfoTable.getValue("head"));
						teacerInfo.put("gender", teacherInfoTable.getValue("gender"));
						teacerInfo.put("age", teacherInfoTable.getValue("age"));
						teacerInfo.put("head_teacher", teacherInfoTable.getValue("head_teacher"));
						teacerInfo.put("course", teacherInfoTable.getValue("c"));
						circle.put("teacherInfo", teacerInfo);
					}
				}
				
				// 当前查询动态的用户是否赞过本评论！ is
				circle.put("isLike", table.getValue("is_like"));
				
				// 获取赞过的人信息 List
				likesTable = db.executeQuery(StringUtils.format(SERACH_USER_LIKES, new Object[]{table.getValue("id")}));
				likes = new ArrayList<Map<String,String>>();
				while (likesTable.next()) {
					like = new HashMap<String, String>();
					like.put("userId", likesTable.getValue("user_id"));
					like.put("publishRole", likesTable.getValue("publish_role"));
					like.put("likeName", likesTable.getValue("like_name"));
					like.put("head", likesTable.getValue("head"));
					likes.add(like);
				}
				circle.put("likes", likes);
				
				// 获取评论信息
				commentsTable = db.executeQuery(StringUtils.format(SERACHE_COMMENTS, new Object[]{table.getValue("id")}));
				comments = new ArrayList<Map<String,String>>();
				while (commentsTable.next()) {
					comment = new HashMap<String, String>();
					comment.put("commentId", commentsTable.getValue("id"));
					comment.put("content", commentsTable.getValue("content"));
					// 回复人信息
					comment.put("replyName", commentsTable.getValue("reply_name"));
					comment.put("replyUserId", commentsTable.getValue("rp_id"));
					comment.put("replayRole", commentsTable.getValue("reply_role"));
					
					// 被回复人信息
					comment.put("byCommentId", commentsTable.getValue("by_reply_id"));
					comment.put("byReplyName", commentsTable.getValue("by_reply_name"));
					comment.put("byReplyUserId", commentsTable.getValue("by_rp_id"));
					comment.put("byReplayRole", commentsTable.getValue("by_reply_name"));
					
					comments.add(comment);
				}
				circle.put("comments", comments);
				circles.add(circle);
			}
			ifdata.put("result", circles);
			head.setRet(Constants.RET_S);
		} catch (Exception e) {
			head.setRet(Constants.RET_E);
			head.setErrMsg("获取班级动态失败： " + e.getMessage());
		}
		return ret();
	}

}
