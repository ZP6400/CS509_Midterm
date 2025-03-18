import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomerService {

    //The DatabaseManager object is initialized as an attribute of this class. It will be used to
    //access the database of accounts in order to make changes to certain columns
    private final DatabaseManager db_manager;

    //The constructor for the CustomerService class; it requires a database manager be provided, rather
    //than initializing one within said constructor
    public CustomerService(DatabaseManager db_manager) {

        this.db_manager = db_manager;
    }

    public boolean withdrawCash(Customer customer, int amount) {

        //The account of the customer is retrieved via the getAccount() function from the Customer class
        Account account = customer.getAccount();

        //If the account is equal to null, this means it does not exist and therefore cash can not
        //be withdrawn from it. The user is informed of this and 'false' is returned
        if (account == null) {

            System.out.println("Error: Account not found.");
            return false;
        }

        //If the account exists (is not null) and the amount provided can safely be withdrawn:
        if (account.withdraw(amount)) {

            //The db_manager updates the account balance
            db_manager.updateAccountBalance(account.getAccountNumber(), account.getBalance());

            //The local data and time are initialized using a class and function provided by the
            //packages imported at the very beginning of this class
            LocalDateTime current_date = LocalDateTime.now();

            //The formatter object is used here to ensure that the way in which the date is
            //portrayed is in mm/dd/yyyy format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String formatted_date = current_date.format(formatter);

            //The user is informed that cash was successfully withdrawn. Other relevant
            //data is printed out to the terminal as well and 'true' is returned
            System.out.println("Cash Successfully Withdrawn.");
            System.out.println("Account #" + account.getAccountNumber());
            System.out.println("Date: " + formatted_date);
            System.out.println("Withdrawn: " + amount);
            System.out.println("Balance: " + account.getBalance());
            return true;
        }
        else {

            //If the amount provided cannot be safely withdrawn, the user is informed of this and
            //'false' is returned
            System.out.println("Withdrawal Amount Exceeds Current Balance Please try again.\n");
            return false;
        }
    }

    public void depositCash(Customer customer, int amount) {

        //The account of the customer is retrieved via the getAccount() function from the Customer class
        Account account = customer.getAccount();

        //If the account is not equal to null, meaning it exists:
        if (account != null) {

            //The deposit function is called to update the current state of the account. The updateAccountBalance
            //for db_manager is then called to ensure that the database is up to date
            account.deposit(amount);
            db_manager.updateAccountBalance(account.getAccountNumber(), account.getBalance());

            //The local date and time are retrieved and initialized
            LocalDateTime current_date = LocalDateTime.now();

            //The current date is formatted to meet the mm/dd/yyyy standard
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String formatted_date = current_date.format(formatter);

            //The user is informed that cash was successfully deposited. Other relevant
            //data is printed out to the terminal as well
            System.out.println("Cash Deposited Successfully.");
            System.out.println("Account #" + account.getAccountNumber());
            System.out.println("Date: " + formatted_date);
            System.out.println("Deposited: " + amount);
            System.out.println("Balance: " + account.getBalance());
        }
    }

    public void displayBalance(Customer customer) {

        //The account of the customer is retrieved via the getAccount() function from the Customer class
        Account account = customer.getAccount();

        //If the account is not equal to null, meaning it exists:
        if (account != null) {

            //The local date and time are retrieved and initialized
            LocalDateTime current_date = LocalDateTime.now();

            //The current date is formatted to meet the mm/dd/yyyy standard
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String formatted_date = current_date.format(formatter);

            //Finally, the balance is printed to the terminal using the getBalance() function.
            //Other relevant data is printed out as well
            System.out.println("Account #" + account.getAccountNumber());
            System.out.println("Date: " + formatted_date);
            System.out.println("Balance: " + account.getBalance());
        }
    }
}