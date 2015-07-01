package com.amazon.search.page;

public class Page {
	
	private int itemIndex, pageIndex;
	
	public Page(int pageIndex,int itemIndex){
		this.pageIndex = pageIndex;
		this.itemIndex = itemIndex;
	}

	public int getItemIndex() {
		return itemIndex;
	}

	public void setItemIndex(int itemIndex) {
		this.itemIndex = itemIndex;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

}
