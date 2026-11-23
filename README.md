# LexStudy

**LexStudy** es una aplicación móvil Android creada como proyecto académico para apoyar el estudio y la organización de contenidos relacionados con la legislación chilena. Permite consultar fichas de normas de ejemplo, crear apuntes personales y elaborar mapas conceptuales, con almacenamiento local de la información.

## Funcionalidades

### Registro e inicio de sesión

- Registro con nombre, correo institucional y contraseña.
- Validación del nombre, formato del correo, longitud mínima de la contraseña y coincidencia de confirmación.
- Admite correos con los dominios `@duocuc.cl` y `@profesor.duoc.cl`.
- Comprueba que no exista otra cuenta con el mismo correo.
- Inicio y cierre de sesión, con restauración de la sesión guardada al volver a abrir la aplicación.

### Consulta de normas

- Catálogo de fichas de ejemplo para materias civil, laboral, constitucional y penal.
- Búsqueda y filtrado por texto y materia.
- Vista de resumen, artículo y referencia informativa.

### Estudiar y organizar

- Ficha de estudio para la norma seleccionada.
- Ajuste del tamaño del texto.
- Creación, edición y eliminación de apuntes personales.
- Posibilidad de adjuntar un archivo PDF seleccionado desde el dispositivo.
- Confirmaciones y mensajes sobre el resultado de las operaciones.

### Mapas conceptuales

- Creación manual de mapas con título, concepto central y dos conceptos relacionados.
- Vista de los mapas guardados.
- Eliminación con confirmación.
- Opción de crear un ejemplo de propuesta asistida para demostración.

### Perfil

- Consulta del nombre, correo institucional y tipo de cuenta.
- Captura y guardado local de una fotografía de perfil usando la cámara.
- Cambio de contraseña mediante la verificación de la contraseña actual.

## Tecnologías utilizadas

- **Kotlin** como lenguaje de programación.
- **Jetpack Compose** y **Material 3** para construir la interfaz.
- **Android Jetpack Navigation Compose** para la navegación entre secciones.
- **Arquitectura MVVM** con `ViewModel` y repositorios.
- **Room** para la base de datos local.
- **KSP** para el procesamiento de anotaciones de Room.
- **Kotlin Coroutines y Flow** para operaciones asíncronas y observación de datos.

## Arquitectura del proyecto

El código principal se encuentra en `app/src/main/java/com/example/lexstudy/`.

```text
com.example.lexstudy/
├── MainActivity.kt
├── model/
│   ├── AppDatabase.kt
│   ├── Usuario.kt
│   ├── UsuarioDao.kt
│   ├── Apunte.kt
│   ├── ApunteDao.kt
│   ├── MapaConceptual.kt
│   ├── MapaConceptualDao.kt
│   ├── Sesion.kt
│   └── SesionDao.kt
├── repository/
│   ├── LexStudyRepository.kt
│   ├── SesionRepository.kt
│   ├── FotoPerfilRepository.kt
│   └── LeyesDemo.kt
├── ui/
│   ├── AppNavigation.kt
│   ├── LoginScreen.kt
│   ├── ConsultarNormasScreen.kt
│   ├── EstudiarOrganizarScreen.kt
│   ├── MapasConceptualesScreen.kt
│   ├── PerfilScreen.kt
│   ├── BotonCargando.kt
│   ├── ColoresLexStudy.kt
│   └── theme/
└── viewmodel/
    ├── AuthViewModel.kt
    ├── LexStudyViewModel.kt
    └── ViewModelFactories.kt
```

- **`model/`** contiene las entidades de Room y sus DAO.
- **`repository/`** concentra las operaciones de acceso a datos, sesión y fotografía de perfil.
- **`ui/`** contiene las pantallas Compose, la navegación y los componentes visuales.
- **`viewmodel/`** gestiona el estado de autenticación y las operaciones de estudio.

## Persistencia de datos

La aplicación usa una base de datos local Room llamada `lexstudy.db`. Incluye entidades para usuarios, apuntes, mapas conceptuales y sesión activa.

