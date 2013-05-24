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
			<xsl:sequence select="fun:printHeader('Add User')"/>
			<script type="text/javascript">
				<xsl:value-of select="fun:printUserAccessJavascript()"/>
			</script>
			<body>
				<div class="container">	
					<xsl:apply-templates select="useraccess"/>
					<div class="actions">
						<input type="submit" value="Submit"/>
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
			          		<xsl:when test="credentType = '2'">
			          			<attr name="disabled">false</attr>
			          			<attr name="required">required</attr>
			          		</xsl:when>
			          		<xsl:otherwise><attr name="disabled">false</attr></xsl:otherwise>
			          	</xsl:choose>
			          </xsl:variable>
		              <td class="useraccess_credential2">
		              	<label>Subnet Mask</label> <xsl:sequence select="fun:textInput(credential2, false(), $atts)"/>
		              	<input type="submit" value="Submit"/>
		              </td>
				</tr>
	          </table>
	        </div>
	   </form>		
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
	
</xsl:stylesheet>