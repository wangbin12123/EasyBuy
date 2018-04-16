package cn.easybuy.web.pre;

import cn.easybuy.entity.Product;
import cn.easybuy.entity.User;
import cn.easybuy.service.product.ProductService;
import cn.easybuy.service.product.ProductServiceImpl;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.MemcachedUtils;
import cn.easybuy.utils.ReturnResult;
import cn.easybuy.web.AbstractServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bdqn on 2016/5/5.
 */
@WebServlet(urlPatterns = {"/Favorite"}, name = "Favorite")
public class FavoriteServlet extends AbstractServlet {


    private ProductService productService;

    public void init() throws ServletException {
        productService = new ProductServiceImpl();
    }

    @Override
    public Class getServletClass() {
        return FavoriteServlet.class;
    }

    /**
     * 跳转到历史记录
     *
     * @param request
     * @param response
     * @return
     */
    public String toFavoriteList(HttpServletRequest request, HttpServletResponse response)throws Exception {
        List<Product> recentProducts=queryFavoriteList(request);
        request.setAttribute("recentProducts",recentProducts);
        return "/pre/product/favoriteList";
    }

    /**
     * 添加到收藏
     *
     * @return
     */
    public ReturnResult addFavorite(HttpServletRequest request, HttpServletResponse response)throws Exception {
        ReturnResult result = new ReturnResult();
        String id = request.getParameter("id");
        Product product = productService.getProductById(Integer.valueOf(id));
        List<Product> favoriteList = queryFavoriteList(request);
        //判断是否满了
        if (favoriteList.size() > 0 && favoriteList.size() == 5) {
            favoriteList.remove(0);
        }
        boolean temp = false;
        for (int i = 0; i < favoriteList.size(); i++) {
            if (favoriteList.get(i).getId().equals(product.getId())) {
                temp = true;
                break;
            }
        }
        if (!temp) {
            favoriteList.add(favoriteList.size(), product);
        }
        MemcachedUtils.add(getFavoriteKey(request),favoriteList);
        result.returnSuccess();
        return result;
    }

    /**
     * 查询最近商品
     *
     * @return
     */
    private List<Product> queryFavoriteList(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");
        //判断用户是否登录
        String key = EmptyUtils.isEmpty(user) ? session.getId() : user.getLoginName();
        List<Product> recentProducts = (List<Product>) MemcachedUtils.get(key);
        if (EmptyUtils.isEmpty(recentProducts)) {
            recentProducts = new ArrayList<Product>();
        }
        return recentProducts;
    }
    /**
     *
     * @param request
     * @return
     */
    private String getFavoriteKey(HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");
        return EmptyUtils.isEmpty(user) ? session.getId() : user.getLoginName();
    }
}
