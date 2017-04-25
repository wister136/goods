package cn.lsm.goods.category.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import cn.lsm.goods.category.domain.Category;

/**
 * 分类持久层
 * 
 * @author Administrator
 * 
 */
public class CategoryDao {
	private QueryRunner qr = new TxQueryRunner();

	/*
	 * 把一个map中的数据映射到category中
	 */
	private Category toCategory(Map<String, Object> map) {
		/*
		 * map{cid:xx,cname:xx,pid:xx.desc:xx.orderBy:xx}
		 * Category{cid:xx,cname:xx,parent:(cid=pid),desc:xx}
		 */
		Category category = CommonUtils.toBean(map, Category.class);
		String pid = (String) map.get("pid");// 如果是一级分类，那么pid是null
		if (pid != null) {// 如果父分类id不为空
			/*
			 * 使用一个父分类对象来拦截 再把父分类设置给category
			 */
			Category parent = new Category();
			parent.setCid(pid);
			category.setParent(parent);
		}
		return category;

	}

	/**
	 * 把多个map映射成多了Category(List<Category>)
	 * 
	 * @param mapList
	 * @return
	 */
	public List<Category> toCategoryList(List<Map<String, Object>> mapList) {
		List<Category> categoryList = new ArrayList<Category>();
		for (Map<String, Object> map : mapList) {
			Category c = toCategory(map);
			categoryList.add(c);
		}
		return categoryList;
	}

	/**
	 * 返回所有分类
	 * 
	 * @throws SQLException
	 */
	public List<Category> findAll() throws SQLException {
		/*
		 * 1.查询出所有的一级分类
		 */
		String sql = "select * from t_category where pid is null order by orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
		List<Category> parents = toCategoryList(mapList);

		/*
		 * 2.循环所有的一级分类，为每一个一级分类加载他的二级分类
		 */
		for (Category parent : parents) {
			// 查询出当前是有多父分类的子分类
			List<Category> children = findByParent(parent.getCid());
			// 设置给父分类
			parent.setChildren(children);
		}
		return parents;
	}

	/**
	 * 通过父分类查询子分类
	 * 
	 * @throws SQLException
	 */
	public List<Category> findByParent(String pid) throws SQLException {

		String sql = "select * from t_category where pid=?";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(),
				pid);
		return toCategoryList(mapList);
	}

	/**
	 * 以下是在后台编辑一级和二级目录的方法
	 */

	/**
	 * 添加一级分类和二级分类
	 * 
	 * @throws SQLException
	 */
	public void addParent(Category category) throws SQLException {
		String sql = "insert into t_category(cid,cname,pid,`desc`) values(?,?,?,?)";
		/*
		 * 因为一级分类没有parent，二级分类有 这个方法，需要兼容两个分类，所以需要判断
		 */
		String pid = null;// 一级分类
		if (category.getParent() != null) {
			// 获取你的值，给出pid
			pid = category.getParent().getCid();
		}
		Object[] params = { category.getCid(), category.getCname(), pid,
				category.getDesc() };
		qr.update(sql, params);

	}

	/**
	 * 获取所有夫分类，但不带子分类的！
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<Category> findParents() throws SQLException {
		/*
		 * 1.查询出所有的一级分类
		 */
		String sql = "select * from t_category where pid is null order by orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
		return toCategoryList(mapList);
	}

	/**
	 * 加载分类 两级分类都可以加载
	 * 
	 * @throws SQLException
	 */
	public Category load(String cid) throws SQLException {
		String sql = "select * from t_category where cid=?";
		return toCategory(qr.query(sql, new MapHandler(), cid));

	}

	/**
	 * 修改分类 即可修改一级分类，又可修改二级分类
	 * 
	 * @param category
	 * @throws SQLException
	 */
	public void edit(Category category) throws SQLException {
		String sql = "update t_category set cname=?,pid=?,`desc`=? where cid=?";
		String pid = null;
		if (category.getParent() != null) {
			pid = category.getParent().getCid();
		}
		Object[] params = { category.getCname(), pid, category.getDesc(),
				category.getCid() };
		qr.update(sql, params);
	}

	/**
	 * 查询指定的父分类下的子分类的个数
	 * 
	 * @param pid
	 * @return
	 * @throws SQLException
	 */
	public int findChildrenCountByParent(String pid) throws SQLException {
		String sql = "select count(*) from t_category where pid=?";
		Number number = (Number) qr.query(sql, new ScalarHandler(), pid);
		return number == null ? 0 : number.intValue();
	}

	/**
	 * 删除指定的分类
	 * 
	 * @param cid
	 * @throws SQLException
	 */
	public void delete(String cid) throws SQLException {
		String sql = "delete from t_category where cid=?";
		qr.update(sql, cid);
	}
}
