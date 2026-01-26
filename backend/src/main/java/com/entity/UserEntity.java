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
 * 用户
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("user")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 账号
     */
    @TableField(value = "`useraccount`")
    private String useraccount;
    /**
     * 密码
     */
    @TableField(value = "`userpassword`")
    private String userpassword;
    /**
     * 姓名
     */
    @TableField(value = "`name`")
    private String name;
    /**
     * 性别
     */
    @TableField(value = "`sex`")
    private String sex;
    /**
     * 年龄
     */
    @TableField(value = "`age`")
    private Integer age;
    /**
     * 手机号码
     */
    @TableField(value = "`phone`")
    private String phone;
    /**
     * 头像
     */
    @TableField(value = "`image`")
    private String image;
    /**
     * 余额
     */
    @TableField(value = "`money`")
    private Double money;
    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date addtime;
    /**
     * 会员卡号
     */
    @TableField(value = "`cardno`")
    private String cardno;
    /**
     * 会员等级
     */
    @TableField(value = "`memberlevel`")
    private String memberlevel;
    /**
     * 会员到期时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "`expiretime`")
    private Date expiretime;
    /**
     * 剩余课时
     */
    @TableField(value = "`remainingcourses`")
    private Integer remainingcourses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUseraccount() {
        return useraccount;
    }

    public void setUseraccount(String useraccount) {
        this.useraccount = useraccount;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getMemberlevel() {
        return memberlevel;
    }

    public void setMemberlevel(String memberlevel) {
        this.memberlevel = memberlevel;
    }

    public Date getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(Date expiretime) {
        this.expiretime = expiretime;
    }

    public Integer getRemainingcourses() {
        return remainingcourses;
    }

    public void setRemainingcourses(Integer remainingcourses) {
        this.remainingcourses = remainingcourses;
    }
}
