package TRIVIA_GAME;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Discordbot extends ListenerAdapter{

    public int jogadorId = 0;
    public boolean statuslog = false;
	
    /** Carregar as perguntas do arquivo TXT na lista perguntas */
    ListaPerguntas Perguntas = new ListaPerguntas();
    
    public static ArrayList<Pergunta> perguntas = new ArrayList<>();
    public ArrayList<Jogador> jogadores = new ArrayList<>();
    public ArrayList<Admin> admins = new ArrayList<Admin>();

    /** Variaveis usadas para conversao das linhas UTF-8 do TXT e resolver problemas com acentuacao*/
    public static String linha0;
    public static String linha1;
    public static String linha2;
    public static String linha3;
    public static String linha4;
    public static String linha5;

    /** Variaveis para controle do jogo*/
    boolean gameStatus = false;
    boolean espera = true;
    int quantidadedeperguntas = 0;


    private Comparator<Jogador> comparator = new Rank();
    
    public static void main(String[] args)
    {
  
    	/** Construtor para o BOT */
        try
        {
        	/** O token da conta para login. Esse token foi criado em https://discord.com/developers/applications*/
        	JDA jda = JDABuilder.createDefault("BOT-TOKEN-AQUI")
        			/** A instancia da classe que vai cuidar dos eventos*/
        			.addEventListeners(new Discordbot())
                    .build();
            jda.awaitReady(); /** Garante que o JDA tenha carregado completamente*/
            System.out.println("Finished Building JDA!");
        }
        catch (LoginException e)
        {
        	/** Excecao em que algo da errado com o login*/
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
        	/** como o metodo awaitRedy e um metodo que faz o bloqueio,
             * a espera pode ser interrompida.
             * Essa excecao ocorre nessa situacao. */
            e.printStackTrace();
        }


    
    }
 
	/**acessa o ranking e devolve ordem dos jogadores
	    * @return ordem dos jogadores
	    */
    public Collection<Jogador> getRanking() {
        Collections.sort(jogadores, comparator);
        return Collections.unmodifiableCollection(jogadores);
    }

    	
    /** O metodo abaixo e atualizado constantemente monitorando as mensagens e tomando as acoes
     * @param event O parametro event e usado para captar todas as mensagens no chat.
 	*/
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
    	 /*JDA, o nucleo do API.*/
        JDA jda = event.getJDA(); 
        /*A quantidade de eventos Discord que o JDA Recebeu desde a ultima conexao.   */
        long responseNumber = event.getResponseNumber();

        /**informacoes de eventos especificos*/
        User author = event.getAuthor();                //Usuário que mandou mensagem
        Message message = event.getMessage();           //A mensagem que foi recebida.
        MessageChannel channel = event.getChannel();    //O canal no qual a mensagem foi enviada

        String msg = message.getContentDisplay();       //Retorna a mensagem de uma forma que podemos ler.

        boolean bot = author.isBot();                    //determinar se quem enviou a mensagem é um bot ou não

        if (event.isFromType(ChannelType.TEXT))         
        {
            
            Guild guild = event.getGuild();             
            TextChannel textChannel = event.getTextChannel(); 
            Member member = event.getMember();          

            String name;
            if (message.isWebhookMessage())
            {
                name = author.getName();                
            }                                           
            else
            {
                name = member.getEffectiveName();       
            } 
            
            /**Cria a mensagem no terminal atraves dos dados coletados do discord. Vai exibir o nome do server
             *o nome do usuario (efetivo ou exibido, no caso de o servidor permitir trocar o nick internamente)*/
            System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);
        }
        else if (event.isFromType(ChannelType.PRIVATE)) 
        {
            
            PrivateChannel privateChannel = event.getPrivateChannel();
            
            //idem, mas para canal privado
            System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);
        }


        if (msg.equals("!ping"))
        {
            channel.sendMessage("pong!").queue(); //O queue() faz a gestão do rate limit automaticamente
        }
        else if (msg.equals("!roll")) //comando para sortear um numero de 1 a 6 e mostra uma mensagem se for menor de 3
        {
            Random rand = ThreadLocalRandom.current();
            int roll = rand.nextInt(6) + 1; 
            channel.sendMessage("Your roll: " + roll)
                   .flatMap(
                       (v) -> roll < 3,
                       sentMessage -> channel.sendMessage("The roll for messageId: " + sentMessage.getId() + " wasn't very good... Must be bad luck!\n")
                   )
                   .queue();
        }
        else if (msg.equals("!whoami"))
        {
            // Mesagem para retornar os dados do usuário

            Member member = event.getMember();
            if (member != null)
            {
                channel.sendMessage(
                    "Your ID: " + member.getId() +                          
                    "\n Your EffectiveName: " + member.getEffectiveName() + 
                    "\n Your Nickname: " + member.getNickname() +           
                    "\n As Mention" + member.getAsMention()                 
                ).queue();
            }
        }

        /** Inicio do jogo
         *Comando !Start inicia o jogo caso ainda nao tenha sido iniciado*/
        else if (msg.equals("!start") && !gameStatus)
        {
        	Perguntas = new ListaPerguntas();
        	perguntas = Perguntas.getPerguntas();
        	Member member = event.getMember();
        	if (member != null)
            {
        		Jogador jogadorSelecionado = (Jogador) Jogador.existe(member.getEffectiveName(), jogadores);

                if(jogadorSelecionado != null && jogadorSelecionado.getIsLogged()) {
                channel.sendMessage(
                    member.getEffectiveName() + " iniciou o jogo!" +
                    "\nPara saber as regras digite !regras" +
                    "\nPara ver o ranking digite !rank " + 
                    "\nBoa sorte!"
                ).queue();
                gameStatus = true;
            }
            	else {
            		channel.sendMessage(
                            member.getEffectiveName() + " você não está logado!" +
                            "\ndigite !login para entrar no game"
                        ).queue();
            	}	
            }
        }
        
        //Envia mensagem dizendo que o jogo já foi iniciado
        else if (msg.equals("!start") && gameStatus)
        {
            Member member = event.getMember();
            if (member != null)
            {
                channel.sendMessage(
                    member.getEffectiveName() + " O jogo já foi iniciado."
                ).queue();
            }
        }
        
        //O Comando !Stop encerra o jogo e retorna as variaveis de controle aos valores iniciais
        else if (msg.equals("!stop") && gameStatus)
        {
            Member member = event.getMember();
            if (member != null) 
            {
            	Jogador jogadorSelecionado = (Jogador) Jogador.existe(member.getEffectiveName(), jogadores);

            	  if(jogadorSelecionado != null && jogadorSelecionado.getIsLogged()) {
            	channel.sendMessage(
                    member.getEffectiveName() + " encerrou o jogo!" +
                    "\nCaso queira reiniciar digite !start" +
                    "\nEsperamos vocês nos próximos jogos  " + 
                    "\nObrigado por jogar"
                ).queue();
                gameStatus = false;
                quantidadedeperguntas = 0;
                espera = true;

            }
          	  else {
          		channel.sendMessage(
                        member.getEffectiveName() + " você não está logado!" +
                        "\ndigite !login para entrar no game"
                    ).queue();
          	  }
            }
        }
        
        //Caso o jogo ainda não tenha sido iniciado, envia a mensagem informando
        else if (msg.equals("!stop") && !gameStatus)
        {
            Member member = event.getMember();
            if (member != null)
            {
                channel.sendMessage(
                    member.getEffectiveName() + " o jogo ainda não foi iniciado."
                ).queue();
            }
        }

        //Comando para exibir as regras do jogo
        else if (msg.equals("!regras"))
        {
            Member member = event.getMember();
            if (member != null)
            {
                channel.sendMessage(
                    " Regras do Trivia Game:" +
                    "\n\nDigite !cadastrar para se cadastrar no jogo:" +
                    "\nDigite !login para fazer login no jogo" +
                    "\nDigite !start para iniciar o jogo" +
                    "\nDigite !repete para repetir a última pergunta feita no jogo" +
                    "\nDigite !stop para encerrar o jogo " +
                    "\nDigite !login-admin <login> <senha> para fazer login como admin " + 
                    "\nDigite !reset-ranking para zerar a pontuação de todos os jogadores (apenas para admins) " +
                    "\nDigite !reset-admins para remover todos os admins (apenas para admins) " + 
                    "\n\nO jogo consiste em um quiz de perguntas e respostas." +
                    " O bot irá fazer uma pergunta e o primeiro jogador do canal a dar a resosta certa ganha ponto."+
                    "\nA resposta deve corresponder a uma das opções apresentadas na questão." +
                    "\nOs jogadores vão somando pontos e ao final do game será exibido o rank."
                ).queue();
            }
        }

        //Lança a questão armezenada em perguntas
      if (gameStatus && quantidadedeperguntas < perguntas.size() && espera) {
            channel.sendMessage(
                "Por favor responda a seguinte questão:\n"+
                perguntas.get(quantidadedeperguntas).getPergunta() + "\n" +
                perguntas.get(quantidadedeperguntas).StringAlternativas() + "\n" 
            ).queue();

            espera = false;
            
      }
      
      //recebe a resposta dos jogadores e avaliar se é certa ou não, adicionando pontos
      if (gameStatus && quantidadedeperguntas < perguntas.size() && !espera) {
    	  
    	  Member member = event.getMember();
          if (member != null) 
          {
        	  Jogador jogadorSelecionado = (Jogador) Jogador.existe(member.getEffectiveName(), jogadores);

              boolean isLogged = jogadorSelecionado != null && jogadorSelecionado.getIsLogged();

        	  if (isLogged &&
    			  msg.equalsIgnoreCase(perguntas.get(quantidadedeperguntas).getResposta())) {
    		  jogadorSelecionado.setPontuacao(jogadorSelecionado.getPontuacao()+1);
          	channel.sendMessage(
          			"Certo! " + member.getEffectiveName()+ ", você tem "
	                            + jogadorSelecionado.getPontuacao() + " pontos.\n").queue();
          	espera = true;
          	quantidadedeperguntas++;
    	  }
    	  else if ((isLogged && (msg.equalsIgnoreCase("A") || msg.equalsIgnoreCase("B") || 
    			  msg.equalsIgnoreCase("C") || msg.equalsIgnoreCase("D"))) 
    			  ) {
    		  channel.sendMessage(
          			"Errooou! "+ member.getEffectiveName() + ", você tem "
	                            + jogadorSelecionado.getPontuacao() + " pontos.\n").queue();
          	espera = true;
          	quantidadedeperguntas++;
    	  }
      }
          }
 
      
      //Avalia o vencedor e envia mensagem informando o vencedor
      if (gameStatus && quantidadedeperguntas == perguntas.size() && espera) {
    	  
    	  Collections.sort(jogadores, comparator);

    		  channel.sendMessage(
          			"Acabaram as perguntas! Segue o ranking atual: \n").queue();
    		  Iterator<Jogador> iterator = jogadores.iterator();
        	  while (iterator.hasNext()) {
        	        Jogador jogador = iterator.next();
        	        channel.sendMessage(
                  			jogador.getNome() + " - Pontos:"+ jogador.getPontuacao()+"\n").queue();
        	    }
        	    channel.sendMessage(
              			"\nPara iniciar o jogo novamente e somar mais pontos, use !start").queue();
    	  gameStatus = false;
    	  quantidadedeperguntas = 0;

      }
      
      // Repete a última pergunta que ainda não foi respondida 
      if (msg.equals("!repete") && gameStatus){
          channel.sendMessage(
          "Por favor responda a seguinte questão:\n"+
          perguntas.get(quantidadedeperguntas).getPergunta() + "\n" +
          perguntas.get(quantidadedeperguntas).StringAlternativas() + "\n" 
      ).queue();
                } 
      
      // Comando para se cadastrar no jogo
      if (msg.equals("!cadastrar"))
      {
          Member member = event.getMember();
          if (member != null)
          {
        	  Jogador jogadorSelecionado = (Jogador) Jogador.existe(member.getEffectiveName(), jogadores);
        	  
              if(jogadorSelecionado != null) {
        	        	channel.sendMessage(                      
                                member.getEffectiveName() + 
                                " você já está cadastrado. Use o comando !login para entrar"                 
                            ).queue();
              } else {
        		  Jogador jogadorAtual = new Jogador(member.getEffectiveName());
        		  channel.sendMessage(                      
                          member.getEffectiveName() + 
                          " você foi cadastrado com sucesso."                 
                      ).queue();
        		  jogadores.add(jogadorAtual);
        		  jogadorId = jogadores.indexOf(jogadorAtual);
        		  jogadorAtual.setId(jogadorId);
        	  }
          }
      }
      
      
   // Comando para fazer login
      if (msg.equals("!login"))
      {
          Member member = event.getMember();
          if (member != null)
          {
        	  Jogador jogadorSelecionado = (Jogador) Jogador.existe(member.getEffectiveName(), jogadores);

        	  if(jogadorSelecionado != null && !jogadorSelecionado.getIsLogged()) {
        		  jogadorSelecionado.setIsLogged(true);
        		  channel.sendMessage(                      
                          member.getEffectiveName() + 
                          " você fez login com sucesso!"                 
                      ).queue();  
        	  }
        	  else if(jogadorSelecionado != null && jogadorSelecionado.getIsLogged()) {
        		  channel.sendMessage(                      
                          member.getEffectiveName() + 
                          " você já está logado"                 
                      ).queue();
        	  }
        	  else {
        		  channel.sendMessage(                      
                          member.getEffectiveName() + 
                          ", não conseguimos encontrar seu usuário. "+
                          "Use o comando !cadastrar para criar um usuário."                 
                      ).queue();
        		  
        	  }
          }
      }
      
      // Comando para fazer logout
      if (msg.equals("!logout"))
      {
    	  Member member = event.getMember();
          if (member != null)
          {
        	  Jogador jogadorSelecionado = (Jogador) Jogador.existe(member.getEffectiveName(), jogadores);

        	  if(jogadorSelecionado != null && jogadorSelecionado.getIsLogged()) {
        		  jogadorSelecionado.setIsLogged(false);
        		  channel.sendMessage(                      
                          member.getEffectiveName() + 
                          " você fez logout com sucesso!"                 
                      ).queue();  
        	  }
        	  else {
        		  channel.sendMessage(                      
                          member.getEffectiveName() + 
                          " você ainda não fez login."+
                          " use o comando !login para entrar."
                      ).queue();
        		  
        	  }
          }
      }
      
      if (msg.equals("!rank"))
      {
    	  Collections.sort(jogadores, comparator);

		  channel.sendMessage(
      			"Segue o ranking atual: \n").queue();
		  Iterator<Jogador> iterator = jogadores.iterator();
    	  while (iterator.hasNext()) {
    	        Jogador jogador = iterator.next();
    	        channel.sendMessage(
              			jogador.getNome() + " - Pontos:"+ jogador.getPontuacao()+"\n").queue();
    	    }
    
      }
      
      if (msg.startsWith("!login-admin")) {
          Member member = event.getMember();

          if (member != null) {
            String nome = member.getEffectiveName();
            String[] strings = msg.split(" ");
            if (Admin.existe(nome, admins) != null) {
              channel.sendMessage("Você já está logado como admin.\n").queue();
            }

            
            else if (strings.length == 3) {
              String login = strings[1];
              String senha = strings[2];

              if (Admin.adminValido(login, senha)) {
                admins.add(new Admin(nome));
                channel.sendMessage("Login de admin efetuado com sucesso!\n").queue();
              } else {
                channel.sendMessage("Login e/ou senha inválidos.\n").queue();
              }
            } else {
              channel.sendMessage("Parâmetros inválidos.\n").queue();
            }
          }
        }

      if (msg.equals("!reset-admins")) {
          Member member = event.getMember();

          if (member != null) {
            String nome = member.getEffectiveName();

            if (Admin.existe(nome, admins) != null) {
              admins.clear();
              channel.sendMessage("Todos os admins foram excluídos!\n").queue();

            } else {
              channel.sendMessage("Você não tem permissão para fazer isso.\n" +
                                  "Use !login-admin <login> <senha> e tente novamente.\n")
                                  .queue();
            }
          }
        }
      
      if (msg.equals("!reset-ranking")) {
          Member member = event.getMember();

          if (member != null) {
            String nome = member.getEffectiveName();

            if (Admin.existe(nome, admins) != null) {
              Jogador.resetRanking(jogadores);
              channel.sendMessage("O ranking foi resetado!\n").queue();

            } else {
              channel.sendMessage("Você não tem permissão para fazer isso.\n" +
                                  "Use !login-admin <login> <senha> e tente novamente.\n")
                                  .queue();
            }
          }
        }

    
      
    }
    

}
