<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fun="http://gov.ca.bc.qp.qpdefender/xsl/functions" 
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
	exclude-result-prefixes="#all">

	<xsl:import href="common.xsl"/>
	<xsl:import href="header.xsl"/>
	<xsl:import href="components.xsl"/>

	<xsl:param name="roles" select="''"/>
	<xsl:param name="msg" select="''"/>
	<xsl:param name="groupid" select="'-1'"/>
	    
	<xsl:output method="html" doctype-system="about:legacy-compat" />
	            

	<xsl:template match="/">
		<html>
			<xsl:sequence select="fun:printHeader('Add Product')"/>
			<script type="text/javascript">
				<xsl:value-of select="fun:printUserAccessJavascript()"/>
			</script>
			<body>
				<div class="container">
					<form name="addGroupProduct" action="/QPDefender/app/group/groupproducts/add" method="post">
						<table width="539" border="0" class="productSpecs">
						  <tr class="thead">
						    <td>Product</td>
						    <td>Concurrent </td>
						    <td>Expiry</td>
						    <td>&#160;</td>
						  </tr>	
						  <xsl:apply-templates select="groupproduct"/>
						</table>
						<div class="actions">
							<input type="submit" value="Submit"/>
							<a href="#" class="lbAction" rel="deactivate"><button>Cancel</button></a>
						</div>
					</form>
				</div>
			</body>
		</html>	
	</xsl:template>
	<xsl:variable name="enabled">true</xsl:variable>
	
	<xsl:template match="groupproduct">
		  <input type="hidden" value="{id}" name="id"/>
		  <xsl:choose>
		  	<xsl:when test="not(id = '-1')">
		  		<input type="hidden" value="{id}" name="groupid"/>
		  	</xsl:when>
		  	<xsl:otherwise>
		  		<input type="hidden" value="{$groupid}" name="groupid"/>
		  	</xsl:otherwise>
		  </xsl:choose>
		   <tr>
			    <td><xsl:sequence select="fun:productType(product/id)"/></td>
			    <td><xsl:sequence select="fun:textInput(concurrent, true())"/></td>
			    <td><xsl:sequence select="fun:dateInput(expiryDate, true())"/></td>
			    <td>&#160;</td>
			</tr>	
	</xsl:template>
	
	<xsl:function name="fun:printUserAccessJavascript">
		<![CDATA[
			function switchCredentials(elem) {
				alert("here");
				var tr = elem.parentNode.parentNode;
				var value = elem.options[elem.selectedIndex].value;
				var elems = tr.getElementsByTagName("*");
				for(var i = 0; i < elems.length; i++) {
					// rules for credential box
					if(elems[i].name == "credential") {
						// Ok check through the selected value to determine behaviour
						
						// First case is if value is STANDARD or nothing.
						if(value == "3" || value == "") {
							// If the type wasn't already password clear and change to text type.
							if(elems[i].type != "password") {
								elems[i].type = "password";
								elems[i].value = "";
							}
							
						} else {
							// All other cases the input type is text and clear content.
							elems[i].type = "text";
							elems[i].value = "";
						}
					}
					
					// Rules for subnet box.
					if(elems[i].name == "credential2") {
						// Only time we care about credential2 is if user has picked a subnet mask option.
						elems[i].value = ""; // Clear every time.
						if(value == "2") {
							elems[i].disabled = false;
							elems[i].required = "required";
						} else {
							// Disable if it's anything other than
							elems[i].disabled = true;
						}
					}
				}
				return false;
			}
		]]>
		
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