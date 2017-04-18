package cn.lsm.goods.car.domain;

import cn.lsm.goods.category.domain.Category;

public class Car {
	private String bid;//主键
	private String bname;//车名
	private String brand;//品牌
	private double price;//价钱
	private double currprice;//指导价
	private double discount;//折扣
	private String manufacturer;//生产厂家
	private String registration;//上牌日期
	private String timetomarket;//上市时间
	private double carkil; //公里数
	private String registarea;//上牌地区
	private Category category;//所属的分类
	private String image_w;//大图路径
	private String image_b;//小图路径
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getImage_w() {
		return image_w;
	}
	public void setImage_w(String image_w) {
		this.image_w = image_w;
	}
	public String getImage_b() {
		return image_b;
	}
	public void setImage_b(String image_b) {
		this.image_b = image_b;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getCurrprice() {
		return currprice;
	}
	public void setCurrprice(double currprice) {
		this.currprice = currprice;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getRegistration() {
		return registration;
	}
	public void setRegistration(String registration) {
		this.registration = registration;
	}
	public String getTimetomarket() {
		return timetomarket;
	}
	public void setTimetomarket(String timetomarket) {
		this.timetomarket = timetomarket;
	}
	public double getCarkil() {
		return carkil;
	}
	public void setCarkil(double carkil) {
		this.carkil = carkil;
	}
	public String getRegistarea() {
		return registarea;
	}
	public void setRegistarea(String registarea) {
		this.registarea = registarea;
	}
	
}
