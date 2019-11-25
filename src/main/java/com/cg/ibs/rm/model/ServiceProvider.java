package com.cg.ibs.rm.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Service_providers")
public class ServiceProvider implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7898401150996938520L;
	@Id
    private String userId;
    private String category;
    private String nameOfCompany;
    private String gstin;
    private String panNumber;
    private byte[] panCardUpload;
    private BigInteger accountNumber;
    private String bankName;
    private byte[] addressProofUpload;
    private String companyAddress;
    private BigInteger mobileNumber;
    private String password;
    private BigInteger spi = BigInteger.valueOf(-1);
    private String status = "Pending";
    private LocalDateTime requestDate;
    private String remarks = "None";

	public ServiceProvider() {
		super();
	}

	public byte[] getPanCardUpload() {
		return panCardUpload;
	}


	public void setPanCardUpload(byte[] panCardUpload) {
		this.panCardUpload = panCardUpload;
	}


	public byte[] getAddressProofUpload() {
		return addressProofUpload;
	}


	public void setAddressProofUpload(byte[] addressProofUpload) {
		this.addressProofUpload = addressProofUpload;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public String getNameOfCompany() {
		return nameOfCompany;
	}

	public void setNameOfCompany(String nameOfCompany) {
		this.nameOfCompany = nameOfCompany;
	}

	public String getGstin() {
		return gstin;
	}

	public void setGstin(String gstin) {
		this.gstin = gstin;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public BigInteger getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(BigInteger accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public BigInteger getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(BigInteger mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public BigInteger getSpi() {
		return spi;
	}

	public void setSpi(BigInteger spi) {
		this.spi = spi;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(LocalDateTime requestDate) {
		this.requestDate = requestDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "ServiceProvider [category=" + category + ", nameOfCompany=" + nameOfCompany + ", gstin=" + gstin
				+ ", panNumber=" + panNumber + ", accountNumber=" + accountNumber + ", bankName=" + bankName
				+ ", companyAddress=" + companyAddress + ", mobileNumber=" + mobileNumber + ", userId=" + userId
				+ ", password=" + password + ", spi=" + spi + ", status=" + status + ", requestDate=" + requestDate
				+ "]";
	}

}
