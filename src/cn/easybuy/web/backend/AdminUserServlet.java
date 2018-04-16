package cn.easybuy.web.backend;

import cn.easybuy.entity.User;
import cn.easybuy.service.user.UserService;
import cn.easybuy.service.user.UserServiceImpl;
import cn.easybuy.utils.Constants;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.Pager;
import cn.easybuy.utils.Params;
import cn.easybuy.utils.RegUtils;
import cn.easybuy.utils.ReturnResult;
import cn.easybuy.utils.SecurityUtils;
import cn.easybuy.web.AbstractServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by bdqn on 2016/5/6.
 */
@WebServlet(urlPatterns = {"/admin/user"}, name = "adminUser")
public class AdminUserServlet extends AbstractServlet {

    private UserService userService;

    public void init() throws ServletException {
        userService = new UserServiceImpl();
    }

    @Override
    public Class getServletClass() {
        return AdminUserServlet.class;
    }

    /**
     * 跳到用户主页
     *
     * @param request
     * @param response
     * @return
     */
    public String index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取登陆用户
        User user = (User) request.getSession().getAttribute("loginUser");
        request.setAttribute("user", user);
        request.setAttribute("menu", 2);
        return "/backend/user/userInfo";
    }

    /**
     * 订单的主页面
     *
     * @param request
     * @param response
     * @return
     */
    public String queryUserList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取当前页数
        String currentPageStr = request.getParameter("currentPage");
        //获取页大小
        String pageSize = request.getParameter("pageSize");
        int rowPerPage = EmptyUtils.isEmpty(pageSize) ? 10 : Integer.parseInt(pageSize);
        int currentPage = EmptyUtils.isEmpty(currentPageStr) ? 1 : Integer.parseInt(currentPageStr);
        int total = userService.count();
        Pager pager = new Pager(total, rowPerPage, currentPage);
        List<User> userList = userService.getUserList(currentPage, rowPerPage);
        pager.setUrl("/admin/user?action=queryUserList");
        request.setAttribute("userList", userList);
        request.setAttribute("pager", pager);
        request.setAttribute("menu", 8);
        return "/backend/user/userList";
    }

    /**
     * 修改用户信息
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String toUpdateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        User user = userService.getUser(Integer.parseInt(id), null);
        request.setAttribute("user", user);
        return "/backend/user/toUpdateUser";
    }

    /**
     * 去添加用户
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String toAddUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = new User();
        request.setAttribute("user", user);
        return "/backend/user/toUpdateUser";
    }
    /**
     * 更新用户
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ReturnResult updateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReturnResult result = new ReturnResult();
        String id = request.getParameter("id");
        User user = new User();
        String loginName = request.getParameter("loginName");
        String sex = request.getParameter("sex");
        User oldUser = userService.getUser(null, loginName);
        
        //判断用户
        if (EmptyUtils.isNotEmpty(oldUser) && (EmptyUtils.isEmpty(id) || oldUser.getId() != Integer.parseInt(id))) {
            result.returnFail("用户已经存在");
            return result;
        }
        user.setLoginName(loginName);
        user.setUserName(request.getParameter("userName"));
        user.setSex(EmptyUtils.isEmpty(sex) ? 1 : 0);
        if (EmptyUtils.isEmpty(id) || id.equals("0")) {
        	user.setPassword(SecurityUtils.md5Hex(request.getParameter("password")));
        }
        user.setIdentityCode(request.getParameter("identityCode"));
        user.setEmail(request.getParameter("email"));
        user.setMobile(request.getParameter("mobile"));
        user.setType(Integer.parseInt(request.getParameter("type")));
        
        result=checkUser(user);
        //是否验证通过
        if(result.getStatus()==Constants.ReturnResult.SUCCESS){
        	 if (EmptyUtils.isEmpty(id) || id.equals("0")) {
                 if(!userService.add(user)){
                	 return result.returnFail("增加失败！");
                 }
             } else {
             	user.setId(Integer.parseInt(id));
             	 if(!userService.update(user)){
                	 return result.returnFail("修改失败！");
                 }
             }
        }
        result.returnSuccess();
        return result;
        
        
    }
    
    public ReturnResult deleteUserById(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	  ReturnResult result = new ReturnResult();
    	  String id = request.getParameter("id");
    	  userService.deleteUserById(Integer.parseInt(id));
    	  result.returnSuccess();
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
