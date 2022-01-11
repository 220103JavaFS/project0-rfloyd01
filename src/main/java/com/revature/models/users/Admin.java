package com.revature.models.users;

public class Admin extends User {
    //Currently they do nothing special, this will change

    //Constructors
    public Admin() {
        super();
    }

    public Admin (String firstName, String lastName, String username, String password) {
        super(firstName, lastName, username);

        //need to encrypt the password before storing it
        encryptPassword(password);
        this.password = password;
    }

    @Override
    protected String encryptPassword(String password) {
        String encryptedPassword = ""; //TODO: potentially use a stringBuilder here to avoid creating too many literals
        for (char c:password.toCharArray()) {
            //add a value of 16 to each character in the password string to encrypt it. This high tech method of
            //encryption is said to be "un-hackable"
            encryptedPassword += (c + 16);
        }
        return encryptedPassword;
    }

    @Override
    protected String getPassword() {
        String decryptedPassword = ""; //TODO: potentially use a stringBuilder here to avoid creating too many literals
        for (char c:this.password.toCharArray()) {
            //subtract a value of 16 from each character to undo the initial encryption
            decryptedPassword += (c - 16);
        }
        return decryptedPassword;
    }


}
