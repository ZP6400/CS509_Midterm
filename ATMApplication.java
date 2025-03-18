import java.sql.SQLException;
import java.util.Scanner;

public class ATMApplication {


    public static void main(String[] args) {

        //Upon starting, a scanner object is initialized to take in user input
        Scanner scanner = new Scanner(System.in);

        //The following string variables are then initialized, as this is the information
        //that will be provided to the Database Manager object in order to connect to MySQL
        String db_url = "jdbc:mysql://localhost:3307/atm_db";
        String db_user = "root";
        String db_password = "Joyful#83900";

        try {

            //A Database Manager object is initialized with the necessary url, username,
            //and password parameters
            DatabaseManager db_manager = new DatabaseManager(db_url, db_user, db_password);

            //Initialize the CustomerService and AdminService objects by passing
            //the now initialized db_manager object to their constructors
            CustomerService customer_service = new CustomerService(db_manager);
            AdminService admin_service = new AdminService(db_manager);

            //Print a message to the terminal welcoming the user to the ATM System. Then, ask
            //for the necessary login credentials
            System.out.println("Welcome to the ATM System");

            //A user object is initialized as null
            User user = null;

            //While the user is still null:
            while (user == null) {

                //The user is prompted to enter their login and pin code information
                System.out.print("Enter Login: ");
                String login = scanner.next();
                System.out.print("Enter Pin Code: ");
                String pin = scanner.next();

                //Initialize a new user object with the provided username and pin as parameters
                user = db_manager.getUser(login, pin);

                //If the user is still null following this initialization:
                if (user == null) {

                    //The incorrect credentials were provided, and the user is notified
                    System.out.println("Invalid login or pin code. Please try again.\n");
                }
            }

            //Following the initialization of the user, if this user is an instance of a Customer:
            if (user instanceof Customer) {

                //Welcome the customer to the ATM service, then call the handleCustomerMenu() function
                System.out.println("\nLogin Successful! Welcome, Customer.");
                handleCustomerMenu((Customer) user, customer_service, scanner);
            }
            //If the user is instead an instance of an Admin:
            else if (user instanceof Administrator) {

                //Welcome the admin to the ATM service, then call the handleAdminMenu() function
                System.out.println("\nLogin Successful! Welcome, Administrator.");
                handleAdminMenu(admin_service, scanner);
            }
            else {

                //Otherwise, the user is null, meaning the credentials provided were not valid
                System.out.println("Invalid login or pin code.");
            }

        }
        //In the event of an SQLException, the error is caught:
        catch (SQLException e) {

            //The details of the error are then printed to the terminal via the getMessage() function
            System.out.println(e.getMessage());
        }
    }


