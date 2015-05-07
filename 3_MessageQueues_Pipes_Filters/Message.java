/**
 * Created by LiYecheng on 04/25/15.
 */

public abstract class Message {

    abstract String getHeader();

    abstract String getBody();

    abstract int getID();

    abstract void print();

}