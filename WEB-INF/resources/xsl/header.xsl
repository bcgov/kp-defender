<?xml version='1.0'?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fun="http://gov.ca.bc.qp.qpdefender/xsl/functions"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="#all">

	<xsl:function name="fun:printHeader">
		<xsl:param name="title" as="xsd:string"/>
		<head>
			<link href="/QPDefender/app/none/resources/media/css/group.css" rel="stylesheet" type="text/css"/>
			<title><xsl:value-of select="$title"/></title>
		    <script type="text/javascript" src="/QPDefender/app/none/resources/media/js/modernizr.min.js"></script>
		    <script type="text/javascript" src="/QPDefender/app/none/resources/media/js/webforms2-0.5.4/webforms2-p.js"></script>
		    <script type="text/javascript" src="/QPDefender/app/none/resources/media/js/jquery-ui-1.10.2.custom/js/jquery-1.9.1.js"></script>
		    <script type="text/javascript" src="/QPDefender/app/none/resources/media/js/jquery-ui-1.10.2.custom/js/jquery-ui-1.10.2.custom.min.js"></script>
		    <link rel="stylesheet" href="/QPDefender/app/none/resources/media/js/jquery-ui-1.10.2.custom/css/ui-lightness/jquery-ui-1.10.2.custom.min.css" />
		    <script type="text/javascript" src="/QPDefender/app/none/resources/media/js/fallback.js"></script>
		</head>
	</xsl:function>
	
</xsl:stylesheet>