import java.util.Vector;

/**
 * Created by LiYecheng on 04/25/15.
 */
public class MessageQueue {

    private Vector<Message> msgQueue = new Vector<Message>();

    public void addMsg(Message newMsg){
        this.msgQueue.add(newMsg);
    }

    public Message popMsg(){
        if(isEmpty() || msgQueue==null)
            return null;
        return  msgQueue.remove(0);
    }

    public boolean isEmpty(){
        return msgQueue.size()==0;
    }

    public Message peekMsg(){
        if(isEmpty())
            return null;
        return msgQueue.elementAt(0);
    }

}
