package com.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 意见反馈
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("feedback")
public class FeedbackEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 反馈编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 标题
     */
    @TableField(value = "`feedbacktitle`")
    private String feedbacktitle;
    /**
     * 内容
     */
    @TableField(value = "`feedbackcontent`")
    private String feedbackcontent;
    /**
     * 反馈类型
     */
    @TableField(value = "`feedbacktype`")
    private String feedbacktype;
    /**
     * 用户ID
     */
    @TableField(value = "`userid`")
    private Long userid;
    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date addtime;
    /**
     * 审核状态
     */
    @TableField(value = "`auditstatus`")
    private String auditstatus;
    /**
     * 审核回复
     */
    @TableField(value = "`auditreply`")
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
