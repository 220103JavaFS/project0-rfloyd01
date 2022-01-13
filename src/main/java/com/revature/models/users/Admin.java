package com.revature.models.users;

public class Admin extends User {
    //Currently they do nothing special, this will change

    //Constructors
    public Admin() {
        super();
    }

    public Admin (String userType, String firstName, String lastName, String username, String password) {
        super(userType, firstName, lastName, username);

        //need to encrypt the password before storing it
        this.password = encryptPassword(password);
    }

    @Override
    protected String encryptPassword(String password) {
        StringBuilder encryptedPassword = new StringBuilder(password); //use string builder to build one char at a time
        char newChar;

        for (int i = 0; i < encryptedPassword.length(); i++) {
            //add a value of 16 to each character in the password string to encrypt it. This high tech method of
            //encryption is said to be "un-hackable"
            newChar = encryptedPassword.charAt(i);
            newChar += 16;
            encryptedPassword.setCharAt(i, newChar);
        }
        log.info("Encrypted password for Admin: " + this.firstName + " " + this.lastName + " is " + encryptedPassword.toString());
        return encryptedPassword.toString();
    }

    @Override
    protected String getPassword() {
        StringBuilder decryptedPassword = new StringBuilder(this.password); //use string builder to build one char at a time
        char newChar;

        for (int i = 0; i < decryptedPassword.length(); i++) {
            //remove 16 from each character in the password to decrypt it
            newChar = decryptedPassword.charAt(i);
            newChar -= 16;
            decryptedPassword.setCharAt(i, newChar);
        }
        log.info("Actual password for Admin: " + this.firstName + " " + this.lastName + " is " + decryptedPassword.toString());
        return decryptedPassword.toString();
    }
}
