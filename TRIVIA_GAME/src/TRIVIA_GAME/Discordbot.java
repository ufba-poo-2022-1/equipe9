package TRIVIA_GAME;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import javax.security.auth.login.LoginException;

public static class Discordbot extends ListenerAdapter{

    private static String nomeAdmin = "admin";
    private static int idAdmin = 1000;
    public static Admin admin = new Admin(nomeAdmin);
    public int jogadorId = 0;
    public boolean statuslog = false;

    public static ArrayList<Pergunta> perguntas = new ArrayList<>();
    public ArrayList<Jogador> jogadores = new ArrayList<>();

    //Vari�veis usadas para convers�o das linhas UTF-8 do TXT e resolver problemas com acentua��o
    public static String linha0;
    public static String linha1;
    public static String linha2;
    public static String linha3;
    public static String linha4;
    public static String linha5;

    //Vari�veis para controle do jogo
    boolean gameStatus = false;
    boolean espera = true;
    int quantidadedeperguntas = 0;
    int pontosPlayerUm = 0;
    int pontosPlayerDois = 0;

    private Comparator<Jogador> comparator = new Rank();

    public static void  main(final String[] args)
    {
  
    	//Construtor para o BOT
        try
        {
            JDA jda = JDABuilder.createDefault("BOT-TOKEN-AQUI") // O token da conta para login. Esse token � criado em https://discord.com/developers/applications
                    .addEventListeners(new Discordbot())   // A instancia da classe que vai cuidar dos eventos
                    .build();
            jda.awaitReady(); // Garante que o JDA tenha carregado completamente
            System.out.println("Finished Building JDA!");
        }
        catch (LoginException e)
        {
            //Exce��o em que algo da errado com o login
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            //como o m�todo awaitRedy � um metodo que faz o bloqueio, a espera pode ser interrompida.
            //Essa exce��o ocorre nessa situa��o.
            e.printStackTrace();
        }
        perguntas = new ArrayList<>(leitorDePerguntas()); //Carregar as perguntas do arquivo TXT na lista perguntas
        admin.dadosAdmin("admin", "1234");
        admin.setId(idAdmin);
    
    }


	
	//M�todo para leitura das perguntas do arquivo.TXT
	public static ArrayList<Pergunta> leitorDePerguntas() {
        //Optamos por usar ArrayList ao invés de interface aqui por ser, no escopo do programa, mais fácil de manipular e de utilizar no momento;
		Scanner ler = null;
		try {
			ler = new Scanner(new File("perguntas.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
    
        int numeroPerguntas = Integer.parseInt(ler.nextLine());

        for(int i = 0; i < numeroPerguntas; i++) {
            try {
                linha0 = new String(ler.nextLine().getBytes(), "UTF-8");
                linha1 = new String(ler.nextLine().getBytes(), "UTF-8");
                linha2 = new String(ler.nextLine().getBytes(), "UTF-8");
                linha3 = new String(ler.nextLine().getBytes(), "UTF-8");
                linha4 = new String(ler.nextLine().getBytes(), "UTF-8");
                linha5 = new String(ler.nextLine().getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        
            ArrayList<String> alternativas = new ArrayList<>();
            alternativas.add(linha1);
            alternativas.add(linha2);
            alternativas.add(linha3);
            alternativas.add(linha4);
        
            perguntas.add(new Pergunta(linha0, alternativas, linha5));
	    }
        
		return perguntas;
		
	}


    
    
    public Collection<Jogador> getRanking() {
        Collections.sort(jogadores, comparator);
        return Collections.unmodifiableCollection(jogadores);
    }

    	
	//O parametro event � usado para captar todas as mensagens no chat.
	//O m�todo abaixo � atualizado constantemente monitorando as mensagens e tomando as a��es
    @Override
    public void  onMessageReceived(final MessageReceivedEvent event)
    {

        JDA jda = event.getJDA();                       //JDA, o n�cleo do API.
        long responseNumber = event.getResponseNumber();//A quantidade de eventos Discord que o JDA Recebeu desde a �ltima conex�o.

        //informa��es de eventos espec�ficos
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
            
            //Cria a mensagem no terminal atrav�s dos dados coletados do discord. Vai exibir o nome do server
            //o nome do usu�rio (efetivo ou exibido, no caso de o servidor permitir trocar o nick internamente)
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
            channel.sendMessage("pong!").queue(); //O queue() faz a gest�o do rate limit automaticamente
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
            // Mesagem para retornar os dados do usu�rio

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

        //In�cio do jogo
        //Comando !Start inicia o jogo caso ainda n�o tenha sido iniciado
        else if (msg.equals("!start") && !gameStatus)
        {
        	Jogador jogadorSelecionado = null;
            Member member = event.getMember();
            Iterator<Jogador> iterator = jogadores.iterator();
      	  boolean existe = false;
      	    while (iterator.hasNext()) {
      	        Jogador jogador = iterator.next();
      	        if (jogador.getNome().equals(member.getEffectiveName())) {
      	        	existe = true;
      	        	jogadorSelecionado = jogador;
      	        }}
            if (member != null)
            {
            	if(existe && jogadorSelecionado.getIsLogged()) {
            		
            	
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
        else if (msg.equals("!start") && gameStatus)
        {
            Member member = event.getMember();
            if (member != null)
            {
                channel.sendMessage(
                    member.getEffectiveName() + " O jogo j� foi iniciado."
                ).queue();
            }
        }
        
        //O Comando !Stop encerra o jogo e retorna as variaveis de controle aos valores iniciais
        else if (msg.equals("!stop") && gameStatus)
        {
            Member member = event.getMember();
            Jogador jogadorSelecionado = null;
            if (member != null) 
            {
            	Iterator<Jogador> iterator = jogadores.iterator();
          	  boolean existe = false;
          	    while (iterator.hasNext()) {
          	        Jogador jogador = iterator.next();
          	        if (jogador.getNome().equals(member.getEffectiveName())) {
          	        	existe = true;
          	        	jogadorSelecionado = jogador;
          	        }}
          	  if(existe && jogadorSelecionado.getIsLogged()) {
            	channel.sendMessage(
                    member.getEffectiveName() + " encerrou o jogo!" +
                    "\nCaso queira reiniciar digite !start" +
                    "\nEsperamos voc�s nos pr�ximos jogos  " + 
                    "\nObrigado por jogar"
                ).queue();
                gameStatus = false;
                quantidadedeperguntas = 0;
                espera = true;
                pontosPlayerUm = 0;
                pontosPlayerDois = 0;
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
        else if (msg.equals("!stop") && !gameStatus)
        {
            Member member = event.getMember();
            if (member != null)
            {
                channel.sendMessage(
                    member.getEffectiveName() + " o jogo ainda n�o foi iniciado."
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
                    "\nDigite !stop para encerrar o jogo " + 
                    "\n\nO jogo consiste em um quiz de perguntas e respostas." +
                    " O bot ir� fazer uma pergunta e o primeiro jogador do canal a dar a resosta certa ganha ponto."+
                    "\nA resposta deve corresponder a uma das op��es apresentadas na quest�o." +
                    "\nOs jogadores v�o somando pontos e ao final do game ser� exibido o rank."
                ).queue();
            }
        }

        //Lan�a a quest�o armezenada em perguntas
      if (gameStatus && quantidadedeperguntas < perguntas.size() && espera) {
            channel.sendMessage(
                "Por favor responda a seguinte quest�o:\n"+
                perguntas.get(quantidadedeperguntas).getPergunta() + "\n" +
                perguntas.get(quantidadedeperguntas).StringAlternativas() + "\n" 
            ).queue();

            espera = false;
            
      }
      
      //recebe a resposta dos jogadores e avaliar se � certa ou n�o, adicionando pontos
      if (gameStatus && quantidadedeperguntas < perguntas.size() && !espera) {
    	  
    	  Member member = event.getMember();
          Jogador jogadorSelecionado = null;
          if (member != null) 
          {
          	Iterator<Jogador> iterator = jogadores.iterator();
        	  boolean existe = false;
        	    while (iterator.hasNext()) {
        	        Jogador jogador = iterator.next();
        	        if (jogador.getNome().equals(member.getEffectiveName())) {
        	        	existe = true;
        	        	jogadorSelecionado = jogador;
        	        }}
    	  
    	  if (existe && jogadorSelecionado.getIsLogged() &&
    			  msg.equalsIgnoreCase(perguntas.get(quantidadedeperguntas).getResposta())) {
    		  jogadorSelecionado.setPontuacao(jogadorSelecionado.getPontuacao()+1);
          	channel.sendMessage(
          			"Certo! " + member.getEffectiveName()+ ", voc� tem "
	                            + jogadorSelecionado.getPontuacao() + " pontos.\n").queue();
          	espera = true;
          	quantidadedeperguntas++;
    	  }
    	  else if (existe && jogadorSelecionado.getIsLogged() && (msg.equalsIgnoreCase("A") || msg.equalsIgnoreCase("B") ||
    			  msg.equalsIgnoreCase("C") || msg.equalsIgnoreCase("D"))) 
    			   {
    		  channel.sendMessage(
          			"Errooou! "+ member.getEffectiveName() + ", voc� tem "
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
        	  boolean existe = false;
        	    while (iterator.hasNext()) {
        	        Jogador jogador = iterator.next();
        	        channel.sendMessage(
                  			jogador.getNome() + " - Pontos:"+ jogador.getPontuacao()+"\n").queue();
        	    }
        	    channel.sendMessage(
              			"\nPara iniciar o jogo novamente e somar mais pontos, use !start").queue();
    	  gameStatus = false;
    	  quantidadedeperguntas = 0;
    	    pontosPlayerUm = 0;
    	    pontosPlayerDois = 0;
      }
      
   // Comando para se cadastrar no jogo
      if (msg.equals("!cadastrar"))
      {
          Member member = event.getMember();
          if (member != null)
          {
        	  Iterator<Jogador> iterator = jogadores.iterator();
        	  boolean existe = false;
        	    while (iterator.hasNext()) {
        	        Jogador jogador = iterator.next();
        	        if (jogador.getNome().equals(member.getEffectiveName())) {
        	        	channel.sendMessage(                      
                                member.getEffectiveName() + 
                                " voc� j� est� cadastrado. Use o comando !login para entrar"                 
                            ).queue();
        	        	existe = true;
        	        }}
        	  if(!existe) {
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
      }
      
      
   // Comando para fazer login
      if (msg.equals("!login"))
      {
    	  Jogador jogadorSelecionado = null;
          Member member = event.getMember();
          if (member != null)
          {
        	  Iterator<Jogador> iterator = jogadores.iterator();
        	  boolean existe = false;
        	    while (iterator.hasNext()) {
        	        Jogador jogador = iterator.next();
        	        if (jogador.getNome().equals(member.getEffectiveName())) {
        	        	existe = true;
        	        	jogadorSelecionado = jogador;
        	        }}
        	  
        	  
        	  if(existe && !jogadorSelecionado.getIsLogged()) {
        		  jogadorSelecionado.setIsLogged(true);
        		  channel.sendMessage(                      
                          member.getEffectiveName() + 
                          " voc� fez login com sucesso!"                 
                      ).queue();  
        	  }
        	  else if(existe && jogadorSelecionado.getIsLogged()) {
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
      }
      
      // Comando para fazer logout
      if (msg.equals("!logout"))
      {
    	  Jogador jogadorSelecionado = null;
    	  Member member = event.getMember();
          if (member != null)
          {
        	  Iterator<Jogador> iterator = jogadores.iterator();
        	  boolean existe = false;
        	    while (iterator.hasNext()) {
        	        Jogador jogador = iterator.next();
        	        if (jogador.getNome().equals(member.getEffectiveName())) {
        	        	existe = true;
        	        	jogadorSelecionado = jogador;
        	        }}
        	  
        	  if(existe && jogadorSelecionado.getIsLogged()) {
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
      }
      
      if (msg.equals("!rank"))
      {
    	  Collections.sort(jogadores, comparator);

		  channel.sendMessage(
      			"Segue o ranking atual: \n").queue();
		  Iterator<Jogador> iterator = jogadores.iterator();
    	  boolean existe = false;
    	    while (iterator.hasNext()) {
    	        Jogador jogador = iterator.next();
    	        channel.sendMessage(
              			jogador.getNome() + " - Pontos:"+ jogador.getPontuacao()+"\n").queue();
    	    }
    
      }
      
    }
    

}
