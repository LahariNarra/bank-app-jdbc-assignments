package com.capgemini.bankapp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.capgemini.bankapp.exception.BankAccountIdNotFoundException;
import com.capgemini.bankapp.exception.LowBalanceException;
import com.capgemini.bankapp.model.BankAccount;
import com.capgemini.bankapp.service.BankAccountService;
import com.capgemini.bankapp.service.impl.BankAccountServiceImpl;

public class BankAccountClient {

	static final Logger logger = Logger.getLogger(BankAccountClient.class);

	public static void main(String[] args) {
		int choice;
		long accountId;
		String accountHolderName;
		String accountType;
		double accountBalance;
		double amount;
		long fromAccountId;
		long toAccountId;
		double balance;
		BankAccountService bankAccountService = new BankAccountServiceImpl();
		try (BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in))) {
			while (true) {
				System.out.println("1.Open a new account\n2.Withdraw\n3.Deposit\n4.Fund Transfer");
				System.out.println("5.Check Balance\n6.Display all Bank Account Details");
				System.out.println("7.Search a paricular Bank account\n8.Delete Account\n9.Update Details\n10.Exit");

				System.out.println("Enter your choice");
				choice = Integer.parseInt(bReader.readLine());
				switch (choice) {
				case 1:
					System.out.println("Enter the account holder name");
					accountHolderName = bReader.readLine();
					System.out.println("Enter the account type");
					accountType = bReader.readLine();
					System.out.println("Enter the account balance");
					accountBalance = Double.parseDouble(bReader.readLine());
					BankAccount account = new BankAccount(accountHolderName, accountType, accountBalance);
					if (bankAccountService.addNewBankAccount(account))
						System.out.println("Account is created successfully!!");
					else
						System.out.println("Sorry, account is not created!");
					break;
				case 2:
					System.out.println("Enter your account number");
					accountId = Long.parseLong(bReader.readLine());
					System.out.println("Enter amount to be withdrawn");
					amount = Double.parseDouble(bReader.readLine());
					try {
						balance = bankAccountService.withdraw(accountId, amount);
						System.out.println("Amount Withdrawn " + amount + " from Account Id " + accountId);
						System.out.println("Balance ammount is" + balance);
					} catch (LowBalanceException | BankAccountIdNotFoundException e) {
						logger.error("Withdraw falied :", e);
					}

					break;
				case 3:
					System.out.println("Enter your account number");
					accountId = Long.parseLong(bReader.readLine());
					try {
						System.out.println("Enter amount to be deposited");
						amount = Double.parseDouble(bReader.readLine());
						balance = bankAccountService.deposit(accountId, amount);
						System.out.println("Amount Deposited " + amount + " from Account Id " + accountId);
						System.out.println("Total Balance amount is" + balance);
					} catch (BankAccountIdNotFoundException e) {
						logger.error("Exception:", e);
					}

					break;
				case 4:
					System.out.println("Enter your account number");
					fromAccountId = Long.parseLong(bReader.readLine());
					System.out.println("Enter account number whom who want to tarnsfer");
					toAccountId = Long.parseLong(bReader.readLine());
					System.out.println("Enter amount to be transfer");
					amount = Double.parseDouble(bReader.readLine());
					try {
						balance = bankAccountService.fundTransfer(fromAccountId, toAccountId, amount);
						System.out.println("Account Id " + fromAccountId + " has balance of "
								+ bankAccountService.checkBalance(fromAccountId));
						System.out.println("Account Id " + toAccountId + " has balance of "
								+ bankAccountService.checkBalance(toAccountId));
					} catch (LowBalanceException | BankAccountIdNotFoundException e) {
						logger.error("Exception :", e);
					}

					break;
				case 5:
					System.out.println("Enter your account number");
					accountId = Long.parseLong(bReader.readLine());
					try {
						System.out.println("Account Id " + accountId + " has balance of "
								+ bankAccountService.checkBalance(accountId));
					} catch (BankAccountIdNotFoundException e) {
						logger.error("Exception:", e);
					}

					break;
				case 6:
					List<BankAccount> bankAccount = new ArrayList<BankAccount>();
					bankAccount = bankAccountService.findAllBankAccount();
					for (BankAccount accounts : bankAccount) {
						System.out.println(accounts);
					}
					break;
				case 7:
					System.out.println("Enter the account number that you want to search");
					accountId = Long.parseLong(bReader.readLine());
					try {

						BankAccount bankAccounts = bankAccountService.searchBankAccount(accountId);
						System.out.println(bankAccounts);
					} catch (BankAccountIdNotFoundException e) {
						logger.error("Exception:", e);
					}
					break;
				case 8:
					System.out.println("Enter the account number that you want to delete");
					accountId = Long.parseLong(bReader.readLine());
					try {

						if (bankAccountService.deleteBankAccount(accountId))
							System.out.println("Account is deleted successfully!!");
						else
							System.out.println("Sorry, account is not deleted!");
					} catch (BankAccountIdNotFoundException e) {
						logger.error("Exception:", e);
					}

					break;
				case 9:
					System.out.println("Enter the account number that you want to update");
					accountId = Long.parseLong(bReader.readLine());
					try {
						System.out.println("Enter the name to be modified");
						accountHolderName = bReader.readLine();
						System.out.println("Enter the account type to be modified");
						accountType = bReader.readLine();
						if (bankAccountService.updateBankAccountDetails(accountId, accountHolderName, accountType))
							System.out.println("Account is updated successfully");
						else
							System.out.println("Account is not updated");
					} catch (BankAccountIdNotFoundException e) {
						logger.error("Exception:", e);
					}

					break;
				case 10:
					System.out.println("Thanks for using our Banking Services");
					System.exit(0);
				}

			}
		} catch (IOException e) {
			logger.error("Exception :", e);
		}
	}

}
