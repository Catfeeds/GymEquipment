package com.saiyi.libfast.error.http;

import com.saiyi.libfast.error.CustomError;
import com.saiyi.libfast.http.exception.ErrorStatus;

import java.util.HashMap;
import java.util.Map;

public class CodeHelper {

    private final static Map<Integer, CustomError> codeMap = new HashMap<>();

    static {
        codeMap.put(1001, CodeError.CODE1001);
    }

    public static ErrorStatus handleCodeError(int code) {
        ErrorStatus exception = new ErrorStatus();
        if (codeMap.containsKey(code)) {
            exception.setError(codeMap.get(code));
        } else {
            exception.setError(CodeError.CODE_UNKNOW);
        }
        return exception;
    }

}
