import sun.util.resources.cldr.sg.CurrencyNames_sg;

import java.io.IOException;

/**
 * Created by LiYecheng on 04/30/15.
 */
public class Driver {
    protected static MessageQueue testQueue = new MessageQueue();
    protected static Responder responder = new Responder(testQueue);
    protected static int initialID = 0;

    public static void main(String[] args) throws IOException {

        startTestClient(); // populating value into testQueue

        while( !testQueue.isEmpty()) {
            testQueue.peekMsg().print(); // print the current request message
            responder.processQueue(responder.getQueue());
        }
    }

    // this method is to add testing data to the testQueue
    private static void startTestClient() {
        responder.addRequest(new QueryMsg("1234", ++initialID));
        responder.addRequest(new QueryMsg("2345", ++initialID));
        responder.addRequest(new QueryMsg("3456", ++initialID));
        responder.addRequest(new QueryMsg("4567", ++initialID));
        responder.addRequest(new QueryMsg("5678", ++initialID));
    }

}
