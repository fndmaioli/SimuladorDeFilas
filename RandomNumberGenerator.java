
public class RandomNumberGenerator {
    
    public double seed;
    public int c = 7954281;
    public int a = 273160675;
    public double max = Math.pow(2, 32);

    public RandomNumberGenerator(){
        this.seed = java.time.LocalTime.now().getNano()/1000000;
    }

    public RandomNumberGenerator(double seed){
        this.seed = seed;
    }

    public double generateRandomNumber() {
        seed = (a * seed + c) % max;
        return seed / max;
    }

    public double generateNewEventTime(double min, double max) {
        double res = (max - min) * generateRandomNumber() + min;
        return res;
    }

}