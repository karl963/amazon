package com.amazon.search;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.crypto.Mac;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;

import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.amazon.search.object.Item;

@Controller
public class SearchController {

	@RequestMapping(value="/search", method=RequestMethod.GET)
    public ModelAndView getView(ModelMap model) {
        model.addAttribute("headerText","Search from Amazon");
        model.addAttribute("descriptionText","Search uses UK locale and a "
        		+ "preset associate tag, insert keywords and press enter to search");

        return new ModelAndView("search");
    }
	
	@RequestMapping(value="/search/do", method=RequestMethod.POST
			, params={"accessID","secret","keywords","page"})
	@ResponseBody
    public String doSearch(HttpSession session, ModelMap model,
    		@RequestParam("accessID")String accessID,
    		@RequestParam("secret")String secret,
    		@RequestParam("keywords")String keywords,
    		@RequestParam("page")String page) {
		
		RequestXmlJson request = new RequestXmlJson(accessID, secret, keywords, Integer.parseInt(page));
		
		if(request.isError()){
			return "error";
		}
		return request.getJson();
    }
	
}
