ant
sonar-scanner \
  -Dsonar.organization=ufba-poo-2022-1 \
  -Dsonar.projectKey=ufba-poo-2022-1_equipe9 \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.sources=TRIVIA_GAME/src/TRIVIA_GAME \
	-Dsonar.java.binaries=TRIVIA_GAME
