package by.shimakser.interceptor;

import org.springframework.stereotype.Component;

@Component
public abstract class BaseInterceptor {

    public static final String USER_ID = "id";
    public static final String USERNAME = "username";
    public static final String USERNAME_KEY = "preferred_username";
    public static final String USER_ATTRIBUTE = "attribute";
    public static final String USER_ATTRIBUTE_KEY = "users_attribute";

    public static final String TRACE_ID = "X-REQUEST-ID";
    public static final String OPERATION_ID = "operationId";
    public static final String PARENT_OPERATION_ID = "parentOperationId";

    public String getRandomNumber() {
        int value = (int) (Math.random() * ((100 - 1) + 1));
        return String.valueOf(value);
    }
}
