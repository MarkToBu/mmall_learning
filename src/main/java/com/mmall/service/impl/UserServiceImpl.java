package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.ibatis.session.ResultContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iuserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {

        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        //  todo 密码登录md5
        String md5Password = MD5Util.MD5EncodeUtf8(password);


        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        //登录成功
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    public ServerResponse<String> register(User user) {
//        int resultCount = userMapper.checkUsername(user.getUsername());
//        if (resultCount > 0) {
//            return ServerResponse.createByErrorMessage("用户名已经存在");555
//        }

        ServerResponse vaildReponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if( !vaildReponse.isSuccess()){
            return vaildReponse;
        }
           vaildReponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if( !vaildReponse.isSuccess()){
            return vaildReponse;
        }

        int  resultCount = userMapper.checkEmail(user.getEmail());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("邮箱已经存在");
        }
        user.setRole(Const.Role.ROLE_CONSTOMER);

        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        resultCount = userMapper.insert(user);

        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    public ServerResponse<String> checkValid(String str, String type) {
         if(StringUtils.isNotBlank(type)){
              //开始校验
             if(Const.USERNAME.equals(type)){
                 int resultCount = userMapper.checkUsername(str);
                 if(resultCount > 0){
                     return ServerResponse.createByErrorMessage("用户名已经存在");
                 }
             }

             if(Const.EMAIL.equals(type)){
                 int resultCount = userMapper.checkEmail(str);
                 if(resultCount > 0){
                      return ServerResponse.createByErrorMessage("邮箱已经存在");
                 }
             }


         }else{
             return ServerResponse.createByErrorMessage("参数错误");
         }
         return ServerResponse.createBySuccessMessage("校验成功");
    }

    public ServerResponse selectQuestion(String username){
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //说明用户不存在，
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return  ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题时空的");
    }

    public ServerResponse<String> checkAnswer(String username,String question,String  answer){
        int resultCont = userMapper.checkAnnswer( username,  question,   answer);
        if(resultCont > 0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken );
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题回答错误");
    }
    public ServerResponse <String> forgetResetPassword(String username ,String passwordNew,String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return  ServerResponse.createByErrorMessage("参数错误，Token需要传递");
        }
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //说明用户不存在，
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String token = TokenCache.getkey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }
        if(StringUtils.equals(forgetToken,token)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);

            if(rowCount >0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else {
            return  ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的Token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
         if(resultCount == 0){
             return ServerResponse.createByErrorMessage("原始密码错误");
         }
         user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
         int updateCount = userMapper.updateByPrimaryKeySelective(user);
         if(updateCount > 0){
             return ServerResponse.createBySuccessMessage("密码更新成功");
         }
         return ServerResponse.createByErrorMessage("密码更新失败");
    }

    public ServerResponse<User> updateInformation(User user){
        //校验邮箱和用户名是否一致
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount >0 ){
            return ServerResponse.createByErrorMessage("email已经存在了，请更换email进行尝试");
        }
        User updateUser  = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("更新个人信息成功",updateUser);
        }
        return  ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    //获取用户的信息
    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY );
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse checkAdminRole(User user) {
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return  ServerResponse.createByError();
    }
}
