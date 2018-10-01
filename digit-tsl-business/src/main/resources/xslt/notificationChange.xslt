<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:text="java://eu.europa.ec.joinup.tsl.business.xslt.NotificationReportTranslator">

    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes" />

    <xsl:template match="/root">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="body-even" page-width="21.0cm" page-height="29.7cm" margin-top="1.5cm" margin-left="1cm" margin-right="1cm"
                    margin-bottom="2cm">
                    <fo:region-body region-name="xsl-region-body" />
                    <fo:region-after region-name="xsl-region-after" />
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="body-even">
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block font-size="7pt" text-align="right" padding-bottom="5pt" padding-top="3pt">
                        <xsl:value-of select="@docID" />
                        | Page
                        <fo:page-number />
                        of
                        <fo:page-number-citation ref-id="lastPage" />
                    </fo:block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body" font-family="Times New Roman">
                    <xsl:call-template name="content" />
                    <xsl:apply-templates />
                    <fo:block id="lastPage"></fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

    <xsl:template name="content">
        <xsl:call-template name="intro" />
        <xsl:call-template name="table" />
        <fo:block padding-top="15pt" font-size="9pt">
            <xsl:value-of select="text:translatePointersIntro(@memberState)" />
        </fo:block>
    </xsl:template>

    <xsl:template name="intro">
        <fo:block text-align="center" font-size="9pt" color="#b3b3cc" padding-bottom="5pt">
            <xsl:value-of select="@docID" />
        </fo:block>

        <fo:table page-break-after="avoid">
            <fo:table-column column-number="1" />
            <fo:table-body>
                <fo:table-row border="solid black" background-color="#bfbfbf">
                    <fo:table-cell>
                        <fo:block padding="5pt" text-align="center" font-size="11pt" font-weight="bold">
                            <fo:inline font-weight="bold">
                                <xsl:value-of select="text:translate('pdf.notification.title')" />
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
        <fo:block font-size="9pt" padding="0pt" padding-top="5pt">
            <fo:table border-collapse="separate" border-separation="5pt" border="solid black">
                <fo:table-column column-number="1" column-width="180pt" />
                <fo:table-column column-number="2" />
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block font-weight="bold">
                                <xsl:value-of select="text:translate('pdf.notification.territory')" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                            <fo:block>
                                <xsl:value-of select="@memberState" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block font-weight="bold">
                                <xsl:value-of select="text:translate('tSchemeOperatorName')" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding-left="0pt">
                            <fo:block>
                                <fo:table text-align="left">
                                    <fo:table-column column-number="1" column-width="20pt"></fo:table-column>
                                    <fo:table-column column-number="2"></fo:table-column>
                                    <fo:table-body>
                                        <xsl:for-each select="schemeOperatorNames/map/entry">
                                            <fo:table-row>
                                                <fo:table-cell>
                                                    <fo:block>
                                                        <xsl:value-of select="string[1].text()" />
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell>
                                                    <fo:block>
                                                        <xsl:value-of select="string[2].text()" />
                                                    </fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                        </xsl:for-each>
                                    </fo:table-body>
                                </fo:table>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <!-- Contact -->
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block font-weight="bold">
                                <xsl:value-of select="text:translate('pdf.notification.contact')" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell padding-left="0pt">
                            <fo:block>
                                <fo:table text-align="left">
                                    <fo:table-column column-number="1" column-width="80pt"></fo:table-column>
                                    <fo:table-column column-number="2"></fo:table-column>
                                    <fo:table-body>
                                        <fo:table-row>
                                            <fo:table-cell>
                                                <fo:block>
                                                    <xsl:value-of select="text:translate('pdf.notification.contact.name')" />
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell>
                                                <fo:block>
                                                    <xsl:value-of select="@contactName" />
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                        <fo:table-row>
                                            <fo:table-cell>
                                                <fo:block>
                                                    <xsl:value-of select="text:translate('pdf.notification.contact.address')" />
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell>
                                                <fo:block>
                                                    <xsl:value-of select="@contactAdress" />
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                        <fo:table-row>
                                            <fo:table-cell>
                                                <fo:block>
                                                    <xsl:value-of select="text:translate('pdf.notification.contact.phone')" />
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell>
                                                <fo:block>
                                                    <xsl:value-of select="@contactPhone" />
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                        <fo:table-row>
                                            <fo:table-cell>
                                                <fo:block>
                                                    <xsl:value-of select="text:translate('pdf.notification.contact.mail')" />
                                                </fo:block>
                                            </fo:table-cell>
                                            <xsl:if test="count(contactMail/string) > '0'">
                                                <fo:table-cell>
                                                    <xsl:for-each select="contactMail/string">
                                                        <fo:block>
                                                            <xsl:value-of select="." />
                                                        </fo:block>
                                                    </xsl:for-each>
                                                </fo:table-cell>
                                            </xsl:if>
                                        </fo:table-row>
                                    </fo:table-body>
                                </fo:table>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <!-- Location -->
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block font-weight="bold">
                                <xsl:value-of select="text:translate('pdf.notification.location.xml')" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                            <fo:block>
                                <xsl:value-of select="@mpPointer" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block font-weight="bold">
                                <xsl:value-of select="text:translate('pdf.notification.location.pdf')" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                            <fo:block>
                                <xsl:value-of select="@hrPointer" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>

                </fo:table-body>
            </fo:table>
            <!-- Users -->
            <fo:block padding-top="10pt">
                <fo:table border-collapse="separate" border-separation="5pt" border="solid black">
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block font-weight="bold">
                                    <xsl:value-of select="text:translate('tAuthorizedUsers')" />
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding-left="0pt">
                                <xsl:if test="users/user">
                                    <xsl:for-each select="users/user">
                                        <fo:block margin-bottom="4pt">
                                            <xsl:value-of select="./ecasId" />
                                        </fo:block>
                                    </xsl:for-each>
                                </xsl:if>
                                <xsl:if test="not(users/user)">
                                    <fo:block></fo:block>
                                </xsl:if>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>
            <!-- Signature -->
            <xsl:if test="signatureInformation">
                <fo:block padding-top="10pt">
                    <fo:table border-collapse="separate" border-separation="5pt" border="solid black">
                        <fo:table-header border="solid black">
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block font-weight="bold">
                                        <xsl:value-of select="text:translate('tSignature')" />
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-header>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:table border-collapse="separate" border-separation="5pt">
                                        <fo:table-column column-number="1" column-width="180pt" />
                                        <fo:table-column column-number="2" />
                                        <fo:table-body>
                                            <!-- Status (Indication) -->
                                            <fo:table-row>
                                                <fo:table-cell>
                                                    <fo:block>
                                                        <xsl:value-of select="text:translate('signature.indication')" />
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell>
                                                    <fo:block>
                                                        <xsl:value-of select="signatureInformation/indication" />
                                                    </fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                            <!-- Signed By -->
                                            <xsl:if test="signatureInformation/signedBy!=''">
                                                <fo:table-row>
                                                    <fo:table-cell>
                                                        <fo:block>
                                                            <xsl:value-of select="text:translate('signature.signBy')" />
                                                        </fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell>
                                                        <fo:block>
                                                            <xsl:value-of select="signatureInformation/signedBy" />
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </xsl:if>
                                            <!-- Signing Date -->
                                            <xsl:if test="signatureInformation/signingDate">
                                                <fo:table-row>
                                                    <fo:table-cell>
                                                        <fo:block>
                                                            <xsl:value-of select="text:translate('signature.signingDate')" />
                                                        </fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell>
                                                        <fo:block>
                                                            <xsl:value-of select="text:toDateFormat(signatureInformation/signingDate)" />
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </xsl:if>
                                        </fo:table-body>
                                    </fo:table>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                </fo:block>
            </xsl:if>
            <!-- Signing Certificate -->
            <fo:block padding-top="10pt">
                <xsl:if test="certificate != ''">
                    <fo:table border-collapse="separate" border-separation="5pt" border="solid black">
                        <fo:table-header border="solid black">
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block font-weight="bold">
                                        <xsl:value-of select="text:translate('pdf.notification.certificate')" />
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-header>
                        <fo:table-body>
                            <xsl:for-each select="certificate">
                                <fo:table-row>
                                    <fo:table-cell>
                                        <fo:block>
                                            <xsl:value-of select="." />
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </xsl:for-each>
                        </fo:table-body>
                    </fo:table>
                </xsl:if>
            </fo:block>
            <fo:block padding-top="10pt">
                <fo:table border-collapse="separate" border-separation="5pt" border="solid black">
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block font-weight="bold">
                                    <xsl:value-of select="text:translate('pdf.notification.date.submission')" />
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <xsl:value-of select="@dateOfSubmission" />
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block font-weight="bold">
                                    <xsl:value-of select="text:translate('pdf.notification.date.application')" />
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block>
                                    <xsl:value-of select="@dateOfEffect" />
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>
        </fo:block>
    </xsl:template>

    <xsl:template name="table">
        <fo:table page-break-after="avoid" margin-top="15pt">
            <fo:table-column column-number="1" column-width="150pt" />
            <fo:table-column column-number="2" />
            <fo:table-body>
                <fo:table-row border="solid black" background-color="#bfbfbf">
                    <fo:table-cell number-columns-spanned="2">
                        <fo:block padding="5pt" margin-left="3pt">
                            <fo:inline font-weight="bold">
                                <xsl:value-of select="text:translate('pdf.changes')" />
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>

        <fo:block font-size="9pt" padding-top="15pt">
            <fo:block>
                <xsl:value-of select="text:translateTableIntro(@memberState)" />
            </fo:block>
            <fo:block padding-top="10pt">
                <fo:table border="solid black" text-align="left">
                    <fo:table-column column-number="1" />
                    <fo:table-column column-number="2" />
                    <fo:table-header>
                        <fo:table-row font-weight="bold">
                            <fo:table-cell border="solid black" background-color="#E0E0E0">
                                <fo:block padding="5pt" margin-left="1pt">
                                    <xsl:value-of select="text:translate('pdf.notification.table.change')" />
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell border="solid black" background-color="#E0E0E0">
                                <fo:block padding="5pt" margin-left="1pt">
                                    <xsl:value-of select="text:translateTableSecondColumn(@memberState)" />
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-header>
                    <fo:table-body font-size="7">
                        <xsl:choose>
                            <xsl:when test="changeList != ''">
                                <xsl:for-each select="changeList/changes">
                                    <fo:table-row>
                                        <fo:table-cell border="solid black">
                                            <fo:block padding="5pt" margin-left="1pt">
                                                <xsl:for-each select="change/string">
                                                    <xsl:variable name="text" select="."></xsl:variable>
                                                    <xsl:choose>
                                                        <xsl:when test="starts-with( $text , 'Removed')">
                                                            <fo:block text-decoration="underline">
                                                                <xsl:value-of select="$text" />
                                                            </fo:block>
                                                        </xsl:when>
                                                        <xsl:when test="starts-with( $text , 'Added')">
                                                            <fo:block text-decoration="underline" margin-top="1pt">
                                                                <xsl:value-of select="$text" />
                                                            </fo:block>
                                                        </xsl:when>
                                                        <xsl:otherwise>
                                                            <fo:block>
                                                                <xsl:value-of select="$text" />
                                                            </fo:block>
                                                        </xsl:otherwise>
                                                    </xsl:choose>
                                                </xsl:for-each>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border="solid black">
                                            <fo:block padding="5pt" margin-left="1pt">
                                                <xsl:for-each select="measure/string">
                                                    <fo:block>
                                                        <xsl:value-of select="." />
                                                    </fo:block>
                                                </xsl:for-each>
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </xsl:for-each>
                            </xsl:when>
                            <xsl:otherwise>
                                <fo:table-row>
                                    <fo:table-cell border="solid black">
                                        <fo:block padding="5pt" margin-left="1pt">No change
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid black">
                                        <fo:block padding="5pt" margin-left="1pt">No change
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </xsl:otherwise>
                        </xsl:choose>
                    </fo:table-body>
                </fo:table>
            </fo:block>
            <fo:block padding-top="10pt">
                <xsl:value-of select="text:translate('pdf.notification.table.outro')" />
            </fo:block>
        </fo:block>
    </xsl:template>

    <xsl:template name="checkInfo" match="pointersToOtherTslChecks">
        <xsl:variable name="content">
            <xsl:value-of select="." />
        </xsl:variable>
        <xsl:call-template name="tablecheck"></xsl:call-template>
    </xsl:template>

    <xsl:template name="tablecheck">
        <fo:block page-break-before="always"></fo:block>
        <fo:table page-break-after="avoid" margin-top="15pt">
            <fo:table-column column-number="1" column-width="150pt" />
            <fo:table-column column-number="2" />
            <fo:table-body>
                <fo:table-row border="solid black" background-color="#bfbfbf">
                    <fo:table-cell number-columns-spanned="2">
                        <fo:block padding="5pt" margin-left="3pt">
                            <fo:inline font-weight="bold">
                                <xsl:value-of select="text:translate('pdf.checks')" />
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>

        <!-- Checks Resume -->
        <fo:table page-break-after="avoid" margin-top="10pt">
            <fo:table-column column-number="1" column-width="150pt" />
            <fo:table-column column-number="2" />
            <fo:table-body>
                <fo:table-row border="solid black" background-color="#E0E0E0">
                    <fo:table-cell number-columns-spanned="2">
                        <fo:block padding="5pt" margin-left="3pt">
                            <fo:inline font-weight="bold">
                                <xsl:value-of select="text:translate('pdf.summary')" />
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <xsl:if test="/root/@numberOfCheck =0">
                    <fo:table-row>
                        <fo:table-cell border="solid black" number-columns-spanned="2" margin-left="3pt" padding="2pt">
                            <fo:block font-size="7pt">
                                <xsl:value-of select="text:translate('tNotification.checkSuccess')"></xsl:value-of>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </xsl:if>
                <xsl:if test="/root/@numberOfCheck > 0">
                    <!-- Checks -->
                    <fo:table-row font-size="7pt">
                        <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                            <fo:block>
                                <xsl:value-of select="text:translate('pdf.errors')" />
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="solid black" margin-left="1pt" padding="2pt">
                            <fo:block>
                                <xsl:value-of select="/root/@errorChecks" />
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
                                <xsl:value-of select="/root/@warningChecks" />
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
                                <xsl:value-of select="/root/@infoChecks" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </xsl:if>
            </fo:table-body>
        </fo:table>

        <xsl:if test="grouped-checks != ''">
            <fo:table border="solid black" text-align="center" margin-top="5pt">
                <fo:table-column column-number="1" column-width="280pt" />
                <fo:table-column column-number="2" column-width="50pt" />
                <fo:table-column column-number="3" />
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
                                    <xsl:call-template name="locationId" />
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
                            <fo:table-row font-size="7pt">
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
        </xsl:if>
    </xsl:template>

    <xsl:template name="locationId">
        <xsl:variable name="first" select="substring-after(tlLocationId, '||')" />
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

</xsl:stylesheet>	