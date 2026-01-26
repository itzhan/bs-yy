
package com.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 分页工具类
 */
public class PageUtils implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	//总记录数
	private long total;
	//每页记录数
	private long pageSize;
	//总页数
	private long totalPage;
	//当前页数
	private long currPage;
	//列表数据
	private List<?> list;
	
	/**
	 * 分页
	 * @param list        列表数据
	 * @param total  总记录数
	 * @param pageSize    每页记录数
	 * @param currPage    当前页数
	 */
	public PageUtils(List<?> list, int total, int pageSize, int currPage) {
		this.list = list;
		this.total = total;
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.totalPage = (int)Math.ceil((double)total/pageSize);
	}

	/**
	 * 分页
	 */
	public PageUtils(Page<?> page) {
		this.list = page.getRecords();
		this.total = page.getTotal();
		this.pageSize = page.getSize();
		this.currPage = page.getCurrent();
		this.totalPage = page.getPages();
	}
	
	/*
	 * 空数据的分页
	 */
	public PageUtils(Map<String, Object> params) {
 		Page page =new Query(params).getPage();
		new PageUtils(page);
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	public long getCurrPage() {
		return currPage;
	}

	public void setCurrPage(long currPage) {
		this.currPage = currPage;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
}
