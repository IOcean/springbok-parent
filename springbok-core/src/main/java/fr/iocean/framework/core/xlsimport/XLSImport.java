package fr.iocean.framework.core.xlsimport;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public abstract class XLSImport<R extends XLSReport> {

    private InputStream xlsInputStream;
    protected List<Short> columnIndexes;
    protected R report;
    private Iterator<Row> rowIterator;

    protected XLSImport(InputStream xlsInputStream, R report) {
        this.xlsInputStream = xlsInputStream;
        this.columnIndexes = new ArrayList<>();
        this.report = report;
    }

    /**
     * Load the file and process the import.
     */
    public void importData() {
        try {
            if (xlsInputStream != null) {
                Workbook workbook = WorkbookFactory.create(xlsInputStream);
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    processData(workbook, i);
                }
                postProcessData();
            }
        } catch (IllegalArgumentException | InvalidFormatException e) {
            log.error("The file format is not valid :" + e.getMessage());
            report.addGlobalError("xlsimport.error.invalidFileFormat");
        } catch (FileNotFoundException e) {
            log.error("The file was not found : " + e.getMessage());
            report.addGlobalError("xlsimport.error.internal");
        } catch (IOException e) {
            log.error("An error occured while opening the file : " + e.getMessage());
            report.addGlobalError("xlsimport.error.internal");
        }
        report.getErrors().forEach(error -> log.debug(error.toString()));
        report.getGlobalErrors().forEach(log::debug);
    }

    public R getReport() {
        return this.report;
    }

    /**
     * Get the first sheet of the XLS file, skips to the first line to parse, and processes each row.
     *
     * @param workbook   workbook to parse
     * @param sheetIndex sheet index to parse
     */
    private void processData(Workbook workbook, Integer sheetIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        if (!shouldProcessSheet(sheet, sheetIndex)) {
            return;
        }
        computeColumnIndexes(sheet);
        rowIterator = sheet.iterator();
        skipToLineStart(sheet);
        rowIterator.forEachRemaining(this::processRow);
    }

    private void skipToLineStart(Sheet sheet) {
        for (int i = 0; i < getStartingLine(sheet); i++) {
            rowIterator.next();
        }
    }

    /**
     * Create the list of indexes from the first column to the last one containing data.
     *
     * @param sheet the sheet to compute the column indexes on
     */
    private void computeColumnIndexes(Sheet sheet) {
        Row firstRow = sheet.getRow(0);
        short minColumnIndex = firstRow.getFirstCellNum();
        short maxColumnIndex = firstRow.getLastCellNum();

        for (short columnIndex = minColumnIndex; columnIndex < maxColumnIndex; columnIndex++) {
            columnIndexes.add(columnIndex);
        }
    }

    /**
     * Should we process the given XLS sheet.
     *
     * @param sheet the sheet info
     * @return true if the sheet should be processed.
     */
    protected abstract boolean shouldProcessSheet(Sheet sheet, Integer index);

    /**
     * Get starting line for current sheet.
     *
     * @param sheet the sheet
     * @return the starting line1
     */
    protected abstract Integer getStartingLine(Sheet sheet);

    /**
     * Business logic to implement for each row,
     * parsing, error checking should be implemented here.
     *
     * @param row a row in the XLS file representing one entry of the data to import
     */
    protected abstract void processRow(Row row);

    /**
     * Business logic to implement when every row have been parsed,
     * persisting should be implemented here.
     */
    protected abstract void postProcessData();
}
