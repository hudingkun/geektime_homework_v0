package com.china.bang.function;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ResultMapperMethod {

    static Map<Class, String> resultSetMethodMappings = new HashMap<>();

    static {
        resultSetMethodMappings.put(Long.class, "getLong");
        resultSetMethodMappings.put(String.class, "getString");
    }

    public static <T,R> R resultMapper(ResultSet resultSet,Class<R> rClass) throws Throwable {
        //Introspector 了解
        BeanInfo beanInfo = Introspector.getBeanInfo(rClass, Object.class);
        R returnObject = rClass.newInstance();
        for(PropertyDescriptor propertyDescriptor:beanInfo.getPropertyDescriptors())
        {
            String fieldName = propertyDescriptor.getName();
            Class<?> propertyType = propertyDescriptor.getPropertyType();

            String resultSetMethodName = resultSetMethodMappings.get(propertyType);
            Method resultSetMethod = resultSet.getClass().getMethod(resultSetMethodName);
            Object resultValue = resultSetMethod.invoke(resultSet, fieldName);

            Method writeMethod = propertyDescriptor.getWriteMethod();
            writeMethod.invoke(returnObject,resultValue);
        }
        return returnObject;
    }
}
