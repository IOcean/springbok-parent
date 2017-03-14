package fr.iocean.framework.core.xlsimport;

import com.google.common.base.Strings;
import fr.iocean.framework.core.i18n.MessageService;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class XLSImportUtils {

    /**
     * @param row          the row containing the cell
     * @param columnNumber the column (0 based)
     */
    public static String parseCell(Row row, int columnNumber) {
        Cell cell = getOriginCell(row.getCell(columnNumber));
        if (cell == null) {
            return null;
        }
        cell.setCellType(Cell.CELL_TYPE_STRING);
        String stringCellValue = cell.getStringCellValue();

        return Strings.emptyToNull(stringCellValue);
    }

    public static Optional<Float> parseFloatAndReport(String value, int lineNumber, String errorMessage, XLSReport report) {
        if (value == null)
            return Optional.empty();
        
        try {
            return Optional.of(Float.parseFloat(value));
        } catch (Exception e) {
            report.addError(lineNumber, errorMessage);
            return Optional.empty();
        }
    }
    
    public static Optional<Integer> parseFloatAndReportAndRoundInteger(String value, int lineNumber, String errorMessage, XLSReport report) {
        if (value == null)
            return Optional.empty();
        
        try {
            return Optional.of(Math.round(Float.parseFloat(value)));
        } catch (Exception e) {
            report.addError(lineNumber, errorMessage);
            return Optional.empty();
        }
    }

    public static Optional<Integer> parseIntegerAndReport(String value, int lineNumber, String errorMessage, XLSReport report) {
        if (value == null)
            return Optional.empty();
        
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (Exception e) {
            report.addError(lineNumber, errorMessage);
            return Optional.empty();
        }
    }
    
    public static Optional<Date> parseDateAndReport(String value, String datePatern, int lineNumber, String errorMessage, XLSReport report) {
        if (value == null)
            return Optional.empty();
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(datePatern);
            return Optional.of(sdf.parse(value));
        } catch (Exception e) {
            report.addError(lineNumber, errorMessage);
            return Optional.empty();
        }
    }

    /**
     * Add an error into the report if the given value is null or empty
     *
     * @param value
     * @param lineNumber
     * @param errorMessage
     * @return the given value
     */
    public static String requireStringAndReport(String value, int lineNumber, String errorMessage, XLSReport report) {
        if (Strings.isNullOrEmpty(value)) {
            report.addError(lineNumber, errorMessage);
        }
        return value;
    }

    /**
     * Add an error to the report if the given email is invalid
     *
     * @param email
     * @param lineNumber
     * @param errorMessage
     * @return the given email
     */
    public static String validEmailAndReport(String email, int lineNumber, String errorMessage, XLSReport report) {
        if (email != null && !EmailValidator.getInstance().isValid(email)) {
            report.addError(lineNumber, errorMessage);
        }
        return email;
    }


    /**
     * Get original cell when a cell is in a merged range.
     *
     * @param cell the cell
     */
    public static Cell getOriginCell(Cell cell) {
        if (cell == null) {
            return null;
        }
        Sheet sheet = cell.getSheet();
        for (int index = 0; index < sheet.getNumMergedRegions(); index++) {
            CellRangeAddress cra = sheet.getMergedRegion(index);
            if (cra.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
                return sheet.getRow(cra.getFirstRow()).getCell(cra.getFirstColumn());
            }
        }
        return cell;
    }

    /**
     * Translate error messages (inside the xls report itself)
     *
     * @param messageService
     * @param xlsReport
     */
    public static void translate(MessageService messageService, XLSReport xlsReport) {
        xlsReport.getErrors().forEach(xlsLineError -> {
            String translate = messageService.getMessage(xlsLineError.getMessage());
            xlsLineError.setMessage(translate);
        });

        List<String> globalErrors =
                xlsReport.getGlobalErrors().stream().map(messageService::getMessage).collect(Collectors.toList());
        xlsReport.setGlobalErrors(globalErrors);
    }
}
