import java.text.DecimalFormat;
import java.util.HashMap;

public class ResultsTracker {

    public HashMap<String, HashMap<Integer, Double>> queuesResult = new HashMap<String, HashMap<Integer, Double>>();

    public ResultsTracker() {

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

    public void printResults() {

        // System.out.println("Tempo total: " + this.time);
        // for(String keyName : queueNetwork.keySet()) {
        //     System.out.println("Probabilidade de cada estado da fila: " + keyName);
        //     for(Integer key : queueNetwork.get(keyName).queueStates.keySet()) {
        //         if (key >= 0) {
        //             System.out.println(key + "   -   tempo: " + new DecimalFormat("#.##").format(queueNetwork.get(keyName).queueStates.get(key)) + "   -   " + new DecimalFormat("#.##").format((queueNetwork.get(keyName).queueStates.get(key) * 100 )/this.time) + "%");
        //         }
        //     }
        //     if (queueNetwork.get(keyName).queueStates.containsKey(-1)) {
        //         System.out.println("Numero de perdas: " + queueNetwork.get(keyName).queueStates.get(-1));
        //     } else {
        //         System.out.println("Numero de perdas: " + 0);
        //     }
        // }

    }
}