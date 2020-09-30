import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class QueueController {

    public HashMap<String, Queue> queueNetwork = new HashMap<String, Queue>();
    public double time = 0;
    public ArrayList<Event> eventsSchedule = new ArrayList<Event>();
    public RandomNumberGenerator randGen;

    public int count = 100000;

    public QueueController(HashMap<String, Queue> queueNetwork, ArrayList<Event> events) {
        this.queueNetwork = queueNetwork;
        for(Event event : events) {
            scheduleEvent(event);
        }
    }

    public void runSimulation() {
        this.randGen = new RandomNumberGenerator();
        Event nextEvent;
        while (count > 0) {
            
            nextEvent = eventsSchedule.remove(0);
            if (nextEvent.type == EventType.ARRIVAL) {
                arrival(nextEvent);
            } else if (nextEvent.type == EventType.CHANGEQUEUE) {
                changeQueue(nextEvent);
            } else {
                departure(nextEvent);
            }
        }
        System.out.println("Tempo total: " + this.time);

        for(String keyName : queueNetwork.keySet()) {
            System.out.println("Probabilidade de cada estado da fila: " + keyName);
            for(Integer key : queueNetwork.get(keyName).queueStates.keySet()) {
                if (key >= 0) {
                    System.out.println(key + "   -   tempo: " + new DecimalFormat("#.##").format(queueNetwork.get(keyName).queueStates.get(key)) + "   -   " + new DecimalFormat("#.##").format((queueNetwork.get(keyName).queueStates.get(key) * 100 )/this.time) + "%");
                }
            }
            if (queueNetwork.get(keyName).queueStates.containsKey(-1)) {
                System.out.println("Numero de perdas: " + queueNetwork.get(keyName).queueStates.get(-1));
            } else {
                System.out.println("Numero de perdas: " + 0);
            }
        }


    }

    public void arrival(Event arrival){
        // contabiliza tempo
        this.time = arrival.time;
        arrival.queue.computeQueueState(this.time);
        // se fila < fila.capacity
        if (arrival.queue.queueCount < arrival.queue.capacity) {
            // fila++
            arrival.queue.queueCount++;
            // se fila <= 1
            if (arrival.queue.queueCount <= arrival.queue.servers) {
                // agenda saida
                String destination = arrival.queue.getDestination(randGen.generateNewEventTime(0,1));
                if (destination.isEmpty()) {
                    double eventTime = randGen.generateNewEventTime(arrival.queue.minService, arrival.queue.maxService);
                    scheduleEvent(new Event(EventType.DEPARTURE, eventTime, arrival.queue));
                } else {
                    double eventTime = randGen.generateNewEventTime(arrival.queue.minService, arrival.queue.maxService);
                    scheduleEvent(new Event(EventType.CHANGEQUEUE, eventTime, arrival.queue, queueNetwork.get(destination)));
                }
            }
        // se nao entra na fila
        } else {
            arrival.queue.computeLost();
        }   
        // agenda chegada
        if (arrival.queue.minArrival > -1 && arrival.queue.maxArrival > -1) {
            double eventTime = randGen.generateNewEventTime(arrival.queue.minArrival, arrival.queue.maxArrival);
            scheduleEvent(new Event(EventType.ARRIVAL, eventTime, arrival.queue));
        }
    }

    public void departure(Event departure) {
        // contabiliza tempo
        this.time = departure.time;
        departure.queue.computeQueueState(this.time);
        // fila--
        if (departure.queue.queueCount > 0) {
            departure.queue.queueCount--;
        }
        // se fila >= servers
        if (departure.queue.queueCount >= departure.queue.servers) {
            // agenda saida
            String destination = departure.queue.getDestination(randGen.generateNewEventTime(0,1));
            if (destination.isEmpty()) {
                double eventTime = randGen.generateNewEventTime(departure.queue.minService, departure.queue.maxService);
                scheduleEvent(new Event(EventType.DEPARTURE, eventTime, departure.queue));
            } else { 
                double eventTime = randGen.generateNewEventTime(departure.queue.minService, departure.queue.maxService);
                scheduleEvent(new Event(EventType.CHANGEQUEUE, eventTime, departure.queue, queueNetwork.get(destination)));
            }
        }  
    }

    public void changeQueue(Event change) { 
        this.time = change.time;
        change.queue.computeQueueState(this.time);
        change.nextQueue.computeQueueState(this.time);
        
        if (change.queue.queueCount > 0) {
            change.queue.queueCount--;
        }
        if (change.queue.queueCount >= change.queue.servers) {
            String destination = change.queue.getDestination(randGen.generateNewEventTime(0,1));
            if (destination.isEmpty()) {
                double eventTime = randGen.generateNewEventTime(change.queue.minService, change.queue.maxService);
                scheduleEvent(new Event(EventType.DEPARTURE, eventTime, change.queue));
            } else { 
                double eventTime = randGen.generateNewEventTime(change.queue.minService, change.queue.maxService);
                scheduleEvent(new Event(EventType.CHANGEQUEUE, eventTime, change.queue, queueNetwork.get(destination)));
            }
        }

        if (change.nextQueue.queueCount < change.nextQueue.capacity) {
            change.nextQueue.queueCount ++;
            if (change.nextQueue.queueCount <= change.nextQueue.servers) { 
                String destination = change.nextQueue.getDestination(randGen.generateNewEventTime(0,1));
                if (destination.isEmpty()) {
                    double eventTime = randGen.generateNewEventTime(change.nextQueue.minService, change.nextQueue.maxService);
                    scheduleEvent(new Event(EventType.DEPARTURE, eventTime, change.nextQueue));
                } else { 
                    double eventTime = randGen.generateNewEventTime(change.nextQueue.minService, change.nextQueue.maxService);
                    scheduleEvent(new Event(EventType.CHANGEQUEUE, eventTime, change.nextQueue, queueNetwork.get(destination)));
                }
            }
        } else {
            change.nextQueue.computeLost();
        }   
    }

    public void scheduleEvent(Event event) {
        event.time += this.time;

        for(int i=0; i<eventsSchedule.size(); i++) {
            if(event.time < eventsSchedule.get(i).time) {
                eventsSchedule.add(i, event);  
                this.count--;
                return;
            }
        }
        eventsSchedule.add(event);
        this.count--;
    }
}