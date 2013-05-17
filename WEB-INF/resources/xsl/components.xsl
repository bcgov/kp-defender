<?xml version='1.0'?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fun="http://gov.ca.bc.qp.qpdefender/xsl/functions"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="#all">

	<xsl:import href="common.xsl"/>
	
	<xsl:variable name="custTypeDoc" select="doc('/QPDefender/app/none/custtype/all')"/>

	<!--  Function for creating text input boxes. If the value of the elem/text() is -1
			it will be suppressed as this is a empty placeholder value within QPDefender. -->
	<xsl:function name="fun:textInput">
		<xsl:param name="elem" as="item()*"/>
		<xsl:param name="required" as="xsd:boolean"/>
		<xsl:variable name="val"><xsl:if test="not($elem/text() = '-1')"><xsl:value-of select="$elem/text()"/></xsl:if></xsl:variable>
		<input name="{$elem/name()}" value="{$val}">
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

</xsl:stylesheet>