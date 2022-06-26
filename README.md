# Trivia Bot

Trabalho final de Programação Orientada a Objetos - UFBA.

## Descrição

Trivia Bot é um bot para Discord, que permite rodar um jogo de perguntas e respostas. O jogo, que foi desenvolvido em Java, recebe as respostas dos participantes e compara-as com a resposta correta, gerando a pontuação de cada jogador (e consequentemente o ranking).

O principal objetivo do Trivia Bot é despertar o interesse dos competidores nos diversos temas abordados pelo jogo, incentivando uma competição saudável entre os participantes, na tentativa de subir cada vez mais no ranking.

## Configuração e build

1) Criar um bot no Discord e adicioná-lo a um servidor, conforme as instruções disponíveis em: https://discordpy.readthedocs.io/en/stable/discord.html.

    > :warning: **Você deve salvar o token gerado. Ele será usado no próximo passo.**

2) Crie um arquivo chamado `token.txt` na raiz do projeto e cole nele o token gerado no item anterior.

3) Rode o build do Gradle.

    a) Se você usa um sistema operacional baseado em Unix (Linux ou MacOs), você pode rodar o comando `./gradlew build`

    b) Você também pode fazer isso com as ferramentas do Gradle da sua IDE preferida. Aqui estão links para as principais:

      * [Intellij](https://www.jetbrains.com/help/idea/work-with-gradle-tasks.html)

      * [Eclipse](https://www.vogella.com/tutorials/EclipseGradle/article.html#import-an-existing-gradle-project)

      * [Visual Studio Code](https://code.visualstudio.com/docs/java/java-build#_gradle)

    ** :information_source: Independentemente da IDE, deve-se rodar a task de `build` do Gradle.**

4) Rode o build do Gradle.

    a) Se você usa um sistema operacional baseado em Unix (Linux ou MacOs), você pode rodar o comando `./gradlew run --scan`

    b) Você também pode fazer isso com as ferramentas do Gradle da sua IDE preferida, conforme links do item anterior.

    ** :information_source: Independentemente da IDE, deve-se rodar a task de `run` do Gradle.**
