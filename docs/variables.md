# Descripción de Variables de Entorno

Este documento proporciona una explicación de cada variable de entorno utilizada en el proyecto.

## Variables Generales

- **APP_HOST**: La URL base donde se ejecuta la aplicación. Usualmente es `http://localhost:8080` durante el desarrollo local.

## Variables de la Base de Datos

- **DB_HOST**: El host o dirección IP del servidor de base de datos.
- **DB_NAME**: El nombre de la base de datos a la que se conecta la aplicación.
- **DB_PASSWORD**: La contraseña para autenticarse en la base de datos.
- **DB_PORT**: El puerto en el que el servidor de base de datos está escuchando. Por defecto, para MySQL suele ser `3306`.
- **DB_USER**: El nombre de usuario para conectarse a la base de datos.

## Variables de Google OAuth2

- **GOOGLE_CLIENT_ID**: El ID del cliente OAuth2 de Google utilizado para la autenticación.
- **GOOGLE_SECRET_ID**: La clave secreta del cliente OAuth2 de Google utilizada para la autenticación.

## Variables de Wompi

Estas variables se utilizan para integrar la pasarela de pagos Wompi.

- **WOMPI_CLIENT_ID**: El ID del cliente Wompi que identifica a la aplicación en la plataforma de Wompi.
- **WOMPI_SECRET_KEY**: La clave secreta para realizar autenticaciones seguras en la API de Wompi.
- **WOMPI_API_URL**: La URL base de la API de Wompi. Por lo general, es `https://api.wompi.sv`.

## Variables de Firebase

Estas variables son necesarias para conectar la aplicación con Firebase y utilizar sus servicios, como Cloud Storage.

- **FIREBASE_PROJECT_ID**: El ID del proyecto de Firebase.
- **FIREBASE_PRIVATE_KEY_ID**: El ID de la clave privada del servicio de cuenta de Firebase.
- **FIREBASE_PRIVATE_KEY**: La clave privada utilizada para autenticar la aplicación con Firebase. Debe estar en formato PEM.
- **FIREBASE_CLIENT_EMAIL**: El correo electrónico del cliente de servicio utilizado para la autenticación con Firebase.
- **FIREBASE_CLIENT_ID**: El ID del cliente utilizado en la autenticación OAuth2.
- **FIREBASE_AUTH_URI**: La URL de autorización de OAuth2 utilizada por Google.
- **FIREBASE_TOKEN_URI**: La URL del token de OAuth2 utilizada por Google para la autenticación.
- **FIREBASE_AUTH_PROVIDER_X509_CERT_URL**: La URL de los certificados x509 del proveedor de autenticación de Google.
- **FIREBASE_CLIENT_X509_CERT_URL**: La URL del certificado x509 del cliente utilizado para verificar la autenticidad de las solicitudes.
- **FIREBASE_STORAGE_BUCKET**: El nombre del bucket de Cloud Storage en Firebase donde se almacenan los archivos.

