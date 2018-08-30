package com.mmall.util;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import org.apache.commons.lang3.ObjectUtils;


public class BusinessUtil {
    public static ServerResponse userNotBeNull(User user){
        if(user == null){
            return   ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录管理员");
        }
        else{
            return  ServerResponse.createBySuccess(user);
        }
    }

    public static  ServerResponse checkAdminRole(User user) {
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return  ServerResponse.createByError();
    }
}
