import java.util.ArrayList;
import java.util.HashMap;

public class QueueController {

    public HashMap<String, Queue> queueNetwork = new HashMap<String, Queue>();
    public double time = 0.0;
    public ArrayList<Event> eventsSchedule = new ArrayList<Event>();
    public RandomNumberGenerator randGen;

    // Cria instancia deixando que o RandomNumberGenerator crie uma seed automaticamente
    public QueueController(HashMap<String, Queue> queueNetwork, ArrayList<Event> events, int numPerSeed) {
        this.queueNetwork = queueNetwork;
        for(Event event : events) {
            scheduleEvent(event);
        }
        this.randGen = new RandomNumberGenerator(numPerSeed);
    }

    // Cria instancia com um seed especifica para o RandomNumberGenerator
    public QueueController(HashMap<String, Queue> queueNetwork, ArrayList<Event> events, double seed, int numPerSeed) {
        this.queueNetwork = queueNetwork;
        for(Event event : events) {
            scheduleEvent(event);
        }
        this.randGen = new RandomNumberGenerator(seed, numPerSeed);
    }

    public void runSimulation() {
        Event nextEvent;
        // Equanto ainda não gerou todos os números aleatórios continua rodando a simulação
        while (this.randGen.count > 0) {
            
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
        // contabiliza tempo e estado da fila
        this.time = arrival.time;
        Queue arrivalQueue = this.queueNetwork.get(arrival.queue);
        arrivalQueue.computeQueueState(this.time);
        // se número de clientes na fila < fila.capacity
        if (arrivalQueue.queueCount < arrivalQueue.capacity) {
            // número de clientes na fila++
            arrivalQueue.queueCount++;
            // se número de clientes na fila <= servers
            if (arrivalQueue.queueCount <= arrivalQueue.servers) {
                // agenda saida da fila
                scheduleEvent(createEventForLeavingQueue(arrivalQueue));
            }
        // se nao entra na fila conta como cliente perdido
        } else {
            arrivalQueue.computeLost();
        }   
        // se a fila possui tempos minimos e maximos de chegada de clientes
        if (arrivalQueue.minArrival > -1 && arrivalQueue.maxArrival > -1) {
            // agenda chegada na fila
            double eventTime = randGen.generateNewEventTime(arrivalQueue.minArrival, arrivalQueue.maxArrival);
            scheduleEvent(new Event(EventType.ARRIVAL, eventTime, arrival.queue));
        }
    }

    public void departure(Event departure) {
        // contabiliza tempo e estado da fila
        this.time = departure.time;
        Queue departureQueue = this.queueNetwork.get(departure.queue);
        departureQueue.computeQueueState(this.time);
        // número de clientes na fila--
        if (departureQueue.queueCount > 0) {
            departureQueue.queueCount--;
        }
        // se número de clientes na fila >= servers
        if (departureQueue.queueCount >= departureQueue.servers) {
            // agenda saida da fila
            scheduleEvent(createEventForLeavingQueue(departureQueue));
        }  
    }

    public void changeQueue(Event change) { 
        // contabiliza tempo e estado das filas
        this.time = change.time;
        Queue changeQueue = this.queueNetwork.get(change.queue);
        Queue changeNextQueue = this.queueNetwork.get(change.nextQueue);
        changeQueue.computeQueueState(this.time);
        changeNextQueue.computeQueueState(this.time);
        
        // número de clientes na fila--
        if (changeQueue.queueCount > 0) {
            changeQueue.queueCount--;
        }
        // se número de clientes na fila >= servers
        if (changeQueue.queueCount >= changeQueue.servers) {
            // agenda saida da fila
            scheduleEvent(createEventForLeavingQueue(changeQueue));
        }

        // se número de clientes na fila < fila.capacity
        if (changeNextQueue.queueCount < changeNextQueue.capacity) {
            // número de clientes na fila++
            changeNextQueue.queueCount ++;
            // se número de clientes na fila <= servers
            if (changeNextQueue.queueCount <= changeNextQueue.servers) { 
                // agenda saida da fila
                scheduleEvent(createEventForLeavingQueue(changeNextQueue));
            }
        // se nao entra na fila conta como cliente perdido
        } else {
            changeNextQueue.computeLost();
        }   
    }

    // Cria um evento para o cliente sair da fila, podendo trocar para outra fila ou sair do modelo
    public Event createEventForLeavingQueue(Queue queue) {            
        Event newEvent;
        String destination = "";
        // Verifica se deve gastar um numero aleatório para escolher o destino do cliente
        if (queue.shouldGenerateRndNumber()) {
            destination = queue.getDestination(randGen.generateNewEventTime(0,1));
        } else {
            destination = queue.getDestination(1.0);
        }

        // Calcula o momento em que o evento ocorrerá
        double eventTime = randGen.generateNewEventTime(queue.minService, queue.maxService);
        if (destination.isEmpty()) {
            // Se destination esta vazia agenda uma saida da fila e do modelo
            newEvent = new Event(EventType.DEPARTURE, eventTime, queue.name);
        } else { 
            // Se destination não esta vazia agenda um evento CHANGEQUEUE para o cliente trocar de fila
            newEvent = new Event(EventType.CHANGEQUEUE, eventTime, queue.name, destination);
        }
        return newEvent;
    }

    // Agenda um evento
    public void scheduleEvent(Event event) {
        event.time += this.time;

        // Adiciona o evento na posição correta em ordem de tempo na fila de eventos agendados
        for(int i=0; i<eventsSchedule.size(); i++) {
            if(event.time < eventsSchedule.get(i).time) {
                eventsSchedule.add(i, event);  
                return;
            }
        }
        eventsSchedule.add(event);
    }
}