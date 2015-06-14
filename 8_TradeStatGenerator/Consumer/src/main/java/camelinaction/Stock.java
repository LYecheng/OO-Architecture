package camelinaction;

/**
 * Created by LiYecheng on 06/02/15.
 */

public class Stock {

    protected String name;
    protected int bidQuantitySum;
    protected double bidPriceXQuantitySum;
    protected double bidPriceSqrXQuantitySum;
    protected int askQuantitySum;
    protected double askPriceXQuantitySum;
    protected double askPriceSqrXQuantitySum;

    public Stock(String name){
        this.name = name;
        this.bidQuantitySum = 0;
        this.bidPriceXQuantitySum = 0;
        this.bidPriceSqrXQuantitySum = 0;
        this.askQuantitySum = 0;
        this.askPriceXQuantitySum = 0;
        this.askPriceSqrXQuantitySum = 0;
    }

    public void addTick(String message){
        String[] parts = message.split("\t");
        double bidPrice = Double.valueOf(parts[1].replaceAll("[^\\d.]", ""));
        int bidQuantity = Integer.valueOf(parts[2].replaceAll("[^\\d.]", ""));
        double askPrice = Double.valueOf(parts[3].replaceAll("[^\\d.]", ""));
        int askQuantity = Integer.valueOf(parts[4].replaceAll("[^\\d.]", ""));

        this.bidQuantitySum += bidQuantity;
        this.bidPriceXQuantitySum += bidPrice * bidQuantity;
        this.bidPriceSqrXQuantitySum += Math.pow(bidPrice, 2) * bidQuantity;

        this.askQuantitySum += askQuantity;
        this.askPriceXQuantitySum += askPrice * askQuantity;
        this.askPriceSqrXQuantitySum += Math.pow(askPrice, 2) * askQuantity;
    }

    public double getAsk(Strategy criteria){
        return criteria.calculateAsk(this);
    }

    public double getBid(Strategy criteria){
        return criteria.calculateBid(this);
    }

}
