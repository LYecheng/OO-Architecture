import java.util.Calendar;

/**
 * Created by LiYecheng on 04/14/15.
 */

// own CalcInterest( ) method (which returns any interest that might have accrued)

public class CheckingAccount extends Account {

    public CheckingAccount(Calendar openDate, double initialBalance) {
        this.accountSetup(openDate, initialBalance);
    }

    @Override
    public void calInterest() {
        // for checking accounts, interest is 0
    }

}
