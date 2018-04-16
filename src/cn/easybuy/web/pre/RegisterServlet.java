package cn.easybuy.web.pre;

import cn.easybuy.entity.User;
import cn.easybuy.service.user.UserService;
import cn.easybuy.service.user.UserServiceImpl;
import cn.easybuy.utils.Constants;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.RegUtils;
import cn.easybuy.utils.ReturnResult;
import cn.easybuy.utils.SecurityUtils;
import cn.easybuy.web.AbstractServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by bdqn on 2016/5/3.
 */
@WebServlet(urlPatterns = {"/Register"}, name = "Register")
public class RegisterServlet extends AbstractServlet {

    private UserService userService;

    public void init() throws ServletException {
        userService = new UserServiceImpl();
    }

    /**
     * 返回
     *
     * @return
     */
    @Override
    public Class getServletClass() {
        return RegisterServlet.class;
    }

    /**
     * 跳到注册页面
     *
     * @param request
     * @param response
     * @return
     */
    public String toRegister(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "/pre/register";
    }

    /**
     * 保存用户信息
     *
     * @param request
     * @param response
     * @return
     */
    public ReturnResult saveUserToDatabase(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReturnResult result = new ReturnResult();
        try {
            User user = new User();
            String loginName = request.getParameter("loginName");
            String sex = request.getParameter("sex");
            User oldUser = userService.getUser(null, loginName);
            //判断用户
            if (EmptyUtils.isNotEmpty(oldUser)) {
                result.returnFail("用户已经存在");
                return result;
            }
            user.setLoginName(request.getParameter("loginName"));
            user.setUserName(request.getParameter("userName"));
            user.setSex(EmptyUtils.isEmpty(sex) ? 1 : 0);
            user.setPassword(SecurityUtils.md5Hex(request.getParameter("password")));
            user.setIdentityCode(request.getParameter("identityCode"));
            user.setEmail(request.getParameter("email"));
            user.setMobile(request.getParameter("mobile"));
            user.setType(Constants.UserType.PRE);
            result=checkUser(user);
            //是否验证通过
            if(result.getStatus()==Constants.ReturnResult.SUCCESS){
            	 if(!userService.add(user)){
                	 return result.returnFail("注册失败！");
                 }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.returnSuccess("注册成功");
        return result;
    }
    
    private ReturnResult checkUser(User user){
    	ReturnResult result = new ReturnResult();
    	boolean flag=true;
    	if(EmptyUtils.isNotEmpty(user.getMobile())){
    		if(!RegUtils.checkMobile(user.getMobile())){
    			return result.returnFail("手机格式不正确");
    		}
    	}
    	
    	if(EmptyUtils.isNotEmpty(user.getIdentityCode())){
    		if(!RegUtils.checkIdentityCodeReg(user.getIdentityCode())){
    			return result.returnFail("身份证号码不正确");
    		}
    	}
    	
    	if(EmptyUtils.isNotEmpty(user.getEmail())){
    		if(!RegUtils.checkEmail(user.getEmail())){
    			return result.returnFail("邮箱格式不正确");
    		}
    	}
    	return result.returnSuccess();
    }
}
