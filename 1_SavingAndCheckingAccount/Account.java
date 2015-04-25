import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by LiYecheng on 04/14/15.
 */

//This class will have a Template Method called GetAccountSummary( ).
// This method in turn calls 3 other methods:  CalcInterest( ), UpdateBalance( ) and PrintSummary( ).
// (Note that CalcInterest( ) cannot be implemented in the base class (as it's abstract) and is overridden by subclasses).

public abstract class Account {
    public static int accountID = 0;

    protected String accountNo;

    protected Calendar openDate;
    protected Calendar updatedDate;
    protected double balance;
    protected double currentInterest;

    public void accountSetup(Calendar openDate, double initialBalance) {
        accountNo = Integer.toString(accountID);
        accountID++;

        this.openDate = openDate;
        this.updatedDate = openDate;
        this.balance = initialBalance;
        this.currentInterest = 0.0;
    }

    public void getAccountSummary(){
        calInterest();
        updateBalance();
        printSummary();
    }

    public abstract void calInterest();

    public void updateBalance(){
        balance = balance + currentInterest;
        updatedDate = Calendar.getInstance();
    }

    public void printSummary(){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        if(this.getClass().getSimpleName().equals("SavingsAccount"))
            this.accountNo = "SavingsAccount_No." + accountNo;
        else
            this.accountNo = "CheckingAccount_No." + accountNo;

        System.out.println("Summary of " + accountNo + ":");
        System.out.println("\tAccount Balance: $" + String.format("%.2f", balance));
        System.out.println("\tOpen Date: " + dateFormatter.format(openDate.getTime()));
        System.out.println("\tUpdated Date: " + dateFormatter.format(updatedDate.getTime()) + "\n");
    }

}
