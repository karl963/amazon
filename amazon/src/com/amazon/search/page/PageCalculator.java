package com.amazon.search.page;

public class PageCalculator {

	private Page startPage, endPage;
	public static final int RESULTS_PER_PAGE = 13;
	public static final int MAX_AMAZON_RESULTS = 10;
	
	public PageCalculator(int searchPage){
		int lastItem = searchPage * RESULTS_PER_PAGE;
		int firstItem = lastItem - RESULTS_PER_PAGE;
		
		int firstItemPage = 1;
		int firstItemIndex = 1;
		if(searchPage != 1){
			firstItemPage = (int) Math.ceil((double)firstItem / (double)MAX_AMAZON_RESULTS);
			if(firstItem % MAX_AMAZON_RESULTS == 0){
				firstItemIndex = 1;
			}
			else{
				firstItemIndex = firstItem % MAX_AMAZON_RESULTS;
			}
		}

		int lastItemPage = (int) Math.ceil((double)lastItem / (double)MAX_AMAZON_RESULTS);
		int lastItemIndex;
		if(lastItem % MAX_AMAZON_RESULTS == 0){
			lastItemPage--;
			lastItemIndex = 10;
		}
		else{
			lastItemIndex = lastItem % MAX_AMAZON_RESULTS;
		}
		
		startPage = new Page(firstItemPage,firstItemIndex-1);
		endPage = new Page(lastItemPage,lastItemIndex-1);
	}

	public Page getStartPage() {
		return startPage;
	}

	public void setStartPage(Page startPage) {
		this.startPage = startPage;
	}

	public Page getEndPage() {
		return endPage;
	}

	public void setEndPage(Page endPage) {
		this.endPage = endPage;
	}
	
	
	
}
