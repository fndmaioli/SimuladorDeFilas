
public class RandomNumberGenerator {
    
    public double seed;
    public int c = 1013904223;
    public int a = 1664525;
    public double max = Math.pow(2, 32);
    public int count;

    // Caso uma seed não seja especifica, cria ela com base nos nanosegundos atuais
    public RandomNumberGenerator(int numPerSeed){
        this.seed = java.time.LocalTime.now().getNano()/1000000;
        this.count = numPerSeed;
    }

    // Cria uma instancia com a seed especificada
    public RandomNumberGenerator(double seed, int numPerSeed){
        this.seed = seed;
        this.count = numPerSeed;
    }

    private double generateRandomNumber() {
        seed = (a * seed + c) % max;
        return seed / max;
    }

    public double generateNewEventTime(double min, double max) {
        this.count--; // Diminui o count sempre que gera um número aleatório para a simulação
        double res = (max - min) * generateRandomNumber() + min;
        return res;
    }

}