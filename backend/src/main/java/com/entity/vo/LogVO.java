package com.entity.vo;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 操作日志
 * @author ds
 * @email 1312461553@qq.com
 */
public class LogVO  implements Serializable {
    @Serial
	private static final long serialVersionUID = 1L;

    /*
     * 主键
     */
    private Long id;
    /*
     * 业务表
     */
    private String tablename;
    /*
     * 模块名称
     */
    private String module;
    /*
     * 操作类型
     */
    private String operatetype;
    /*
     * 业务ID
     */
    private Long businessid;
    /*
     * 操作人ID
     */
    private Long operatorid;
    /*
     * 操作人名称
     */
    private String operatorname;
    /*
     * 请求路径
     */
    private String requesturl;
    /*
     * 请求方法
     */
    private String requestmethod;
    /*
     * 请求IP
     */
    private String requestip;
    /*
     * 操作明细
     */
    private String detail;
    /*
     * 记录时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
    private Date addtime;
    /*
     * 用户ID
     */
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
