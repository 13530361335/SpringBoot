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

    public static <T> T toObject(Map<String, Object> map, Class<T> clazz) {
        if (map == null)
            return null;
        try {
            if ("java.util.Map".equals(clazz.getTypeName())) {
                return (T) map;
            }
            T obj = clazz.newInstance();
            BeanUtils.populate(obj, map);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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