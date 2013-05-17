<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fun="http://gov.ca.bc.qp.qpdefender/xsl/functions" 
	xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
	exclude-result-prefixes="#all">

	<xsl:include href="common.xsl"/>
	<xsl:include href="header.xsl"/>
	<xsl:include href="components.xsl"/>

	<xsl:param name="roles" select="''"/>
	<xsl:param name="msg" select="''"/>
	
	<xsl:output media-type="xhtml" method="xhtml" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" encoding="utf-8" indent="yes" omit-xml-declaration="yes"
	            exclude-result-prefixes="#all"/>
	            

	<xsl:template match="/">
		<html>

			<xsl:sequence select="fun:printHeader('Add Group')"/>
			<body>
				<div class="group_information">
					<xsl:choose>
						<xsl:when test="group/id &gt; 0">
							<h1>
								<xsl:value-of select="group/dept_branch"/>
							</h1>
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
						<xsl:apply-templates/>
					</div>
				</div>
			</body>
		</html>
	</xsl:template>


	<xsl:template match="group">
		<form action="/QPDefender/app/group/groups/add" method="post" name="AddUpdateGroup">
			<div class="group_info">
					<input type="hidden" value="{id}" name="id" id="id"/>	
					<table border="0" cellspacing="0" cellpadding="3">
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
				</div>
				<div class="access_info" xmlns="">
					<table border="0" cellspacing="0" cellpadding="3">
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

			<!--
		<div class="group_info">
			<div class="company_ministy"><label>Company/Ministry:</label> <input type="text" name="company_ministry"/></div>
			<div class="organisation_type"><label>Organisation Type:</label> <input type="text" name="organisation_type"/></div>
			<div class="dept_branch"><label>Department/Branch:</label> <input type="text" name="dept_branch"/></div>
			<div class="s_package"><label>Package:</label> <input type="text" name="s_package"/></div>
			<div class="addr1"><label>Address Line 1:</label> <input type="text" name="addr1"/></div>
			<div class="addr2"><label>Address Line 2:</label> <input type="text" name="addr2"/></div>
			<div class="city"><label>City:</label> <input type="text" name="city"/></div>
			<div class="prov"><label>Province:</label> <input type="text" name="prov"/></div>
			<div class="country"><label>Country:</label> <input type="text" name="country"/></div>
			<div class="pcode"><label>Postal Code:</label> <input type="text" name="pcode" placeholder="X1X 1X1" pattern="[A-Z][0-9][A-Z] [0-9][A-Z][0-9]"/></div>
			<div class="phone"><label>Phone:</label> <input type="text" name="phone" placeholder="(111) 111-1111"/></div>
			<div class="fax"><label>Fax:</label> <input type="text" name="fax" placeholder="(111) 111-1111"/></div>
			<div class="email"><label>Email:</label> <input type="email" name="email" placeholder="someone@something.com"/></div>
		</div>
		<div class="contact_info">
			<div class="contact_name"><label>Contact Name:</label> <input type="text" name="contact_name"/></div>
			<div class="contact_phone"><label>Contact Phone:</label> <input type="text" name="contact_phone"/></div>
			<div class="contact_email"><label>Contact Email:</label> <input type="text" name="contact_email"/></div>
			<div class="contact_fax"><label>Contact Fax:</label> <input tabindex="text" name="contact_fax"/></div>
		</div>
		<div class="access_info">
			<div class="active"><label>Active</label> <input type="checkbox" checked="checked" name="active"/></div>
			<div class="custType"><label>Customer Type</label> 
				<select name="custType">
					<option value="0"></option>
					<option value="1">ONLINE</option>
				</select>
			</div>
			<div class="start_dt"><label>Start Date</label> <input type="date" name="start_dt"/></div>
			<div class="end_dt"><label>End Date</label> <input type="text" name="expiry_dt"/></div>
			<div class="auto_expire"><label>Auto Expire</label> <input type="checkbox" name="auto_expire" checked="checked"/></div>
			<div class="daysleft"><label>Days Left</label> <input type="text" name="daysleft"/></div>
		</div>
		<div class="order_info">
			<div class="sap_order"><label>SAP Order Number:</label> <input type="text" name="sap_order"/></div>
			<div class="sap_customer"><label>SAP Customer:</label> <input type="text" name="sap_customer"/></div>
			<div class="cust_note"><label>Customer Note:</label> <textarea name="cust_note" cols="25" rows="5"/></div>
		</div>
		<div class="product_info">
			<div class="groupProducts"><label>Product Access</label> <select name="groupProducts" multiple="multiple"/></div>
			<div class="addGroupProducts"><input type="button" value="Add Product"/></div>
		</div>

		<div class="users">
			<div class="useraccess">
				<div class="user_info">
					<div class="useraccess_username"><label>Username:</label> <input type="text" name="useraccess_username"/></div>
					<div class="useraccess_email"><label>Email:</label> <input type="email" name="useraccess_email" required="required"/></div>
					<div class="useraccess_credentialType"><label>Credential Type</label>
						<select>
							<option value="-1"></option>
							<option value="1">STANDARD</option>
							<option value="2">IP Based</option>
							<option value="3">Subnet Mask</option>
						</select>
					</div>
					<div class="useraccess_credential1"><label>Credential 1:</label> <input type="text" name="productaccess_credential1"/></div>
					<div class="useraccess_credential2"><label>Credential 2:</label> <input type="text" name="productaccess_credential2"/></div>
					<div class="productaccess">
						<div class="product_info">
							<div class="productname"><label>Product Name:</label>
								<select name="productaccess_productid">
									<option value="-1"></option>
									<option value="1">BCLaws</option>
									<option value="2">Other</option>
								</select>
							</div>
							<div class="timeout"><label>Timeout:</label> <input type="text" name="productaccess_timeout"/></div>
							<div class="active"><label>Active:</label> <input type="checkbox" name="productaccess_active"/></div>
						</div>
						<div class="addProduct"><a href="#" onclick="addProduct(this)">Add Product</a></div>
					</div>
				</div>
			</div>
			<div class="addUser"><a href="#" onclick="addUser(this)">Add User</a></div>
		</div>

		<button type="submit" value="Submit"/>
		-->
	</xsl:template>
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