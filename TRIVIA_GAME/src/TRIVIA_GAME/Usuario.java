package TRIVIA_GAME;

public class Usuario {
    private String nome, login, tipo;
    String senha;

    /**
     * Construtor sem parametros.
     */
    public Usuario(){
    }

    /**
     * Construtor usuário.
     *
     * @param nome nome do usuário 
     * @param login login de acesso do usuário 
     * @param senha senha de acesso do usuário 
     * @param tipo identifica o tipo de usuário 
     */

    public Usuario (String nome, String login, String senha, String tipo){
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.tipo = tipo;
    }
    
    /**
     * Retorna nome do usuário.
     * @param nome nome do usuário
     * @return retorna o nome
     */
    public String getNome() {
        return nome;
    }
     /**
     * Retorna login do usuário.
     * @param login login do usuário
     * @return retorna o login
     */
    public String getLogin() {
        return login;
    }
     /**
     * Retorna senha do usuário.
     * @param senha senha do usuário
     * @return retorna a senha
     */

    public String getSenha() {
        return senha;
    }
     /**
     * Retorna tipo do usuário.
     * @param tipo tipo do usuário
     * @return retorna o tipo
     */
    public String getTipo() {
        return tipo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
