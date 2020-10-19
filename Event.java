public class Event {

    EventType type;
    double time;
    String queue;
    String nextQueue;

    public Event(EventType type, double time, String queue) {
        this.type = type;
        this.time = time;
        this.queue = queue;
        this.nextQueue = queue;
    }

    public Event(EventType type, double time, String queue, String nextQueue) {
        this.type = type;
        this.time = time;
        this.queue = queue;
        this.nextQueue = nextQueue;
    } 

}