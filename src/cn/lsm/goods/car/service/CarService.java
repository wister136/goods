package cn.lsm.goods.car.service;

import java.sql.SQLException;

import cn.lsm.goods.car.dao.CarDao;
import cn.lsm.goods.car.domain.Car;

public class CarService {
	private CarDao carDao=new CarDao();
	
	/**
	 * 根据指定的分类查找车辆个数
	 * @param cid
	 * @return
	 */
	public int findCarCountByCategory(String cid) {
		try {
			return carDao.findCarCountByCategory(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 添加车辆
	 * 
	 * @param car
	 */
	public void add(Car car) {
		try {
			carDao.add(car);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
