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
 * 操作日志
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("log")
public class LogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 业务表
     */
    @TableField(value = "`tablename`")
    private String tablename;
    /**
     * 模块名称
     */
    @TableField(value = "`module`")
    private String module;
    /**
     * 操作类型
     */
    @TableField(value = "`operatetype`")
    private String operatetype;
    /**
     * 业务ID
     */
    @TableField(value = "`businessid`")
    private Long businessid;
    /**
     * 操作人ID
     */
    @TableField(value = "`operatorid`")
    private Long operatorid;
    /**
     * 操作人名称
     */
    @TableField(value = "`operatorname`")
    private String operatorname;
    /**
     * 请求路径
     */
    @TableField(value = "`requesturl`")
    private String requesturl;
    /**
     * 请求方法
     */
    @TableField(value = "`requestmethod`")
    private String requestmethod;
    /**
     * 请求IP
     */
    @TableField(value = "`requestip`")
    private String requestip;
    /**
     * 操作明细
     */
    @TableField(value = "`detail`")
    private String detail;
    /**
     * 记录时间
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

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getOperatetype() {
        return operatetype;
    }

    public void setOperatetype(String operatetype) {
        this.operatetype = operatetype;
    }

    public Long getBusinessid() {
        return businessid;
    }

    public void setBusinessid(Long businessid) {
        this.businessid = businessid;
    }

    public Long getOperatorid() {
        return operatorid;
    }

    public void setOperatorid(Long operatorid) {
        this.operatorid = operatorid;
    }

    public String getOperatorname() {
        return operatorname;
    }

    public void setOperatorname(String operatorname) {
        this.operatorname = operatorname;
    }

    public String getRequesturl() {
        return requesturl;
    }

    public void setRequesturl(String requesturl) {
        this.requesturl = requesturl;
    }

    public String getRequestmethod() {
        return requestmethod;
    }

    public void setRequestmethod(String requestmethod) {
        this.requestmethod = requestmethod;
    }

    public String getRequestip() {
        return requestip;
    }

    public void setRequestip(String requestip) {
        this.requestip = requestip;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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
