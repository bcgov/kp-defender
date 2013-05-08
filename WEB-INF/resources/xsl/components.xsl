<?xml version='1.0'?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fun="http://gov.ca.bc.qp.foodsafe/xsl/functions"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="#all">

	<xsl:variable name="custTypeDoc" select="doc('/QPDefender/app/none/custtype/all')"/>

	<xsl:function name="fun:textInput">
		<xsl:param name="elem" as="item()*"/>
		<xsl:param name="required" as="xsd:boolean"/>
		<input name="{$elem/name()}" value="{$elem/text()}">
			<xsl:if test="$required">
				<xsl:attribute name="required">required</xsl:attribute>
			</xsl:if>	
		</input>
	</xsl:function>

	<xsl:function name="fun:custType">
		<select name="custType">
			<option value="-1"/>
			<xsl:for-each select="$custTypeDoc/types/CustType">
				<option value="{id}"><xsl:value-of select="custType"/></option>
			</xsl:for-each>
		</select>

	</xsl:function>

</xsl:stylesheet>