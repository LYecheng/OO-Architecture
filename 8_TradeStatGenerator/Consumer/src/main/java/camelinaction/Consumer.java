package camelinaction; 

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;

import java.text.DecimalFormat;

public class Consumer {

    public static void main(String args[]) throws Exception {
    	final DecimalFormat meanFormatter = new DecimalFormat("###.###");
    	final DecimalFormat varianceFormatter = new DecimalFormat("##.####");

    	final Stock microsoftStock = new Stock("MSFT");
    	final Stock oracleStock = new Stock("ORCL");
    	final Stock ibmStock = new Stock("IBM");

        // create CamelContext
        CamelContext context = new DefaultCamelContext();

        // connect to ActiveMQ JMS broker listening on localhost on port 61616
        ConnectionFactory connectionFactory = 
            new ActiveMQConnectionFactory("tcp://localhost:61616");
        context.addComponent("jms",
            JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        
        // add our route to the CamelContext
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                from("jms:queue:Final_Raw_Data")
                .log("RECEIVED: queue ${body}")
                .choice() 
                
                // parse messages in Final_Raw_Data queue
                // calculate the statistics and form new messages containing those statistics
                // send these 3 different stocks to 3 individual topics
                
                    .when(body().regex(".*MSFT.*"))
                    .process(new Processor() {
                        public void process(Exchange e) throws Exception {
                            microsoftStock.addTick(e.getIn().getBody(String.class));

                            StringBuilder sb = new StringBuilder();
                            sb.append(microsoftStock.name+"\t"
                            	+meanFormatter.format(microsoftStock.getBid(new MeanStat()))+"\t"
                            	+varianceFormatter.format(microsoftStock.getBid(new VarianceStat()))+"\t"
                            	+varianceFormatter.format(microsoftStock.getBid(new StdDeviationStat()))+"\t"
                            	+meanFormatter.format(microsoftStock.getAsk(new MeanStat()))+"\t"
                            	+varianceFormatter.format(microsoftStock.getAsk(new VarianceStat()))+"\t"
                            	+varianceFormatter.format(microsoftStock.getAsk(new StdDeviationStat()))+"\t");
                            	System.out.println("Topic: "+sb.toString()+" added to Final_Topic_MSFT.");
                            	e.getIn().setBody(sb);
                        	}
                    	}).to("jms:topic:Final_Topic_MSFT")

					.when(body().regex(".*ORCL.*"))
                    .process(new Processor() {
                        public void process(Exchange e) throws Exception {
                        	oracleStock.addTick(e.getIn().getBody(String.class));

                            StringBuilder sb = new StringBuilder();
                            sb.append(oracleStock.name+"\t"
                            	+meanFormatter.format(oracleStock.getBid(new MeanStat()))+"\t"
                            	+varianceFormatter.format(oracleStock.getBid(new VarianceStat()))+"\t"
                            	+varianceFormatter.format(oracleStock.getBid(new StdDeviationStat()))+"\t"
                            	+meanFormatter.format(oracleStock.getAsk(new MeanStat()))+"\t"
                            	+varianceFormatter.format(oracleStock.getAsk(new VarianceStat()))+"\t"
                            	+varianceFormatter.format(oracleStock.getAsk(new StdDeviationStat()))+"\t");
                            	System.out.println("Topic: "+sb.toString()+" added tp Final_Topic_ORCL.");	
                            	e.getIn().setBody(sb);
                        	}
                    	}).to("jms:topic:Final_Topic_ORCL")

					.when(body().regex(".*IBM.*"))
                    .process(new Processor() {
                        public void process(Exchange e) throws Exception {
                            ibmStock.addTick(e.getIn().getBody(String.class));

                            StringBuilder sb = new StringBuilder();
                            sb.append(ibmStock.name+"\t"
                            	+meanFormatter.format(ibmStock.getBid(new MeanStat()))+"\t"
                            	+varianceFormatter.format(ibmStock.getBid(new VarianceStat()))+"\t"
                            	+varianceFormatter.format(ibmStock.getBid(new StdDeviationStat()))+"\t"
                            	+meanFormatter.format(ibmStock.getAsk(new MeanStat()))+"\t"
                            	+varianceFormatter.format(ibmStock.getAsk(new VarianceStat()))+"\t"
                            	+varianceFormatter.format(ibmStock.getAsk(new StdDeviationStat()))+"\t");
                            	System.out.println("Topic: "+sb.toString()+" added to Final_Topic_IBM.");
                            	e.getIn().setBody(sb);
                        	}
                    	}).to("jms:topic:Final_Topic_IBM")
                    	.otherwise()
                    	.to("jms:queue: Final_Invalid_Data");
                    
            } // configure
        }); // add route

        // start the route and let it do its work
        context.start();
        Thread.sleep(50000);

        // stop the CamelContext
        context.stop();
    }
}