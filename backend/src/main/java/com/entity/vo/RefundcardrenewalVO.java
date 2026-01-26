package com.entity.vo;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 续卡记录退款
 * @author ds
 * @email 1312461553@qq.com
 */
public class RefundcardrenewalVO  implements Serializable {
    @Serial
	private static final long serialVersionUID = 1L;

    /*
     * 续卡编号
     */
    private Long id;
    /*
     * 套餐名称
     */
    private String packagename;
    /*
     * 套餐类型
     */
    private String packagetype;
    /*
     * 价格
     */
    private Double packageprice;
    /*
     * 续费时长
     */
    private Integer renewaldays;
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
