public class Event {

    EventType type;
    double time;
    Queue queue;
    Queue nextQueue;

    public Event(EventType type, double time, Queue queue) {
        this.type = type;
        this.time = time;
        this.queue = queue;
        this.nextQueue = queue;
    }

    public Event(EventType type, double time, Queue queue, Queue nextQueue) {
        this.type = type;
        this.time = time;
        this.queue = queue;
        this.nextQueue = nextQueue;
    } 

}