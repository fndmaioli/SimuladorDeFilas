import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

public class FileReader {

    public HashMap<String, Queue> queueNetwork = new HashMap<String, Queue>();
    public ArrayList<Event> events = new ArrayList<Event>();

    public FileReader() {}

    public void readInputFile(File file) {
        try {
            Scanner input = new Scanner(file);
            String line;
    
            while(input.hasNextLine()) {
    
                line = input.nextLine().strip();
                if (line.startsWith("#")) {
                    continue;
                }

                if (line.startsWith("queues:")) {
                    readQueuesInput(input);
                }
                else if (line.startsWith("arrivals:")) {
                    readArrivalsInput(input);
                }
                else if (line.startsWith("network:")) {
                    readNetworkInput(input);
                }
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }

    public void readArrivalsInput(Scanner input) {
        String line = "a";

        while(!line.isBlank() && input.hasNextLine()) {
            
            line = input.nextLine().strip();
            if (line.startsWith("#") || line.isBlank()) {
                continue;
            }

            String[] lineArray = line.split(":");
            
            Double arrivalTime = Double.parseDouble(lineArray[1].strip());
            Queue arrivalQueue = queueNetwork.get(lineArray[0].strip());

            Event newEvent = new Event(EventType.ARRIVAL, arrivalTime, arrivalQueue);
            events.add(newEvent);
        }
    }

    public void readNetworkInput(Scanner input) {
        String line = "a";

        while(!line.isBlank() && input.hasNextLine()) {
            line = input.nextLine().strip();
            if (line.startsWith("#") || line.isBlank()) {
                continue;
            }

            String[] lineArray = line.split(":");

            String sourceQueueName = lineArray[0];
            String[] array = lineArray[1].split("-");
            String targetQueueName = array[0];
            double probability = Double.parseDouble(array[1]);

            queueNetwork.get(sourceQueueName).addTarget(targetQueueName, probability);
        }
    }

    public void readQueuesInput(Scanner input){
        String line = "a";
        String currentQueueName = "";

        while(!line.isBlank() && input.hasNextLine()) {

            line = input.nextLine().strip();
            if (line.startsWith("#") || line.isBlank()) {
                continue;
            }
            
            String[] lineArray = line.split(":");
            
            if (lineArray[0].equalsIgnoreCase("QueueName")) {
                
                Queue newQueue = new Queue(lineArray[1]);
                currentQueueName = lineArray[1].strip();
                queueNetwork.put(currentQueueName, newQueue);
            } 
            else {
                if (currentQueueName.isBlank()) {
                    System.out.println("Queue does not have a name, fix the input file.");
                    System.exit(0);
                }

                if (lineArray[0].equalsIgnoreCase("servers")) {
                    int serversNumber = Integer.parseInt(lineArray[1].strip());
                    queueNetwork.get(currentQueueName).servers = serversNumber;
                }
                else if (lineArray[0].equalsIgnoreCase("capacity")) {
                    double queueCapacity = Double.parseDouble(lineArray[1].strip());
                    queueNetwork.get(currentQueueName).capacity = queueCapacity;
                }
                else if (lineArray[0].equalsIgnoreCase("minArrival")) {
                    double value = Double.parseDouble(lineArray[1].strip());
                    queueNetwork.get(currentQueueName).minArrival = value;
                }
                else if (lineArray[0].equalsIgnoreCase("maxArrival")) {
                    double value = Double.parseDouble(lineArray[1].strip());
                    queueNetwork.get(currentQueueName).maxArrival = value;
                }
                else if (lineArray[0].equalsIgnoreCase("minService")) {
                    double value = Double.parseDouble(lineArray[1].strip());
                    queueNetwork.get(currentQueueName).minService = value;
                }
                else if (lineArray[0].equalsIgnoreCase("maxService")) {
                    double value = Double.parseDouble(lineArray[1].strip());
                    queueNetwork.get(currentQueueName).maxService = value;
                }
            }
        }
    }
}