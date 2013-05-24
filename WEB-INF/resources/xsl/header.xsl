<?xml version='1.0'?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fun="http://gov.ca.bc.qp.qpdefender/xsl/functions"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="#all">

	<xsl:function name="fun:printHeader">
		<xsl:param name="title" as="xsd:string"/>
		<head>
			
			<title><xsl:value-of select="$title"/></title>
			<script src="/QPDefender/app/none/resources/media/js/modernizr-1.5.min.js"></script>
			<!-- Webforms2 -->
			<script src="/QPDefender/app/none/resources/media/webforms2/webforms2-p.js"></script>	
			<!-- jQuery and jQuery UI -->
			<link rel="stylesheet" href="/QPDefender/app/none/resources/media/ui-themes/aristo/jquery.ui.all.css"/> 
			<script src="/QPDefender/app/none/resources/media/js/jquery-1.4.3.min.js"></script>
			<script src="/QPDefender/app/none/resources/media/js/jquery-ui-1.8.5.min.js"></script>
			<!-- jQuery Numeric Spinner -->	
			<link rel="stylesheet" href="/QPDefender/app/none/resources/media/components/spinner/ui.spinner.css"/> 
			<script src="/QPDefender/app/none/resources/media/components/spinner/ui.spinner.js"></script>
			<!-- jQuery Color Picker -->
			<link rel="stylesheet" href="/QPDefender/app/none/resources/media/components/colorpicker/colorpicker.css"/>
			<script src="/QPDefender/app/none/resources/media/components/colorpicker/colorpicker.js"></script>
			<!-- jQuery Placehol -->
			<script src="/QPDefender/app/none/resources/media/components/placeholder/jquery.placehold-0.2.min.js"></script>
			<script src="/QPDefender/app/none/resources/media/js/html5forms.fallback.js"></script>	
			
			<!--  Following is to enable lightbox functionality -->
			<script type="text/javascript" src="/QPDefender/app/none/resources/media/js/prototype.js"/>
			<script type="text/javascript" src="/QPDefender/app/none/resources/media/js/lightbox.js"/>
			<link href="/QPDefender/app/none/resources/media/css/lightbox.css" rel="stylesheet" media="screen,projection" type="text/css"/>
			
			<!--  Finally our specific css overrides all others. -->
			<link href="/QPDefender/app/none/resources/media/group.css" rel="stylesheet" type="text/css"/>
		</head>
	</xsl:function>
	
</xsl:stylesheet>