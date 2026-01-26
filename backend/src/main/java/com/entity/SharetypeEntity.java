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
 * 交流分类
 * @author ds
 * @email 1312461553@qq.com
 */
@TableName("sharetype")
public class SharetypeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类编号
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 分类名称
     */
    @TableField(value = "`sharetypename`")
    private String sharetypename;
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

    public String getSharetypename() {
        return sharetypename;
    }

    public void setSharetypename(String sharetypename) {
        this.sharetypename = sharetypename;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
