
public class RandomNumberGenerator {
    
    public double seed;
    public int c = 1013904223;
    public int a = 1664525;
    public double max = Math.pow(2, 32);
    public int count;

    public RandomNumberGenerator(int numPerSeed){
        this.seed = java.time.LocalTime.now().getNano()/1000000;
        this.count = numPerSeed;
    }

    public RandomNumberGenerator(double seed, int numPerSeed){
        this.seed = seed;
        this.count = numPerSeed;
    }

    private double generateRandomNumber() {
        seed = (a * seed + c) % max;
        return seed / max;
    }

    public double generateNewEventTime(double min, double max) {
        this.count--;
        double res = (max - min) * generateRandomNumber() + min;
        return res;
    }

}