- Los apuntes y mapas se asocian al usuario correspondiente.
- La sesión se guarda localmente para restaurarla al iniciar la aplicación.
- La fotografía de perfil se guarda en el almacenamiento privado de la aplicación y su ruta queda asociada al usuario.
- Las contraseñas no se guardan en texto plano: el proyecto almacena un salt y un hash calculado con SHA-256.

Los datos se almacenan en el dispositivo. El proyecto no incorpora sincronización con un servidor ni autenticación remota.

## Requisitos de desarrollo

La configuración actual del proyecto utiliza:

| Componente | Versión/configuración |
|---|---|
| Android Gradle Plugin | `9.2.1` |
| Gradle Wrapper | `9.8.2` |
| Kotlin | `2.2.10` |
| Compile SDK | `37` |
| Minimum SDK | `37` |
| Target SDK | `37` |
| Jetpack Compose BOM | `2026.02.01` |
| Room | `2.8.5` |
| Navigation Compose | `2.10.2` |
| Lifecycle | `2.10.0` |
| KSP | `2.3.12` |

Para compilar, instala Android Studio con un JDK compatible con Gradle 9.8.2 y las herramientas/plataforma Android SDK 37. El valor `minSdk = 37` significa que la aplicación requiere un dispositivo o emulador con API 37 o superior.

## Cómo abrir y ejecutar el proyecto

1. Clona o descarga el repositorio.
2. Abre la carpeta raíz `LexStudy` en Android Studio.
3. Permite que Gradle sincronice las dependencias.
4. Configura un emulador o conecta un dispositivo compatible con API 37 o superior.
5. Ejecuta la configuración `app` desde Android Studio.

También puedes compilar desde la terminal ubicada en la carpeta raíz del proyecto:

**Windows (PowerShell):**

```powershell
.\gradlew.bat assembleDebug
```

**macOS/Linux:**

```bash
./gradlew assembleDebug
```

El APK de depuración se genera normalmente en `app/build/outputs/apk/debug/app-debug.apk`.

## Alcance y consideraciones

- Las normas incluidas en `LeyesDemo.kt` son fichas sintéticas de demostración. Los resúmenes no sustituyen el texto legal íntegro y el catálogo no consulta ni actualiza automáticamente una fuente oficial. Para estudiar un caso real, revisa la legislación vigente en una fuente oficial, como LeyChile de la Biblioteca del Congreso Nacional de Chile.
- La opción **“Con IA · demo”** de mapas conceptuales genera un ejemplo local predefinido. No se conecta a un modelo de inteligencia artificial ni a un servicio externo.
- Los archivos PDF se seleccionan desde el dispositivo y la aplicación conserva la URI del documento adjunto.
- La información de usuarios, apuntes, mapas y sesión se guarda localmente; no se comparte entre dispositivos mediante una cuenta en línea.

## Arquitectura y datos

El proyecto usa pantallas Compose, navegación, ViewModels y Room para usuarios, sesiones, apuntes y mapas. El perfil obtiene una miniatura de la cámara mediante `TakePicturePreview`. La pantalla de estudio usa `OpenDocument` para elegir PDF. La biblioteca consulta datos mediante Retrofit, corrutinas y Gson. El repositorio dirige la selección a su endpoint y valida las sugerencias. Solo el texto que el usuario envía desde ese formulario se transmite a la biblioteca.

## Contrato de la biblioteca

| Acción en la app | Método | Ruta |
|---|---|---|
| Abrir Glosario | GET | /glosario |
| Abrir Materias | GET | /materias |
| Abrir Fuentes | GET | /fuentes |
| Abrir Ejercicios | GET | /ejercicios |
| Abrir Técnicas | GET | /tecnicas |
| Abrir Preguntas | GET | /preguntas |
| Enviar sugerencia | POST | /sugerencias |

Cada GET devuelve un arreglo JSON de objetos con campos de texto `titulo` y `detalle`. Una lista sin registros devuelve `[]`. El POST recibe por ejemplo `{"texto":"Interpretación de fuentes"}` y devuelve el registro creado con un `id` entero positivo. Se admiten entre 5 y 500 caracteres tras recortar espacios.

