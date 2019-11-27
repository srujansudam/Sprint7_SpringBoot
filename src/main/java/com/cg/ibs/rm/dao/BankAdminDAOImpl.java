package com.cg.ibs.rm.dao;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

//import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cg.ibs.rm.exception.ExceptionMessages;
import com.cg.ibs.rm.exception.IBSExceptions;
import com.cg.ibs.rm.model.Beneficiary;
import com.cg.ibs.rm.model.CreditCard;
import com.cg.ibs.rm.model.Customer;
import com.cg.ibs.rm.ui.CardStatus;

@Repository("BankAdminDao")
public class BankAdminDAOImpl implements BankAdminDAO {
	public BankAdminDAOImpl() {
		super();
	}

	// private static Logger logger = Logger.getLogger(BankAdminDAOImpl.class);

	@PersistenceContext
	EntityManager manager;

	public Set<BigInteger> getRequests(Integer bankerId) {
		// logger.info("entering into getRequests method of BankRepresentativeDAOImpl
		// class");
		Set<BigInteger> ucis = new HashSet<>();
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<BigInteger> query = builder.createQuery(BigInteger.class);

		Root<Customer> custRoot = query.from(Customer.class);
		Join<Customer, CreditCard> creditCards = custRoot.join("creditCards");
		query.select(custRoot.<BigInteger>get("uci"))
				.where(builder.and(builder.equal(creditCards.get("cardStatus"), CardStatus.PENDING),
						(builder.equal(creditCards.get("bankerId"), bankerId))));
		ucis.addAll(new HashSet<BigInteger>(manager.createQuery(query).getResultList()));

		CriteriaQuery<BigInteger> query2 = builder.createQuery(BigInteger.class);
		Root<Customer> custRoot2 = query2.from(Customer.class);
		Join<Customer, Beneficiary> beneficiaries = custRoot2.join("beneficiaries");
		query2.select(custRoot2.<BigInteger>get("uci"))
				.where(builder.equal(beneficiaries.get("status"), CardStatus.PENDING));
		ucis.addAll(new HashSet<BigInteger>(manager.createQuery(query2).getResultList()));

		return ucis;
	}

	public Set<CreditCard> getCreditCardDetails(Integer bankerId) {// credit cards list which goes to bank admin
		// logger.info("entering into getCreditCardDetails method of
		// BankRepresentativeDAOImpl class");
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<CreditCard> query = builder.createQuery(CreditCard.class);
		Root<Customer> custRoot = query.from(Customer.class);
		Join<Customer, CreditCard> unapprovedCreditCards = custRoot.join("creditCards");
		query.select(unapprovedCreditCards)
				.where(builder.and(builder.equal(unapprovedCreditCards.get("cardStatus"), CardStatus.PENDING),
						(builder.equal(unapprovedCreditCards.get("bankerId"), bankerId))));
		return new HashSet<>(manager.createQuery(query).getResultList());
	}

	public Set<Beneficiary> getBeneficiaryDetails() {// beneficiary list which goes to bank admin
		// logger.info("entering into getBeneficiaryDetails method of
		// BankRepresentativeDAOImpl class");
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Beneficiary> query = builder.createQuery(Beneficiary.class);
		Root<Customer> custRoot = query.from(Customer.class);
		Join<Customer, Beneficiary> unapprovedBeneficiaries = custRoot.join("beneficiaries");
		query.select(unapprovedBeneficiaries)
				.where(builder.equal(unapprovedBeneficiaries.get("status"), CardStatus.PENDING));
		return new HashSet<>(manager.createQuery(query).getResultList());
	}

	@Override
	public boolean checkedCreditCardDetails(BigInteger cardNumber) throws IBSExceptions {// bank admin gives his
		// approval/disapproval
		// logger.info("entering into checkedCreditCardDetails method of
		// BankRepresentativeDAOImpl class");
		boolean check = false;
		CreditCard cardCheck = manager.find(CreditCard.class, cardNumber);
		if (null == cardCheck) {
			throw new IBSExceptions(ExceptionMessages.CARD_DOESNT_EXIST);
		} else if (cardCheck.getCardStatus().equals(CardStatus.ACTIVE)) {
			throw new IBSExceptions(ExceptionMessages.CARD_ALREADY_ADDED);
		} else if (cardCheck.getCardStatus().equals(CardStatus.PENDING)) {
			cardCheck.setCardStatus(CardStatus.ACTIVE);
			manager.merge(cardCheck);
			check = true;
		}
		return check;
	}

	@Override
	public boolean decliningCreditCardDetails(BigInteger cardNumber) throws IBSExceptions {// bank admin gives his
																							// approval/disapproval
		// logger.info("entering into decliningCreditCardDetails method of
		// BankRepresentativeDAOImpl class");
		boolean check = false;
		CreditCard cardCheck = manager.find(CreditCard.class, cardNumber);
		if (null == cardCheck) {
			throw new IBSExceptions(ExceptionMessages.CARD_DOESNT_EXIST);
		} else if (cardCheck.getCardStatus().equals(CardStatus.ACTIVE)) {
			throw new IBSExceptions(ExceptionMessages.CARD_ALREADY_ADDED);
		} else if (cardCheck.getCardStatus().equals(CardStatus.PENDING)) {
			cardCheck.setCardStatus(CardStatus.BLOCKED);
			manager.merge(cardCheck);
			check = true;
		}
		return check;
	}

	@Override
	public boolean checkedBeneficiaryDetails(BigInteger accountNumber) throws IBSExceptions {// bank admin gives his
																								// approval/disapproval
		// logger.info("entering into checkedBeneficiaryDetails method of
		// BankRepresentativeDAOImpl class");
		boolean result = false;
		Beneficiary beneficiaryCheck = manager.find(Beneficiary.class, accountNumber);
		if (null == beneficiaryCheck) {
			throw new IBSExceptions(ExceptionMessages.BENEFICIARY_DOESNT_EXIST);
		} else if (beneficiaryCheck.getStatus().equals(CardStatus.ACTIVE)) {
			throw new IBSExceptions(ExceptionMessages.BENEFICIARY_ALREDY_ADDED);
		} else if (beneficiaryCheck.getStatus().equals(CardStatus.PENDING)) {
			beneficiaryCheck.setStatus(CardStatus.ACTIVE);
			manager.merge(beneficiaryCheck);
			result = true;
		}
		return result;
	}

	@Override
	public boolean decliningBeneficiaryDetails(BigInteger accountNumber) throws IBSExceptions {// bank admin gives his
																								// approval/disapproval
		// logger.info("entering into decliningBeneficiaryDetails method of
		// BankRepresentativeDAOImpl class");
		boolean result = false;
		Beneficiary beneficiaryCheck = manager.find(Beneficiary.class, accountNumber);
		if (null == beneficiaryCheck) {
			throw new IBSExceptions(ExceptionMessages.BENEFICIARY_DOESNT_EXIST);
		} else if (beneficiaryCheck.getStatus().equals(CardStatus.ACTIVE)) {
			throw new IBSExceptions(ExceptionMessages.BENEFICIARY_ALREDY_ADDED);
		} else if (beneficiaryCheck.getStatus().equals(CardStatus.PENDING)) {
			beneficiaryCheck.setStatus(CardStatus.BLOCKED);
			manager.merge(beneficiaryCheck);
			result = true;
		}
		return result;
	}

}
