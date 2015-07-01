package com.amazon.search;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.amazon.search.object.Item;
import com.amazon.search.page.PageCalculator;

public class RequestXmlJson {
	
	private final String urlBase = "http://webservices.amazon.co.uk/onca/xml?";
	private String json;
	private boolean error = false;
	
	public RequestXmlJson(String accessID, String secret, String keywords, int page){
	    List<Item> items;
	    
		if(page == 1){
		    items = getItems(accessID,secret,keywords,1);
		    items.addAll(getItems(accessID,secret,keywords,2));
		}
		else{
		    items = getItems(accessID,secret,keywords,page);
		}
		
	    setJson(makeJSONFromItems(items));
	}
	
	private String makeJSONFromItems(List<Item> items){
		String json = "{\"items\":[";
		
        for(Item i : items){
        	json += "{";
        	json += "\"detailedLink\":\""+i.getDetailedLink()+"\",";
        	json += "\"smallImageLink\":\""+i.getSmallImageLink()+"\",";
        	json += "\"mediumImageLink\":\""+i.getMediumImageLink()+"\",";
        	json += "\"title\":\""+i.getTitle().replaceAll("\"", "\'")+"\",";
        	json += "\"attributes\":{";
        	
    		Iterator<Entry<String, String>> it = i.getOtherAttributes().entrySet().iterator();
    	    while (it.hasNext()) {
        		Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
    	    	json += "\""+pair.getKey().replaceAll("\"", "\'")+"\":\""+pair.getValue().replaceAll("\"", "\'")+"\",";
    	    }
        	
    	    json = json.substring(0, json.length()-1);
        	json += "}";

        	json += "},";
        }
        
        if(items.size() != 0){
        	json = json.substring(0, json.length()-1);
        }

        json += "]}";
        
        return json;
	}
	
	private List<Item> getItems(String accessID,String secret
			, String keywords, int page){
		List<Item> items = new ArrayList<Item>();
		
		PageCalculator pageCalculator = new PageCalculator(page);

		for(int currentPage = pageCalculator.getStartPage().getPageIndex();
				currentPage <= pageCalculator.getEndPage().getPageIndex();
				currentPage++){
			try{
				String time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
				
				Map<String,String> parameters = new HashMap<String,String>();
				parameters.put("Service", "AWSECommerceService");
				parameters.put("AWSAccessKeyId", accessID);
				parameters.put("Operation", "ItemSearch");
				parameters.put("Keywords", URLEncoder.encode(keywords, "UTF-8")
						.replaceAll("\\+", "%20"));
				parameters.put("SearchIndex", "All");
				parameters.put("ResponseGroup", "Medium");
				parameters.put("Version", "2013-08-01");
				parameters.put("AssociateTag", "aztag-20");
				parameters.put("ItemPage", String.valueOf(currentPage));
				parameters.put("Timestamp", URLEncoder.encode(time, "UTF-8"));
	
				String[] sorted = sortASCII(parameters);
				String canonical = makeCanonical(sorted);
				String signature = HmacSHA256(canonical,secret);
				String url = makeURLFromArray(sorted)+"Signature="+signature;
	
			    BufferedReader in = new BufferedReader(
			                            new InputStreamReader(
			                            		new URL(url).openStream()));
			    String inputLine;
				StringBuffer response = new StringBuffer();
			    while ((inputLine = in.readLine()) != null){
			        response.append(inputLine);
			    }
			    in.close();

				if(currentPage == pageCalculator.getStartPage().getPageIndex()){
					items.addAll(makeItemsFromXML(response.toString(),pageCalculator.getStartPage().getItemIndex(),9));
				}
				else if(currentPage == pageCalculator.getEndPage().getPageIndex()){
					items.addAll(makeItemsFromXML(response.toString(),0,pageCalculator.getEndPage().getItemIndex()));
				}
				else{
					items.addAll(makeItemsFromXML(response.toString(),0,9));
				}
			}catch(Exception x){
				error = true;
				x.printStackTrace();
			}
		}
		
		return items;
	}
	
