package camelinaction;

/**
 * Created by LiYecheng on 06/02/15.
 */

public class Portfolio extends Composite {

    public Portfolio(String name){
        super(name);
    }

    public String getName(){
        return this.name;
    }
    
    public void add(Component component){
        this.components.add(component);
    }
    
    public void remove(Component component){ 
    	this.components.remove(component); 
    }
    
    public void update(String message) {
		StockIterator i = new StockIterator(this);
		for(Component component = i.first(); !i.isDone(); component = i.next()){
			component.update(message);
		}
	}

    public String generateReport(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.name+":\n");
        StockIterator iterator = new StockIterator(this);
        Component component = iterator.first();

        while(!iterator.isDone()){
            sb.append(component.generateReport()); // recursion & polymorphism
            component = iterator.next();
        }
        
        return sb.toString();
    }

}
