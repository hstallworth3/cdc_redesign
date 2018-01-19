<!DOCTYPE stylesheet [
	<!ENTITY nbsp "&#160;">
]>
<!--<?xml version="1.0" encoding="UTF-8"?>-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:strip-space elements="*"/>
	
	
	<xsl:key name="SRT_CODES" match="content/selects/select" use="./@name"></xsl:key>


	
	<xsl:template match="content">
	
		

		<content>
			<xsl:if test="./@form">
				<xsl:attribute name="form"><xsl:value-of select="normalize-space(./@form)"/></xsl:attribute>
			</xsl:if>
			<xsl:if test="./@title">
				<xsl:attribute name="title"><xsl:value-of select="normalize-space(./@title)"/></xsl:attribute>
			</xsl:if>

			<xsl:apply-templates>
				
			</xsl:apply-templates>
		</content>
	</xsl:template>
		
		
		
	<xsl:template match="tab">
			
		

			<tab>
				<html>
					<head>				
						<SCRIPT Type="text/javascript" Language="JavaScript" SRC="MergePersonsCompare.js"/>					
					</head>
					<body>
						<table width="600" border="0" cellspacing="0" cellpadding="0" align="center">
							<!--tr>
								<td colspan="3"><b>Please select the record you would like to remain in the system as the Surviving record.</b><br/>
								</td>
							</tr>
							<tr>
								<td colspan="3" height="1" bgcolor="black"><img src="transparent.gif" width="600" height="1" border="0" alt=""/></td>
							</tr>
							<tr>
								<td width="50%"><input name="entityTop1" id="entityTop1" type="checkbox" onclick="clickentityTop1()"><xsl:value-of select="../entity1"></xsl:value-of></input></td>
								<td width="1" bgcolor="black"><img src="transparent.gif" width="1" height="1" border="0" alt=""/></td>
								<td width="50%"><input name="entityTop2" id="entityTop2" type="checkbox" onclick="clickentityTop2()"><xsl:value-of select="../entity2"></xsl:value-of></input></td>
							</tr-->
							
							<!--	header	-->
							<xsl:for-each select="group">
									<tr><td colspan="3" bgcolor="#003470"><center><font class="boldTwelveYellow"><xsl:value-of select="@name"></xsl:value-of></font></center></td></tr>
									<!--	group content	-->
									<tr>
										<!--	column one content		-->
										
										<td valign="top" width="50%">
											<table width="100%" border="0" cellspacing="0" cellpadding="2">
												<xsl:for-each select="column1/line">
													<xsl:call-template name="displayMergeCompareElements"/>
												</xsl:for-each>
											</table>

										</td>
										
										
										
										<td width="1" bgcolor="black"><img src="transparent.gif" width="1" height="1" border="0" alt=""/></td>
										
										
										
										<!--	column two content		-->
										<td  valign="top" width="50%">
											<table width="100%" border="0" cellspacing="0" cellpadding="2">
												<xsl:for-each select="column2/line">
													<xsl:call-template name="displayMergeCompareElements"/>

												</xsl:for-each>
											</table>
										</td>							
									</tr>
							</xsl:for-each>
							<!--tr>
								<td colspan="3" height="1" bgcolor="black"><img src="transparent.gif" width="600" height="1" border="0" alt=""/></td>
							</tr>
							<tr>
								<td width="50%"><input name="entityBottom1" id="entityBottom1" type="checkbox" onclick="clickentityBottom1()"><xsl:value-of select="../entity1"></xsl:value-of></input></td>
								<td width="1" bgcolor="black"><img src="transparent.gif" width="1" height="1" border="0" alt=""/></td>
								<td width="50%"><input name="entityBottom2" id="entityBottom2" type="checkbox" onclick="clickentityBottom2()"><xsl:value-of select="../entity2"></xsl:value-of></input></td>
								
							</tr>
							<tr>
								<td colspan="3" height="1" bgcolor="black"><img src="transparent.gif" width="600" height="1" border="0" alt=""/></td>
							</tr>
							<tr>
								
								<td colspan="3"><b>Please select the record you would like to remain in the system as the Surviving record.</b><br/>
								</td>
								
							</tr-->
						</table>

					</body>
				</html>

			</tab>
		
	</xsl:template>
	
		<xsl:template match="button-bar">
			<button-bar>
				<xsl:attribute name="name"><xsl:value-of select="normalize-space(../tab/@name)"/></xsl:attribute>
				<xsl:for-each select="right">
					<xsl:element name="right">
						<xsl:attribute name="authorized"><xsl:value-of select="normalize-space(./@authorized)"/></xsl:attribute>
						<xsl:element name="label">
							<xsl:value-of select="./label"/>
						</xsl:element>
						<xsl:element name="javascript-action">
							<xsl:value-of select="./javascript-action"/>
						</xsl:element>
					</xsl:element>
				</xsl:for-each>
				<xsl:for-each select="left">
					<xsl:element name="left">
						<xsl:attribute name="authorized"><xsl:value-of select="normalize-space(./@authorized)"/></xsl:attribute>
						<xsl:element name="label">
							<xsl:value-of select="./label"/>
						</xsl:element>
						<xsl:element name="javascript-action">
							<xsl:value-of select="./javascript-action"/>     
						</xsl:element>
					</xsl:element>
				</xsl:for-each>
			</button-bar>
		</xsl:template>
		
		<xsl:template match="link-bar">
			<link-bar>
				<xsl:for-each select="link">
					<xsl:element name="link">
						<xsl:attribute name="name">
							<xsl:value-of select="normalize-space(./@name)"/>
						</xsl:attribute>
						<xsl:value-of select="normalize-space(.)"/>
					</xsl:element>
				</xsl:for-each>
			</link-bar>
		</xsl:template>
	
	
	
	
	
	<xsl:template name="srt-codes-readonly">
		<xsl:param name="delimiter"/>
		<xsl:param name="string"/>
		<xsl:param name="value"/>
		<xsl:choose>
			<xsl:when test="contains($string, $delimiter) ">
				<xsl:call-template name="srt-create-options-readonly">
					<xsl:with-param name="delimiter" select="'$'"/>
					<xsl:with-param name="string" select="substring-before($string, $delimiter)"/>
					<xsl:with-param name="value" select="$value"/>
				</xsl:call-template>
				<xsl:call-template name="srt-codes-readonly">
					<xsl:with-param name="delimiter" select="'|'"/>
					<xsl:with-param name="string" select="substring-after($string, $delimiter)"/>
					<xsl:with-param name="value" select="$value"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="srt-create-options-readonly">
					<xsl:with-param name="delimiter" select="'$'"/>
					<xsl:with-param name="string" select="$string"/>
					<xsl:with-param name="value" select="$value"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="srt-create-options-readonly">
		<xsl:param name="delimiter"/>
		<xsl:param name="string"/>
		<xsl:param name="value"/>
		<xsl:choose>
			<xsl:when test="contains($string, $delimiter) ">
				<xsl:if test="$value=substring-before($string, $delimiter)">
					<xsl:value-of select="substring-after($string, $delimiter)"/>
				</xsl:if>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="displayMergeCompareElements">
		<tr>

			<xsl:variable name="colon">:</xsl:variable>
			<xsl:for-each select="element">
				<xsl:choose>

					<!-- Display LABEL when we have both Label and Value -->
					<xsl:when test="value and string-length(normalize-space(string(@label)))>0">
						<td align="right" VAlign="top" width="50%">
							<xsl:choose>
								<xsl:when test="@srt-name-label">
								<b>
									<xsl:variable name="myVar">

										<xsl:call-template name="srt-codes-readonly">
											<xsl:with-param name="delimiter" select="'|'"/>
											<xsl:with-param name="string" select="normalize-space(key('SRT_CODES',@srt-name-label ))"/>
											<xsl:with-param name="value" select="normalize-space(@label)"/>
										</xsl:call-template>
									</xsl:variable>
									
									<!-- Don't display : if there is no label -->
									<xsl:if test="string-length(normalize-space($myVar))>0">									
										<!-- Don't display : if there is a question mark at the end -->
										<xsl:if test="substring(normalize-space($myVar), string-length(normalize-space($myVar)), 1) != '?'">
											<xsl:value-of select = "normalize-space($myVar)"/>
											<xsl:value-of select = "$colon"/>
										</xsl:if>
									</xsl:if>
								</b>
								</xsl:when>
								<xsl:otherwise>
									<b>
										<xsl:choose>
											<xsl:when test="@label-color">
												<b style="color:Maroon"><xsl:value-of select="normalize-space(@label)"></xsl:value-of></b>											
											</xsl:when>
											<xsl:otherwise>	
												<xsl:value-of select="normalize-space(@label)"></xsl:value-of>
											</xsl:otherwise>													
										</xsl:choose>											
										
										<xsl:if test="substring(normalize-space(@label), string-length(normalize-space(@label)), 1) != '?'">
											<xsl:value-of select = "$colon"/>
										</xsl:if>
									</b>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</xsl:when>
					<!-- Display LABEL when we have Label only, No Value -->
					<xsl:when test="string-length(normalize-space(@label))>0 and not(value)">
						<td align="right" VAlign="top" width="50%">
							<xsl:choose>
								<xsl:when test="@srt-name-label">
								<b>
									<xsl:call-template name="srt-codes-readonly">
										<xsl:with-param name="delimiter" select="'|'"/>
										<xsl:with-param name="string" select="normalize-space(key('SRT_CODES',@srt-name-label ))"/>
										<xsl:with-param name="value" select="normalize-space(@label)"/>
									</xsl:call-template>
								</b>
								</xsl:when>
								<xsl:otherwise><b><xsl:value-of select="@label"></xsl:value-of></b></xsl:otherwise>
							</xsl:choose>
						</td>
					</xsl:when>



				</xsl:choose>


				<xsl:choose>

					<!-- Display VALUE when we have both Label and Value -->
					<xsl:when test="value and string-length(normalize-space(@label))>0">
						<td align="left">	
							&nbsp;
							<!-- Display value -->
							<xsl:call-template name="displayValues"/>	
						</td>																			
					</xsl:when>
					<!-- Display VALUE when we have Value only, No Label -->
					<xsl:when test="value and string-length(normalize-space(@label))=0">
					<td width="43%"></td>
						<td align="left">
							<xsl:attribute name="colspan">4</xsl:attribute>
							  <xsl:call-template name="displayValues"/>
						</td>
					</xsl:when>
				</xsl:choose>


			</xsl:for-each>


		</tr>	
	</xsl:template>					
	
	<xsl:template name="displayValues">
		<xsl:choose>
			<xsl:when test="@srt-name-value">
					<xsl:choose>
					<!--multiple-->
					<xsl:when test="contains(value,'|' )">
								<xsl:variable name="test">
									<xsl:call-template name="multiple-entry-selectbox-display">
										<xsl:with-param name="values" select="normalize-space(value)"/>
										<xsl:with-param name="srts" select="normalize-space(key('SRT_CODES',@srt-name-value ))"/>
									</xsl:call-template>
								</xsl:variable>
								<!--	remove the leading comma and the trailing comma	-->
								<xsl:choose>
									<xsl:when test="starts-with(normalize-space($test),',')">
										<xsl:value-of select="substring(normalize-space(substring-after($test,',')),1,string-length(normalize-space(substring-after($test,',')))-1)"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="substring(normalize-space($test),1,string-length(normalize-space($test))-1)"/>
									</xsl:otherwise>
								</xsl:choose>


					</xsl:when>
					<xsl:otherwise>
								<xsl:call-template name="srt-codes-readonly">
									<xsl:with-param name="delimiter" select="'|'"/>
									<xsl:with-param name="string" select="normalize-space(key('SRT_CODES',@srt-name-value ))"/>
									<xsl:with-param name="value" select="normalize-space(value)"/>
								</xsl:call-template>																			

					</xsl:otherwise>

				</xsl:choose>

			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="string-length(normalize-space(value))&lt;20">
						<nobr><xsl:value-of select="normalize-space(value)"></xsl:value-of></nobr>
					</xsl:when>
					<xsl:otherwise><xsl:value-of select="normalize-space(value)"></xsl:value-of></xsl:otherwise>
				</xsl:choose>
				
				
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>							

	<!--	display values for multiple entry select boxes	-->
	<xsl:template name="multiple-entry-selectbox-display">
		<xsl:param name="values"/>
		<xsl:param name="srts"/>
		<xsl:choose>
			<xsl:when test="contains($values, '|') ">
				<xsl:call-template name="srt-codes">
					<xsl:with-param name="delimiter" select="'|'"/>
					<xsl:with-param name="string" select="normalize-space($srts)"/>
					<xsl:with-param name="value" select="normalize-space(substring-before($values, '|'))"/>
				</xsl:call-template>,
				<xsl:call-template name="multiple-entry-selectbox-display">
					<xsl:with-param name="values" select="normalize-space(substring-after($values, '|'))"/>
					<xsl:with-param name="srts" select="normalize-space($srts)"/>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="srt-codes">
		<xsl:param name="delimiter"/>
		<xsl:param name="string"/>
		<xsl:param name="value"/>
		<xsl:choose>
			<xsl:when test="contains($string, $delimiter) ">
				<xsl:call-template name="srt-create-options">
					<xsl:with-param name="delimiter" select="'$'"/>
					<xsl:with-param name="string" select="substring-before($string, $delimiter)"/>
					<xsl:with-param name="value" select="$value"/>
				</xsl:call-template>
				<xsl:call-template name="srt-codes">
					<xsl:with-param name="delimiter" select="'|'"/>
					<xsl:with-param name="string" select="substring-after($string, $delimiter)"/>
					<xsl:with-param name="value" select="$value"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="srt-create-options">
					<xsl:with-param name="delimiter" select="'$'"/>
					<xsl:with-param name="string" select="$string"/>
					<xsl:with-param name="value" select="$value"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	
	<xsl:template name="srt-create-options">
		<xsl:param name="delimiter"/>
		<xsl:param name="string"/>
		<xsl:param name="value"/>
		<xsl:choose>
			<xsl:when test="contains($string, $delimiter) ">
				<xsl:if test="$value=substring-before($string, $delimiter)">
					<xsl:value-of select="substring-after($string, $delimiter)"/>
				</xsl:if>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
								

</xsl:stylesheet>
