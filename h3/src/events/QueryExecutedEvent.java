package events;


public class QueryExecutedEvent {

    Object sender;

    public Object getSender() {
        return sender;
    }

    /**
     * @param sender
     */
    public QueryExecutedEvent(Object sender) {
        super();
        this.sender = sender;
    }


}
