//Abstract base class for Users (Customers and Administrators extend from this)
public abstract class User {

    //The login and pin_code fields of the User class are protected, so that they can
    //be extended down to its subclasses
    protected String login;
    protected String pin_code; // 5-digit string

    //The constructor for the User class
    public User(String login, String pin_code) {

        this.login = login;
        this.pin_code = pin_code;
    }

    public String getLogin() {

        return login;
    }

    public String getPin() {

        return pin_code;
    }
}

