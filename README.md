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

## Pruebas

El proyecto incluye las carpetas de pruebas unitarias (`app/src/test`) y pruebas instrumentadas (`app/src/androidTest`). Las pruebas incluidas actualmente son las pruebas base del proyecto Android; se recomienda ampliarlas con pruebas específicas para registro, autenticación, persistencia y navegación.

## Identificador de aplicación

```text
com.example.lexstudy
```

## 👥 Autores

- **Miguel Gutierrez (Migueldinho):** [GitHub](https://github.com/Migueldinho)
- **Sebastian Ramirez (FlayingWolf):** [GitHub](https://github.com/FlayingWolf)

---

Proyecto académico **LexStudy**.
