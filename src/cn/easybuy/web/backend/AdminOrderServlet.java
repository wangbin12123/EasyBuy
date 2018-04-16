package cn.easybuy.web.backend;
import cn.easybuy.entity.*;
import cn.easybuy.service.order.OrderService;
import cn.easybuy.service.order.OrderServiceImpl;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.Pager;
import cn.easybuy.utils.Params;
import cn.easybuy.web.AbstractServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by bdqn on 2016/5/6.
 */
@WebServlet(urlPatterns = {"/admin/order"},name = "order")
public class AdminOrderServlet extends AbstractServlet{

    private OrderService orderService;

    public void init() throws ServletException {
        orderService = new OrderServiceImpl();
    }
    /**
     * 订单的主页面
     * @param request
     * @param response
     * @return
     */
    public String index(HttpServletRequest request,HttpServletResponse response)throws Exception{
        //获取用户id
        String userId=request.getParameter("userId");
        //获取当前页数
        String currentPageStr = request.getParameter("currentPage");
        //获取页大小
        String pageSize = request.getParameter("pageSize");
        int rowPerPage  = EmptyUtils.isEmpty(pageSize)?5:Integer.parseInt(pageSize);
        int currentPage = EmptyUtils.isEmpty(currentPageStr)?1:Integer.parseInt(currentPageStr);
        int total=orderService.count(Integer.valueOf(userId));
        Pager pager=new Pager(total,rowPerPage,currentPage);
        pager.setUrl("/admin/order?action=index&userId="+userId);
        List<Order> orderList=orderService.getOrderList(Integer.valueOf(userId), currentPage, rowPerPage);
        request.setAttribute("orderList", orderList);
        request.setAttribute("pager", pager);
        request.setAttribute("menu", 1);
        return "/backend/order/orderList";
    }
    /**
     * 查询订单明细
     * @param request
     * @param response
     * @return
     */
    public String queryOrderDeatil(HttpServletRequest request,HttpServletResponse response)throws Exception{
        String orderId=request.getParameter("orderId");
        List<OrderDetail> orderDetailList=orderService.getOrderDetailList(Integer.valueOf(orderId));
        request.setAttribute("orderDetailList", orderDetailList);
        request.setAttribute("menu", 1);
        return "/backend/order/orderDetailList";
    }
    
    public String queryAllOrder(HttpServletRequest request,HttpServletResponse response)throws Exception{
        //获取当前页数
        String currentPageStr = request.getParameter("currentPage");
        //获取页大小
        String pageSize = request.getParameter("pageSize");
        int rowPerPage  = EmptyUtils.isEmpty(pageSize)?5:Integer.parseInt(pageSize);
        int currentPage = EmptyUtils.isEmpty(currentPageStr)?1:Integer.parseInt(currentPageStr);
        int total=orderService.count(null);
        Pager pager=new Pager(total,rowPerPage,currentPage);
        pager.setUrl("/admin/order?action=queryAllOrder");
        List<Order> orderList=orderService.getOrderList(null, currentPage, rowPerPage);
        request.setAttribute("orderList", orderList);
        request.setAttribute("pager", pager);
        request.setAttribute("menu", 9);
        return "/backend/order/orderList";
    }

    @Override
    public Class getServletClass() {
        return AdminOrderServlet.class;
    }
}
