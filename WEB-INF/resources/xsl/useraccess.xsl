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
	<xsl:param name="uriPath" select="''"/>
	<xsl:param name="groupid" select="'-1'"/>
	    
	<xsl:output method="html" doctype-system="about:legacy-compat" />
	            

	<xsl:template match="/">
		<html>
			<xsl:sequence select="fun:printHeader('Add User')"/>
			<script type="text/javascript">
				<xsl:value-of select="fun:printUserAccessJavascript()"/>
			</script>
			<body>
				<div class="container">	
					<xsl:apply-templates select="useraccess"/>
					<div class="actions">
						<a href="#" class="lbAction" rel="deactivate"><button>Cancel</button></a>
					</div>
				</div>
			</body>
		</html>	
	</xsl:template>
	<xsl:variable name="enabled">true</xsl:variable>
	
	<xsl:template match="useraccess">
		<form name="addUser" action="/QPDefender/app/group/user/credentials/add" method="post">
			<div class="user">
			  <input type="hidden" value="{userCredentialId}" name="userCredentialId"/>
			  <input type="hidden" value="{user/id}" name="userid"/>
			  <xsl:choose>
			  	<xsl:when test="not(user/groupId = '-1')">
			  		<input type="hidden" value="{user/groupId}" name="groupid"/>
			  	</xsl:when>
			  	<xsl:otherwise>
			  		<input type="hidden" value="{$groupid}" name="groupid"/>
			  	</xsl:otherwise>
			  </xsl:choose>
			  
	          <table border="0" cellspacing="0" cellpadding="3">
				<tr>
		              <td class="useraccess_username"><label>Username:</label><xsl:sequence select="fun:textInput(user/username, true())"/></td>
		              <td class="useraccess_email"><label>Email:</label><xsl:sequence select="fun:textInput(user/email, true())"/></td>
		              <td class="useraccess_credentialType"><label>Credential Type</label>
		              	<xsl:sequence select="fun:credentialType(credentialType)"/>
		              </td>
		              <td class="useraccess_credential1"><label>Credential</label> 
			              <xsl:choose>
			              	<xsl:when test="credentialType='STANDARD'"><xsl:sequence select="fun:passwordInput(credential, true())"/></xsl:when>
			              	<xsl:otherwise><xsl:sequence select="fun:textInput(credential, true())"/></xsl:otherwise>
			              </xsl:choose>
			          </td>
			          <xsl:variable name="atts">
			          	<xsl:choose>
			          		<xsl:when test="credentialType='SERVER_IP'">
			          			<attr name="required">required</attr>
			          		</xsl:when>
			          		<xsl:otherwise><attr name="disabled">true</attr></xsl:otherwise>
			          	</xsl:choose>
			          </xsl:variable>
		              <td class="useraccess_credential2">
		              	<label>Subnet Mask</label> <xsl:sequence select="fun:textInput(credential2, false(), $atts)"/>    	
		              </td>
					  <td class="meta_data">
					  	<label>Meta String (ns:name=>value;)</label>
						<textarea cols="10" rows="4" name="meta" id="meta"><xsl:value-of select="user/meta"/></textarea>
					  </td>
				</tr>
	          </table>
				<input type="hidden" name="return_URI">
					<xsl:attribute name="value">
						<xsl:choose>
							<xsl:when test="ends-with($uriPath, '/me')">/QPDefender/app/group/groups/me</xsl:when>
							<xsl:otherwise>/QPDefender/app/group/groups/ID/<xsl:value-of select="$groupid"/></xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>
				</input>
			  <input type="submit" value="Submit"/>
	        </div>
	   </form>		
	</xsl:template>
	
	<xsl:function name="fun:printUserAccessJavascript">
		<![CDATA[
			function switchCredentials(elem) {
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