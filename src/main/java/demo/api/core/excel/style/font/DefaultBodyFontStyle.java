package demo.api.core.excel.style.font;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;


public class DefaultBodyFontStyle extends ExcelFontStyle {
    public DefaultBodyFontStyle(Font font) {
        super(font);
    }

    @Override
    public Font getFont() {
        fontStyle.setFontName("ëëęł ë");
        fontStyle.setFontHeightInPoints((short) 10);
        fontStyle.setColor(IndexedColors.BLACK.getIndex());
        fontStyle.setBold(false);

        return fontStyle;
    }
}
