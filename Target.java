public class Target {
    double prob; // "Probabilidade" de ir para aquele destino
    String target; // Nome da fila

    public Target(double prob, String target) {
        this.prob = prob;
        this.target = target;
    }

    public Target() {
        this.prob = 1.0;
        this.target = ""; // String vazia indica que o cliente vai sair do esquema de filas
    }
}