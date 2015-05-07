import javax.naming.OperationNotSupportedException;

/**
 * Created by LiYecheng on 05/01/15.
 */
public class Responder {

    private MessageQueue queue;

    public Responder(MessageQueue queue) {
        this.queue = queue;
    }

    // takes QueryMsg, process it into a ReplyMsg and add to the queue
    public void processQueue (MessageQueue queue) {
        Message msg = queue.popMsg(); // dequeue

        while(msg.getHeader().equals("REPLY")){ // skip ReplyMsg
            if(!queue.isEmpty())
                msg = queue.popMsg();
            else
                return;
        }
        int body = Integer.parseInt(msg.getBody()); // get body and convert to int
        ReplyMsg reply = new ReplyMsg(intToRoman(body), msg.getID()); // convert int to Roman to get ReplyMsg
        reply.print();

        this.queue.addMsg(reply);
    }

    // add now client request (QueryMsg into the queue)
    public void addRequest(QueryMsg qMsg) {
        this.queue.addMsg(qMsg); // enqueue new query message
    }

    public MessageQueue getQueue() {
        return this.queue;
    }

    private String intToRoman(int num) {
        StringBuffer res = new StringBuffer();

        // I - 1; V - 5; X - 10; L - 50; C - 100; D - 500; M - 1000
        String[] arrayC = {"","C","CC","CCC","CD","D","DC","DCC","DCCC","CM"};
        String[] arrayX = {"","X","XX","XXX","XL","L","LX","LXX","LXXX","XC"};
        String[] arrayI = {"","I","II","III","IV","V","VI","VII","VIII","IX"};

        int mNums = num/1000;
        int cNums = (num%1000)/100;
        int xNums = (num%100)/10;
        int iNums = num%10;

        for(int i = mNums; i>0; i--){
            res.append("M");
        }
        res.append(arrayC[cNums]);
        res.append(arrayX[xNums]);
        res.append(arrayI[iNums]);

        return res.toString();
    }

}
