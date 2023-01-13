package demo.api.core.excel;

import org.apache.commons.compress.archivers.zip.Zip64Mode;

import java.util.List;

public class MultiSheetExcelFile<T> extends ExcelFile<T>{
    private int currentRowIndex = ROW_START_INDEX;

    public MultiSheetExcelFile(Class<T> clazz, List<T> data, String sheetName) throws Exception{
        super(clazz, data, sheetName);
        workbook.setZip64Mode(Zip64Mode.Always);
    }

    @Override
    protected void renderExcel(List<T> data, String sheetName) {
        currentRowIndex = ROW_START_INDEX;

        sheet = workbook.createSheet(sheetName);

        renderHeader(sheet, currentRowIndex++, COLUMN_START_INDEX);

        if (data.isEmpty()) {
            return;
        }

        addRows(data);
    }

    @Override
    public void addRows(List<T> data) {
        for (Object d : data) {
            renderBody(d, currentRowIndex++, COLUMN_START_INDEX);
        }
    }

    public void addRenderExcelNewSheet(Class<T> clazz, List<T> data, String sheetName) {
        preparedResource(clazz, data, sheetName);
    }

}
