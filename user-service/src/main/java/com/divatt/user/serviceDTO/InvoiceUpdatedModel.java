package com.divatt.user.serviceDTO;

public class InvoiceUpdatedModel {
	
	private String billedUserName;
	private String bllingAddress;
	private String billingCity;
	private String billingState;
	private String pincode;
	private String billingMobile;
	private String shippingUserName;
	private String shippingAddress;
	private String shippingCity;
	private String shippingState;
	private String shippingPincode;
	private String shippingMobile;
	private String orderId;
	private String orderDate;
	private String invoiceId;
	private String soldBy;
	private String sellerName;
	private String sellerAddress;
	private String sellerCity;
	private String sellerState;
	private String sellerPincode;
	private String sellerMobile;
	private String productName;
	private String qty;
	private String grossAmount;
	private String discount;
	private String cgst;
	private String sgst;
	private String igst;
	private String total;
	private String grandTotal;
	public InvoiceUpdatedModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InvoiceUpdatedModel(String billedUserName, String bllingAddress, String billingCity, String billingState,
			String pincode, String billingMobile, String shippingUserName, String shippingAddress, String shippingCity,
			String shippingState, String shippingPincode, String shippingMobile, String orderId, String orderDate,
			String invoiceId, String soldBy, String sellerName, String sellerAddress, String sellerCity,
			String sellerState, String sellerPincode, String sellerMobile, String productName, String qty,
			String grossAmount, String discount, String cgst, String sgst, String igst, String total,
			String grandTotal) {
		super();
		this.billedUserName = billedUserName;
		this.bllingAddress = bllingAddress;
		this.billingCity = billingCity;
		this.billingState = billingState;
		this.pincode = pincode;
		this.billingMobile = billingMobile;
		this.shippingUserName = shippingUserName;
		this.shippingAddress = shippingAddress;
		this.shippingCity = shippingCity;
		this.shippingState = shippingState;
		this.shippingPincode = shippingPincode;
		this.shippingMobile = shippingMobile;
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.invoiceId = invoiceId;
		this.soldBy = soldBy;
		this.sellerName = sellerName;
		this.sellerAddress = sellerAddress;
		this.sellerCity = sellerCity;
		this.sellerState = sellerState;
		this.sellerPincode = sellerPincode;
		this.sellerMobile = sellerMobile;
		this.productName = productName;
		this.qty = qty;
		this.grossAmount = grossAmount;
		this.discount = discount;
		this.cgst = cgst;
		this.sgst = sgst;
		this.igst = igst;
		this.total = total;
		this.grandTotal = grandTotal;
	}
	@Override
	public String toString() {
		return "InvoiceUpdatedModel [billedUserName=" + billedUserName + ", bllingAddress=" + bllingAddress
				+ ", billingCity=" + billingCity + ", billingState=" + billingState + ", pincode=" + pincode
				+ ", billingMobile=" + billingMobile + ", shippingUserName=" + shippingUserName + ", shippingAddress="
				+ shippingAddress + ", shippingCity=" + shippingCity + ", shippingState=" + shippingState
				+ ", shippingPincode=" + shippingPincode + ", shippingMobile=" + shippingMobile + ", orderId=" + orderId
				+ ", orderDate=" + orderDate + ", invoiceId=" + invoiceId + ", soldBy=" + soldBy + ", sellerName="
				+ sellerName + ", sellerAddress=" + sellerAddress + ", sellerCity=" + sellerCity + ", sellerState="
				+ sellerState + ", sellerPincode=" + sellerPincode + ", sellerMobile=" + sellerMobile + ", productName="
				+ productName + ", qty=" + qty + ", grossAmount=" + grossAmount + ", discount=" + discount + ", cgst="
				+ cgst + ", sgst=" + sgst + ", igst=" + igst + ", total=" + total + ", grandTotal=" + grandTotal + "]";
	}
	public String getBilledUserName() {
		return billedUserName;
	}
	public void setBilledUserName(String billedUserName) {
		this.billedUserName = billedUserName;
	}
	public String getBllingAddress() {
		return bllingAddress;
	}
	public void setBllingAddress(String bllingAddress) {
		this.bllingAddress = bllingAddress;
	}
	public String getBillingCity() {
		return billingCity;
	}
	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}
	public String getBillingState() {
		return billingState;
	}
	public void setBillingState(String billingState) {
		this.billingState = billingState;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getBillingMobile() {
		return billingMobile;
	}
	public void setBillingMobile(String billingMobile) {
		this.billingMobile = billingMobile;
	}
	public String getShippingUserName() {
		return shippingUserName;
	}
	public void setShippingUserName(String shippingUserName) {
		this.shippingUserName = shippingUserName;
	}
	public String getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	public String getShippingCity() {
		return shippingCity;
	}
	public void setShippingCity(String shippingCity) {
		this.shippingCity = shippingCity;
	}
	public String getShippingState() {
		return shippingState;
	}
	public void setShippingState(String shippingState) {
		this.shippingState = shippingState;
	}
	public String getShippingPincode() {
		return shippingPincode;
	}
	public void setShippingPincode(String shippingPincode) {
		this.shippingPincode = shippingPincode;
	}
	public String getShippingMobile() {
		return shippingMobile;
	}
	public void setShippingMobile(String shippingMobile) {
		this.shippingMobile = shippingMobile;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getSoldBy() {
		return soldBy;
	}
	public void setSoldBy(String soldBy) {
		this.soldBy = soldBy;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getSellerAddress() {
		return sellerAddress;
	}
	public void setSellerAddress(String sellerAddress) {
		this.sellerAddress = sellerAddress;
	}
	public String getSellerCity() {
		return sellerCity;
	}
	public void setSellerCity(String sellerCity) {
		this.sellerCity = sellerCity;
	}
	public String getSellerState() {
		return sellerState;
	}
	public void setSellerState(String sellerState) {
		this.sellerState = sellerState;
	}
	public String getSellerPincode() {
		return sellerPincode;
	}
	public void setSellerPincode(String sellerPincode) {
		this.sellerPincode = sellerPincode;
	}
	public String getSellerMobile() {
		return sellerMobile;
	}
	public void setSellerMobile(String sellerMobile) {
		this.sellerMobile = sellerMobile;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getGrossAmount() {
		return grossAmount;
	}
	public void setGrossAmount(String grossAmount) {
		this.grossAmount = grossAmount;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getCgst() {
		return cgst;
	}
	public void setCgst(String cgst) {
		this.cgst = cgst;
	}
	public String getSgst() {
		return sgst;
	}
	public void setSgst(String sgst) {
		this.sgst = sgst;
	}
	public String getIgst() {
		return igst;
	}
	public void setIgst(String igst) {
		this.igst = igst;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(String grandTotal) {
		this.grandTotal = grandTotal;
	}
	

}