package cz.robotron.rf.events;


public class ContentChangedEvent {

    Object sender;

    public Object getSender() {
        return sender;
    }

    /**
     * @param sender
     */
    public ContentChangedEvent(Object sender) {
        super();
        this.sender = sender;
    }


}
