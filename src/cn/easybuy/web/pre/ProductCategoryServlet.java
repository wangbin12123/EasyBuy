package cn.easybuy.web.pre;
import cn.easybuy.service.product.ProductService;
import cn.easybuy.service.product.ProductServiceImpl;
import cn.easybuy.web.AbstractServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 * Created by bdqn on 2016/4/21.
 */
@WebServlet(urlPatterns = {"/productCategory"},name = "productCategory")
public class ProductCategoryServlet extends AbstractServlet{

    private ProductService productService;

    public void init() throws ServletException {
        productService = new ProductServiceImpl();
    }

    @Override
    public Class getServletClass() {
        return ProductCategoryServlet.class;
    }
}
