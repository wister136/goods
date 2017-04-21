package cn.lsm.goods.car.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.jdbc.TxQueryRunner;
import cn.lsm.goods.car.domain.Car;
import cn.lsm.goods.pager.Expression;
import cn.lsm.goods.pager.PageBean;
import cn.lsm.goods.pager.PageConstants;

public class CarDao {
	private QueryRunner qr = new TxQueryRunner();

	/**
	 * 按分类查询
	 * 
	 * @param id
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Car> findByCategory(String cid, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("cid", "=", cid));
		return findByCriteria(exprList, pc);
	}

	/**
	 * 按车名字模糊查询
	 * 
	 * @param cname
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Car> findByCname(String bname, int pc) throws SQLException {
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname", "like", "%" + bname + "%"));
		return findByCriteria(exprList, pc);
	}

	/**
	 * 通用的查询方法
	 * 
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Car> findByCriteria(List<Expression> exprList, int pc)
			throws SQLException {
		/*
		 * 1.得到ps(每页记录数) 2.得到tr 3.得到beanList 4.创建PageBean，返回
		 */

		// 1.得到ps
		int ps = PageConstants.CAR_PAGE_SIZE;// 每页记录数

		// 2.通过exprList生成where子句
		StringBuilder whereSql = new StringBuilder("where 1=1");
		List<Object> params = new ArrayList<Object>();// SQL中有问号，他是对应问号的值
		for (Expression expr : exprList) {
			/*
			 * 1.添加一个条件以and开头， 2.条件的名称 3.条件的运算符，可以是=，！=，>,<,=...is null(is
			 * null没有值)
			 */
			whereSql.append(" and ").append(expr.getName()).append(" ")
					.append(expr.getOperator()).append(" ");

			if (!expr.getOperator().equals("is null")) {
				whereSql.append("?");
				params.add(expr.getValue());
			}
		}
		// System.out.println(whereSql);
		// System.out.println(params);

		/*
		 * 3.得到总记录数
		 */
		String sql = "select count(*) from t_car" + whereSql;
		Number number = (Number) qr.query(sql, new ScalarHandler(),
				params.toArray());
		int tr = number.intValue();// 得到总记录数

		/*
		 * 4.得到beanList，即当前页记录
		 */
		sql = "select * from t_car" + whereSql + "order by orderBy limit ?,?";
		params.add((pc - 1) * ps);// 第一个问号：记录当前页首行记录的下标
		params.add(ps);// 一共查询几行，就是每页记录数

		List<Car> beanList = qr.query(sql, new BeanListHandler<Car>(Car.class),
				params.toArray());

		/*
		 * 5.创建PageBean,设置参数
		 */
		PageBean<Car> pb = new PageBean<Car>();
		/*
		 * 其中PageBean没有URl，这个任务由Servlet完成
		 */
		pb.setBeanList(beanList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		return null;
	}

	public static void main(String[] args) {
		CarDao caoDao = new CarDao();
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bid", "=", "1"));
		exprList.add(new Expression("bname", "like", "%java%"));
		exprList.add(new Expression("brand", "is null", null));

		// caoDao.findByCriteria(exprList, 10);
	}
}
