package demo.api.core.excel.style.cell;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

public class DefaultHeaderCellStyle extends ExcelCellStyle {
    public DefaultHeaderCellStyle(CellStyle cellStyle) {
        super(cellStyle);
    }

    @Override
    public XSSFCellStyle getStyle() {
        applyCellBackgroundColor(new byte[]{(byte)234,(byte)234,(byte)234});
        applyCellBorderStyle(BorderStyle.THIN);
        applyCellAlign(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

        return cellStyle;
    }
}
