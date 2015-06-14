package camelinaction;

/**
 * Created by LiYecheng on 06/0s2/15.
 */

public class MeanStat extends Strategy {

    public double calculateAsk(Stock stock){
        return stock.askPriceXQuantitySum / stock.askQuantitySum;
    }

    public double calculateBid(Stock stock){
        return stock.bidPriceXQuantitySum / stock.bidQuantitySum;
    }

}
