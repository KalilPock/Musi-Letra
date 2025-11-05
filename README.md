MusiLetra é um aplicativo Android simples que permite aos usuários armazenar e gerenciar letras de músicas. Você pode adicionar, editar, visualizar e excluir suas letras de músicas favoritas, além de pesquisar letras online.

Recursos
Adicionar, editar e excluir músicas: Gerencie facilmente sua coleção pessoal de letras de músicas.
Visualizar lista de músicas: Veja todas as suas músicas salvas em uma lista clara e organizada.
Visualização detalhada: Toque em uma música para ver sua letra completa.
Pesquisa online: Encontre novas letras de uma fonte online e adicione-as diretamente à sua coleção.
Estrutura do Projeto
O aplicativo segue uma arquitetura moderna do Android e é construído usando:

UI: Jetpack Compose para construir a interface do usuário.
Gerenciamento de Estado: ViewModel para gerenciar dados relacionados à UI e lidar com interações do usuário.
Navegação: Navegação Jetpack para navegar entre telas.
O código-fonte está organizado nos seguintes pacotes:

data: Contém as fontes de dados e o repositório.
model: Define os modelos de dados (por exemplo, Song).
ui: Abriga todos os componentes relacionados à UI, incluindo telas, temas e o SongViewModel.
Como Construir
Para construir e executar este projeto, você precisará:

Android Studio (versão estável mais recente recomendada)
Um dispositivo Android ou emulador
Siga estas etapas:

Clone o repositório para sua máquina local.
Abra o projeto no Android Studio.
Deixe o Android Studio sincronizar o projeto e baixar as dependências necessárias.
Execute o aplicativo em um dispositivo Android ou emulador.
Dependências
Este projeto usa as seguintes dependências principais:

Jetpack Compose: Para construir a UI.
AndroidX: Bibliotecas Core KTX, Lifecycle e Activity.
Material 3: Para componentes de Material Design.
Navigation Compose: Para navegar entre telas.
Retrofit: Para fazer requisições de rede à API de pesquisa online.
Gson: Para analisar respostas JSON da API.
