package fr.iocean.framework.core.xlsimport;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents an error on a line of a XLS file.
 */
@ToString(of = {"sheetName", "lineNumber", "message"})
@NoArgsConstructor
@Setter
public class XLSLineError {
    protected String sheetName;
    protected int lineNumber;
    protected String message;

    /**
     * @param sheetName  the sheet name of the error
     * @param lineNumber the line number of the error
     * @param message i18n message key (or value depending the need) of the message to display
     */
    public XLSLineError(String sheetName, int lineNumber, String message) {
        this.sheetName = sheetName;
        this.lineNumber = lineNumber;
        this.message = message;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getMessage() {
        return message;
    }

    public String getSheetName() {
        return sheetName;
    }
}
