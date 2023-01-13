package demo.api.core.httplogger;

import demo.api.server.mapper.primary.AccessLogMapper;
import demo.api.server.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpLogDataLoader {
    private final AccessLogMapper accessLogMapper;

//    @Scheduled(cron = "0 44 * * * *")
    @Scheduled(cron = "0 0 1 * * *")
    public void doLoad() {
        File file = new File(".");
        String targetDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String targetLogFileName = "access."+targetDate+".log";
        String targetPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-1)+"logs\\"+targetLogFileName;

        File targetLogFile = new File(targetPath);
        if (targetLogFile.exists()) {
            int maximumBulkSize = 10;
            log.info("HttpLogLoader|File Found > LogFileName : " + targetLogFileName);

            JSONParser parser = new JSONParser();
            List<HttpRequestLogVo> httpRequestLogVoList = new ArrayList<>();

            try(Stream<String> lines = Files.lines(Paths.get(targetPath), StandardCharsets.UTF_8)) {
                lines.forEach(line->{
                    try {
                        httpRequestLogVoList.add(new HttpRequestLogVo.RequestBuilder((JSONObject) parser.parse(line)).build());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (httpRequestLogVoList.size() >= maximumBulkSize) {
                        accessLogMapper.requestLogIns(httpRequestLogVoList);
                        httpRequestLogVoList.clear();
                    }
                });
                if (httpRequestLogVoList.size() > 0) {
                    accessLogMapper.requestLogIns(httpRequestLogVoList);
                    httpRequestLogVoList.clear();
                }
            } catch (Exception e) {
                LogUtils.logStackTrace(e);
            }
        } else {
            log.info("HttpLogLoader|File Not Found > LogFileName : " +targetLogFileName);
        }
    }
}