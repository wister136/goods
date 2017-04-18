package cn.lsm.goods.car.dao;

import org.apache.commons.dbutils.QueryRunner;

import cn.itcast.jdbc.TxQueryRunner;

public class CarDao {
	private QueryRunner qr=new TxQueryRunner();
}
