package cn.easybuy.dao.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.easybuy.entity.Product;

public interface ProductMapper {
	public Integer add(Product product);
	public Integer deleteProductById(Integer id);
	public Integer update(Product product);
	public Product getProductById(Integer id);
	public List<Product> getProductList(@Param("currentPageNo")Integer currentPageNo,
										@Param("pageSize")Integer pageSize,
										@Param("name")String name,
										@Param("categoryId")Integer categoryId,
										@Param("level")Integer level);
	public Integer queryProductCount(@Param("name")String name,
									 @Param("categoryId")Integer categoryId,
									 @Param("level")Integer level);
	public Integer updateStock(@Param("productId") Integer productId,@Param("stock") Integer stock);
}
