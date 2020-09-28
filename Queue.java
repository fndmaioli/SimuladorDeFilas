import java.util.*;

public class Queue {

    public ArrayList<Target> targets;
    
    public String name;
    public int queueCount;
    public double lastEventTime;
    public HashMap<Integer, Double> queueStates;

    public int servers;
    public double capacity;
    public double minArrival;
    public double maxArrival;
    public double minService;
    public double maxService;

    public Queue(String name) {
        this.name = name;
        this.queueCount = 0;
        this.lastEventTime = 0;
        queueStates = new HashMap<Integer, Double>();
        queueStates.put(-1, 0.0);
        targets = new ArrayList<Target>();

        this.servers = 1;
        this.capacity = Double.POSITIVE_INFINITY;
        this.minArrival = -1;
        this.maxArrival = -1;
        this.minService = -1;
        this.maxService = -1;
    }

    public void addTarget(String queue, double probability) {
        Target newTarget = new Target(probability, queue);

        if (targets.size() == 0) {
            targets.add(new Target()); 
        }

        int targetSize = targets.size();
        newTarget.prob += targets.get(targetSize-1).prob;
         if (newTarget.prob == 1.0) {
            targets.get(targetSize -1).target = newTarget.target;
        } else {
            targets.add(targetSize -1, newTarget);
        }
    }

    public String getDestination(double probability) {
        if (targets.size() != 0) {
            for(Target target: targets) {
                if (probability < target.prob) { 
                    return target.target;
                }
            }
        }
        return "";
    }

    public void computeQueueState(double currentTime) {
        double time = currentTime - this.lastEventTime;
        this.lastEventTime = currentTime;
        // se ja existe a key no hashmap
        if (queueStates.containsKey(this.queueCount)) {
            // atualiza o tempo que a fila ficou no estado atual
            double updatedTime = time + queueStates.get(queueCount);
            queueStates.replace(queueCount, updatedTime);
        } else {
            // se nao existe a key, cria instancia no hashmap
            queueStates.put(queueCount, time);
        }
    }

    public void computeLost() {
        // se ja existe a key '-1' no hashmap (usada para guardar numero de clientes perdidos)
        if (queueStates.containsKey(-1)) {
            // atualiza a quantidade de clientes perdidos
            queueStates.replace(-1, queueStates.get(-1)+1);
        } 
        // else {
        //     queueStates.put(-1, 1.0);
        // }
    }
}