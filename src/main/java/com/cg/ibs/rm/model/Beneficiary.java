package com.cg.ibs.rm.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cg.ibs.rm.ui.CardStatus;
import com.cg.ibs.rm.ui.Beneficiary_Type;

@Entity
@Table(name = "Beneficiaries")
public class Beneficiary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1597299572298697066L;
	@Id
	@Column(name = "Account_number")
	private BigInteger accountNumber;
	@Column(name = "Account_name")
	private String accountName;
	@Column(name = "Ifsc_Code")
	private String ifscCode;
	@Column(name = "Bank_name")
	private String bankName;
	@Column(name = "Type_of_account")
	@Enumerated(EnumType.STRING)
	private Beneficiary_Type type;
	@Column(name = "Status")
	@Enumerated(EnumType.STRING)
	private CardStatus status;
	@Column(name = "Remarks")
	private String adminRemarks = " ";
	@Column(name = "Beneficiary_Timestamp")
	private LocalDateTime timestamp;
	@Column(name = "BANKER_ID")
	private Integer bankId;

	@ManyToOne
	@JoinColumn(name = "UCI")
	private Customer customer;

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Beneficiary() {
		super();
	}

	public Beneficiary(BigInteger accountNumber, String accountName, String ifscCode, String bankName,
			Beneficiary_Type type, CardStatus status, String adminRemarks, LocalDateTime timestamp, Integer bankId,
			Customer customer) {
		super();
		this.accountNumber = accountNumber;
		this.accountName = accountName;
		this.ifscCode = ifscCode;
		this.bankName = bankName;
		this.type = type;
		this.status = status;
		this.adminRemarks = adminRemarks;
		this.timestamp = timestamp;
		this.bankId = bankId;
		this.customer = customer;
	}

	public Beneficiary_Type getType() {
		return type;
	}

	public void setType(Beneficiary_Type type) {
		this.type = type;
	}

	public BigInteger getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(BigInteger accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAdminRemarks() {
		return adminRemarks;
	}

	public void setAdminRemarks(String adminRemarks) {
		this.adminRemarks = adminRemarks;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public CardStatus getStatus() {
		return status;
	}

	public void setStatus(CardStatus status) {
		this.status = status;
	}

}
