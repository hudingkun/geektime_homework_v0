package com.china.bang.function;

public interface ResultMapperFunction<T,R> {

    R apply(T t,Class<R> rClass) throws Throwable;

    default R execute(T t,Class<R> rClass) throws RuntimeException
    {
        R rResult = null;
        try {
            rResult = (R) apply(t,rClass);
        }catch (Throwable throwable)
        {
            throw new RuntimeException(throwable.getCause());
        }
        return rResult;
    }

    static <T, R> R execute(T t, Class<R> rClass, ResultMapperFunction<T, R> function) {
        return function.execute(t,rClass);
    }
}
