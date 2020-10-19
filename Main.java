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
            
            for (int i = 0; i<5; i++) {
                HashMap<String, Queue> cleanQueueNetwork = fileReader.getQueueNetwork();
                ArrayList<Event> cleanEvents = fileReader.getEvents();
                QueueController queueController = new QueueController(cleanQueueNetwork, cleanEvents);
                queueController.runSimulation();
            }
            
        } else {
            System.out.println("Insira um nome de arquivo válido");
        }
    }    
}