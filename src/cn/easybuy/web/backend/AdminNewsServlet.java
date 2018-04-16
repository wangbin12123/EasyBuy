package cn.easybuy.web.backend;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easybuy.entity.News;
import cn.easybuy.param.NewsParams;
import cn.easybuy.service.news.NewsService;
import cn.easybuy.service.product.ProductService;
import cn.easybuy.service.news.NewsServiceImpl;
import cn.easybuy.service.product.ProductServiceImpl;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.Pager;
import cn.easybuy.web.AbstractServlet;

import java.util.List;

@WebServlet(urlPatterns = {"/admin/news"},name = "adminNews")
public class AdminNewsServlet extends AbstractServlet {

	private NewsService newsService;
	private ProductService productService;

	public void init() throws ServletException {
		this.newsService = new NewsServiceImpl();
		this.productService = new ProductServiceImpl();
	}

	@Override
	public Class getServletClass() {
		return AdminNewsServlet.class;
	}

	/**
	 * 查询新闻列表
	 * @param request
	 * @param response
	 * @return
	 */
	public String queryNewsList(HttpServletRequest request, HttpServletResponse response)throws Exception{
		//获取当前页数
		String currentPageStr = request.getParameter("currentPage");
		//获取页大小
		String pageSize = request.getParameter("pageSize");
		int rowPerPage = EmptyUtils.isEmpty(pageSize)?10:Integer.parseInt(pageSize);
		int currentPage = EmptyUtils.isEmpty(currentPageStr)?1:Integer.parseInt(currentPageStr);
		int total=newsService.queryNewsCount(new NewsParams());
		Pager pager=new Pager(total,rowPerPage,currentPage);
		pager.setUrl("/admin/news?action=queryNewsList");
		NewsParams params = new NewsParams();
        params.openPage((pager.getCurrentPage() - 1) * pager.getRowPerPage(),pager.getRowPerPage());
		List<News> newsList = newsService.queryNewsList(params);
		request.setAttribute("newsList", newsList);
		request.setAttribute("pager", pager);
		request.setAttribute("menu", 7);
		return "/backend/news/newsList";
	}
	/**
	 * 查询新闻详情
	 * @param request
	 * @param response
	 * @return
	 */
	public String newsDeatil(HttpServletRequest request,HttpServletResponse response)throws Exception{
		String id = request.getParameter("id");
		News news=newsService.findNewsById(id);
		request.setAttribute("news",news);
		request.setAttribute("menu", 7);
		return "/backend/news/newsDetail";
	}

}
