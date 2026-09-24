<div align="center">
  <img src="app/src/main/res/drawable/logo_empilhadeira.png" width="120" alt="Logo do Empilha Segura">
  <h1>Empilha Segura</h1>
  <p>Checklists digitais para uma operação de empilhadeiras mais segura, organizada e rastreável.</p>

  ![Android](https://img.shields.io/badge/Android-24%2B-3DDC84?style=for-the-badge&logo=android&logoColor=white)
  ![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
  ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
  ![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
</div>

> 🗺️ **Continuidade do ecossistema:** consulte o [Mapa do Tesouro](MAPA_DO_TESOURO.md), o guia oficial para manutenção, releases, Firebase, APKs e landing page.

## Sobre o projeto

O **Empilha Segura** é um aplicativo Android voltado ao acompanhamento preventivo de empilhadeiras. Ele centraliza verificações diárias e mensais, mantém o histórico dos registros e ajuda equipes a adotarem uma rotina operacional mais segura.

## Funcionalidades

- Autenticação e cadastro de usuários;
- Cadastro e gerenciamento de funcionários;
- Checklists diários de inspeção;
- Checklists mensais de acompanhamento;
- Consulta aos registros realizados;
- Armazenamento e sincronização com Firebase.

## Tecnologias

- Android SDK com Java e Kotlin;
- Material Design e View Binding;
- Firebase Authentication, Firestore, Realtime Database e Storage;
- Picasso para carregamento de imagens;
- Gradle Kotlin DSL.

## Como executar

1. Clone este repositório.
2. Abra o projeto no Android Studio.
3. Configure um projeto Firebase e adicione `app/google-services.json`.
4. Sincronize as dependências do Gradle.
5. Execute em um dispositivo ou emulador com Android 7.0 (API 24) ou superior.

```bash
git clone https://github.com/apppoletto-etec/empilha_segura.git
```

## Estrutura

```text
app/src/main/
├── java/com/lima/checklist/
│   ├── adapter/   # Listagens e componentes de interface
│   ├── bd/        # Integração com Firebase
│   ├── model/     # Entidades da aplicação
│   └── view/      # Telas e fluxos do aplicativo
└── res/           # Layouts, imagens, menus e temas
```

## Objetivo

Transformar inspeções operacionais em um processo simples, padronizado e acessível, contribuindo para a prevenção de falhas e acidentes.

---

<div align="center">Desenvolvido como parte do ecossistema de segurança do trabalho <strong>Poletto ETEC</strong>.</div>