    private static void handleCustomerMenu(Customer customer, CustomerService customer_service, Scanner scanner) {

        //An int variable is initialized to keep track of the options that the customer is selecting
        int choice;

        //A do loop is initiated to guarantee that the following code performs at least once:
        do {

            //The actions that the customer can perform on their account are printed to the terminal
            System.out.println("\n1----Withdraw Cash");
            System.out.println("2----Deposit Cash");
            System.out.println("3----Display Balance");
            System.out.println("4----Exit");
            System.out.print("Enter choice: ");

            //While the user has yet to input an integer:
            while (!scanner.hasNextInt()) {

                //The user is notified that an invalid input has been provided
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();

                System.out.print("Enter choice: ");
            }

            //The value that the user inputs next is stored in the choice variable
            choice = scanner.nextInt();

            //Depending on what value the user selects:
            switch (choice) {

                case 1:

                    boolean withdrawal_success = false;

                    while (!withdrawal_success) {

                        //If the value selected was 1, the user wishes to withdraw cash. They are prompted
                        //to specify the amount that will be withdrawn
                        System.out.print("Enter the withdrawal amount: ");

                        //The value that is input will be stored in an initialized int variable called
                        //withdrawn_amount
                        // Keep prompting until a valid integer is entered
                        while (!scanner.hasNextInt()) {

                            System.out.println("Invalid input. Please enter an integer.");
                            scanner.next();

                            System.out.print("\nEnter the withdrawal amount: ");
                        }

                        int withdrawn_amount = scanner.nextInt();

                        //The customer_service object will then perform the operation of withdrawing
                        //the specified amount from the provided customer's current balance
                        withdrawal_success = customer_service.withdrawCash(customer, withdrawn_amount);
                    }

                case 2:

                    //If the value selected was 2, the user wishes to deposit cash. They are prompted
                    //to specify the amount that will be deposited
                    System.out.print("Enter the cash amount to deposit: ");

                    //While the user has yet to input an int:
                    while (!scanner.hasNextInt()) {

                        System.out.println("Invalid input. Please enter an integer.");
                        scanner.next();

                        System.out.print("\nEnter the cash amount to deposit: ");
                    }

                    //The value that is input will be stored in an initialized int variable called
                    //withdrawn_amount
                    int deposited_amount = scanner.nextInt();

                    //The customer_service object will then perform the operation of withdrawing
                    //the specified amount from the provided customer's current balance
                    customer_service.depositCash(customer, deposited_amount);
                    break;

                case 3:

                    //If the value selected was 3, the user wishes to view their current balance.
                    //The customer_service object performs the operation of displaying said
                    //balance for the specific customer object
                    customer_service.displayBalance(customer);
                    break;

                case 4:

                    //If the value selected was 4, the user wishes to exit the program. The program
                    //sends an acknowledgement message and the loop will close upon the next
                    //attempt of iteration due to the choice int now being 4
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    break;

                default:

                    //If any other number was input, the user will be notified and asked to try again
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        //The loop continues to iterate as long as the choice int is not equal to 4
        while (choice != 4);
    }


    private static void handleAdminMenu(AdminService admin_service, Scanner scanner) {

        //An int variable is initialized to keep track of the options that the customer is selecting
        int choice;

        //A do loop is initiated to guarantee that the following code performs at least once:
        do {

            //The actions that the admin can perform on their account are printed to the terminal
            System.out.println("\n1----Create New Account");
            System.out.println("2----Delete Existing Account");
            System.out.println("3----Update Account Information");
            System.out.println("4----Search for Account");
            System.out.println("5----Exit");
            System.out.print("Enter choice: ");

            //While the user has yet to input an int:
            while (!scanner.hasNextInt()) {

                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();

                System.out.print("Enter choice: ");
            }


            //The value that the user inputs next is stored in the choice variable
            choice = scanner.nextInt();

            //Depending on what value the user selects:
            switch (choice) {

                case 1:

                    //If the value selected was 1, the user wishes to create a new account. They are
                    //prompted to enter relevant information. This information is stored in respective
                    //variables
                    System.out.print("Enter Login: ");
                    String login = scanner.next();

                    String pin;
                    while (true) {

                        System.out.print("Enter 5-digit Pin Code: ");
                        pin = scanner.next();

                        //To ensure that the pin meets the standards of the ATM system, it is checked
                        //Upon input to see if it meets the length requirement and each of its 5
                        //characters are digits (using regex)
                        if (pin.length() == 5 && pin.matches("\\d{5}")) {

                            break;
                        }
                        else {

                            //Otherwise, the user is notified that an invalid pin was provided
                            System.out.println("Invalid pin. Please enter exactly 5 digits.");
                        }
                    }

                    System.out.print("Enter Holder’s Name: ");
                    String holder = scanner.next();
                    scanner.nextLine();

                    //The following ensures that a valid starting balance is provided; in other words, the value
                    //isn't negative nor is it anything other than an int
                    int starting_balance = -1;

                    while (starting_balance < 0) {

                        System.out.print("Enter Starting Balance: ");

                        if (scanner.hasNextInt()) {

                            starting_balance = scanner.nextInt();

                            if (starting_balance < 0) {

                                System.out.println("Starting balance must be a positive number. Please try again.");
                            }
                        }
                        else {

                            System.out.println("Invalid input. Please enter a valid number.");
                            scanner.next();
                        }
                    }

                    //For status specifically, the user will input whether the account is active or not.
                    //A boolean will then be initialized, becoming either true or false depending
                    //on the string input by the user
                    String status;
                    boolean is_active;

                    while (true) {

                        System.out.print("Is the Account Active? (Y/N): ");
                        status = scanner.next().toLowerCase();

                        if (status.equals("y")) {

                            is_active = true;
                            break;
                        }
                        else if (status.equals("n")) {

                            is_active = false;
                            break;
                        }
                        else {

                            System.out.println("Invalid input. Please enter 'Y' or 'N'.");
                        }
                    }

                    //The admin service finally calls the createAccount() function, with all the information
                    //just provided by the user passed in as parameters
                    admin_service.createAccount(login, pin, holder, starting_balance, is_active);
                    break;

                case 2:

                    //If the value selected was 2, the user wishes to delete an existing account.
                    //They are prompted to provide the number of the account that is to be deleted

                    //The following code ensures that the account that is selected for deletion is
                    //a positive int
                    int account_num_delete = -1;
                    while (account_num_delete <= 0) {

                        System.out.print("Enter account number to delete: ");

                        if (scanner.hasNextInt()) {

                            account_num_delete = scanner.nextInt();

                            if (account_num_delete <= 0) {

                                System.out.println("Account number must be greater than 0. Please try again.");
                            }
                        }
                        else {

                            System.out.println("Invalid input. Please enter a valid integer.");
                            scanner.next();
                        }
                    }

                    //One the account number that will be deleted has been decided, it and the
                    //current scanner object are passed through the admin service's deleteAccount() function
                    admin_service.deleteAccount(account_num_delete, scanner);
                    break;

                case 3:

                    //If the value selected was 3, the user wishes to update an existing account.
                    //They are prompted to provide the number of the account that is to be modified

                    //The following code ensures that the account that is selected for deletion is
                    //a positive int
                    int account_num_update = -1;
                    while (account_num_update <= 0) {

                        System.out.print("Enter account number to update: ");

                        if (scanner.hasNextInt()) {

                            account_num_update = scanner.nextInt();

                            if (account_num_update <= 0) {

                                System.out.println("Account number must be greater than 0. Please try again.");
                            }
                        }
                        else {

                            System.out.println("Invalid input. Please enter a valid integer.");
                            scanner.next();
                        }
                    }

                    //Once the account that is being updated has been decided, the necessary variables are
                    //initialized; specifically, the fields of the account that can be altered
                    String new_holder = "";
                    String new_status = "";
                    String new_login = "";
                    String new_pin = "";

                    //The value that the user inputs next is stored in the update_choice variable
                    int update_choice;

                    //A do loop is initiated to guarantee that the following code performs at least once:
                    do {

                        //The fields that the admin can update are displayed to the terminal; the user is
                        //then prompted to select which field they will be updating
                        System.out.println("\nSelect the field to update:");
                        System.out.println("1----Update Holder’s Name");
                        System.out.println("2----Update Status");
                        System.out.println("3----Update Login");
                        System.out.println("4----Update Pin Code");
                        System.out.println("5----Exit");
                        System.out.print("Enter choice: ");

                        //While the user has yet to input an integer:
                        while (!scanner.hasNextInt()) {

                            System.out.println("Invalid input. Please enter a valid integer.");
                            scanner.next();

                            System.out.print("Enter choice: ");
                        }

                        //The value that the user inputs next is stored in the choice variable
                        update_choice = scanner.nextInt();
                        scanner.nextLine();

                        //Depending on what value the user selects:
                        switch (update_choice) {

                            case 1:

                                //If the value selected was 1, the user wishes to update the name of the
                                //holder of the account. The next input is the new name
                                System.out.print("Enter new Holder’s Name: ");
                                new_holder = scanner.nextLine();
                                break;

                            case 2:

                                //If the value selected was 2, the user wishes to update the current status of
                                //the account. The following code ensures that a proper input is provided:
                                while (true) {

                                    System.out.print("Is the Account Active? (Y/N): ");
                                    new_status = scanner.next().toLowerCase();

                                    if (new_status.equals("y")) {

                                        new_status = "Active";
                                        break;
                                    }
                                    else if (new_status.equals("n")) {

                                        new_status = "Disabled";
                                        break;
                                    }
                                    else {

                                        System.out.println("Invalid input. Please enter 'Y' or 'N'.");
                                    }
                                }
                                break;

                            case 3:

                                //If the value selected was 3, the user wishes to update the login of
                                //the account. The next input string is the new login name
                                System.out.print("Enter new Login: ");
                                new_login = scanner.next();
                                break;

                            case 4:

                                //If the value selected was 4, the user wishes to update the pin-code of
                                //the account. The following code ensures that a 5-digit string consisting
                                //of all digits is provided
                                while (true) {

                                    System.out.print("Enter new 5-digit Pin Code: ");
                                    new_pin = scanner.next();

                                    if (new_pin.length() == 5 && new_pin.matches("\\d{5}")) {

                                        break;
                                    }
                                    else {

                                        System.out.println("Invalid pin. Please enter exactly 5 digits.");
                                    }
                                }
                                break;

                            case 5:

                                //If the value selected was 5, the user wishes to exit the menu. Anything they already
                                //changed is to be passed into admin_service's updateAccount function. Blank strings
                                //are also passed through
                                admin_service.updateAccount(account_num_update, new_holder, new_status, new_login, new_pin);
                                System.out.println("Exiting update menu. Updates are now finalized.");
                                break;

                            default:

                                //The user is notified if an incorrect value is entered
                                System.out.println("Invalid choice. Please try again.");
                        }
                    }
                    //The loop continues to iterate as long as the choice int is not equal to 4
                    while (update_choice != 5);

                    break;

                case 4:

                    //If the value selected was 4, the user wishes to search for an existing account.
                    //They are prompted to provide the number of the account they wish to see.
                    //The following code ensures that the value that is passed is a positive int
                    int account_num_search = -1;
                    while (account_num_search <= 0) {

                        System.out.print("Enter account number to search: ");

                        if (scanner.hasNextInt()) {

                            account_num_search = scanner.nextInt();

                            if (account_num_search <= 0) {

                                System.out.println("Account number must be greater than 0. Please try again.");
                            }
                        }
                        else {

                            System.out.println("Invalid input. Please enter a valid integer.");
                            scanner.next();
                        }
                    }

                    //The adminService object then calls its searchAccount() function with
                    //the retrieved account number provided as its parameter
                    admin_service.searchAccount(account_num_search);
                    break;

                case 5:

                    //If the value selected was 5, the user wishes to exit the program. The program
                    //sends an acknowledgement message and the loop will close upon the next
                    //attempt of iteration due to the choice int now being 5
                    System.out.println("Exiting Admin Menu. Goodbye!");
                    break;

                default:

                    //If any other number was input, the user will be notified and asked to try again
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        //The loop continues to iterate as long as the choice int is not equal to 5
        while (choice != 5);
    }
}