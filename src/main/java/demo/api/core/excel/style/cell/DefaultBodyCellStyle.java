package demo.api.core.excel.style.cell;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

public final class DefaultBodyCellStyle extends ExcelCellStyle {
    public DefaultBodyCellStyle(CellStyle cellStyle) {
        super(cellStyle);
    }

    @Override
    public XSSFCellStyle getStyle() {
        applyCellBorderStyle(BorderStyle.THIN);

        return cellStyle;
    }

}
