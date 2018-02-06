package com.aplus.bo;


/**
 * ------------------------------------------------------------
 * <p>Title:@Notice.java </p>
 * <p>Description: 
 *    通知业务对象
 * </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: 成都艾尔帕思科技有限公司</p>
 * <p>Project: K12</p>
 * @author 徐易<594642389@qq.com>
 * @version 1.0
 * @date 2015-10-10
 * ------------------------------------------------------------
 */
public class Notice {
	
	/** @Fields title : 消息标题 */
	private String title;

	/** @Fields content : 消息内容 */
	private String content;

	/** @Fields noticeTypeCode : 通知类型code  */
	private String noticeTypeCode;

	/** @Fields isrelease : 发布状态 */
	private String isrelease;

	/** @Fields pushMark : 推送标识 */
	private String pushMark;
	
	/** @Fields releaseStatus :  记录推送是否成功*/
	private boolean releaseStatus;
	
	/** @Fields pushTag : 推送标签，便于重新发送 */
	private String pushTag;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNoticeTypeCode() {
		return noticeTypeCode;
	}

	public void setNoticeTypeCode(String noticeTypeCode) {
		this.noticeTypeCode = noticeTypeCode;
	}

	public String getIsrelease() {
		return isrelease;
	}

	public void setIsrelease(String isrelease) {
		this.isrelease = isrelease;
	}

	public String getPushMark() {
		return pushMark;
	}

	public void setPushMark(String pushMark) {
		this.pushMark = pushMark;
	}

	public boolean isReleaseStatus() {
		return releaseStatus;
	}

	public void setReleaseStatus(boolean releaseStatus) {
		this.releaseStatus = releaseStatus;
	}

	public String getPushTag() {
		return pushTag;
	}

	public void setPushTag(String pushTag) {
		this.pushTag = pushTag;
	}

}
