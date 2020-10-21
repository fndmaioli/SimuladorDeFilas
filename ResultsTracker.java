import java.text.DecimalFormat;
import java.util.HashMap;

public class ResultsTracker {

    public HashMap<String, Queue> queuesResult = new HashMap<String, Queue>();
    public Double time;
    public int numberOfSimulations = 0;

    public ResultsTracker() {
        this.time = 0.0;
    }

    public void addResults(HashMap<String, Queue> newResults) {
        for(String queueKey : newResults.keySet()) {
            if (queuesResult.containsKey(queueKey)) {

                for(Integer stateKey : newResults.get(queueKey).queueStates.keySet()) {
                    Double newVal = newResults.get(queueKey).queueStates.get(stateKey);
                    if (queuesResult.get(queueKey).queueStates.containsKey(stateKey)) {
                        newVal += queuesResult.get(queueKey).queueStates.get(stateKey);
                        queuesResult.get(queueKey).queueStates.replace(stateKey, newVal);
                    } 
                    else {
                        queuesResult.get(queueKey).queueStates.put(stateKey, newVal);
                    }
                }
            } 
            else {
                queuesResult.put(queueKey, newResults.get(queueKey));
            }
        }
    }

    public void addTime(Double time) {
        this.numberOfSimulations++;
        this.time += time;
    }

    public void printResults() {

        System.out.println("Tempo total: " + this.time/this.numberOfSimulations);
        for(String keyName : queuesResult.keySet()) {
            System.out.println("Probabilidade de cada estado da fila: " + keyName);
            for(Integer key : queuesResult.get(keyName).queueStates.keySet()) {
                if (key >= 0) {
                    System.out.println(key + "   -   tempo: " + new DecimalFormat("#.##").format(queuesResult.get(keyName).queueStates.get(key)/this.numberOfSimulations) + "   -   " + new DecimalFormat("#.##").format((queuesResult.get(keyName).queueStates.get(key) * 100)/this.time) + "%");
                }
            }
            if (queuesResult.get(keyName).queueStates.containsKey(-1)) {
                System.out.println("Numero de perdas: " + (int) (queuesResult.get(keyName).queueStates.get(-1)/this.numberOfSimulations));
            } else {
                System.out.println("Numero de perdas: " + 0);
            }
            double ap = averagePopulation(queuesResult.get(keyName));
            double fr = flowRate(queuesResult.get(keyName));
            utilization(queuesResult.get(keyName));
            responseTime(ap, fr);
        }
    }

    // Cálculo da Populacao Média
    public double averagePopulation(Queue queue) {
        double sum = 0.0;
        for(Integer key : queue.queueStates.keySet()) {
            if (key == 0 || key == -1) { continue; }
            sum += key * (queue.queueStates.get(key)/this.time);
        }
        System.out.println("População média: " + new DecimalFormat("#.##").format(sum));
        return sum;
    }

    // Cálculo da Vazão
    public double flowRate(Queue queue) {
        double sum = 0.0;
        for(Integer key : queue.queueStates.keySet()) {
            if (key == 0 || key == -1) { continue; }
            sum += (queue.queueStates.get(key)/this.time) * ((queue.maxService + queue.minService)/2);
        }
        System.out.println("Vazão: " + new DecimalFormat("#.##").format(sum));
        return sum;
    }

    // Cálculo da Utilização
    public void utilization(Queue queue) {
        double sum = 0.0;
        for(Integer key : queue.queueStates.keySet()) {
            if (key == 0 || key == -1) { continue; }
            sum += (queue.queueStates.get(key)/this.time) * (Integer.min(key, (int) queue.capacity)/(int) queue.capacity);
        }
        System.out.println("Utilização: " + new DecimalFormat("#.##").format(sum));
    }

    // Cálculo do Tempo de Resposta
    public void responseTime(double averagePopulation, double flowRate) {
        System.out.println("Tempo de resposta: " + new DecimalFormat("#.##").format(averagePopulation/flowRate));
    }
}