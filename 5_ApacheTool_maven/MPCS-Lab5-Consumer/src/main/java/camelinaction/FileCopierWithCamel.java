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
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;

public class FileCopierWithCamel {

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
                //from("file:data/inbox?noop=true").to("file:data/outbox");
            	from("jms:queue:MPCS_51050_LAB5").log("RECEIVED:  jms queue: ${body} from file: ${header.CamelFileNameOnly}").convertBodyTo(String.class).to("file:data/outbox?noop=true&fileName=Thread-${threadName}-${header.CamelFileNameOnly}.out");
                try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
                //from("file:data/outbox").to("jms:MPCS51050_config_test");
            }
        });

        // start the route and let it do its work
        context.start();
        Thread.sleep(20000);

        // stop the CamelContext
        context.stop();
    }
}


/*
 
1.  Consume messages from the MPCS_51050_LAB5 queue [CIA 7.3]
2.  Log the string "RECEIVED:  jms queue: ${body} from file: ${header.CamelFileNameOnly}" [CIA Appendix A]
3.  Convert the body of the message taken from the queue into a string using a String.class conversion [CIA 3.6.2]
4.  Write the resulting set of messages out to "file:data/outbox" [CIA 7.2.1] appending ".out" to the the current thread name and the original Camel input file name in the the output file name [CIA Table A.1], with the result that you have 100 files in the data/outbox directory that look something like this:
  
 
 */
