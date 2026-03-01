package com.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 教练评价
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("coachreview")
public class CoachreviewEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
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
     * 用户头像
     */
    @TableField(value = "`userimage`")
    private String userimage;
    /**
     * 评分
     */
    @TableField(value = "`rating`")
    private Integer rating;
    /**
     * 评价内容
     */
    @TableField(value = "`content`")
    private String content;
    /**
     * 回复内容
     */
    @TableField(value = "`reply`")
    private String reply;
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

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
