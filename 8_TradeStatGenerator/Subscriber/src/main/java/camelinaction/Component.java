package camelinaction;

/**
 * Created by LiYecheng on 06/02/15.
 */

public abstract class Component {
	
	// reference: <Design Patterns> sample code

    protected String name;

    public Component(){
        this.name = "Default Name.";
    }

    public Component(String name){
        this.name = name;
    }

    abstract public String getName();
    abstract public void add(Component component);
    abstract public void remove(Component component);
    abstract public void update(String message); 
    
    abstract public String generateReport();

}
