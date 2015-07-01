<html>
	
	<script>var contextPath = "${pageContext.request.contextPath}";</script>
 	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css" type="text/css" />
 	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/jquery-ui.min.css" type="text/css" />
 	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/jquery-ui.structure.min.css" type="text/css" />
 	<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/jquery-ui.theme.min.css" type="text/css" />
 	<script src="${pageContext.request.contextPath}/scripts/jquery-1.11.3.min.js" type="text/javascript" /></script>
    <script src="${pageContext.request.contextPath}/scripts/script.js" type="text/javascript" /></script>
  	<script src="${pageContext.request.contextPath}/scripts/jquery-ui.min.js" type="text/javascript" /></script>
  	
  <head><title>Amazon Custom Search</title></head>
  
  <body>
  
  <div id="text"><h1>${headerText}</h1></div>

  <div id="description"><p>${descriptionText}</p></div>

  <div id="access">
  		<div>AWS Access Key ID : <input type="password" id="accessID"></div>
  		<div>AWS Secret Key : <input type="password" id="secretKey"></div>
  </div>
  
  <div class="emptyDiv"></div>
  
  <div id="box"><input type="search" id="search" title="Insert search tags here"></div>
  
  <div class="emptyDiv"></div>
  
  <div id="loading"><img src="${pageContext.request.contextPath}/img/loading.gif" height="60" width="60"></div>
  
  <div class="buttonDiv"><button type="button" class="nextPage">NEXT PAGE --></button></div>
  
  <div id="results"></div>
  
  <div class="buttonDiv"><button type="button" class="nextPage">NEXT PAGE --></button></div>
  
  
  </body>
</html>
