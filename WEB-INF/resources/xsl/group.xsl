<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fun="http://gov.ca.bc.qp.qpdefender/xsl/functions" 
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
	exclude-result-prefixes="#all">

	<xsl:import href="common.xsl"/>
	<xsl:import href="header.xsl"/>
	<xsl:import href="components.xsl"/>
	<xsl:import href="useraccess.xsl"/>

	<xsl:param name="roles" select="''"/>
	<xsl:param name="msg" select="''"/>
	    
	<xsl:output method="html" doctype-system="about:legacy-compat" />
	            

	<xsl:template match="/">
		<html>
			<xsl:sequence select="fun:printHeader('Add Group')"/>
			<script type="text/javascript">
				/* Here */
				<xsl:sequence select="fun:printUserAccessJavascript()"/>
			</script>
			<body>
					<div class="group_information">
						<xsl:choose>
							<xsl:when test="group/id &gt; 0">
								<h1>
									<xsl:value-of select="group/dept_branch"/>
								</h1>
								<div class="topNav"><a href="#">Search Group</a> | <a href="#">Search Users</a> | <a href="#">Add Product</a></div>
								<div class="auto_info">
									<div class="CreatedBy">Created By: <xsl:value-of select="group/insert_user/username"/></div>
									<div class="CreatedDate">Created On: <xsl:value-of select="fun:parseDate(group/insert_dt)"/></div>
									<div class="ModifiedBy">Modified By: <xsl:value-of select="group/modify_user/username"/></div>
									<div class="ModifiedDate">Modified On: <xsl:value-of select="fun:parseDate(group/modify_dt)"/></div>
								</div>
							</xsl:when>
							<xsl:otherwise>
								<h1>Add Group</h1>
							</xsl:otherwise>
						</xsl:choose>
						<div class="form_container">
							<xsl:if test="not($msg = '')">
								<h3><font color="red"><xsl:value-of select="fun:decodeUrL($msg)"/></font></h3>
							</xsl:if>
							<xsl:apply-templates select="group"/>
						</div>
						<xsl:if test="group/id &gt; 0">
							<div class="addProduct"> 
								<a href="/QPDefender/app/useraccess/groupid={group/id}/useraccess/empty" class="lbOn">
									<img src="media/images/add.png" alt="" width="18" height="18" border="0" align="absmiddle"/> Add User</a>
							</div>
							<xsl:apply-templates select="group/users"/>
						</xsl:if>
					</div>
			</body>
		</html>
	</xsl:template>


	<xsl:template match="group">
		<form action="/QPDefender/app/group/groups/add" method="post" name="AddUpdateGroup">
			<div class="group_info">
					<input type="hidden" value="{id}" name="id" id="id"/>	
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="company_ministy">Company/Ministry:</td>
							<td class="company_ministy">
								<xsl:sequence select="fun:textInput(company_ministry, true())"/>
								<!--<input type="text" name="company_ministry" value="{company_ministry}"/>-->
							</td>
							<td class="addr1">
								<label>Address Line 1:</label>
							</td>
							<td class="addr1">
								<xsl:sequence select="fun:textInput(addr1, false())"/>
							</td>
						</tr>
						<tr>
							<td class="organisation_type">Organisation Type:</td>
							<td class="organisation_type">
								<xsl:sequence select="fun:textInput(organisation_type, false())"/>
							</td>
							<td>Address Line 2:</td>
							<td>
								<span class="addr2">
									<xsl:sequence select="fun:textInput(addr2, false())"/>
								</span>
							</td>
						</tr>
						<tr>
							<td class="dept_branch">Department/Branch:</td>
							<td class="dept_branch">
								<xsl:sequence select="fun:textInput(dept_branch, false())"/>
							</td>
							<td class="city">City:</td>
							<td class="city">
								<xsl:sequence select="fun:textInput(city, false())"/>
							</td>
						</tr>
						<tr>
							<td class="s_package">
								<label>Package:</label>
							</td>
							<td class="s_package">
								<xsl:sequence select="fun:textInput(s_package, false())"/>
							</td>
							<td class="prov">Province:</td>
							<td class="prov">
								<xsl:sequence select="fun:textInput(prov, false())"/>
							</td>
						</tr>
						<tr>
							<td class="phone">
								<label>Phone:</label>
							</td>
							<td class="phone">
								<input type="text" name="phone" value="{phone}" placeholder="(111) 111-1111"/>
							</td>
							<td class="country">Country:</td>
							<td class="country">
								<xsl:sequence select="fun:textInput(country, false())"/>
							</td>
						</tr>
						<tr>
							<td class="fax">
								<label>Fax:</label>
							</td>
							<td class="fax">
								<input type="text" name="fax" value="{fax}" placeholder="(111) 111-1111"/>
							</td>
							<td class="pcode">Postal Code:</td>
							<td class="pcode">
								<input type="text" name="pcode" value="{pcode}" placeholder="X1X 1X1" pattern="[A-Z][0-9][A-Z] [0-9][A-Z][0-9]"/>
							</td>
						</tr>
						<tr>
							<td class="email">
								<label>Email:</label>
							</td>
							<td class="email">
								<input type="email" name="email" value="{email}" placeholder="someone@something.com"/>
							</td>
							<td class="city">&#xA0;</td>
							<td class="city">&#xA0;</td>
						</tr>
						<tr>
							<td class="phone">&#xA0;</td>
							<td class="phone">&#xA0;</td>
							<td class="city">&#xA0;</td>
							<td class="city">&#xA0;</td>
						</tr>
						<tr>
							<td class="contact_name">
								<label>Contact Name:</label>
							</td>
							<td class="contact_name">
								<xsl:sequence select="fun:textInput(contact_name, false())"/>
							</td>
							<td class="contact_email">
								<label>Contact Email:</label>
							</td>
							<td class="contact_email">
								<xsl:sequence select="fun:textInput(contact_email, false())"/>
							</td>
						</tr>
						<tr>
							<td class="contact_phone">
								<label>Contact Phone:</label>
							</td>
							<td class="contact_phone">
								<xsl:sequence select="fun:textInput(contact_phone, false())"/>
							</td>
							<td class="contact_fax">
								<label>Contact Fax:</label>
							</td>
							<td class="contact_email">
								<xsl:sequence select="fun:textInput(contact_fax, false())"/>
							</td>
						</tr>
					</table>
					<!-- If we are not adding a new group show the products this group has access to -->
					<xsl:if test="number(/group/id) &gt; 0">
						<xsl:apply-templates select="groupProducts"/>
					</xsl:if>
					<!-- 
					<table width="539" border="0" class="productSpecs">
					  <tr class="thead">
					    <td>Product</td>
					    <td>Concurrent </td>
					    <td>Expirey</td>
					    <td>Edit/Delete</td>
					  </tr>
					  <tr>
					    <td>QP Legaleze</td>
					    <td>3</td>
					    <td>2014-01-01</td>
					    <td>&#160;</td>
					  </tr>
					  <tr>
					    <td>BC Codes</td>
					    <td>5</td>
					    <td>2014-01-01</td>
					    <td>&#160;</td>
					  </tr>
					  <tr>
					    <td><a href="#" onclick="addUser(this)"><img src="media/images/add.png" alt="" width="18" height="18" border="0" align="absmiddle" /></a><a href="#"> Add Product</a></td>
					    <td>&#160;</td>
					    <td>&#160;</td>
					    <td>&#160;</td>
					  </tr>
					</table>
					-->
				</div>
				<div class="access_info">
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="active">
								<label>Active</label>
							</td>
							<td>
								<input type="checkbox" checked="checked" name="active">
									<xsl:if test="active = 'true'">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</td>
						</tr>
						<tr>
							<td class="custType">
								<label>Customer Type</label>
							</td>
							<td>
								<xsl:sequence select="fun:custType(custType)"/>
							</td>
						</tr>
						<tr>
							<td class="start_dt">
								<label>Start Date</label>
							</td>
							<td>
								<xsl:sequence select="fun:dateInput(start_dt, false())"/>
							</td>
						</tr>
						<tr>
							<td class="end_dt">
								<label>End Date</label>
							</td>
							<td>
								<xsl:sequence select="fun:dateInput(expiry_dt, false())"/>
							</td>
						</tr>
						<tr>
							<td class="auto_expire">
								<label>Auto Expire</label>
							</td>
							<td>
								<span class="auto_expire">
									<input type="checkbox" name="auto_expire">
									<xsl:if test="auto_expire = 'true'">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
									</input>
								</span>
							</td>
						</tr>
						<tr>
							<td class="daysleft">
								<label>Days Left</label>
							</td>
							<td>
								<xsl:sequence select="fun:textInput(daysleft, false())"/>
							</td>
						</tr>
						<tr>
							<td class="sap_order">
								<label>SAP Order Number:</label>
							</td>
							<td>
								<xsl:sequence select="fun:textInput(sap_order, false())"/>
							</td>
						</tr>
						<tr>
							<td class="sap_customer">
								<label>SAP Customer:</label>
							</td>
							<td>
								<xsl:sequence select="fun:textInput(sap_customer, false())"/>
							</td>
						</tr>
						<tr>
							<td class="cust_note">
								<label>Customer Note:</label>
							</td>
							<td>
								<textarea name="cust_note" cols="20" rows="5">
									<xsl:value-of select="cust_note"/>
								</textarea>
							</td>
						</tr>
						<xsl:variable name="groupAction">
							<xsl:choose>
								<xsl:when test="id = '-1'">Add Group</xsl:when>
								<xsl:otherwise>Update Group</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<tr>
							<td colspan="2" align="right"><input type="submit" value="{$groupAction}"/></td>
						</tr>
					</table>
			</div>
		</form>
	</xsl:template>
	
	<xsl:template match="users">
		<xsl:for-each select="useraccess">
			<xsl:apply-templates select="self::useraccess"/>
			<xsl:apply-templates select="productAccess"/>
			<div class="addProduct"> 
				<a href="/QPDefender/app/productaccess/userCredId={userCredentialId}/groupid={/group/id}/productid={product/id}/productAccess/empty" class="lbOn">
					<img src="media/images/add.png" alt="" width="18" height="18" border="0" align="absmiddle"/> Add Product</a>
			</div>
		</xsl:for-each>

	</xsl:template>

	<xsl:template match="productAccess">
		<div class="product">
        	
			  	<!--
			    <div class="productname"><label>Product Name:</label>      
					<select name="productaccess_productid2">
			        	<option value="-1">Please Choose</option>
						<xsl:variable name="pid" select="product/id"/>
						<xsl:for-each select="/group/groupProducts/groupProduct">
							<option value="{id}">
								<xsl:if test="product/id = $pid">
									<xsl:attribute name="selected">selected</xsl:attribute>
								</xsl:if>
								<xsl:value-of select="product/productname"/>
							</option>
						</xsl:for-each>
			      	</select>
				
				-->
			    <div class="productname"><label>Product Name:</label> <xsl:value-of select="product/productname"/></div>
			    <div class="timeout"><label>Timeout:</label>   <xsl:value-of select="timeout"/></div>
			    <div class="active"><label>Active:</label>      
					<input type="checkbox" name="productaccess_active" disabled="disabled">
						<xsl:if test="active = 'true'">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</input>
				</div>
				<div class="role"><xsl:apply-templates select="roles/role[1]"/>
			    
			  	<xsl:for-each select="roles/role">
					<xsl:if test="position() != 1">
						
			   				<xsl:apply-templates select="self::role"/>
					  
					</xsl:if>
				</xsl:for-each></div>
			
			<div class="controlButtons">
				<form action="/QPDefender/app/group/productAccess/delete/ID/{userProductsID}" method="post"><input type="submit" value="Delete Product"/></form>
				<a href="/QPDefender/app/productaccess/userCredId={parent::useraccess/userCredentialId}/groupid={/group/id}/productid={product/id}/productAccess/ID/{userProductsID}" class="lbOn"><img src="media/images/remove.png" alt="" width="18" height="18" border="0" align="absmiddle" /> Edit Product</a>
				<a href="/QPDefender/app/userroles/userid={userid}/productid={product/id}/roles/userrole/empty" class="lbOn"><img src="media/images/add.png" alt="" width="18" height="18" border="0" align="absmiddle" /> Add Role</a>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="role">
		<xsl:if test="preceding-sibling::role">
			<td colspan="4">&#160;</td>
		</xsl:if>
		<td class="useraccess_username"><label>Role Name:</label>&#160; <xsl:value-of select="roleName"/></td>
		<td class="submit"><a href="/QPDefender/app/roles/delete/{id}">Delete Role</a></td>
	</xsl:template>

	<xsl:template match="groupProducts">
		<table width="539" border="0" class="productSpecs">
		  <tr class="thead">
		    <td>Product</td>
		    <td>Concurrent </td>
		    <td>Expiry</td>
		    <td>&#160;</td>
		  </tr>	
		  <xsl:apply-templates select="descendant::groupProduct"/>
		  <tr>
			<td align="right" colspan="4">
				<a href="/QPDefender/app/groupproduct/groupid={/group/id}/groupproducts/empty" class="lbOn">Add Product</a>
			</td>
		  </tr>
		</table>
	</xsl:template>

	<xsl:template match="groupProduct">
		<tr>
			<td><xsl:value-of select="product/productname"/></td>
			<td><xsl:value-of select="concurrent"/></td>
			<td><xsl:value-of select="fun:parseDate(expiryDate)"/></td>
			<td>
				<a href="/QPDefender/app/groupproduct/groupproducts/ID/{id}"  class="lbOn">Edit</a>
				<a href="/QPDefender/app/groupproduct/group/delete/{id}">Delete</a>
			</td>
		</tr>
	</xsl:template>
	<!-- 
	<xsl:template match="useraccess">
		<tr>
              <td class="useraccess_username"><label>Username:</label><xsl:sequence select="fun:textInput(user/username, true())"/></td>
              <td class="useraccess_email"><label>Email:</label><xsl:sequence select="fun:textInput(user/username, true())"/></td>
              <td class="useraccess_credentialType"><label>Credential Type</label>
              	<xsl:sequence select="fun:credentialType(credentialType)"/>
              </td>
              <td class="useraccess_credential1"><label>Credential</label> <xsl:sequence select="fun:passwordInput(credential, false())"/></td>
              <td class="useraccess_credential2"><label>Subnet Mask</label> <xsl:sequence select="fun:textInput(credential2, false())"/><button value="Reset" type="Reset">Reset</button></td>
		</tr>
	</xsl:template>
	-->
</xsl:stylesheet><!-- Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" url="..\xml_test\group.xml" htmlbaseurl="" outputurl="" processortype="saxon8" useresolver="yes" profilemode="0" profiledepth="" profilelength="" urlprofilexml=""
		          commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" validateoutput="no" validator="internal" customvalidator="">
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