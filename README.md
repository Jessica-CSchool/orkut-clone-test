# Orkut Clone - Automação de Testes com Self-Healing (Healenium)

Este projeto contém a esteira de testes automatizados de Interface (UI) para a aplicação **Orkut Clone**, desenvolvida utilizando **Java** e **Selenium WebDriver**. O grande diferencial desta arquitetura é a integração com o **Healenium**, um mecanismo de Inteligência Artificial que intercepta falhas de seletores (*NoSuchElementException*) em tempo real e realiza a autocura (*Self-Healing*) dos elementos para evitar a quebra dos testes.

---

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java 20
* **Framework de Testes:** JUnit 5 (Jupiter)
* **Automação de Web:** Selenium WebDriver (v4.14.0)
* **Inteligência Artificial (Self-Healing):** Healenium Web (v3.5.0)
* **Orquestração de Infraestrutura:** Docker & Docker Compose
* **Banco de Dados:** PostgreSQL 15 (Para persistência histórica dos seletores)

---

## 🚀 Pré Requisitos
* Estar rodando o projeto orkut-clone no localhost:3000
* Estar com o docker UP, conforme passo-a-passo abaixo:

---

## 🛠️ Arquitetura do Ambiente (Docker)

O ecossistema do Healenium roda de forma isolada em contêineres Docker para garantir a estabilidade do banco de dados e do servidor de IA. A estrutura é composta por:

1.  **`postgres-db`:** Banco de dados responsável por armazenar a árvore de elementos (DOM) e os históricos de sucesso.
2.  **`healenium`:** O backend do framework (Spring Boot) que analisa os seletores e calcula a probabilidade de cura.
3.  **`selector-imitator`:** Componente auxiliar de inteligência para mapeamento de similaridade de nós do HTML.

---

## 🏁 Como Rodar o Projeto do Zero

### 1. Pré-requisitos
Certifique-se de ter instalado em sua máquina local:
* [Docker Desktop](https://www.docker.com/products/docker-desktop/)
* [Java JDK 20](https://openjdk.org/)
* [IntelliJ IDEA](https://www.jetbrains.com/idea/) (ou IDE de sua preferência)

### 2. Inicializar a Infraestrutura (Healenium)
Abra o terminal do seu sistema operacional (PowerShell ou terminal da IDE) na raiz do projeto onde está localizado o arquivo `docker-compose.yaml` e execute:

```bash
# Derruba resquícios antigos limpando volumes e inicializa os containers em background
docker compose down -v; docker compose up -d
```

💡 Nota de Estabilidade: Aguarde cerca de 15 a 20 segundos antes de rodar os testes. Esse tempo é necessário para que o banco de dados processe o script de inicialização (init.sql) e o servidor do Healenium complete o deploy interno do Spring Boot.
Você pode checar se tudo subiu com sucesso rodando:
```bash
docker ps
```

📊 Acompanhando as Curas em Tempo Real
http://localhost:7878/healenium/report
