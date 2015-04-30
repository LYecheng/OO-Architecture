import java.util.Calendar;

/**
 * Created by LiYecheng on 04/14/15.
 */

// A Bank class that is implemented as a Singleton.
// Therefore, no code in your program should be able to instantiate more than one Bank during a single execution of your code.

public class Bank {
    private static Bank bankInstance = null;

    // exists to avoid instantiation so that only Bank() methods can call it
    // Con: the Bank class cannot be subclassed
    private Bank() {

    }

    // make sure Bank class is Singleton
    public static Bank getBankInstance() {
        if(bankInstance == null) {
            bankInstance = new Bank();
        }
        return  bankInstance;
    }

    public CheckingAccount openCheckingAccount() {
        CheckingAccount cAccount = new CheckingAccount(Calendar.getInstance(), 0.0);
        return cAccount;
    }

    public SavingsAccount openSavingAccount() {
        SavingsAccount sAccount = new SavingsAccount(Calendar.getInstance(), 0.0);
        return sAccount;
    }

}
