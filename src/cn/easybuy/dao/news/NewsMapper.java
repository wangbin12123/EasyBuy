package cn.easybuy.dao.news;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.easybuy.entity.News;
import cn.easybuy.entity.Product;

public interface NewsMapper{
	public Integer add(News news);
	public Integer deleteById(Integer id);
	public Integer update(News news);
	public News getNewsById(Integer id);
	public List<Product> queryNewsList(@Param("sort")Integer sort,
										@Param("startIndex")Integer startIndex,
										@Param("pageSize")Integer pageSize,
										@Param("title")String title);
	public Integer queryNewsCount(@Param("title")String title);
}
