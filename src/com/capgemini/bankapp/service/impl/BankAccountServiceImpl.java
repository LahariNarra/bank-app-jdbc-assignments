package com.capgemini.bankapp.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.capgemini.bankapp.dao.BankAccountDao;
import com.capgemini.bankapp.dao.impl.BankAccountDaoImpl;
import com.capgemini.bankapp.exception.BankAccountIdNotFoundException;
import com.capgemini.bankapp.exception.LowBalanceException;
import com.capgemini.bankapp.model.BankAccount;
import com.capgemini.bankapp.service.BankAccountService;
import com.capgemini.bankapp.util.DbUtil;

public class BankAccountServiceImpl implements BankAccountService {

	private BankAccountDao bankAccountDao;
	static final Logger logger = Logger.getLogger(BankAccountServiceImpl.class);

	public BankAccountServiceImpl() {
		bankAccountDao = new BankAccountDaoImpl();
	}

	@Override
	public double checkBalance(long accountId) throws BankAccountIdNotFoundException {

		double balance = bankAccountDao.getBalance(accountId);
		if (balance >= 0)
			return balance;
		throw new BankAccountIdNotFoundException("Account Id Not Found");
	}

	@Override
	public double withdraw(long accountId, double amount) throws LowBalanceException, BankAccountIdNotFoundException {
		double balance = bankAccountDao.getBalance(accountId);
		if (balance < 0)
			throw new BankAccountIdNotFoundException("Account Id Doesnot Exist");
		else if (balance - amount >= 0) {
			balance = balance - amount;
			bankAccountDao.updateBalance(accountId, balance);
			DbUtil.commit();
			return balance;
		} else
			throw new LowBalanceException("You dont have sufficient balance");
	}
	

	public double withdrawForFundTransfer(long accountId, double amount) throws LowBalanceException, BankAccountIdNotFoundException {
		double balance = bankAccountDao.getBalance(accountId);
		if (balance < 0)
			throw new BankAccountIdNotFoundException("Account Id Doesnot Exist");
		else if (balance - amount >= 0) {
			balance = balance - amount;
			bankAccountDao.updateBalance(accountId, balance);
			return balance;
		} else
			throw new LowBalanceException("You dont have sufficient balance");
	}

	@Override
	public double deposit(long accountId, double amount) throws BankAccountIdNotFoundException {
		double balance = bankAccountDao.getBalance(accountId);
		if (balance < 0)
			throw new BankAccountIdNotFoundException("Account Id doesn't exist");
		balance = balance + amount;
		bankAccountDao.updateBalance(accountId, balance);
		DbUtil.commit();
		return balance;
	}

	@Override
	public boolean deleteBankAccount(long accountId) throws BankAccountIdNotFoundException {

		boolean result = bankAccountDao.deleteBankAccount(accountId);
		if(result) {
			DbUtil.commit();
			return result;
		}
		throw new BankAccountIdNotFoundException("Bank Account doesn't exist..");
	}

	@Override
	public double fundTransfer(long fromAccount, long toAccount, double amount)
			throws BankAccountIdNotFoundException, LowBalanceException {
		try {
			double newBlance = withdrawForFundTransfer(fromAccount, amount);
			deposit(toAccount, amount);
			DbUtil.commit();
			return newBlance;
		} catch (BankAccountIdNotFoundException | LowBalanceException e) {
			logger.error("Exception", e);
			DbUtil.rollback();
			throw e;
		}
	}

	@Override
	public boolean addNewBankAccount(BankAccount account) {
		boolean result = bankAccountDao.addNewBankAccount(account);
		if(result)
			DbUtil.commit();
		return result;
	}

	@Override
	public List<BankAccount> findAllBankAccount() {
		return bankAccountDao.findAllBankAccounts();
	}

	@Override
	public BankAccount searchBankAccount(long accountId) throws BankAccountIdNotFoundException {
		BankAccount account = bankAccountDao.searchBankAccount(accountId);
		if(account == null)
			throw new BankAccountIdNotFoundException("Account Id not found");
		return account;
	}

	@Override
	public boolean updateBankAccountDetails(long accountId, String accountName, String accountType) {
		boolean result = bankAccountDao.updateBankAccountDetails(accountId, accountName, accountType);
		if(result)
			DbUtil.commit();
		return result;
	}

}
