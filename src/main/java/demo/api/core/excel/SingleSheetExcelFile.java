package demo.api.core.excel;

import java.util.List;

public class SingleSheetExcelFile<T> extends ExcelFile<T> {
    private int currentRowIndex = ROW_START_INDEX;

    public SingleSheetExcelFile(Class<T> clazz) throws Exception{
        super(clazz);
    }

    public SingleSheetExcelFile(Class<T> clazz, List<T> data) throws Exception{
        super(clazz, data);
    }

    @Override
    protected void renderExcel(List<T> data, String sheetName) {
        sheet = workbook.createSheet();

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
}
