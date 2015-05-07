/**
 * Created by LiYecheng on 04/25/15.
 */
public class QueryMsg extends Message {

    private String body;
    private int id;

    public QueryMsg(String body, int id) {
        this.body = body;
        this.id = id;
    }

    public String getHeader(){
        return "REQUEST";
    }

    public String getBody(){
        return this.body;
    }

    public int getID() {
        return this.id;
    }

    public void print() {
        System.out.printf("ID: %d, Header: %s, Body: %s.\n", this.id, "REQUEST", this.body);
    }

}
