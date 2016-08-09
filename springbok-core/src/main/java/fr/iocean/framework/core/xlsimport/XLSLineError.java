package fr.iocean.framework.core.xlsimport;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents an error on a line of a XLS file.
 */
@ToString(of = {"sheetName", "lineNumber", "messageKey"})
@NoArgsConstructor
@Setter
public class XLSLineError {
    protected String sheetName;
    protected int lineNumber;
    protected String messageKey;

    /**
     * @param sheetName  the sheet name of the error
     * @param lineNumber the line number of the error
     * @param messageKey i18n message key of the message to display
     */
    public XLSLineError(String sheetName, int lineNumber, String messageKey) {
        this.sheetName = sheetName;
        this.lineNumber = lineNumber;
        this.messageKey = messageKey;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getSheetName() {
        return sheetName;
    }
}
