package trivia_bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import javax.security.auth.login.LoginException;

public class Discordbot extends ListenerAdapter{
 
	
    public static void main(String[] args)
    {
  
    	//Construtor para o BOT
        try
        {
            JDA jda = JDABuilder.createDefault("OTc1MDc3ODAwODk1NzI1NTk5.GXAWOy.aXmcYBWjTJgsbB04gKyy6dtRv-3I4UVk1lBxMk") // O token da conta para login. Esse token é criado em https://discord.com/developers/applications
                    .addEventListeners(new Discordbot())   // A instancia da classe que vai cuidar dos eventos
                    .build();
            jda.awaitReady(); // Garante que o JDA tenha carregado completamente
            System.out.println("Finished Building JDA!");
        }
        catch (LoginException e)
        {
            //Exceção em que algo da errado com o login
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            //como o método awaitRedy é um metodo que faz o bloqueio, a espera pode ser interrompida.
            //Essa exceção ocorre nessa situação.
            e.printStackTrace();
        }
        perguntas = new ArrayList<Pergunta>(LeitorDePerguntas()); //Carregar as perguntas do arquivo TXT na lista perguntas
    }

    public static ArrayList<Pergunta> perguntas = new ArrayList<>();
    
	//Variáveis usadas para conversão das linhas UTF-8 do TXT e resolver problemas com acentuação
	public static String linha0;
	public static String linha1;
	public static String linha2;
	public static String linha3;
	public static String linha4;
	public static String linha5;
	
