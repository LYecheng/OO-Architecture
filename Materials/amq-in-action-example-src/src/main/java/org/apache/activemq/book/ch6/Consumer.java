package org.apache.activemq.book.ch6;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.book.ch3.portfolio.Listener;

public class Consumer {

    private static String brokerURL = "tcp://localhost:61616";
    private static transient ConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    
    private String username = "guest";
    private String password = "password";
    
    public Consumer() throws JMSException {
    	factory = new ActiveMQConnectionFactory(brokerURL);
    	connection = factory.createConnection(username, password);
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }
    
    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }    
    
    public static void main(String[] args) throws Exception {
    	Consumer consumer = new Consumer();
    	try {
	    	for (String stock : args) {
	    		Destination destination = consumer.getSession().createTopic("STOCKS." + stock);
	    		MessageConsumer messageConsumer = consumer.getSession().createConsumer(destination);
	    		messageConsumer.setMessageListener(new Listener());
	    	}
	    	System.out.println("Press any key to exit");
	    	System.in.read();
    	} finally {
    		consumer.close();
    	}
    }
	
	public Session getSession() {
		return session;
	}

}
