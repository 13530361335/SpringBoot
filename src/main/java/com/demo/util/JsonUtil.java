package com.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.beanutils.BeanUtils.populate;


public class JsonUtil {

    private final static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static String toString(Object object) {
        return JSON.toJSONString(object, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

        public static <T> T map2Object(Map<String, Object> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }
        T obj = null;
        try {
            obj = clazz.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                String filedTypeName = field.getType().getName();
                if (filedTypeName.equalsIgnoreCase("java.util.date")) {
                    String datetimestamp = String.valueOf(map.get(field.getName()));
                    if (datetimestamp.equalsIgnoreCase("null")) {
                        field.set(obj, null);
                    } else {
                        field.set(obj, new Date(Long.parseLong(datetimestamp)));
                    }
                } else {
                    field.set(obj, map.get(field.getName()));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return obj;
    }
    
        /**
     * 实体对象转成Map
     * @param obj 实体对象
     * @return
     */
    public static Map<String, Object> object2Map(Object obj) {
        Map<String, Object> map = new HashMap();
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return map;
    }

    public static Map<String, Object> toMap(Object object) {
        if (object == null) {
            return null;
        }
        return new BeanMap(object);
    }

    public static <T> T change(Object object, Class<T> clazz) {
        return toObject(toMap(object), clazz);
    }

    public static String[] getFieldNames(Object obj) {
        if (obj == null) {
            return null;
        }
        List<String> list = new LinkedList<>();
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(obj.getClass());
        } catch (IntrospectionException e) {
            logger.error(e.getMessage(), e);
            return new String[0];
        }
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String fieldName = property.getName();
            //默认PropertyDescriptor会有一个class对象，剔除之
            if (fieldName.compareToIgnoreCase("class") == 0) {
                continue;
            }
            list.add(fieldName);
        }
        return list.toArray(new String[list.size()]);
    }


}
