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
 * 在线客服
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("chat")
public class ChatEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    @TableField(value = "`userid`")
    private Long userid;
    /**
     * 管理员ID
     */
    @TableField(value = "`adminid`")
    private Long adminid;
    /**
     * 提问
     */
    @TableField(value = "`ask`")
    private String ask;
    /**
     * 回复
     */
    @TableField(value = "`reply`")
    private String reply;
    /**
     * 提问类型(1文本/2图片)
     */
    @TableField(value = "`asktype`")
    private Integer asktype;
    /**
     * 回复类型(1文本/2图片)
     */
    @TableField(value = "`replytype`")
    private Integer replytype;
    /**
     * 是否回复(0/1)
     */
    @TableField(value = "`isreply`")
    private Integer isreply;
    /**
     * 是否已读(0/1)
     */
    @TableField(value = "`isread`")
    private Integer isread;
    /**
     * 用户名
     */
    @TableField(value = "`uname`")
    private String uname;
    /**
     * 用户头像
     */
    @TableField(value = "`uimage`")
    private String uimage;
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

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getAdminid() {
        return adminid;
    }

    public void setAdminid(Long adminid) {
        this.adminid = adminid;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Integer getAsktype() {
        return asktype;
    }

    public void setAsktype(Integer asktype) {
        this.asktype = asktype;
    }

    public Integer getReplytype() {
        return replytype;
    }

    public void setReplytype(Integer replytype) {
        this.replytype = replytype;
    }

    public Integer getIsreply() {
        return isreply;
    }

    public void setIsreply(Integer isreply) {
        this.isreply = isreply;
    }

    public Integer getIsread() {
        return isread;
    }

    public void setIsread(Integer isread) {
        this.isread = isread;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUimage() {
        return uimage;
    }

    public void setUimage(String uimage) {
        this.uimage = uimage;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
