package com.web.core.mybatis;

import java.util.List;

public class Pager {

	private int pageSize = 10;//ÿҳ��ʾ��
	private int pageNum = 1;//��ǰҳҳ��
	private int totalPage;//��ҳ��
	private int totalCount;//������
	private List listView;//��ҳ����
	
	public int getMaxRows(){
		return pageNum * pageSize;
	}
	
	public int getMinRows(){
		return (pageNum - 1) * pageSize;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public List getListView() {
		return listView;
	}
	public void setListView(List listView) {
		this.listView = listView;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		this.totalPage = 
			totalCount%pageSize == 0?totalCount/pageSize:totalCount/pageSize+1;
	}
	
}
