package cn.easybuy.web.pre;
import cn.easybuy.entity.User;
import cn.easybuy.service.user.UserService;
import cn.easybuy.service.user.UserServiceImpl;
import cn.easybuy.utils.EmptyUtils;
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
@WebServlet(urlPatterns = { "/Login" }, name = "Login")
public class LoginServlet extends AbstractServlet{
    //注入用户业务类
    private UserService userService;

    public void init() throws ServletException {
        userService = new UserServiceImpl();
    }
    /**
     * 重写相关方法
     * @return
     */
    @Override
    public Class getServletClass() {
        return LoginServlet.class;
    }
    /**
     * 跳转到登陆界面
     * @param request
     * @param response
     * @return
     */
    public String toLogin(HttpServletRequest request,HttpServletResponse response)throws Exception{
        return "/pre/login";
    }
    /**
     * 登陆的方法
     * @param request
     * @return
     */
    public ReturnResult login(HttpServletRequest request,HttpServletResponse response)throws Exception{
        ReturnResult result=new ReturnResult();
        //参数获取
        String loginName=request.getParameter("loginName");
        String password=request.getParameter("password");
        User user=userService.getUser(null, loginName);
        if(EmptyUtils.isEmpty(user)){
            result.returnFail("用户不存在");
        }else{
           if(user.getPassword().equals(SecurityUtils.md5Hex(password))){
               //登陆成功
               request.getSession().setAttribute("loginUser", user);
               result.returnSuccess("登陆成功");
           }else{
               result.returnFail("密码错误");
           }
        }
        return result;
    }
    /**
     * 登陆的方法
     * @param request
     * @return
     */
    public String loginOut(HttpServletRequest request,HttpServletResponse response)throws Exception{
        ReturnResult result=new ReturnResult();
        try {
            User user=(User)request.getSession().getAttribute("loginUser");
            request.getSession().removeAttribute("loginUser");
            // 清除购物车
            request.getSession().removeAttribute("cart");
            request.getSession().removeAttribute("cart2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.returnSuccess("注销成功");
        return "/pre/login";
    }
}
