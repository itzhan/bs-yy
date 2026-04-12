package com.entity.vo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 支付设置
 * @author ds
 */
public class PaymentsettingVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String payname;
    private String paycode;
    private String payicon;
    private String enabled;
    private Integer sortorder;

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
}
