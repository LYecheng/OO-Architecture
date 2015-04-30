import java.util.Iterator;

/**
 * Created by LiYecheng on 04/22/15.
 */

public class Bond extends Asset {

    static final double INTEREST_RATE = 0.0294;

    private String name;
    private int timePeriods;
    private int couponFrequency; //k
    private double yieldPerYear; //y
    private double couponRate; //c
    private int faceValue; //f

    public Bond(String bondName, int timePeriods, int faceValue, int couponFrequency, double yieldPerYear, double couponRate){
        this.name = "bond-" + bondName;
        this.timePeriods = timePeriods;
        this.faceValue = faceValue;
        this.couponFrequency =couponFrequency;
        this.yieldPerYear = yieldPerYear;
        this.couponRate = couponRate;
    }

    double getPresentValue(){
        return this.couponRate * this.faceValue * (1 - Math.pow(1+INTEREST_RATE, 0-this.timePeriods) / INTEREST_RATE) + this.faceValue / Math.pow(1+INTEREST_RATE, this.timePeriods);
    }

    double getDuration(Duration duration){
        return duration.getValue(this);
    }

    public void print(){
        MacaulayDuration macD = new MacaulayDuration();
        ModifiedDuration modD = new ModifiedDuration();

        System.out.printf("%s. MacD is %.3f and ModD is %.3f.\n", this.name, this.getDuration(macD), this.getDuration(modD));
    }

    public Iterator<Asset> createAssetIterator() {
        return new LeafIterator();
    }

    public int getTimePeriods(){
        return this.timePeriods;
    }

    public int getCouponFrequency(){
        return this.couponFrequency;
    }

    public double getYieldPerYear(){
        return this.yieldPerYear;
    }

    public double getCouponRate(){
        return this.couponRate;
    }

    public int getFaceValue(){
        return this.faceValue;
    }

}
