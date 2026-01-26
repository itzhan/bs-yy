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
 * 健身商品
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("product")
public class ProductEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 商品名称
     */
    @TableField(value = "`productname`")
    private String productname;
    /**
     * 商品图片
     */
    @TableField(value = "`productimage`")
    private String productimage;
    /**
     * 商品分类
     */
    @TableField(value = "`producttype`")
    private String producttype;
    /**
     * 价格
     */
    @TableField(value = "`productprice`")
    private Double productprice;
    /**
     * 库存
     */
    @TableField(value = "`stock`")
    private Integer stock;
    /**
     * 商品描述
     */
    @TableField(value = "`productdesc`")
    private String productdesc;
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getProductdesc() {
        return productdesc;
    }

    public void setProductdesc(String productdesc) {
        this.productdesc = productdesc;
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