	private List<Item> makeItemsFromXML(String xml,int startIndex,int endIndex){
		List<Item> items = new ArrayList<Item>();
		Document doc = parseXML(xml);
		NodeList xmlItems = doc.getElementsByTagName("Item");
		
		for(int i = startIndex;i <= endIndex;i++){
			if(xmlItems.item(i) == null){
				break;
			}
			
			NodeList itemNodes = xmlItems.item(i).getChildNodes();
			
			Item item = new Item();
			
			for(int x = 0; x < itemNodes.getLength(); x++){
				if(itemNodes.item(x).getNodeName().equals("DetailPageURL")){
					item.setDetailedLink(itemNodes.item(x).getTextContent());
				}
				else if(itemNodes.item(x).getNodeName().equals("MediumImage")){
					NodeList childs = itemNodes.item(x).getChildNodes();
					for(int c = 0;c < childs.getLength();c++){
						if(childs.item(c).getNodeName().equals("URL")){
							item.setMediumImageLink(childs.item(c).getTextContent());
							break;
						}
					}
				}
				else if(itemNodes.item(x).getNodeName().equals("SmallImage")){
					NodeList childs = itemNodes.item(x).getChildNodes();
					for(int c = 0;c < childs.getLength();c++){
						if(childs.item(c).getNodeName().equals("URL")){
							item.setSmallImageLink(childs.item(c).getTextContent());
							break;
						}
					}
				}
				else if(itemNodes.item(x).getNodeName().equals("ItemAttributes")){
					NodeList childs = itemNodes.item(x).getChildNodes();
					for(int c = 0;c < childs.getLength();c++){
						if(childs.item(c).getNodeName().equals("Title")){
							item.setTitle(childs.item(c).getTextContent());
						}
						else{
							if(childs.item(c).getChildNodes().getLength() == 1){
								item.getOtherAttributes().put(childs.item(c).getNodeName(),childs.item(c).getTextContent());
							}
						}
					}
				}
			}
			
			items.add(item);
		}
		return items;
	}
	
	private Document parseXML(String xml){
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
			doc.getDocumentElement().normalize();
			return doc;
		}catch(Exception x){
			x.printStackTrace();
		}
		return null;
	}
	
	private String makeURLFromArray(String[] array){
		String newURL = urlBase;
		for(String s : array){
			newURL+=s+"&";
		}
		return newURL;
	}
	
	public String[] sortASCII(Map<String,String> map){
		String[] sorted = new String[map.size()];
		int x = 0;
		
		while(map.size() > 0){
			String lowestKey = getLowestASCII(map);
		    sorted[x] = lowestKey+"="+map.get(lowestKey);
		    x++;
		    map.remove(lowestKey);
		}

		return sorted;
	}
	
	private String getLowestASCII(Map<String,String> map){
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
		char[] lowest = (pair.getKey()+""+pair.getValue()).toCharArray();
		String lowestKey = (String)pair.getKey();
		
	    while (it.hasNext()) {
	        pair = (Map.Entry<String,String>)it.next();
	        char[] chars = (pair.getKey()+""+pair.getValue()).toCharArray();

	        for(int i = 0;i < chars.length;i++){

	        	if(i == lowest.length){
	        		break;
	        	}
	        	
	        	if((int)chars[i] < (int)lowest[i]){
		        	lowestKey = (String)pair.getKey();
		        	lowest = (pair.getKey()+""+pair.getValue()).toCharArray();
		        	break;
	        	}
	        	else if((int)chars[i] == (int)lowest[i]){
	        		continue;
	        	}
	        	else{
	        		break;
	        	}
	        }
	    }
	    return lowestKey;
	}
	
	private String makeCanonical(String[] array){
		String canonical = "";
		
		for(int j = 0; j < array.length; j++){
			canonical += array[j]+"&";
		}
		canonical = canonical.substring(0,canonical.length()-1);
		
		canonical = "GET\nwebservices.amazon.co.uk\n/onca/xml\n"+canonical;
		return canonical;
	}
	
	private String HmacSHA256(String data, String key) throws Exception  {
	    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
	    SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
	    sha256_HMAC.init(secret_key);

	    return URLEncoder.encode(Base64.encodeBase64String(sha256_HMAC.doFinal(data.getBytes())), "UTF-8");
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}
}
