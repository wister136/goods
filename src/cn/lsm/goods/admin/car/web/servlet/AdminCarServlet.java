package cn.lsm.goods.admin.car.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import cn.lsm.goods.car.service.CarService;
import cn.lsm.goods.category.domain.Category;
import cn.lsm.goods.category.service.CategoryService;

public class AdminCarServlet extends BaseServlet {
	
	private CarService carService = new CarService(); 
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
		return "f:/adminjsps/admin/book/left.jsp";
	}
	
	/**
	 * 添加图书：第一步
	 */
	public String addPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.获取所有的一级分类，保存之
		 * 2.转发到add.jsp,该页面会在下拉列表中现在所有一级分类
		 */
		List<Category> parents=categoryService.findParents();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/add.jsp";
	}
	
	public String ajaxFindChildren(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.获取pid
		 * 2.通过pid查询出所有的子分类
		 * 3.返回所有的子分类List<Category>转换成json，输出给客户端
		 */
		String pid=req.getParameter("pid");
		List<Category> children=categoryService.findByParent(pid);
		String json=toJson(children);
		System.out.println(json);
		resp.getWriter().print(json);
		
		return null;
	}
	
	private String toJson(Category category){
		StringBuilder sb=new StringBuilder("{");
		sb.append("\"cid\"").append(":").append("\"").append(category.getCid()).append("\"");
		sb.append(",");
		sb.append("\"cname\"").append(":").append("\"").append(category.getCname()).append("\"");
		sb.append("}");
		return sb.toString();
	}
	
	private String toJson(List<Category> categoryList){
		StringBuilder sb=new StringBuilder("[");
		for (int i = 0; i < categoryList.size(); i++) {
			sb.append(toJson(categoryList.get(i)));
			if(i<categoryList.size()-1){
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
}
