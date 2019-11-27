package com.cg.ibs.rm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "BANK_ADMINS")
public class RemmitanceHistory implements Serializable {

	private static final long serialVersionUID = -3690871004696421591L;
	private String response;

	public RemmitanceHistory() {
		super();
	}

	public RemmitanceHistory(String response) {
		super();
		this.response = response;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
