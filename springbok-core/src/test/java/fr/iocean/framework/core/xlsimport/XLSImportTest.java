package fr.iocean.framework.core.xlsimport;

import fr.iocean.framework.core.xlsimport.model.NumericAndTextAndEmpty;
import fr.iocean.framework.core.xlsimport.model.XLSImportSample;
import fr.iocean.framework.core.xlsimport.model.XLSReportSample;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

public class XLSImportTest {

    @Test
    public void importXLSFile() throws Exception {
        String fileName = "import.xlsx";
        InputStream xlsInputStream = XLSImportIOUtils.toInputStream(getClass(), "/xlsimport", fileName);
        XLSImportSample xlsImportSample = new XLSImportSample(xlsInputStream, fileName);

        xlsImportSample.importData();

        Map<Integer, NumericAndTextAndEmpty> map = xlsImportSample.getMap();
        Assertions.assertThat(map).hasSize(2);

        NumericAndTextAndEmpty valuesLine2 = map.get(2);
        Assertions.assertThat(valuesLine2.getFloatValue()).isEqualTo(1.2f);
        Assertions.assertThat(valuesLine2.getTextValue()).isEqualTo("ok");
        Assertions.assertThat(valuesLine2.getNullValue()).isNull();

        NumericAndTextAndEmpty valuesLine5 = map.get(5);
        Assertions.assertThat(valuesLine5.getFloatValue()).isEqualTo(18f);
        Assertions.assertThat(valuesLine5.getTextValue()).isEqualTo("ok2");
        Assertions.assertThat(valuesLine5.getNullValue()).isNull();

        Assertions.assertThat(xlsImportSample.isPostProcessDone()).isTrue();
    }

    @Test
    public void importXLSFileWithErrors() throws Exception {
        String fileName = "importWithErrors.xlsx";
        InputStream xlsInputStream = XLSImportIOUtils.toInputStream(getClass(), "/xlsimport", fileName);
        XLSImportSample xlsImportSample = new XLSImportSample(xlsInputStream, fileName);

        xlsImportSample.importData();

        Map<Integer, NumericAndTextAndEmpty> map = xlsImportSample.getMap();
        Assertions.assertThat(map).hasSize(1);

        XLSReportSample report = xlsImportSample.getReport();
        Assertions.assertThat(report.hasErrors()).isTrue();
        Assertions.assertThat(report.getErrors()).hasSize(2);

        Iterator<XLSLineError> iterator = report.getErrors().iterator();
        XLSLineError xlsLineError = iterator.next();

        Assertions.assertThat(xlsLineError.getLineNumber()).isEqualTo(2);
        Assertions.assertThat(xlsLineError.getMessage()).isEqualTo("xlsimport.error.invalidvalue");

        xlsLineError = iterator.next();
        Assertions.assertThat(xlsLineError.getLineNumber()).isEqualTo(3);
        Assertions.assertThat(xlsLineError.getMessage()).isEqualTo("xlsimport.error.invalidvalue");

        Assertions.assertThat(xlsImportSample.isPostProcessDone()).isTrue();
    }
}
