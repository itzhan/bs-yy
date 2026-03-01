package com.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 训练计划明细
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("trainingplanitem")
public class TrainingplanitemEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 计划ID
     */
    @TableField(value = "`planid`")
    private Long planid;
    /**
     * 计划名称
     */
    @TableField(value = "`planname`")
    private String planname;
    /**
     * 用户ID
     */
    @TableField(value = "`userid`")
    private Long userid;
    /**
     * 教练ID
     */
    @TableField(value = "`coachid`")
    private Long coachid;
    /**
     * 第几天/序号
     */
    @TableField(value = "`dayno`")
    private Integer dayno;
    /**
     * 训练内容
     */
    @TableField(value = "`itemcontent`")
    private String itemcontent;
    /**
     * 目标
     */
    @TableField(value = "`target`")
    private String target;
    /**
     * 排序
     */
    @TableField(value = "`sortno`")
    private Integer sortno;
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

    public Long getPlanid() {
        return planid;
    }

    public void setPlanid(Long planid) {
        this.planid = planid;
    }

    public String getPlanname() {
        return planname;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getCoachid() {
        return coachid;
    }

    public void setCoachid(Long coachid) {
        this.coachid = coachid;
    }

    public Integer getDayno() {
        return dayno;
    }

    public void setDayno(Integer dayno) {
        this.dayno = dayno;
    }

    public String getItemcontent() {
        return itemcontent;
    }

    public void setItemcontent(String itemcontent) {
        this.itemcontent = itemcontent;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Integer getSortno() {
        return sortno;
    }

    public void setSortno(Integer sortno) {
        this.sortno = sortno;
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
