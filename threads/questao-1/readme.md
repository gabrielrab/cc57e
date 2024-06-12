
### Diagrama de Atividades - Bar

```mermaid
sequenceDiagram
    participant User
    participant Bar
    participant WaiterThread
    participant ClientThread
    participant roundController
    participant BartenderThread

    User ->> Bar: start()
    Bar ->> WaiterThread: new WaiterThread()
    Bar ->> ClientThread: new ClientThread()
    Bar ->> roundController: new roundController()

    loop Cada round
        roundController ->> WaiterThread: iniciar round
        ClientThread ->> WaiterThread: fazer pedido
        WaiterThread ->> BartenderThread: entregar pedido
        BartenderThread ->> WaiterThread: preparar pedido
        WaiterThread ->> ClientThread: entregar pedido
    end


