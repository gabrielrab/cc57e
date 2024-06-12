
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
    loop Cada NUM_Waiter
        Bar ->> WaiterThread: criar WaiterThread()
    end
    loop Cada NUM_Client
        Bar ->> ClientThread: criar ClientThread()
    end
    Bar ->> roundController: criar roundController()

    loop Cada round
        roundController ->> WaiterThread: iniciar round
        ClientThread ->> WaiterThread: fazer pedido
        WaiterThread ->> BartenderThread: entregar pedido
        BartenderThread ->> WaiterThread: preparar pedido
        WaiterThread ->> ClientThread: entregar pedido
    end


