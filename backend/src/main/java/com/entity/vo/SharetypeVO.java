package com.entity.vo;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 交流分类
 * @author ds
 * @email 1312461553@qq.com
 */
public class SharetypeVO  implements Serializable {
    @Serial
	private static final long serialVersionUID = 1L;

    /*
     * 分类编号
     */
    private Long id;
    /*
     * 分类名称
     */
    private String sharetypename;
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
