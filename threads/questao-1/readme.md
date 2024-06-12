
### Diagrama de Sequencia - Bar

```mermaid
sequenceDiagram
    participant User
    participant Bar
    participant WaiterThread
    participant ClientThread
    participant roundController
    participant BartenderThread

    User ->> Bar: rodar programa
    Bar ->> WaiterThread: cria WaiterThread()
    Bar ->> ClientThread: cria ClientThread()
    Bar ->> roundController: cria roundController()

    loop Cada round
        roundController ->> WaiterThread: iniciar round
        ClientThread ->> WaiterThread: fazer pedido
        WaiterThread ->> BartenderThread: entregar pedido
        BartenderThread ->> WaiterThread: preparar pedido
        WaiterThread ->> ClientThread: entregar pedido
    end


