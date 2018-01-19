<!DOCTYPE stylesheet [
	<!ENTITY nbsp "&#160;">
]>
<!--<?xml version="1.0" encoding="UTF-8"?>-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:strip-space elements="*"/>
	<xsl:template match="content">
		<content>
		<xsl:if test="./@form">
				<xsl:attribute name="form"><xsl:value-of select="normalize-space(./@form)"/></xsl:attribute>
			</xsl:if>
			<xsl:if test="./@title">
				<xsl:attribute name="title"><xsl:value-of select="normalize-space(./@title)"/></xsl:attribute>
			</xsl:if>
			
		<xsl:apply-templates/>
		
		<link-bar>
			<link name="New Search"><xsl:value-of select="normalize-space(newSearchHref)"/></link>
			
			<link name="Refine Search"><xsl:value-of select="normalize-space(refineSearchHref)"/></link>
		</link-bar>>
		
		<tab>
		<html><body>
				<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
					<thead>
						<tr>
							<td valign="top">
								<!-- BEGIN Task Bar -->
								
								<!--END Task Bar-->
								<!--\\\\\\\\\\\ BEGIN CONTENT ////////////-->
								
								<xsl:for-each select="text">
									<xsl:choose>
										<xsl:when test="first">
											  <p>
												<div align="center" class="boldTenDkRed">Your Search Criteria: <xsl:value-of select="substring(normalize-space(../searchCriteria),1,string-length(normalize-space(../searchCriteria))-1)"/> 
													resulted in <xsl:choose><xsl:when test="(../totalCounts>../NoOfRows)">more than <xsl:value-of select="normalize-space(../NoOfRows)"></xsl:value-of></xsl:when><xsl:otherwise><xsl:value-of select="../totalCounts"></xsl:value-of></xsl:otherwise></xsl:choose> possible matches.<xsl:if test="(../totalCounts>../NoOfRows)or(../totalCounts=0)or(../totalCounts=1)">
														<br/>Would you like to <a><xsl:attribute name="href"><xsl:value-of select="normalize-space(../refineSearchHref)"/></xsl:attribute>refine your search?</a>
													</xsl:if>
												</div>
											 </p><br/>
										</xsl:when>
										<xsl:when test="second">
											   <div align="left"><b>Number of groups remaining to view for merge: <xsl:value-of select="normalize-space(../groupNo)"></xsl:value-of></b>
									             </div><br/>										
									      </xsl:when>
									      <xsl:when test="third">
											   <div align="left"><b>Candidate group batch process last run: <xsl:value-of select="normalize-space(../groupAOD)"></xsl:value-of></b>
									             </div>	<br/>									
									      </xsl:when>
                                                  <xsl:when test="fifth">
											   <div align="left" ><b>All Candidate Groups must be selected for Merge or No Merge before additional
											     candidate groups can be added by the system.</b>
									             </div><br/>										
									      </xsl:when>
									      <xsl:when test="sixth">
											   <div align="left"><b>No candidate groups are available to view. Contact System Adminstrator to create new candidate groups.</b>
									             </div><br/>										
									      </xsl:when>
									      <xsl:when test="seventh">
											   <div align="center"><b>To compare records, check 2 candidates in the Compare column.</b><br/>
											                                     <b>To merge records, check 2 or more candidates in the Merge column.</b>
									             </div><br/>										
									      </xsl:when>
									       <xsl:when test="eighth">
											   <div align="center"><b>Compare and Merge can not be performed with only one record. Please refine your search or Cancel.</b>
											                                   
									             </div><br/>										
									      </xsl:when>

									</xsl:choose>
								</xsl:for-each>
								
								<!--div align="right">
									<xsl:if test="(currentIndex>0)">
										<a>
											<xsl:attribute name="href"><xsl:value-of select="normalize-space(prevHref)"/>&amp;currentIndex=<xsl:value-of select="normalize-space(currentIndex)-10"/></xsl:attribute>Previous</a>
									</xsl:if>
									<xsl:if test="(currentIndex>0) and (number(totalRecords) > number(currentIndex)+10)">
										&nbsp;|&nbsp;
									</xsl:if>
									<xsl:if test="number(totalRecords) > number(currentIndex)+10">
										<a>
											<xsl:attribute name="href"><xsl:value-of select="normalize-space(nextHref)"/>&amp;currentIndex=<xsl:value-of select="normalize-space(currentIndex)+10"/></xsl:attribute>Next</a>
									</xsl:if>
								</div-->
							</td>
						</tr>
					</thead>
					<tbody><tr><td>
										<table class="TableOuter" width="100%" border="0"><tr><td>
											<table cellpadding="4" cellspacing="0" border="0" width="100%" class="TableInner" id="searchResultsTable">

												<thead class="nestedElementsTypeFootHeader">
													<tr class="Shaded">
													<xsl:for-each select="header/script">
														<script type="text/javascript">
															<xsl:value-of select="text()" disable-output-escaping="yes"/>
														</script>
													</xsl:for-each>
														<xsl:for-each select="header/column">
															<td class="ColumnHeader" align="left">
																<xsl:choose>
																	<xsl:when test="link">
																		<a class="boldTenBlack">
																			<xsl:attribute name="href"><xsl:value-of select="normalize-space(./link)"/></xsl:attribute>
																			<xsl:value-of select="normalize-space(./name)"/>
																		</a>
																	</xsl:when>
																	<xsl:otherwise>
																		<b><xsl:value-of select="normalize-space(./name)"/></b>
																	</xsl:otherwise>
																</xsl:choose>
																
															</td>
														</xsl:for-each>
													</tr>
												</thead>

												<tbody>										
													<xsl:choose>
														<xsl:when test="records/record">
															<xsl:for-each select="records/record">
																<xsl:variable name="mpr"><xsl:value-of select="normalize-space(uid)"/></xsl:variable>
																<xsl:variable name="className">
																	<!-- VL code to alternate color of data lines -->
																		<xsl:choose>
																			<xsl:when test=" position() mod 2">NotShaded</xsl:when>
																			<xsl:otherwise>Shaded</xsl:otherwise>
																		</xsl:choose>
																</xsl:variable>
																<tr valign="top">
																	<!--xsl:attribute name="onclick">reorderBatchEntry(this);</xsl:attribute-->
																	<xsl:attribute name="class"><xsl:value-of select="$className"></xsl:value-of></xsl:attribute>
																<!--		view link		-->
																	<xsl:for-each select="column">
																		<xsl:choose>
																	   <!-- checkbox-->
																	    <xsl:when test="checkbox">
																	    		<td>
																	    		<table border="0" cellspacing="0" cellpadding="2">
													                                    <tr>
													                                     <td valign="top" align="left">
																				 <xsl:for-each select="checkbox">
																					<xsl:element name="input">
																				       <xsl:attribute name="type">checkbox</xsl:attribute>
																				       <xsl:attribute name="name"><xsl:value-of select="normalize-space(@name)"/></xsl:attribute>
																				       <xsl:attribute name="id"><xsl:value-of select="normalize-space(@name)"/></xsl:attribute>
																				       <xsl:attribute name="value"><xsl:value-of select="normalize-space(value)"/></xsl:attribute>
																				        <xsl:attribute name="onclick">CountChecked(this);</xsl:attribute>

																				       <xsl:if test="@default='yes'">
																				           <xsl:attribute name="checked">checked</xsl:attribute>
																				       </xsl:if>
																				   </xsl:element>
																			    </xsl:for-each>
																			    </td>
																			       <xsl:if test="../nested-records/record">
																					<td valign="middle" align="center"><img src="plus_sign.gif" border="0" alt=""><xsl:attribute name="onclick">searchResultsControlNestedRows(<xsl:value-of select="$mpr"/>, this);</xsl:attribute></img></td>
	  																			</xsl:if>
																		        </tr></table>
																		        </td>
																		</xsl:when>
																		</xsl:choose>
																		</xsl:for-each >

																	
																	<xsl:call-template name="create-data-columns"></xsl:call-template>
																	
																	
																	
																	<xsl:for-each select="column">
																	    <xsl:choose>
																			<xsl:when test="option1">
																		    		<td valign="top" align="left">
																					 <xsl:for-each select="option1">
																						<xsl:element name="input">
																				                    <xsl:attribute name="type">checkbox</xsl:attribute>
																					           <xsl:attribute name="name"><xsl:value-of select="normalize-space(@name)"/></xsl:attribute>
																					           <xsl:attribute name="value"><xsl:value-of select="normalize-space(value)"/></xsl:attribute>
																					           <xsl:attribute name="id"><xsl:value-of select="normalize-space(@name)"/></xsl:attribute>
																					            <xsl:if test="@default='yes'">
																					           <xsl:attribute name="checked">checked</xsl:attribute>
																					       </xsl:if>
																					   </xsl:element>
																				    </xsl:for-each>
																			   </td>
																			</xsl:when>
																	</xsl:choose>
																 </xsl:for-each >
																<xsl:for-each select="column">
																	<xsl:choose>
																			<xsl:when test="option2">
																				<td valign="top" align="left">
																					<xsl:for-each select="option2">
																						<xsl:element name="input">
																							<xsl:attribute name="type">checkbox</xsl:attribute>
																							<xsl:attribute name="name"><xsl:value-of select="normalize-space(@name)"/></xsl:attribute>
																							<xsl:attribute name="id"><xsl:value-of select="normalize-space(@name)"/></xsl:attribute>
																							<xsl:attribute name="value"><xsl:value-of select="normalize-space(value)"/></xsl:attribute>
																							<xsl:attribute name="onclick">CountSurvivorChecked(this);</xsl:attribute>
																						</xsl:element>
																						<xsl:copy-of select="."/>
																					</xsl:for-each>
																				</td>												
																			</xsl:when>
																	</xsl:choose>
																</xsl:for-each>	
																										
																	<!-- create the hidden revisions	-->
																	
																</tr>
																<!-- create the hidden revisions	-->
															
																<xsl:for-each select="nested-records/record">
																	<tr class="none" >
																		<xsl:attribute name="bgcolor">
																			<xsl:choose>
																				<xsl:when test="$className='Shaded' ">#C0C0C0</xsl:when>
																				<xsl:otherwise>#DCDCDC</xsl:otherwise>
																			</xsl:choose>	
																		</xsl:attribute>

																			
																		<xsl:attribute name="mpr"><xsl:value-of select="$mpr"></xsl:value-of></xsl:attribute>
																		<td><!-- place holder for the view links-->&nbsp;</td>
																		<xsl:call-template name="create-data-columns"></xsl:call-template>
																	</tr>
																</xsl:for-each>
																				
															</xsl:for-each>
														</xsl:when>
														<xsl:otherwise>
															<TR>
																<TD ALIGN="LEFT" VALIGN="MIDDLE" colspan="100%">
																	<FONT SIZE="2" COLOR="#3131B1" FACE="Helvetica">
																		<SPAN STYLE="font-size:12;">There is no information to display</SPAN>
																	</FONT>
																</TD>
															</TR>
														</xsl:otherwise>
													</xsl:choose>
												</tbody>
										</table>							
									</td>									
							</tr>
						</table>
						</td></tr></tbody>								
						</table>
							<!--div align="right">
								<xsl:if test="(currentIndex>0)">
									<a>
										<xsl:attribute name="href"><xsl:value-of select="normalize-space(prevHref)"/>&amp;currentIndex=<xsl:value-of select="normalize-space(currentIndex)-10"/></xsl:attribute>Previous</a>
								</xsl:if>
								<xsl:if test="(currentIndex>0) and (number(totalRecords) > number(currentIndex)+10)">
									&nbsp;|&nbsp;
								</xsl:if>
								<xsl:if test="number(totalRecords) > number(currentIndex)+10">
									<a>
										<xsl:attribute name="href"><xsl:value-of select="normalize-space(nextHref)"/>&amp;currentIndex=<xsl:value-of select="normalize-space(currentIndex)+10"/></xsl:attribute>Next</a>
								</xsl:if>
							</div-->
					</body>
				</html>
			</tab>
		</content>
		
	</xsl:template>
	
	<xsl:template name="create-data-columns">
							<xsl:for-each select="column">
									<xsl:choose>
									<!-- name	-->
									<xsl:when test="name">
										<td>
										<xsl:for-each select="name">
												<table border="0" cellspacing="0" cellpadding="2">
													<tr><td colspan="100%"><i><xsl:value-of select="normalize-space(use-code)"/></i></td></tr>
													<tr><td><xsl:value-of select="normalize-space(last-name)"/><xsl:if test="string-length(normalize-space(first-name))!=0">, </xsl:if></td></tr>
													<tr>
														<td><xsl:value-of select="normalize-space(first-name)"/></td>
													</tr>
												</table>
											</xsl:for-each>
										</td>

									</xsl:when>
									<!--	address	-->
									<xsl:when test="address">
										<td>
											<xsl:for-each select="address">
												<table border="0" cellspacing="0" cellpadding="2">
													<tr><td><i><xsl:value-of select="normalize-space(use-code)"/> <xsl:if test="string-length(normalize-space(type-code))!=0"> - </xsl:if> <xsl:value-of select="normalize-space(type-code)"/></i></td></tr>
													<tr><td><xsl:value-of select="normalize-space(street-address1)"/></td></tr>
													<tr><td><xsl:value-of select="normalize-space(city)"/><xsl:if test="string-length(normalize-space(state))!=0">, </xsl:if>&nbsp;<xsl:value-of select="normalize-space(state)"/>&nbsp;<xsl:value-of select="normalize-space(zip)"/></td></tr>
												</table>
											</xsl:for-each>
										</td>
									</xsl:when>

									<!--	telephone	-->
									<xsl:when test="telephone|id">
										<td>
											<xsl:for-each select="telephone">
												<table border="0" cellspacing="0" cellpadding="2">
													<tr><td><i><xsl:value-of select="normalize-space(use-code)"/><xsl:if test="string-length(normalize-space(type-code))!=0"> - </xsl:if><xsl:value-of select="normalize-space(type-code)"/></i></td></tr>
													<tr><td><xsl:value-of select="normalize-space(telephone-number)"/></td></tr>
												</table>
											</xsl:for-each>
											<xsl:for-each select="id">
												<table border="0" cellspacing="0" cellpadding="2">
													<tr><td><i><xsl:value-of select="normalize-space(use-code)"/> </i></td></tr>
													  <tr><td><xsl:value-of select="normalize-space(local-id)"/></td></tr>
												</table>
											</xsl:for-each>
										</td>
									</xsl:when>
									<!--	id	-->
									<!--xsl:when test="id">
										<td>
										</td>
									</xsl:when-->
									<xsl:when test="checkbox"></xsl:when>
									<xsl:when test="option1"></xsl:when>
									<xsl:when test="option2"></xsl:when>
									<xsl:otherwise>
										<td>
											<!--xsl:value-of select="column" disable-output-escaping="yes"/-->
											<xsl:copy-of select="."/>
										</td>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:for-each>

		
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
	
	<xsl:template match="javascript-files">
		<javascript-files>
			<xsl:for-each select="import">
				<xsl:element name="import">
					<xsl:value-of select="."/>
				</xsl:element>
			</xsl:for-each>
		</javascript-files>
	</xsl:template>
</xsl:stylesheet>
