package com.example.microservice.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.UUID;

@UtilityClass
public class BeanUtils {
    @SneakyThrows
    public void copyNonNullProperties(Object source, Object destination) {
        Class<?> clazz = source.getClass();
        Field[] filelds = clazz.getDeclaredFields();
        for (Field field : filelds) {
            field.setAccessible(true);
            Object value = field.get(source);
            if (value != null) {
                field.set(destination, value);
            }
        }
    }
}
