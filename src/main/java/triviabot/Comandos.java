package triviabot;

import java.util.ArrayList;
import java.util.List;

public class Comandos {

    final List<String> listaDeComandos = new ArrayList<>();

    //Ao iniciar o contrustor dessa classe ela carrega todos os comandos v�lidos na lista
    public Comandos() {
        listaDeComandos.add("!ping");
        listaDeComandos.add("!roll");
        listaDeComandos.add("!whoami");
        listaDeComandos.add("!start");
        listaDeComandos.add("!stop");
        listaDeComandos.add("!regras");
        listaDeComandos.add("!repete");
        listaDeComandos.add("!cadastrar");
        listaDeComandos.add("!login");
        listaDeComandos.add("!logout");
        listaDeComandos.add("!rank");
        listaDeComandos.add("!login-admin");
        listaDeComandos.add("!logout-admin");
        listaDeComandos.add("!reset-ranking");

    }

    public boolean comandoValido(String comando) {
        return listaDeComandos.contains(comando);
    }

    public int codigoDoComando(String comando) {

        if (comandoValido(comando)) {
            return listaDeComandos.indexOf(comando);
        } else {
            return -1;
        }
    }

}
