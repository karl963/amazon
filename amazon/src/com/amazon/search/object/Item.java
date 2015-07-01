package com.amazon.search.object;

import java.util.HashMap;
import java.util.Map;

public class Item {
	
	private String title, smallImageLink, mediumImageLink, detailedLink;
	private Map<String,String> otherAttributes;
	
	public Item(){
		setOtherAttributes(new HashMap<String,String>());
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getSmallImageLink() {
		return smallImageLink;
	}

	public void setSmallImageLink(String imageLink) {
		this.smallImageLink = imageLink;
	}

	public String getMediumImageLink() {
		return mediumImageLink;
	}

	public void setMediumImageLink(String imageLink) {
		this.mediumImageLink = imageLink;
	}

	public String getDetailedLink() {
		return detailedLink;
	}

	public void setDetailedLink(String detailedLink) {
		this.detailedLink = detailedLink;
	}

	public Map<String,String> getOtherAttributes() {
		return otherAttributes;
	}

	public void setOtherAttributes(Map<String,String> otherAttributes) {
		this.otherAttributes = otherAttributes;
	}
	
	
}
