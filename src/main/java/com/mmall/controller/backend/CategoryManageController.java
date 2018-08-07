package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired
    private IUserService  iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    //
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue ="0")  int parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，需要登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员
            //增加分类的逻辑
            return iCategoryService.addCategory(categoryName,parentId);
        }else {
            return ServerResponse.createByErrorMessage("你无权限进行此项操作");
        }
    }

    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName){
       User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，需要登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员
            //增加分类的逻辑
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        }else {
            return ServerResponse.createByErrorMessage("你无权限进行此项操作");
        }
    }
}
