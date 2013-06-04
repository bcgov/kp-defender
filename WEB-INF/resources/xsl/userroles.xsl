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

	<!-- Unique identifier for the user we will add the role to. -->
	<xsl:param name="userid" select="-1"/>
	<!-- Unique identifer for the product the user has access to. -->
	<xsl:param name="productid" select="-1"/>
	    
	<xsl:output method="html" doctype-system="about:legacy-compat" />
	            

	<xsl:template match="/">
		<html>
			<xsl:sequence select="fun:printHeader('Add Role')"/>
			<script type="text/javascript">
				<!--<xsl:value-of select="fun:printUserAccessJavascript()"/>-->
			</script>
			<body>
				<div class="container">
					<form name="addUserRole" action="/QPDefender/app/group/roles/userrole/add" method="post">
						<input type="hidden" name="userid" value="{$userid}"/>
						<input type="hidden" name="productid" value="{$productid}"/>
						<xsl:sequence select="fun:getProductRoleCombo()"/>
						<div class="actions">
							<input type="submit" value="Submit"/>
							<a href="#" class="lbAction" rel="deactivate"><button>Cancel</button></a>
						</div>
					</form>
				</div>
			</body>
		</html>	
	</xsl:template>

	<xsl:variable name="productRoleDoc" select="doc(concat('/QPDefender/app/none/roles/productid/', $productid))"/>

	<xsl:function name="fun:getProductRoleCombo">
		<select name="roleName" required="true">
			<option value="">Please Select</option>
			<xsl:for-each select="$productRoleDoc/ProductRoles/productrole">
				<option value="{roleName}">
					<xsl:value-of select="roleName"/>
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