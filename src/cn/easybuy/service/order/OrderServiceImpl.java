package cn.easybuy.service.order;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import org.apache.ibatis.session.SqlSession;

import cn.easybuy.dao.order.*;
import cn.easybuy.dao.product.ProductDao;
import cn.easybuy.dao.product.ProductDaoImpl;
import cn.easybuy.dao.product.ProductMapper;
import cn.easybuy.utils.*;
import cn.easybuy.entity.Order;
import cn.easybuy.entity.OrderDetail;
import cn.easybuy.entity.User;

public class OrderServiceImpl implements OrderService {

    /**
     * 结算购物车物品包含以下步骤：
     * 1.生成订单
     * 2.生成订单明细
     * 3.更新商品表，减库存
     * 注意加入事物的控制
     */

    @Override
    public Order payShoppingCart(ShoppingCart cart, User user, String address) {
        // TODO Auto-generated method stub
        Connection connection = null;
        Order order = new Order();
        try {
            connection = DataSourceUtil.openConnection();
            connection.setAutoCommit(false);
            ProductDao productDao = new ProductDaoImpl(connection);
            OrderDao orderDao = new OrderDaoImpl(connection);
            OrderDetailDao orderDetailDao = new OrderDetailDaoImpl(connection);
            //增加订单
            order.setUserId(user.getId());
            order.setLoginName(user.getLoginName());
            order.setUserAddress(address);
            order.setCreateTime(new Date());
            order.setCost(cart.getTotalCost());
            order.setSerialNumber(StringUtils.randomUUID());
            orderDao.add(order);
            System.out.println(order.getId());
            //增加订单对应的明细信息
            for (ShoppingCartItem item : cart.getItems()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(order.getId());
                orderDetail.setCost(item.getCost());
                orderDetail.setProduct(item.getProduct());
                orderDetail.setQuantity(item.getQuantity());
                orderDetailDao.add(orderDetail);
                //更新商品表的库存
                productDao.updateStock(item.getProduct().getId(), item.getQuantity());
                connection.commit();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            order = null;
        } finally {
            try {
                connection.setAutoCommit(true);
                DataSourceUtil.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return order;
    }

    @Override
    public List<Order> getOrderList(Integer userId, Integer currentPageNo, Integer pageSize) {
        SqlSession session = null;
        List<Order> orderList = new ArrayList<Order>();
        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        try {
            session = MyBatisUtil.createSession();
			orderList=session.getMapper(OrderMapper.class).getOrderList(userId,(currentPageNo-1)*pageSize,pageSize);
			for (Order order : orderList) {
				order.setOrderDetailList(session.getMapper(OrderDetailMapper.class).getOrderDetailList(order.getId()));
			}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyBatisUtil.closeSession(session);
            session.rollback();
            return orderList;
        }
    }

    @Override
    public int count(Integer userId) {
    	SqlSession session=null;
        Integer count=0;
        try {
            session=MyBatisUtil.createSession();
            count=session.getMapper(OrderMapper.class).count(userId);
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
        	 MyBatisUtil.closeSession(session);
        }
        return count;
    }

    /**
     * 调用dao接口：OrderDetailMapper的方法实现
     */
    @Override
    public List<OrderDetail> getOrderDetailList(Integer orderId) {
    	SqlSession session=null;
        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        try {
        	 session=MyBatisUtil.createSession();
            orderDetailList = session.getMapper(OrderDetailMapper.class).getOrderDetailList(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	MyBatisUtil.closeSession(session);
        	session.rollback();
            return orderDetailList;
        }
    }
}
