package TriviaBot;


public class Admin extends Usuario {

    private static final String loginPadrao = "admin";
    private static final String senhaPadrao = "1234";

    /**
     * Construtor Admin.
     *
     * @param nome nome do usuario admin
     */
    public Admin(String nome) {
        super(nome);
    }

    public static boolean adminValido(String login, String senha) {
        return login.equals(loginPadrao) && senha.equals(senhaPadrao);
    }

}