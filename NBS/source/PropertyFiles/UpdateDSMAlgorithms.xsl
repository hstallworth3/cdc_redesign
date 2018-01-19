<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:lab="http://www.cdc.gov/NEDSS" version="1.0" exclude-result-prefixes="xsl lab">
    <xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="UTF-8"/>
    <xsl:strip-space elements="*"/>
    <xsl:template match="lab:ElrTextResultValue" >
            <xsl:choose>
                <xsl:when test="not(lab:ComparatorCode)">
                    <xsl:copy>
                        <ComparatorCode>
                            <Code>=</Code>
                            <CodeDescTxt>Equal</CodeDescTxt>
                        </ComparatorCode>
                        <TextValue>
                            <xsl:value-of select="ElrTextResultValue"/>
                            <xsl:apply-templates/>
                        </TextValue>
                    </xsl:copy>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy>
                        <xsl:apply-templates select="@*|node()"/>
                    </xsl:copy>
                </xsl:otherwise>
            </xsl:choose>
    </xsl:template>
    <xsl:template match="lab:ElrNumericResultValue" >
            <xsl:choose>
                <xsl:when test="not(lab:ComparatorCode)">
                    <xsl:choose>
						<xsl:when test="not(lab:SeperatorCode)">
							<xsl:copy>
								<ComparatorCode>
									<Code>=</Code>
									<CodeDescTxt>Equal</CodeDescTxt>
								</ComparatorCode>
								<xsl:apply-templates select="@*|node()"/>
							</xsl:copy>
						</xsl:when>
						<xsl:otherwise>
							<xsl:copy>
								<ComparatorCode>
									<Code>BET</Code>
									<CodeDescTxt>Between</CodeDescTxt>
								</ComparatorCode>
								<xsl:apply-templates select="@*|node()"/>
							</xsl:copy>
						</xsl:otherwise>
					</xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy>
                        <xsl:apply-templates select="@*|node()"/>
                    </xsl:copy>
                </xsl:otherwise>
            </xsl:choose>
    </xsl:template>
    <xsl:template match="lab:ElrNumericResultValue/lab:ComparatorCode" >
        <xsl:choose>
            <xsl:when test="not(lab:Code)">
                <xsl:copy>
                        <Code>
                            <xsl:value-of select="ComparatorCode"/>
                            <xsl:apply-templates/>
                        </Code>
                </xsl:copy>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="@*|node()"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="lab:CriteriaNumericValue/lab:ComparatorCode" >
        <xsl:choose>
            <xsl:when test="not(lab:Code)">
                <xsl:copy>
                    <Code>
                        <xsl:value-of select="ComparatorCode"/>
                        <xsl:apply-templates/>
                    </Code>
                </xsl:copy>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="@*|node()"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="lab:DefaultNumericValue/lab:ComparatorCode" >
        <xsl:choose>
            <xsl:when test="not(lab:Code)">
                <xsl:copy>
                    <Code>
                        <xsl:value-of select="ComparatorCode"/>
                        <xsl:apply-templates/>
                    </Code>
                </xsl:copy>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="@*|node()"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>