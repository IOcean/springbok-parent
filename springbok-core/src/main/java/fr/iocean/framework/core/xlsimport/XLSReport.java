package fr.iocean.framework.core.xlsimport;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a basic report of an import based on a XLS file.
 */
@Getter
@Setter
public class XLSReport implements Serializable {

    protected String fileName;
    protected int numberOfCreatedEntries;
    protected int numberOfUpdatedEntries;
    protected int numberOfLines;

    protected List<XLSLineError> errors;
    protected List<String> globalErrors;

    public XLSReport() {
        this.numberOfCreatedEntries = 0;
        this.numberOfUpdatedEntries = 0;
        this.numberOfLines = 0;
        this.errors = Lists.newArrayList();
        this.globalErrors = Lists.newArrayList();
    }

    public XLSReport(String fileName) {
        this();
        this.fileName = fileName;
    }
    
    
    public void incrementNumberOfCreatedEntries() {
        numberOfCreatedEntries++;
    }

    public void incrementNumberOfUpdatedEntries() {
        numberOfUpdatedEntries++;
    }
    
    public void incrementNumberOfTotalLines() {
        numberOfLines++;
    }

    public int getNumberOfValidLines() {
        return numberOfLines - getNumberOfInvalidLines();
    }
    
    public int getNumberOfInvalidLines() {
        Set<Integer> dinstinctLines = new HashSet<>();
        
        for (XLSLineError error : errors) {
            dinstinctLines.add(error.getLineNumber());
        }
        
        return dinstinctLines.size();
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
    
    public void addGlobalError(String message) {
        globalErrors.add(message);
    }

    public static XLSReport generalError(String error) {
        XLSReport xlsReport = new XLSReport();
        xlsReport.addGlobalError(error);
        return xlsReport;
    }

}