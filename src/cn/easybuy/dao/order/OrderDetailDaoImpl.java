package cn.easybuy.dao.order;
import cn.easybuy.dao.BaseDaoImpl;
import cn.easybuy.dao.product.ProductDaoImpl;
import cn.easybuy.dao.product.ProductDao;
import cn.easybuy.entity.News;
import cn.easybuy.entity.Order;
import cn.easybuy.entity.OrderDetail;
import cn.easybuy.entity.Product;
import cn.easybuy.param.OrderDetailParam;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.Params;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bdqn on 2016/5/8.
 */
public class OrderDetailDaoImpl extends BaseDaoImpl implements OrderDetailDao{

    ProductDao productDao;

    public OrderDetailDaoImpl(Connection connection) {
        super(connection);
        productDao=new ProductDaoImpl(connection);
    }
    

    @Override
    public OrderDetail tableToClass(ResultSet rs) throws Exception {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(rs.getInt("id"));
        orderDetail.setOrderId(rs.getInt("orderId"));
        orderDetail.setProduct((Product) productDao.getProductById(rs.getInt("productId")));
        orderDetail.setProductId(rs.getInt("productId"));
        orderDetail.setQuantity(rs.getInt("quantity"));
        orderDetail.setCost(rs.getFloat("cost"));
        return orderDetail;
    }

    public void add(OrderDetail detail) throws SQLException {//保存订单详情
        Integer id=0;
		String sql=" insert into easybuy_order_detail(orderId,productId,quantity,cost) values(?,?,?,?) ";
        try {
            Object param[]=new Object[]{detail.getOrderId(),detail.getProduct().getId(),detail.getQuantity(),detail.getCost()};
            id=this.executeInsert(sql,param);
            detail.setId(id);
        } catch (Exception e) {
			this.closeResource();
            e.printStackTrace();
        }
    }

	@Override
	public void deleteById(OrderDetail detail) throws Exception {
		String sql = " delete from easybuy_order_detail where id = ? ";
		Object params[] = new Object[] { detail.getId() };
		try {
		this.executeUpdate(sql.toString(), params);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.closeResource();
		}
	}

	@Override
	public OrderDetail getOrderDetailById(Integer id) throws Exception {
		String sql = " select orderId,productId,quantity,cost from easybuy_order_detail where id = ? ";
		ResultSet resultSet = null;
		OrderDetail orderDetail = null;
		try {
			Object params[] = new Object[] { id };
			resultSet = this.executeQuery(sql, params);
			while (resultSet.next()) {
				orderDetail = tableToClass(resultSet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeResource(resultSet);
			this.closeResource();
			return orderDetail;
		}
	}

	@Override
	public List<OrderDetail> getOrderDetailList(Integer orderId)
			throws Exception {
		List<OrderDetail> orderDetailList = null;
		List<Object> paramsList=new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" select id,orderId,productId,quantity,cost FROM easybuy_order_detail where 1=1 ");
		
		if(EmptyUtils.isNotEmpty(orderId)){
			sql.append(" and orderId=? ");
			paramsList.add(orderId);
		}
		ResultSet resultSet = this.executeQuery(sql.toString(), paramsList.toArray());
		try {
			orderDetailList=new ArrayList<OrderDetail>();
			while (resultSet.next()) {
				OrderDetail orderDetail = this.tableToClass(resultSet);
				orderDetailList.add(orderDetail);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.closeResource(resultSet);
			this.closeResource();
			return orderDetailList;
		}
	}

	@Override
	public Integer queryOrderDetailCount(OrderDetailParam params)throws Exception {
		Integer count = 0;
		List<OrderDetail> orderDetailList = null;
		String sql = " select count(*) FROM easybuy_order_detail ";
		ResultSet resultSet = this.executeQuery(sql, new Object[] {});
		try {
			orderDetailList=new ArrayList<OrderDetail>();
			while (resultSet.next()) {
				count = resultSet.getInt("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.closeResource(resultSet);
			this.closeResource();
			return count;
		}
	}
}
