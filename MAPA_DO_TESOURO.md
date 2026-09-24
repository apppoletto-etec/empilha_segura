# 🗺️ Mapa do Tesouro — Ecossistema SST Digital

> Documento oficial de continuidade técnica da **SST Digital: Tecnologia a Serviço da Segurança**.

**Última atualização:** 24 de setembro de 2026  
**Responsável pelo ecossistema:** SST Digital: Tecnologia a Serviço da Segurança  
**Organização no GitHub:** [apppoletto-etec](https://github.com/apppoletto-etec)

---

## 1. Finalidade deste documento

Este guia é a fonte central de orientação para qualquer pessoa ou agente que precise manter, corrigir, evoluir, compilar ou publicar os aplicativos.

Antes de alterar qualquer projeto:

1. Leia este documento por completo.
2. Confirme qual aplicativo será alterado.
3. Preserve o funcionamento das integrações Firebase.
4. Nunca publique credenciais, senhas ou tokens.
5. Teste o aplicativo antes de gerar uma nova APK.
6. Atualize este documento quando o fluxo, os endereços ou a infraestrutura mudarem.

## 2. Visão geral do ecossistema

O ecossistema é formado por quatro aplicativos Android e uma landing page central de distribuição:

A **SST Digital: Tecnologia a Serviço da Segurança** é uma iniciativa acadêmica voltada à modernização da gestão de Saúde e Segurança do Trabalho. Os projetos demonstram como aplicativos e plataformas integradas podem substituir pilhas de papel e planilhas manuais por dados organizados, mobilidade e uma cultura preventiva mais ativa.

Os aplicativos possuem finalidade educacional e demonstrativa. Eles não substituem plataformas corporativas homologadas, consultoria técnica, obrigações legais ou a atuação de profissionais habilitados em SST.

| Aplicativo | Finalidade | Pacote Android | Repositório |
|---|---|---|---|
| **Empilha Segura** | Checklists diários e mensais para inspeção de empilhadeiras | `com.lima.checklist` | [empilha_segura](https://github.com/apppoletto-etec/empilha_segura) |
| **Dicionário de Segurança** | Consulta e cadastro de termos de segurança do trabalho | `com.lima.dicionario` | [dicionario](https://github.com/apppoletto-etec/dicionario) |
| **EPI Express** | Controle de EPIs, entregas e devoluções | `com.lima.epimanager` | [epi_express](https://github.com/apppoletto-etec/epi_express) |
| **Check Saúde** | Gestão de funcionários e exames ocupacionais | `com.lima.medicinatrabalho` | [check_saude](https://github.com/apppoletto-etec/check_saude) |

### Arquitetura em uma frase

Cada aplicativo é um projeto Android independente, todos usam serviços Firebase e as APKs oficiais são distribuídas pela landing page armazenada no repositório `empilha_segura`.

### Pilares conceituais

- **Gestão de riscos e documentos:** digitalização de PGR, LTCAT, APR e registros preventivos;
- **Controle de EPIs:** entregas, devoluções, evidências e rastreabilidade por colaborador;
- **Inspeções e checklists:** formulários eletrônicos para máquinas, veículos e atividades críticas;
- **Saúde ocupacional:** acompanhamento de ASOs, exames e eventos relacionados ao eSocial;
- **Treinamentos e DDS:** cronogramas, presença, certificados e reciclagens;
- **Indicadores e alertas:** transformação dos dados em informações para decisões preventivas.

### Benefícios estudados

- Redução de controles manuais e uso de papel;
- Maior rastreabilidade dos registros;
- Alertas proativos sobre prazos e pendências;
- Mobilidade para uso no campo;
- Indicadores que apoiam CIPA, SIPAT e gestão preventiva.

## 3. Localização dos projetos

Diretório-base atual no computador de desenvolvimento:

```text
C:\Users\saulo.lima\AndroidStudioProjects\Projeto_Polleto-20260806T163933Z-1-001\
└── Projeto_Polleto\projeto epi\
    ├── CheckList\           # Empilha Segura
    ├── Dicionario\          # Dicionário de Segurança
    ├── EpiManager\          # EPI Express
    ├── MedicinaTrabalho\    # Check Saúde
    ├── APKs\                # APKs oficiais geradas
    └── acesso_git.txt        # Credencial local; nunca versionar ou compartilhar
```

> O caminho local pode mudar em outra máquina. Os nomes das quatro pastas e a correspondência acima são a referência importante.

## 4. Tecnologias utilizadas

- Android SDK;
- Java e Kotlin;
- Gradle Kotlin DSL;
- Material Design;
- View Binding nos projetos que utilizam Kotlin;
- Firebase Authentication;
- Cloud Firestore;
- Firebase Realtime Database;
- Firebase Storage;
- Firebase Analytics;
- Picasso;
- WorkManager no Dicionário e no EPI Express.

### Requisitos atuais

| Configuração | Valor |
|---|---|
| `minSdk` | 24 — Android 7.0 |
| `targetSdk` | 33 |
| `compileSdk` | 34 |
| Java/JVM | 1.8 |
| Versão inicial | 1.0 (`versionCode 1`) |

## 5. Organização interna dos aplicativos

Os projetos seguem aproximadamente esta divisão:

```text
app/src/main/
├── java/com/lima/<projeto>/
│   ├── adapter/   # Adaptadores de listas e componentes visuais
│   ├── bd/        # Configuração e acesso ao Firebase
│   ├── helper/    # Rotinas auxiliares, quando existentes
│   ├── model/     # Entidades e modelos de dados
│   └── view/      # Activities e fluxos de interface
├── res/
│   ├── drawable/  # Imagens e fundos
│   ├── layout/    # Telas XML
│   ├── menu/      # Menus
│   └── values/    # Cores, textos e temas
└── AndroidManifest.xml
```

## 6. Firebase

### Projeto central de armazenamento

As quatro APKs são armazenadas no mesmo bucket:

```text
Projeto: Dicionario
Project ID: dicionario-e6982
Bucket: gs://dicionario-e6982.firebasestorage.app
```

Console:

[Abrir Firebase Storage](https://console.firebase.google.com/project/dicionario-e6982/storage/dicionario-e6982.firebasestorage.app/files?hl=pt-br)

### Projetos Firebase vinculados aos aplicativos

| Aplicativo | Project ID configurado no app |
|---|---|
| Empilha Segura | `checklist-26486` |
| Dicionário | `dicionario-e6982` |
| EPI Express | `epi-manager-e29c3` |
| Check Saúde | `medicinatrabalho-10b32` |

Cada aplicativo contém seu próprio arquivo:

```text
app/google-services.json
```

Esse arquivo deve corresponder ao pacote Android correto. Não copie o arquivo de um aplicativo para outro.

## 7. APKs oficiais

Diretório local:

```text
Projeto_Polleto\projeto epi\APKs\
```

| Aplicativo | Arquivo oficial | Tamanho aproximado |
|---|---|---:|
| Check Saúde | `check_saude_1.0.apk` | 11,3 MB |
| Dicionário | `dicionario_v1.apk` | 11,5 MB |
| Empilha Segura | `empilha_segura_1.0.apk` | 11,3 MB |
| EPI Express | `epi_express_v1.apk` | 11,6 MB |

### Links públicos atuais

- [Baixar Check Saúde](https://firebasestorage.googleapis.com/v0/b/dicionario-e6982.firebasestorage.app/o/check_saude_1.0.apk?alt=media&token=07428b1a-00b0-4f26-800e-6bc168327090)
- [Baixar Dicionário](https://firebasestorage.googleapis.com/v0/b/dicionario-e6982.firebasestorage.app/o/dicionario_v1.apk?alt=media&token=24e345b1-6ed3-4e33-861d-4bfb0f07fa0f)
- [Baixar Empilha Segura](https://firebasestorage.googleapis.com/v0/b/dicionario-e6982.firebasestorage.app/o/empilha_segura_1.0.apk?alt=media&token=ef3e73f7-733b-41d1-bdf1-c390a013abe7)
- [Baixar EPI Express](https://firebasestorage.googleapis.com/v0/b/dicionario-e6982.firebasestorage.app/o/epi_express_v1.apk?alt=media&token=6bea23ab-1423-4c24-b918-a87abf196a55)

> Se uma APK for substituída e o token de download mudar, atualize imediatamente os links na landing page e nesta seção.

## 8. Landing page

A landing page é um projeto independente, armazenado na pasta irmã `Page`:

```text
Page/
├── index.html
├── styles.css
├── script.js
└── favicon.svg
```

### O que a página oferece

- Apresentação institucional do ecossistema;
- Quatro cards responsivos;
- Logo, descrição, versão e tamanho de cada aplicativo;
- Download direto das APKs no Firebase Storage;
- Acesso ao repositório GitHub de cada aplicativo;
- Orientação de instalação fora da Play Store;
- Layout responsivo para computador e celular;
- Animações com respeito à preferência de movimento reduzido.

### Prévia local

Abra um terminal na pasta `CheckList` e execute um servidor HTTP na pasta `docs`.

Exemplo com Python:

```bash
python -m http.server 4173 --directory docs
```

Depois abra:

```text
http://127.0.0.1:4173
```

Não abra o `index.html` diretamente pelo explorador se quiser reproduzir com fidelidade o comportamento da versão hospedada.

### Publicação recomendada

Usar **GitHub Pages** para a landing page e manter as APKs no **Firebase Storage**. A página deve possuir um repositório próprio, separado dos quatro aplicativos Android.

Configuração planejada:

```text
Repositório: apppoletto-etec/landingPage
Branch: main
Pasta: raiz do repositório
Endereço esperado: https://apppoletto-etec.github.io/landingPage/
```

No GitHub:

1. Crie ou abra o repositório exclusivo da landing page.
2. Entre em `Pages`.
3. Em `Build and deployment`, escolha `Deploy from a branch`.
4. Selecione a branch `main` e a pasta `/ (root)`.
5. Salve e aguarde a primeira publicação.
6. Abra o endereço público e teste os quatro downloads.

## 9. Como gerar uma nova versão

### 9.1 Preparação

1. Abra o projeto correto no Android Studio.
2. Atualize `versionCode` e `versionName` em `app/build.gradle.kts`.
3. Confirme que `applicationId` e `google-services.json` pertencem ao aplicativo.
4. Sincronize o Gradle.
5. Teste login, cadastro, listagens e operações principais.

### 9.2 Geração da APK

Para uma versão de distribuição, gere uma APK assinada pelo Android Studio:

```text
Build → Generate Signed Bundle / APK → APK
```

Nunca publique a chave de assinatura ou suas senhas.

### 9.3 Nomeação

Use nomes previsíveis, sem espaços ou acentos:

```text
<aplicativo>_<versao>.apk
```

Exemplos:

```text
empilha_segura_1.1.apk
epi_express_1.1.apk
```

### 9.4 Publicação

1. Copie a APK final para a pasta local `APKs`.
2. Envie o arquivo ao Storage do projeto `Dicionario`.
3. Obtenha o novo link de download.
4. Atualize o link, versão e tamanho no `Page/index.html`.
5. Atualize a tabela e os links deste documento.
6. Teste o download em uma janela anônima.
7. Faça commit e push das alterações.

## 10. Fluxo Git

Branch principal em todos os projetos:

```text
main
```

Antes de alterar:

```bash
git status
git pull --ff-only origin main
```

Depois de alterar e testar:

```bash
git add <arquivos>
git commit -m "Descreve claramente a alteração"
git push origin main
```

Regras:

- Não usar `git push --force` na branch `main`;
- Não sobrescrever alterações locais sem entender sua origem;
- Não versionar `local.properties`, senhas, tokens ou chaves privadas;
- Separar correções de código, documentação e releases quando possível;
- Usar mensagens de commit objetivas em português.

## 11. Segurança e credenciais

### Nunca versionar

- Tokens do GitHub;
- Senhas de contas;
- Chaves de assinatura Android;
- Arquivos `.jks` ou `.keystore`;
- Senhas do Firebase ou Google;
- Credenciais em arquivos Gradle;
- O arquivo local `acesso_git.txt`.

### Arquivo de acesso ao GitHub

Existe localmente em:

```text
Projeto_Polleto\projeto epi\acesso_git.txt
```

Ele serve apenas para autenticação local. Não deve ser copiado para nenhum repositório, documento, mensagem ou captura de tela.

### Histórico importante

Uma credencial antiga esteve escrita nos arquivos `settings.gradle.kts` e foi removida dos arquivos atuais em 24/09/2026. Como versões antigas permanecem no histórico Git, qualquer senha que tenha sido usada naquele trecho deve ser considerada comprometida e substituída.

### Tokens do Firebase nos links

Os parâmetros `token` presentes nos links das APKs são tokens de download dos objetos, utilizados intencionalmente pela landing page pública. Se um link for revogado ou regenerado, atualize todos os pontos que o utilizam.

## 12. Checklist de validação por aplicativo

Antes de publicar uma nova APK, valide:

- [ ] O projeto compila sem erros.
- [ ] A instalação limpa funciona.
- [ ] O login funciona.
- [ ] O cadastro de usuário funciona.
- [ ] O aplicativo acessa o Firebase correto.
- [ ] As telas principais abrem sem falhas.
- [ ] Cadastros e listagens persistem corretamente.
- [ ] Voltar e navegar entre telas não encerra o app.
- [ ] A versão exibida corresponde ao arquivo publicado.
- [ ] A APK foi testada em Android 7.0 ou superior.
- [ ] O link público baixa o arquivo correto.
- [ ] O card da landing page foi atualizado.

### Validações específicas

**Empilha Segura**

- [ ] Checklist diário;
- [ ] Checklist mensal;
- [ ] Cadastro e seleção de funcionário;
- [ ] Consulta aos registros.

**Dicionário de Segurança**

- [ ] Cadastro de termo;
- [ ] Listagem dos termos;
- [ ] Consulta das definições.

**EPI Express**

- [ ] Cadastro de EPI;
- [ ] Cadastro de funcionário;
- [ ] Entrega e devolução;
- [ ] Histórico de movimentações;
- [ ] Rotinas do WorkManager.

**Check Saúde**

- [ ] Cadastro de funcionário;
- [ ] Registro de exame;
- [ ] Listagem dos exames.

## 13. Como um novo agente deve iniciar

Ao receber uma tarefa neste ecossistema, siga esta ordem:

1. Identifique o aplicativo pela tabela da seção 2.
2. Leia o README do respectivo repositório.
3. Verifique `git status` antes de modificar qualquer arquivo.
4. Inspecione `app/build.gradle.kts`, `AndroidManifest.xml` e as classes envolvidas.
5. Preserve alterações locais que não pertencem à tarefa.
6. Faça a menor alteração capaz de resolver o objetivo.
7. Execute validações proporcionais ao risco.
8. Nunca revele credenciais em logs ou respostas.
9. Atualize testes, README e este mapa quando necessário.
10. Só considere a tarefa concluída após verificar o resultado final.

## 14. Estado atual

Em 24/09/2026:

- [x] Os quatro projetos estão publicados no GitHub.
- [x] Todos utilizam a branch `main`.
- [x] Os quatro repositórios possuem README próprio.
- [x] As quatro APKs estão no Firebase Storage central.
- [x] Os links diretos de download estão funcionando.
- [x] A landing page foi criada e validada localmente.
- [x] A landing page foi separada dos aplicativos na pasta local `Page`.
- [x] Repositório próprio criado em `apppoletto-etec/landingPage`.
- [x] O GitHub Pages foi ativado em `Settings → Pages` (`main` / root).
- [x] O endereço público (https://apppoletto-etec.github.io/landingPage/) está ativo e verificado.

## 15. Regra de ouro

> Código, infraestrutura e documentação devem sempre contar a mesma história.

Se um repositório, caminho, APK, versão, link, token de download ou fluxo mudar, atualize este documento no mesmo conjunto de alterações.

---

**Este é o mapa oficial de continuidade do ecossistema SST Digital. Preserve-o, mantenha-o atualizado e use-o como ponto de partida para qualquer nova jornada.**
