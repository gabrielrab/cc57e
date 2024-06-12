
### Diagrama de Sequência em Mermaid

```mermaid
sequenceDiagram
    participant User
    participant Bar
    participant GarcomThread
    participant ClienteThread
    participant RodadaController
    participant BartenderThread
    participant GarsomClass

    User ->> Bar: start()
    Bar ->> GarcomThread: new GarcomThread()
    Bar ->> ClienteThread: new ClienteThread()
    Bar ->> RodadaController: new RodadaController()

    loop Cada rodada
        RodadaController ->> GarcomThread: iniciar rodada
        ClienteThread ->> GarcomThread: fazer pedido
        GarcomThread ->> BartenderThread: entregar pedido
        BartenderThread ->> GarcomThread: preparar pedido
        GarcomThread ->> ClienteThread: entregar pedido
    end