### Probar inmediatamente con el emulador

En una terminal del proyecto inicia `python backend/servidor.py` (Python 3.9+). En otra terminal ejecuta `adb reverse tcp:8765 tcp:8765` con el emulador o teléfono conectado. En Windows, adb.exe está en la carpeta platform-tools del Android SDK. Ejecuta la variante debug y abre Biblioteca en línea. Si no has configurado Xano, la aplicación usa el servidor del computador. Las consultas leen las seis colecciones CSV y las sugerencias se guardan en SQLite junto al servidor. Repite adb reverse si desconectas el dispositivo.

### Publicar en Xano

1. Crea un grupo API propio llamado LexStudy. Importa cada CSV de `backend/xano` en una tabla con el mismo nombre del archivo, sin extensión. Conserva `titulo` y `detalle` como texto. Crea además la tabla `sugerencias` con ID automático y campo de texto `texto`.
2. Publica en ese grupo los seis GET del cuadro, cada uno con una consulta de todos los registros de su tabla y respuesta de tipo lista sin paginación. Para el POST define la entrada `texto`, recorta espacios, valida su longitud, agrega el registro y devuelve el objeto creado. Expón únicamente las siete operaciones indicadas.
3. Puedes pegar la URL del grupo directamente en **Biblioteca en línea → Configurar servidor**. Para preconfigurar futuras instalaciones, copia la URL base **del grupo publicado** y agrega en tu archivo de usuario `~/.gradle/gradle.properties` (en Windows, `%USERPROFILE%\.gradle\gradle.properties`) la línea `LEXSTUDY_API_URL=https://TU-INSTANCIA.xano.io/api:TU-GRUPO/`. Sustituye los dos marcadores y conserva la barra final. El enlace Swagger de todo el workspace muestra varios grupos y sirve para consultarlos.
4. Sincroniza y recompila. Abre las seis secciones y envía una sugerencia. Comprueba que el ID de confirmación coincide con el guardado en Xano.

## Pruebas unitarias

En Android Studio, abre `app/src/test/java/com/example/lexstudy/BibliotecaRepositoryTest.kt` y ejecuta la clase con el triángulo verde. También puedes usar `.\gradlew.bat :app:testDebugUnitTest` en Windows. Tres pruebas JUnit 4 verifican la selección de las seis colecciones, la limpieza y confirmación de una sugerencia y el rechazo de longitudes inválidas antes de llamar al servicio. Una API falsa permite ejecutarlas sin red ni emulador. El informe se genera en `app/build/reports/tests/testDebugUnitTest/index.html`.

## APK firmado de entrega

Configura la URL HTTPS de producción y prueba la biblioteca con ella primero. En Android Studio elige **Build → Generate Signed App Bundle or APK → APK**, módulo **app**, selecciona tu almacén de claves o créalo fuera del repositorio, escoge **release** y genera el archivo. Abre la carpeta indicada por el asistente. Verifica con `apksigner verify --verbose --print-certs app-release.apk` desde Android SDK Build Tools y prueba la instalación en un equipo con API 37 o superior. El launcher utiliza el libro del logotipo de LexStudy. Guarda el keystore y su contraseña de forma privada para poder firmar futuras actualizaciones.

El flujo de GitHub Actions también compila release y firma un APK de evaluación con una clave temporal distinta por ejecución. Su artefacto se llama `LexStudy-release`. Para actualizar una instalación conservando sus datos, usa tu clave estable desde Android Studio.

## Seguimiento

[Tablero Trello de LexStudy](https://trello.com/b/5SmXPykU/lexstudy) · [Referencia de pruebas del profesor](https://github.com/amaurelia/pruebas-unitarias-kotlin)


## Identificador de aplicación

```text
com.example.lexstudy
```

## 👥 Autores

- **Miguel Gutierrez (Migueldinho):** [GitHub](https://github.com/Migueldinho)
- **Sebastian Ramirez (FlayingWolf):** [GitHub](https://github.com/FlayingWolf)

---

Proyecto académico **LexStudy**.

