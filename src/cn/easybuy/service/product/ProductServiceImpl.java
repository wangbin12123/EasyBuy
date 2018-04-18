package cn.easybuy.service.product;
import java.sql.Connection;
import java.util.List;
import cn.easybuy.dao.product.ProductDao;
import cn.easybuy.dao.product.ProductDaoImpl;
import cn.easybuy.dao.product.ProductMapper;
import cn.easybuy.utils.DataSourceUtil;
import cn.easybuy.utils.MyBatisUtil;
import cn.easybuy.utils.Pager;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import cn.easybuy.entity.Product;
/**
 * 商品的业务类
 */
public class ProductServiceImpl implements ProductService {
	
	private Logger logger = Logger.getLogger(ProductServiceImpl.class);
	
	@Override
	public boolean add(Product product) {
		Integer count=0;
		SqlSession session = null;
		try {
			session = MyBatisUtil.createSession();
			count = session.delete("cn.easybuy.dao.product.ProductMapper.add",product);
			//count = session.getMapper(ProductMapper.class).add(product);
			session.commit();
		}catch(Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally {
			MyBatisUtil.closeSession(session);
			return count>0;
		}
	}

	@Override
	public boolean update(Product product) {
		Integer count=0;
		SqlSession session = null;
		try {
			session = MyBatisUtil.createSession();
			count=session.getMapper(ProductMapper.class).update(product);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MyBatisUtil.closeSession(session);
			session.rollback();
			return count>0;
		}
	}

	@Override
	public boolean deleteProductById(Integer productId) {
		Integer count=0;
		SqlSession session = null;
		try {
			session = MyBatisUtil.createSession();
			count = session.delete("cn.easybuy.dao.product.ProductMapper.deleteProductById",productId);
			//count=session.getMapper(ProductMapper.class).deleteProductById(productId);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MyBatisUtil.closeSession(session);
			session.rollback();
			return count>0;
		}
	}

	@Override
	public Product getProductById(Integer productId) {
		SqlSession session = null;
		Product product=null;
		try {
			session = MyBatisUtil.createSession();
			product=session.getMapper(ProductMapper.class).getProductById(productId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MyBatisUtil.closeSession(session);
			session.rollback();
			return product;
		}
	}

	@Override
	public List<Product> getProductList(Integer currentPageNo,Integer pageSize,String proName, Integer categoryId, Integer level) {
		SqlSession session=null;
		List<Product> productList=null;
		try {
			session=MyBatisUtil.createSession();
			int total = count(proName,categoryId,level);
			Pager pager = new Pager(total, pageSize, currentPageNo);
			productList=session.getMapper(ProductMapper.class).getProductList((pager.getCurrentPage() - 1) * pager.getRowPerPage(), 
					pager.getRowPerPage(), proName, categoryId, level);		
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			MyBatisUtil.closeSession(session);
			session.rollback();
		}
		return productList;
	}

	@Override
	public int count(String proName,Integer categoryId, Integer level) {
		SqlSession session=null;
		int count=0;
		try {
			session=MyBatisUtil.createSession();
			count=session.getMapper(ProductMapper.class).queryProductCount(proName, categoryId, level);		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MyBatisUtil.closeSession(session);
			session.rollback();
		}
		return count;
	}

	@Override
	public boolean updateStock(Integer productId, Integer stock) {
		SqlSession session=null;
		Integer count=0;
		try {
			session=MyBatisUtil.createSession();
			count=session.getMapper(ProductMapper.class).updateStock(productId, stock);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MyBatisUtil.closeSession(session);
			session.rollback();
			return count>0;
		}
	}
   
}
