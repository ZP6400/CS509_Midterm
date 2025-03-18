import java.util.Scanner;

public class AdminService {

    //The DatabaseManager object is initialized as an attribute of this class. It will be used to
    //access the database of accounts in order to make changes to certain rows or columns
    private final DatabaseManager db_manager;

    //The constructor for the AdminService class; it requires a database manager be provided, rather
    //than initializing one within said constructor
    public AdminService(DatabaseManager db_manager) {

        this.db_manager = db_manager;
    }

    public void createAccount(String login, String pin, String holder, int starting_balance, boolean is_active) {

        //Initialize the necessary status string variable
        String status;

        //If the boolean passed through is true:
        if (is_active) {

            //The string is adjusted to "Active"
            status = "Active";
        }
        else {

            //Otherwise, the string is set to "Disabled"
            status = "Disabled";
        }

        //Now that the status has been turned into a string, the following parameters can be passed
        //through to the DatabaseManager's createNewAccount function. The result is returned
        //and stored in the account_num int
        int account_num = db_manager.createNewAccount(login, pin, holder, starting_balance, status);

        //If the account number is equal to -2:
        if (account_num == -2) {

            //A specific error occurred in which a duplicate entry was trying to be made. The user
            //is notified of this error
            System.out.println("Account Creation Failed - Duplicate Entry.");
        }
        //If the account number is equal to -1:
        else if (account_num == -1) {

            //A different kind of error occurred; the user is notified of this as well
            System.out.println("Account Creation Failed - An Error Occurred.");
        }
        //In the event that no error occurs:
        else {

            //The user is notified of the account that was successfully created and the number that
            //was assigned to it
            System.out.println("Account Successfully Created – the account number assigned is: " + account_num);
        }
    }

    public void deleteAccount(int account_num, Scanner scanner) {

        //Get a hold of the account that is being deleted, based on the account number that was provided
        Account account = db_manager.getAccount(account_num);

        //If the account is not equal to null, meaning it exists:
        if (account != null) {

            //The user is notified of the account they wish to delete and are asked to confirm
            //that they are certain of this action
            System.out.print("You wish to delete the account held by " + account.getHolderName() +
                    ". If this information is correct, please re-enter the account number: ");

            //While the user has yet to input an integer:
            while (!scanner.hasNextInt()) {

                //The user is notified that an invalid input has been provided
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();

                System.out.print("If this information is correct, please re-enter the account number: ");
            }

            //The next int input is stored in a confirmation variable
            int confirmation = scanner.nextInt();

            //If the confirmation value is equal to the account number:
            if (confirmation == account_num) {

                //The deleteAccount() function of the DatabaseManager class is called
                db_manager.deleteAccount(account_num);

                //The user is notified that the deletion occurred successfully
                System.out.println("Account Deleted Successfully.");
            }
            else {

                //If the account numbers don't match, the user is informed and the deletion is cancelled
                System.out.println("Input does not match the account number. Deletion cancelled.");
            }
        }
        else {

            //If the account is equal to null, that means the account with the account number provided by the
            //user is not in the database. The user is informed of this
            System.out.println("An Account with this account number does not exist.");
        }
    }

    public void updateAccount(int account_num, String new_holder, String new_status, String new_login, String new_pin) {

        //The updateAccountInfo() function is called, with the same parameters provided to updateAccount()
        //provided to this function
        db_manager.updateAccountInfo(account_num, new_holder, new_status, new_login, new_pin);
    }

    public void searchAccount(int account_num) {

        //Get a hold of the account that is being searched, based on the account number that was provided
        Account account = db_manager.getAccount(account_num);

        //If the account is not equal to null, meaning it exists:
        if (account != null) {

            //Getters for only the relevant information that is to be printed is returned and sent
            //out to the terminal
            System.out.println("Account # " + account.getAccountNumber());
            System.out.println("Holder: " + account.getHolderName());
            System.out.println("Balance: " + account.getBalance());
            System.out.println("Status: " + account.getStatus());

            User user = db_manager.getUser(account_num);

            System.out.println("Login: " + user.getLogin());
            System.out.println("Pin Code: " + user.getPin());
        }
        else {

            //If the account is equal to null, that means the account with the account number provided by the
            //user is not in the database. The user is informed of this
            System.out.println("An Account with this account number does not exist.");
        }
    }
}
