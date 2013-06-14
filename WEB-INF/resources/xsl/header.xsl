<?xml version='1.0'?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fun="http://gov.ca.bc.qp.qpdefender/xsl/functions"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="#all">

	<xsl:function name="fun:printHeader">
		<xsl:param name="title" as="xsd:string"/>
		<head>
			
			<title><xsl:value-of select="$title"/></title>
			<!--  Following is to enable lightbox functionality -->
			<script type="text/javascript" src="/QPDefender/app/none/resources/media/js/prototype.js"/>
			<script type="text/javascript" src="/QPDefender/app/none/resources/media/js/lightbox.js"/>
			<link href="/QPDefender/app/none/resources/media/css/lightbox.css" rel="stylesheet" media="screen,projection" type="text/css"/>

			<script  type="text/javascript" src="/QPDefender/app/none/resources/media/js/modernizr-1.5.min.js"></script>
			<!-- Webforms2 -->
			<!-- <script src="/QPDefender/app/none/resources/media/webforms2/webforms2-p.js"></script>-->	
			<!-- jQuery and jQuery UI -->
			<!-- <link rel="stylesheet" href="/QPDefender/app/none/resources/media/ui-themes/aristo/jquery.ui.all.css"/>--> 
			<!-- <script src="/QPDefender/app/none/resources/media/js/jquery-1.4.3.min.js"></script>-->
			<script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.js"></script>
			<script type="text/javascript" src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
			<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css"></link>
			<script type="text/javascript">
				var $j = jQuery.noConflict();
			</script>
			<!-- <script src="/QPDefender/app/none/resources/media/js/jquery-ui-1.8.5.min.js"></script>-->
			<!-- jQuery Numeric Spinner -->	
			<!-- 
			<link rel="stylesheet" href="/QPDefender/app/none/resources/media/components/spinner/ui.spinner.css"/> 
			<script src="/QPDefender/app/none/resources/media/components/spinner/ui.spinner.js"></script>
			-->
			<!-- jQuery Color Picker -->
			<!-- 
			<link rel="stylesheet" href="/QPDefender/app/none/resources/media/components/colorpicker/colorpicker.css"/>
			<script src="/QPDefender/app/none/resources/media/components/colorpicker/colorpicker.js"></script>
			-->
			<!-- jQuery Placehol -->
			<!-- 
			<script src="/QPDefender/app/none/resources/media/components/placeholder/jquery.placehold-0.2.min.js"></script>
			-->
			<script type="text/javascript" src="/QPDefender/app/none/resources/media/js/html5forms.fallback.js"></script>	
			
			<!--  Finally our specific css overrides all others. -->
			<link href="/QPDefender/app/none/resources/media/group.css" rel="stylesheet" type="text/css"/>
			<!-- TO faciliate our overlay -->
			<!--
			<script src="http://cdn.jquerytools.org/1.2.7/full/jquery.tools.min.js"></script>-->
<!--
			<link href="/QPDefender/app/none/resources/media/overlay-apple.css" rel="stylesheet" type="text/css"/>
			<script type="text/javascript">
				$(function() {
 
				    // if the function argument is given to overlay,
				    // it is assumed to be the onBeforeLoad event listener
				    $("a[rel]").overlay({
 
				        mask: 'darkred',
				        effect: 'apple',
 
				        onBeforeLoad: function() {
 
				            // grab wrapper element inside content
				            var wrap = this.getOverlay().find(".contentWrap");
 
				            // load the page specified in the trigger
				            wrap.load(this.getTrigger().attr("href"));
				        }
 
				    });
				});
			</script>
			-->
			<!--

			-->
		</head>
	</xsl:function>
	
</xsl:stylesheet><!-- Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios/>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition></MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
-->