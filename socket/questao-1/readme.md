# Servidor de Fortunes

O objetivo deste exercício é imitar o funcionamento do conhecido biscoito da sorte chinês (fortune cookie) em um ambiente distribuído. O `fortune`, a cada invocação, imprime para o usuário uma frase escolhida aleatoriamente a partir de uma base de dados de frases.

## Funcionalidades

A ideia nesse exercício é construir um servidor de fortunes que suporte 4 operações:

1. **GET-FORTUNE**: retorna uma frase correntemente armazenada no servidor, escolhida aleatoriamente.
2. **ADD-FORTUNE**: adiciona uma nova frase ao banco de frases do servidor.
3. **UPD-FORTUNE**: modifica uma frase armazenada no servidor.
4. **LST-FORTUNE**: lista todas as frases armazenadas.

## Protocolo de Comunicação

O protocolo de comunicação entre clientes e servidor é baseado em comandos representados como texto. 

- Ao receber a string `GET-FORTUNE`, o servidor deve responder com uma frase.
- A operação `ADD-FORTUNE` assume a forma: `ADD-FORTUNE <frase>`.
- A operação `UPD-FORTUNE` altera a frase por posicionamento: `UPD-FORTUNE <índice> <nova frase>`.
- A operação `LST-FORTUNE` não requer parâmetros adicionais e retorna todas as frases armazenadas.

## Exemplos

- `GET-FORTUNE` 
  - Resposta: "A vida é como andar de bicicleta. Para manter o equilíbrio, você deve continuar se movendo."
- `ADD-FORTUNE A persistência é o caminho do êxito.`
  - Resposta: "Frase adicionada com sucesso."
- `UPD-FORTUNE 2 A maior glória não é ficar de pé, mas levantar-se cada vez que se cai.`
  - Resposta: "Frase atualizada com sucesso."
- `LST-FORTUNE`
  - Resposta: 
    ```
    1. A vida é como andar de bicicleta. Para manter o equilíbrio, você deve continuar se movendo.
    2. A persistência é o caminho do êxito.
    3. A maior glória não é ficar de pé, mas levantar-se cada vez que se cai.
    ```


Fluxo:
sequenceDiagram
    participant Client
    participant FortuneClient
    participant FortuneServer
    participant ClientHandler

    Client->>FortuneClient: Envia comando
    FortuneClient->>FortuneServer: Conecta ao servidor
    FortuneServer->>ClientHandler: Cria nova thread para o cliente

    loop Operações
        Client->>FortuneClient: Envia comando
        FortuneClient->>ClientHandler: Transmite comando

        alt GET-FORTUNE
            ClientHandler->>ClientHandler: Seleciona frase aleatória
            ClientHandler->>FortuneClient: Retorna frase
            FortuneClient->>Client: Exibe frase
        else ADD-FORTUNE
            ClientHandler->>ClientHandler: Adiciona nova frase
            ClientHandler->>FortuneClient: Confirma adição
            FortuneClient->>Client: Exibe confirmação
        else UPD-FORTUNE
            ClientHandler->>ClientHandler: Atualiza frase existente
            ClientHandler->>FortuneClient: Confirma atualização
            FortuneClient->>Client: Exibe confirmação
        else LST-FORTUNE
            ClientHandler->>ClientHandler: Lista todas as frases
            ClientHandler->>FortuneClient: Retorna lista de frases
            FortuneClient->>Client: Exibe lista de frases
        end
    end
