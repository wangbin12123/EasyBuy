package cn.easybuy.dao.product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.easybuy.dao.BaseDaoImpl;
import cn.easybuy.dao.product.ProductDao;
import cn.easybuy.entity.News;
import cn.easybuy.entity.Order;
import cn.easybuy.entity.OrderDetail;
import cn.easybuy.entity.Product;
import cn.easybuy.param.ProductParam;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.Pager;
import cn.easybuy.utils.Params;

public class ProductDaoImpl extends BaseDaoImpl implements ProductDao {

    public ProductDaoImpl(Connection connection) {
        super(connection);
    }

    /**
     * 字段 和 列名 的对应
     *
     * @param rs
     * @return
     * @throws Exception
     */
    @Override
    public Product tableToClass(ResultSet rs) throws Exception {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getFloat("price"));
        product.setStock(rs.getInt("stock"));
        product.setCategoryLevel1Id(rs.getInt("categoryLevel1Id"));
        product.setCategoryLevel2Id(rs.getInt("categoryLevel2Id"));
        product.setCategoryLevel3Id(rs.getInt("categoryLevel3Id"));
        product.setFileName(rs.getString("fileName"));
        return product;
    }

    public Integer updateStock(Integer id, Integer quantity) {
       Integer count=0;
        try {
        	Object[] params = new Object[] {quantity,id};
        	String sql = " update easybuy_product set stock=? where id=? ";
			count=this.executeUpdate(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
			this.closeResource();
			return count;
		}
    }

	@Override
	public Integer add(Product product) {
		Integer id=0;
		String sql=" insert into easybuy_product(name,description,price,stock,categoryLevel1Id,categoryLevel2Id,categoryLevel3Id,fileName,isDelete) values(?,?,?,?,?,?,?,?,?) ";
        try {
            Object param[]=new Object[]{product.getName(),product.getDescription(),product.getPrice(),product.getStock(),product.getCategoryLevel1Id(),product.getCategoryLevel2Id(),product.getCategoryLevel3Id(),product.getFileName(),0};
            id=this.executeInsert(sql,param);
            product.setId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
			this.closeResource();
			return id;
		}
	}

	@Override
	public Integer update(Product product) {
		Integer count=0;
		try {
        	Object[] params = new Object[] {product.getName(),product.getFileName(),product.getCategoryLevel1Id(),product.getCategoryLevel2Id(),product.getCategoryLevel3Id(),product.getId()};
        	String sql = " update easybuy_product set name=?,fileName=?,categoryLevel1Id=?,categoryLevel3Id=?,categoryLevel3Id=? where id=? ";
			count=this.executeUpdate(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
			this.closeResource();
			return count;
		}
	}

	@Override
	public Integer deleteProductById(Integer id) throws Exception {
		String sql = " delete from easybuy_product where id = ? ";
		Object params[] = new Object[] { id };
		Integer count=0;
		try{
			count=this.executeUpdate(sql.toString(), params);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.closeResource();
			return count;
		}
	}

	@Override
	public Product getProductById(Integer id) throws Exception {
		String sql = " select id,name,description,price,stock,categoryLevel1Id,categoryLevel2Id,categoryLevel3Id,fileName,isDelete from easybuy_product where id = ? ";
		ResultSet resultSet = null;
		Product product = null;
		try {
			Object params[] = new Object[] { id };
			resultSet = this.executeQuery(sql, params);
			while (resultSet.next()) {
				product = tableToClass(resultSet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeResource(resultSet);
			this.closeResource();
			return product;
		}
	}

	@Override
	public List<Product> getProductList(Integer currentPageNo,Integer pageSize,String proName,Integer categoryId,Integer level) throws Exception {
		List<Object> paramsList=new ArrayList<Object>();   
		List<Product> productList=new ArrayList<Product>();
		StringBuffer sql=new StringBuffer("  select id,name,description,price,stock,categoryLevel1Id,categoryLevel2Id,categoryLevel3Id,fileName,isDelete from easybuy_product  where 1=1 ");
		ResultSet resultSet = null;
		try {
			if(EmptyUtils.isNotEmpty(proName)){
				sql.append(" and name like ? ");
				paramsList.add("%"+proName+"%");
			}

			if(EmptyUtils.isNotEmpty(categoryId)){
				sql.append(" and (categoryLevel1Id = ? or categoryLevel2Id=? or categoryLevel3Id=? ) ");
				paramsList.add(categoryId);
				paramsList.add(categoryId);
				paramsList.add(categoryId);
			}
			int total = queryProductCount(proName,categoryId,level);
			Pager pager = new Pager(total, pageSize, currentPageNo);
			sql.append(" limit  " + (pager.getCurrentPage() - 1) * pager.getRowPerPage() + "," + pager.getRowPerPage());
			resultSet=this.executeQuery(sql.toString(),paramsList.toArray());
			while (resultSet.next()) {
				Product product = this.tableToClass(resultSet);
				productList.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.closeResource(resultSet);
			this.closeResource();
		}
				
		return productList;
	}

	
	@Override
	public Integer queryProductCount(String proName,Integer categoryId,Integer level) throws Exception {
		List<Object> paramsList=new ArrayList<Object>();   
		Integer count=0;
		StringBuffer sql=new StringBuffer("  select count(*) count from easybuy_product where 1=1 ");
		if(EmptyUtils.isNotEmpty(proName)){
			sql.append(" and name like ? ");
			paramsList.add("%"+proName+"%");
		}

		if(EmptyUtils.isNotEmpty(categoryId)){
			sql.append(" and (categoryLevel1Id = ? or categoryLevel2Id=? or categoryLevel3Id=? ) ");
			paramsList.add(categoryId);
			paramsList.add(categoryId);
			paramsList.add(categoryId);
		}
		ResultSet resultSet = this.executeQuery(sql.toString(),paramsList.toArray());
		try {
			while (resultSet.next()) {
				count=resultSet.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.closeResource(resultSet);
			this.closeResource();
		}
		return count;
	}
	
	
}
