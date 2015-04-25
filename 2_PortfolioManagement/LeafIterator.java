import java.util.Iterator;

/**
 * Created by LiYecheng on 04/24/15.
 */
public class LeafIterator implements Iterator<Asset> {

    public boolean hasNext() {
        return false;
    }

    public Asset next() {
        return null;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}


