package triviabot;


public class Admin extends Usuario {

    private static final String LOGIN_PADRAO = "admin";
    private static final String SENHA_PADRAO = "1234";

    /**
     * Construtor Admin.
     *
     * @param nome nome do usuario admin
     */
    public Admin(String nome) {
        super(nome);
    }

    public static boolean adminValido(String login, String senha) {
        return login.equals(LOGIN_PADRAO) && senha.equals(SENHA_PADRAO);
    }

}