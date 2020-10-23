public class Event {

    EventType type;
    double time;
    String queue;
    String nextQueue;

    // Quando é um evento do tipo ARRIVAL ou DEPARTURE que envolve apenas 1 fila
    public Event(EventType type, double time, String queue) {
        this.type = type;
        this.time = time;
        this.queue = queue;
        this.nextQueue = queue;
    }

    // Quando é um evento do tipo CHANGEQUEUE e o cliente sai de uma fila e entra em outra 
    public Event(EventType type, double time, String queue, String nextQueue) {
        this.type = type;
        this.time = time;
        this.queue = queue;
        this.nextQueue = nextQueue;
    } 

}