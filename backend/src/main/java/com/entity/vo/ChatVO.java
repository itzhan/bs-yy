package com.entity.vo;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 在线客服
 * @author ds
 * @email 1312461553@qq.com
 */
public class ChatVO  implements Serializable {
    @Serial
	private static final long serialVersionUID = 1L;

    /*
     * 主键
     */
    private Long id;
    /*
     * 用户ID
     */
    private Long userid;
    /*
     * 管理员ID
     */
    private Long adminid;
    /*
     * 提问
     */
    private String ask;
    /*
     * 回复
     */
    private String reply;
    /*
     * 提问类型(1文本/2图片)
     */
    private Integer asktype;
    /*
     * 回复类型(1文本/2图片)
     */
    private Integer replytype;
    /*
     * 是否回复(0/1)
     */
    private Integer isreply;
    /*
     * 是否已读(0/1)
     */
    private Integer isread;
    /*
     * 用户名
     */
    private String uname;
    /*
     * 用户头像
     */
    private String uimage;
    /*
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
    private Date addtime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getAdminid() {
        return adminid;
    }

    public void setAdminid(Long adminid) {
        this.adminid = adminid;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Integer getAsktype() {
        return asktype;
    }

    public void setAsktype(Integer asktype) {
        this.asktype = asktype;
    }

    public Integer getReplytype() {
        return replytype;
    }

    public void setReplytype(Integer replytype) {
        this.replytype = replytype;
    }

    public Integer getIsreply() {
        return isreply;
    }

    public void setIsreply(Integer isreply) {
        this.isreply = isreply;
    }

    public Integer getIsread() {
        return isread;
    }

    public void setIsread(Integer isread) {
        this.isread = isread;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUimage() {
        return uimage;
    }

    public void setUimage(String uimage) {
        this.uimage = uimage;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
