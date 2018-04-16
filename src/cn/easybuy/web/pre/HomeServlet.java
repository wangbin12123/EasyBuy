package cn.easybuy.web.pre;

import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easybuy.entity.News;
import cn.easybuy.entity.Product;
import cn.easybuy.param.NewsParams;
import cn.easybuy.service.news.NewsService;
import cn.easybuy.service.product.ProductCategoryService;
import cn.easybuy.service.product.ProductService;
import cn.easybuy.service.news.NewsServiceImpl;
import cn.easybuy.service.product.ProductCategoryServiceImpl;
import cn.easybuy.service.product.ProductServiceImpl;
import cn.easybuy.utils.Pager;
import cn.easybuy.utils.Params;
import cn.easybuy.utils.ProductCategoryVo;
import cn.easybuy.web.AbstractServlet;

@WebServlet(urlPatterns = {"/Home"}, name = "Home")
public class HomeServlet extends AbstractServlet {

    private ProductService productService;
    private NewsService newsService;
    private ProductCategoryService productCategoryService;

    public void init() throws ServletException {
        productService = new ProductServiceImpl();
        newsService = new NewsServiceImpl();
        productCategoryService = new ProductCategoryServiceImpl();
    }

    /**
     * 商城主页的方法
     * @param request
     * @param response
     * @return
     */
    public String index(HttpServletRequest request, HttpServletResponse response)throws Exception {
        //查询商品分裂
        List<ProductCategoryVo> productCategoryVoList = productCategoryService.queryAllProductCategoryList();
        Pager pager= new Pager(5, 5, 1);
        NewsParams params = new NewsParams();
        params.openPage((pager.getCurrentPage() - 1) * pager.getRowPerPage(),pager.getRowPerPage());
        List<News> news = newsService.queryNewsList(params);
        //查询一楼
        for (ProductCategoryVo vo : productCategoryVoList) {
            List<Product> productList = productService.getProductList(1, 8, null, vo.getProductCategory().getId(), 1);
            vo.setProductList(productList);
        }
        //封装返回
        request.setAttribute("productCategoryVoList", productCategoryVoList);
        request.setAttribute("news", news);
        return "/pre/index";
    }

    @Override
    public Class getServletClass() {
        return HomeServlet.class;
    }
}
