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


[![](https://mermaid.ink/img/pako:eNqtlE1OwzAQha9ied1eIItKFS2wqChqyy6bwZ5Si8QTHKfiRz0NCw6AOEEvxjRuFCWkRUREWTjjec_z2RO_SUUaZSRzfCrQKpwYeHCQxlbwk4HzRpkMrBcXiUHrf8YvyfnC4i_TS3RbdKdcr8Hq5DAdEkJwOBo1vCMxtVsDQlHK6RRSGxm1IiwXiQuyqDwIIJFzyGhyDV3IY12jDtY5XsjSFoTfOAR9qBkECVWmYVVoQpSJeYYO9h_7L8xD9I8IXRitclYObJ4aj7Wy1kLixdV0NbycL1Z3N9N6oq7jaPTTeIkJKkMWxNpBjuyF4PefTH_epkW1QE-uMmkqu8l4I57NPbYFmHAN48mkF8tYm4BSnltHKb9AcK-sjUu5WbTh43ynv4Coo7glLIHubnsC-QIS81qdDT6b3Ifu60V1dPtXtNmyX9_NGIX_LdKQC35LwLxXyyWlk8ZOj_NgJ6VodfgoB3IgU2R-o_mafDtMxNJvMMVYRjzU4B5jGdsd50HhaflilYy8K3AgHRUPGxmtgfdqIItMg68u2GN09w3m09Uk?type=png)](https://mermaid.live/edit#pako:eNqtlE1OwzAQha9ied1eIItKFS2wqChqyy6bwZ5Si8QTHKfiRz0NCw6AOEEvxjRuFCWkRUREWTjjec_z2RO_SUUaZSRzfCrQKpwYeHCQxlbwk4HzRpkMrBcXiUHrf8YvyfnC4i_TS3RbdKdcr8Hq5DAdEkJwOBo1vCMxtVsDQlHK6RRSGxm1IiwXiQuyqDwIIJFzyGhyDV3IY12jDtY5XsjSFoTfOAR9qBkECVWmYVVoQpSJeYYO9h_7L8xD9I8IXRitclYObJ4aj7Wy1kLixdV0NbycL1Z3N9N6oq7jaPTTeIkJKkMWxNpBjuyF4PefTH_epkW1QE-uMmkqu8l4I57NPbYFmHAN48mkF8tYm4BSnltHKb9AcK-sjUu5WbTh43ynv4Coo7glLIHubnsC-QIS81qdDT6b3Ifu60V1dPtXtNmyX9_NGIX_LdKQC35LwLxXyyWlk8ZOj_NgJ6VodfgoB3IgU2R-o_mafDtMxNJvMMVYRjzU4B5jGdsd50HhaflilYy8K3AgHRUPGxmtgfdqIItMg68u2GN09w3m09Uk)
