package fr.iocean.framework.core.xlsimport;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a basic report of an import based on a XLS file.
 */
@Getter
@Setter
public class XLSReport implements Serializable {

    protected String fileName;
    protected int numberOfCreatedEntries;
    protected int numberOfUpdatedEntries;

    protected List<XLSLineError> errors;
    protected List<String> globalErrors;

    public XLSReport() {
        this.numberOfCreatedEntries = 0;
        this.numberOfUpdatedEntries = 0;
        this.errors = Lists.newArrayList();
        this.globalErrors = Lists.newArrayList();
    }

    public XLSReport(String fileName) {
        this();
        this.fileName = fileName;
    }

    public boolean hasErrors() {
        return !CollectionUtils.isEmpty(errors) ||
                !CollectionUtils.isEmpty(globalErrors);
    }

    public void addError(int lineNumber, String message) {
        errors.add(new XLSLineError(null, lineNumber, message));
    }

    public void addError(String sheetName, int lineNumber, String message) {
        errors.add(new XLSLineError(sheetName, lineNumber, message));
    }

    public void incrementNumberOfCreatedEntries() {
        numberOfCreatedEntries++;
    }

    public void incrementNumberOfUpdatedEntries() {
        numberOfUpdatedEntries++;
    }

    public void addGlobalError(String message) {
        globalErrors.add(message);
    }

    public static XLSReport generalError(String error) {
        XLSReport xlsReport = new XLSReport();
        xlsReport.addGlobalError(error);
        return xlsReport;
    }

}