package com.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 教练-会员绑定
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("coachmember")
public class CoachmemberEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 教练ID
     */
    @TableField(value = "`coachid`")
    private Long coachid;
    /**
     * 教练账号
     */
    @TableField(value = "`coachaccount`")
    private String coachaccount;
    /**
     * 教练姓名
     */
    @TableField(value = "`coachname`")
    private String coachname;
    /**
     * 用户ID
     */
    @TableField(value = "`userid`")
    private Long userid;
    /**
     * 用户账号
     */
    @TableField(value = "`useraccount`")
    private String useraccount;
    /**
     * 用户姓名
     */
    @TableField(value = "`username`")
    private String username;
    /**
     * 手机号码
     */
    @TableField(value = "`phone`")
    private String phone;
    /**
     * 用户头像
     */
    @TableField(value = "`userimage`")
    private String userimage;
    /**
     * 绑定状态
     */
    @TableField(value = "`bindstatus`")
    private String bindstatus;
    /**
     * 备注
     */
    @TableField(value = "`remark`")
    private String remark;
    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date addtime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCoachid() {
        return coachid;
    }

    public void setCoachid(Long coachid) {
        this.coachid = coachid;
    }

    public String getCoachaccount() {
        return coachaccount;
    }

    public void setCoachaccount(String coachaccount) {
        this.coachaccount = coachaccount;
    }

    public String getCoachname() {
        return coachname;
    }

    public void setCoachname(String coachname) {
        this.coachname = coachname;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getUseraccount() {
        return useraccount;
    }

    public void setUseraccount(String useraccount) {
        this.useraccount = useraccount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getBindstatus() {
        return bindstatus;
    }

    public void setBindstatus(String bindstatus) {
        this.bindstatus = bindstatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
