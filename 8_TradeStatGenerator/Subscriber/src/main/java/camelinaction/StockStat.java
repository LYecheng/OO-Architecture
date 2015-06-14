package camelinaction;

/**
 * Created by LiYecheng on 06/02/15.
 */

public class StockStat extends Component {	
	
	// A StockStat object stores a statistical index (mean, variance, or standard deviation) and its value
	
    protected String index;
    protected double value;

    public StockStat(String name, String criteria){
        super(name);
        this.index = criteria;
        this.value = 0;
    }

    public void add(Component component){
        System.out.println("Cannot add.");
    }
    public void remove(Component component) {
        System.out.println("Cannot remove."); }

    public String getName(){
        return this.name;
    }

    // update the stat indices of the new tick added
    // eg: name = MSFT, index = bidMean, value = 39.807
    
    public void update(String message){
        String[] parts = message.split("\t");
        if(this.name.equalsIgnoreCase(parts[0])){ 
            if(this.index.equals("bidMean")){
                this.value = Double.valueOf(parts[1].replaceAll("[^\\d.]", ""));
            } else if(this.index.equals("bidVariance")){
                this.value = Double.valueOf(parts[2].replaceAll("[^\\d.]", ""));
            } else if(this.index.equals("bidStdDev")){
                this.value = Double.valueOf(parts[3].replaceAll("[^\\d.]", ""));
            } else if(this.index.equals("askMean")){
                this.value = Double.valueOf(parts[4].replaceAll("[^\\d.]", ""));
            } else if(this.index.equals("askVariance")){
                this.value = Double.valueOf(parts[5].replaceAll("[^\\d.]", ""));
            } else if(this.index.equals("askStdDev")){
                this.value = Double.valueOf(parts[6].replaceAll("[^\\d.]", ""));
            }
        }
    }

    // form part of the report message of the current stat index and its value
    // eg: "MSFT-bidMean: 39.807"
    
    public String generateReport(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.name+"-"+this.index+": "+String.valueOf(this.value)+"\n");
        return sb.toString();
    }

}
