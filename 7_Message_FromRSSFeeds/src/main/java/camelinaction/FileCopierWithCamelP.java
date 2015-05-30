/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package camelinaction;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;

public class FileCopierWithCamelP {

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
            	String googleNewsURL = "https://news.google.com/?output=rss&splitEntries=false";
            	
            	from("rss:" + googleNewsURL)
            	.marshal().rss()
            	.marshal().string()
            	.split(xpath("/rss/channel/item/title/text()"))
            	.setBody(body())
            /*
                You may try to comment line 54 out to get all RSS feeds (10 entries in total)
            */
           	.filter(body().regex(".*New.*")) // filter out the feeds contains the keyword "New"
            	.process(new Processor() {
                    public void process(Exchange e) throws Exception {
                        String[] feed = e.getIn().getBody(String.class).split(" - ");
                        e.getOut().setBody("Title: "+feed[0]+"; Source: "+feed[1]);
                    }
            	})
            	.to("log:We received ${body}") // log()
            	.to("file:data/outbox") // file()
            	.to("jms:queue:RSS_GOOGLE_NEWS_UPDATES"); // send to queue
            	
                try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
            }
        });

        // start the route and let it do its work
        context.start();
        Thread.sleep(10000);

        // stop the CamelContext
        context.stop();
    }
}
