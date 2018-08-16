package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;

    /**
     * 新增或修改商品
     * @param session
     * @param product
     * @return
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充增加产品的逻辑
           return  iProductService.saveOrUpdateProduct(product);

        }else{
            return ServerResponse.createByErrorMessage("你无权进行此操作");
        }
    }

    /**
     * 新增或修改商品
     * @param session
     * @param
     * @return
     */
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus (HttpSession session,Integer productId,Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充增加产品的逻辑
            return  iProductService.setSaleStatus(productId,status);

        }else{
            return ServerResponse.createByErrorMessage("你无权进行此操作");
        }
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetial(HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
           //显示详情页
            return iProductService.manageProductDetial(productId);

        }else{
            return ServerResponse.createByErrorMessage("你无权进行此操作");
        }

    }

    /**
     * 获取产品列表（分页）
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10")  int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //显示详情页
            return iProductService.getProductList(pageNum,pageSize);


        }else{
            return ServerResponse.createByErrorMessage("你无权进行此操作");
        }

    }

    /**
     * 文件上传，根据指定产品名称和id进行查找.
     * @param session
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session,String productName,Integer productId, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10")  int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
             //填充业务逻辑
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);

        }else{
            return ServerResponse.createByErrorMessage("你无权进行此操作");
        }

    }
}

