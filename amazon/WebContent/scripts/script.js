$(document).ready(function() {
	
	var page = 1;
	var keywords = "";
	var items;
	
	$("#search").focus(function(){
       if($(this).val() == $(this).attr("title")){
    	   $(this).removeClass("inactiveBox");
    	   $(this).val("");
       }
	});
			    
    $("#search").blur(function(){
    	if($(this).val() == ""){
    		$(this).addClass("inactiveBox");
    		$(this).val($(this).attr("title"));
        }
    });
    $("#search").blur();
    
    $("#search").keypress(function(e) {
    	if(e.keyCode == 13){
    		page = 1;
    		keywords = $("#search").val();
    		$("#loading").show();
    		doPost();
    	}
    });
    
    $(".nextPage").click(function(){
    	if(items == undefined){
    		return;
    	}
    	page++;
    	doPost();
    });
    
    function doPost(){
    	
		if(page != 1){
			addPreloaded();
		}

    	var data = "accessID="+encodeURIComponent($("#accessID").val())+
			"&secret="+encodeURIComponent($("#secretKey").val())+
			"&keywords="+encodeURIComponent(keywords)+
    		"&page="+page;
    	
    	$.ajax({
    		type: "POST",
    		url: contextPath+"/search/do",
    		data: data,
    		success: function(response){
				if(response == "unknown"){
					addMessageDiv("Unknown error");
				}
				else if(response == "error"){
    				addMessageDiv("Check your access ID and secret key");
    			}
    			else{
    				addResults(jQuery.parseJSON(response));
    			}
    		},
    		error: function(e){
    			console.log(e);
    		}
    	});
    }
    
    function addPreloaded(){
    	if(items.length >= 12){
    		items = items.slice(12,items.length);
    	}
    	else{
    		items = [];
    	}
    	
    	addItemsToSearch();
    }
    
    function addMessageDiv(message){
		$("#results").empty();
    	$("#results").append("<p class='message'><b>"+message+"</b></p>");
    	$(".buttonDiv").hide();
    }
    
    function addItemsToSearch(){
    	$("#results").empty();
    	
    	if(items.length == 0){
    		addMessageDiv("No Items Found");
    	}
    	
    	for(var i = 0; i < items.length; i++){
    		if(i == 12){
    			break;
    		}
    		addItemDiv(items[i]);
    	}
    	
    	$(".buttonDiv").show();
    	$("#loading").hide();
    	$("#results").accordion("refresh"); 
    }
    
    function addResults(response){
    	if(page == 1){
    		items = response.items;
	    	$("#results").accordion({
			      heightStyle: "content",
			      active: false,
			      collapsible: true,
			      icons: false
		  	});
    		addItemsToSearch();
    	}
    	else{
    		for(var i = 0; i < response.items.length; i++){
    			items.push(response.items[i]);
    		}
    	}
    }
    
    function addItemDiv(item){
    	if(item == undefined || item == null ){return;}
    	var mediumImage = "<img src='"+item.mediumImageLink+"' />";
    	var smallImage = "<img src='"+item.smallImageLink+"' />";

    	if(item.mediumImageLink == "null"){
    		mediumImage = "<img src='"+contextPath+"/img/amazon-no-image.jpg"+"' />";
    	}
    	if(item.smallImageLink == "null"){
    		smallImage = "<img height='75' width='75' src='"+contextPath+"/img/amazon-no-image.jpg"+"' />";
    	}
    	
    	var itemDiv = "<h3>"+smallImage+item.title+"</h3>";
    	itemDiv += "<div class='item'>";
    	itemDiv += mediumImage;
    	
    	for(var key in item.attributes){
    		itemDiv += "<p><b>"+key+" :</b> "+item.attributes[key]+"</p>"
    	}
    	
    	itemDiv += "<a href='"+item.detailedLink+"' target='_blank' >Click here for detailed info from Amazon </a>";
    	itemDiv += "</div>";
    	$("#results").append(itemDiv);
    }

});