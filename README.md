# Sistema de Gestão de Obras Civis

Este projeto foi desenvolvido como um estudo prático para aplicar, de forma estruturada, os princípios da arquitetura MVC (Model-View-Controller) utilizando Java puro. Meu objetivo foi sair do modelo de codificação linear e explorar uma abordagem mais escalável, separando responsabilidades em camadas bem definidas.

A aplicação simula um sistema de gerenciamento de obras civis, permitindo cadastrar e manipular entidades como engenheiros, materiais e obras. Todo o controle de fluxo é feito via terminal, com persistência de dados em arquivos binários através de serialização (`ObjectOutputStream` / `ObjectInputStream`).

Na camada **Model**, implementei classes de domínio com encapsulamento, validações internas e uso de `enum` para representar status e unidades. Também utilizei expressões regulares para validação de CREA e métodos de controle sobre etapas e orçamentos.

A camada **Controller** centraliza a lógica de negócio, aplicando o padrão de orquestração e garantindo integridade entre as entidades. Trabalhei com estruturas como `Map`, `List` e `Optional`, além de empregar tratamento de exceções para controlar fluxos inválidos.

Na **camada de persistência**, optei por manter os dados em arquivos `.dat`, com suporte à serialização de objetos. Cada repositório implementa os métodos de CRUD de forma isolada, garantindo coesão e testabilidade. Cuidei também da consistência entre os dados em memória e os dados armazenados, implementando estratégias de sincronização ao iniciar os controladores.

A camada **View** (em desenvolvimento) está sendo projetada para funcionar como uma interface de linha de comando modular, com menus específicos para cada tipo de entidade. A ideia é evoluir futuramente para uma interface gráfica ou RESTful API, aplicando princípios de separação de interesses e injeção de dependência.

Além de reforçar conhecimentos básicos em Java, esse projeto ilustra conceitos como:
- Abstração e encapsulamento
- Serialização de dados
- Validações com regex
- Manipulação de coleções (`Map`, `List`)
- Modularização de código
- Tratamento de exceções
- Controle de fluxo e persistência em camadas

Esse projeto segue em evolução, e novas funcionalidades e melhorias estruturais serão adicionadas com o tempo.

## Estrutura do Projeto

Abaixo, a organização do projeto seguindo o padrão MVC (Model-View-Controller) com persistência em arquivos:
```
gestao-obras-civis-mvc/
├── src/
│ ├── model/
│ │ ├── Engenheiro.java
│ │ ├── Obra.java
│ │ ├── Material.java
│ │ ├── Status.java
│ │ ├── UnidadeMedida.java
│ │ └── Especialidade.java
│ │
│ ├── controller/
│ │ ├── EngenheiroController.java
│ │ ├── ObraController.java
│ │ └── MaterialController.java
│ │
│ ├── persistence/
│ │ ├── EngenheiroRepository.java
│ │ ├── ObraRepository.java
│ │ └── MaterialRepository.java
│ │
│ └── view/
│ ├── MainView.java
│ ├── EngenheiroView.java
│ ├── ObraView.java
│ └── MaterialView.java
│
├── data/
│ ├── engenheiros.dat
│ ├── obras.dat
│ └── materiais.dat
```

Cada camada tem responsabilidade única, garantindo manutenibilidade e separação de interesses. A comunicação entre camadas segue o padrão: `View → Controller → Model ↔ Repository`.

## Autor

Desenvolvido por **Josevan Tavares** como projeto pessoal com o objetivo de aprofundar a aplicação prática dos princípios da Orientação a Objetos em Java, explorando de forma estruturada a arquitetura MVC, persistência em arquivos, modularização e separação de responsabilidades.
