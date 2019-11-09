package com.bailun.gogirl.bean;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Customer {
    private Integer id;

    private String openid;
    private String unionid;

    private String phone;

    private String nickname;

    private String password;

    private String sex;

    private String country;

    private String province;

    private String city;

    private String headimgurl;

    private String privilege;

    private String state;

    private Date registerTime;

    private Date updateTime;

    private String realName;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthday;

    private Integer registerDepartment;

    private String storeRecordRealName;

    private String remark;
    private Integer source;
    private Integer scheduledTimes;
    private Integer orderTimes;

    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl == null ? null : headimgurl.trim();
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege == null ? null : privilege.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getRegisterDepartment() {
        return registerDepartment;
    }

    public void setRegisterDepartment(Integer registerDepartment) {
        this.registerDepartment = registerDepartment;
    }

    public String getStoreRecordRealName() {
        return storeRecordRealName;
    }

    public void setStoreRecordRealName(String storeRecordRealName) {
        this.storeRecordRealName = storeRecordRealName == null ? null : storeRecordRealName.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }



	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getScheduledTimes() {
		return scheduledTimes;
	}

	public void setScheduledTimes(Integer scheduledTimes) {
		this.scheduledTimes = scheduledTimes;
	}

	public Integer getOrderTimes() {
		return orderTimes;
	}

	public void setOrderTimes(Integer orderTimes) {
		this.orderTimes = orderTimes;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", openid=" + openid + ", unionid="
				+ unionid + ", phone=" + phone + ", nickname=" + nickname
				+ ", password=" + password + ", sex=" + sex + ", country="
				+ country + ", province=" + province + ", city=" + city
				+ ", headimgurl=" + headimgurl + ", privilege=" + privilege
				+ ", state=" + state + ", registerTime=" + registerTime
				+ ", updateTime=" + updateTime + ", realName=" + realName
				+ ", birthday=" + birthday + ", registerDepartment="
				+ registerDepartment + ", storeRecordRealName="
				+ storeRecordRealName + ", remark=" + remark + ", source="
				+ source + ", scheduledTimes=" + scheduledTimes
				+ ", orderTimes=" + orderTimes + "]";
	}
}