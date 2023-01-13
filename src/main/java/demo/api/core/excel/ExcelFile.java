package demo.api.core.excel;

import demo.api.core.excel.annotation.ExcelColumn;
import demo.api.core.excel.annotation.NoDataFormatter;
import demo.api.core.excel.style.cell.DefaultBodyCellStyle;
import demo.api.core.excel.style.cell.DefaultHeaderCellStyle;
import demo.api.core.excel.style.font.DefaultBodyFontStyle;
import demo.api.core.excel.style.font.DefaultHeaderFontStyle;
import demo.api.server.exception.CustomizedStatusException;
import demo.api.server.exception.ExceptionMessage;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static demo.api.server.utils.SuperClassReflectionUtils.*;

public abstract class ExcelFile<T> {
    protected static final SpreadsheetVersion EXCEL_VERSION = SpreadsheetVersion.EXCEL2007;
    protected static final int ROW_START_INDEX = 0;
    protected static final int COLUMN_START_INDEX = 0;

    public SXSSFWorkbook workbook;
    public Sheet sheet;

    protected Map<String, String> columnNameMap = new LinkedHashMap<>();
    protected List<String> fieldNames = new ArrayList<>();

    protected ExcelDataFormatter dataFormatter;
    protected ExcelDataRefiner dataRefiner;

    public ExcelFile(Class<T> clazz) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this(clazz, Collections.emptyList());
    }

    public ExcelFile(Class<T> clazz, List<T> data) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        this(clazz, data, null);
    }

    public ExcelFile(Class<T> clazz, List<T> data, String sheetName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        workbook = new SXSSFWorkbook();
        dataFormatter = new ExcelDataFormatter();
        dataRefiner = new ExcelDataRefiner();
        preparedResource(clazz, data, sheetName);
    }

    protected void preparedResource(Class<T> clazz, List<T> data, String sheetName) {
        validateData(data);
        preparedRenderResource(clazz);
        renderExcel(data, sheetName);
    }

    private void validateData(List<T> data) {
        int maxRows = EXCEL_VERSION.getMaxRows();
        if (data.size() > maxRows) {
            throw new IllegalArgumentException(String.format("조회 대상의 수가 엑셀 다운로드 가능한 최대 크기를 초과하였습니다. (최대 크기 : %s, 데이터 수 : %s)", maxRows, data.size()));
        }
    }

    protected abstract void renderExcel(List<T> data, String sheetName);

    protected void renderHeader(Sheet sheet, int rowIndex, int columnStartIndex) {
        Row row = sheet.createRow(rowIndex);
        int columnIndex = columnStartIndex;
        for (String fieldName : fieldNames) {
            Cell cell = row.createCell(columnIndex);

            cell.setCellValue(columnNameMap.get(fieldName));

            XSSFCellStyle headerCellStyle = new DefaultHeaderCellStyle(workbook.createCellStyle()).getStyle();
            headerCellStyle.setFont(new DefaultHeaderFontStyle(workbook.createFont()).getFont());
            cell.setCellStyle(headerCellStyle);

            columnIndex++;
        }
    }

    protected void renderBody(Object data, int rowIndex, int columnStartIndex) {
        DataFormat dataFormat = workbook.createDataFormat();
        Row row = sheet.createRow(rowIndex);
        int columnIndex = columnStartIndex;

        for (String fieldName : fieldNames) {
            Cell cell = row.createCell(columnIndex++);
            try {
                Field field = getField(data.getClass(), fieldName);
                Method method = getGetterMethod(data.getClass(), fieldName);
                method.setAccessible(true);
                Object cellValue = method.invoke(data);
                renderCellValue(cell, dataRefiner.getDataRefiner(cellValue, field.getType()));

                XSSFCellStyle bodyCellStyle = new DefaultBodyCellStyle(workbook.createCellStyle()).getStyle();
                bodyCellStyle.setFont(new DefaultBodyFontStyle(workbook.createFont()).getFont());
                if (!field.isAnnotationPresent(NoDataFormatter.class)){
                    bodyCellStyle.setDataFormat(dataFormatter.getDataFormat(dataFormat, field.getType()));
                }
                cell.setCellStyle(bodyCellStyle);

            } catch (Exception e) {
                throw new CustomizedStatusException(ExceptionMessage.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private void renderCellValue(Cell cell, Object cellValue) {
        if (cellValue instanceof Number) {
            Number numberValue = (Number) cellValue;
            cell.setCellValue(numberValue.doubleValue());
            return;
        }
        cell.setCellValue(cellValue == null ? "" : cellValue.toString());
    }

    private void preparedRenderResource(Class<T> clazz) {
        fieldNames.clear();
        columnNameMap.clear();

        for (Field field : getAllFields(clazz)) {
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                fieldNames.add(field.getName());
                columnNameMap.put(field.getName(), annotation.columnName());
            }
        }
    }

    public String write(String filePath, String fileName) throws IOException {
        filePath = "excel/" + filePath;

        if (workbook.getNumberOfSheets() == 1) {
            int columnSize = 0;
            if (!ObjectUtils.isEmpty(sheet.getRow(0))) {
                columnSize = sheet.getRow(0).getLastCellNum();
            }

//            sheet.setAutoFilter(new CellRangeAddress(0,sheet.getLastRowNum()+1,0,columnSize));

            ((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
            for (int index=0; index < columnSize; index++){
                sheet.autoSizeColumn(index);
                sheet.setColumnWidth(index, (sheet.getColumnWidth(index)+(short) 512));
            }
        }

        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        if ("/".equals(filePath.charAt(filePath.length()-1))) filePath = filePath.substring(0, filePath.length()-1);

        String outPath = filePath+"/"+fileName;

        FileOutputStream fileOutputStream = new FileOutputStream(outPath);
        workbook.write(fileOutputStream);

        fileOutputStream.flush();
        fileOutputStream.getFD().sync();
        fileOutputStream.close();

        workbook.close();
        workbook.dispose();

        return outPath;
    }

    public void write(OutputStream outputStream) throws IOException {
        if (workbook.getNumberOfSheets() == 1) {
            int columnSize = sheet.getRow(0).getLastCellNum();
            int rowSize = sheet.getLastRowNum()+1;

            sheet.setAutoFilter(new CellRangeAddress(0,rowSize,0,columnSize));

            ((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
            for (int index=0; index < columnSize; index++){
                sheet.autoSizeColumn(index);
                sheet.setColumnWidth(index, (sheet.getColumnWidth(index)+(short) 512));
            }
        }

        workbook.write(outputStream);
        workbook.close();
        workbook.dispose();
        outputStream.close();
    }

    public abstract void addRows(List<T> data);

}
