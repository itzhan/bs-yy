package com.entity.vo;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 会员交流
 * @author ds
 * @email 1312461553@qq.com
 */
public class ShareVO  implements Serializable {
    @Serial
	private static final long serialVersionUID = 1L;

    /*
     * 交流编号
     */
    private Long id;
    /*
     * 标题
     */
    private String sharetitle;
    /*
     * 内容
     */
    private String sharecontent;
    /*
     * 配图
     */
    private String shareimage;
    /*
     * 分类
     */
    private String sharetype;
    /*
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
    private Date addtime;
    /*
     * 收藏数
     */
    private Integer favoritenum;
    /*
     * 点赞数
     */
    private Integer likenum;
    /*
     * 踩数
     */
    private Integer stepnum;
    /*
     * 点击数
     */
    private Integer clicknum;
    /*
     * 最后点击时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
    private Date clicktime;
    /*
     * 审核状态
     */
    private String auditstatus;
    /*
     * 审核回复
     */
    private String auditreply;
    /*
     * 评论数
     */
    private Integer discussnum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSharetitle() {
        return sharetitle;
    }

    public void setSharetitle(String sharetitle) {
        this.sharetitle = sharetitle;
    }

    public String getSharecontent() {
        return sharecontent;
    }

    public void setSharecontent(String sharecontent) {
        this.sharecontent = sharecontent;
    }

    public String getShareimage() {
        return shareimage;
    }

    public void setShareimage(String shareimage) {
        this.shareimage = shareimage;
    }

    public String getSharetype() {
        return sharetype;
    }

    public void setSharetype(String sharetype) {
        this.sharetype = sharetype;
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
