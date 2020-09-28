import java.io.IOException;
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
            
            QueueController queueController = new QueueController(fileReader.queueNetwork, fileReader.events);
            queueController.runSimulation();
        } else {
            System.out.println("Insira um nome de arquivo válido");
        }
    }    
}