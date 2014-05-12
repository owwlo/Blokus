<!doctype html>
<htmtype filter textl>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="gwt:property" content="locale=<%= request.getLocale() %>">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=1, minimum-scale=1, maximum-scale=1">
<title>Blokus</title>
<script type="text/javascript" language="javascript"
	src="org.BlokusGame/org.BlokusGame.nocache.js"></script>
<script type="text/javascript" language="javascript">
function scaleBody() {
  var myGameWidth = 500;
  var myGameHeight = 800;
  var scaleX = window.innerWidth / myGameWidth;
  var scaleY = window.innerHeight / myGameHeight;
  var scale = Math.min(scaleX, scaleY);
  var transformString = "scale(" + scale + "," + scale + ")";
  document.body.style.transform = transformString;
  document.body.style['-o-transform'] = transformString;
  document.body.style['-webkit-transform'] = transformString;
  document.body.style['-moz-transform'] = transformString;
  document.body.style['-ms-transform'] = transformString;
  var transformOriginString = "0 0 0";
  document.body.style['transform-origin'] = transformOriginString;
  document.body.style['-o-transform-origin'] = transformOriginString;
  document.body.style['-webkit-transform-origin'] = transformOriginString;
  document.body.style['-moz-transform-origin'] = transformOriginString;
  document.body.style['-ms-transform-origin'] = transformOriginString;
}
window.onresize = scaleBody;
window.onorientationchange = scaleBody;
window.onload = scaleBody;
document.addEventListener("orientationchange", scaleBody);
</script>
<style>
html { overflow-x:hidden; 
overflow-y:hidden;
}
.imgContainer {
	overflow: hidden;
	width: 73px;
	height: 97px;
}

.imgShortContainer {
	overflow: hidden;
	width: 15px;
	height: 97px;
}

.color-icon {
	background-image: none !important;
	background-color: #ff0000;
}

.color-icon2 {
	background-image: none !important;
	background-color: #00ff00;
}

.color-try {
	background-image: none !important;
	background-color: #aaaaaa;
}

.label_colored_red {
  background-color: #ffaaaa;
}

.margined { 
  margin: 10px; 
}

.imageBtn {
  border-style:solid;
  border-width:1px;
}
.maxWidthFixedDiv {
  max-width:500px;
}
</style>
</head>
<body>
	<div id="mainDiv" width="100%" height="100%"></div>
</body>
</html>
