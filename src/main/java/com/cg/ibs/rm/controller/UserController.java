package com.cg.ibs.rm.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.model.Account;
import com.cg.ibs.rm.model.AutoPayment;
import com.cg.ibs.rm.model.Beneficiary;
import com.cg.ibs.rm.model.CreditCard;
import com.cg.ibs.rm.model.Customer;
import com.cg.ibs.rm.model.ServiceProvider;
import com.cg.ibs.rm.model.ServiceProviderId;
import com.cg.ibs.rm.service.AccountService;
import com.cg.ibs.rm.service.AutoPaymentService;
import com.cg.ibs.rm.service.BeneficiaryAccountService;
import com.cg.ibs.rm.service.CreditCardService;
import com.cg.ibs.rm.service.CustomerService;
import com.cg.ibs.rm.ui.Type;

@RestController
@Scope("session")
public class UserController {
	@Autowired
	private AutoPaymentService autoPaymentService;

	BigInteger uci;
	@Autowired
	CustomerService customerService;
	@Autowired
	private CreditCardService creditCard;
	@Autowired
	private AccountService accountService;
	@Autowired
	private BeneficiaryAccountService beneficiaryservice;

	@PostMapping("/userinput")
	public ResponseEntity<String> getName(@RequestBody Customer bean) {
		ResponseEntity<String> result;
		try {
			if (bean.getUserId() == null) {
				result = new ResponseEntity<>("No User Details Received", HttpStatus.BAD_REQUEST);
			} else {
				uci = customerService.returnUCI(bean.getUserId());
				System.out.println(uci);
				result = new ResponseEntity<>(
						"welcome " + customerService.returnName(customerService.returnUCI(bean.getUserId())),
						HttpStatus.OK);
			}
		} catch (IBSExceptions e) {
			result = new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
			System.out.println(e.getMessage());
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/addcard")
	public String addCardGo() {
		return "addcard";
	}

	@PostMapping("/addcard")
	public ResponseEntity<String> addCard(@RequestBody CreditCard card) {
		ResponseEntity<String> result;
		System.out.println(card.getMonth());
		LocalDate date = LocalDate.of(card.getYear(), card.getMonth(), 01);
		card.setDateOfExpiry(date);
		card.setTimestamp(LocalDateTime.now());
		try {
			creditCard.saveCardDetails(uci, card);
			result = new ResponseEntity<String>("Card added successfully!!", HttpStatus.OK);
		} catch (IBSExceptions e) {
			result = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/viewcard")
	public ModelAndView viewCardDetails() {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("savedCards", creditCard.showCardDetails(uci));
			mv.setViewName("viewcard");
		} catch (IBSExceptions e) {
			mv.setViewName("exceptionpage");
			mv.addObject("exception", e.getMessage());
		}
		return mv;
	}

	@RequestMapping("/deletecard")
	public ModelAndView deleteCreditCard(@RequestParam BigInteger cardNumber, @RequestParam String delete) {
		ModelAndView modelAndView = new ModelAndView();
		if (delete.equalsIgnoreCase("Delete")) {
			try {
				boolean check = creditCard.deleteCardDetails(cardNumber);
				if (check) {
					modelAndView.addObject("savedCards", creditCard.showCardDetails(uci));
					modelAndView.setViewName("viewcard");
				}

			} catch (IBSExceptions e) {
				modelAndView.setViewName("exceptionpage");
				modelAndView.addObject("exception", e.getMessage());
			}
		}

		return modelAndView;

	}

	@RequestMapping("/beneficiary")
	public ModelAndView showBenHome() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("benhome");
		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/addbentype")
	public String addBenGo() {
		return "addbentype";
	}

	@RequestMapping("/samebank")
	public String sambank() {
		return "samebank";
	}

	@RequestMapping("/otherbank")
	public String otherbank() {
		return "otherbank";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/samebank")
	public ModelAndView addSameBeneficiary(@ModelAttribute Beneficiary beneficiary) {
		ModelAndView mv = new ModelAndView();
		beneficiary.setBankName("IBS");
		beneficiary.setIfscCode("IBS45623778");
		beneficiary.setTimestamp(LocalDateTime.now());
		try {
			beneficiaryservice.saveBeneficiaryAccountDetails(uci, beneficiary);
			mv.addObject("name", customerService.returnName(uci));
			mv.setViewName("submitben");
		} catch (IBSExceptions e) {
			mv.setViewName("exceptionpage");
			mv.addObject("exception", e.getMessage());
		}
		return mv;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/otherbank")
	public ModelAndView addOtherBeneficiary(@ModelAttribute Beneficiary beneficiary) {
		ModelAndView mv = new ModelAndView();
		beneficiary.setTimestamp(LocalDateTime.now());
		try {
			beneficiaryservice.saveBeneficiaryAccountDetails(uci, beneficiary);
			mv.addObject("name", customerService.returnName(uci));
			mv.setViewName("submitben");
		} catch (IBSExceptions e) {
			mv.setViewName("exceptionpage");
			mv.addObject("exception", e.getMessage());
		}
		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/viewben")
	public ModelAndView viewBeneficiaries() {
		ModelAndView mv = new ModelAndView();
		try {
			mv.addObject("savedBeneficiaries", beneficiaryservice.showBeneficiaryAccount(uci));
			mv.setViewName("viewben");
		} catch (IBSExceptions e) {
			mv.setViewName("exceptionpage");
			mv.addObject("exception", e.getMessage());
		}
		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/modifybeneficiary")
	public ModelAndView modifybeneficiary(@RequestParam("accountNumber") BigInteger accountNumber) {
		ModelAndView mv = new ModelAndView();
		Beneficiary beneficiary;
		try {
			beneficiary = beneficiaryservice.getBeneficiary(accountNumber);
			if (beneficiary.getType().equals(Type.MYACCOUNTINIBS)
					|| beneficiary.getType().equals(Type.OTHERSACCOUNTINIBS)) {
				mv.addObject(beneficiary);
				mv.setViewName("modifyinibs");
			} else {
				mv.addObject(beneficiary);
				mv.setViewName("modifyinother");
			}
		} catch (IBSExceptions e) {
			mv.setViewName("exceptionpage");
			mv.addObject("exception", e.getMessage());
		}
		return mv;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/modifyinibs")
	public String modifyibs() {
		return "modifyinibs";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/modifyinother")
	public String modifyother() {
		return "modifyinother";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/modifyinother")
	public ModelAndView modifyOtherBeneficiary(@ModelAttribute Beneficiary beneficiary) {
		ModelAndView mv = new ModelAndView();
		beneficiary.setTimestamp(LocalDateTime.now());
		try {
			beneficiaryservice.modifyBeneficiaryAccountDetails(beneficiary.getAccountNumber(), beneficiary);
			mv.addObject("name", customerService.returnName(uci));
			mv.setViewName("submitben");
		} catch (IBSExceptions | IOException e) {
			mv.setViewName("exceptionpage");
			mv.addObject("exception", e.getMessage());
		}
		return mv;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/modifyinibs")
	public ModelAndView modifyIbsBeneficiary(@ModelAttribute Beneficiary beneficiary) {
		ModelAndView mv = new ModelAndView();
		beneficiary.setTimestamp(LocalDateTime.now());
		try {
			beneficiary.setBankName("IBS");
			beneficiary.setIfscCode("IBS45623778");
			beneficiaryservice.modifyBeneficiaryAccountDetails(beneficiary.getAccountNumber(), beneficiary);
			mv.addObject("name", customerService.returnName(uci));
			mv.setViewName("submitben");

		} catch (IBSExceptions | IOException e) {
			mv.setViewName("exceptionpage");
			mv.addObject("exception", e.getMessage());
		}
		return mv;
	}

	@DeleteMapping("/deleteben")
	public ResponseEntity<String> deletebeneficiary(@RequestParam BigInteger accountNumber, @RequestParam String delete) {
		ModelAndView modelAndView = new ModelAndView();
		if (delete.equalsIgnoreCase("Delete")) {
			try {
				boolean check = beneficiaryservice.deleteBeneficiaryAccountDetails(accountNumber);
				if (check) {
					modelAndView.addObject("savedBeneficiaries", beneficiaryservice.showBeneficiaryAccount(uci));
					modelAndView.setViewName("viewben");
				}

			} catch (IBSExceptions e) {
				modelAndView.setViewName("exceptionpage");
				modelAndView.addObject("exception", e.getMessage());
			}
		}
		return modelAndView;
	}

	@GetMapping("/addautopaymentaccounts")
	public ResponseEntity<Set<Account>> showAccounts() {
		Set<Account> accounts = null;
		ResponseEntity<Set<Account>> result;
		try {
			accounts = accountService.getAccountsOfUci(uci);
			result = new ResponseEntity<Set<Account>>(accounts, HttpStatus.OK);
		} catch (IBSExceptions e) {
			e.printStackTrace();
			result = new ResponseEntity<Set<Account>>(accounts, HttpStatus.NO_CONTENT);
		}
		return result;
	}

	@GetMapping("/addautopaymentproviders")
	public ResponseEntity<Set<ServiceProvider>> showServiceProviders() {
		Set<ServiceProvider> providers = null;
		ResponseEntity<Set<ServiceProvider>> result;

		providers = autoPaymentService.showIBSServiceProviders();
		if (providers.isEmpty()) {
			result = new ResponseEntity<Set<ServiceProvider>>(providers, HttpStatus.NO_CONTENT);
		} else {
			result = new ResponseEntity<Set<ServiceProvider>>(providers, HttpStatus.OK);
		}

		return result;
	}

	@PostMapping("/addautopayment/{accountNumber}")
	public ResponseEntity<String> addAutopayment(@PathVariable("accountNumber") BigInteger accountNumber,
			@RequestBody AutoPayment autoPayment) {
		ResponseEntity<String> result = null;
		BigInteger spId = null;
		Set<ServiceProvider> serviceProviders = autoPaymentService.showIBSServiceProviders();
		for (ServiceProvider serviceProvider : serviceProviders) {
			if (serviceProvider.getNameOfCompany().equalsIgnoreCase(autoPayment.getServiceName())) {
				spId = serviceProvider.getSpi();
			}
		}
		autoPayment.setServiceProviderId(new ServiceProviderId(spId, uci));

		try {
			autoPaymentService.autoDeduction(uci, accountNumber, autoPayment);
			result = new ResponseEntity<String>("Autopayment added successfully", HttpStatus.OK);
		} catch (IBSExceptions e) {
			result = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return result;
	}

	@GetMapping("/viewautopayment")
	public ResponseEntity<Set<AutoPayment>> viewAutoPayments() {

		Set<AutoPayment> autoPayments = null;
		ResponseEntity<Set<AutoPayment>> result;
		try {
			autoPayments = autoPaymentService.showAutopaymentDetails(uci);
			result = new ResponseEntity<Set<AutoPayment>>(autoPayments, HttpStatus.OK);

		} catch (IBSExceptions e) {
			result = new ResponseEntity<Set<AutoPayment>>(autoPayments, HttpStatus.NO_CONTENT);
		}
		return result;
	}

	@PutMapping(value = "/modifyautopayment/{spi}")
	public ResponseEntity<String> modifyautopaymentDetails(@RequestBody AutoPayment autoPayment,
			@PathVariable("spi") BigInteger spi) {
		ResponseEntity<String> result;
		try {
			if ((autoPayment == null) || (spi == null))
				result = new ResponseEntity<String>("Values not entered", HttpStatus.NO_CONTENT);
			if (autoPaymentService.updateDetails(new ServiceProviderId(spi, uci), autoPayment))
				result = new ResponseEntity<String>("Modified autopayment", HttpStatus.OK);
			else
				result = new ResponseEntity<String>("Not Modified", HttpStatus.BAD_REQUEST);
		} catch (IBSExceptions e) {
			result = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return result;
	}

	@DeleteMapping("/deleteautopayment/{spi}")
	public ResponseEntity<String> deleteAutoPayment(@PathVariable("spi") BigInteger spId1) {
		ResponseEntity<String> result;
		try {
			if (spId1 == null)
				result = new ResponseEntity<String>("No details entered", HttpStatus.NO_CONTENT);
			if (autoPaymentService.deleteAutopayment(uci, spId1))
				result = new ResponseEntity<String>("Deleted", HttpStatus.OK);
			else
				result = new ResponseEntity<String>("Not Deleted", HttpStatus.BAD_REQUEST);
		} catch (IBSExceptions e) {
			result = new ResponseEntity<String>(e.getMessage(), HttpStatus.NO_CONTENT);
		}

		return result;

	}

	@RequestMapping("/exceptionuser")
	public ModelAndView exception() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("exception", "Under Maintenance");
		mv.setViewName("exceptionpage");
		return mv;
	}

}
