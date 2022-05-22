package TRIVIA_GAME;

public class Usuario {
    private String nome;
    int id;
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

    public Usuario (String nome, int id){
        this.nome = nome;
        this.id = id;
    }
    
    /**
     * Retorna nome do usuário.
     * @return retorna o nome
     */
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

     /**
     * Retorna id do usuário.
     * @return retorna o id
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
