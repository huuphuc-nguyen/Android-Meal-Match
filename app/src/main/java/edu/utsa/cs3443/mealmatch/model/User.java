package edu.utsa.cs3443.mealmatch.model;

public class User {
    private String email;
    private String password;
    private String firstname;
    private String lastname;

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }
}
