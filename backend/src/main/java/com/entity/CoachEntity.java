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
 * 教练
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("coach")
public class CoachEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 教练编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 账号
     */
    @TableField(value = "`coachaccount`")
    private String coachaccount;
    /**
     * 密码
     */
    @TableField(value = "`coachpassword`")
    private String coachpassword;
    /**
     * 教练姓名
     */
    @TableField(value = "`coachname`")
    private String coachname;
    /**
     * 教练头像
     */
    @TableField(value = "`coachimage`")
    private String coachimage;
    /**
     * 性别
     */
    @TableField(value = "`sex`")
    private String sex;
    /**
     * 手机号码
     */
    @TableField(value = "`phone`")
    private String phone;
    /**
     * 工号
     */
    @TableField(value = "`jobno`")
    private String jobno;
    /**
     * 擅长领域
     */
    @TableField(value = "`specialty`")
    private String specialty;
    /**
     * 教练等级
     */
    @TableField(value = "`coachlevel`")
    private String coachlevel;
    /**
     * 从业年限
     */
    @TableField(value = "`workyears`")
    private Integer workyears;
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

    public String getCoachaccount() {
        return coachaccount;
    }

    public void setCoachaccount(String coachaccount) {
        this.coachaccount = coachaccount;
    }

    public String getCoachpassword() {
        return coachpassword;
    }

    public void setCoachpassword(String coachpassword) {
        this.coachpassword = coachpassword;
    }

    public String getCoachname() {
        return coachname;
    }

    public void setCoachname(String coachname) {
        this.coachname = coachname;
    }

    public String getCoachimage() {
        return coachimage;
    }

    public void setCoachimage(String coachimage) {
        this.coachimage = coachimage;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJobno() {
        return jobno;
    }

    public void setJobno(String jobno) {
        this.jobno = jobno;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getCoachlevel() {
        return coachlevel;
    }

    public void setCoachlevel(String coachlevel) {
        this.coachlevel = coachlevel;
    }

    public Integer getWorkyears() {
        return workyears;
    }

    public void setWorkyears(Integer workyears) {
        this.workyears = workyears;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
