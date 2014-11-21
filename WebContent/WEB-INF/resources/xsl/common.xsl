<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fun="http://gov.ca.bc.qp.qpdefender/xsl/functions" 
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
	exclude-result-prefixes="#all">

	<xsl:variable name="emptyDate">1900-11-05</xsl:variable>
	
	<!--  Parses the time off our dates for readability. Also returns an empty
			string for the specific date 1900-11-05 which we've denoted as
			an arbitrary empty date. -->
	<xsl:function name="fun:parseDate">
		<xsl:param name="unparsedDate"/>
		<xsl:variable name="parsedDate">
			<xsl:analyze-string regex="(\d{{4}}-\d{{2}}-\d{{2}})T.+" select="$unparsedDate">
				<xsl:matching-substring>
					<xsl:value-of select="regex-group(1)"/>
				</xsl:matching-substring>
			</xsl:analyze-string>
		</xsl:variable>
		<xsl:if test="not(normalize-space($emptyDate) = normalize-space($parsedDate))"><xsl:value-of select="$parsedDate"/></xsl:if>
	</xsl:function>
	
	<!--  Replaces special characters associated with an encoded url to those associated with
			a regular string. -->
	<xsl:function name="fun:decodeUrL">
		<xsl:param name="encodedUrl"/>
		<xsl:value-of select="replace($encodedUrl, '\+', ' ')"/>	
	</xsl:function>	

</xsl:stylesheet>