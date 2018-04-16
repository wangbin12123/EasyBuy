package cn.easybuy.dao.news;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import cn.easybuy.dao.BaseDaoImpl;
import cn.easybuy.entity.News;
import cn.easybuy.param.NewsParams;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.Pager;

public class NewsDaoImpl extends BaseDaoImpl implements NewsDao {

	public NewsDaoImpl(Connection connection) {
		super(connection);
	}

	@Override
	public News tableToClass(ResultSet rs) throws Exception {
		News news = new News();
		news.setId(rs.getInt("id"));
		news.setTitle(rs.getString("title"));
		news.setContent(rs.getString("content"));
		news.setCreateTime(rs.getDate("createTime"));
		return news;
	}

	public void add(News news) throws Exception {// 保存新闻
		String sql = " INSERT into easybuy_news(title,content,createTime) values( ?, ?, ?) ";
		Object[] params = new Object[] { news.getTitle(), news.getContent(),
				new Date() };
		this.executeUpdate(sql, params);
	}

	public void update(News news) throws Exception {// 更新新闻
		String sql = " update easybuy_news set title=?,content=? where id=? ";
		Object[] params = new Object[] {news.getTitle(), news.getContent(),news.getId() };
		this.executeUpdate(sql, params);
	}

	public void deleteById(Integer id) throws Exception {
		String sql = " delete from easybuy_news where id = ? ";
		Object params[] = new Object[] { id };
		this.executeUpdate(sql.toString(), params);
	}

	public News getNewsById(Integer id) {
		String sql = " select * from easybuy_news where id = ? ";
		ResultSet resultSet = null;
		News news = null;
		try {
			Object params[] = new Object[] { id };
			resultSet = this.executeQuery(sql, params);
			while (resultSet.next()) {
				news = tableToClass(resultSet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeResource(resultSet);
			this.closeResource();
			return news;
		}
	}

	@Override
	public List<News> queryNewsList(NewsParams params) {
		List<Object> paramsList=new ArrayList<Object>();   
		List<News> newsList=new ArrayList<News>();
		StringBuffer sql=new StringBuffer(" select id,title,content,createTime FROM easybuy_news where 1=1 ");
		if(EmptyUtils.isNotEmpty(params.getTitle())){
			sql.append(" and title like ? ");
			paramsList.add(params.getTitle());
		}
		if(EmptyUtils.isNotEmpty(params.getSort())){
			sql.append(" order by " + params.getSort()+" ");
		}
		if(params.isPage()){
			sql.append(" limit  " + params.getStartIndex() + "," + params.getPageSize());
		}
		
		ResultSet resultSet = this.executeQuery(sql.toString(),paramsList.toArray());
		try {
			while (resultSet.next()) {
				News news = this.tableToClass(resultSet);
				newsList.add(news);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.closeResource(resultSet);
			this.closeResource();
		}
				
		return newsList;
	}
	
	@Override
	public Integer queryNewsCount(NewsParams params) {
		List<Object> paramsList=new ArrayList<Object>();   
		Integer count = 0;
		StringBuffer sql=new StringBuffer(" select count(*) as count FROM easybuy_news where 1=1 ");
		if(EmptyUtils.isNotEmpty(params.getTitle())){
			sql.append(" and title like ? ");
			paramsList.add(params.getTitle());
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
			this.closeResource(resultSet);
			this.closeResource();
		}
		return count;
	}

	
}
