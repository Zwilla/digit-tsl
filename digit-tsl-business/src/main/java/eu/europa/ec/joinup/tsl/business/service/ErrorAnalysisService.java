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
package eu.europa.ec.joinup.tsl.business.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.xml.messaging.saaj.util.ByteOutputStream;

import eu.europa.ec.joinup.tsl.business.dto.TrustedListsReport;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.ErrorAnalysisDTO;
import eu.europa.ec.joinup.tsl.business.repository.CheckRepository;
import eu.europa.ec.joinup.tsl.business.repository.ResultRepository;
import eu.europa.ec.joinup.tsl.model.DBCheck;
import eu.europa.ec.joinup.tsl.model.DBCheckResult;

@Service
public class ErrorAnalysisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorAnalysisService.class);

    @Autowired
    private TLService tlService;

    @Autowired
    private CheckRepository checkRepository;

    @Autowired
    private ResultRepository resultRepository;

    public byte[] getErrorAnalysis() {
        ByteOutputStream out = new ByteOutputStream();

        List<TrustedListsReport> allProdTlReports = tlService.getAllProdTlReports();
        Iterable<DBCheck> checks = checkRepository.findAllByOrderByTarget();
        List<ErrorAnalysisDTO> countErrorDto = new ArrayList<ErrorAnalysisDTO>();
        for (DBCheck check : checks) {
            int totalCheck = 0;
            int tlImpacted = 0;
            String tlImpactedCc = "";
            ErrorAnalysisDTO errorDTO = new ErrorAnalysisDTO();
            errorDTO.setCheck(check);
            for (TrustedListsReport dbTl : allProdTlReports) {
                List<DBCheckResult> results = resultRepository.findByCheckAndTrustedListId(check, dbTl.getId());
                errorDTO.getResultMap().put(dbTl.getName() + "(Sn" + dbTl.getSequenceNumber() + ")", results.size());
                if (!CollectionUtils.isEmpty(results)) {
                    totalCheck = totalCheck + results.size();
                    tlImpacted = tlImpacted + 1;
                    tlImpactedCc = tlImpactedCc + ";" + dbTl.getTerritoryCode();
                }

            }

            errorDTO.setTotalCheck(totalCheck);
            errorDTO.setTlImpacted(tlImpacted);
            errorDTO.setTlImpactedCc(tlImpactedCc);
            countErrorDto.add(errorDTO);
        }
        try {
            generateExcelFile(countErrorDto, out);
        } catch (IOException e) {
            LOGGER.error("Error during excel error analysis generation", e);
            return null;
        }
        return out.getBytes();
    }

    private ByteOutputStream generateExcelFile(List<ErrorAnalysisDTO> countErrorDto, ByteOutputStream out) throws IOException {
        @SuppressWarnings("resource")
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Check Analysis");
        HSSFRow row;

        // Style
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        font.setColor(HSSFColor.WHITE.index);

        CellStyle checkHeadStyle = workbook.createCellStyle();
        checkHeadStyle.setBorderTop(BorderStyle.THIN);
        checkHeadStyle.setBorderBottom(BorderStyle.THIN);
        checkHeadStyle.setBorderRight(BorderStyle.THIN);
        checkHeadStyle.setBorderLeft(BorderStyle.THIN);
        checkHeadStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        checkHeadStyle.setFillPattern((FillPatternType.SOLID_FOREGROUND));
        checkHeadStyle.setFont(font);
        checkHeadStyle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle tlHeadStyle = workbook.createCellStyle();
        tlHeadStyle.setBorderTop(BorderStyle.THIN);
        tlHeadStyle.setBorderBottom(BorderStyle.THIN);
        tlHeadStyle.setBorderRight(BorderStyle.THIN);
        tlHeadStyle.setBorderLeft(BorderStyle.THIN);
        tlHeadStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        tlHeadStyle.setFillPattern((FillPatternType.SOLID_FOREGROUND));
        tlHeadStyle.setFont(font);
        tlHeadStyle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle wrapText = workbook.createCellStyle();
        wrapText.setWrapText(true);
        wrapText.setBorderTop(BorderStyle.THIN);
        wrapText.setBorderBottom(BorderStyle.THIN);
        wrapText.setBorderRight(BorderStyle.THIN);
        wrapText.setBorderLeft(BorderStyle.THIN);

        CellStyle simpleBorder = workbook.createCellStyle();
        simpleBorder.setBorderTop(BorderStyle.THIN);
        simpleBorder.setBorderBottom(BorderStyle.THIN);
        simpleBorder.setBorderRight(BorderStyle.THIN);
        simpleBorder.setBorderLeft(BorderStyle.THIN);

        // Row 1
        row = sheet.createRow(0);
        // Check
        Cell c1 = row.createCell(0);
        c1.setCellValue("Check");
        c1.setCellStyle(checkHeadStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
        // TrustedLists
        Cell c2 = row.createCell(7);
        c2.setCellValue("TrustedLists");
        c2.setCellStyle(tlHeadStyle);
        int nbCellMerged = countErrorDto.get(0).getResultMap().size() > 0 ? countErrorDto.get(0).getResultMap().size() - 1 : 1;
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 7, (7 + nbCellMerged)));

        // Row 2
        row = sheet.createRow(1);
        Map<String, Integer> checkHeadTitle = new LinkedHashMap<String, Integer>();
        checkHeadTitle.put("Name", 15000);
        checkHeadTitle.put("Impact", 4800);
        checkHeadTitle.put("Target", 9000);
        checkHeadTitle.put("Status", 3000);
        checkHeadTitle.put("Description", 15000);
        checkHeadTitle.put("Total", 2000);
        checkHeadTitle.put("TL Impacted", 3000);
        int nbCellHead = 0;

        // Check Head
        for (Map.Entry<String, Integer> head : checkHeadTitle.entrySet()) {
            Cell cell = row.createCell(nbCellHead);
            cell.setCellValue(head.getKey());
            sheet.setColumnWidth(nbCellHead, head.getValue());
            cell.setCellStyle(simpleBorder);
            nbCellHead = nbCellHead + 1;
        }
        // TL List
        for (Entry<String, Integer> entry : countErrorDto.get(0).getResultMap().entrySet()) {
            Cell errCell = row.createCell(nbCellHead);
            errCell.setCellValue(entry.getKey());
            errCell.setCellStyle(simpleBorder);
            sheet.setColumnWidth(nbCellHead, 4000);
            nbCellHead = nbCellHead + 1;
        }

        int nbRow = 2;
        for (ErrorAnalysisDTO error : countErrorDto) {
            row = sheet.createRow(nbRow);
            // Name
            Cell cName = row.createCell(0);
            cName.setCellValue(error.getCheck().getName().toString());
            cName.setCellStyle(simpleBorder);
            // Impact
            Cell cImpact = row.createCell(1);
            cImpact.setCellValue(error.getCheck().getImpact().toString());
            cImpact.setCellStyle(simpleBorder);

            Cell cTarget = row.createCell(2);
            cTarget.setCellValue(error.getCheck().getTarget().toString());
            cTarget.setCellStyle(simpleBorder);

            Cell cStatus = row.createCell(3);
            cStatus.setCellValue(error.getCheck().getPriority().toString());
            cStatus.setCellStyle(simpleBorder);

            Cell cDescription = row.createCell(4);
            cDescription.setCellValue(error.getCheck().getDescription());
            cDescription.setCellStyle(wrapText);

            Cell cTotal = row.createCell(5);
            cTotal.setCellValue(error.getTotalCheck());
            cTotal.setCellStyle(simpleBorder);

            Cell cTlImpacted = row.createCell(6);
            if (error.getTlImpacted() < 4) {
                cTlImpacted.setCellValue(error.getTlImpactedCc());
            } else {
                cTlImpacted.setCellValue(error.getTlImpacted());
            }
            cTlImpacted.setCellStyle(simpleBorder);

            int nbCell = 7;
            for (Entry<String, Integer> entry : error.getResultMap().entrySet()) {
                Cell errCell = row.createCell(nbCell);
                errCell.setCellValue(entry.getValue());
                errCell.setCellStyle(simpleBorder);
                nbCell = nbCell + 1;
            }
            nbRow = nbRow + 1;
        }

        workbook.write(out);
        return out;
    }

}
