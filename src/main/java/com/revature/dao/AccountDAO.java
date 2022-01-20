package com.revature.dao;

import com.revature.models.accounts.*;
import com.revature.models.users.*;
import com.revature.models.util.ConnectionUtil;
import com.revature.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;

public class AccountDAO {

    private static Logger log = LoggerFactory.getLogger(AccountDAO.class); //Do all classes get their own logger?
    private UserDAO userDAO = new UserDAO();

    public AccountDAO () { super();}

    //GET FUNCTIONS
    //General
    public ArrayList<Account> getAllAccountsDAO() {
        //used by admins to view all users in the database
        try (Connection conn = ConnectionUtil.getConnection()) {
            //Since each employee has a list of customers associated with them, we don't need to actually query the
            //customer table in our original call to the database.
            String sql = "SELECT * FROM accounts;";

            //String sql = "SELECT * FROM customers;";
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);

            //create array list to return user objects
            ArrayList <Account> accounts = new ArrayList<>();

            log.info("Succesfully queried database for accounts");

            while(result.next()) {
                //log.info("Currently inside the UserDAO db string."); //if this doesn't print out, something was wrong with the command passed to SQL
                //first we need to see what kind of user it is
                Account account;

                if (result.getString("account_type").equals("Checking")) {
                    account = new CheckingAccount();
                    account.accountType = "Checking";
                }
                else {
                    account = new SavingAccount();
                    account.accountType = "Saving";
                }
                account.accountNumber = result.getInt("account_number");
                account.accountValue = result.getDouble("account_amount");
                account.accountOwner = result.getString("account_owner");
                accounts.add(account); //upcast the admin to a generic User
                //TODO: If I end up adding more account types add them here
            }

            //if no username match was found then return null
            return accounts;

        } catch (SQLException e) {
            log.info(e.toString());
            return null;
        }
    }
    public Account getAccount(int accountNumber) {
        //returns the info for a single account, if it exists
        try (Connection conn = ConnectionUtil.getConnection()) {
            //Since each employee has a list of customers associated with them, we don't need to actually query the
            //customer table in our original call to the database.
            String sql = "SELECT * FROM accounts WHERE account_number = ?;";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, accountNumber);
            ResultSet result = statement.executeQuery(sql);

            while(result.next()) {
                Account account;
                if (result.getString("account_type").equals("Checking")) {
                    account = new CheckingAccount();
                    account.accountType = "Checking";
                }
                else {
                    account = new SavingAccount();
                    account.accountType = "Saving";
                }
                account.accountNumber = accountNumber;
                account.accountValue = result.getDouble("account_amount");
                account.accountOwner = result.getString("account_owner");
                return account;
            }

            //if no accounts match the account number, return null
            return null;

        } catch (SQLException e) {
            log.info(e.toString());
            return null;
        }
    }
    public ArrayList<Account> getEmployeeAccountsDAO(String employeeUsername) {
        //call the getCustomerAccountsDAO() function on the customers passed in the customer list

        ArrayList<Customer> customers = new ArrayList<>();
        ArrayList<Account> accounts = new ArrayList<>();

        try (Connection conn = ConnectionUtil.getConnection()) {
            //we need to establish a connection to first get the appropriate customers
            customers = userDAO.getEmployeeCustomers(conn, employeeUsername);
        } catch (SQLException e) {
            log.info(e.toString());
            return null;
        }

        for (int i = 0; i < customers.size(); i++) {
            log.info("Current customer name is: " + customers.get(i).username);
            accounts.addAll(getCustomerAccountsDAO(customers.get(i).username)); //add all accounts from the employee at current index
        }

        return accounts;
    }
    public ArrayList<Account> getCustomerAccountsDAO(String customerUsername) {
        //used by admins to view all users in the database
        try (Connection conn = ConnectionUtil.getConnection()) {
            //Since each employee has a list of customers associated with them, we don't need to actually query the
            //customer table in our original call to the database.
            String sql = "SELECT * FROM accounts WHERE account_owner = ?;";

            //String sql = "SELECT * FROM customers;";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, customerUsername);

            ResultSet result = statement.executeQuery();

            //create array list to return user objects
            ArrayList <Account> accounts = new ArrayList<>();

            log.info("Succesfully queried database for accounts");

            while(result.next()) {
                //log.info("Currently inside the UserDAO db string."); //if this doesn't print out, something was wrong with the command passed to SQL
                //first we need to see what kind of user it is
                Account account;

                if (result.getString("account_type").equals("Checking")) {
                    account = new CheckingAccount();
                    account.accountType = "Checking";
                }
                else {
                    account = new SavingAccount();
                    account.accountType = "Saving";
                }
                account.accountNumber = result.getInt("account_number");
                account.accountValue = result.getDouble("account_amount");
                account.accountOwner = result.getString("account_owner");
                accounts.add(account); //upcast the admin to a generic User
                //TODO: If I end up adding more account types add them here
            }

            //if no username match was found then return null
            return accounts;

        } catch (SQLException e) {
            log.info(e.toString());
            return null;
        }
    }
    public ArrayList<NewAccountRequest> getEmployeeAccountRequestsDAO(String employeeUsername) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            //first we need a list of all the employees users
            ArrayList<NewAccountRequest> newAccountRequests = new ArrayList<>();
            ArrayList<Customer> customers = userDAO.getEmployeeCustomers(conn, employeeUsername);

            for (int i = 0; i < customers.size(); i++) {
                Customer cust = customers.get(i);

                String sql = "SELECT * FROM account_requests WHERE account_requester = ?;";

                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, cust.username);

                ResultSet result = statement.executeQuery();

                while(result.next()) {
                    //log.info("Currently inside the UserDAO db string."); //if this doesn't print out, something was wrong with the command passed to SQL
                    //first we need to see what kind of user it is
                    NewAccountRequest nar = new NewAccountRequest();
                    nar.requestNumber = result.getInt("account_request_num");
                    nar.customerName = cust.username;
                    nar.accountType = result.getString("account_type");
                    newAccountRequests.add(nar);
                }
            }
            return newAccountRequests;
        } catch (SQLException e) {
            log.info(e.toString());
            return null;
        }
    }
    public NewAccountRequest getAccountRequest(int requestNumber) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "SELECT * FROM account_requests WHERE account_request_num = ?;";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, requestNumber);

            ResultSet result = statement.executeQuery();

            while(result.next()) {
                //log.info("Currently inside the UserDAO db string."); //if this doesn't print out, something was wrong with the command passed to SQL
                //first we need to see what kind of user it is
                NewAccountRequest nar = new NewAccountRequest();
                nar.requestNumber = requestNumber;
                nar.customerName = result.getString("account_requester");
                nar.accountType = result.getString("account_type");
                return nar;
            }
            return null; //no account requests with this number were found
        } catch (SQLException e) {
            log.info(e.toString());
            return null;
        }
    }

    //ADD FUNCTIONS
    public boolean createNewAccountRequestDAO(NewAccountRequest nar) {
        //add the new account request to the appropriate table in the database
        log.info("create accountRequestDAO called");
        try (Connection conn = ConnectionUtil.getConnection()) {
            //adding a user will be handled differently depending on of it's a Customer of an Admin/Employee
            String sql;
            PreparedStatement statement;
            int locationCounter = 0;

            sql = "INSERT INTO account_requests (account_type, account_requester) " +
                    "VALUES (?, ?);";

            statement = conn.prepareStatement(sql);
            statement.setString(++locationCounter, nar.accountType);
            statement.setString(++locationCounter, nar.customerName);

            statement.execute();
            return true;
        } catch (SQLException e) {
            log.info(e.toString());
            return false;
        }
    }
    public void createNewAccount(NewAccountRequest nar) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            //TODO: I should probably use the accountFactory here, but ran out of time

            Account account;
            if (nar.accountType.equals("Checking")) account = new CheckingAccount();
            else account = new SavingAccount();
            //TODO: If I add more account types include them here

            String sql = "INSERT INTO accounts (account_type, account_amount, account_owner) VALUES (?, 0.0, ?);"; //accounts get created with no money in them
            PreparedStatement statement;

            int locationCounter = 0;
            statement = conn.prepareStatement(sql);
            statement.setString(++locationCounter, nar.accountType);
            statement.setString(++locationCounter, nar.customerName);

            statement.execute();
        } catch (SQLException e) {
            log.info(e.toString());
        }
    }

    //DELETE FUNCTION
    public void deleteAccountRequestDAO(int requestNumber) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            String sql = "DELETE FROM account_requests WHERE account_request_num = ?;";

            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, requestNumber);

            statement.execute();
        } catch (SQLException e) {
            log.info(e.toString());
        }
    }

    //EDIT FUNCTIONS
    public boolean editAccount(double newAmount, Account a) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            //first get the existing info on the User
            String sql = "UPDATE accounts SET account_amount = ? WHERE account_number = ?;";
            PreparedStatement statement = conn.prepareStatement(sql);

            int currentLocation = 0;
            statement.setDouble(++currentLocation, newAmount);
            statement.setInt(++currentLocation, a.accountNumber);
            statement.execute();

            return true; //updated without issue
        } catch (SQLException e) {
            log.info(e.toString());
            return false;
        }
    }
}
