package cn.easybuy.dao.order;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.easybuy.dao.BaseDaoImpl;
import cn.easybuy.dao.order.OrderDao;
import cn.easybuy.entity.News;
import cn.easybuy.entity.Order;
import cn.easybuy.param.OrderParams;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.Pager;
import cn.easybuy.utils.Params;
/**
 * 订单dao的实现类
 */
public class OrderDaoImpl extends BaseDaoImpl implements OrderDao {

	public OrderDaoImpl(Connection connection) {
		super(connection);
	}

	@Override
	public Order tableToClass(ResultSet rs) throws Exception {
		Order order = new Order();
		order.setId(rs.getInt("id"));
		order.setUserId(rs.getInt("userId"));
		order.setCreateTime(rs.getDate("createTime"));
		order.setCost(rs.getFloat("cost"));
		order.setUserAddress(rs.getString("userAddress"));
		order.setSerialNumber(rs.getString("serialNumber"));
		order.setLoginName(rs.getString("loginName"));
		return order;
	}
	/**
	 * 保存订单
	 * @param order
	 * @throws java.sql.SQLException
	 */
	public void add(Order order) {//保存订单
		Integer id=0;
		String sql="insert into easybuy_order(userId,loginName,userAddress,createTime,cost,serialNumber) values(?,?,?,?,?,?) ";
		Object[] param=new Object[]{order.getUserId(),order.getLoginName(),order.getUserAddress(),new Date(),order.getCost(),order.getSerialNumber()};
		try {
			id=this.executeInsert(sql, param);
			order.setId(new Integer(id).intValue());
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.closeResource();
		}
	}

	@Override
	public void deleteById(Integer id) {
		String sql = " delete from easybuy_order where id = ? ";
		Object params[] = new Object[] { id };
		try {
			this.executeUpdate(sql.toString(), params);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.closeResource();
		}
	}

	@Override
	public Order getOrderById(Integer id) {
		String sql = " select * from easybuy_order where id = ? ";
		ResultSet resultSet = null;
		Order order = null;
		try {
			Object params[] = new Object[] { id };
			resultSet = this.executeQuery(sql, params);
			while (resultSet.next()) {
				order = tableToClass(resultSet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeResource(resultSet);
			this.closeResource();
			return order;
		}
	}

	@Override
	public List<Order> getOrderList(Integer userId,Integer currentPageNo,Integer pageSize) {
		List<Object> paramsList=new ArrayList<Object>();   
		List<Order> orderList=new ArrayList<Order>();
		StringBuffer sql=new StringBuffer(" select id,userId,loginName,userAddress,createTime,cost,serialNumber from easybuy_order  where 1=1 ");
		if(EmptyUtils.isNotEmpty(userId)){
			sql.append(" and userId = ? ");
			paramsList.add(userId);
		}
		int total = count(userId);
		Pager pager = new Pager(total, pageSize, currentPageNo);
		sql.append(" limit  " + (pager.getCurrentPage() - 1) * pager.getRowPerPage() + "," + pager.getRowPerPage());
		ResultSet resultSet = this.executeQuery(sql.toString(),paramsList.toArray());
		try {
			while (resultSet.next()) {
				Order order = this.tableToClass(resultSet);
				orderList.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.closeResource();
			this.closeResource(resultSet);
		}
				
		return orderList;
	}

	@Override
	public Integer count(Integer userId)  {
		List<Object> paramsList=new ArrayList<Object>();   
		Integer count=0;
		StringBuffer sql=new StringBuffer(" select count(id) count from easybuy_order  where 1=1 ");
		if(EmptyUtils.isNotEmpty(userId)){
			sql.append(" and userId = ? ");
			paramsList.add(userId);
		}
		ResultSet resultSet = this.executeQuery(sql.toString(),paramsList.toArray());
		try {
			while (resultSet.next()) {
				count = resultSet.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.closeResource();
			this.closeResource(resultSet);
		}
		return count;
	}
}
