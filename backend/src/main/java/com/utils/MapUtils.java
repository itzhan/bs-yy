
package com.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


/**
 * Map工具类
 */
public class MapUtils extends HashMap<String, Object> {

    @Override
    public MapUtils put(String key, Object value) {
        super.put(key, value);
        return this;
    }
    
         
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }
 
    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T obj = clazz.newInstance();
        for (Entry<String, Object> entry : map.entrySet()) {
            Field field = null;
            try {
                field = clazz.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                                Object value = entry.getValue();

                if (value != null) {
                    Class<?> fieldType = field.getType();

                    if (fieldType == Double.class && value instanceof String) {
                        try {
                            value = Double.parseDouble((String) value);
                        } catch (NumberFormatException e) {
                            value = null;
                        }
                    } else if (fieldType == Integer.class && value instanceof String) {
                        try {
                            value = Integer.parseInt((String) value);
                        } catch (NumberFormatException e) {
                            value = null;
                        }
                    } else if (fieldType == Long.class && value instanceof String) {
                        try {
                            value = Long.parseLong((String) value);
                        } catch (NumberFormatException e) {
                            value = null;
                        }
                    } else if (fieldType == Float.class && value instanceof String) {
                        try {
                            value = Float.parseFloat((String) value);
                        } catch (NumberFormatException e) {
                            value = null;
                        }
                    }
                }

                field.set(obj, value);
            } catch (NoSuchFieldException ignored) {
            }
        }
        return obj;
    }
}

