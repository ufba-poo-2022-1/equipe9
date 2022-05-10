public class Usuario {
    private String nome, login, tipo;
    int senha;

    public Usuario (String nome, String login, int senha, String tipo){
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(int senha) {
        this.senha = senha;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
