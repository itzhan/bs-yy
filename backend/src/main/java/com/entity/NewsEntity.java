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
 * 公告资讯
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("news")
public class NewsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 标题
     */
    @TableField(value = "`title`")
    private String title;
    /**
     * 简介
     */
    @TableField(value = "`introduction`")
    private String introduction;
    /**
     * 分类名称
     */
    @TableField(value = "`typename`")
    private String typename;
    /**
     * 发布人
     */
    @TableField(value = "`name`")
    private String name;
    /**
     * 头像
     */
    @TableField(value = "`avatar`")
    private String avatar;
    /**
     * 图片
     */
    @TableField(value = "`image`")
    private String image;
    /**
     * 内容
     */
    @TableField(value = "`content`")
    private String content;
    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date addtime;
    /**
     * 收藏数
     */
    @TableField(value = "`favoritenum`")
    private Integer favoritenum;
    /**
     * 点赞数
     */
    @TableField(value = "`likenum`")
    private Integer likenum;
    /**
     * 踩数
     */
    @TableField(value = "`stepnum`")
    private Integer stepnum;
    /**
     * 点击数
     */
    @TableField(value = "`clicknum`")
    private Integer clicknum;
    /**
     * 最后点击时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "`clicktime`")
    private Date clicktime;
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
    /**
     * 评论数
     */
    @TableField(value = "`discussnum`")
    private Integer discussnum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Integer getFavoritenum() {
        return favoritenum;
    }

    public void setFavoritenum(Integer favoritenum) {
        this.favoritenum = favoritenum;
    }

    public Integer getLikenum() {
        return likenum;
    }

    public void setLikenum(Integer likenum) {
        this.likenum = likenum;
    }

    public Integer getStepnum() {
        return stepnum;
    }

    public void setStepnum(Integer stepnum) {
        this.stepnum = stepnum;
    }

    public Integer getClicknum() {
        return clicknum;
    }

    public void setClicknum(Integer clicknum) {
        this.clicknum = clicknum;
    }

    public Date getClicktime() {
        return clicktime;
    }

    public void setClicktime(Date clicktime) {
        this.clicktime = clicktime;
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

    public Integer getDiscussnum() {
        return discussnum;
    }

    public void setDiscussnum(Integer discussnum) {
        this.discussnum = discussnum;
    }
}
