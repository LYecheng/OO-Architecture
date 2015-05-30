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
            	from("file:data/inbox?noop=true").log("RETRIEVED:  ${file:name}").unmarshal().csv().split(body()).process(new Processor() {
                    public void process(Exchange e) throws Exception {
                    	System.out.println("MESSAGE FROM FILE: " + e.getIn().getHeader("CamelFileName") + 
            					" is heading to MPCS_51050_Lab6 Queue for Stock: " +  (e.getIn().getBody(String.class).split("\t"))[0].substring(1));
                    }
            }).to("jms:queue:MPCS_51050_LAB6");
                try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

                from("file:data/outbox").to("jms:MPCS51050_config_test");
            }
        });

        // start the route and let it do its work
        context.start();
        Thread.sleep(3000);

        // stop the CamelContext
        context.stop();
    }
}


//1.  Consume the input directory "data/input" [CIA 2.3 & 7.1-7.2]
//2.  Logs the string "RETRIEVED:  ${file:name}" [CIA Appendix A]
//3.  Unmarshals the data read [CIA 3.4]
//4.  Runs the CSV translator on the data [CIA 3.4.2]
//5.  Splits the body (so that the individual lines go on the queue as individual messages) [CIA 3.4.2]
//6.  Sends the output to jms:queue:MPCS_51050_LAB5 [CIA 3.4.2]
