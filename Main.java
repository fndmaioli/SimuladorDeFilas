import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            File file = new File(args[0]);

            if (!file.exists()) {
                System.out.println("Insira um nome de arquivo válido");
                return;
            }

            // Lê o arquivo de entrada
            FileReader fileReader = new FileReader();
            fileReader.readInputFile(file);

            ResultsTracker rt = new ResultsTracker();
            // Verifica se foram especificadas seeds no arquivo de entrada
            if (fileReader.getSeeds().size() == 0) {
                // Se não foram especificadas, realiza apenas uma simulação com uma seed criada automaticamente
                HashMap<String, Queue> cleanQueueNetwork = fileReader.getQueueNetwork();
                ArrayList<Event> cleanEvents = fileReader.getEvents();
                QueueController queueController = new QueueController(cleanQueueNetwork, cleanEvents, fileReader.getNumbersPerSeed());
                queueController.runSimulation();
                rt.addResults(queueController.queueNetwork);
                rt.addTime(queueController.time);
            }
            else {
                // Se foram especificadas as seeds faz uma simulação para cada seed
                for (double seed : fileReader.getSeeds()) {
                    HashMap<String, Queue> cleanQueueNetwork = fileReader.getQueueNetwork();
                    ArrayList<Event> cleanEvents = fileReader.getEvents();
                    QueueController queueController = new QueueController(cleanQueueNetwork, cleanEvents, seed, fileReader.getNumbersPerSeed());
                    queueController.runSimulation();
                    // Ao fim de cada simulação, guarda os resultados no ResultsTracker
                    rt.addResults(queueController.queueNetwork);
                    rt.addTime(queueController.time);
                }
            }
            rt.printResults();
            
        } else {
            System.out.println("Insira um nome de arquivo válido");
        }
    }    
}