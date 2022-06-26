package TriviaBot;

import java.util.ArrayList;

public class Usuario {

    protected String nome;
    boolean logged = false;
    int id;

    /**
     * Construtor usuario.
     *
     * @param nome nome do usuario
     */

    public Usuario(String nome) {
        this.nome = nome;
    }

    public static Usuario existe(String nome, ArrayList<? extends Usuario> usuarios) {
        for (Usuario usuario : usuarios) {
            if (usuario.nome.equals(nome)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Retorna nome do usuario.
     *
     * @return retorna o nome
     */
    public String getNome() {
        return nome;
    }


    public void setId(int id) {
        this.id = id;
    }


    /**
     * Retorna status do login.
     *
     * @return true caso o usuario tenha um login valido.
     */
    public boolean getIsLogged() {
        return this.logged;
    }

    public void setIsLogged(boolean logged) {
        this.logged = logged;
    }

}
