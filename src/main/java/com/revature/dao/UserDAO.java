package com.revature.dao;

import com.revature.models.accounts.Account;
import com.revature.models.accounts.CheckingAccount;
import com.revature.models.accounts.SavingAccount;
import com.revature.models.users.*;
import com.revature.models.util.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class UserDAO {

    //private static ArrayList<User> users;
    private static Logger log = LoggerFactory.getLogger(UserDAO.class); //Do all classes get their own logger?

    public UserDAO () { super();}

    //GET FUNCTIONS
    //General
    public static User getBasicUserInformationDAO(String userName) {

        try (Connection conn = ConnectionUtil.getConnection()) {
            //create a union between the Admin, Employee and Customer tables when searching for a username.
            String sql = "SELECT first_name, last_name, username, encrypted_password, user_type FROM admins WHERE username = ? UNION " +
                    "SELECT first_name, last_name, username, encrypted_password, user_type FROM employees WHERE username = ? UNION " +
                    "SELECT first_name, last_name, username, encrypted_password, user_type FROM customers WHERE username = ?;"; //we need statement protection because id could be a SQL command by mistake

            PreparedStatement statement = conn.prepareStatement(sql);

            //fill in the ?'s above using the passed userName
            int statementCounter = 0;
            statement.setString(++statementCounter, userName);
            statement.setString(++statementCounter, userName);
            statement.setString(++statementCounter, userName);
            //Statement statement = conn.createStatement();

            //desired user information will be stored in the "user" variable
            User user;

            ResultSet result = statement.executeQuery();

            //TODO: Currently I have this in a while loop but there should only every be a single hit here.
            //  Recode this if I get the time.
            while(result.next()) {
                log.info("Currently inside the UserDAO db string."); //if this doesn't print out, something was wrong with the command passed to SQL

                //first we need to see what kind of user it is
                if (result.getString("user_type").equals("Admin")) user = new Admin();
                else if (result.getString("user_type").equals("Employee")) user = new Employee();
                else user = new Customer();

                //then we take the information from the databse
                user.firstName = result.getString("first_name");
                user.lastName = result.getString("last_name");
                user.username = result.getString("username");
                user.password = result.getString("encrypted_password");
                user.userType = result.getString("user_type");

                //log.info("UserDAO GetBasicUserInfo(): encrypted password is: " + user.password);

                return user;
            }

            //if no username match was found then return null
            log.info("No users with that username found, returning null value");
            return null;

        } catch (SQLException e) {
            log.info(e.toString());
            return null;
        }
    }
    public ArrayList<User> getAllUsersDAO() {
        //used by admins to view all users in the database
        try (Connection conn = ConnectionUtil.getConnection()) {
            log.info("UserDAO getAllUsersDAO() method was called");
            //Since each employee has a list of customers associated with them, we don't need to actually query the
            //customer table in our original call to the database.
            String sql = "SELECT first_name, last_name, username, encrypted_password, user_type FROM admins UNION " +
                    "SELECT first_name, last_name, username, encrypted_password, user_type FROM employees;";

            //String sql = "SELECT * FROM customers;";
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);

            //create array list to return user objects
            ArrayList <User> users = new ArrayList<>();

            while(result.next()) {
                //log.info("Currently inside the UserDAO db string."); //if this doesn't print out, something was wrong with the command passed to SQL
                //first we need to see what kind of user it is
                if (result.getString("user_type").equals("Admin")) {
                    Admin admin = new Admin();
                    admin.firstName = result.getString("first_name");
                    admin.lastName = result.getString("last_name");
                    admin.username = result.getString("username");
                    admin.password = result.getString("encrypted_password");
                    admin.userType = result.getString("user_Type");
                    users.add(admin); //upcast the admin to a generic User
                }
                else {
                    Employee emp = new Employee();
                    emp.firstName = result.getString("first_name");
                    emp.lastName = result.getString("last_name");
                    emp.username = result.getString("username");
                    emp.password = result.getString("encrypted_password");
                    emp.userType = result.getString("user_Type");
                    emp.setAssignedCustomers(getEmployeeCustomers(conn, emp.username));
                    users.add(emp); //upcast the employee to a generic User
                }
            }

            //if no username match was found then return null
            return users;

        } catch (SQLException e) {
            log.info(e.toString());
            return null;
        }
    }

    //Customer Related
    public ArrayList<Account> getCustomerAccounts(Connection conn, String username) {
        //An overloaded version of the getCustomerAccounts() function. Uses an existing connection to
        //create another query while currently connected to the database. Returns all of the accounts owned
        //by the Customer passed to the function
        try {
            log.info("UserDAO getCustomerAccounts() method was called");
            ArrayList<Account> accounts = new ArrayList<>(); //first create the list to return

            String sql = "SELECT account_number, account_type, account_amount FROM accounts WHERE account_owner = ?;"; //statement protection needed
            PreparedStatement statement = conn.prepareStatement(sql);

            //fill in the ?'s above using the passed userName
            int statementCounter = 0;
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                Account currentAccount;

                //instantiate the account based on the type of account it is
                if (result.getString("account_type").equals("Saving")) currentAccount = new SavingAccount();
                else currentAccount = new CheckingAccount(); //TODO: If more account types are created add them to if else statement here

                //then we take the information from the databse
                currentAccount.accountNumber = result.getInt("account_number");
                currentAccount.accountType = result.getString("account_type");
                currentAccount.accountValue = result.getDouble("account_amount");

                //log.info("UserDAO GetBasicUserInfo(): encrypted password is: " + user.password);

                accounts.add(currentAccount);
            }
            return accounts;
        } catch (SQLException e) {
            log.info(e.toString());
            return null;
        }
    }
    public Customer getCustomerDAO(String customerUsername) {
        try (Connection conn = ConnectionUtil.getConnection()) {

            //first we need to make a query to get the basic info about the employee
            Customer cust = null; //new Customer();

            //statement protection needed
            String sql = "SELECT first_name, last_name, username, encrypted_password, user_type, assigned_employee FROM customers WHERE username = ?;";
            PreparedStatement statement = conn.prepareStatement(sql);

            //fill in the ?'s above using the passed userName
            statement.setString(1, customerUsername);
            ResultSet result = statement.executeQuery();

            //TODO:There should only be a single employee found here, don't need a while loop
            while(result.next()) {
                cust = new Customer();
                cust.firstName = result.getString("first_name");
                cust.lastName = result.getString("last_name");
                cust.username = result.getString("username");
                cust.password = result.getString("encrypted_password");
                cust.userType = result.getString("user_type");
                cust.setAssignedEmployee(result.getString("assigned_employee"));
                cust.setActiveAccounts(getCustomerAccounts(conn, customerUsername));
            }
            return cust; //if no customers with the given username exist in the database, return null
        } catch (SQLException e) {
            log.info(e.toString());
            return null;
        }
    }
    public ArrayList<Customer> getEmployeeCustomers(Connection conn, String userName) {
        //An overloaded version of the standard getEmployee() function
        //utilizes an existing connection to create another query. Employees have a list of customers that we need
        //to access that differentiates them from a standard user
        try {
            log.info("UserDAO getEmployeeCustomers() method was called");
            String sql = "SELECT first_name, last_name, username, encrypted_password, user_type FROM customers WHERE assigned_employee = ?;";
            PreparedStatement statement = conn.prepareStatement(sql);
            ArrayList<Customer> customers = new ArrayList<>();

            //fill in the ?'s above using the passed userName
            statement.setString(1, userName);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                Customer customer = new Customer();
                customer.firstName = result.getString("first_name");
                customer.lastName = result.getString("last_name");
                customer.username = result.getString("username");
                customer.password = result.getString("encrypted_password");
                customer.userType = result.getString("user_Type");
                customer.setAssignedEmployee(userName);
                customer.setActiveAccounts(getCustomerAccounts(conn, customer.username)); //query the accounts database to find all accounts tied to this customer
                customers.add(customer); //add this customer to the employees list
            }
            return customers;
        } catch (SQLException e) {
            log.info(e.toString());
            return null;
        }
    }

    //Employee Related
    public ArrayList<Employee> getAllEmployeesDAO() {
        //used by admins to view all users in the database
        try (Connection conn = ConnectionUtil.getConnection()) {
            log.info("UserDAO getAllUsersDAO() method was called");
            //Since each employee has a list of customers associated with them, we don't need to actually query the
            //customer table in our original call to the database.
            String sql = "SELECT * FROM employees;";

            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);

            //create array list to return user objects
            ArrayList <Employee> employees = new ArrayList<>();

            while(result.next()) {
                //log.info("Currently inside the UserDAO db string."); //if this doesn't print out, something was wrong with the command passed to SQL
                //first we need to see what kind of user it is
                Employee emp = new Employee();
                emp.firstName = result.getString("first_name");
                emp.lastName = result.getString("last_name");
                emp.username = result.getString("username");
                emp.password = result.getString("encrypted_password");
                emp.userType = result.getString("user_Type");
                emp.setAssignedCustomers(getEmployeeCustomers(conn, emp.username));
                employees.add(emp); //upcast the employee to a generic User
            }

            return employees;

        } catch (SQLException e) {
            log.info(e.toString());
            return null;
        }
    }
    public Employee getEmployeeDAO(String userName) {
        //utilizes an existing connection to create another query. Employees have a list of customers that we need
        //to access that differentiates them from a standard user
        try (Connection conn = ConnectionUtil.getConnection()) {

            //first we need to make a query to get the basic info about the employee
            Employee emp;// = new Employee();

            //statement protection needed
            String sql = "SELECT first_name, last_name, username, encrypted_password, user_type FROM employees WHERE username = ?;";
            PreparedStatement statement = conn.prepareStatement(sql);

            //fill in the ?'s above using the passed userName
            statement.setString(1, userName);
            ResultSet result = statement.executeQuery();

            //TODO:There should only be a single employee found here, don't need a while loop
            while(result.next()) {
                emp = new Employee();
                emp.firstName = result.getString("first_name");
                emp.lastName = result.getString("last_name");
                emp.username = result.getString("username");
                emp.password = result.getString("encrypted_password");
                emp.userType = result.getString("user_Type");
                emp.setAssignedCustomers(getEmployeeCustomers(conn, emp.username)); //find customers with another function
                return emp;
            }
            return null; //if no employee in the database matches the given username, return null
        } catch (SQLException e) {
            log.info(e.toString());
            return null;
        }
    }

    //ADD FUNCTIONS
    public int addUser(UserRequest u) {
        //TODO: If I get time, encryption should really be happening in the Service layer, not down here
        try {
            //first, invoke the UserFactory to create a user with the given information.
            //the information should all be valid to have reached this method so this is
            //safe
            UserFactory uf = UserFactory.getFactory();
            User newUser = uf.makeUser(u);

            //connect to the database
            try (Connection conn = ConnectionUtil.getConnection()) {
                //adding a user will be handled differently depending on of it's a Customer of an Admin/Employee
                String sql;
                PreparedStatement statement;
                int locationCounter = 0;

                if (u.userType.equals("Customer")) {

//                    String yeet = "INSERT INTO homes (home_name, home_str_num, home_str, home_city, home_region, home_zip, home_country) " +
//                            "VALUES (?, ?, ?, ?, ?, ?, ?);"; //don't update residents here as that is covered by our internal trigger

                    sql = "INSERT INTO customers (first_name, last_name, username, encrypted_password, user_type, assigned_employee) " +
                            "VALUES (?, ?, ?, ?, ?, ?);";

                    Customer cust = (Customer) newUser; //cast the user created in the factory to a customer
                    log.info("Original Password passed to Customer create in the DAOLayer is: " + u.password);
                    String encryptedPassword = cust.encryptPassword(u.password); //make sure to encrypt password before storing
                    log.info("Ecrypted Customer password is:" + encryptedPassword);

                    statement = conn.prepareStatement(sql);
                    statement.setString(++locationCounter, cust.firstName);
                    statement.setString(++locationCounter, cust.lastName);
                    statement.setString(++locationCounter, cust.username);
                    statement.setString(++locationCounter, encryptedPassword);
                    statement.setString(++locationCounter, cust.userType);
                    statement.setString(++locationCounter, cust.getAssignedEmployee());

                    //log.info("Tried to create Customer in UserDAO with the following parameters: " + cust.toString());
                }
                else {
                    log.info("Original Password passed to Admin/Employee create in the DAOLayer is: " + u.password);
                    if (u.userType.equals("Employee")) sql = "INSERT INTO employees ";
                    else sql = "INSERT INTO admins ";
                    sql += "(first_name, last_name, username, user_type, encrypted_password) VALUES " +
                            "(?, ?, ?, ?, ?);";

                    locationCounter = 0; //don't start at the table value as it will be handled later

                    statement = conn.prepareStatement(sql);
                    statement.setString(++locationCounter, newUser.firstName);
                    statement.setString(++locationCounter, newUser.lastName);
                    statement.setString(++locationCounter, newUser.username);
                    statement.setString(++locationCounter, newUser.userType);
                    //statement.setString(++locationCounter, newUser.password);

                    //password is encrypted differently depending on if the new user is an employee or an admin
                    if (u.userType.equals("Employee")) {
                        Employee emp = (Employee) newUser;
                        String encryptedPassword = emp.encryptPassword(u.password);
                        log.info("Ecrypted Employee password is:" + encryptedPassword);
//                        log.info("Originally passed password was : " + emp.password);
//                        emp.password = emp.encryptPassword(emp.password);
//                        log.info("The encrypted password is: " + emp.password );
                        statement.setString(++locationCounter, encryptedPassword);

                    }
                    else {
                        Admin adm = (Admin) newUser;
                        String encryptedPassword = adm.encryptPassword(u.password);
                        log.info("Ecrypted Admin password is:" + encryptedPassword);
                        statement.setString(++locationCounter, encryptedPassword);
                    }
                }

                log.info("Here's the statement being executed in SQL: " + statement.toString());
                statement.execute();
            } catch (SQLException e) {
                log.info(e.toString());
                return 0b10000000; //this will indicate there was a server side issue
            }
            return 0; //if add works then return 0 error code
        }
        catch (Exception e) {
            //I'm not entirely sure what would cause a failure here, but put the creation of a new
            //user into a try/catch to be safe
            return 0b10000000;
        }
    }

    //UPDATE FUNCTIONS
    public boolean updateUser(UserRequest newInformation, String existingUsername) {
        //iterate through the users until we find the one who needs to have their info updated. Searching by the
        //username in the UserRequest won't work because it's possible the name isn't in the database yet. For this
        //reason we also need to pass the existing username, so we can actually find the entry
//        log.info("updateUserDAO() function called.");
        try (Connection conn = ConnectionUtil.getConnection()) {
            //first get the existing info on the User
            User u = getBasicUserInformationDAO(existingUsername);
            String sql = "UPDATE ";
            String encryptedPassword;

            if (u.userType.equals("Admin")) {
                sql += "admins ";
                Admin adm = new Admin();
                encryptedPassword = adm.encryptPassword(newInformation.password);
            } else if (u.userType.equals("Employee")) {
                sql += "employees ";
                Employee emp = new Employee();
                encryptedPassword = emp.encryptPassword(newInformation.password);
            } else {
                sql += "customers ";
                Customer cust = new Customer();
                encryptedPassword = cust.encryptPassword(newInformation.password);
            }

            log.info("Original Password passed to user update was: " + newInformation.password);
            log.info("Encrypted Password is : " + encryptedPassword);
            sql += "SET first_name = ?, last_name = ?, username = ?, encrypted_password = ? WHERE username = ?;";

            PreparedStatement statement = conn.prepareStatement(sql);
            int currentLocation = 0;
            statement.setString(++currentLocation, newInformation.firstName);
            statement.setString(++currentLocation, newInformation.lastName);
            statement.setString(++currentLocation, newInformation.username);
            statement.setString(++currentLocation, encryptedPassword);
            statement.setString(++currentLocation, existingUsername);

            statement.execute();
            return true; //updated without issue
        } catch (SQLException e) {
            log.info(e.toString());
            return false;
        }
    }

    //OTHER FUNCTIONS
    public boolean validUsernameDAO(String username) {
        //this function scans the user database to see if the passed username already exists or not
        //log.info("username passed to the DAO layer is: " + username);
        try (Connection conn = ConnectionUtil.getConnection()) {
            //check the usernames of all users in the database to make sure that the passed username isn't alread taken
            String sql = "SELECT username FROM admins WHERE username = ? UNION " +
                    "SELECT username FROM employees WHERE username = ? UNION " +
                    "SELECT username FROM customers WHERE username = ?;";

            //String sql = "SELECT * FROM customers;";
            PreparedStatement statement = conn.prepareStatement(sql);
            int currentLocation = 0;
            statement.setString(++currentLocation, username);
            statement.setString(++currentLocation, username);
            statement.setString(++currentLocation, username);

            ResultSet result = statement.executeQuery();
            while(result.next()) {
                //if a username is actually found it means that the username is taken so we can't use it
                return false;
            }
            return true; //no username was found so the passed username is available
        } catch (SQLException e) {
            log.info(e.toString());
            return false;
        }
    }
}
