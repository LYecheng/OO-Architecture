import java.util.Iterator;
import java.util.Stack;

/**
 * Created by LiYecheng on 04/22/15.
 */
public class CompositeIterator implements Iterator {

    // Reference: Head First Java

    private Stack<Iterator<Asset>> stack;

    public CompositeIterator(Iterator<Asset> iterator) {
        stack = new Stack<Iterator<Asset>>();
        stack.push(iterator);
    }

    public Asset next() {
        if (hasNext()) {
            Iterator<Asset> iterator = stack.peek();
            Asset asset = iterator.next();
            stack.push(asset.createAssetIterator()); // will return LeafIterator is asset is Leaf (ie. Bond)
            return asset;
        } else {
            return null;
        }
    }

    public boolean hasNext() {
        if (stack.empty()) {
            return false;
        } else {
            Iterator<Asset> iterator = stack.peek();
            if (!iterator.hasNext()) {
                stack.pop();
                return hasNext();
            } else {
                return true;
            }
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}
