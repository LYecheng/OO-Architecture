package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;

public class Producer {

    public static void main(String args[]) throws Exception {
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
            	from("file:data/inbox?noop=true")
            	.log("RETRIEVED:  ${file:name}")
                .unmarshal().csv().split(body())
                .choice()
                	.when(body().regex(".*(MSFT|IBM|ORCL).*"))
                	.process(new Processor() {
                		public void process(Exchange e) throws Exception {
                			System.out.println("MESSAGE FROM FILE: " 
                					+ e.getIn().getHeader("CamelFileName") 
                					+ " is heading to Final_Raw_Data Queue for Stock: " 
                					+  (e.getIn().getBody(String.class).split("\t"))[0].substring(1));
                		}
                	}).to("jms:queue:Final_Raw_Data")
                	.otherwise()
                		.process(new Processor() {
            				public void process(Exchange e) throws Exception {
            					System.out.println("MESSAGE FROM FILE:" + e.getIn().getHeader("CamelFileName")
        								+ " is heading to MPCS_51050_Invalid_Data: "
        								+ (e.getIn().getBody(String.class).split("\t"))[0].substring(1));
            				}
            	}).to("jms:queue:Final_Invalid_Data");
            }
        });

        // start the route and let it do its work
        context.start();
        Thread.sleep(50000);

        // stop the CamelContext
        context.stop();
    }
}

