package com.china.bang.projects.user.sql;

import com.china.bang.function.ResultMapperFunction;
import org.apache.commons.lang.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DBOperatorUtils {

    private static final Map<Class,String> statementParamsMapper = new HashMap<>();
    static{
        statementParamsMapper.put(int.class,"setInt");
        statementParamsMapper.put(int.class,"setInt");
        statementParamsMapper.put(Long.class,"setLong");
        statementParamsMapper.put(BigDecimal.class,"setBigDecimal");
        statementParamsMapper.put(Date.class,"setDate");
        statementParamsMapper.put(String.class,"setString");
    }


    public static<T> T queryForObject(String sql,Class<T> tClass, ResultMapperFunction<ResultSet,T> resultMapperFunction,Object... params)
    {
        List<T> tList = executeQueryList(sql, tClass, resultMapperFunction, params);
        if(Objects.isNull(tList) || tList.isEmpty())
        {
            return null;
        }
        return tList.get(0);
    }

    public static<T> List<T> queryForList(String sql, Class<T> tClass, ResultMapperFunction<ResultSet,T> resultMapperFunction,Object... params)
    {
        return executeQueryList(sql,tClass,resultMapperFunction,params);
    }


    /**
     * 执行查询语句，返回对应的值对象
     *
     * @param sql
     * @param resultMapperFunction
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> executeQueryList(String sql, Class<T> tClass, ResultMapperFunction<ResultSet, T> resultMapperFunction, Object... params) {

        Connection connection = DBConnectionManager.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            if (!Objects.isNull(params) && params.length > 0) {
                setPreparedStatement(preparedStatement, params);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            return generatorResult(resultSet, tClass, resultMapperFunction);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables.getMessage());
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable.getMessage());
        }


    }

    private static <T> List<T> generatorResult(ResultSet resultSet, Class<T> tClass,ResultMapperFunction<ResultSet,T> resultMapperFunction) {
        List<T> resultList = new ArrayList<>();
        try {
            while(resultSet.next())
            {
                T oneResult = resultMapperFunction.apply(resultSet, tClass);
                if(Objects.isNull(oneResult))
                {
                    resultList.add(oneResult);
                }
            }
            return resultList;
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables.getCause());
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable.getCause());
        }
    }

    private static void setPreparedStatement(PreparedStatement preparedStatement, Object[] params) {
        for(int paramIndex = 0;paramIndex < params.length;paramIndex++)
        {
            try {
                Class<?> paramType = params[paramIndex].getClass();
                Class primitiveWrapperType = ClassUtils.wrapperToPrimitive(paramType);
                if(Objects.isNull(primitiveWrapperType))
                {
                    primitiveWrapperType = paramType;
                }
                String methodName = statementParamsMapper.get(primitiveWrapperType);
                if(Objects.isNull(methodName) || methodName.isEmpty())
                {
                    throw new RuntimeException("error type ");
                }

                Method method = preparedStatement.getClass().getMethod(methodName,int.class,primitiveWrapperType);
                method.invoke(preparedStatement,paramIndex+1,params[paramIndex]);
            } catch (IllegalAccessException | NoSuchMethodException |InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }


    /**
     * 执行非查询语句
     *
     * @param sql
     * @param params
     * @return
     */
    public static boolean execute(String sql,Object... params)
    {
        Connection connection = DBConnectionManager.getConnectionFromSPI();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            if (!Objects.isNull(params) && params.length > 0) {
                setPreparedStatement(preparedStatement, params);
            }

             preparedStatement.execute();
            return true;
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables.getCause());
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable.getCause());
        }
    }
}
