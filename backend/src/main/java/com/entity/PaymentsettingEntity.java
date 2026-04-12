package com.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * 支付设置
 * @author ds
 */
@TableName("paymentsetting")
public class PaymentsettingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "`payname`")
    private String payname;

    @TableField(value = "`paycode`")
    private String paycode;

    @TableField(value = "`payicon`")
    private String payicon;

    @TableField(value = "`enabled`")
    private String enabled;

    @TableField(value = "`sortorder`")
    private Integer sortorder;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    @TableField(value = "addtime", fill = FieldFill.INSERT)
    private Date addtime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPayname() { return payname; }
    public void setPayname(String payname) { this.payname = payname; }

    public String getPaycode() { return paycode; }
    public void setPaycode(String paycode) { this.paycode = paycode; }

    public String getPayicon() { return payicon; }
    public void setPayicon(String payicon) { this.payicon = payicon; }

    public String getEnabled() { return enabled; }
    public void setEnabled(String enabled) { this.enabled = enabled; }

    public Integer getSortorder() { return sortorder; }
    public void setSortorder(Integer sortorder) { this.sortorder = sortorder; }

    public Date getAddtime() { return addtime; }
    public void setAddtime(Date addtime) { this.addtime = addtime; }
}
