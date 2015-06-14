package camelinaction;

/**
 * Created by LiYecheng on 06/02/15.
 */

public class VarianceStat extends Strategy {

    public double calculateAsk(Stock stock){
        return stock.askPriceSqrXQuantitySum / stock.askQuantitySum 
        		- Math.pow(stock.askPriceXQuantitySum / stock.askQuantitySum, 2);
    }

    public double calculateBid(Stock stock){
        return stock.bidPriceSqrXQuantitySum / stock.bidQuantitySum 
        		- Math.pow(stock.bidPriceXQuantitySum / stock.bidQuantitySum, 2);
    }
}
