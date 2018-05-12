package com.yy.util;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
    public static boolean checkVerifyCode(HttpServletRequest request){
        //获取实际的验证码
        String verifyCodeExpected = (String) request.getSession().getAttribute(
                     Constants.KAPTCHA_SESSION_KEY);
        //获取输入的验证码
        String verifyCodeActual = HttpServletRequestUtil.getString(request,"verifyCodeActual");
        if(verifyCodeActual==null || !verifyCodeActual.equals(verifyCodeExpected)){
            return false;
        }
        return true;

    }
}
