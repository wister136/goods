package cn.lsm.goods.admin.admin.service;

import java.sql.SQLException;

import javax.management.RuntimeErrorException;

import cn.lsm.goods.admin.admin.dao.AdminDao;
import cn.lsm.goods.admin.admin.domain.Admin;

public class AdminService {
	private AdminDao adminDao=new AdminDao();
	
	/*
	 * 登录功能
	 */
	public Admin login(Admin admin){
		try {
			return adminDao.find(admin.getAdminname(), admin.getAdminpwd());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
}
