package cn.lsm.goods.category.service;

import java.sql.SQLException;
import java.util.List;

import cn.lsm.goods.category.dao.CategoryDao;
import cn.lsm.goods.category.domain.Category;

/**
 * 分类模块的业务层
 * @author Administrator
 *
 */
public class CategoryService {
	private CategoryDao categoryDao =new CategoryDao();
	
	/**
	 * 查询指定父分类下子分类的个数
	 * @param pid
	 * @return
	 */
	public int findChildrenCountByParent(String pid){
		try {
			return categoryDao.findChildrenCountByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 删除分类
	 * @param cid
	 */
	public void delete (String cid){
		try {
			categoryDao.delete(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	} 
	
	/**
	 * 1,2级信息载入
	 * @param cid
	 * @return
	 */
	public Category load(String cid){
		try {
			return categoryDao.load(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 1,2级信息修改
	 * @param cid
	 * @return
	 */
	public void edit(Category category){
		try {
			 categoryDao.edit(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 添加分类
	 */
	public void add(Category category){
		try {
			categoryDao.addParent(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查询所有分类
	 * @return
	 */
	public List<Category> findAll(){
		try {
			return categoryDao.findAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取所有父分类，不带子分类
	 * @return
	 */
	public List<Category> findParents(){
		try {
			return categoryDao.findParents();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 通过父分类来查找
	 * @param pid
	 * @return
	 */
	public List<Category> findByParent(String pid){
		try {
			return categoryDao.findByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
