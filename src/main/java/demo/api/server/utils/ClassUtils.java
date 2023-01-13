package demo.api.server.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ClassUtils {
    /**
     * Class 객체를 쿼리스트링 형태로 변환
     * @Author hanhyeyeon
     */
    public static String classToQueryString(Class clazz, Object o) {
        StringBuilder qsBuilder = new StringBuilder();
        Map<String, String> qsParams = new LinkedHashMap<>();

        try {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                qsParams.put(field.getName(), String.valueOf(field.get(o)));
            }

            for (Map.Entry<String, String> entry : qsParams.entrySet())
                qsBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");


        } catch (Exception e) {
            e.printStackTrace();
        }

        String qsString = qsBuilder.toString();

        return qsString.substring(0, qsString.length()-1);
    }

    /**
     * Class 객체를 쿼리스트링 형태로 변환(null인 경우 default값으로 넣음)
     * @Author hanhyeyeon
     */
    public static String classToQueryStringWithDefault(Class clazz, Object o, String defaultIfNull) {
        StringBuilder qsBuilder = new StringBuilder();
        Map<String, String> qsParams = new LinkedHashMap<>();

        try {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);

                String val = (ObjectUtils.isNotEmpty(field.get(o))? String.valueOf(field.get(o)) : defaultIfNull);
                qsParams.put(field.getName(), val);
            }

            for (Map.Entry<String, String> entry : qsParams.entrySet())
                qsBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");


        } catch (Exception e) {
            e.printStackTrace();
        }

        String qsString = qsBuilder.toString();

        return qsString.substring(0, qsString.length()-1);
    }

    public static <T> String printEnum(Class cls) {
        String comma = ",";
        if (cls.isEnum()) {
            StringBuffer sb = new StringBuffer();
            sb.append(cls.getSimpleName());
            sb.append(" : ");

            Object classList = Arrays.stream(cls.getEnumConstants()).collect(Collectors.toList());
            //enumList.addAll((Collection<? extends T>) classList);
            List<? extends T> list = new ArrayList<>();
            list.addAll((Collection) classList);

            for (T code : list) {
                sb.append(code.toString());
                sb.append(comma + " ");
            }

            String result = StringUtils.removeEnd(sb.toString().trim(), comma);

            return StringUtils.isNotEmpty(result)? result : "";
        }
        return "";
    }

    /**
     * 클래스 타입 변경
     * Entity -> Dto 등 변환처리에 필요 : 같은 필드의 값을 넣어 줌.
     * 유의사항) 필드명이 같아도 데이터 타입이 다르면 오류날 수 있음.
     * :: 사용방법 ::
     * OrdEntity ordEntity = this.ordRepository.getOne(주문번호); //@Transactional(rollbackFor = Exception.class) 이 선언되어 있어야 함!!
     * OrdDto ordDto = (OrdDto) ClassUtils.classTypeChange(OrdEntity.class, ordEntity, new OrdDto());
     * ※ @Transactional(rollbackFor = Exception.class) 이 선언되어 있지 않는 경우 아래 처럼 세팅.
     * Optional<OrdEntity> optionalOrdEntity = this.ordRepository.findById(주문번호);
     * OrdEntity ordEntity = null;
     * if(optionalOrdEntity.isPresent()) ordEntity = optionalOrdEntity.get();
     *
     * @author Ryan
     * @param orgClass 원본 클래스
     * @param dataObj 원본 데이터 객체
     * @param destObj 대상 객체 생성자
     * @return 변환 완료된 대상 객체
     */
    public static Object classTypeChange(Class<?> orgClass, Object dataObj, Object destObj) {

        Field[] orgFields = orgClass.getDeclaredFields();

        Class<?> dataClass = dataObj.getClass();

        Class<?> destClass = destObj.getClass();
        Field[] destFields = destClass.getDeclaredFields();


        for(Field destField : destFields) {
            String fieldName = destField.getName();
            if (Arrays.stream(orgFields).anyMatch(field -> field.getName().equals( fieldName ) )) {
                String methodFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                try {
                    Method dataMethod = dataClass.getDeclaredMethod("get" + methodFieldName);

                    dataMethod.setAccessible(true);
                    destField.setAccessible(true);

                    //※필드명이 같은데 타입이 다르면 오류 날 수 있음.
                    destField.set(destObj, dataMethod.invoke(dataObj));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return destObj;
    }
}
