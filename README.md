# The Wise Old Man - Backend

**"The Wise Old Man"** é um jogo interativo de perguntas e respostas projetado para testar e expandir o conhecimento dos usuários de uma forma envolvente e desafiadora. O objetivo do jogo é responder corretamente às perguntas para acumular experiência (XP), subir de nível e conquistar patentes à medida que se avança. Este projeto combina uma robusta arquitetura backend com **Spring Boot** e uma interface de usuário moderna desenvolvida com **Ionic Angular** para criar uma experiência de usuário intuitiva e interativa.

## Características Principais

- **Sistema de Perguntas e Respostas**: Desafios diversos em múltiplas categorias e níveis de dificuldade.
- **Progressão e XP**: Acúmulo de experiência e avanço por níveis com patentes distintas.
- **Interface Amigável**: Design responsivo e intuitivo para uma experiência de usuário otimizada.
- **Escalabilidade**: Arquitetura projetada para crescer com a adição de novas funcionalidades e categorias de perguntas.

## Arquitetura e Tecnologias

### Backend

- **Spring Boot**: Framework principal para o desenvolvimento do backend, fornecendo uma estrutura robusta e escalável.
  - **Spring Boot Starter Web**: Para criar APIs RESTful e gerenciar a lógica de negócios.
  - **Spring Boot Starter Data JPA**: Para simplificar a interação com o banco de dados usando JPA/Hibernate.
  - **PostgreSQL**: Sistema de gerenciamento de banco de dados relacional utilizado para armazenar dados persistentes.
  - **Spring Boot DevTools**: Facilita o desenvolvimento com reinicialização automática e outras ferramentas úteis.
  - **Lombok**: Reduz o código boilerplate com anotações para gerar getters, setters, e outros métodos automaticamente.

### Segurança e Configuração

- **Spring Security** (futuro): Para autenticação e autorização, se necessário.

## Configuração

### 1. Clonar o Repositório

```bash
git clone https://github.com/usuario/the-wise-old-man-backend.git
cd the-wise-old-man-backend
