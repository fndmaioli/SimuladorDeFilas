import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

public class FileReader {

    private HashMap<String, Queue> queueNetwork = new HashMap<String, Queue>();
    private ArrayList<Event> events = new ArrayList<Event>();
    private ArrayList<Double> seeds = new ArrayList<Double>();
    private int rndNumPerSeed = 100000;

    public FileReader() {}

    public int getNumbersPerSeed() {
        return rndNumPerSeed;
    }

    // retorna uma nova instancia do Array de seeds para cada simulação
    public ArrayList<Double> getSeeds() {
        return new ArrayList<Double>(this.seeds);
    }

    // retorna uma nova instancia do Array com cada Event pre-agendado
    public ArrayList<Event> getEvents() {
        ArrayList<Event> auxArray = new ArrayList<Event>();
        for(Event ev: events) {
            auxArray.add(new Event(ev.type, ev.time, ev.queue));
        }
        return auxArray;
    }
    
    // retorna uma nova instancia do HashMap com cada Queue
    public HashMap<String, Queue> getQueueNetwork() {

        HashMap<String, Queue> auxMap = new HashMap<String, Queue>();

        for(String queueKey: this.queueNetwork.keySet()) {
            Queue auxQueue1 = this.queueNetwork.get(queueKey);
            Queue auxQueue2 = new Queue(queueKey);
            auxQueue2.targets = auxQueue1.targets;
            auxQueue2.servers = auxQueue1.servers;
            auxQueue2.capacity = auxQueue1.capacity;
            auxQueue2.minArrival = auxQueue1.minArrival;
            auxQueue2.maxArrival = auxQueue1.maxArrival;
            auxQueue2.minService = auxQueue1.minService;
            auxQueue2.maxService = auxQueue1.maxService;
            auxMap.put(queueKey, auxQueue2);
        }
        return auxMap;
    }

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
                else if (line.startsWith("seeds:")) {
                    readSeedsInput(input);
                }
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }

    public void readSeedsInput(Scanner input) {
        String line = "a";

        while(!line.isBlank() && input.hasNextLine()) {

            line = input.nextLine().strip();
            if (line.startsWith("#") || line.isBlank()) {
                continue;
            }

            if (line.startsWith("RandomNumbersPerSeed:")) {
                String[] lineArray = line.split(":");
                this.rndNumPerSeed = Integer.parseInt(lineArray[1].strip());
            } 
            else {
                double auxSeed = Double.parseDouble(line.strip());
                seeds.add(auxSeed);
            }
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
            String arrivalQueue = lineArray[0].strip();

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
                currentQueueName = lineArray[1].strip();
                Queue newQueue = new Queue(currentQueueName);
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