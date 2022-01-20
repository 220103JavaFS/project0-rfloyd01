package com.revature.models.users;

public class Admin extends User {
    //Currently they do nothing special, this will change

    //Constructors
    public Admin() {
        super();
    }

    public Admin (String userType, String firstName, String lastName, String username, String password) {
        super(userType, firstName, lastName, username, password);
    }

    @Override
    public String encryptPassword(String password) {
        StringBuilder encryptedPassword = new StringBuilder(password); //use string builder to build one char at a time
        char newChar;

        for (int i = 0; i < encryptedPassword.length(); i++) {
            //subtracts a value of 2 from each character in the password string to encrypt it. This high tech method of
            //encryption is said to be "un-hackable"
            newChar = encryptedPassword.charAt(i);
            newChar -= 2;
            encryptedPassword.setCharAt(i, newChar);
        }
        //log.info("Encrypted password for Admin: " + this.firstName + " " + this.lastName + " is " + encryptedPassword.toString());
        return encryptedPassword.toString();
    }

//    @Override
//    public String getUnencryptedPassword() {
//        //used for debugging originally
//        return getPassword();
//    }

    @Override
    protected String getPassword() {
        StringBuilder decryptedPassword = new StringBuilder(this.password); //use string builder to build one char at a time
        char newChar;

        for (int i = 0; i < decryptedPassword.length(); i++) {
            //add 2 to each character in the password to decrypt it
            newChar = decryptedPassword.charAt(i);
            newChar += 2;
            decryptedPassword.setCharAt(i, newChar);
        }
        //log.info("Actual password for Admin: " + this.firstName + " " + this.lastName + " is " + decryptedPassword.toString());
        return decryptedPassword.toString();
    }

    public void assignCustomer(Employee emp, Customer cust) {
        //adds the customer to the employees customer list
        emp.addCustomer(cust);
    }
}
