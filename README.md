# MusiLetra

MusiLetra é um aplicativo Android simples que permite aos usuários armazenar e gerenciar letras de músicas. Você pode adicionar, editar, visualizar e excluir suas letras de músicas favoritas, além de pesquisar letras online.

---

## 🎵 Funcionalidades (Features)

* **Adicionar, editar e excluir músicas:** Gerencie facilmente sua coleção pessoal de letras de músicas.
* **Visualizar lista de músicas:** Veja todas as suas músicas salvas em uma lista clara e organizada.
* **Visualização detalhada:** Toque em uma música para visualizar sua letra completa.
* **Pesquisa online:** Encontre novas letras de uma fonte online e adicione-as diretamente à sua coleção.

---

## 🏗️ Estrutura do Projeto (Project Structure)

O aplicativo segue uma arquitetura Android moderna e é construído usando:

* **Interface (UI):** Jetpack Compose para a construção da interface do usuário.
* **Gerenciamento de Estado (State Management):** `ViewModel` para gerenciar dados relacionados à UI e lidar com interações do usuário.
* **Navegação (Navigation):** Jetpack Navigation para navegar entre as telas.

O código-fonte está organizado nos seguintes pacotes:

* `data`: Contém as fontes de dados e o repositório.
* `model`: Define os modelos de dados (ex: `Song`).
* `ui`: Contém todos os componentes relacionados à UI, incluindo telas, temas e o `SongViewModel`.

---

## ⚙️ Como Construir (How to Build)

Para construir e executar este projeto, você precisará de:

* Android Studio (a versão estável mais recente é recomendada)
* Um dispositivo ou emulador Android

Siga estes passos:

1. Clone o repositório para sua máquina local.
2. Abra o projeto no Android Studio.
3. Permita que o Android Studio sincronize o projeto e baixe as dependências necessárias.
4. Execute o aplicativo em um dispositivo ou emulador Android.

---

## 📚 Dependências

Este projeto utiliza as seguintes dependências principais:

* **Jetpack Compose:** Para a construção da UI.
* **AndroidX:** Bibliotecas Core KTX, Lifecycle e Activity.
* **Material 3:** Para componentes de Material Design.
* **Navigation Compose:** Para navegação entre telas.
* **Retrofit:** Para fazer requisições de rede à API de pesquisa online.
* **Gson:** Para fazer o *parsing* (análise) de respostas JSON da API.
