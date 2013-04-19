<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output media-type="xhtml" method="xhtml" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" 
	doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" encoding="utf-8" indent="yes"/>

<xsl:template match="/">
	<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Add Group</title>
	    <script type="text/javascript" src="media/js/modernizr.min.js"></script>
	    <script type="text/javascript" src="media/js/webforms2-0.5.4/webforms2-p.js"></script>
	    <script type="text/javascript" src="media/js/jquery-ui-1.10.2.custom/js/jquery-1.9.1.js"></script>
	    <script type="text/javascript" src="media/js/jquery-ui-1.10.2.custom/js/jquery-ui-1.10.2.custom.min.js"></script>
	    <link rel="stylesheet" href="media/js/jquery-ui-1.10.2.custom/css/ui-lightness/jquery-ui-1.10.2.custom.min.css" />
	    <script type="text/javascript" src="media/js/fallback.js"></script>
	</head>
	<body>
		<div class="group_information">
			<form name="addGroup">
				<xsl:apply-templates/>
			</form>
		</div>
	</body>
	</html>	
</xsl:template>

<xsl:template match="group">
	<h1>Add Group</h1>
	<div class="auto_info">
		<div class="CreatedBy">Created By: Spencer Tickner</div>
		<div class="CreatedDate">Created On: January 1, 2012</div>
		<div class="ModifiedBy">Modified By: Amar Shiota</div>
		<div class="ModifiedDate">Modified On: January 31, 2012</div>
	</div>
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
</xsl:template>

</xsl:stylesheet><!-- Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" url="..\..\..\..\..\..\..\test\input.xml" htmlbaseurl="" outputurl="file:///u:/Projects/Current/QPSecurity/group.html" processortype="saxon8" useresolver="yes"
		          profilemode="0" profiledepth="" profilelength="" urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext=""
		          validateoutput="no" validator="internal" customvalidator="">
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