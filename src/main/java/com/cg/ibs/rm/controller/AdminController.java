package com.cg.ibs.rm.controller;

import java.math.BigInteger;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.model.Beneficiary;
import com.cg.ibs.rm.model.CreditCard;
import com.cg.ibs.rm.service.Bank_AdminService;

@RestController
public class AdminController {

	@Autowired
	private Bank_AdminService service;

	@GetMapping("/cardRequests")
	public ResponseEntity<Set<CreditCard>> showUnapprovedCardRequests() {
		Set<CreditCard> cardList = service.showUnapprovedCreditCards();
		ResponseEntity<Set<CreditCard>> result;
		if (cardList.isEmpty())
			result = new ResponseEntity<Set<CreditCard>>(cardList, HttpStatus.NO_CONTENT);
		else
			result = new ResponseEntity<Set<CreditCard>>(cardList, HttpStatus.OK);
		return result;
	}

	@GetMapping("/beneficiaryRequests")
	public ResponseEntity<Set<Beneficiary>> showUnapprovedBenRequests() {
		Set<Beneficiary> benList = service.showUnapprovedBeneficiaries();
		ResponseEntity<Set<Beneficiary>> result;
		if (benList.isEmpty())
			result = new ResponseEntity<Set<Beneficiary>>(benList, HttpStatus.NO_CONTENT);
		else
			result = new ResponseEntity<Set<Beneficiary>>(benList, HttpStatus.OK);
		return result;
	}

	@GetMapping("/acceptCards/{cardNumber}/{decision}")
	public ResponseEntity<String> acceptCards(@PathVariable("cardNumber") BigInteger cardNumber,
			@PathVariable("decision") String decision) {
		ResponseEntity<String> result;
		if (cardNumber == null)
			result = new ResponseEntity<String>("No details entered", HttpStatus.NO_CONTENT);
		if (decision.equalsIgnoreCase("approve")) {
			try {
				service.saveCreditCardDetails(cardNumber);
				result = new ResponseEntity<String>("Approved", HttpStatus.OK);

			} catch (IBSExceptions e) {
				result = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		} else if (decision.equalsIgnoreCase("disapprove")) {
			try {
				service.disapproveCreditCard(cardNumber);
				result = new ResponseEntity<String>("Disapproved", HttpStatus.OK);
			} catch (IBSExceptions e) {
				result = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		} else {
			result = new ResponseEntity<String>("inappropriate decision", HttpStatus.UNAUTHORIZED);
		}

		return result;

	}

	@GetMapping("/acceptBeneficiaries/{accountNumber}/{decision}")
	public ResponseEntity<String> acceptBeneficiaries(@PathVariable("accountNumber") BigInteger accountNumber,
			@PathVariable("decision") String decision) {
		ResponseEntity<String> result;
		if (accountNumber == null)
			result = new ResponseEntity<String>("No details entered", HttpStatus.NO_CONTENT);
		if (decision.equalsIgnoreCase("approve")) {
			try {
				service.saveBeneficiaryDetails(accountNumber);
				result = new ResponseEntity<String>("Approved", HttpStatus.OK);

			} catch (IBSExceptions e) {
				result = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		} else if (decision.equalsIgnoreCase("disapprove")) {
			try {
				service.disapproveBenficiary(accountNumber);
				result = new ResponseEntity<String>("Disapproved", HttpStatus.OK);
			} catch (IBSExceptions e) {
				result = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		} else {
			result = new ResponseEntity<String>("inappropriate decision", HttpStatus.UNAUTHORIZED);
		}

		return result;
	}
}