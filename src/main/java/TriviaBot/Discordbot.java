package TriviaBot;


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
    public String s;
	
    /** Carregar as perguntas do arquivo TXT na lista perguntas */
    ListaPerguntas Perguntas = new ListaPerguntas();
    
    Comandos Comandos = new Comandos();
    
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
        User author = event.getAuthor();                //Usu�rio que mandou mensagem
        Message message = event.getMessage();           //A mensagem que foi recebida.
        MessageChannel channel = event.getChannel();    //O canal no qual a mensagem foi enviada

        String msg = message.getContentDisplay();       //Retorna a mensagem de uma forma que podemos ler.

        boolean bot = author.isBot();                    //determinar se quem enviou a mensagem � um bot ou n�o

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

        Member member = event.getMember();
        String[] comandoCompleto = msg.split(" ");
        String comandoExecutar; //tratamento da string de entada para o caso do comando !login-admin que recebe mais de uma palavra
        if(comandoCompleto[0].equalsIgnoreCase("!login-admin")) {
        	comandoExecutar = "!login-admin";
        }
        else {
        	comandoExecutar = msg;
        }
        switch (Comandos.codigoDoComando(comandoExecutar)) {
        
        case 0: //comando !ping
        	channel.sendMessage("pong!").queue(); //O queue() faz a gest�o do rate limit automaticamente
        	break;
        
        case 1:	//comando !roll
        	 Random rand = ThreadLocalRandom.current();
             int roll = rand.nextInt(6) + 1; 
             channel.sendMessage("Your roll: " + roll)
                    .flatMap(
                        (v) -> roll < 3,
                        sentMessage -> channel.sendMessage("The roll for messageId: " + sentMessage.getId() + " wasn't very good... Must be bad luck!\n")
                    )
                    .queue();
        	break;
        	
        case 2: //comando !whoami
        	// Mesagem para retornar os dados do usu�rio
            if (member != null)
            {
                channel.sendMessage(
                    "Your ID: " + member.getId() +                          
                    "\n Your EffectiveName: " + member.getEffectiveName() + 
                    "\n Your Nickname: " + member.getNickname() +           
                    "\n As Mention" + member.getAsMention()                 
                ).queue();
            }
        	break;
        	
        case 3: //comando !start
        	/** Inicio do jogo
             *Comando !Start inicia o jogo caso ainda nao tenha sido iniciado*/
        	if (!gameStatus)
            {
            	Perguntas = new ListaPerguntas();
            	perguntas = Perguntas.getPerguntas();
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
                                member.getEffectiveName() + " voc� n�o est� logado!" +
                                "\ndigite !login para entrar no game"
                            ).queue();
                	}	
                }
            }
            
            //Envia mensagem dizendo que o jogo j� foi iniciado
            else if (gameStatus)
            {
                if (member != null)
                {
                    channel.sendMessage(
                        member.getEffectiveName() + " O jogo j� foi iniciado."
                    ).queue();
                }
            }
        	break;
        	
        case 4: //comando !stop
        	//O Comando !Stop encerra o jogo e retorna as variaveis de controle aos valores iniciais
        	if (gameStatus)
            {
                if (member != null) 
                {
                	Jogador jogadorSelecionado = (Jogador) Jogador.existe(member.getEffectiveName(), jogadores);

                	  if(jogadorSelecionado != null && jogadorSelecionado.getIsLogged()) {
                	channel.sendMessage(
                        member.getEffectiveName() + " encerrou o jogo!" +
                        "\nCaso queira reiniciar digite !start" +
                        "\nEsperamos voc�s nos pr�ximos jogos  " + 
                        "\nObrigado por jogar"
                    ).queue();
                    gameStatus = false;
                    quantidadedeperguntas = 0;
                    espera = true;

                }
              	  else {
              		channel.sendMessage(
                            member.getEffectiveName() + " voc� n�o est� logado!" +
                            "\ndigite !login para entrar no game"
                        ).queue();
              	  }
                }
            }
            
            //Caso o jogo ainda n�o tenha sido iniciado, envia a mensagem informando
        	else if (!gameStatus)
            {
                if (member != null)
                {
                    channel.sendMessage(
                        member.getEffectiveName() + " o jogo ainda n�o foi iniciado."
                    ).queue();
                }
            }
        	
        	break;
        	
        case 5: //comando !regras
        	//Comando para exibir as regras do jogo
        	if (member != null)
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
                    " O bot ir� fazer uma pergunta e o primeiro jogador do canal a dar a resosta certa ganha ponto."+
                    "\nA resposta deve corresponder a uma das op��es apresentadas na quest�o." +
                    "\nOs jogadores v�o somando pontos e ao final do game ser� exibido o rank."
                ).queue();
            }
        	break;
        	
        case 6: //comando !repete
        	if (gameStatus){
                channel.sendMessage(
                "Por favor responda a seguinte quest�o:\n"+
                perguntas.get(quantidadedeperguntas).getPergunta() + "\n" +
                perguntas.get(quantidadedeperguntas).StringAlternativas() + "\n" 
            ).queue();
                      }
        	break;
        	
        case 7: //comando !cadastrar
        	// Comando para se cadastrar no jogo

                if (member != null)
                {
              	  Jogador jogadorSelecionado = (Jogador) Jogador.existe(member.getEffectiveName(), jogadores);
              	  
                    if(jogadorSelecionado != null) {
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
              		  jogadorId = jogadores.indexOf(jogadorAtual);
              		  jogadorAtual.setId(jogadorId);
              	  }
                }
        	break;
        	
        case 8: //comando !login
         // Comando para fazer login
        	if (member != null)
            {
          	  Jogador jogadorSelecionado = (Jogador) Jogador.existe(member.getEffectiveName(), jogadores);

          	  if(jogadorSelecionado != null && !jogadorSelecionado.getIsLogged()) {
          		  jogadorSelecionado.setIsLogged(true);
          		  channel.sendMessage(                      
                            member.getEffectiveName() + 
                            " voc� fez login com sucesso!"                 
                        ).queue();  
          	  }
          	  else if(jogadorSelecionado != null && jogadorSelecionado.getIsLogged()) {
          		  channel.sendMessage(                      
                            member.getEffectiveName() + 
                            " voc� j� est� logado"                 
                        ).queue();
          	  }
          	  else {
          		  channel.sendMessage(                      
                            member.getEffectiveName() + 
                            ", n�o conseguimos encontrar seu usu�rio. "+
                            "Use o comando !cadastrar para criar um usu�rio."                 
                        ).queue();
          		  
          	  }
            }
        	break;
        
        case 9: //comando !logout
        // Comando para fazer logout	 
        	if (member != null)
             {
           	  Jogador jogadorSelecionado = (Jogador) Jogador.existe(member.getEffectiveName(), jogadores);

           	  if(jogadorSelecionado != null && jogadorSelecionado.getIsLogged()) {
           		  jogadorSelecionado.setIsLogged(false);
           		  channel.sendMessage(                      
                             member.getEffectiveName() + 
                             " voc� fez logout com sucesso!"                 
                         ).queue();  
           	  }
           	  else {
           		  channel.sendMessage(                      
                             member.getEffectiveName() + 
                             " voc� ainda n�o fez login."+
                             " use o comando !login para entrar."
                         ).queue();
           		  
           	  }
             }
        	break;
        	
        case 10: //comando !rank
        	Collections.sort(jogadores, comparator);

  		  channel.sendMessage(
        			"Segue o ranking atual: \n").queue();
  		  Iterator<Jogador> iterator = jogadores.iterator();
      	  while (iterator.hasNext()) {
      	        Jogador jogador = iterator.next();
      	        channel.sendMessage(
                			jogador.getNome() + " - Pontos:"+ jogador.getPontuacao()+"\n").queue();
      	    }
        	break;
        
        case 11: //comando !login-admin
        	if (member != null) {
                String nome = member.getEffectiveName();
                String[] strings = msg.split(" ");
                if (Admin.existe(nome, admins) != null) {
                  channel.sendMessage("Voc� j� est� logado como admin.\n").queue();
                }

                
                else if (strings.length == 3) {
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
        	
        case 12: //comando !logout-admin
        	if (member != null) {
                String nome = member.getEffectiveName();

                Admin admin = (Admin) Admin.existe(nome, admins);

                if (admin != null) {
                  admins.remove(admin);
                  channel.sendMessage(nome + ", voc� fez logout como admin com sucesso!\n").queue();

                } else {
                  channel.sendMessage("Voc� n�o est� logado como admin.\n")
                                      .queue();
                }
              }
        	break;
        	
        case 13: //comando !reset-ranking
            if (member != null) {
                String nome = member.getEffectiveName();

                if (Admin.existe(nome, admins) != null) {
                  Jogador.resetRanking(jogadores);
                  channel.sendMessage("O ranking foi resetado!\n").queue();

                } else {
                  channel.sendMessage("Voc� n�o tem permiss�o para fazer isso.\n" +
                                      "Use !login-admin <login> <senha> e tente novamente.\n")
                                      .queue();
                }
              }
        	break;
        	
        }
        Trivia(msg, event);
    }

    
    
    
	private void Trivia(String msg, MessageReceivedEvent event) {
		MessageChannel channel = event.getChannel();
		//Lan�a a quest�o armezenada em perguntas
	      if (gameStatus && quantidadedeperguntas < perguntas.size() && espera) {
	           s =
	                "Por favor responda a seguinte quest�o:\n"+
	                perguntas.get(quantidadedeperguntas).getPergunta() + "\n" +
	                perguntas.get(quantidadedeperguntas).StringAlternativas() + "\n" 
	            ;

	            espera = false;
	            channel.sendMessage(s).queue();
	            }
	      
	      //recebe a resposta dos jogadores e avaliar se � certa ou n�o, adicionando pontos
	      if (gameStatus && quantidadedeperguntas < perguntas.size() && !espera) {
	    	  
	    	  Member member = event.getMember();
	          if (member != null) 
	          {
	        	  Jogador jogadorSelecionado = (Jogador) Jogador.existe(member.getEffectiveName(), jogadores);

	              boolean isLogged = jogadorSelecionado != null && jogadorSelecionado.getIsLogged();

	        	  if (isLogged &&
	    			  msg.equalsIgnoreCase(perguntas.get(quantidadedeperguntas).getResposta())) {
	    		  jogadorSelecionado.setPontuacao(jogadorSelecionado.getPontuacao()+1);
	          	s =
	          			"Certo! " + member.getEffectiveName()+ ", voc� tem "
		                            + jogadorSelecionado.getPontuacao() + " pontos.\n";
	          	espera = true;
	          	quantidadedeperguntas++;
	          	channel.sendMessage(s).queue();
	    	  }
	    	  else if ((isLogged && (msg.equalsIgnoreCase("A") || msg.equalsIgnoreCase("B") || 
	    			  msg.equalsIgnoreCase("C") || msg.equalsIgnoreCase("D"))) 
	    			  ) {
	    		  s = 
	          			"Errooou! "+ member.getEffectiveName() + ", voc� tem "
		                            + jogadorSelecionado.getPontuacao() + " pontos.\n";
	          	espera = true;
	          	quantidadedeperguntas++;
	          	channel.sendMessage(s).queue();
	    	  }
	      }
	          }
	 
	      
	      //Avalia o vencedor e envia mensagem informando o vencedor
	      if (gameStatus && quantidadedeperguntas == perguntas.size() && espera) {
	    	  
	    	  Collections.sort(jogadores, comparator);

	    		  s = 
	          			"Acabaram as perguntas! Segue o ranking atual: \n";
	    		  Iterator<Jogador> iterator = jogadores.iterator();
	        	  while (iterator.hasNext()) {
	        	        Jogador jogador = iterator.next();
	        	        s = s+
	                  			jogador.getNome() + " - Pontos:"+ jogador.getPontuacao()+"\n";
	        	    }
	        	    s = s +
	              			"\nPara iniciar o jogo novamente e somar mais pontos, use !start";
	    	  gameStatus = false;
	    	  quantidadedeperguntas = 0;
	    	  channel.sendMessage(s).queue();
	      }
	}    
}
