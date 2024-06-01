# Gerenciamento de Vendas de Patos
## API Rest Backend Java

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

Este projeto é uma API construída usando **Java, Java Spring, Postgres, IntelliJ IDEA, Postman e Docker.**

## Índice

- [Descrição](#Descrição)
- [Pré-requisitos](#Pré-requisitos)
- [Instalação](#instalação)
- [Configuração](#Configuração)
- [Uso](#uso)
- [API Endpoints](#api-endpoints)
- [Contribuindo](#contribuindo)

## Descrição:
API Rest usando Java/Spring Boot para uma granja de patos.
Para um melhor controle os patos serão cadastrados individualmente indicando
sempre a “mãe” de cada pato. Será possível também a venda (vários patos na
mesma venda) dos patos para um cliente cadastrado dentro do sistema e
dependendo do tipo do cliente (com/sem desconto) será aplicado um desconto de
20%. Também devem ser gerados relatórios de gerenciamento de patos excel e
PDF.

## Preços:
- Pato com filhos R$ 50,00;
- Pato sem filhos R$ 70,00;

## Pré-requisitos

- JDK 11 ou superior
- Maven 3 ou superior
- IDE java exemplo IntelliJ IDEA (caso deseje analisar o código)
- Postman ou Insomnia para as chamadas de Endpoints
- Docker (Caso deseje subir em container)

## Instalação

1. Clone o repositório:

```bash
git clone https://github.com/alefealves/api-granja-patos.git
```

2. Abra a pasta do projeto com uma IDE por exemplo Intellij IDEA

3. Verifique as configurações das variáveis de ambiente e das credenciais Postgres que são usadas em `application.properties` e altere para as suas credencias caso esteja usando banco postgres local ou deixe as configurações padrão. 

```yaml
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
spring.datasource.driver-class-name=
```

4. Para utiliza a API em container docker verifique as variáveis de ambiente baseado no .env.example, lembre-se que os valores das variáveis de conexão com o postgres tem ser as mesmas do arquivo .env

**Configuração**

```yaml
POSTGRESQL_USER=
POSTGRESQL_PASSWORD=
POSTGRESQL_DATABASE=
POSTGRESQL_LOCAL_PORT=
POSTGRESQL_DOCKER_PORT=
SPRING_HOST=
SPRING_LOCAL_PORT=
SPRING_DOCKER_PORT=
```

**Container Docker**

1. Para subir a API em um docker container, não é necessário as configurações acima, somente no caso de sua máquina já esteja rodando outras aplicações nas mesmas portas requeridas pela essa API: 

2. Execute no terminal dentro da pasta do projeto:
```bash
docker-compose up
```

## Uso

1. A API estará acessível em http://localhost:8080

## API Endpoints
A API fornece os seguintes endpoints:

**CLIENTE Endpoints**
```redução
POST /cliente - Crie um novo cliente
GET /cliente - Recupera todos os clientes
GET /cliente/{id} - Recupera um cliente
PUT /cliente/{id} - Atualiza um cliente
DELETE /cliente/{id} - Excluir um cliente
```

**Body POST**
```json
{
	"nome": "Jana",
	"tipo": "SEM_DESCONTO"
}
```
```json
{
	"nome": "Zé",
	"tipo": "COM_DESCONTO"
}
```

**PATO Endpoints**
```redução
POST /pato - Crie um novo pato
GET /pato - Recupera todos os patos
GET /pato/{id} - Recupera um pato
PUT /pato/{id} - Atualiza um pato
DELETE /pato/{id} - Excluir um pato
```

Para pato tipo PATO_FILHO é requerido maeId
**Body POST**
```json
{
	"tipo": "PATA_MAE",
	"valor": 50.00
}
```
```json
{
	"tipo": "PATO_FILHO",
	"valor": 70.00,
  	"maeId": 1
}
```

**VENDA Endpoints**
```redução
POST /venda - Crie uma nova venda
GET /venda - Recupera todas os vendas
GET /venda/{id} - Recupera uma venda
PUT /venda/{id} - Atualiza uma venda
DELETE /venda/{id} - Excluir uma venda
```

**Body POST**
```json
{
	"clienteId": 1,
	"patosIds": [
        {
            "id": 1
        },
        {
            "id": 3
        }
    ]
}
```

**RELATÓRIOS Endpoints**
```redução
GET /pato/relatoio/excel - Exporta relatório de patos em excel
GET /pato/relatoio/pdf - Exporta relatório de patos em pdf
```

## Contribuições

Contribuições são bem-vindas! Se você encontrar algum problema ou tiver sugestões de melhorias, abra um problema ou envie uma solicitação pull ao repositório.

Ao contribuir para este projeto, siga o estilo de código existente, [convenções de commit](https://www.conventionalcommits.org/en/v1.0.0/), e envie suas alterações em um branch separado.



