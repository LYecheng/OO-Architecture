import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by LiYecheng on 04/22/15.
 */
public class Portfolio extends Asset{

    private String name;
    private List<Asset> assets = new LinkedList<Asset>();

    public Portfolio(String portfolioName, List<Asset> assets){
        this.name = "portfolio-" + portfolioName;
        this.assets = assets;
    }

    double getPresentValue(){
        Iterator<Asset> i = this.createAssetIterator();
        double result = 0.0;

        while(i.hasNext()){
            result += i.next().getPresentValue();
        }
        return result;
    }

    double getDuration(Duration duration){
        Iterator<Asset> i = this.createAssetIterator();
        double result = 0.0;

        while(i.hasNext()){
            Asset item = i.next();
            result += item.getPresentValue() * item.getDuration(duration);
        }
        return result / this.getPresentValue();
    }

    public void print(){
        MacaulayDuration macD = new MacaulayDuration();
        ModifiedDuration modD = new ModifiedDuration();
        System.out.printf("%s. MacD is %.3f and ModD is %.3f.\n", this.name, this.getDuration(macD), this.getDuration(modD));
    }

    public Iterator<Asset> createAssetIterator() {
        return assets.iterator();
    }

    public void add(Asset asset) {
        this.assets.add(asset);
    }
}
