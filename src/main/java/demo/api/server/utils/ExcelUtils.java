package demo.api.server.utils;

import javax.servlet.http.HttpServletResponse;

public class ExcelUtils {
    public static HttpServletResponse ofExcelResponse(HttpServletResponse httpServletResponse) throws Exception{

        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=unknown.xlsx");
        httpServletResponse.setHeader("Content-Transfer-Encoding", "binary");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setHeader("Expires", "0");

        httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return httpServletResponse;
    }
}
