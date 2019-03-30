package com.capgemini.bankapp.service;

import java.util.List;

import com.capgemini.bankapp.exception.BankAccountIdNotFoundException;
import com.capgemini.bankapp.exception.LowBalanceException;
import com.capgemini.bankapp.model.BankAccount;

public interface BankAccountService {

	public double checkBalance(long accountId)throws BankAccountIdNotFoundException;

	public double withdraw(long accountId, double amount) throws LowBalanceException,BankAccountIdNotFoundException;

	public double deposit(long accountId, double amount)throws BankAccountIdNotFoundException;

	public boolean deleteBankAccount(long accountId) throws BankAccountIdNotFoundException;

	public double fundTransfer(long fromAccount, long toAccount, double amount) throws LowBalanceException,BankAccountIdNotFoundException;

	public boolean addNewBankAccount(BankAccount account);

	public List<BankAccount> findAllBankAccount();

	public BankAccount searchBankAccount(long accountId)throws BankAccountIdNotFoundException;
	
	public boolean updateBankAccountDetails(long accountId,String accountName,String accountType) throws BankAccountIdNotFoundException;

}
