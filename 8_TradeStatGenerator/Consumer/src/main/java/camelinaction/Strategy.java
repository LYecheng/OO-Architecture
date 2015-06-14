package camelinaction;

/**
 * Created by LiYecheng on 06/02/15.
 */

public abstract class Strategy {

    abstract public double calculateAsk(Stock stock);
    abstract public double calculateBid(Stock stock);

}

