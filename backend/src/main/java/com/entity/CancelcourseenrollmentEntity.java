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
 * 取消课程报名记录
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("cancelcourseenrollment")
public class CancelcourseenrollmentEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 报名编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 课程名称
     */
    @TableField(value = "`coursename`")
    private String coursename;
    /**
     * 课程封面
     */
    @TableField(value = "`courseimage`")
    private String courseimage;
    /**
     * 课程分类
     */
    @TableField(value = "`coursetype`")
    private String coursetype;
    /**
     * 上课时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "`classtime`")
    private Date classtime;
    /**
     * 课程时长
     */
    @TableField(value = "`duration`")
    private Integer duration;
    /**
     * 教练
     */
    @TableField(value = "`coachname`")
    private String coachname;
    /**
     * 用户ID
     */
    @TableField(value = "`userid`")
    private Long userid;
    /**
     * 取消原因
     */
    @TableField(value = "`cancelreason`")
    private String cancelreason;
    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date addtime;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getCourseimage() {
        return courseimage;
    }

    public void setCourseimage(String courseimage) {
        this.courseimage = courseimage;
    }

    public String getCoursetype() {
        return coursetype;
    }

    public void setCoursetype(String coursetype) {
        this.coursetype = coursetype;
    }

    public Date getClasstime() {
        return classtime;
    }

    public void setClasstime(Date classtime) {
        this.classtime = classtime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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

    public String getCancelreason() {
        return cancelreason;
    }

    public void setCancelreason(String cancelreason) {
        this.cancelreason = cancelreason;
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
