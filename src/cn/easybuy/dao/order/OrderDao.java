package cn.easybuy.dao.order;
import java.sql.SQLException;
import java.util.List;
import cn.easybuy.dao.IBaseDao;
import cn.easybuy.entity.News;
import cn.easybuy.entity.Order;
import cn.easybuy.param.NewsParams;
import cn.easybuy.param.OrderParams;

/***
 * 订单处理的dao层
 * getRowCount
 * getRowList(Params params)
 * getById(Integer id)
 * addObject(Params params)
 */
public interface OrderDao extends IBaseDao {

	public void add(Order order) ;

	public void deleteById(Integer id);
	
	public Order getOrderById(Integer id) ;
	
	public List<Order> getOrderList(Integer userId,Integer currentPageNo,Integer pageSize) ;
	
	public Integer count(Integer userId);
}
