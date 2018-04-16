package cn.easybuy.dao.product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.easybuy.dao.BaseDaoImpl;
import cn.easybuy.dao.product.ProductCategoryDao;
import cn.easybuy.entity.Product;
import cn.easybuy.entity.ProductCategory;
import cn.easybuy.entity.User;
import cn.easybuy.param.ProductCategoryParam;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.Params;

public class ProductCategoryDaoImpl extends BaseDaoImpl implements ProductCategoryDao {

	public ProductCategoryDaoImpl(Connection connection) {
		super(connection);
	}

	@Override
	public ProductCategory tableToClass(ResultSet rs) throws Exception {
		ProductCategory productCategory = new ProductCategory();
		productCategory.setId(rs.getInt("id"));
		productCategory.setName(rs.getString("name"));
		productCategory.setParentId(rs.getInt("parentId"));
		productCategory.setType(rs.getInt("type"));
		productCategory.setIconClass(rs.getString("iconClass"));
		return productCategory;
	}
	
	public ProductCategory mapToClass(Map map) throws Exception {
		ProductCategory productCategory = new ProductCategory();
		Object idObject=map.get("id");
		Object nameObject=map.get("name");
		Object parentIdObject=map.get("parentId");
		Object typeObject=map.get("type");
		Object iconClassObject=map.get("iconClass");
		Object parentNameObject=map.get("parentName");
		productCategory.setId(EmptyUtils.isEmpty(idObject)?null:(Integer)idObject);
		productCategory.setName(EmptyUtils.isEmpty(nameObject)?null:(String)nameObject);
		productCategory.setParentId(EmptyUtils.isEmpty(parentIdObject)?null:(Integer)parentIdObject);
		productCategory.setType(EmptyUtils.isEmpty(typeObject)?null:(Integer)typeObject);
		productCategory.setIconClass(EmptyUtils.isEmpty(iconClassObject)?null:(String)iconClassObject);
		productCategory.setParentName(EmptyUtils.isEmpty(parentNameObject)?null:(String)parentNameObject);
		return productCategory;
	}
	
	public List<ProductCategory> queryProductCategorylist(ProductCategoryParam params){
		List<ProductCategory> list=new ArrayList<ProductCategory>();
		List<Object> paramsList=new ArrayList<Object>();
		StringBuffer sqlBuffer=new StringBuffer("SELECT epc1.*,epc2.name as parentName FROM easybuy_product_category epc1 LEFT JOIN easybuy_product_category epc2 ON epc1.parentId=epc2.id where 1=1 ");
		ResultSet resultSet=null;
		try{
			if(EmptyUtils.isNotEmpty(params.getName())){
				sqlBuffer.append(" and epc1.name like ? ");
				paramsList.add("%"+params.getName()+"%");
			}
			if(EmptyUtils.isNotEmpty(params.getParentId())){
				sqlBuffer.append(" and epc1.parentId = ? ");
				paramsList.add(params.getParentId());
			}
			if(EmptyUtils.isNotEmpty(params.getType())){
				sqlBuffer.append(" and epc1.type = ? ");
				paramsList.add(params.getType());
			}
			if(params.isPage()){
				sqlBuffer.append(" limit  " + params.getStartIndex() + "," + params.getPageSize());
			}
			resultSet=this.executeQuery(sqlBuffer.toString(),paramsList.toArray());
			while (resultSet.next()) {
				ProductCategory productCategory = this.tableToClass(resultSet);
				productCategory.setParentName(resultSet.getString("parentName"));
				list.add(productCategory);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.closeResource();
			this.closeResource(resultSet);
		}

		
		return list;
	}

	@Override
	public void deleteById(Integer id){
		String sql = " delete from easybuy_product_category where id = ? ";
		Object params[] = new Object[] { id };
		this.executeUpdate(sql.toString(), params);	
	}

	public Integer queryProductCategoryCount(ProductCategoryParam params){
		List<Object> paramsList=new ArrayList<Object>();   
		Integer count=0;
		StringBuffer sql=new StringBuffer("SELECT count(*) count FROM easybuy_product_category where 1=1 ");
		if(EmptyUtils.isNotEmpty(params.getName())){
			sql.append(" and name like ? ");
			paramsList.add("%"+params.getName()+"%");
		}
		if(EmptyUtils.isNotEmpty(params.getParentId())){
			sql.append(" and parentId = ? ");
			paramsList.add(params.getParentId());
		}
		ResultSet resultSet=this.executeQuery(sql.toString(), paramsList.toArray());
		try {
			while (resultSet.next()) {
				count=resultSet.getInt("count");
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
	
	public ProductCategory queryProductCategoryById(Integer id){
		List<Object> paramsList=new ArrayList<Object>();   
		ProductCategory productCategory=null;
		StringBuffer sql=new StringBuffer("SELECT id,name,parentId,type,iconClass  FROM easybuy_product_category where id = ? ");
		ResultSet resultSet=this.executeQuery(sql.toString(),new Object[]{id});
		try {
			while (resultSet.next()) {
				productCategory = this.tableToClass(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.closeResource();
			this.closeResource(resultSet);
		}
		return productCategory;
	}
	
	public Integer add(ProductCategory productCategory)  {//新增用户信息
    	Integer id=0;
    	try {
    		String sql=" INSERT into easybuy_product_category(name,parentId,type,iconClass) values(?,?,?,?) ";
            Object param[]=new Object[]{productCategory.getName(),productCategory.getParentId(),productCategory.getType(),productCategory.getIconClass()};
            id=this.executeInsert(sql,param);
            productCategory.setId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	this.closeResource();
        }
    	return id;
    }

	@Override
	public void update(ProductCategory productCategory) {
		try {
        	Object[] params = new Object[] {productCategory.getName(),productCategory.getParentId(),productCategory.getType(),productCategory.getIconClass(),productCategory.getId()};
        	String sql = " UPDATE easybuy_product_category SET name=?,parentId=?,type=?,iconClass=? WHERE id =?  ";
    		this.executeUpdate(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	this.closeResource();
        }		
	}
	
	
}
