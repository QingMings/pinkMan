package com.yhl.pinkMan.util;

import com.yhl.pinkMan.util.annotation.EnumKey;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;


public abstract class EnumUtils {

    public static <E extends Enum<E>> E valueOf(Class<E> enumClass, Object value) {
    	 if (Objects.isNull(value)) {
            return null;
        }
        if (Objects.isNull(enumClass)) {
            throw new IllegalArgumentException("enumClass can't be null");
        }
        // 找到枚举类中贴有@EnumKey注解的字段
        Optional<Field> enumKeyField = findEnumKeyField(enumClass);
        if (!enumKeyField.isPresent()) {
            throw new IllegalArgumentException(
                    enumClass + " must have a field with the annotation " + EnumKey.class);
        }
        Field field = enumKeyField.get();
        E[] enumConstants = enumClass.getEnumConstants();
        for (E enumConstant : enumConstants) {
            try {
                if (value.equals(field.get(enumConstant))) {
                    return enumConstant;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        throw new IllegalArgumentException(value + " can't convert to " + enumClass);
    }


    private static <E extends Enum<E>> Optional<Field> findEnumKeyField(Class<E> enumClass) {
        return Arrays.stream(enumClass.getDeclaredFields())
                //过滤出有EnumKey注解的属性
                .filter(field -> Objects.nonNull(field.getAnnotation(EnumKey.class)))
                //将私有属性设为可以获取值
                .peek(field -> field.setAccessible(Boolean.TRUE)).findAny();
    }
}
