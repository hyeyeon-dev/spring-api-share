//package demo.api.core.cache.schedule;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import demo.api.core.context.BeanInjector;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.ObjectUtils;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.env.Environment;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//@Slf4j
//@Component
//@RequiredArgsConstructor
////@EnableAsync
//public class CacheSyncScheduler {
//    private final CacheService cacheService;
//    private final CacheStampService cacheStampService;
//
//    @Value("${schedule.cache.enable}")
//    private boolean enableCacheSyncScheduler;
//
//    @Value("${schedule.cache.server}")
//    private String apiServerUrl;
//
//    @Value("${schedule.cache.stampEnable}")
//    private boolean isEnableCacheStampScheduler;
//
//    @Value("${schedule.cache.stampServer}")
//    private String apiStampServerUrl;
//
//    @Autowired
//    private Environment environment;
//
//    private static final String ROLE_ADMIN = "admin";
//    private static final String ROLE_WEBSITE = "website";
//
//    @Scheduled(fixedDelayString = "${schedule.cache.fixedDelayMs}", initialDelayString = "${schedule.cache.initialDelayMs}")
//    public void syncCacheStampTask() {
//        if (!isEnableCacheStampScheduler) {
//            return;
//        }
//
//        String serverRole = environment.getProperty("server.role");
//        serverRole = ObjectUtils.isEmpty(serverRole)? ROLE_WEBSITE:serverRole;
//        if (!serverRole.equals(ROLE_ADMIN) && !serverRole.equals(ROLE_WEBSITE)) {
//            log.error("If you are using cache, please enter the correct role(admin or website). Wrong Input => -Dserver.role : " + serverRole);
//            System.exit(1);
//        }
//
//        log.info("Start CacheSyncScheduler - syncCacheStampTask()");
//        log.info("serverRole  : " + serverRole);
//        if (!ROLE_ADMIN.equals(serverRole)) {
//            log.info("serverApi  : " + apiStampServerUrl);
//        }
//
//        try {
//            String jsonStr = "";
//            if (ROLE_ADMIN.equals(serverRole)) {
//                List<CacheStampEntity> maxStampAll = cacheStampService.getMaxStampAll();
//                jsonStr = new ObjectMapper().writeValueAsString(maxStampAll);
//
//            } else {
//                jsonStr = Jsoup.connect(apiStampServerUrl)
//                    .method(Connection.Method.GET)
//                    .header("Content-Type", "application/json;charset=UTF-8")
//                    .ignoreContentType(true)
//                    .execute().body();
//            }
//
//            JSONParser jsonParser = new JSONParser();
//            JSONArray jsonArray = (JSONArray) jsonParser.parse(jsonStr);
//
//            for (int i = 0; i < jsonArray.size(); i++) {
//                JSONObject jo = (JSONObject) jsonArray.get(i);
//                String cacheCode = (String) jo.get("cacheCode");
//                String maxStamp = (String) jo.get("stamp");
//
//                CacheStampEntity localCache = cacheStampService.getOne(cacheCode);
//
//                log.info("[" + cacheCode + "] server : " + maxStamp + " / local : " + localCache.getStamp());
//
//                if (!maxStamp.equals(localCache.getStamp())) {
//                    Class clazz = BaseService.CACHE_CODE.of(cacheCode).clazz();
//                    if (ObjectUtils.isNotEmpty(clazz)) {
//                        Object obj = BeanInjector.getBean(clazz);
//                        Method method = obj.getClass().getMethod("saveCacheStampData", String.class);
//                        method.invoke(obj, maxStamp);
//
//                        cacheStampService.saveCacheData();
//                        log.info("reload cache stamp!!!");
//                    }
//                }
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Scheduled(fixedDelayString = "${schedule.cache.fixedDelayMs}", initialDelayString = "${schedule.cache.initialDelayMs}")
//    public void syncCacheVersionTask() {
//        // ?????? ??????????????? off ??? ???????????? ???????????? ???????????????~ => ??????????
//        if (!enableCacheSyncScheduler) {
//            return;
//        }
//
//        String serverRole = environment.getProperty("server.role");
//        serverRole = ObjectUtils.isEmpty(serverRole)? ROLE_WEBSITE:serverRole;
//        if (!serverRole.equals(ROLE_ADMIN) && !serverRole.equals(ROLE_WEBSITE)) {
//            log.error("If you are using cache, please enter the correct role(admin or website). Wrong Input => -Dserver.role : " + serverRole);
//            System.exit(1);
//        }
//
//        log.info("Start CacheSyncScheduler - syncCacheVersionTask()");
//        log.info("serverRole  : " + serverRole);
//        if (!ROLE_ADMIN.equals(serverRole)) {
//            log.info("serverApi : " + apiServerUrl);
//        }
//
//        try {
//            String jsonStr = "";
//            if (ROLE_ADMIN.equals(serverRole)) {
//                List<CacheEntity> cacheAll = cacheService.findAllGroupByCacheCode();
//                jsonStr = new ObjectMapper().writeValueAsString(cacheAll);
//
//            } else {
//                jsonStr = Jsoup.connect(apiServerUrl)
//                    .method(Connection.Method.GET)
//                    .header("Content-Type", "application/json;charset=UTF-8")
//                    .ignoreContentType(true)
//                    .execute().body();
//            }
//
//            JSONParser jsonParser = new JSONParser();
//            JSONArray jsonArray = (JSONArray) jsonParser.parse(jsonStr);
//
//            for (int i = 0; i < jsonArray.size(); i++) {
//                JSONObject jo = (JSONObject) jsonArray.get(i);
//                String cacheCode = (String) jo.get("cacheCode");
//                Long longVersion = (Long) jo.get("version");
//                int serverVersion = longVersion.intValue();
//
//                CacheEntity localCache = cacheService.getOne(cacheCode);
//                int localVersion = localCache.getVersion();
//
//                log.info("[" + cacheCode + "] server : " + serverVersion + " / local : " + localVersion);
//
//                if (serverVersion != localVersion) {
//                    Class clazz = BaseService.CACHE_CODE.of(cacheCode).clazz();
//                    if (ObjectUtils.isNotEmpty(clazz)) {
//                        Object obj = BeanInjector.getBean(clazz);
//                        Method method = obj.getClass().getMethod("saveCacheData");
//                        method.invoke(obj);
//
//                        cacheService.saveCacheData();
//                        log.info("reload cache data!!!");
//                    }
//                }
//            }
//            //log.info("response : " + jsonArray.toJSONString());
//
//        } catch (InvocationTargetException e) {
//          e.getTargetException().printStackTrace();
//
//        } catch (Exception e) {
//            e.printStackTrace();
////            LogUtils.logStackTrace(e);
//        }
//
////        try {
////            String jsonStr = Jsoup.connect(apiServerUrl)
////                    .method(Connection.Method.GET)
////                    .header("Content-Type", "application/json;charset=UTF-8")
////                    .ignoreContentType(true)
////                    .execute().body();
////            JSONParser jsonParser = new JSONParser();
////            JSONArray jsonArray = (JSONArray) jsonParser.parse(jsonStr);
////
////            for (int i = 0; i < jsonArray.size(); i++) {
////                JSONObject jo = (JSONObject) jsonArray.get(i);
////                String cacheCode = (String) jo.get("cacheCode");
////                Long longVersion = (Long) jo.get("version");
////                int serverVersion = longVersion.intValue();
////
////                CacheEntity localCache = cacheService.getOne(cacheCode);
////                int localVersion = localCache.getVersion();
////
////                log.info("[" + cacheCode + "] server : " + serverVersion + " / local : " + localVersion);
////
////                if (serverVersion != localVersion) {
////                    itemService.saveCacheData();
////                    cacheService.saveCacheData();
////                    log.info("reload cache data!!!");
////                }
////            }
////            //log.info("response : " + jsonArray.toJSONString());
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//    }
//
//    /**
//     * ?????? ????????? ????????? ????????? ?????? ??????, milliseconds ???????????? ??????
//     * ?????? ????????? ????????? ????????? ??????
//     */
////    @Scheduled(fixedDelay = 1000)
////    public void scheduleFixedDelayTask() throws InterruptedException {
////        log.info("Fixed delay task - {}", System.currentTimeMillis() / 1000);
////        Thread.sleep(5000);
////    }
//
//    /**
//     * ?????? ????????? ????????? ???????????? ?????? ??????, milliseconds ???????????? ??????
//     * ?????? ????????? ????????? ????????? ?????? ????????? ???????????? ??????
//     */
////    @Async // ????????? Scheduler ??? ????????? ?????? @Async ??????
////    @Scheduled(fixedRate = 1000)
////    public void scheduleFixedRateTask() throws InterruptedException {
////        log.info("Fixed rate task - {}", System.currentTimeMillis() / 1000);
////        Thread.sleep(5000);
////    }
//
//    /**
//     * ????????? initialDelay ??????(milliseconds) ????????? fixedDelay ??????(milliseconds) ???????????? ??????
//     */
////    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
////    public void scheduleFixedRateWithInitialDelayTask() {
////        long now = System.currentTimeMillis() / 1000;
////        log.info("Fixed rate task with one second initial delay -{}", now);
////    }
//
//    /**
//     * Cron ???????????? ????????? ?????? ??????
//     * ???(0-59) ???(0-59) ??????(0-23) ???(1-31) ???(1-12) ??????(0-7)
//     */
////    @Scheduled(cron = "0 15 10 15 * ?")
////    public void scheduleTaskUsingCronExpression() {
////        long now = System.currentTimeMillis() / 1000;
////        log.info("schedule tasks using cron jobs - {}", now);
////    }
//
//}
