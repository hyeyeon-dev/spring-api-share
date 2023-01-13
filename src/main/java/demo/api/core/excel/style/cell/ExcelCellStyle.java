package demo.api.core.excel.style.cell;

import demo.api.core.excel.style.font.ExcelFontStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

public abstract class ExcelCellStyle {
    protected XSSFCellStyle cellStyle;
    public ExcelCellStyle(CellStyle cellStyle) {
        this.cellStyle = (XSSFCellStyle) cellStyle;
//        cellStyle.setWrapText(true);
    }

    protected abstract XSSFCellStyle getStyle();

    protected void applyCellBackgroundColor(byte[] rgb) {
        cellStyle.setFillForegroundColor(new XSSFColor(rgb, new DefaultIndexedColorMap()));
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    protected void applyCellBorderStyle(BorderStyle borderStyle) {
        applyCellBorderStyle(borderStyle, borderStyle, borderStyle, borderStyle);
    }

    protected void applyCellBorderStyle(BorderStyle top, BorderStyle left, BorderStyle right, BorderStyle bottom) {
        cellStyle.setBorderTop(top);
        cellStyle.setBorderLeft(left);
        cellStyle.setBorderRight(right);
        cellStyle.setBorderBottom(bottom);
    }

    protected void applyCellAlign(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
        cellStyle.setAlignment(horizontalAlignment);
        cellStyle.setVerticalAlignment(verticalAlignment);
    }

    protected void applyFont(ExcelFontStyle fontStyle) {
        cellStyle.setFont((Font) fontStyle);
    }
}
