package cn.lsm.goods.car.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import cn.lsm.goods.car.service.CarService;

public class CarServlet extends BaseServlet {

	private CarService carService =new CarService();
}
