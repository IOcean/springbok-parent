package fr.iocean.framework.core.xlsimport.model;

import fr.iocean.framework.core.xlsimport.XLSImport;
import fr.iocean.framework.core.xlsimport.XLSImportUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Getter
public class XLSImportSample extends XLSImport<XLSReportSample> {

    public static final int COLONNE_NUMERIC = 0;
    public static final int COLONNE_TEXT = COLONNE_NUMERIC + 1;
    public static final int COLONNE_EMPTY = COLONNE_TEXT + 1;

    private Map<Integer, NumericAndTextAndEmpty> map;

    private boolean postProcessDone = false;

    public XLSImportSample(InputStream xlsInputStream, String fileName) {
        super(xlsInputStream, new XLSReportSample(fileName));
        map = new HashMap<>();
    }

    @Override
    protected boolean shouldProcessSheet(Sheet sheet, Integer index) {
        return true;
    }

    @Override
    protected Integer getStartingLine(Sheet sheet) {
        return 1;
    }

    @Override
    protected void processRow(Row row) {
        int lineNumber = row.getRowNum() + 1;

        String numericAsString = XLSImportUtils.parseCell(row, COLONNE_NUMERIC);
        String textAsString = XLSImportUtils.parseCell(row, COLONNE_TEXT);
        String empty = XLSImportUtils.parseCell(row, COLONNE_EMPTY);

        Optional<Float> numeric = parseFloat(numericAsString, lineNumber, "xlsimport.error.invalidvalue");
        String text = requireString(textAsString, lineNumber, "xlsimport.error.invalidvalue");

        if (numeric.isPresent()) {
            NumericAndTextAndEmpty numericAndTextAndEmpty = new NumericAndTextAndEmpty(numeric.get(), text, empty);
            map.put(lineNumber, numericAndTextAndEmpty);
        }
    }

    @Override
    protected void postProcessData() {
        postProcessDone = true;
    }
}
