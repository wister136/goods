package cn.lsm.goods.admin.category.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import cn.lsm.goods.car.service.CarService;
import cn.lsm.goods.category.domain.Category;
import cn.lsm.goods.category.service.CategoryService;

public class AdminCategoryServlet extends BaseServlet {

	private CategoryService categoryService = new CategoryService();
	private CarService carService = new CarService();

	/**
	 * 查询所有的分类
	 */
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("parents", categoryService.findAll());
		return "f:/adminjsps/admin/category/list.jsp";
	}

	/**
	 * 添加一级分类
	 */
	public String addParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.封装表单数据，得到Category parent 2.调用service方法完成添加
		 * 3.返回list.jsp显示所有分类，return finall
		 */
		Category parent = CommonUtils.toBean(req.getParameterMap(),
				Category.class);
		parent.setCid(CommonUtils.uuid());
		categoryService.add(parent);
		return findAll(req, resp);
	}

	/**
	 * 添加子分类
	 */
	public String addChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.封装表单数据，得到Category parent 2.需要手动的把表单中的pid映射到child对象中
		 * 2.调用service方法完成添加 3.返回list.jsp显示所有分类，return finall
		 */
		Category child = CommonUtils.toBean(req.getParameterMap(),
				Category.class);
		child.setCid(CommonUtils.uuid());

		// 手动映射pid
		String pid = req.getParameter("pid");
		Category parent = new Category();
		parent.setCid(pid);
		child.setParent(parent);

		categoryService.add(child);
		return findAll(req, resp);
	}

	/**
	 * 添加二级分类
	 */
	public String addChildPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.封装表单数据，得到Category parent 2.调用service方法完成添加
		 * 3.返回list.jsp显示所有分类，return finall
		 */
		// 获得当前点击的夫分类的id
		String pid = req.getParameter("pid");
		List<Category> parents = categoryService.findParents();
		req.setAttribute("pid", pid);
		req.setAttribute("parents", parents);

		return "f:/adminjsps/admin/category/add2.jsp";
	}
	/**
	 * 修改一级分类第一步
	 */
	public String editParentPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.获取连接中的cid 2.使用cid加载category 3.保存category 4.转发到edit.jsp页面显示category
		 */
		String cid = req.getParameter("cid");
		Category parent = categoryService.load(cid);
		req.setAttribute("parent", parent);
		return "f:/adminjsps/admin/category/edit.jsp";
	}

	/**
	 * 修改一级分类第二步
	 */
	public String editParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.封装表单数据到category中 2.调用service方法完成修改 3.转发到list.jsp显示所有分类（return
		 * findall（））
		 */
		Category parent = CommonUtils.toBean(req.getParameterMap(),
				Category.class);
		categoryService.edit(parent);
		return findAll(req, resp);
	}

	/**
	 * 修改二级分类第一步
	 */
	public String editChildPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.获取连接的cid 2.使用cid加载category 3.保存到category 4.查询出所有的一级分类，保存之
		 * 5.转发到edit2.jsp
		 */
		String cid = req.getParameter("cid");
		Category child = categoryService.load(cid);
		req.setAttribute("child", child);
		req.setAttribute("parents", categoryService.findParents());

		return "f:/adminjsps/admin/category/edit2.jsp";

	}

	/**
	 * 修改二级分类第二步
	 */
	public String editChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.封装表单参数到Category child 2.把表单中的pid封装到child。。。 3.调用service.edit()完成修改
		 * 4.返回到list.jsp
		 */
		Category child = CommonUtils.toBean(req.getParameterMap(),
				Category.class);
		String pid = req.getParameter("pid");
		Category parent = new Category();
		parent.setCid(pid);
		child.setParent(parent);
		categoryService.edit(child);
		return findAll(req, resp);
	}

	/**
	 * 删除一级分类
	 */
	public String deleteParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.获取连接的参数cid，他是一个一级分类的id 2.通过该id查找该分类下面子分类的个数
		 * 3.如果大于0说明还有子分类，不能删除，保存错误信息，转发到msg.jsp
		 * 4.如果等于0，说明已经没有子分类了，可以删除，返回到list.jsp
		 */
		String cid = req.getParameter("cid");
		int num = categoryService.findChildrenCountByParent(cid);
		if (num > 0) {
			req.setAttribute("msg", "该分类下还有子分类，不能删除");
			return "f:/adminjsps/msg.jsp";
		} else {
			categoryService.delete(cid);
			return findAll(req, resp);
		}
	}
	
	/**
	 * 删除二级分类
	 */
	public String deleteChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.获取cid,即2级分类id
		 * 2.获取该分类下面的车辆个数
		 * 3.如果该分类下个数>0,保存错误信息，转发到msg.jsp
		 * 4.如果等于0，删除，返回list.jsp
		 */
		String cid=req.getParameter("cid");
		int num=carService.findCarCountByCategory(cid);
		if (num > 0) {
			req.setAttribute("msg", "该分类下还有子分类，不能删除");
			return "f:/adminjsps/msg.jsp";
		} else {
			categoryService.delete(cid);
			return findAll(req, resp);
		}
		
		
	}
}
