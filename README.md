<h1 align="center"> 
	🛠️ Chamados - Microserviço de chamados técnicos 🛠️
</h1>


---

## 🛠️ Sobre o projeto

Chamados é um microserviço feito com **Java + Spring Boot**, **API REST** e **MongoDB Atlas**, responsável pela abertura, acompanhamento e resolução de chamados técnicos.

Esse projeto é a **Atividade Prática M1 - Parte 2 de Engenharia de Software**. A ideia é que ele seja acoplado num sistema que já tem login. Por isso ele não tem tela, cadastro de usuário nem login próprios: quem autentica é o sistema de fora, que entrega um **JWT** com o id, o nome e as permissões do usuário. O microserviço só confere a assinatura do token e checa as permissões de cada rota.

---

## 🔧 Funcionalidades

#### Abertura de chamado
- [x] Usuário autenticado abre um chamado informando título e descrição
- [x] Solicitante, data de abertura e status `ABERTO` são preenchidos pelo servidor, a requisição não consegue definir
- [x] Título e descrição obrigatórios

#### Consulta e acompanhamento
- [x] Listar os chamados de um usuário
- [x] Consultar um chamado com todos os detalhes e o status
- [x] Cada usuário só vê os próprios chamados, o técnico vê de todos

#### Resolução do chamado
- [x] Técnico registra a solução aplicada, o status vira `RESOLVIDO` e grava a data de resolução e quem resolveu
- [x] Solução obrigatória
- [x] Chamado resolvido não pode ser alterado de novo

---

## 🔌 Endpoints

| Método | Rota | O que faz |
|---|---|---|
| `POST` | `/api/chamados` | abre um chamado |
| `GET` | `/api/chamados/usuario/{id}` | lista os chamados de um usuário |
| `GET` | `/api/chamados/{id}` | consulta um chamado |
| `PUT` | `/api/chamados/{id}/resolver` | resolve um chamado |

Todas as rotas precisam do header `Authorization: Bearer <token>`.

Abrir chamado:
```json
{ "titulo": "Impressora", "descricao": "nao imprime" }
```

Resolver chamado:
```json
{ "solucao": "reiniciei o spooler de impressao" }
```

---

## 🛡️ Permissões

Aqui não existe perfil nem role, só permissões. Quem decide qual perfil tem qual permissão é o sistema de login, então o microserviço não precisa saber quantos perfis existem.

| Ação | Precisa de |
|---|---|
| Abrir chamado | `chamados:abrir` |
| Listar chamados de um usuário | ser o próprio usuário ou ter `chamados:resolver` |
| Consultar chamado | ser quem abriu ou ter `chamados:resolver` |
| Resolver chamado | `chamados:resolver` |

Formato do token esperado (HS256, assinado com o mesmo `JWT_SECRET` do sistema de login):

```json
{
  "sub": "id-do-usuario",
  "nome": "Maria",
  "permissoes": ["chamados:abrir"]
}
```

Sem token ou com token inválido a API responde **401**, e sem a permissão responde **403**.

---

## 🖼️ Layout


---

## 🔩 Como executar o projeto

### Pré-requisitos

- [Git](https://git-scm.com)
- [Nix + devenv](https://devenv.sh/getting-started/) (gerencia o JDK 21 e o Maven automaticamente)
- Uma conta no [MongoDB Atlas](https://www.mongodb.com/cloud/atlas) (gratuita) com um cluster criado
- [Postman](https://www.postman.com/), [Insomnia](https://insomnia.rest/) ou equivalente pra chamar a API

---

**1 - Instalar o Nix e o devenv**, seguindo o guia oficial: [devenv.sh/getting-started](https://devenv.sh/getting-started/)

**2 - Clonar o projeto e entrar na pasta**
```bash
git clone https://github.com/0LucasMatheus/chamados-ms.git
cd chamados-ms
```

**3 - Entrar no ambiente**
```bash
devenv shell
# ou, se você usa direnv:
direnv allow
```

**4 - Configurar as variáveis de ambiente**
```bash
cp .env.example .env
# preencha com os valores reais
```

| Variável | Pra que serve |
|---|---|
| `MONGODB_URI` | string de conexão do MongoDB Atlas |
| `MONGODB_DATABASE` | nome do banco (padrão `chamados`) |
| `JWT_SECRET` | segredo compartilhado com o sistema de login pra validar o token, mínimo 32 caracteres |

**5 - Subir a aplicação**
```bash
mvn spring-boot:run
```

**6 - Gerar um token pra testar**

Como o sistema de login fica fora desse projeto, dá pra gerar o token no [jwt.io](https://jwt.io):
- algoritmo `HS256`
- no campo do segredo, o mesmo valor do `JWT_SECRET` do `.env`
- no payload, um usuário comum e um técnico, por exemplo:

```json
{ "sub": "u1", "nome": "Maria", "permissoes": ["chamados:abrir"] }
```
```json
{ "sub": "t1", "nome": "Carlos", "permissoes": ["chamados:abrir", "chamados:resolver"] }
```

**7 - Chamar a API** em http://localhost:8080/api/chamados com o header `Authorization: Bearer <token>`
