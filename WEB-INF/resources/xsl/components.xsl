<?xml version='1.0'?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fun="http://gov.ca.bc.qp.qpdefender/xsl/functions"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="#all">

	<xsl:import href="common.xsl"/>
	
	<xsl:variable name="custTypeDoc" select="doc('/QPDefender/app/none/custtype/all')"/>
	<xsl:variable name="credTypeDoc" select="doc('/QPDefender/app/none/credentialtype/all')"/>
	<xsl:variable name="productTypeDoc" select="doc('/QPDefender/app/none/products/all')"/>

	<!--  Function for creating text input boxes. If the value of the elem/text() is -1
			it will be suppressed as this is a empty placeholder value within QPDefender. -->
	<xsl:function name="fun:textInput">
		<xsl:param name="elem" as="item()*"/>
		<xsl:param name="required" as="xsd:boolean"/>
		<xsl:sequence select="fun:textInput($elem, $required, /..)"/>
	</xsl:function>
	
	<!--  Function for creating text input boxes.  If the value of the elem/text() is -1
			it will be suppressed as this is a empty placeholder value within QPDefender. You
			may pass in optional extra attributes in the form <attr name="name">value</attr> -->
	<xsl:function name="fun:textInput">
		<xsl:param name="elem" as="item()*"/>
		<xsl:param name="required" as="xsd:boolean"/>
		<xsl:param name="optAtts" as="item()*"/>
		<xsl:variable name="val"><xsl:if test="not($elem/text() = '-1')"><xsl:value-of select="$elem/text()"/></xsl:if></xsl:variable>
		<input name="{$elem/name()}" value="{$val}">
			<xsl:if test="$required">
				<xsl:attribute name="required">required</xsl:attribute>
			</xsl:if>
			<xsl:for-each select="$optAtts/attr">
				<xsl:attribute name="{@name}"><xsl:value-of select="."/></xsl:attribute>
			</xsl:for-each>	
		</input>	
	</xsl:function>
	
	<!--  Function for creating password input boxes. If the value of the elem/text() is -1
			it will be suppressed as this is a empty placeholder value within QPDefender. -->	
	<xsl:function name="fun:passwordInput">
		<xsl:param name="elem" as="item()*"/>
		<xsl:param name="required" as="xsd:boolean"/>
		<xsl:variable name="val"><xsl:if test="not($elem/text() = '-1')"><xsl:value-of select="$elem/text()"/></xsl:if></xsl:variable>
		<input name="{$elem/name()}" value="{$val}" type="password">
			<xsl:if test="$required">
				<xsl:attribute name="required">required</xsl:attribute>
			</xsl:if>	
		</input>	
	</xsl:function>
	
	<xsl:function name="fun:dateInput">
		<xsl:param name="elem" as="item()*"/>
		<xsl:param name="required" as="xsd:boolean"/>
		<input name="{$elem/name()}" value="{fun:parseDate($elem/text())}" type="date">
			<xsl:if test="$required">
				<xsl:attribute name="required">required</xsl:attribute>
			</xsl:if>	
		</input>
	</xsl:function>

	<xsl:function name="fun:custType">
		<xsl:param name="value"/>
		<select name="custtype" required="required">
			<option value="">Please Select</option>
			<xsl:for-each select="$custTypeDoc/types/CustType">
				<option value="{id}">
					<xsl:if test="id = $value">
						<xsl:attribute name="selected">selected</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="custType"/>
				</option>
			</xsl:for-each>
		</select>
	</xsl:function>
	
	<xsl:function name="fun:credentialType">
		<xsl:param name="value"/>
		<select name="credentialType" required="true" onchange="switchCredentials(this)">
			<option value="">Please Select</option>
			<xsl:for-each select="$credTypeDoc/CredentialTypes/credentialType">
				<option value="{id}">
					<xsl:if test="id = $value or type = $value">
						<xsl:attribute name="selected">selected</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="type"/>
				</option>
			</xsl:for-each>
		</select>
	</xsl:function>

	<xsl:function name="fun:productType">
		<xsl:param name="id"/>
		<select name="productType" required="true">
			<option value="">Please Select</option>
			<xsl:for-each select="$productTypeDoc/products/product">
				<option value="{id}" defaultTimeout="{defaultTimeout}">
					<xsl:if test="id = $id">
						<xsl:attribute name="selected">selected</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="productname"/>
				</option>
			</xsl:for-each>
		</select>
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