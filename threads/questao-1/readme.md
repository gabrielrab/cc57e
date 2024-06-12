# Simulação de Bar com Threads

Este projeto simula o funcionamento de um bar utilizando threads para representar clientes, garçons e o bartender. O objetivo é gerenciar pedidos e rodadas de serviço de maneira concorrente.

## Estrutura do Projeto

- **Bar.java**: Classe principal que inicializa o bar e os threads.
- **BartenderThread.java**: Representa o bartender que prepara os pedidos.
- **ClienteThread.java**: Representa os clientes que fazem pedidos.
- **GarcomThread.java**: Representa os garçons que atendem os clientes.
- **RodadaController.java**: Controla as rodadas de serviço.
- **model/GarsomClass.java**: Classe que modela as características dos garçons.

## Parâmetros

- `NUM_CLIENTES`: Número de clientes presentes no bar.
- `NUM_GARCONS`: Número de garçons trabalhando.
- `CAPACIDADE_GARCONS`: Capacidade de atendimento dos garçons.
- `NUM_RODADAS`: Número de rodadas de serviço no bar.

### Diagrama de Sequência Atualizado

Para criar um diagrama de sequência mais completo, agora temos as seguintes interações detalhadas:

1. **Inicialização do Bar**:
    - Iniciar threads para garçons (`GarcomThread`).
    - Iniciar o controlador de rodadas (`RodadaController`).
    - Iniciar threads para clientes (`ClienteThread`).

2. **Execução de Rodadas**:
    - `RodadaController` inicia cada rodada.
    - `ClienteThread` faz pedidos.
    - `GarcomThread` coleta pedidos e entrega para o bartender (`BartenderThread`).
    - `BartenderThread` prepara pedidos.
    - `GarcomThread` entrega pedidos aos clientes.

Ainda precisamos do código da classe `GarsomClass.java` para garantir que todos os detalhes estão cobertos no diagrama de execução. Por favor, forneça o conteúdo dessa classe.

## Execução

Para executar a simulação, compile e rode a classe `Bar.java`. Ela irá inicializar os threads necessários e começar a simulação.

```java
public static void main(final String[] args) {
    for (int i = 0; i < NUM_GARCONS; i++) {
        garcoms.add(new GarsomClass(i, CAPACIDADE_GARCONS)); 
        System.err.println("\n  Garçom " + (i + 1) + " está aguardando no salão.");
        new Thread(new GarcomThread(i, CAPACIDADE_GARCONS)).start();
        garcomsCriados += 1;
    }

    new Thread(new RodadaController()).start();

    for (int i = 0; i < NUM_CLIENTES; i++) {
        new Thread(new ClienteThread(i)).start();
        clientesCriados += 1;
    }
}


