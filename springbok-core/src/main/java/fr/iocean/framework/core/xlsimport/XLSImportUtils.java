package fr.iocean.framework.core.xlsimport;

import com.google.common.base.Strings;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Optional;

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
}
