package camelinaction;

import java.util.ArrayList;

/**
 * Created by LiYecheng on 06/02/15.
 */

public abstract class Composite extends Component {
	
	// reference: <Design Patterns> sample code

    protected ArrayList<Component> components;

    public Composite(String name){
        super(name);
        this.components = new ArrayList<Component>();
    }

}
