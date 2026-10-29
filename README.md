# dge-api-paef



## Envio de email

Injetar `cv.gov.dge.paef.application.generic.service.EmailService` nos serviços que necessitam de enviar email:

```java
emailService.sendText(destinatario, assunto, mensagem);
emailService.sendHtml(destinatario, assunto, "<p>Mensagem</p>");
```

O envio usa uma fila assíncrona em memória. `sendText` deteta HTML automaticamente. Falhas de SMTP e rejeições da fila ficam registadas nos logs; o retorno do método não confirma a entrega e não existem tentativas automáticas de reenvio.

Configuração: `spring.mail.*` para SMTP, `application.mail.enabled` para ativar/desativar o envio e `MAIL_FROM` para substituir o remetente (por padrão, a conta SMTP). O ficheiro de exemplo usa `SMTP_PASSWORD` para a palavra-passe. As configurações SMTP foram copiadas do `zeemsv-api`.

Teste isolado, sem ligação ao SMTP nem à base de dados: `mvn -Dtest=EmailServiceTest test`.

## Criar notificação

`POST /api/notificacoes` (`/api` é o context-path configurado).

```json
{
  "nif": 123456789,
  "email": "autor@example.com",
  "assunto": "Nova notificação",
  "mensagem": "<p>Mensagem para os técnicos PAEF.</p>",
  "id_alvara": "id-opcional",
  "id_processo": "processo-opcional",
  "id_pagina_etapa": "etapa-opcional"
}
```

`nif`, `email`, `assunto` e `mensagem` são obrigatórios; os restantes campos podem ser omitidos. Assunto limitado a 255 caracteres e mensagem a 4000. O alvará, quando informado, deve pertencer à entidade do NIF.

Cria um único registo em `paef.t_notificacao`. O email recebido fica guardado nesse registo; os destinatários do envio são exclusivamente os endereços de `paef.paef_t_emails`, coluna `email`. Os endereços vazios são ignorados e os duplicados são eliminados sem distinguir maiúsculas de minúsculas. Configuração sem destinatários ou com endereço inválido impede a criação.

O retorno é HTTP 201 com `data.success`, `data.message` e `data.data` (ID, email, assunto, mensagem, data de registo, tipo e total de destinatários). A criação confirma o registo, não a entrega dos emails. O envio ocorre após o commit; `data_envio` só é preenchida depois de o SMTP aceitar todos os envios. Se um falhar, o registo permanece e a data continua vazia. A fila é em memória, sem reenvio automático.

Testes sem SMTP nem base de dados: `mvn -Dtest=EmailServiceTest,NotificacaoCreateServiceTest,NotificacaoEmailListenerTest,NotificacaoControllerTest test`.

## Responder a uma notificação

`POST /api/notificacoes/{id}/respostas`

```json
{
  "nif": 123456789,
  "email": "autor@example.com",
  "mensagem": "<p>Resposta à notificação.</p>"
}
```

Todos os campos são obrigatórios. A mensagem aceita texto ou HTML, até 4000 caracteres. A entidade do NIF deve corresponder à relação `ENTIDADE` da notificação. O assunto do email é `Re: ` seguido do assunto original.

Retorna HTTP 201 com a mensagem criada no envelope `data.data`. Cada chamada cria apenas uma linha em `paef.paef_t_notificacao_mensagem`, ligada por chave estrangeira a `paef.t_notificacao`. A notificação original mantém-se intacta. O email recebido identifica o autor no registo; o envio é exclusivamente para todos os emails configurados em `paef.paef_t_emails`, sem duplicados. O envio ocorre depois do commit e a data de envio da mensagem só é preenchida após sucesso de todos os envios SMTP. Mantém-se a fila em memória, sem reenvio automático.

`GET /api/notificacoes/{id}/mensagens?nif=123456789` devolve as respostas por ordem cronológica, com ID, ID da notificação, email, mensagem, data de registo e data de envio. O GET de detalhe da notificação (`GET /api/notificacoes/{id}?nif=...`) inclui também o campo `respostas`, por ordem cronológica, com os anexos de cada resposta. Sem respostas, devolve uma lista vazia.

A migração Flyway `V202610290001__create_notificacao_mensagem.sql` cria a tabela e o índice do histórico. Deve ser aplicada na base de dados antes de utilizar estes endpoints; não foi executada durante os testes locais.

Testes sem SMTP nem base de dados: `mvn -Dtest=EmailServiceTest,Notificacao*Test test`.

## Anexos de notificações e respostas

Os dois POST aceitam também `multipart/form-data`. Enviar uma parte JSON com `Content-Type: application/json`, chamada `notificacao` na criação ou `resposta` na resposta, e repetir a parte `anexos` para cada ficheiro:

```bash
curl -X POST http://localhost:8080/api/notificacoes \
  -F 'notificacao={"nif":123456789,"email":"autor@example.com","assunto":"Assunto","mensagem":"Mensagem"};type=application/json' \
  -F 'anexos=@prova.pdf' \
  -F 'anexos=@comprovativo.pdf'

curl -X POST http://localhost:8080/api/notificacoes/ID/respostas \
  -F 'resposta={"nif":123456789,"email":"autor@example.com","mensagem":"Resposta"};type=application/json' \
  -F 'anexos=@resposta.pdf'
```

As chamadas JSON sem anexos continuam disponíveis. Limites: 5 ficheiros, 5 MB por ficheiro e 10 MB no total. Ficheiros vazios são rejeitados.

Os ficheiros ficam na coluna binária `file` de `paef.paef_t_documento`, com nome em `descricao`, tipo MIME em `content_type` e ligação à notificação (`NOTIFICACAO`) ou à mensagem (`NOTIFICACAO_MENSAGEM`). O registo e os documentos são gravados na mesma transação. O envio assíncrono inclui os ficheiros originais como anexos MIME, para cada destinatário configurado.

