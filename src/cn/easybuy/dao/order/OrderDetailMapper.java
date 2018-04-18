package cn.easybuy.dao.order;

import java.util.List;

import cn.easybuy.entity.OrderDetail;
import cn.easybuy.param.OrderDetailParam;

public interface OrderDetailMapper {
	public void add(OrderDetail detail);
	public void deleteById(OrderDetail detail);
	public OrderDetail getOrderDetailById(Integer id);
	public List<OrderDetail> getOrderDetailList(Integer orderId);
	public Integer queryOrderDetailCount(OrderDetailParam params);
}
