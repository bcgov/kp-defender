<?xml version='1.0'?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fun="http://gov.ca.bc.qp.qpdefender/xsl/functions" 
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
	exclude-result-prefixes="#all">

	<xsl:import href="common.xsl"/>
	<xsl:import href="header.xsl"/>
	<xsl:import href="components.xsl"/>

	<xsl:param name="roles" select="''"/>
	<xsl:param name="msg" select="''"/>
	<xsl:param name="uriPath" select="''"/>
	<!-- Unique identifer for this users access and credentials, error if this is -1 (how do we add a product to a non-existant user access. -->
	<xsl:param name="userCredId" select="-1"/>
	<!-- Unique identifer for a product that this user access can access, if -1 we're adding -->
	<!--<xsl:param name="userProductId" select="-1"/>-->
	<!-- Unique identifier for the group of users this product Access belongs to. Error if this is -1 -->
	<xsl:param name="groupid" select="-1"/>
	<!-- Unique identifer for the product the user has access to, if -1 then we're adding -->
	<xsl:param name="productid" select="-1"/>
	    
	<xsl:output method="html" doctype-system="about:legacy-compat" />
	            

	<xsl:template match="/">
		<html>
			<xsl:sequence select="fun:printHeader('Add Product')"/>
			<script type="text/javascript">
				<!--<xsl:value-of select="fun:printUserAccessJavascript()"/>-->
			</script>
			<body>
				<div class="container">
					<form name="addProductAccess" action="/QPDefender/app/group/productAccess/userproduct/add" method="post">
						<input type="hidden" name="userCredId" value="{$userCredId}"/>
						<input type="hidden" name="userProductId" value="{productAccess/userProductsID}"/>
						<input type="hidden" name="return_URI">
							<xsl:attribute name="value">
								<xsl:choose>
									<xsl:when test="ends-with($uriPath, '/me')">/QPDefender/app/group/groups/me</xsl:when>
									<xsl:otherwise>/QPDefender/app/group/groups/ID/<xsl:value-of select="$groupid"/></xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
						</input>
						<table width="539" border="0" class="productSpecs">
						  <tr class="thead">
						    <td>Product</td>
						    <td>Timeout</td>
						    <td>Active</td>
						  </tr>	
						  <xsl:apply-templates select="productAccess"/>
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

	<xsl:template match="productAccess">
		<tr>
			<td><xsl:sequence select="fun:getGroupProductCombo(.)"/></td>
			<td><xsl:sequence select="fun:textInput(timeout, true())"/></td>
			<td><input type="checkbox" name="active" id="active">
					<xsl:if test="active = 'true'">
						<xsl:attribute name="checked">checked</xsl:attribute>
					</xsl:if>
				</input>
			</td>
		</tr>
	</xsl:template>

	<xsl:variable name="groupProductDoc" select="doc(concat('/QPDefender/app/none/groupproducts/groupid/', $groupid))"/>

	<xsl:function name="fun:getGroupProductCombo">
		<xsl:param name="me"/>
		<!-- See bug below -->
		<xsl:if test="$me/userProductsID != -1">
			<input type="hidden" name="groupProductId" value="{$groupProductDoc/GroupProducts/groupproduct[product/id = $productid]/id}"/>
		</xsl:if>
		<select required="true" onchange="switchTimeout(this)">
			<!-- Disable select box if user is editing to ensure a user can't have multiple accesses to the same product -->
			<!-- Strange bug, when inputs are disabled they view as null to jax-rs. Do a switch to change the name and let 
					the hidden input take care of passing the info instead. -->
			<xsl:choose>
				<xsl:when test="$me/userProductsID != -1">
					<xsl:attribute name="disabled">true</xsl:attribute>
					<xsl:attribute name="name">dummy</xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="name">groupProductId</xsl:attribute>			
				</xsl:otherwise>
			</xsl:choose>
			<option value="">Please Select</option>
			<xsl:for-each select="$groupProductDoc/GroupProducts/groupproduct">
				<option value="{id}">
					<xsl:if test="product/id = $productid">
						<xsl:attribute name="selected">selected</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="product/productname"/>
				</option>
			</xsl:for-each>
		</select>
	</xsl:function>


</xsl:stylesheet><!-- Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" url="..\..\..\..\..\..\..\test\blah.xml" htmlbaseurl="" outputurl="" processortype="saxon8" useresolver="yes" profilemode="0" profiledepth="" profilelength=""
		          urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" validateoutput="no" validator="internal"
		          customvalidator="">
			<advancedProp name="sInitialMode" value=""/>
			<advancedProp name="bXsltOneIsOkay" value="true"/>
			<advancedProp name="bSchemaAware" value="true"/>
			<advancedProp name="bXml11" value="false"/>
			<advancedProp name="iValidation" value="0"/>
			<advancedProp name="bExtensions" value="true"/>
			<advancedProp name="iWhitespace" value="0"/>
			<advancedProp name="sInitialTemplate" value=""/>
			<advancedProp name="bTinyTree" value="true"/>
			<advancedProp name="bWarnings" value="true"/>
			<advancedProp name="bUseDTD" value="false"/>
			<advancedProp name="iErrorHandling" value="fatal"/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/>
		<MapperBlockPosition></MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
-->