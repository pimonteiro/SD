package extra;

public class Message {

    private int source;
    private int destination;
    private String message;

    public Message(int source, int destination, String message) {
        this.source = source;
        this.destination = destination;
        this.message = message;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
