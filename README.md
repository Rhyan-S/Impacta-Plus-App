# Impacta+
Aplicativo Android nativo para conectar voluntários a ações sociais de impacto.

O Impacta+ é uma solução mobile desenvolvida para facilitar o engajamento em causas sociais, permitindo que usuários encontrem, se inscrevam e gerenciem sua participação em eventos de voluntariado.

### Funcionalidades
- **Autenticação:** Cadastro e Login de voluntários (SQLite).
- **Feed de Ações:** Listagem de oportunidades de voluntariado disponíveis.
- **Detalhes da Ação:** Informações completas sobre local, data e impacto da ação.
- **Inscrição:** Sistema de candidatura com controle de vagas em tempo real.
- **Histórico:** Visualização das ações em que o usuário já participou.
- **Perfil:** Gestão de dados do voluntário.

### Tecnologias Utilizadas
- Linguagem: Java (Android Nativo)
- Banco de Dados: SQLite (com padrão Singleton e Repository)
- Layout: XML (ConstraintLayout, LinearLayout, RecyclerView)
- Componentes: Material Design (Cards, BottomNavigation)
- Arquitetura: MVC/MVP (Em processo de refatoração para Clean Architecture)

### Roadmap para os próximos passos do projeto:
    Implementação de Hashing de Senha (SHA-256);
    Migração do banco de dados;
    Funcionalidade de "Esqueci minha senha".
