package cn.lsm.goods.category.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import cn.lsm.goods.category.domain.Category;
import cn.lsm.goods.category.service.CategoryService;
/**
 * 分类模块web层
 */
public class CategoryServlet extends BaseServlet {

	private CategoryService categoryService=new CategoryService();

	/**
	 * 查询所有的分类
	 */
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.通过service得到所有的分类
		 * 2.保存到request中，转发到left.jsp
		 */
		List<Category> parents=categoryService.findAll();
		req.setAttribute("parents", parents);
		return "f:/jsps/left.jsp";
	}

}
