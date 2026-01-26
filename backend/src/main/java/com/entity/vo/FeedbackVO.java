package com.entity.vo;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 意见反馈
 * @author ds
 * @email 1312461553@qq.com
 */
public class FeedbackVO  implements Serializable {
    @Serial
	private static final long serialVersionUID = 1L;

    /*
     * 反馈编号
     */
    private Long id;
    /*
     * 标题
     */
    private String feedbacktitle;
    /*
     * 内容
     */
    private String feedbackcontent;
    /*
     * 反馈类型
     */
    private String feedbacktype;
    /*
     * 用户ID
     */
    private Long userid;
    /*
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
    private Date addtime;
    /*
     * 审核状态
     */
    private String auditstatus;
    /*
     * 审核回复
     */
    private String auditreply;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeedbacktitle() {
        return feedbacktitle;
    }

    public void setFeedbacktitle(String feedbacktitle) {
        this.feedbacktitle = feedbacktitle;
    }

    public String getFeedbackcontent() {
        return feedbackcontent;
    }

    public void setFeedbackcontent(String feedbackcontent) {
        this.feedbackcontent = feedbackcontent;
    }

    public String getFeedbacktype() {
        return feedbacktype;
    }

    public void setFeedbacktype(String feedbacktype) {
        this.feedbacktype = feedbacktype;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getAuditstatus() {
        return auditstatus;
    }

    public void setAuditstatus(String auditstatus) {
        this.auditstatus = auditstatus;
    }

    public String getAuditreply() {
        return auditreply;
    }

    public void setAuditreply(String auditreply) {
        this.auditreply = auditreply;
    }
}
