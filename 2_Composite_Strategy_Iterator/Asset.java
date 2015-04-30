import java.util.Iterator;

/**
 * Created by LiYecheng on 04/22/15.
 */
public abstract class Asset {

    abstract double getPresentValue();

    abstract double getDuration(Duration duration);

    abstract public void print();

    abstract public Iterator<Asset> createAssetIterator();

}
