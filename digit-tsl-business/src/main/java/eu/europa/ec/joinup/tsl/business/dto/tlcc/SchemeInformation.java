/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 *  
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
 *  
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package eu.europa.ec.joinup.tsl.business.dto.tlcc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java pour anonymous complex type.
 * 
 * <p>
 * Le fragment de schema suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TSLVersionIdentifier" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TSLSequenceNumber" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TSLType" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}SchemeTerritory" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}SchemeOperatorName" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}SchemeOperatorAddress" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}SchemeName" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}SchemeInformationURI" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}StatusDeterminationApproach" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}SchemeTypeCommunityRules" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}PolicyOrLegalNotice" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}HistoricalInformationPeriod" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}PointersToOtherTSL" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}NextUpdate" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}DistributionPoints" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "tslVersionIdentifier", "tslSequenceNumber", "tslType", "schemeTerritory", "schemeOperatorName", "schemeOperatorAddress", "schemeName", "schemeInformationURI",
        "statusDeterminationApproach", "schemeTypeCommunityRules", "policyOrLegalNotice", "historicalInformationPeriod", "pointersToOtherTSL", "nextUpdate", "distributionPoints" })
@XmlRootElement(name = "SchemeInformation", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class SchemeInformation {

    @XmlElement(name = "TSLVersionIdentifier", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TSLVersionIdentifier tslVersionIdentifier;
    @XmlElement(name = "TSLSequenceNumber", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TSLSequenceNumber tslSequenceNumber;
    @XmlElement(name = "TSLType", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TSLType tslType;
    @XmlElement(name = "SchemeTerritory", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected SchemeTerritory schemeTerritory;
    @XmlElement(name = "SchemeOperatorName", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected SchemeOperatorName schemeOperatorName;
    @XmlElement(name = "SchemeOperatorAddress", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected SchemeOperatorAddress schemeOperatorAddress;
    @XmlElement(name = "SchemeName", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected SchemeName schemeName;
    @XmlElement(name = "SchemeInformationURI", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected SchemeInformationURI schemeInformationURI;
    @XmlElement(name = "StatusDeterminationApproach", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected StatusDeterminationApproach statusDeterminationApproach;
    @XmlElement(name = "SchemeTypeCommunityRules", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected SchemeTypeCommunityRules schemeTypeCommunityRules;
    @XmlElement(name = "PolicyOrLegalNotice", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected PolicyOrLegalNotice policyOrLegalNotice;
    @XmlElement(name = "HistoricalInformationPeriod", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected HistoricalInformationPeriod historicalInformationPeriod;
    @XmlElement(name = "PointersToOtherTSL", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected PointersToOtherTSL pointersToOtherTSL;
    @XmlElement(name = "NextUpdate", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected NextUpdate nextUpdate;
    @XmlElement(name = "DistributionPoints", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected DistributionPoints distributionPoints;

    /**
     * Obtient la valeur de la propriete tslVersionIdentifier.
     * 
     * @return possible object is {@link TSLVersionIdentifier }
     * 
     */
    public TSLVersionIdentifier getTSLVersionIdentifier() {
        return tslVersionIdentifier;
    }

    /**
     * Definit la valeur de la propriete tslVersionIdentifier.
     * 
     * @param value
     *            allowed object is {@link TSLVersionIdentifier }
     * 
     */
    public void setTSLVersionIdentifier(TSLVersionIdentifier value) {
        this.tslVersionIdentifier = value;
    }

    /**
     * Obtient la valeur de la propriete tslSequenceNumber.
     * 
     * @return possible object is {@link TSLSequenceNumber }
     * 
     */
    public TSLSequenceNumber getTSLSequenceNumber() {
        return tslSequenceNumber;
    }

    /**
     * Definit la valeur de la propriete tslSequenceNumber.
     * 
     * @param value
     *            allowed object is {@link TSLSequenceNumber }
     * 
     */
    public void setTSLSequenceNumber(TSLSequenceNumber value) {
        this.tslSequenceNumber = value;
    }

    /**
     * Obtient la valeur de la propriete tslType.
     * 
     * @return possible object is {@link TSLType }
     * 
     */
    public TSLType getTSLType() {
        return tslType;
    }

    /**
     * Definit la valeur de la propriete tslType.
     * 
     * @param value
     *            allowed object is {@link TSLType }
     * 
     */
    public void setTSLType(TSLType value) {
        this.tslType = value;
    }

    /**
     * Obtient la valeur de la propriete schemeTerritory.
     * 
     * @return possible object is {@link SchemeTerritory }
     * 
     */
    public SchemeTerritory getSchemeTerritory() {
        return schemeTerritory;
    }

    /**
     * Definit la valeur de la propriete schemeTerritory.
     * 
     * @param value
     *            allowed object is {@link SchemeTerritory }
     * 
     */
    public void setSchemeTerritory(SchemeTerritory value) {
        this.schemeTerritory = value;
    }

    /**
     * Obtient la valeur de la propriete schemeOperatorName.
     * 
     * @return possible object is {@link SchemeOperatorName }
     * 
     */
    public SchemeOperatorName getSchemeOperatorName() {
        return schemeOperatorName;
    }

    /**
     * Definit la valeur de la propriete schemeOperatorName.
     * 
     * @param value
     *            allowed object is {@link SchemeOperatorName }
     * 
     */
    public void setSchemeOperatorName(SchemeOperatorName value) {
        this.schemeOperatorName = value;
    }

    /**
     * Obtient la valeur de la propriete schemeOperatorAddress.
     * 
     * @return possible object is {@link SchemeOperatorAddress }
     * 
     */
    public SchemeOperatorAddress getSchemeOperatorAddress() {
        return schemeOperatorAddress;
    }

    /**
     * Definit la valeur de la propriete schemeOperatorAddress.
     * 
     * @param value
     *            allowed object is {@link SchemeOperatorAddress }
     * 
     */
    public void setSchemeOperatorAddress(SchemeOperatorAddress value) {
        this.schemeOperatorAddress = value;
    }

    /**
     * Obtient la valeur de la propriete schemeName.
     * 
     * @return possible object is {@link SchemeName }
     * 
     */
    public SchemeName getSchemeName() {
        return schemeName;
    }

    /**
     * Definit la valeur de la propriete schemeName.
     * 
     * @param value
     *            allowed object is {@link SchemeName }
     * 
     */
    public void setSchemeName(SchemeName value) {
        this.schemeName = value;
    }

    /**
     * Obtient la valeur de la propriete schemeInformationURI.
     * 
     * @return possible object is {@link SchemeInformationURI }
     * 
     */
    public SchemeInformationURI getSchemeInformationURI() {
        return schemeInformationURI;
    }

    /**
     * Definit la valeur de la propriete schemeInformationURI.
     * 
     * @param value
     *            allowed object is {@link SchemeInformationURI }
     * 
     */
    public void setSchemeInformationURI(SchemeInformationURI value) {
        this.schemeInformationURI = value;
    }

    /**
     * Obtient la valeur de la propriete statusDeterminationApproach.
     * 
     * @return possible object is {@link StatusDeterminationApproach }
     * 
     */
    public StatusDeterminationApproach getStatusDeterminationApproach() {
        return statusDeterminationApproach;
    }

    /**
     * Definit la valeur de la propriete statusDeterminationApproach.
     * 
     * @param value
     *            allowed object is {@link StatusDeterminationApproach }
     * 
     */
    public void setStatusDeterminationApproach(StatusDeterminationApproach value) {
        this.statusDeterminationApproach = value;
    }

    /**
     * Obtient la valeur de la propriete schemeTypeCommunityRules.
     * 
     * @return possible object is {@link SchemeTypeCommunityRules }
     * 
     */
    public SchemeTypeCommunityRules getSchemeTypeCommunityRules() {
        return schemeTypeCommunityRules;
    }

    /**
     * Definit la valeur de la propriete schemeTypeCommunityRules.
     * 
     * @param value
     *            allowed object is {@link SchemeTypeCommunityRules }
     * 
     */
    public void setSchemeTypeCommunityRules(SchemeTypeCommunityRules value) {
        this.schemeTypeCommunityRules = value;
    }

    /**
     * Obtient la valeur de la propriete policyOrLegalNotice.
     * 
     * @return possible object is {@link PolicyOrLegalNotice }
     * 
     */
    public PolicyOrLegalNotice getPolicyOrLegalNotice() {
        return policyOrLegalNotice;
    }

    /**
     * Definit la valeur de la propriete policyOrLegalNotice.
     * 
     * @param value
     *            allowed object is {@link PolicyOrLegalNotice }
     * 
     */
    public void setPolicyOrLegalNotice(PolicyOrLegalNotice value) {
        this.policyOrLegalNotice = value;
    }

    /**
     * Obtient la valeur de la propriete historicalInformationPeriod.
     * 
     * @return possible object is {@link HistoricalInformationPeriod }
     * 
     */
    public HistoricalInformationPeriod getHistoricalInformationPeriod() {
        return historicalInformationPeriod;
    }

    /**
     * Definit la valeur de la propriete historicalInformationPeriod.
     * 
     * @param value
     *            allowed object is {@link HistoricalInformationPeriod }
     * 
     */
    public void setHistoricalInformationPeriod(HistoricalInformationPeriod value) {
        this.historicalInformationPeriod = value;
    }

    /**
     * Obtient la valeur de la propriete pointersToOtherTSL.
     * 
     * @return possible object is {@link PointersToOtherTSL }
     * 
     */
    public PointersToOtherTSL getPointersToOtherTSL() {
        return pointersToOtherTSL;
    }

    /**
     * Definit la valeur de la propriete pointersToOtherTSL.
     * 
     * @param value
     *            allowed object is {@link PointersToOtherTSL }
     * 
     */
    public void setPointersToOtherTSL(PointersToOtherTSL value) {
        this.pointersToOtherTSL = value;
    }

    /**
     * Obtient la valeur de la propriete nextUpdate.
     * 
     * @return possible object is {@link NextUpdate }
     * 
     */
    public NextUpdate getNextUpdate() {
        return nextUpdate;
    }

    /**
     * Definit la valeur de la propriete nextUpdate.
     * 
     * @param value
     *            allowed object is {@link NextUpdate }
     * 
     */
    public void setNextUpdate(NextUpdate value) {
        this.nextUpdate = value;
    }

    /**
     * Obtient la valeur de la propriete distributionPoints.
     * 
     * @return possible object is {@link DistributionPoints }
     * 
     */
    public DistributionPoints getDistributionPoints() {
        return distributionPoints;
    }

    /**
     * Definit la valeur de la propriete distributionPoints.
     * 
     * @param value
     *            allowed object is {@link DistributionPoints }
     * 
     */
    public void setDistributionPoints(DistributionPoints value) {
        this.distributionPoints = value;
    }

}
