package demo.api.server.controller;

import demo.api.core.excel.SingleSheetExcelFile;
import demo.api.core.web.resolver.RequestClient;
import demo.api.core.web.resolver.RequestData;
import demo.api.server.exception.CustomizedStatusException;
import demo.api.server.exception.ExceptionMessage;
import demo.api.server.jpa.entity.RequestClientEntity;
import demo.api.server.service.AccessLogService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/access")
@ApiOperation(value="접근 로그 관리 컨트롤러")
@RestController
public class AccessLogController {
    private final AccessLogService accessLogService;

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @ApiOperation(value = "전체로그조회")
    @GetMapping("/list")
    public void list(@RequestData RequestClient requestClient) {
        try {
            log.info("ver.MyBatis > " + accessLogService.findAllVerMyBatis().toString());
            log.info("ver.QueryDSL > " + accessLogService.findAllVerQueryDsl().toString());
        } catch (Exception e) {
            new CustomizedStatusException(ExceptionMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "ERROR")
    })
    @ApiOperation(value = "전체로그조회(엑셀)")
    @GetMapping("/list/excel")
    public void listExcel(@RequestData RequestClient requestClient) {
        try {
            SingleSheetExcelFile excelFile = new SingleSheetExcelFile<>(RequestClientEntity.class, accessLogService.findAllVerQueryDsl());
            String outPath = excelFile.write("access_log", "전체로그조회.xlsx");

        } catch (Exception e) {
            new CustomizedStatusException(ExceptionMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
