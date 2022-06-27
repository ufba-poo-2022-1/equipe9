package triviabot;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import static triviabot.Usuario.existe;

public class Discordbot extends ListenerAdapter {

    private static final Logger LOGGER = Logger.getLogger(Discordbot.class.getName());
    private final List<Jogador> jogadores = new ArrayList<>();
    private final List<Admin> admins = new ArrayList<>();
    private final Comandos comandos = new Comandos();
    private final Comparator<Jogador> comparator = new Rank();
    private List<Pergunta> perguntas = new ArrayList<>();
    /**
     * Variaveis para controle do jogo
     */
    private boolean gameStatus = false;
    private boolean espera = true;
    private int quantidadedeperguntas = 0;

    public static Logger getLogger() {
        return LOGGER;
    }

    public static void main(String[] args) {

        /* Construtor para o BOT */
        try (BufferedReader ler = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get("token.txt"))))) {
            /* O token da conta para login. Esse token foi criado em https://discord.com/developers/applications*/

            String token = new String(ler.readLine().getBytes(), StandardCharsets.UTF_8);

            JDA jda = JDABuilder.createDefault(token)
                    /* A instancia da classe que vai cuidar dos eventos*/
                    .addEventListeners(new Discordbot())
                    .build();
            jda.awaitReady(); /* Garante que o JDA tenha carregado completamente*/
            Discordbot.LOGGER.log(Level.INFO, "Finished Building JDA!");
        } catch (LoginException | IOException e) {
            /* Excecao em que algo da errado com o login*/
            Discordbot.LOGGER.log(Level.SEVERE, Arrays.toString(e.getStackTrace()), e);
        } catch (InterruptedException e) {
            /* Como o metodo awaitRedy e um metodo que faz o bloqueio,
              a espera pode ser interrompida.
              Essa excecao ocorre nessa situacao. */
            Discordbot.LOGGER.log(Level.SEVERE, Arrays.toString(e.getStackTrace()), e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * O metodo abaixo e atualizado constantemente monitorando as mensagens e tomando as acoes
     *
     * @param event O parametro event e usado para captar todas as mensagens no chat.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        /*informacoes de eventos especificos*/
        User author = event.getAuthor();                //Usuario que mandou mensagem
        Message message = event.getMessage();           //A mensagem que foi recebida.
        MessageChannel channel = event.getChannel();    //O canal no qual a mensagem foi enviada

        String msg = message.getContentDisplay();       //Retorna a mensagem de uma forma que podemos ler.

        if (event.isFromType(ChannelType.TEXT)) {

            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();
            Member member = event.getMember();

            String name;
            if (!message.isWebhookMessage() && member != null) {
                name = member.getEffectiveName();
            } else {
                name = author.getName();
            }

            /*Cria a mensagem no terminal atraves dos dados coletados do discord. Vai exibir o nome do server
             o nome do usuario (efetivo ou exibido, no caso de o servidor permitir trocar o nick internamente)*/
            String logMsg = "(" + guild.getName() + ")[" + textChannel.getName() + "]<" + name + ">: " + msg + "\n";
            Discordbot.LOGGER.log(Level.INFO, logMsg);
        } else if (event.isFromType(ChannelType.PRIVATE)) {

            //idem, mas para canal privado
            String logMsg = "[PRIV]<" + author.getName() + ">: " + msg;
            Discordbot.LOGGER.log(Level.INFO, logMsg);
        }

        Member member = event.getMember();
        String[] comandoCompleto = msg.split(" ");
        String comandoExecutar; //tratamento da string de entada para o caso do comando !login-admin que recebe mais de uma palavra
        if (comandoCompleto[0].equalsIgnoreCase("!login-admin")) {
            comandoExecutar = "!login-admin";
        } else {
            comandoExecutar = msg;
        }
        switch (comandos.codigoDoComando(comandoExecutar)) {

            case 0: //comando !ping
            {
                channel.sendMessage("pong!").queue(); //O queue() faz a gestao do rate limit automaticamente
                break;
            }

            case 1:    //comando !roll
            {
                Random rand = ThreadLocalRandom.current();
                int roll = rand.nextInt(6) + 1;
                channel.sendMessage("Your roll: " + roll)
                        .flatMap(
                                v -> roll < 3,
                                sentMessage -> channel.sendMessage("The roll for messageId: " + sentMessage.getId() + " wasn't very good... Must be bad luck!\n")
                        )
                        .queue();
                break;
            }

            case 2: //comando !whoami
                // Mesagem para retornar os dados do usuario
            {
                if (member != null) {
                    channel.sendMessage(
                            "Your ID: " + member.getId() +
                                    "\n Your EffectiveName: " + member.getEffectiveName() +
                                    "\n Your Nickname: " + member.getNickname() +
                                    "\n As Mention" + member.getAsMention()
                    ).queue();
                }
                break;
            }

            case 3: //comando !start
                /* Inicio do jogo
                 Comando !Start inicia o jogo caso ainda nao tenha sido iniciado*/
            {
                if (member != null) {
                    if (!gameStatus) {
                    /*
                      Carregar as perguntas do arquivo TXT na lista perguntas
                     */

                        ListaPerguntas listaDePerguntas = new ListaPerguntas();
                        perguntas.clear();
                        perguntas = listaDePerguntas.getPerguntas();
                        Jogador jogadorSelecionado = (Jogador) existe(member.getEffectiveName(), jogadores);

                        if (jogadorSelecionado != null && jogadorSelecionado.getIsLogged()) {
                            channel.sendMessage(
                                    member.getEffectiveName() + " iniciou o jogo!" +
                                            "\nPara saber as regras digite !regras" +
                                            "\nPara ver o ranking digite !rank " +
                                            "\nBoa sorte!"
                            ).queue();
                            gameStatus = true;
                        } else {
                            channel.sendMessage(
                                    member.getEffectiveName() + " voc� n�o est� logado!" +
                                            "\ndigite !login para entrar no game"
                            ).queue();
                        }

                    } else {
                        //Envia mensagem dizendo que o jogo ja foi iniciado
                        channel.sendMessage(
                                member.getEffectiveName() + " O jogo j� foi iniciado."
                        ).queue();

                    }
                }
                break;
            }

            case 4: //comando !stop
                //O Comando !Stop encerra o jogo e retorna as variaveis de controle aos valores iniciais
            {
                if (member != null) {
                    if (gameStatus) {
                        Jogador jogadorSelecionado = (Jogador) existe(member.getEffectiveName(), jogadores);

                        if (jogadorSelecionado != null && jogadorSelecionado.getIsLogged()) {
                            channel.sendMessage(
                                    member.getEffectiveName() + " encerrou o jogo!" +
                                            "\nCaso queira reiniciar digite !start" +
                                            "\nEsperamos voc�s nos pr�ximos jogos  " +
                                            "\nObrigado por jogar"
                            ).queue();
                            gameStatus = false;
                            quantidadedeperguntas = 0;
                            espera = true;

                        } else {
                            channel.sendMessage(
                                    member.getEffectiveName() + " voc� n�o est� logado!" +
                                            "\ndigite !login para entrar no game"
                            ).queue();
                        }

                    } else {
                        //Caso o jogo ainda nao tenha sido iniciado, envia a mensagem informando
                        channel.sendMessage(
                                member.getEffectiveName() + " o jogo ainda n�o foi iniciado."
                        ).queue();
                    }
                }
                break;
            }

            case 5: //comando !regras
                //Comando para exibir as regras do jogo
            {
                channel.sendMessage(
                        " Regras do Trivia Game:" +
                                "\n\nDigite !cadastrar para se cadastrar no jogo:" +
                                "\nDigite !login para fazer login no jogo" +
                                "\nDigite !start para iniciar o jogo" +
                                "\nDigite !repete para repetir a �ltima pergunta feita no jogo" +
                                "\nDigite !stop para encerrar o jogo " +
                                "\nDigite !login-admin <login> <senha> para fazer login como admin " +
                                "\nDigite !reset-ranking para zerar a pontua��o de todos os jogadores (apenas para admins) " +
                                "\nDigite !logout-admin para deslogar como admin (apenas para admins) " +
                                "\n\nO jogo consiste em um quiz de perguntas e respostas." +
                                " O bot ir� fazer uma pergunta e o primeiro jogador do canal a dar a resosta certa ganha ponto." +
                                "\nA resposta deve corresponder a uma das op��es apresentadas na quest�o." +
                                "\nOs jogadores v�o somando pontos e ao final do game ser� exibido o rank."
                ).queue();
                break;
            }

            case 6: //comando !repete
            {
                if (gameStatus) {
                    channel.sendMessage(
                            "Por favor responda a seguinte quest�o:\n" +
                                    perguntas.get(quantidadedeperguntas).getPergunta() + "\n" +
                                    perguntas.get(quantidadedeperguntas).stringAlternativas() + "\n"
                    ).queue();
                }
                break;
            }

            case 7: //comando !cadastrar
                // Comando para se cadastrar no jogo

            {
                if (member != null) {
                    Jogador jogadorSelecionado = (Jogador) existe(member.getEffectiveName(), jogadores);

                    if (jogadorSelecionado != null) {
                        channel.sendMessage(
                                member.getEffectiveName() +
                                        " voc� j� est� cadastrado. Use o comando !login para entrar"
                        ).queue();
                    } else {
                        Jogador jogadorAtual = new Jogador(member.getEffectiveName());
                        channel.sendMessage(
                                member.getEffectiveName() +
                                        " voc� foi cadastrado com sucesso."
                        ).queue();
                        jogadores.add(jogadorAtual);
                    }
                }
                break;
            }

            case 8: //comando !login
                // Comando para fazer login
            {
                if (member != null) {
                    Jogador jogadorSelecionado = (Jogador) existe(member.getEffectiveName(), jogadores);

                    if (jogadorSelecionado != null && !jogadorSelecionado.getIsLogged()) {
                        jogadorSelecionado.setIsLogged(true);
                        channel.sendMessage(
                                member.getEffectiveName() +
                                        " voc� fez login com sucesso!"
                        ).queue();
                    } else if (jogadorSelecionado != null && jogadorSelecionado.getIsLogged()) {
                        channel.sendMessage(
                                member.getEffectiveName() +
                                        " voc� j� est� logado"
                        ).queue();
                    } else {
                        channel.sendMessage(
                                member.getEffectiveName() +
                                        ", n�o conseguimos encontrar seu usu�rio. " +
                                        "Use o comando !cadastrar para criar um usu�rio."
                        ).queue();
                    }
                }
                break;
            }

            case 9: //comando !logout
                // Comando para fazer logout
            {
                if (member != null) {
                    Jogador jogadorSelecionado = (Jogador) existe(member.getEffectiveName(), jogadores);

                    if (jogadorSelecionado != null && jogadorSelecionado.getIsLogged()) {
                        jogadorSelecionado.setIsLogged(false);
                        channel.sendMessage(
                                member.getEffectiveName() +
                                        " voc� fez logout com sucesso!"
                        ).queue();
                    } else {
                        channel.sendMessage(
                                member.getEffectiveName() +
                                        " voc� ainda n�o fez login." +
                                        " use o comando !login para entrar."
                        ).queue();
                    }
                }
                break;
            }

            case 10: //comando !rank
            {
                jogadores.sort(comparator);

                channel.sendMessage(
                        "Segue o ranking atual: \n").queue();
                for (Jogador jogador : jogadores) {
                    channel.sendMessage(
                            jogador.getNome() + " - Pontos:" + jogador.getPontuacao() + "\n").queue();
                }
                break;
            }

            case 11: //comando !login-admin
            {
                if (member != null) {
                    String nome = member.getEffectiveName();
                    String[] strings = msg.split(" ");
                    if (existe(nome, admins) != null) {
                        channel.sendMessage("Voc� j� est� logado como admin.\n").queue();
                    } else if (strings.length == 3) {
                        String login = strings[1];
                        String senha = strings[2];

                        if (Admin.adminValido(login, senha)) {
                            admins.add(new Admin(nome));
                            channel.sendMessage("Login de admin efetuado com sucesso!\n").queue();
                        } else {
                            channel.sendMessage("Login e/ou senha inv�lidos.\n").queue();
                        }
                    } else {
                        channel.sendMessage("Par�metros inv�lidos.\n").queue();
                    }
                }
                break;
            }

            case 12: //comando !logout-admin
            {
                if (member != null) {
                    String nome = member.getEffectiveName();
                    Admin admin = (Admin) existe(nome, admins);

                    if (admin != null) {
                        admins.remove(admin);
                        channel.sendMessage(nome + ", voc� fez logout como admin com sucesso!\n").queue();
                    } else {
                        channel.sendMessage("Voc� n�o est� logado como admin.\n")
                                .queue();
                    }
                }
                break;
            }

            case 13: //comando !reset-ranking
            {
                if (member != null) {
                    String nome = member.getEffectiveName();

                    if (existe(nome, admins) != null) {
                        Jogador.resetRanking(jogadores);
                        channel.sendMessage("O ranking foi resetado com sucesso!\n").queue();

                    } else {
                        channel.sendMessage("Voc� n�o tem permiss�o para fazer isso.\n" +
                                        "Use !login-admin <login> <senha> e tente novamente.\n")
                                .queue();
                    }
                }
                break;
            }

            case -1: //comando inválido
            {
                if (member != null && !author.isBot() && msg.startsWith("!")) {
                    channel.sendMessage(member.getEffectiveName() +
                                    ", n�o consegui compreender este comando. Se estiver" +
                                    " com duvidas sobre os comandos, digite !regras.\n")
                            .queue();
                }
                break;
            }

            default:
                break;

        }
        trivia(msg, event);
    }

    private void trivia(String msg, MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        //Lan�a a quest�o armezenada em perguntas
        String s;
        if (gameStatus && quantidadedeperguntas < perguntas.size() && espera) {
            s = "Por favor responda a seguinte quest�o:\n" +
                    perguntas.get(quantidadedeperguntas).getPergunta() + "\n" +
                    perguntas.get(quantidadedeperguntas).stringAlternativas() + "\n";

            espera = false;
            channel.sendMessage(s).queue();
        }

        //recebe a resposta dos jogadores e avaliar se � certa ou n�o, adicionando pontos
        if (gameStatus && quantidadedeperguntas < perguntas.size() && !espera) {

            Member member = event.getMember();
            if (member != null) {
                Jogador jogadorSelecionado = (Jogador) existe(member.getEffectiveName(), jogadores);

                boolean isLogged = jogadorSelecionado != null && jogadorSelecionado.getIsLogged();

                if (isLogged &&
                        msg.equalsIgnoreCase(perguntas.get(quantidadedeperguntas).getResposta())) {
                    jogadorSelecionado.setPontuacao(jogadorSelecionado.getPontuacao() + 1);
                    s =
                            "Certo! " + member.getEffectiveName() + ", voc� tem "
                                    + jogadorSelecionado.getPontuacao() + " pontos.\n";
                    espera = true;
                    quantidadedeperguntas++;
                    channel.sendMessage(s).queue();
                } else if ((isLogged && (msg.equalsIgnoreCase("A") || msg.equalsIgnoreCase("B") ||
                        msg.equalsIgnoreCase("C") || msg.equalsIgnoreCase("D")))
                ) {
                    s = "Errooou! " + member.getEffectiveName() + ", voc� tem "
                            + jogadorSelecionado.getPontuacao() + " pontos.\n";
                    espera = true;
                    quantidadedeperguntas++;
                    channel.sendMessage(s).queue();
                }
            }
        }


        //Avalia o vencedor e envia mensagem informando o vencedor
        if (gameStatus && quantidadedeperguntas == perguntas.size() && espera) {

            jogadores.sort(comparator);

            StringBuilder str = new StringBuilder("Acabaram as perguntas! Segue o ranking atual: \n");

            for (Jogador jogador : jogadores) {
                str.append(jogador.getNome()).append(" - Pontos:").append(jogador.getPontuacao()).append("\n");
            }
            str.append("\nPara iniciar o jogo novamente e somar mais pontos, use !start");
            gameStatus = false;
            quantidadedeperguntas = 0;
            channel.sendMessage(str).queue();
        }
    }
}
