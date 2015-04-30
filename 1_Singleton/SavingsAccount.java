import java.util.Calendar;

/**
 * Created by LiYecheng on 04/14/15.
 */

// own CalcInterest( ) method (which returns any interest that might have accrued)

public class SavingsAccount extends Account {

    final double INT_RATE = 0.05;

    public SavingsAccount(Calendar openDate, double initialBalance){
        this.accountSetup(openDate, initialBalance);
    }

    @Override
    public void calInterest() {

        int numberOfYears = Calendar.getInstance().get(Calendar.YEAR) - updatedDate.get(Calendar.YEAR) - 1;
        int numberOfDays = numberOfYears < 0 ? Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - updatedDate.get(Calendar.DAY_OF_YEAR) : Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - updatedDate.get(Calendar.DAY_OF_YEAR) + 365;

        numberOfYears += numberOfDays/365;

        if(numberOfYears < 0)
            currentInterest = 0.0;
        else
            currentInterest = balance * Math.pow(1+INT_RATE, numberOfYears) - balance;
    }

}
