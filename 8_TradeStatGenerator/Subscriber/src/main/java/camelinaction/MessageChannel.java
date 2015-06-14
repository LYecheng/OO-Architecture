package camelinaction;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class MessageChannel extends RouteBuilder {
	
	TradingEngine engine;
	
	public MessageChannel(TradingEngine engine){
		this.engine = engine;
	}
	
	public void configure(){
        from("jms:topic:Final_Topic_MSFT")
        .log("SUBSCRIBER RECEIVED: jms MSFT queue: ${body} from file: ${header.CamelFileNameOnly}")
        .process(new Processor(){
            public void process(Exchange e) throws Exception{
                engine.update(e.getIn().getBody(String.class));
                e.getIn().setBody(engine.report());
                System.out.println(engine.report()+" sent to engine "+engine.name);
            }
        }).to("jms:queue:Final_Trading_Engine_"+engine.name);

        from("jms:topic:Final_Topic_ORCL")
        .log("SUBSCRIBER RECEIVED: jms MSFT queue: ${body} from file: ${header.CamelFileNameOnly}")
        .process(new Processor(){
            public void process(Exchange e) throws Exception{
                engine.update(e.getIn().getBody(String.class));
                e.getIn().setBody(engine.report());
                System.out.println(engine.report()+" sent to engine "+engine.name);
            }
        }).to("jms:queue:Final_Trading_Engine_"+engine.name);

        from("jms:topic:Final_Topic_IBM")
        .log("SUBSCRIBER RECEIVED: jms MSFT queue: ${body} from file: ${header.CamelFileNameOnly}")
        .process(new Processor(){
            public void process(Exchange e) throws Exception{
                engine.update(e.getIn().getBody(String.class));
                e.getIn().setBody(engine.report());
                System.out.println(engine.report()+" sent to engine "+engine.name);
            }
        }).to("jms:queue:Final_Trading_Engine_"+engine.name);
    }
}
