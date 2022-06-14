package TRIVIA_GAME;

public class Opcao {
    private String label;
    private Runnable metodo;

    /**
     * Construtor sem parametros.
     *
     */

    public Opcao() {
    }
    /**
     * Construtor Opcao.
     * @param label 
     * @param metodo
     */
    public Opcao(String label, Runnable metodo) {
        this.label = label;
        this.metodo = metodo;
    }
    /**
     * Retorna senha do usuário.
     * @return retorna label
     */
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    /**
     * Retorna senha do usuário.
     * @return retorna metodo
     */
    public Runnable getMetodo() {
        return metodo;
    }

    public void setMetodo(Runnable metodo) {
        this.metodo = metodo;
    }
}
