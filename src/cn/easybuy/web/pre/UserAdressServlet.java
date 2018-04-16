package cn.easybuy.web.pre;

import cn.easybuy.service.user.UserAddressService;
import cn.easybuy.service.user.UserAddressServiceImpl;
import cn.easybuy.web.AbstractServlet;

import javax.servlet.ServletException;

/**
 * Created by bdqn on 2016/5/12.
 */
public class UserAdressServlet extends AbstractServlet {

    private UserAddressService userAddressService;

    public void init() throws ServletException {
        userAddressService = new UserAddressServiceImpl();
    }

    @Override
    public Class getServletClass() {
        return UserAdressServlet.class;
    }
}
