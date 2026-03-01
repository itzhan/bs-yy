package com.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 训练记录
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("trainingrecord")
public class TrainingrecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 计划ID
     */
    @TableField(value = "`planid`")
    private Long planid;
    /**
     * 计划名称
     */
    @TableField(value = "`planname`")
    private String planname;
    /**
     * 用户ID
     */
    @TableField(value = "`userid`")
    private Long userid;
    /**
     * 用户账号
     */
    @TableField(value = "`useraccount`")
    private String useraccount;
    /**
     * 用户姓名
     */
    @TableField(value = "`username`")
    private String username;
    /**
     * 教练ID
     */
    @TableField(value = "`coachid`")
    private Long coachid;
    /**
     * 教练账号
     */
    @TableField(value = "`coachaccount`")
    private String coachaccount;
    /**
     * 教练姓名
     */
    @TableField(value = "`coachname`")
    private String coachname;
    /**
     * 记录时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "`recorddate`")
    private Date recorddate;
    /**
     * 训练时长(分钟)
     */
    @TableField(value = "`duration`")
    private Integer duration;
    /**
     * 完成度(%)
     */
    @TableField(value = "`completionrate`")
    private Integer completionrate;
    /**
     * 训练内容
     */
    @TableField(value = "`content`")
    private String content;
    /**
     * 教练点评
     */
    @TableField(value = "`coachcomment`")
    private String coachcomment;
    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date addtime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlanid() {
        return planid;
    }

    public void setPlanid(Long planid) {
        this.planid = planid;
    }

    public String getPlanname() {
        return planname;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getUseraccount() {
        return useraccount;
    }

    public void setUseraccount(String useraccount) {
        this.useraccount = useraccount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getCoachid() {
        return coachid;
    }

    public void setCoachid(Long coachid) {
        this.coachid = coachid;
    }

    public String getCoachaccount() {
        return coachaccount;
    }

    public void setCoachaccount(String coachaccount) {
        this.coachaccount = coachaccount;
    }

    public String getCoachname() {
        return coachname;
    }

    public void setCoachname(String coachname) {
        this.coachname = coachname;
    }

    public Date getRecorddate() {
        return recorddate;
    }

    public void setRecorddate(Date recorddate) {
        this.recorddate = recorddate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getCompletionrate() {
        return completionrate;
    }

    public void setCompletionrate(Integer completionrate) {
        this.completionrate = completionrate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoachcomment() {
        return coachcomment;
    }

    public void setCoachcomment(String coachcomment) {
        this.coachcomment = coachcomment;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
