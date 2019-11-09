package gogirl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


public class OrderManage {

    private Integer id;
    private String orderNo;
    private Integer orderUser;
    private Integer departmentId;
    private String createTime;
    private String finishTime;
    private BigDecimal totelPrice;
    private BigDecimal discountPrice;
    private Integer status;
    private String remark;
    private String createUser;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(Integer orderUser) {
        this.orderUser = orderUser;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public BigDecimal getTotelPrice() {
        return totelPrice;
    }

    public void setTotelPrice(BigDecimal totelPrice) {
        this.totelPrice = totelPrice;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

	@Override
	public String toString() {
		return "OrderManage [id=" + id + ", orderNo=" + orderNo
				+ ", orderUser=" + orderUser + ", departmentId=" + departmentId
				+ ", createTime=" + createTime + ", finishTime=" + finishTime
				+ ", totelPrice=" + totelPrice + ", discountPrice="
				+ discountPrice + ", status=" + status + ", remark=" + remark
				+ ", createUser=" + createUser + "]";
	}


}
