package demo.api.core.excel.style.font;

import org.apache.poi.ss.usermodel.Font;

public abstract class ExcelFontStyle {
    protected Font fontStyle;
    public ExcelFontStyle(Font font) {
        this.fontStyle = font;
    }

    protected abstract Font getFont();
}
