package bean;

import java.util.Date;
import java.util.List;

public class UserBean {
	
	private int id;
	private String code;
	private String name;
	private String password;
	private String picturePath;
	private String email;
	private int role;
	private int activeStatus;
	private int isConfirm;
	private String creatorCode;
	private String createdateTime;
	private String lasteditorCode;
	private String lasteditdateTime;
	
	public UserBean() {}

	public UserBean(int id, String code, String name, String password, String picturePath, String email, int role,
			int activeStatus, int isConfirm, String creatorCode, String createdateTime, String lasteditorCode,
			String lasteditdateTime) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.password = password;
		this.picturePath = picturePath;
		this.email = email;
		this.role = role;
		this.activeStatus = activeStatus;
		this.isConfirm = isConfirm;
		this.creatorCode = creatorCode;
		this.createdateTime = createdateTime;
		this.lasteditorCode = lasteditorCode;
		this.lasteditdateTime = lasteditdateTime;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getPicturePath() {
		return picturePath;
	}



	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public int getRole() {
		return role;
	}



	public void setRole(int role) {
		this.role = role;
	}



	public int getActiveStatus() {
		return activeStatus;
	}



	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}



	public int getIsConfirm() {
		return isConfirm;
	}



	public void setIsConfirm(int isConfirm) {
		this.isConfirm = isConfirm;
	}



	public String getCreatorCode() {
		return creatorCode;
	}



	public void setCreatorCode(String creatorCode) {
		this.creatorCode = creatorCode;
	}



	public String getCreatedateTime() {
		return createdateTime;
	}



	public void setCreatedateTime(String createdateTime) {
		this.createdateTime = createdateTime;
	}



	public String getLasteditorCode() {
		return lasteditorCode;
	}



	public void setLasteditorCode(String lasteditorCode) {
		this.lasteditorCode = lasteditorCode;
	}



	public String getLasteditdateTime() {
		return lasteditdateTime;
	}



	public void setLasteditdateTime(String lasteditdateTime) {
		this.lasteditdateTime = lasteditdateTime;
	}
	
	
	
}
