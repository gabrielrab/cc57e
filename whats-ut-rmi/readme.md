<h1 align="center">
    <img src="./src/main/resources/logo.png">
    WhatsUT 
</h1>
<h3 align="center">
💬  Aplicativo de sistema para comunicação interpessoal real-time utilizando Java RMI 
</h3>
<hr>

### Comandos

Lista de comandos utilizados no aplicativo
| Comando | Descrição |
| ----------- | ----------- |
| `/help` | Ajuda com os recursos |
| `/members` | Visualiza a listagem de membros |
| `/invites` | Visualiza a listagem de pedidos de entrar em um chat |
| `/accept {user_name}` | Aceitar um usuário específico no chat |
| `/ban {user_name}` | Banir um usuário específico no chat |
| `/exit` | Sair do chat |

> Caso o administrador do grupo saia, o aplicativo deve decidir quem será o novo administrador, ou se o grupo seja eliminado. Tal opção pode ser ajustada no momento da criação do chat em grupo.

### Diagrama de sequência

```mermaid
---
title: Diagrama de Atividades do Sistema WhatsUT
---
flowchart TD
    A[Usuário abre o aplicativo] --> B{Usuário possui conta?}
    B -->|Sim| C[Autenticação criptografada]
    C --> D{Login bem-sucedido?}
    D -->|Sim| E[Exibir lista de usuários]
    D -->|Não| F[Fim]

    B -->|Não| G[Realizar cadastro]
    G --> C

    E --> H[Exibir lista de grupos]
    H --> I{Usuário seleciona grupo?}
    I -->|Sim| J[Solicitar entrada no grupo]
    J --> K{Aprovado pelo criador do grupo?}
    K -->|Sim| L[Entrar no grupo]
    K -->|Não| H

    I -->|Não| M{Usuário seleciona chat privado?}
    M -->|Sim| N[Iniciar chat privado]
    N --> O[Enviar/receber mensagens e arquivos]

    M -->|Não| H

    L --> P[Iniciar chat em grupo]
    P --> Q[Enviar/receber mensagens]

    Q --> R{Usuário administrador do grupo?}
    R -->|Sim| S[Adicionar/remover usuários do grupo]
    R -->|Não| T[Participar do chat]

    S --> U{Administrador do grupo sai?}
    U -->|Sim| V[Designar novo administrador ou eliminar grupo]
    U -->|Não| H
```

### Tecnologias utilizadas

- Java
- Java RMI
- Swing
