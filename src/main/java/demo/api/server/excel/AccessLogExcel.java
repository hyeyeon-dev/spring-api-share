package demo.api.server.excel;

import demo.api.core.excel.annotation.ExcelColumn;
import demo.api.core.excel.annotation.NoDataFormatter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessLogExcel {
    @ExcelColumn(columnName = "순서")
    @NoDataFormatter
    private Long seq;

    @ExcelColumn(columnName = "사용자 IP")
    private String clientIp;

    @ExcelColumn(columnName = "세션 ID")
    private String sessionId;

    @ExcelColumn(columnName = "User-Agent")
    private String userAgent;

    @ExcelColumn(columnName = "Request-URL")
    private String requestUrl;

    @ExcelColumn(columnName = "디바이스 채널")
    private String deviceChannel;

    @ExcelColumn(columnName = "Referer")
    private String referer;
}
