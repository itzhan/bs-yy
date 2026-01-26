package com.entity.vo;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 管理员
 * @author ds
 * @email 1312461553@qq.com
 */
public class AdminVO  implements Serializable {
    @Serial
	private static final long serialVersionUID = 1L;

    /*
     * 主键
     */
    private Long id;
    /*
     * 账号
     */
    private String adminaccount;
    /*
     * 密码
     */
    private String adminpassword;
    /*
     * 头像
     */
    private String image;
    /*
     * 角色
     */
    private String role;
    /*
     * 新增时间
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

    public String getAdminaccount() {
        return adminaccount;
    }

    public void setAdminaccount(String adminaccount) {
        this.adminaccount = adminaccount;
    }

    public String getAdminpassword() {
        return adminpassword;
    }

    public void setAdminpassword(String adminpassword) {
        this.adminpassword = adminpassword;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
