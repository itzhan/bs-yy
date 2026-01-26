package com.entity.vo;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 商品订单
 * @author ds
 * @email 1312461553@qq.com
 */
public class ProductorderVO  implements Serializable {
    @Serial
	private static final long serialVersionUID = 1L;

    /*
     * 订单编号
     */
    private Long id;
    /*
     * 商品名称
     */
    private String productname;
    /*
     * 商品图片
     */
    private String productimage;
    /*
     * 商品分类
     */
    private String producttype;
    /*
     * 商品单价
     */
    private Double productprice;
    /*
     * 购买数量
     */
    private Integer quantity;
    /*
     * 订单总价
     */
    private Double totalprice;
    /*
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
    private Date addtime;
    /*
     * 是否支付
     */
    private String ispay;
    /*
     * 状态
     */
    private String orderstatus;
    /*
     * 物流信息
     */
    private String logistics;
    /*
     * 评论数
     */
    private Integer discussnum;
    /*
     * 用户ID
     */
    private Long userid;
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

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype;
    }

    public Double getProductprice() {
        return productprice;
    }

    public void setProductprice(Double productprice) {
        this.productprice = productprice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(Double totalprice) {
        this.totalprice = totalprice;
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

    public Integer getDiscussnum() {
        return discussnum;
    }

    public void setDiscussnum(Integer discussnum) {
        this.discussnum = discussnum;
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
