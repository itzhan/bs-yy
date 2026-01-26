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
 * 续卡记录
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("cardrenewal")
public class CardrenewalEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 续卡编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 套餐名称
     */
    @TableField(value = "`packagename`")
    private String packagename;
    /**
     * 套餐类型
     */
    @TableField(value = "`packagetype`")
    private String packagetype;
    /**
     * 价格
     */
    @TableField(value = "`packageprice`")
    private Double packageprice;
    /**
     * 续费时长
     */
    @TableField(value = "`renewaldays`")
    private Integer renewaldays;
    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date addtime;
    /**
     * 是否支付
     */
    @TableField(value = "`ispay`")
    private String ispay;
    /**
     * 状态
     */
    @TableField(value = "`orderstatus`")
    private String orderstatus;
    /**
     * 物流信息
     */
    @TableField(value = "`logistics`")
    private String logistics;
    /**
     * 用户ID
     */
    @TableField(value = "`userid`")
    private Long userid;
    /**
     * 跨表用户id
     */
    @TableField(value = "`crossuserid`")
    private Long crossuserid;
    /**
     * 跨表来源id
     */
    @TableField(value = "`crossrefid`")
    private Long crossrefid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getPackagetype() {
        return packagetype;
    }

    public void setPackagetype(String packagetype) {
        this.packagetype = packagetype;
    }

    public Double getPackageprice() {
        return packageprice;
    }

    public void setPackageprice(Double packageprice) {
        this.packageprice = packageprice;
    }

    public Integer getRenewaldays() {
        return renewaldays;
    }

    public void setRenewaldays(Integer renewaldays) {
        this.renewaldays = renewaldays;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getIspay() {
        return ispay;
    }

    public void setIspay(String ispay) {
        this.ispay = ispay;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getLogistics() {
        return logistics;
    }

    public void setLogistics(String logistics) {
        this.logistics = logistics;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getCrossuserid() {
        return crossuserid;
    }

    public void setCrossuserid(Long crossuserid) {
        this.crossuserid = crossuserid;
    }

    public Long getCrossrefid() {
        return crossrefid;
    }

    public void setCrossrefid(Long crossrefid) {
        this.crossrefid = crossrefid;
    }
}
