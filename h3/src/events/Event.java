package events;

import h2.DataBlock;


public class Event {
    EventTypeEnum _type;
    Object        _info;

    /**
     * @param type
     * @param info
     */
    public Event(EventTypeEnum type, Object info) {
        super();
        _type = type;
        _info = info;
    }

    public EventTypeEnum getType() {
        return _type;
    }

    public Object getInfo() {
        return _info;
    }

    public DataBlock getSender() {
        // TODO Auto-generated method stub
        return null;
    }

}
