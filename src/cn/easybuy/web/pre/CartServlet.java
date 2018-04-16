package cn.easybuy.web.pre;

import cn.easybuy.entity.*;
import cn.easybuy.service.order.CartService;
import cn.easybuy.service.order.CartServiceImpl;
import cn.easybuy.service.order.OrderService;
import cn.easybuy.service.order.OrderServiceImpl;
import cn.easybuy.service.product.ProductCategoryService;
import cn.easybuy.service.product.ProductCategoryServiceImpl;
import cn.easybuy.service.product.ProductService;
import cn.easybuy.service.product.ProductServiceImpl;
import cn.easybuy.service.user.UserAddressService;
import cn.easybuy.service.user.UserAddressServiceImpl;
import cn.easybuy.service.user.UserService;
import cn.easybuy.service.user.UserServiceImpl;
import cn.easybuy.utils.Constants;
import cn.easybuy.utils.DataSourceUtil;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.ProductCategoryVo;
import cn.easybuy.utils.ReturnResult;
import cn.easybuy.utils.ShoppingCart;
import cn.easybuy.utils.ShoppingCartItem;
import cn.easybuy.web.AbstractServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by bdqn 2016/5/3.
 */
@WebServlet(urlPatterns = {"/Cart"}, name = "Cart")
public class CartServlet extends AbstractServlet {

    private ProductService productService;

    private OrderService orderService;

    private UserService userService;

    private ProductCategoryService productCategoryService;

    private CartService cartService;

    private UserAddressService userAddressService;

    public void init() throws ServletException {
        productService = new ProductServiceImpl();
        orderService = new OrderServiceImpl();
        userService = new UserServiceImpl();
        productCategoryService = new ProductCategoryServiceImpl();
        cartService = new CartServiceImpl();
        userAddressService = new UserAddressServiceImpl();
    }

    @Override
    public Class getServletClass() {
        return CartServlet.class;
    }

    /**
     * 添加到购物车
     *
     * @return
     */
    public ReturnResult add(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        ReturnResult result = new ReturnResult();
        String id = request.getParameter("entityId");
        String quantityStr = request.getParameter("quantity");
        Integer quantity = 1;
        if (!EmptyUtils.isEmpty(quantityStr))
            quantity = Integer.parseInt(quantityStr);
        //查询出商品
        Product product = productService.getProductById(Integer.valueOf(id));
        if(product.getStock()<quantity){
        	return result.returnFail("商品数量不足");
        }
        //获取购物车
        ShoppingCart cart = getCartFromSession(request);
        //往购物车放置商品
        result=cart.addItem(product, quantity);
        if(result.getStatus()==Constants.ReturnResult.SUCCESS){
        	cart.setSum((EmptyUtils.isEmpty(cart.getSum()) ? 0.0 : cart.getSum()) + (product.getPrice() * quantity * 1.0));
        }
        return result;
    }
    
    

    /**
     * 刷新购物车
     *
     * @param request
     * @param response
     * @return
     */
    public String refreshCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        ShoppingCart cart = getCartFromSession(request);
        cart = cartService.calculate(cart);
        session.setAttribute("cart", cart);//全部的商品
        return "/common/pre/searchBar";
    }

    /**
     * 跳到结算页面
     *
     * @param request
     * @param response
     * @return
     */
    public String toSettlement(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<ProductCategoryVo> productCategoryVoList = productCategoryService.queryAllProductCategoryList();
        //封装返回
        request.setAttribute("productCategoryVoList", productCategoryVoList);
        return "/pre/settlement/toSettlement";
    }

    /**
     * 跳转到购物车页面
     *
     * @param request
     * @param response
     * @return
     */
    public String settlement1(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ShoppingCart cart = getCartFromSession(request);
    	cart = cartService.calculate(cart);
    	request.getSession().setAttribute("cart",cart);
    	return "/pre/settlement/settlement1";
    }

    /**
     * @param request
     * @param response
     * @return
     */
    public String settlement2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = getUserFromSession(request);
        List<UserAddress> userAddressList = userAddressService.queryUserAdressList(user.getId());
        request.setAttribute("userAddressList", userAddressList);
        return "/pre/settlement/settlement2";
    }

    /**
     * 生成订单
     *
     * @param request
     * @param response
     * @return
     */
    public Object settlement3(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ShoppingCart cart = getCartFromSession(request);
        cart = cartService.calculate(cart);
        User user = getUserFromSession(request);
        ReturnResult result=checkCart(request);
        if(result.getStatus()==Constants.ReturnResult.FAIL){
        	return result;
        }
        //新增地址
        String addressId=request.getParameter("addressId");
        String newAddress=request.getParameter("newAddress");
        String newRemark=request.getParameter("newRemark");
        UserAddress userAddress=new UserAddress();
        if(addressId.equals("-1")){
            userAddress.setRemark(newRemark);
            userAddress.setAddress(newAddress);
            userAddress.setId(userAddressService.addUserAddress(user.getId(),newAddress,newRemark));
        }else{
            userAddress=userAddressService.getUserAddressById(Integer.parseInt(addressId));
        }
        
        //生成订单
        Order order = orderService.payShoppingCart(cart,user,userAddress.getAddress());
        clearCart(request, response);
        request.setAttribute("currentOrder", order);
        return "/pre/settlement/settlement3";
    }

    /**
     * 清空购物车
     *
     * @param request
     * @param response
     */
    public ReturnResult clearCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ReturnResult result = new ReturnResult();
        //结账后清空购物车
        request.getSession().removeAttribute("cart");
        result.returnSuccess(null);
        return result;
    }

    /**
     * 修改购物车信息
     *
     * @param request
     * @return
     */
    public ReturnResult modCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ReturnResult result = new ReturnResult();
    	HttpSession session = request.getSession();
        String id = request.getParameter("entityId");
        String quantityStr = request.getParameter("quantity");
        ShoppingCart cart = getCartFromSession(request);
    	Product product=productService.getProductById(Integer.valueOf(id));
    	if(EmptyUtils.isNotEmpty(quantityStr)){
    		if(Integer.parseInt(quantityStr)>product.getStock()){
    			return result.returnFail("商品数量不足");
        	}
    	}
        cart = cartService.modifyShoppingCart(id, quantityStr, cart);
        session.setAttribute("cart", cart);//全部的商品
        return result.returnSuccess();
    }

    /**
     * 从session中获取购物车
     *
     * @param request
     * @return
     */
    private ShoppingCart getCartFromSession(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
    
    private ReturnResult checkCart(HttpServletRequest request) throws Exception {
    	ReturnResult result = new ReturnResult();
    	HttpSession session = request.getSession();
    	ShoppingCart cart = getCartFromSession(request);
    	cart = cartService.calculate(cart);
    	for (ShoppingCartItem item : cart.getItems()) {
           Product product=productService.getProductById(item.getProduct().getId());
           if(product.getStock()<item.getQuantity()){
        	   return result.returnFail(product.getName()+"商品数量不足");
           }
        }
    	return result.returnSuccess();
    }

    /**
     * @param request
     * @return
     */
    private User getUserFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");
        return user;
    }
}
