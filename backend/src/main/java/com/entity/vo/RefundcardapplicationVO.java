package com.entity.vo;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 办卡记录退款
 * @author ds
 * @email 1312461553@qq.com
 */
public class RefundcardapplicationVO  implements Serializable {
    @Serial
	private static final long serialVersionUID = 1L;

    /*
     * 办卡编号
     */
    private Long id;
    /*
     * 套餐名称
     */
    private String packagename;
    /*
     * 套餐图片
     */
    private String packageimage;
    /*
     * 套餐类型
     */
    private String packagetype;
    /*
     * 价格
     */
    private Double packageprice;
    /*
     * 有效期
     */
    private Integer validdays;
    /*
     * 包含课时
     */
    private Integer includedcourses;
    /*
     * 用户ID
     */
    private Long userid;
    /*
     * 退款原因
     */
    private String refundreason;
    /*
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
    private Date addtime;
    /*
     * 跨表用户id
     */
    private Long crossuserid;
    /*
     * 跨表来源id
     */
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