	//Método para leitura das perguntas do arquivo.TXT
	public static ArrayList<Pergunta> LeitorDePerguntas() {
		Scanner ler = null;
		try {
			ler = new Scanner(new File("perguntas.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        

        int numeroPerguntas = Integer.parseInt(ler.nextLine());

        for(int i = 0; i < numeroPerguntas; i++) {try {
			linha0 = new String(ler.nextLine().getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			linha1 = new String(ler.nextLine().getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			linha2 = new String(ler.nextLine().getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			linha3 = new String(ler.nextLine().getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			linha4 = new String(ler.nextLine().getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			linha5 = new String(ler.nextLine().getBytes(), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       perguntas.add(new Pergunta(linha0, linha1,
               linha2, linha3, linha4,
               linha5));
	        }
		return perguntas;
	}

	//Variáveis para controle do jogo
	boolean gameStatus = false;
	boolean espera = true;
	int quantidadedeperguntas = 0;
    int pontosPlayerUm = 0;
    int pontosPlayerDois = 0;

    	
	//O parametro event é usado para captar todas as mensagens no chat.
	//O método abaixo é atualizado constantemente monitorando as mensagens e tomando as ações
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {

        JDA jda = event.getJDA();                       //JDA, o núcleo do API.
        long responseNumber = event.getResponseNumber();//A quantidade de eventos Discord que o JDA Recebeu desde a última conexão.

        //informações de eventos específicos
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
            
            //Cria a mensagem no terminal através dos dados coletados do discord. Vai exibir o nome do server
            //o nome do usuário (efetivo ou exibido, no caso de o servidor permitir trocar o nick internamente)
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

        //Início do jogo
        //Comando !Start inicia o jogo caso ainda não tenha sido iniciado
        else if (msg.equals("!start") && !gameStatus)
        {
            Member member = event.getMember();
            if (member != null)
            {
                channel.sendMessage(
                    member.getEffectiveName() + " iniciou o jogo!" +
                    "\nPara saber as regras digite !regras" +
                    "\nPara ver o ranking digite !rank " + 
                    "\nBoa sorte!"
                ).queue();
                gameStatus = true;
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
                channel.sendMessage(
                    member.getEffectiveName() + " encerrou o jogo!" +
                    "\nCaso queira reiniciar digite !start" +
                    "\nEsperamos vocês nos próximos jogos  " + 
                    "\nObrigado por jogar"
                ).queue();
                gameStatus = false;
                quantidadedeperguntas = 0;
                espera = true;
                pontosPlayerUm = 0;
                pontosPlayerDois = 0;
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
                    "\n\nDigite !start para iniciar o jogo" +
                    "\nDigite !stop para encerrar o jogo " + 
                    "\n\nO jogo consiste em um quiz de perguntas e respostas." +
                    " O bot irá fazer uma pergunta e o primeiro jogador do canal a dar a resosta certa ganha ponto."+
                    "\nOs jogadores vão somando pontos e ao final do game será exibido o rank."
                ).queue();
            }
        }

        //Lança a questão armezenada em perguntas alternando os jogadores
      if (gameStatus && quantidadedeperguntas < perguntas.size() && espera) {
    	  if(quantidadedeperguntas % 2 == 0) {
          	channel.sendMessage(
          			"Player 1, por favor responda a seguinte"
  	                        + " questão:\n"+
  	                    perguntas.get(quantidadedeperguntas).pergunta+ "\n"+
  	                    perguntas.get(quantidadedeperguntas).respostaUm+ "\n"+
  	                    perguntas.get(quantidadedeperguntas).respostaDois+ "\n"+
  	                    perguntas.get(quantidadedeperguntas).respostaTres+ "\n"+
  	                    perguntas.get(quantidadedeperguntas).respostaQuatro
  	              ).queue();
             	espera = false;
    	  }
    	  if(quantidadedeperguntas % 2 != 0) {
            	channel.sendMessage(
              			"Player 2, por favor responda a seguinte"
      	                        + " questão:\n"+
      	                    perguntas.get(quantidadedeperguntas).pergunta+ "\n"+
      	                    perguntas.get(quantidadedeperguntas).respostaUm+ "\n"+
      	                    perguntas.get(quantidadedeperguntas).respostaDois+ "\n"+
      	                    perguntas.get(quantidadedeperguntas).respostaTres+ "\n"+
      	                    perguntas.get(quantidadedeperguntas).respostaQuatro
      	              ).queue();
               	espera = false;
      	  }
      }
      
      //recebe a resposta dos jogadores e avaliar se é certa ou não, adicionando pontos
      if (gameStatus && quantidadedeperguntas < perguntas.size() && !espera) {
    	  if (msg.equalsIgnoreCase(perguntas.get(quantidadedeperguntas).respostaCerta) && quantidadedeperguntas % 2 == 0) {
    		  pontosPlayerUm++;
          	channel.sendMessage(
          			"Certo! Você tem "
	                            + pontosPlayerUm + " pontos.\n").queue();
          	espera = true;
          	quantidadedeperguntas++;
    	  }
    	  else if ((msg.equalsIgnoreCase("A") || msg.equalsIgnoreCase("B") || msg.equalsIgnoreCase("C") || msg.equalsIgnoreCase("D")) && quantidadedeperguntas % 2 == 0) {
    		  channel.sendMessage(
          			"Errooou! Você tem "
	                            + pontosPlayerUm + " pontos.\n").queue();
          	espera = true;
          	quantidadedeperguntas++;
    	  }
      }
      if (gameStatus && quantidadedeperguntas < perguntas.size() && !espera) {
    	  if (msg.equalsIgnoreCase(perguntas.get(quantidadedeperguntas).respostaCerta) && quantidadedeperguntas % 2 != 0) {
    		  pontosPlayerDois++;
          	channel.sendMessage(
          			"Certo! Você tem "
	                            + pontosPlayerDois + " pontos.\n").queue();
          	espera = true;
          	quantidadedeperguntas++;
    	  }
    	  else if ((msg.equalsIgnoreCase("A") || msg.equalsIgnoreCase("B") || msg.equalsIgnoreCase("C") || msg.equalsIgnoreCase("D")) && quantidadedeperguntas % 2 != 0) {
    		  channel.sendMessage(
          			"Errooou! Você tem "
                            + pontosPlayerDois + " pontos.\n").queue();
          	espera = true;
          	quantidadedeperguntas++;
    	  }
      }
      
      //Avalia o vencedor e envia mensagem informando o vencedor
      if (gameStatus && quantidadedeperguntas == perguntas.size() && espera) {
    	  if(pontosPlayerUm > pontosPlayerDois) {
    		  channel.sendMessage(
          			"Player 1 wins!").queue();
    	  }
    	  else {
    		  channel.sendMessage(
          			"Player 2 wins!").queue();
      	  }
    	  gameStatus = false;
    	  quantidadedeperguntas = 0;
    	    pontosPlayerUm = 0;
    	    pontosPlayerDois = 0;
      }
    }
}
