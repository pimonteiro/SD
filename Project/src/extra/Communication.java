package extra;

import java.util.LinkedList;

public class Communication{

    private LinkedList<Message> msgs;

    public Communication(){
        this.msgs = new LinkedList<>();
    }

    public void addMessage(Message m){
        this.msgs.add(m);
    }

    public Message getmessage(){
        return this.msgs.removeFirst();
    }

    public int length(){
        return this.msgs.size();
    }
}
