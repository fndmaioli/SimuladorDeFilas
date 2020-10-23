import java.util.*;

public class Queue {

    public String name;
    public int queueCount;
    public double lastEventTime;
    public HashMap<Integer, Double> queueStates;

    public ArrayList<Target> targets;

    public int servers;
    public double capacity;
    public double minArrival;
    public double maxArrival;
    public double minService;
    public double maxService;

    // Cria instancia da fila apenas com nome
    public Queue(String name) {
        this.name = name;
        this.queueCount = 0;
        this.lastEventTime = 0;
        // Cria um HashMap para guardar os estados da fila
        queueStates = new HashMap<Integer, Double>();
        // Cria instancia no HashMap com a key -1 para número de clientes perdidos na fila
        queueStates.put(-1, 0.0);
        // Cria array vazio de possíveis destinos dos clientes que saem da fila
        targets = new ArrayList<Target>();

        this.servers = 1;
        this.capacity = Double.POSITIVE_INFINITY;
        this.minArrival = -1;
        this.maxArrival = -1;
        this.minService = -1;
        this.maxService = -1;
    }

    // Adiciona uma Target (possível destino dos clientes que saem da fila) na fila
    public void addTarget(String queue, double probability) {
        Target newTarget = new Target(probability, queue);

        // Se targets tem tamanho 0, nenhuma Target foi criada previamente
        if (targets.size() == 0) {
            // Cria um Target vazia, indicando que 100% dos clientes irão sair da fila e do modelo
            targets.add(new Target()); 
        }

        int targetsSize = targets.size();
        // Se a probabilidade da nova Target criada é de 100%
        if (newTarget.prob == 1.0) {
            // Substitui a Target vazia criada anteriormente pela nova Target
            // Agora 100% dos clientes que saem da fila vão para a fila indicada na nova Target
            targets.get(targetsSize -1).target = newTarget.target;
        } 
        else {
            // Se existe mais de 1 target no array de Targets
            // OBS.: nesses casos, a ultima Target do array sempre é a Target vazia criada inicialmente
            if (targetsSize > 1) {
                // adiciona a probabilidade da penultima Target na probabilidade da nova Target
                newTarget.prob += targets.get(targetsSize-2).prob;
            }
            // adiciona a nova Target no array na penultima posição
            targets.add(targetsSize -1, newTarget);
        }
    }

    // Verifica se existe mais de 1 possível destino para os clientes que saem da fila
    public boolean shouldGenerateRndNumber() {
        // Se targets tem tamanho 1 existe 100% de chance de o cliente ir para outra fila
        // Se targets tem tamanho 0 existe 100% de chance de o cliente sair da fila e do modelo
        if (targets.size() <= 1) {
            return false;
        }
        return true;
    }

    // Verifica para o cliente vai ir ao sair da fila, de acordo com a probabilidade recebida
    public String getDestination(double probability) {
        if (targets.size() != 0) {
            for(Target target: targets) {
                if (probability <= target.prob) { 
                    return target.target;
                }
            }
        }
        return "";
    }

    public void computeQueueState(double currentTime) {
        // Compara o tempo atual da simulação com a última vez que foi computado o estado da fila
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
    }
}