# Jogo da Forca
Implemente um jogo da forca remoto com 1 jogador. A definição da palavra a ser adivinhada ficará no
servidor, sendo que ao início o servidor dará o tamanho da palavra (em caracteres). Faça a simulação
em texto do jogo, retornando cada parte do boneco ou o caractere da palavra do servidor para o
cliente. 

```mermaid

sequenceDiagram
    participant Client
    participant ForcaClient
    participant ForcaServer
    participant ClientHandler

    Client->>ForcaClient: Inicia o cliente
    ForcaClient->>ForcaServer: Conecta ao servidor
    ForcaServer->>ClientHandler: Cria nova thread para o cliente

    ClientHandler->>ForcaClient: "Bem-vindo ao Jogo da Forca! A palavra tem X letras."

    loop Jogadas
        ForcaClient->>Client: Exibe estado atual do jogo
        Client->>ForcaClient: Digita uma letra
        ForcaClient->>ClientHandler: Transmite letra

        alt Letra correta
            ClientHandler->>ClientHandler: Atualiza estado do jogo
            ClientHandler->>ForcaClient: "Letra correta! Estado atual do jogo"
        else Letra incorreta
            ClientHandler->>ClientHandler: Atualiza número de tentativas
            ClientHandler->>ForcaClient: "Letra incorreta. N tentativas restantes. Estado atual do jogo"
        end

        ForcaClient->>Client: Exibe resposta do servidor
    end

    alt Venceu
        ClientHandler->>ForcaClient: "Você venceu! A palavra era: X"
        ForcaClient->>Client: Exibe mensagem de vitória
    else Perdeu
        ClientHandler->>ForcaClient: "Você perdeu! A palavra era: X"
        ForcaClient->>Client: Exibe mensagem de derrota
    end


```
