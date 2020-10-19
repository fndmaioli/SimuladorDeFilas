import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class QueueController {

    public HashMap<String, Queue> queueNetwork = new HashMap<String, Queue>();
    public double time = 0.0;
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
    }

    public void arrival(Event arrival){
        // contabiliza tempo
        this.time = arrival.time;
        Queue arrivalQueue = this.queueNetwork.get(arrival.queue);
        arrivalQueue.computeQueueState(this.time);
        // se fila < fila.capacity
        if (arrivalQueue.queueCount < arrivalQueue.capacity) {
            // fila++
            arrivalQueue.queueCount++;
            // se fila <= 1
            if (arrivalQueue.queueCount <= arrivalQueue.servers) {
                // agenda saida
                String destination = arrivalQueue.getDestination(randGen.generateNewEventTime(0,1));
                if (destination.isEmpty()) {
                    double eventTime = randGen.generateNewEventTime(arrivalQueue.minService, arrivalQueue.maxService);
                    scheduleEvent(new Event(EventType.DEPARTURE, eventTime, arrival.queue));
                } else {
                    double eventTime = randGen.generateNewEventTime(arrivalQueue.minService, arrivalQueue.maxService);
                    scheduleEvent(new Event(EventType.CHANGEQUEUE, eventTime, arrival.queue, destination));
                }
            }
        // se nao entra na fila
        } else {
            arrivalQueue.computeLost();
        }   
        // agenda chegada
        if (arrivalQueue.minArrival > -1 && arrivalQueue.maxArrival > -1) {
            double eventTime = randGen.generateNewEventTime(arrivalQueue.minArrival, arrivalQueue.maxArrival);
            scheduleEvent(new Event(EventType.ARRIVAL, eventTime, arrival.queue));
        }
    }

    public void departure(Event departure) {
        // contabiliza tempo
        this.time = departure.time;
        Queue departureQueue = this.queueNetwork.get(departure.queue);
        departureQueue.computeQueueState(this.time);
        // fila--
        if (departureQueue.queueCount > 0) {
            departureQueue.queueCount--;
        }
        // se fila >= servers
        if (departureQueue.queueCount >= departureQueue.servers) {
            // agenda saida
            String destination = departureQueue.getDestination(randGen.generateNewEventTime(0,1));
            if (destination.isEmpty()) {
                double eventTime = randGen.generateNewEventTime(departureQueue.minService, departureQueue.maxService);
                scheduleEvent(new Event(EventType.DEPARTURE, eventTime, departure.queue));
            } else { 
                double eventTime = randGen.generateNewEventTime(departureQueue.minService, departureQueue.maxService);
                scheduleEvent(new Event(EventType.CHANGEQUEUE, eventTime, departure.queue, destination));
            }
        }  
    }

    public void changeQueue(Event change) { 
        this.time = change.time;
        Queue changeQueue = this.queueNetwork.get(change.queue);
        Queue changeNextQueue = this.queueNetwork.get(change.nextQueue);
        changeQueue.computeQueueState(this.time);
        changeNextQueue.computeQueueState(this.time);
        
        if (changeQueue.queueCount > 0) {
            changeQueue.queueCount--;
        }
        if (changeQueue.queueCount >= changeQueue.servers) {
            String destination = changeQueue.getDestination(randGen.generateNewEventTime(0,1));
            if (destination.isEmpty()) {
                double eventTime = randGen.generateNewEventTime(changeQueue.minService, changeQueue.maxService);
                scheduleEvent(new Event(EventType.DEPARTURE, eventTime, change.queue));
            } else { 
                double eventTime = randGen.generateNewEventTime(changeQueue.minService, changeQueue.maxService);
                scheduleEvent(new Event(EventType.CHANGEQUEUE, eventTime, change.queue, destination));
            }
        }

        if (changeNextQueue.queueCount < changeNextQueue.capacity) {
            changeNextQueue.queueCount ++;
            if (changeNextQueue.queueCount <= changeNextQueue.servers) { 
                String destination = changeNextQueue.getDestination(randGen.generateNewEventTime(0,1));
                if (destination.isEmpty()) {
                    double eventTime = randGen.generateNewEventTime(changeNextQueue.minService, changeNextQueue.maxService);
                    scheduleEvent(new Event(EventType.DEPARTURE, eventTime, change.nextQueue));
                } else { 
                    double eventTime = randGen.generateNewEventTime(changeNextQueue.minService, changeNextQueue.maxService);
                    scheduleEvent(new Event(EventType.CHANGEQUEUE, eventTime, change.nextQueue, destination));
                }
            }
        } else {
            changeNextQueue.computeLost();
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