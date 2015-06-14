package camelinaction;

/**
 * Created by LiYecheng on 06/02/15.
 */

public class StockIterator {
	// reference: <Design Patterns> sample code

    private Composite composite;
    private int ptr; // current pointer

    public StockIterator(Composite composite){
        this.composite = composite;
        this.ptr = 0;
    }

    public Component first(){
        return this.composite.components.get(0);
    }

    public boolean isDone(){
        return this.ptr >= this.composite.components.size() ? true : false;
    }

    public Component next(){
        this.ptr++;
        if(!isDone())
            return this.composite.components.get(this.ptr);
        else
            return null;
    }

    public Component get(){
        return this.composite.components.get(this.ptr);
    }
}
