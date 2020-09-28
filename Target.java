public class Target {
    double prob;
    String target; 

    public Target(double prob, String target) {
        this.prob = prob;
        this.target = target;
    }

    public Target() {
        this.prob = 1.0;
        this.target = ""; 
    }
}