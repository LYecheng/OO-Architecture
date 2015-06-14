package camelinaction;

import org.apache.camel.CamelContext;
//import org.apache.camel.Exchange;
//import org.apache.camel.Processor;
//import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;

public class Subscriber {
	// Read message from Topic, extract certain information, form message, and post to queue

	public static void main(String args[]) throws Exception {

        // connect to ActiveMQ JMS broker listening on localhost on port 61616
        ConnectionFactory connectionFactory = 
            new ActiveMQConnectionFactory("tcp://localhost:61616");

        // Set up 3 trading engines 
        TradingEngine nyTradingEngine = nyTradingEngineSetup();
        TradingEngine londonTradingEngine = londonTradingEngineSetup();
        TradingEngine tokyoTradingEngine = tokyoTradingEngineSetup();

        // create 3 CamelContext and connect to ActiveMQ JMS broker listening on localhost on port 61616
        CamelContext nyContext = new DefaultCamelContext();
        nyContext.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        nyContext.addRoutes(new MessageChannel(nyTradingEngine));

        CamelContext londonContext = new DefaultCamelContext();
        londonContext.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        londonContext.addRoutes(new MessageChannel(londonTradingEngine));

        CamelContext tokyoContext = new DefaultCamelContext();
        tokyoContext.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        tokyoContext.addRoutes(new MessageChannel(tokyoTradingEngine));

        // start the route and let it do its work
        nyContext.start();
        londonContext.start();
        tokyoContext.start();
        Thread.sleep(60000);

        // stop the CamelContext
        nyContext.stop();
        londonContext.stop();
        tokyoContext.stop();
    }

	
	
	// helper methods to setup 3 trading engines with their own portfolios
	
    private static TradingEngine nyTradingEngineSetup(){
        Portfolio nyPortfolio = new Portfolio("NewYorkPortfolio");
        
        Portfolio p1 = new Portfolio("MSFT-Mean");  
        p1.add(new StockStat("MSFT", "bidMean"));
        p1.add(new StockStat("MSFT", "askMean"));
        nyPortfolio.add(p1);
        
        Portfolio p2 = new Portfolio("ORCL-StdDeviation");
        p2.add(new StockStat("ORCL", "bidStdDev"));
        p2.add(new StockStat("ORCL", "askStdDev"));
        nyPortfolio.add(p2);

        Portfolio p3 = new Portfolio("IBM-Variance");
        p3.add(new StockStat("IBM", "bidVariance"));
        p3.add(new StockStat("IBM", "askVariance"));
        nyPortfolio.add(p3);

        return new TradingEngine(nyPortfolio, "NewYorkTradingEngine");
    }    

    private static TradingEngine londonTradingEngineSetup(){
        Portfolio londonPortfolio = new Portfolio("LondonPortfolio");
        
        Portfolio p1 = new Portfolio("MSFT-Variance");
        p1.add(new StockStat("MSFT", "bidVariance"));
        p1.add(new StockStat("MSFT", "askVariance"));
        londonPortfolio.add(p1);
        
        Portfolio p2 = new Portfolio("ORCL-Mean"); 
        p2.add(new StockStat("ORCL", "bidMean"));
        p2.add(new StockStat("ORCL", "askMean"));
        londonPortfolio.add(p2);
        
        Portfolio p3 = new Portfolio("ORCL-StdDeviation");
        p3.add(new StockStat("IBM", "bidStdDev"));
        p3.add(new StockStat("IBM", "askStdDev"));
        londonPortfolio.add(p3);

        return new TradingEngine(londonPortfolio, "LondonTradingEngine");
    }

    private static TradingEngine tokyoTradingEngineSetup(){
        Portfolio tokyoPortfolio = new Portfolio("TokyoPortfolio");

        Portfolio p1 = new Portfolio("MSFT-StdDeviation");
        p1.add(new StockStat("MSFT", "bidStdDev"));
        p1.add(new StockStat("MSFT", "askStdDev"));
        tokyoPortfolio.add(p1);

        Portfolio p2 = new Portfolio("ORCL-Variance");
        p2.add(new StockStat("ORCL", "bidVariance"));
        p2.add(new StockStat("ORCL", "askVariance"));
        tokyoPortfolio.add(p2);

        Portfolio p3 = new Portfolio("IBM-Mean");
        p3.add(new StockStat("IBM", "bidMean"));
        p3.add(new StockStat("IBM", "askMean"));
        tokyoPortfolio.add(p3);

        return new TradingEngine(tokyoPortfolio, "TokyoTradingEngine");
    }

}


