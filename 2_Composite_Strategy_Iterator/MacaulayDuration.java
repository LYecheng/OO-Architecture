/**
 * Created by LiYecheng on 04/23/15.
 */
public class MacaulayDuration extends Duration {

    public double getValue(Bond bond){

        // the 3 helpers are just to shorten the return expression
        double helper1 = bond.getYieldPerYear()/bond.getCouponFrequency(); // y/k
        double helper2 = bond.getCouponRate() / bond.getCouponFrequency(); // c/k
        double helper3 = bond.getTimePeriods()*bond.getCouponFrequency(); // m

        return ((1.0+helper1)/helper1 - (100.0*(1.0+helper1)+helper3*(helper2-100*helper1))/(helper2*(Math.pow(1.0+helper1, helper3)-1.0)+100.0*helper1)) / bond.getCouponFrequency();
    }

}
