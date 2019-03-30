package com.capgemini.bankapp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.bankapp.dao.BankAccountDao;
import com.capgemini.bankapp.model.BankAccount;
import com.capgemini.bankapp.util.DbUtil;

public class BankAccountDaoImpl implements BankAccountDao {

	@Override
	public double getBalance(long accountId) {
		String query = "Select account_balance From bankaccounts Where account_id=" + accountId;
		double balance = -1;
		Connection connection = DbUtil.getConnection();

		try (PreparedStatement statement = connection.prepareStatement(query);
				ResultSet result = statement.executeQuery()) {
			result.next();
			balance = result.getDouble(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return balance;

	}

	@Override
	public void updateBalance(long accountId, double newBalance) {
		String query = "Update bankaccounts SET account_balance =? where account_id = ?";
		Connection connection = DbUtil.getConnection();
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setDouble(1, newBalance);
			statement.setLong(2, accountId);
			int result = statement.executeUpdate();
			System.out.println("No. of rows updated:" + result);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean deleteBankAccount(long accountId) {
		String query = "Delete From bankaccounts Where account_id=" + accountId;
		int result;
		Connection connection = DbUtil.getConnection();
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			result = statement.executeUpdate();
			if (result == 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean addNewBankAccount(BankAccount account) {
		String query = "Insert Into bankaccounts(customer_name,account_type,account_balance) Values(?,?,?)";
		Connection connection = DbUtil.getConnection();
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, account.getAccountName());
			statement.setString(2, account.getAccountType());
			statement.setDouble(3, account.getAccountBalance());
			int result = statement.executeUpdate();
			if (result == 1)
				return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<BankAccount> findAllBankAccounts() {
		String query = "Select * From bankaccounts";
		List<BankAccount> accounts = new ArrayList<BankAccount>();
		Connection connection = DbUtil.getConnection();
		try (PreparedStatement statement = connection.prepareStatement(query);
				ResultSet result = statement.executeQuery()) {
			while (result.next()) {
				long accountId = result.getLong(1);
				String accountName = result.getString(2);
				String accountType = result.getString(3);
				double accountBalance = result.getDouble(4);
				BankAccount account = new BankAccount(accountId, accountName, accountType, accountBalance);
				accounts.add(account);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accounts;
	}

	@Override
	public BankAccount searchBankAccount(long accountId) {
		String query = "Select * from bankaccounts where account_id=" + accountId;
		BankAccount accountDetails = null;
		Connection connection = DbUtil.getConnection();
		try (PreparedStatement statement = connection.prepareStatement(query);
				ResultSet result = statement.executeQuery()) {
			if (result.next())
				accountDetails = new BankAccount(result.getLong(1), result.getString(2), result.getString(3),
						result.getDouble(4));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accountDetails;

	}

	@Override
	public boolean updateBankAccountDetails(long accountId, String accountName, String accountType) {
		String query = "Update bankaccounts Set customer_name = ?,account_type = ? Where account_id=? ";
		Connection connection = DbUtil.getConnection();
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, accountName);
			statement.setString(2, accountType);
			statement.setLong(3, accountId);
			int result = statement.executeUpdate();
			if (result == 1)
				return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
