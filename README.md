# Java Event Planner

Java Event Planner e uma aplicacao desktop feita em Java com Swing para gerenciar eventos em um calendario.

O usuario pode criar, editar, excluir, visualizar e salvar eventos. Os dados ficam armazenados localmente no arquivo `data/events.txt`.

## Funcionalidades

- Calendario mensal interativo.
- Cadastro de eventos com titulo, data, hora, local e descricao.
- Edicao e exclusao de eventos.
- Lista de eventos do dia selecionado.
- Detalhes do evento selecionado.
- Categorias de eventos:
  - Meeting
  - Birthday
  - Appointment
- Cores diferentes no calendario para cada categoria.
- Participantes com nome e e-mail.
- Lembretes pre-definidos.
- Recorrencia:
  - None
  - Daily
  - Weekly
  - Monthly
- Persistencia em arquivo texto.

## Estrutura do Projeto

```text
src/eventplanner/
+-- controller/
|   +-- EventManager.java
+-- model/
|   +-- Event.java
|   +-- MeetingEvent.java
|   +-- BirthdayEvent.java
|   +-- AppointmentEvent.java
|   +-- Attendee.java
|   +-- Category.java
|   +-- Recurrence.java
+-- service/
|   +-- EventService.java
+-- util/
|   +-- EventStorage.java
+-- view/
    +-- EventPlannerApp.java
    +-- CalendarPanel.java
    +-- EventDialog.java
    +-- EventListPanel.java
```

## Organizacao

O projeto segue uma organizacao inspirada em MVC:

- `model`: classes que representam os dados do sistema.
- `view`: telas e componentes Swing.
- `controller`: comunicacao entre interface e servico.
- `service`: regras de negocio dos eventos.
- `util`: leitura e gravacao do arquivo de dados.

## Conceitos de POO

O projeto usa:

- Encapsulamento, com atributos privados e metodos getters/setters.
- Heranca, pois `MeetingEvent`, `BirthdayEvent` e `AppointmentEvent` herdam de `Event`.
- Polimorfismo, pois cada tipo de evento implementa sua propria categoria.
- Abstracao, pois `Event` representa um evento generico.
- Enums, com `Category` e `Recurrence`.

## Requisitos

- Java JDK 17 ou superior.
- PowerShell, CMD ou terminal equivalente.

Para conferir a instalacao:

```powershell
java -version
javac -version
```

## Como Compilar

Na pasta raiz do projeto, execute:

```powershell
javac -d out '@sources.txt'
```

Alternativa:

```powershell
javac -d out src/eventplanner/model/*.java src/eventplanner/util/*.java src/eventplanner/service/*.java src/eventplanner/controller/*.java src/eventplanner/view/*.java
```

## Como Executar

Depois de compilar, execute:

```powershell
java -cp out eventplanner.view.EventPlannerApp
```

## Como Usar

1. Selecione uma data no calendario.
2. Clique em `Add` para criar um evento.
3. Preencha os dados do evento.
4. Clique em `Save` no formulario.
5. Selecione um evento na lista lateral para ver os detalhes.
6. Use `Edit` para alterar um evento.
7. Use `Delete` para excluir um evento.
8. Use `Save` na tela principal para gravar os dados em `data/events.txt`.

## Persistencia

Os eventos sao salvos em:

```text
data/events.txt
```

O projeto usa Base64 em alguns campos para evitar problemas com caracteres especiais no arquivo de texto.


## Autor

Pedro Santos Souza

Bacharelado em Sistemas de Informacao

Universidade de Sao Paulo (USP)
