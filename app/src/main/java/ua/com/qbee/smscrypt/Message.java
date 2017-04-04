package ua.com.qbee.smscrypt;


public class Message implements Comparable<Message> {
    /**
     * The content of the message
     */
    String message;
    long data;
    /**
     * boolean to determine, who is sender of this message
     */
    boolean isMine;
    /**
     * boolean to determine, whether the message is a status message or not.
     * it reflects the changes/updates about the sender is writing, have entered text etc
     */
    boolean isStatusMessage;

    /**
     * Constructor to make a Message object
     */
    private String id;

    public Message(String message, boolean isMine, long data, String id) {
        super();
        this.data = data;
        this.message = message;
        this.isMine = isMine;
        this.isStatusMessage = false;
        this.setId(id);
    }

    /**
     * Constructor to make a status Message object
     * consider the parameters are swaped from default Message constructor,
     * not a good approach but have to go with it.
     */
    public Message(boolean status, String message, long data, String id) {
        super();
        this.data = data;
        this.message = message;
        this.isMine = false;
        this.isStatusMessage = status;
        this.setId(id);
    }

    public String getMessage() {
        return message;
    }

    public long getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isStatusMessage() {
        return isStatusMessage;
    }

    public void setStatusMessage(boolean isStatusMessage) {
        this.isStatusMessage = isStatusMessage;
    }

    @Override
    public int compareTo(Message m) {
        if (data > m.data) return +1;
        if (data < m.data) return -1;
        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
