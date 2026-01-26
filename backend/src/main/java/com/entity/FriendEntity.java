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
 * 好友表
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("friend")
public class FriendEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    @TableField(value = "`uid`")
    private Long uid;
    /**
     * 好友用户ID
     */
    @TableField(value = "`fid`")
    private Long fid;
    /**
     * 好友昵称
     */
    @TableField(value = "`name`")
    private String name;
    /**
     * 头像
     */
    @TableField(value = "`picture`")
    private String picture;
    /**
     * 角色标识
     */
    @TableField(value = "`role`")
    private String role;
    /**
     * 表名
     */
    @TableField(value = "`tablename`")
    private String tablename;
    /**
     * 好友备注
     */
    @TableField(value = "`alias`")
    private String alias;
    /**
     * 关系类型
     */
    @TableField(value = "`type`")
    private Integer type;
    /**
     * 新增时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date addtime;
    /**
     * 用户ID
     */
    @TableField(value = "`userid`")
    private Long userid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }
}
