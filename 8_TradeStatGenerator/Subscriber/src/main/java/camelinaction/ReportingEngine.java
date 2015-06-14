package camelinaction;

/**
 * Created by LiYecheng on 06/02/15.
 */

public class ReportingEngine {

    private static ReportingEngine engine = null;

    private ReportingEngine(){

    }

    public static ReportingEngine getInstance(){
        if(engine == null)
            engine = new ReportingEngine();
        return engine;
    }
    
    // generate the overall report
    /* eg:
     * NewYorkPortfolio:
		MSFT-Mean:
		MSFT-bidMean: 39.807
		MSFT-askMean: 39.817
		ORCL-StdDeviation:
		ORCL-bidStdDev: 0.0367
		ORCL-askStdDev: 0.0322
		IBM-Variance:
		IBM-bidVariance: 21.8526
		IBM-askVariance: 17.596
      */
    
    public String report(TradingEngine tradingEngine){
        StringBuilder sb = new StringBuilder();
        sb.append(tradingEngine.portfolio.generateReport());
        return sb.toString();
    }
}
