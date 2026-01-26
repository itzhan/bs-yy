package com.entity.vo;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 课程报名记录退款
 * @author ds
 * @email 1312461553@qq.com
 */
public class RefundcourseenrollmentVO  implements Serializable {
    @Serial
	private static final long serialVersionUID = 1L;

    /*
     * 报名编号
     */
    private Long id;
    /*
     * 课程名称
     */
    private String coursename;
    /*
     * 课程封面
     */
    private String courseimage;
    /*
     * 课程分类
     */
    private String coursetype;
    /*
     * 上课时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
    private Date classtime;
    /*
     * 课程时长
     */
    private Integer duration;
    /*
     * 教练
     */
    private String coachname;
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
