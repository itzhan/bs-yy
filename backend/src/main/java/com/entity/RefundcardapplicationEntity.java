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
 * 办卡记录退款
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("refundcardapplication")
public class RefundcardapplicationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 办卡编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 套餐名称
     */
    @TableField(value = "`packagename`")
    private String packagename;
    /**
     * 套餐图片
     */
    @TableField(value = "`packageimage`")
    private String packageimage;
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
     * 有效期
     */
    @TableField(value = "`validdays`")
    private Integer validdays;
    /**
     * 包含课时
     */
    @TableField(value = "`includedcourses`")
    private Integer includedcourses;
    /**
     * 用户ID
     */
    @TableField(value = "`userid`")
    private Long userid;
    /**
     * 退款原因
     */
    @TableField(value = "`refundreason`")
    private String refundreason;
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
     * 跨表用户id
     */
    @TableField(value = "`crossuserid`")
    private Long crossuserid;
    /**
     * 跨表来源id
     */
    @TableField(value = "`crossrefid`")
    private Long crossrefid;

    public String getAuditstatus() {
        return auditstatus;
    }

    public void setAuditstatus(String auditstatus) {
        this.auditstatus = auditstatus;
    }

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

    public String getPackageimage() {
        return packageimage;
    }

    public void setPackageimage(String packageimage) {
        this.packageimage = packageimage;
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

    public Integer getValiddays() {
        return validdays;
    }

    public void setValiddays(Integer validdays) {
        this.validdays = validdays;
    }

    public Integer getIncludedcourses() {
        return includedcourses;
    }

    public void setIncludedcourses(Integer includedcourses) {
        this.includedcourses = includedcourses;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getRefundreason() {
        return refundreason;
    }

    public void setRefundreason(String refundreason) {
        this.refundreason = refundreason;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
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
