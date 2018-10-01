<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:text="java://eu.europa.ec.joinup.tsl.business.xslt.Translator">

    <xsl:output method="xml" indent="yes" />

    <xsl:template match="/root">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="body-even" page-width="29.7cm" page-height="21.0cm" margin-top="0.5cm" margin-left="2cm" margin-right="2cm"
                    margin-bottom="2cm">
                    <fo:region-body region-name="xsl-region-body" />
                    <fo:region-after region-name="xsl-region-after" />
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="body-even">
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block font-size="7pt" text-align="right" padding-bottom="5pt" padding-top="3pt">
                        <xsl:value-of select="@countryName" />
                        |
                        <xsl:value-of select=" text:translate('pdf.generatedOn') " />
                        <xsl:value-of select="@generationDate" />
                        | Page
                        <fo:page-number />
                        of
                        <fo:page-number-citation ref-id="lastPage" />
                    </fo:block>
                </fo:static-content>
                <!-- font-family="Times New Roman" -->
                <fo:flow flow-name="xsl-region-body">
                    <xsl:call-template name="intro"></xsl:call-template>
                    <xsl:call-template name="changeTemplate"></xsl:call-template>
                    <xsl:call-template name="checkTemplate"></xsl:call-template>
                    <fo:block id="lastPage"></fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

    <xsl:template name="intro">

        <fo:table page-break-after="avoid" border="solid black" margin-top="10pt">
            <fo:table-column column-number="1" column-width="750pt" />
            <fo:table-body>
                <fo:table-row background-color="#bfbfbf" border="solid black">
                    <fo:table-cell>
                        <fo:block text-align="center" font-weight="bold" font-size="18pt">
                            <xsl:value-of select="text:translatePdfTitle(@countryName) " />
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>

        <!-- Published/Draft -->
        <fo:table page-break-after="avoid" border="solid black" margin-top="10pt">
            <fo:table-column column-number="1" column-width="750pt" />
            <fo:table-body>
                <fo:table-row background-color="#bfbfbf" border="solid black">
                    <fo:table-cell>
                        <fo:block text-align="center" font-weight="bold" font-size="16pt">
                            <xsl:value-of select="text:translate('pdf.summary')" />
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>

        <fo:table page-break-after="avoid" border="solid black" margin-top="10pt">
            <fo:table-column column-number="1" column-width="150pt" />
            <fo:table-column column-number="2" column-width="300pt" />
            <fo:table-column column-number="3" column-width="300pt" />
            <fo:table-body>
                <!-- TL Information -->
                <fo:table-row border="solid black" background-color="#E0E0E0">
                    <fo:table-cell number-columns-spanned="3" border="solid black" padding="2pt" margin-left="1pt" font-weight="bold">
                        <fo:block>
                            TL INFORMATION
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <!-- Published/Draft/Previous -->
                <fo:table-row border="solid black" background-color="#E0E0E0" font-weight="bold">
                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <fo:block>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <fo:block>
                            <fo:inline>
                                <xsl:value-of select="text:translateComparedProduction(@production)" />
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <fo:block>
                            <fo:inline>
                                <xsl:value-of select="text:translateProduction(@production)" />
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <!-- Sequence Number -->
                <fo:table-row font-size="7pt">
                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <fo:block>
                            <xsl:value-of select="text:translate('pdf.sequence.number')" />
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <fo:block>
                            <xsl:value-of select="/root/comparedTL/sequenceNumber" />
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <fo:block>
                            <xsl:value-of select="/root/currentTL/sequenceNumber" />
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <!-- Issue Date -->
                <fo:table-row font-size="7pt">
                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <fo:block>
                            <xsl:value-of select="text:translate('pdf.issue.date')" />
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <fo:block>
                            <xsl:value-of select="/root/comparedTL/issueDate" />
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <fo:block>
                            <xsl:value-of select="/root/currentTL/issueDate" />
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <!-- Next Update -->
                <fo:table-row font-size="7pt">
                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <fo:block>
                            <xsl:value-of select="text:translate('pdf.next.date')" />
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <fo:block>
                            <xsl:value-of select="/root/comparedTL/nextUpdate" />
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <fo:block>
                            <xsl:value-of select="/root/currentTL/nextUpdate" />
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <!-- Signature Info -->
                <fo:table-row font-size="7pt">
                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <fo:block>
                            <xsl:value-of select="text:translate('pdf.signatureStatus')" />
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <xsl:if test="/root/comparedTL/signature/indication = 'VALID'">
                            <fo:block color="green">
                                <xsl:value-of select="/root/comparedTL/signature/indication" />
                            </fo:block>
                        </xsl:if>
                        <xsl:if test="/root/comparedTL/signature/indication != 'VALID'">
                            <fo:block>
                                <fo:inline color="red">
                                    <xsl:value-of select="/root/comparedTL/signature/indication" />
                                </fo:inline>
                                <xsl:if test="/root/comparedTL/signature/subIndication and /root/comparedTL/signature/subIndication!=''">
                                    <fo:inline>
                                        (
                                        <xsl:value-of select="/root/comparedTL/signature/subIndication" />
                                        )
                                    </fo:inline>
                                </xsl:if>
                            </fo:block>
                        </xsl:if>
                        <xsl:if test="not(/root/comparedTL)">
                            <fo:block>
                            </fo:block>
                        </xsl:if>
                    </fo:table-cell>

                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <xsl:if test="/root/currentTL/signature/indication = 'VALID'">
                            <fo:block color="green">
                                <xsl:value-of select="/root/currentTL/signature/indication" />
                            </fo:block>
                        </xsl:if>
                        <xsl:if test="/root/currentTL/signature/indication != 'VALID'">
                            <fo:block>
                                <fo:inline color="red">
                                    <xsl:value-of select="/root/currentTL/signature/indication" />
                                </fo:inline>
                                <xsl:if test="/root/currentTL/signature/subIndication and /root/currentTL/signature/subIndication!=''">
                                    <fo:inline margin-left="3pt">
                                        (
                                        <xsl:value-of select="/root/currentTL/signature/subIndication" />
                                        )
                                    </fo:inline>
                                </xsl:if>
                            </fo:block>
                        </xsl:if>
                    </fo:table-cell>
                </fo:table-row>
                <!-- Availability Status -->
                <fo:table-row font-size="7pt">
                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <fo:block>
                            <xsl:value-of select="text:translate('pdf.availabilityStatus')" />
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <xsl:if test="/root/comparedTL/availability = 'AVAILABLE'">
                            <fo:block color="green">
                                <xsl:value-of select="/root/comparedTL/availability" />
                            </fo:block>
                        </xsl:if>
                        <xsl:if test="/root/comparedTL/availability = 'UNSUPPORTED' or /root/comparedTL/availability = 'UNAVAILABLE'">
                            <fo:block color="red">
                                <xsl:value-of select="/root/comparedTL/availability" />
                            </fo:block>
                        </xsl:if>
                        <xsl:if test="/root/comparedTL/availability = '/'">
                            <fo:block>
                                <xsl:value-of select="/root/comparedTL/availability" />
                            </fo:block>
                        </xsl:if>
                        <xsl:if test="not(/root/comparedTL)">
                            <fo:block>
                            </fo:block>
                        </xsl:if>
                    </fo:table-cell>

                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <xsl:if test="/root/currentTL/availability = 'AVAILABLE'">
                            <fo:block color="green">
                                <xsl:value-of select="/root/currentTL/availability" />
                            </fo:block>
                        </xsl:if>
                        <xsl:if test="/root/currentTL/availability = 'UNSUPPORTED' or /root/currentTL/availability = 'UNAVAILABLE'">
                            <fo:block color="red">
                                <xsl:value-of select="/root/currentTL/availability" />
                            </fo:block>
                        </xsl:if>
                        <xsl:if test="/root/currentTL/availability = '/'">
                            <fo:block>
                                <xsl:value-of select="/root/currentTL/availability" />
                            </fo:block>
                        </xsl:if>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>


        <!-- Changes Resume -->
        <xsl:if test="@numberOfChanges > 0">
            <fo:table page-break-after="avoid" margin-top="15pt">
                <fo:table-column column-number="1" column-width="150pt" />
                <fo:table-column column-number="2" column-width="600pt" />
                <fo:table-body>
                    <fo:table-row border="solid black" background-color="#E0E0E0">
                        <fo:table-cell number-columns-spanned="2">
                            <fo:block font-weight="bold" padding="5pt" margin-left="3pt">
                                <xsl:value-of select="text:translate('pdf.changes')" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <!-- Signature -->
                    <fo:table-row font-size="7pt">
                        <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                            <fo:block>
                                <xsl:value-of select="text:translate('tSignature')" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                            <fo:block>
                                <xsl:value-of select="@signatureChanges" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <!-- Scheme Information -->
                    <fo:table-row font-size="7pt">
                        <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                            <fo:block>
                                <xsl:value-of select="text:translate('pdf.scheme')" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                            <fo:block>
                                <xsl:value-of select="@schemeChanges" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <!-- Pointers -->
                    <fo:table-row font-size="7pt">
                        <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                            <fo:block>
                                <xsl:value-of select="text:translate('pdf.pointer')" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                            <fo:block>
                                <xsl:value-of select="@pointerChanges" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <!-- Service -->
                    <fo:table-row font-size="7pt">
                        <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                            <fo:block>
                                <xsl:value-of select="text:translate('pdf.service')" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                            <fo:block>
                                <xsl:value-of select="@serviceChanges" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </xsl:if>

        <!-- Checks Resume -->
        <fo:table page-break-after="avoid" margin-top="15pt">
            <fo:table-column column-number="1" column-width="150pt" />
            <fo:table-column column-number="2" column-width="600pt" />
            <fo:table-body>
                <fo:table-row border="solid black" background-color="#E0E0E0">
                    <fo:table-cell number-columns-spanned="2">
                        <fo:block padding="5pt" margin-left="3pt">
                            <fo:inline font-weight="bold">
                                <xsl:value-of select="text:translate('pdf.checks')" />
                            </fo:inline>
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="text:translateChecksResume(/root/currentTL/lastTimeChecked)" />
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <xsl:if test="@numberOfCheck =0">
                    <fo:table-row>
                        <fo:table-cell border="solid black" number-columns-spanned="2" margin-left="3pt" padding="2pt">
                            <fo:block font-size="7pt">
                                <xsl:value-of select="text:translate('tNotification.checkSuccess')"></xsl:value-of>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </xsl:if>
                <xsl:if test="@numberOfCheck > 0">
                    <!-- Checks -->
                    <fo:table-row font-size="7pt">
                        <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                            <fo:block>
                                <xsl:value-of select="text:translate('pdf.errors')" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                            <fo:block>
                                <xsl:value-of select="@errorChecks" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <!-- Warning -->
                    <fo:table-row font-size="7pt">
                        <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                            <fo:block>
                                <xsl:value-of select="text:translate('pdf.warning')" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                            <fo:block>
                                <xsl:value-of select="@warningChecks" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <!-- Info -->
                    <fo:table-row font-size="7pt">
                        <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                            <fo:block>
                                <xsl:value-of select="text:translate('pdf.info')" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                            <fo:block>
                                <xsl:value-of select="@infoChecks" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </xsl:if>
            </fo:table-body>
        </fo:table>

    </xsl:template>


    <xsl:template name="checkTemplate">
        <xsl:if test="@numberOfCheck > 0">
            <fo:block page-break-before="always"></fo:block>
            <fo:table page-break-after="avoid" border="solid black" margin-top="10pt">
                <fo:table-column column-number="1" column-width="750pt" />
                <fo:table-body>
                    <fo:table-row background-color="#bfbfbf" border="solid black">
                        <fo:table-cell>
                            <fo:block text-align="center" font-weight="bold" font-size="16pt">
                                <xsl:value-of select="text:translate('pdf.checks')" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>

            <xsl:apply-templates select="/root/tlChecks"></xsl:apply-templates>
            <xsl:apply-templates select="/root/signatureChecks"></xsl:apply-templates>
            <xsl:apply-templates select="/root/schemeInformationChecks"></xsl:apply-templates>
            <xsl:apply-templates select="/root/pointersToOtherTslChecks"></xsl:apply-templates>
            <xsl:apply-templates select="/root/serviceProviderChecks"></xsl:apply-templates>
        </xsl:if>
    </xsl:template>

    <xsl:template name="changeTemplate">
        <xsl:if test="@numberOfChanges > 0">
            <fo:block page-break-before="always"></fo:block>
            <fo:table page-break-after="avoid" border="solid black" margin-top="10pt">
                <fo:table-column column-number="1" column-width="750pt" />
                <fo:table-body>
                    <fo:table-row background-color="#bfbfbf" border="solid black">
                        <fo:table-cell>
                            <fo:block text-align="center" font-weight="bold" font-size="16pt">
                                <xsl:value-of select="text:translate('pdf.changes')" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
            <xsl:apply-templates select="/root/signatureListChanges"></xsl:apply-templates>
            <xsl:apply-templates select="/root/schemeInformationChanges"></xsl:apply-templates>
            <xsl:apply-templates select="/root/pointersToOtherTslChanges"></xsl:apply-templates>
            <xsl:apply-templates select="/root/serviceProviderChanges"></xsl:apply-templates>
        </xsl:if>
    </xsl:template>

    <!-- CHECKS -->
    <xsl:template match="tlChecks">
        <xsl:variable name="content">
            <xsl:value-of select="." />
        </xsl:variable>
        <xsl:if test="$content != ''">
            <fo:table border="solid black" text-align="center" keep-together="always">
                <fo:table-column column-number="1" column-width="750pt" />
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell border="hidden" border-bottom="solid" padding="2pt" margin-left="1pt">
                            <fo:block>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row border="solid black">
                        <fo:table-cell text-align="left" font-weight="bold" background-color="#E0E0E0" padding="2pt" margin-left="1pt">
                            <fo:block>
                                <xsl:value-of select="text:translate('tGlobal')" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
            <xsl:call-template name="table"></xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template match="signatureChecks">
        <xsl:variable name="content">
            <xsl:value-of select="." />
        </xsl:variable>
        <xsl:if test="$content != ''">
            <fo:table border="solid black" text-align="center" keep-together="always">
                <fo:table-column column-number="1" column-width="750pt" />
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell border="hidden" border-bottom="solid" padding="2pt" margin-left="1pt">
                            <fo:block>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row border="solid black">
                        <fo:table-cell text-align="left" font-weight="bold" background-color="#E0E0E0" padding="2pt" margin-left="1pt">
                            <fo:block>
                                <xsl:value-of select="text:translate('signatureInformationMenu')" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
            <xsl:call-template name="table"></xsl:call-template>
        </xsl:if>
    </xsl:template>


    <xsl:template match="schemeInformationChecks">
        <xsl:variable name="content">
            <xsl:value-of select="." />
        </xsl:variable>
        <xsl:if test="$content != ''">
            <fo:table border="solid black" text-align="center" keep-together="always">
                <fo:table-column column-number="1" column-width="750pt" />
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell border="hidden" border-bottom="solid" padding="2pt" margin-left="1pt">
                            <fo:block>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row border="solid black">
                        <fo:table-cell text-align="left" font-weight="bold" background-color="#E0E0E0" padding="2pt" margin-left="1pt">
                            <fo:block>
                                <xsl:value-of select="text:translate('schemeInformationMenu')" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
            <xsl:call-template name="table"></xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template match="pointersToOtherTslChecks">
        <xsl:variable name="content">
            <xsl:value-of select="." />
        </xsl:variable>
        <xsl:if test="$content != ''">
            <fo:table border="solid black" text-align="center" keep-together.within-page="always" keep-with-next="always" page-break-after="avoid">
                <fo:table-column column-number="1" column-width="750pt" />
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell border="hidden" border-bottom="solid">
                            <fo:block padding="5pt">
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row border="solid black">
                        <fo:table-cell text-align="left" font-weight="bold" background-color="#E0E0E0">
                            <fo:block padding="5pt" margin-left="1pt" margin-right="1pt">
                                <xsl:value-of select="text:translate('ptotslMenu')" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
            <xsl:call-template name="table"></xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template match="serviceProviderChecks">
        <xsl:variable name="content">
            <xsl:value-of select="." />
        </xsl:variable>
        <xsl:if test="$content != ''">
            <fo:table border="solid black" text-align="center" keep-together="always">
                <fo:table-column column-number="1" column-width="750pt" />
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell border="hidden" border-bottom="solid">
                            <fo:block padding="5pt">
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                        <fo:table-cell text-align="left" font-weight="bold" background-color="#E0E0E0">
                            <fo:block padding="5pt" margin-left="1pt" margin-right="1pt">
                                <xsl:value-of select="text:translate('serviceProvider')" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
            <xsl:call-template name="table"></xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template name="table">
        <fo:table border="solid black" text-align="center">
            <fo:table-column column-number="1" column-width="400pt" />
            <fo:table-column column-number="2" column-width="50pt" />
            <fo:table-column column-number="3" column-width="300pt" />
            <fo:table-body>
                <fo:table-row border="solid black" font-weight="bold" background-color="#E0E0E0" page-break-after="avoid">
                    <fo:table-cell border="solid black" text-align="left" padding="2pt" margin-left="1pt">
                        <fo:block>Location</fo:block>
                    </fo:table-cell>
                    <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                        <fo:block>Status</fo:block>
                    </fo:table-cell>
                    <fo:table-cell border="solid black" text-align="left" padding="2pt" margin-left="1pt">
                        <fo:block>Check</fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <xsl:for-each select="grouped-checks">
                    <xsl:variable name="rowSpanned" select="number" />
                    <fo:table-row border="solid black" font-size="7pt" page-break-inside="avoid" keep-together.within-page="always">
                        <fo:table-cell border="solid black" text-align="left" display-align="before" number-rows-spanned="{$rowSpanned} + 1">
                            <fo:block padding="5pt" margin-left="1pt" margin-right="1pt">
                                <xsl:call-template name="locationId">
                                    <xsl:with-param name="id" select="tlLocationId" />
                                </xsl:call-template>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <xsl:for-each select="checks/check">
                        <xsl:variable name="colorValue">
                            <xsl:choose>
                                <xsl:when test="status = 'ERROR'">
                                    #FF0000
                                </xsl:when>
                                <xsl:when test="status = 'WARNING'">
                                    #FF9933
                                </xsl:when>
                                <xsl:otherwise>
                                    #a3a3c2
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:variable>
                        <fo:table-row font-size="7pt" keep-together.within-page="always">
                            <fo:table-cell border="solid black" text-align="center" display-align="center" background-color="{$colorValue}">
                                <fo:block padding="5pt" margin-left="1pt" margin-right="1pt">
                                    <xsl:value-of select="status" />
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell border="solid black" text-align="left" display-align="center">
                                <fo:block padding="5pt" margin-left="1pt" margin-right="1pt">
                                    <xsl:value-of select="description" />
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </xsl:for-each>
                </xsl:for-each>
            </fo:table-body>
        </fo:table>
    </xsl:template>

    <xsl:template name="locationId">
        <xsl:param name="id" />
        <xsl:variable name="first" select="substring-after($id, '||')" />
        <xsl:variable name="location" select="$first" />
        <xsl:choose>
            <xsl:when test="contains($first, '||')">
                <fo:block>
                    <xsl:value-of select="substring-before($first, '||')"></xsl:value-of>
                </fo:block>
            </xsl:when>
            <xsl:otherwise>
                <fo:block>
                    <xsl:value-of select="$first"></xsl:value-of>
                </fo:block>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:if test="contains($location, '||')">
            <xsl:variable name="second" select="substring-after($location, '||')" />
            <xsl:variable name="locationSecond" select="$second" />
            <fo:block margin-left="10" vertical-align="top">
                <fo:external-graphic
                    src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACEAAAAZCAYAAAC/zUevAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAEnQAABJ0Ad5mH3gAAAEgSURBVEhL7ZXbCYUwDIbPTg7gAA7gAr67gAP47rvvDuAADuAAruASOfzSHqSkSeoFPOAHAfHSfk3S+qEH8Ep4/kNiWRZ3JTMMwxZHUCXatnVXMl3XUZZl1DSNu2NHlaiqyrRCL4HAN+u6uic6qgRWhoExicReAlEUBc3z7J7KqBLIwn7gvu/ZPgklEHme0ziO7o04qgTSisHCCcqy3PoFUtM0/TLGhZZFVQJwq0wNSMb6xCSBj1EKbvCUQPa4UpokAJqMK0tqcKUxSwCsAqvhBrdE7AxJkvBgx+As4CaKhXTWHJLwoFewM5DimBRKiHckTkns4XZQrBFDbpOo69p8dN8iwe0AiUslUP8jv/PLJNB81h9WyGUSZ3glPA+QIPoCV/B/ozWT9KMAAAAASUVORK5CYII="
                    content-width="scale-to-fit" height="7pt" scaling="uniform" />
                <xsl:choose>
                    <xsl:when test="contains($second, '||')">
                        <xsl:value-of select="substring-before($second, '||')" />
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$second"></xsl:value-of>
                    </xsl:otherwise>
                </xsl:choose>
            </fo:block>
            <xsl:if test="contains($locationSecond, '||')">
                <xsl:variable name="third" select="substring-after($locationSecond, '||')" />
                <xsl:variable name="locationThird" select="$third" />
                <fo:block white-space-collapse="false" white-space-treatment="preserve" linefeed-treatment="preserve" margin-left="20pt">
                    <fo:inline></fo:inline>
                    <fo:external-graphic
                        src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACEAAAAZCAYAAAC/zUevAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAEnQAABJ0Ad5mH3gAAAEgSURBVEhL7ZXbCYUwDIbPTg7gAA7gAr67gAP47rvvDuAADuAAruASOfzSHqSkSeoFPOAHAfHSfk3S+qEH8Ep4/kNiWRZ3JTMMwxZHUCXatnVXMl3XUZZl1DSNu2NHlaiqyrRCL4HAN+u6uic6qgRWhoExicReAlEUBc3z7J7KqBLIwn7gvu/ZPgklEHme0ziO7o04qgTSisHCCcqy3PoFUtM0/TLGhZZFVQJwq0wNSMb6xCSBj1EKbvCUQPa4UpokAJqMK0tqcKUxSwCsAqvhBrdE7AxJkvBgx+As4CaKhXTWHJLwoFewM5DimBRKiHckTkns4XZQrBFDbpOo69p8dN8iwe0AiUslUP8jv/PLJNB81h9WyGUSZ3glPA+QIPoCV/B/ozWT9KMAAAAASUVORK5CYII="
                        content-width="scale-to-fit" height="7pt" scaling="uniform" />
                    <xsl:choose>
                        <xsl:when test="contains($third, '||')">
                            <xsl:value-of select="substring-before($third, '||')" />
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="$third"></xsl:value-of>
                        </xsl:otherwise>
                    </xsl:choose>
                </fo:block>
                <xsl:if test="contains($locationThird, '||')">
                    <xsl:variable name="four" select="substring-after($locationThird, '||')" />
                    <xsl:variable name="locationFour" select="$four" />
                    <fo:block white-space-collapse="false" white-space-treatment="preserve" linefeed-treatment="preserve" margin-left="30pt">
                        <fo:inline></fo:inline>
                        <fo:external-graphic
                            src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACEAAAAZCAYAAAC/zUevAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAEnQAABJ0Ad5mH3gAAAEgSURBVEhL7ZXbCYUwDIbPTg7gAA7gAr67gAP47rvvDuAADuAAruASOfzSHqSkSeoFPOAHAfHSfk3S+qEH8Ep4/kNiWRZ3JTMMwxZHUCXatnVXMl3XUZZl1DSNu2NHlaiqyrRCL4HAN+u6uic6qgRWhoExicReAlEUBc3z7J7KqBLIwn7gvu/ZPgklEHme0ziO7o04qgTSisHCCcqy3PoFUtM0/TLGhZZFVQJwq0wNSMb6xCSBj1EKbvCUQPa4UpokAJqMK0tqcKUxSwCsAqvhBrdE7AxJkvBgx+As4CaKhXTWHJLwoFewM5DimBRKiHckTkns4XZQrBFDbpOo69p8dN8iwe0AiUslUP8jv/PLJNB81h9WyGUSZ3glPA+QIPoCV/B/ozWT9KMAAAAASUVORK5CYII="
                            content-width="scale-to-fit" height="7pt" scaling="uniform" />
                        <xsl:choose>
                            <xsl:when test="contains($four, '||')">
                                <xsl:value-of select="substring-before($four, '||')" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="$four"></xsl:value-of>
                            </xsl:otherwise>
                        </xsl:choose>
                    </fo:block>
                    <xsl:if test="contains($locationFour, '||')">
                        <xsl:variable name="five" select="substring-after($locationFour, '||')" />
                        <xsl:variable name="locationFour" select="$five" />
                        <fo:block white-space-collapse="false" white-space-treatment="preserve" linefeed-treatment="preserve" margin-left="40pt">
                            <fo:inline></fo:inline>
                            <fo:external-graphic
                                src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACEAAAAZCAYAAAC/zUevAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAAEnQAABJ0Ad5mH3gAAAEgSURBVEhL7ZXbCYUwDIbPTg7gAA7gAr67gAP47rvvDuAADuAAruASOfzSHqSkSeoFPOAHAfHSfk3S+qEH8Ep4/kNiWRZ3JTMMwxZHUCXatnVXMl3XUZZl1DSNu2NHlaiqyrRCL4HAN+u6uic6qgRWhoExicReAlEUBc3z7J7KqBLIwn7gvu/ZPgklEHme0ziO7o04qgTSisHCCcqy3PoFUtM0/TLGhZZFVQJwq0wNSMb6xCSBj1EKbvCUQPa4UpokAJqMK0tqcKUxSwCsAqvhBrdE7AxJkvBgx+As4CaKhXTWHJLwoFewM5DimBRKiHckTkns4XZQrBFDbpOo69p8dN8iwe0AiUslUP8jv/PLJNB81h9WyGUSZ3glPA+QIPoCV/B/ozWT9KMAAAAASUVORK5CYII="
                                content-width="scale-to-fit" height="7pt" scaling="uniform" />
                            <xsl:choose>
                                <xsl:when test="contains($five, '||')">
                                    <xsl:value-of select="substring-before($five, '||')" />
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="$five"></xsl:value-of>
                                </xsl:otherwise>
                            </xsl:choose>
                        </fo:block>
                    </xsl:if>
                </xsl:if>
            </xsl:if>
        </xsl:if>
    </xsl:template>


    <!-- CHANGES -->
    <xsl:template match="signatureListChanges">
        <xsl:variable name="content">
            <xsl:value-of select="." />
        </xsl:variable>
        <xsl:if test="$content != ''">
            <fo:table border="solid black" text-align="center" keep-together.within-page="always" keep-with-next="always" page-break-after="avoid">
                <fo:table-column column-number="1" column-width="750pt" />
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell border="hidden" border-bottom="solid">
                            <fo:block padding="5pt">
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row border="solid black">
                        <fo:table-cell text-align="left" font-weight="bold" background-color="#E0E0E0">
                            <fo:block padding="5pt" margin-left="1pt" margin-right="1pt">
                                <xsl:value-of select="text:translate('tSignature')" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
            <xsl:call-template name="tableChange"></xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template match="schemeInformationChanges">
        <xsl:variable name="content">
            <xsl:value-of select="." />
        </xsl:variable>
        <xsl:if test="$content != ''">
            <fo:table border="solid black" text-align="center" keep-together.within-page="always" keep-with-next="always" page-break-after="avoid">
                <fo:table-column column-number="1" column-width="750pt" />
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell border="hidden" border-bottom="solid">
                            <fo:block padding="5pt">
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row border="solid black">
                        <fo:table-cell text-align="left" font-weight="bold" background-color="#E0E0E0">
                            <fo:block padding="5pt" margin-left="1pt" margin-right="1pt">
                                <xsl:value-of select="text:translate('schemeInformationMenu')" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
            <xsl:call-template name="tableChange"></xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template match="pointersToOtherTslChanges">
        <xsl:variable name="content">
            <xsl:value-of select="." />
        </xsl:variable>
        <xsl:if test="$content != ''">
            <fo:table border="solid black" text-align="center" keep-together.within-page="always" keep-with-next="always" page-break-after="avoid">
                <fo:table-column column-number="1" column-width="750pt" />
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell border="hidden" border-bottom="solid">
                            <fo:block padding="5pt">
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row border="solid black">
                        <fo:table-cell text-align="left" font-weight="bold" background-color="#E0E0E0">
                            <fo:block padding="5pt" margin-left="1pt" margin-right="1pt">
                                <xsl:value-of select="text:translate('ptotslMenu')" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
            <xsl:call-template name="tableChange"></xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template match="serviceProviderChanges">
        <xsl:variable name="content">
            <xsl:value-of select="." />
        </xsl:variable>
        <xsl:if test="$content != ''">
            <fo:table border="solid black" text-align="center" keep-together.within-page="always" keep-with-next="always" page-break-after="avoid">
                <fo:table-column column-number="1" column-width="750pt" />
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell border="hidden" border-bottom="solid">
                            <fo:block padding="5pt">
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row border="solid black">
                        <fo:table-cell text-align="left" font-weight="bold" background-color="#E0E0E0">
                            <fo:block padding="5pt" margin-left="1pt" margin-right="1pt">
                                <xsl:value-of select="text:translate('serviceProvider')" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
            <xsl:call-template name="tableChange"></xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template name="tableChange">
        <fo:table border="solid black" text-align="center">
            <fo:table-column column-number="1" column-width="250pt" />
            <fo:table-column column-number="2" column-width="250pt" />
            <fo:table-column column-number="3" column-width="250pt" />
            <fo:table-body>
                <fo:table-row border="solid black" font-weight="bold" background-color="#E0E0E0" page-break-after="avoid">
                    <fo:table-cell border="solid black" text-align="left" padding="2pt" margin-left="1pt">
                        <fo:block>Location</fo:block>
                    </fo:table-cell>
                    <!-- PROD -->
                    <xsl:if test="/*/@production='true'">
                        <fo:table-cell border="solid black" text-align="left" margin-left="1pt" padding="2pt">
                            <fo:block>
                                Previous Value [
                                <xsl:value-of select="/root/comparedTL/name" />
                                ]
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="solid black" text-align="left" padding="2pt" margin-left="1pt">
                            <fo:block>
                                Published Value [
                                <xsl:value-of select="/root/currentTL/name" />
                                ]
                            </fo:block>
                        </fo:table-cell>
                    </xsl:if>
                    <!-- DRAFT -->
                    <xsl:if test="/*/@production='false'">
                        <fo:table-cell border="solid black" text-align="left" margin-left="1pt" padding="2pt">
                            <fo:block>
                                Published Value [
                                <xsl:value-of select="/root/comparedTL/name" />
                                ]
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="solid black" text-align="left" padding="2pt" margin-left="1pt">
                            <fo:block>
                                Draft Value [
                                <xsl:value-of select="/root/currentTL/name" />
                                ]
                            </fo:block>
                        </fo:table-cell>
                    </xsl:if>
                </fo:table-row>
                <xsl:for-each select="change">
                    <fo:table-row font-size="7pt">
                        <fo:table-cell border="solid black" text-align="left" display-align="center">
                            <fo:block padding="5pt" margin-left="1pt" margin-right="1pt">
                                <xsl:call-template name="locationId">
                                    <xsl:with-param name="id" select="hrLocation" />
                                </xsl:call-template>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="solid black" text-align="left" display-align="center">
                            <fo:block padding="5pt" margin-left="1pt" margin-right="1pt">
                                <xsl:value-of select="publishedValue" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="solid black" text-align="left" display-align="center">
                            <fo:block padding="5pt" margin-left="1pt" margin-right="1pt">
                                <xsl:value-of select="currentValue" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </xsl:for-each>
            </fo:table-body>
        </fo:table>
    </xsl:template>

</xsl:stylesheet>	