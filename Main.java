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

            FileReader fileReader = new FileReader();
            fileReader.readInputFile(file);

            ResultsTracker rt = new ResultsTracker();
            if (fileReader.getSeeds().size() == 0) {
                HashMap<String, Queue> cleanQueueNetwork = fileReader.getQueueNetwork();
                ArrayList<Event> cleanEvents = fileReader.getEvents();
                QueueController queueController = new QueueController(cleanQueueNetwork, cleanEvents, fileReader.getNumbersPerSeed());
                queueController.runSimulation();
                rt.addResults(queueController.queueNetwork);
                rt.addTime(queueController.time);
            }
            else {
                for (double seed : fileReader.getSeeds()) {
                    HashMap<String, Queue> cleanQueueNetwork = fileReader.getQueueNetwork();
                    ArrayList<Event> cleanEvents = fileReader.getEvents();
                    QueueController queueController = new QueueController(cleanQueueNetwork, cleanEvents, seed, fileReader.getNumbersPerSeed());
                    queueController.runSimulation();
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