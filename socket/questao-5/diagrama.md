[![](https://mermaid.ink/img/pako:eNqllEtOwzAQhq9izQqkUOLETkMW3RR2sKrEAmUzJAMENXZwnKoP9TTsuEYvhtvSoqoRaqhXo5nv__3QeBaQ6ZwggZo-GlIZ3Rb4arBMFXOrQmOLrKhQWTYqakslDklZg2OG9S7DflLHknv9jnxNrgPG24FgDwTtQLgHwnZA7AHRDsg9IFO1RTZHuxoMDm-VsKFWNF19aka1xWcaU1bk-CsJukvC7hLRXSJPl2xFY60rxqXvswnNqd4m_3qZOzUpkOlMG7P6UpmLL4a6rAxeP5LK8fLQITjbITzbQZztIP_t4MLTW819O2Pw9D475sOOvOjIy5N48KAkU2KRu4GyWKtTsG9UUgqJC1-0cZ2YQqqWjsTG6tFMZZBY05AHTZWj3Y2fXZLywmrzsB1Rm0nlgfvWkCxgCongUS_kcSTiUIRcyhsPZpD0ZY_7fSGi2NWk4MHSg7nWztPvOZbHfhBFsexz0Q82bk-b4nrD5TfKQLcy?type=png)](https://mermaid.live/edit#pako:eNqllEtOwzAQhq9izQqkUOLETkMW3RR2sKrEAmUzJAMENXZwnKoP9TTsuEYvhtvSoqoRaqhXo5nv__3QeBaQ6ZwggZo-GlIZ3Rb4arBMFXOrQmOLrKhQWTYqakslDklZg2OG9S7DflLHknv9jnxNrgPG24FgDwTtQLgHwnZA7AHRDsg9IFO1RTZHuxoMDm-VsKFWNF19aka1xWcaU1bk-CsJukvC7hLRXSJPl2xFY60rxqXvswnNqd4m_3qZOzUpkOlMG7P6UpmLL4a6rAxeP5LK8fLQITjbITzbQZztIP_t4MLTW819O2Pw9D475sOOvOjIy5N48KAkU2KRu4GyWKtTsG9UUgqJC1-0cZ2YQqqWjsTG6tFMZZBY05AHTZWj3Y2fXZLywmrzsB1Rm0nlgfvWkCxgCongUS_kcSTiUIRcyhsPZpD0ZY_7fSGi2NWk4MHSg7nWztPvOZbHfhBFsexz0Q82bk-b4nrD5TfKQLcy)

```mermaid
sequenceDiagram
    participant SistemaCentral as Sistema Central
    participant Loja1 as Loja 1
    participant Loja2 as Loja 2
    participant Loja3 as Loja 3
    participant Loja4 as Loja 4
    participant Loja5 as Loja 5

    Loja1->>SistemaCentral: Conexão estabelecida
    Loja2->>SistemaCentral: Conexão estabelecida
    Loja3->>SistemaCentral: Conexão estabelecida
    Loja4->>SistemaCentral: Conexão estabelecida
    Loja5->>SistemaCentral: Conexão estabelecida

    loop 1500 vezes
        Loja1->>SistemaCentral: Envia ocorrência (Compra/Venda)
        Loja2->>SistemaCentral: Envia ocorrência (Compra/Venda)
        Loja3->>SistemaCentral: Envia ocorrência (Compra/Venda)
        Loja4->>SistemaCentral: Envia ocorrência (Compra/Venda)
        Loja5->>SistemaCentral: Envia ocorrência (Compra/Venda)
    end

    Loja1->>SistemaCentral: Conexão encerrada
    Loja2->>SistemaCentral: Conexão encerrada
    Loja3->>SistemaCentral: Conexão encerrada
    Loja4->>SistemaCentral: Conexão encerrada
    Loja5->>SistemaCentral: Conexão encerrada
```
