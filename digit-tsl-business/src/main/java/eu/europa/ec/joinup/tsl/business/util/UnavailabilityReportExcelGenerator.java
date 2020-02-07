package eu.europa.ec.joinup.tsl.business.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import eu.europa.ec.joinup.tsl.business.dto.availability.UnavailabilityReportEntry;

public class UnavailabilityReportExcelGenerator {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    public static void generateUnavailabilityReport(List<UnavailabilityReportEntry> unavailabilityStories, ByteArrayOutputStream unavailabilityReportOS) throws IOException {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Unavailability Report");
            HSSFRow row;

            createHeader(sheet);
            int indexLine = 1;
            for (UnavailabilityReportEntry tmpEntry : unavailabilityStories) {
                row = sheet.createRow(indexLine);
                createEntry(tmpEntry, row);
                indexLine = indexLine + 1;
            }

            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(unavailabilityReportOS);
            workbook.close();
        } catch (IOException e) {
            throw new IOException("UnavailabilityReportExcelGenerator - Error during report generation", e);
        }
    }

    /**
     * Trusted line unavailable/unsupported data
     *
     * @param tmpEntry
     * @param row
     */
    private static void createEntry(UnavailabilityReportEntry tmpEntry, HSSFRow row) {
        Cell c1 = row.createCell(0);
        c1.setCellValue(tmpEntry.getTrustedList().getTerritory().getCountryName());

        Cell c2 = row.createCell(1);
        long unavailableDuration = TimeUnit.MILLISECONDS.toMinutes(tmpEntry.getUnavailabilityStories().getAvailabilityPieChart().getUnavailableTiming());
        c2.setCellValue(unavailableDuration);

        Cell c3 = row.createCell(2);
        int nbUnavailable = tmpEntry.getUnavailabilityStories().getUnavailableEntryOccurence();
        c3.setCellValue(nbUnavailable);

        Cell c4 = row.createCell(3);
        long unsupportedDuration = TimeUnit.MILLISECONDS.toMinutes(tmpEntry.getUnavailabilityStories().getAvailabilityPieChart().getUnsupportedTiming());
        c4.setCellValue(unsupportedDuration);

        Cell c5 = row.createCell(4);
        int nbUnsupported = tmpEntry.getUnavailabilityStories().getUnsupportedEntryOccurence();
        c5.setCellValue(nbUnsupported);

        Cell c6 = row.createCell(5);
        long unavailabilitySum = unavailableDuration + unsupportedDuration;
        c6.setCellValue(unavailabilitySum);

        Cell c7 = row.createCell(6);
        c7.setCellValue(nbUnavailable + nbUnsupported);

        float availabilityPercentage = 100f - ((unavailabilitySum * 100f) / 1440f);
        Cell c8 = row.createCell(7);
        c8.setCellValue(availabilityPercentage);
    }

    /**
     * Header of data grid
     *
     * @param row
     */
    private static void createHeader(HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(0);
        Cell c1 = row.createCell(0);
        c1.setCellValue(bundle.getString("tbCountry"));

        Cell c2 = row.createCell(1);
        c2.setCellValue(bundle.getString("unavailability.duration"));

        Cell c3 = row.createCell(2);
        c3.setCellValue(bundle.getString("tOccurence"));

        Cell c4 = row.createCell(3);
        c4.setCellValue(bundle.getString("unsupported.duration"));

        Cell c5 = row.createCell(4);
        c5.setCellValue(bundle.getString("tOccurence"));

        Cell c6 = row.createCell(5);
        c6.setCellValue(bundle.getString("tTotalDuration"));

        Cell c7 = row.createCell(6);
        c7.setCellValue(bundle.getString("tTotalOccurence"));

        Cell c8 = row.createCell(7);
        c8.setCellValue(bundle.getString("tbAvailability") + "(%)");
    }

}
