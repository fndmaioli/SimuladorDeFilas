import java.text.DecimalFormat;
import java.util.HashMap;

public class ResultsTracker {

    public HashMap<String, HashMap<Integer, Double>> queuesResult = new HashMap<String, HashMap<Integer, Double>>();
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
                    if (queuesResult.get(queueKey).containsKey(stateKey)) {
                        newVal += queuesResult.get(queueKey).get(stateKey);
                        queuesResult.get(queueKey).replace(stateKey, newVal);
                    } 
                    else {
                        queuesResult.get(queueKey).put(stateKey, newVal);
                    }
                }
            } 
            else {
                queuesResult.put(queueKey, newResults.get(queueKey).queueStates);
            }
        }
    }

    public void addTime(Double time) {
        this.numberOfSimulations++;
        this.time += time;
    }

    public void printResults() {

        System.out.println("Tempo total: " + this.time/5);
        for(String keyName : queuesResult.keySet()) {
            System.out.println("Probabilidade de cada estado da fila: " + keyName);
            for(Integer key : queuesResult.get(keyName).keySet()) {
                if (key >= 0) {
                    System.out.println(key + "   -   tempo: " + new DecimalFormat("#.##").format(queuesResult.get(keyName).get(key)/this.numberOfSimulations) + "   -   " + new DecimalFormat("#.##").format((queuesResult.get(keyName).get(key) * 100 )/this.time) + "%");
                }
            }
            if (queuesResult.get(keyName).containsKey(-1)) {
                System.out.println("Numero de perdas: " + (int) (queuesResult.get(keyName).get(-1)/this.numberOfSimulations));
            } else {
                System.out.println("Numero de perdas: " + 0);
            }
        }

    }
}