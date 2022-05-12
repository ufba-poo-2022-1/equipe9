package TRIVIA_GAME;

public class Opcao {
    private String label;
    private Runnable metodo;

    public Opcao() {
    }

    public Opcao(String label, Runnable metodo) {
        this.label = label;
        this.metodo = metodo;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Runnable getMetodo() {
        return metodo;
    }

    public void setMetodo(Runnable metodo) {
        this.metodo = metodo;
    }
}
