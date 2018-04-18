package cn.easybuy.dao.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.easybuy.entity.Order;

public interface OrderMapper {
		public void add(Order order);
		public void delete(Integer id); 
		public Order getOrderById(Integer id);
		public List<Order> getOrderList(@Param("userId")Integer userId,@Param("currentPageNo")Integer currentPageNo,@Param("pageSize")Integer pageSize);
		public Integer count(Integer userId);
}
