public class Event {

    EventType type;
    double time;
    Queue queue;
    Queue sourceQueue;

    public Event(EventType type, double time, Queue queue) {
        this.type = type;
        this.time = time;
        this.queue = queue;
        this.sourceQueue = queue;
    }

    public Event(EventType type, double time, Queue queue, Queue sourceQueue) {
        this.type = type;
        this.time = time;
        this.queue = queue;
        this.sourceQueue = sourceQueue;
    } 

}