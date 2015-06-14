package camelinaction;

/**
 * Created by LiYecheng on 06/02/15.
 */

public class TradingEngine {
	// An engine contains a portfolio, which may be composed of sub-portfolios and/or stocks
	// An engine reports statistics of its portfolio whenever new portfolio/stock is added
	
    protected String name;
    protected Portfolio portfolio;
    protected ReportingEngine engine;

    public TradingEngine(Portfolio portfolio, String name){
        this.name = name;
        this.portfolio = portfolio;
    }
    
    public void update(String message){
    	StockIterator i = new StockIterator(this.portfolio);
		
		for(Component item = i.first(); !i.isDone(); item = i.next()){
			item.update(message);
		}
	}

    public String report(){
        this.engine = ReportingEngine.getInstance();
        return engine.report(this);
    }   

}
