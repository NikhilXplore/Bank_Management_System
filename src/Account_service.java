import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.time.format.DateTimeParseException;


public class Account_service {
    // check if it is valid email
    public static boolean validemail(String email){

        // check exectly one @ and it should not be in 0th index
        int index = -1;
        for(int i = 0; i < email.length(); i++){ 
            if(email.charAt(i) == '@'){
                if(index == -1) index = i;
                else return false;
            }
        }
        if(index == -1 || index == 0) return false;

        // check spaces
        for(int i = 0; i < email.length(); i++){
            if(email.charAt(i) == 32) return false;
        }

        return true;
    }


    // function to create an Account
    public static Account CreateAccount(Scanner sc){
        
        System.out.println("Enter name of Account holder: ");
        String name = sc.nextLine();

        System.out.println("Please Select Account Type (choose number): ");
        System.err.println("    1. Savings Account");
        System.out.println("    2. Current Account");
        int type = sc.nextInt();
        sc.nextLine(); // consume the newline character
        while (type != 1 && type != 2) {
            System.out.println("Invalid account type. Choose 1 for Savings or 2 for Current: ");
            type = sc.nextInt();
            sc.nextLine();
        }
        switch (type) {
            case 1:
                System.out.println("You have selected Savings Account");
                break;
            case 2:
                System.out.println("You have selected Current Account");
                break;
            default:
                break;
        }

        String phone;
        while(true){
            System.out.println("Enter Phone Number: ");
            phone = sc.nextLine();
            if(phone.length() == 10 && phone.chars().allMatch( Character::isDigit )){
                break;
            }
            else{
                System.out.println("Invalid Phone Number. Please enter a 10-digit number.");
            }
        }

        String email; 
        while(true){
            System.out.println("Enter Your Email ID: ");
            email = sc.nextLine();

            boolean valid = validemail(email);
            if(valid == true) break;
            else System.out.println("Invalid Email ID. Please enter a valid Email ID");
        }

        String Address;
        System.out.println("Please Enter Your Address: ");
        Address = sc.nextLine();

        LocalDate DOB = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        while(DOB == null){
            System.out.println("Please Enter Your DOB in (dd-MM-yyy) formate");
            String input = sc.nextLine();

            try{
                DOB = LocalDate.parse(input, formatter);
            }
            catch(DateTimeParseException e){
                System.out.println("❌ Invalid format! Please use dd-MM-yyyy (e.g., 30-08-2005).");
            }
        }

        System.out.println("Please Enter the Initial Deposite to start yout account: ");
        int Initial = sc.nextInt();
        while (Initial < 0) {
            System.out.println("Initial deposit cannot be negative. Enter a valid amount: ");
            Initial = sc.nextInt();
        }


        Account a1 = new Account(name, Initial, type, phone, email, Address, DOB);
        System.out.println("Account Created.");
        
        return a1;
    }
}
