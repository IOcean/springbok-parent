package fr.iocean.framework.core.xlsimport;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a basic report of an import based on a XLS file.
 */
@Getter
public class XLSReport implements Serializable {

    protected String fileName;
    protected int numberOfCreatedEntries;
    protected int numberOfUpdatedEntries;

    protected List<XLSLineError> errors;
    protected List<String> globalErrors;

    public XLSReport(final String fileName) {
        this.fileName = fileName;
        this.numberOfCreatedEntries = 0;
        this.numberOfUpdatedEntries = 0;
        this.errors = Lists.newArrayList();
        this.globalErrors = Lists.newArrayList();
    }

    public boolean hasErrors() {
        return !CollectionUtils.isEmpty(errors) ||
                !CollectionUtils.isEmpty(globalErrors);
    }

    public void addError(int lineNumber, String messageKey) {
        errors.add(new XLSLineError(null, lineNumber, messageKey));
    }

    public void addError(String sheetName, int lineNumber, String messageKey) {
        errors.add(new XLSLineError(sheetName, lineNumber, messageKey));
    }

    public void incrementNumberOfCreatedEntries() {
        numberOfCreatedEntries++;
    }

    public void incrementNumberOfUpdatedEntries() {
        numberOfUpdatedEntries++;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void addGlobalError(String key) {
        globalErrors.add(key);
    }

}