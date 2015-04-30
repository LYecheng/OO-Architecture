/**
 * Created by LiYecheng on 04/23/15.
 */
public class ModifiedDuration extends Duration {

    public double getValue(Bond bond){
        MacaulayDuration macD = new MacaulayDuration();
        return bond.getDuration(macD)/(1.0+bond.getYieldPerYear()/bond.getCouponFrequency());
    }

}