Os retornos dos POST, o detalhe da notificação e o histórico de mensagens incluem `anexos`, com metadados e o link `documento`. O download usa `GET /api/notificacoes/{id}/anexos/{idDocumento}?nif=...`, validando a entidade e a ligação do documento à notificação ou a uma das suas respostas.

A migração `V202610290002__documento_content_type.sql` acrescenta a coluna `content_type`. Deve ser aplicada antes de usar esta versão; não foi executada na base de dados durante os testes.

## Getting started

To make it easy for you to get started with GitLab, here's a list of recommended next steps.

Already a pro? Just edit this README.md and make it your own. Want to make it easy? [Use the template at the bottom](#editing-this-readme)!

## Add your files

- [ ] [Create](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#create-a-file) or [upload](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#upload-a-file) files
- [ ] [Add files using the command line](https://docs.gitlab.com/ee/gitlab-basics/add-file.html#add-a-file-using-the-command-line) or push an existing Git repository with the following command:

```
cd existing_repo
git remote add origin http://git.nosi.cv/dge/dge-api-paef.git
git branch -M main
git push -uf origin main
```

## Integrate with your tools

- [ ] [Set up project integrations](http://git.nosi.cv/dge/dge-api-paef/-/settings/integrations)

## Collaborate with your team

- [ ] [Invite team members and collaborators](https://docs.gitlab.com/ee/user/project/members/)
- [ ] [Create a new merge request](https://docs.gitlab.com/ee/user/project/merge_requests/creating_merge_requests.html)
- [ ] [Automatically close issues from merge requests](https://docs.gitlab.com/ee/user/project/issues/managing_issues.html#closing-issues-automatically)
- [ ] [Enable merge request approvals](https://docs.gitlab.com/ee/user/project/merge_requests/approvals/)
- [ ] [Automatically merge when pipeline succeeds](https://docs.gitlab.com/ee/user/project/merge_requests/merge_when_pipeline_succeeds.html)

## Test and Deploy

Use the built-in continuous integration in GitLab.

- [ ] [Get started with GitLab CI/CD](https://docs.gitlab.com/ee/ci/quick_start/index.html)
- [ ] [Analyze your code for known vulnerabilities with Static Application Security Testing(SAST)](https://docs.gitlab.com/ee/user/application_security/sast/)
- [ ] [Deploy to Kubernetes, Amazon EC2, or Amazon ECS using Auto Deploy](https://docs.gitlab.com/ee/topics/autodevops/requirements.html)
- [ ] [Use pull-based deployments for improved Kubernetes management](https://docs.gitlab.com/ee/user/clusters/agent/)
- [ ] [Set up protected environments](https://docs.gitlab.com/ee/ci/environments/protected_environments.html)

***

# Editing this README

When you're ready to make this README your own, just edit this file and use the handy template below (or feel free to structure it however you want - this is just a starting point!).  Thank you to [makeareadme.com](https://www.makeareadme.com/) for this template.

## Suggestions for a good README
Every project is different, so consider which of these sections apply to yours. The sections used in the template are suggestions for most open source projects. Also keep in mind that while a README can be too long and detailed, too long is better than too short. If you think your README is too long, consider utilizing another form of documentation rather than cutting out information.

## Name
Choose a self-explaining name for your project.

## Description
Let people know what your project can do specifically. Provide context and add a link to any reference visitors might be unfamiliar with. A list of Features or a Background subsection can also be added here. If there are alternatives to your project, this is a good place to list differentiating factors.

## Badges
On some READMEs, you may see small images that convey metadata, such as whether or not all the tests are passing for the project. You can use Shields to add some to your README. Many services also have instructions for adding a badge.

## Visuals
Depending on what you are making, it can be a good idea to include screenshots or even a video (you'll frequently see GIFs rather than actual videos). Tools like ttygif can help, but check out Asciinema for a more sophisticated method.

## Installation
Within a particular ecosystem, there may be a common way of installing things, such as using Yarn, NuGet, or Homebrew. However, consider the possibility that whoever is reading your README is a novice and would like more guidance. Listing specific steps helps remove ambiguity and gets people to using your project as quickly as possible. If it only runs in a specific context like a particular programming language version or operating system or has dependencies that have to be installed manually, also add a Requirements subsection.

## Usage
Use examples liberally, and show the expected output if you can. It's helpful to have inline the smallest example of usage that you can demonstrate, while providing links to more sophisticated examples if they are too long to reasonably include in the README.

## Support
Tell people where they can go to for help. It can be any combination of an issue tracker, a chat room, an email address, etc.

## Roadmap
If you have ideas for releases in the future, it is a good idea to list them in the README.

## Contributing
State if you are open to contributions and what your requirements are for accepting them.

For people who want to make changes to your project, it's helpful to have some documentation on how to get started. Perhaps there is a script that they should run or some environment variables that they need to set. Make these steps explicit. These instructions could also be useful to your future self.

You can also document commands to lint the code or run tests. These steps help to ensure high code quality and reduce the likelihood that the changes inadvertently break something. Having instructions for running tests is especially helpful if it requires external setup, such as starting a Selenium server for testing in a browser.

## Authors and acknowledgment
Show your appreciation to those who have contributed to the project.

## License
For open source projects, say how it is licensed.

## Project status
If you have run out of energy or time for your project, put a note at the top of the README saying that development has slowed down or stopped completely. Someone may choose to fork your project or volunteer to step in as a maintainer or owner, allowing your project to keep going. You can also make an explicit request for maintainers.
