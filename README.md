# LuzioTeamTime

**Sistema colaborativo de gestión de tareas con temporizador Pomodoro**

Proyecto final de Desarrollo Web  
Licenciatura en Sistemas de Información y Tecnologías Inteligentes  
Universidad Veracruzana  
Periodo: agosto 2026 – enero 2027

## 1. Descripción del proyecto

LuzioTeamTime es un sistema web para organizar el trabajo mediante equipos, tableros y tareas colaborativas. Permite asignar responsabilidades, dar seguimiento al avance de las tareas y registrar sesiones de trabajo con la técnica Pomodoro.

El sistema contempla dos roles por equipo:

- **Líder:** administra integrantes, crea tableros, asigna tareas y revisa las entregas.
- **Colaborador:** trabaja en las tareas que tiene asignadas, actualiza su estado y registra sesiones Pomodoro.

El sistema también incluye funciones para consultar el cumplimiento de las tareas por integrante.

## 2. Tecnologías utilizadas

### Backend

- Java 17.
- Spring Boot 4.1.1.
- Maven.
- Spring Data JPA.
- Thymeleaf.
- MySQL.
- Spring Security Crypto para el cifrado de contraseñas.

### Frontend

- HTML.
- CSS.
- JavaScript.
- Fetch API.

## 3. Requisitos previos

El proyecto se prepara y ejecuta desde **WSL con Ubuntu**. Se requiere:

- Java JDK 17.
- Maven.
- MySQL Server.
- Python 3.
- Git.

MySQL debe instalarse y ejecutarse dentro de WSL. No se utiliza MySQL Workbench ni una instalación de MySQL para Windows.

## 4. Obtener el proyecto

Clonar el repositorio y entrar a la carpeta:

```bash
git clone https://github.com/SaraOrtegaHdez13/desarrollo-web.git
cd desarrollo-web
```

## 5. Preparar el entorno en WSL

Actualizar los paquetes e instalar las herramientas:

```bash
sudo apt update
sudo apt install openjdk-17-jdk maven mysql-server python3 git
```

Comprobar que las herramientas estén disponibles:

```bash
java -version
mvn -version
mysql --version
python3 --version
git --version
```

## 6. Configurar MySQL

### 6.1. Iniciar el servidor

Desde WSL:

```bash
sudo service mysql start
```

### 6.2. Crear la base de datos y el usuario

Entrar a MySQL:

```bash
sudo mysql
```

Crear la base de datos:

```sql
CREATE DATABASE luzioteamtime;
```

Crear un usuario local para la aplicación. Se debe sustituir el texto de ejemplo por una contraseña propia:

```sql
CREATE USER 'usuario_luzio'@'localhost'
IDENTIFIED BY 'CAMBIAR_POR_UNA_CONTRASENA_SEGURA';

GRANT ALL PRIVILEGES ON luzioteamtime.*
TO 'usuario_luzio'@'localhost';

FLUSH PRIVILEGES;
```

Salir de MySQL:

```sql
EXIT;
```

Cada integrante debe configurar su propia base de datos y sus credenciales en su instalación de WSL. No es necesario compartir una misma base de datos para trabajar con el repositorio.

**Importante:** no se deben subir contraseñas reales al repositorio ni escribirlas en este README.

## 7. Configurar las variables de entorno

El backend obtiene la dirección del servidor y las credenciales de MySQL mediante estas variables:

- `BASE_DATOS`
- `DB_USUARIO`
- `DB_CONTRASENA`

En la misma terminal de WSL desde la que se ejecutará el backend, configurar los valores correspondientes a la instalación local:

```bash
export BASE_DATOS=localhost
export DB_USUARIO=usuario_luzio
export DB_CONTRASENA='CAMBIAR_POR_UNA_CONTRASENA_SEGURA'
```

Las variables deben configurarse nuevamente si se abre otra terminal. No se deben guardar contraseñas reales en archivos del repositorio.

## 8. Ejecutar el backend

Desde la raíz del repositorio:

```bash
cd backend
mvn spring-boot:run
```

El backend queda disponible en:

```text
http://localhost:8081
```

La configuración de `backend/src/main/resources/application.properties` establece:

- Puerto `8081`.
- Base de datos `luzioteamtime`.
- Credenciales mediante variables de entorno.
- `spring.jpa.hibernate.ddl-auto=update`.

Hibernate crea o actualiza las tablas a partir de las entidades JPA al iniciar la aplicación. Por ello, para una instalación nueva se debe crear primero la base de datos y configurar correctamente las variables de entorno.

## 9. Ejecutar el frontend

Mantener el backend en ejecución. Abrir una **segunda terminal de WSL** y, desde la raíz del repositorio, ejecutar:

```bash
cd frontend
python3 -m http.server 5500
```

Abrir el navegador en:

```text
http://localhost:5500
```

No abrir los archivos HTML directamente mediante `file://` ni utilizar `127.0.0.1` como dirección del frontend.

## 10. Comunicación entre frontend y backend

El frontend realiza las peticiones al backend mediante:

```text
http://localhost:8081
```

El origen permitido por la configuración CORS es:

```text
http://localhost:5500
```

La configuración permite los métodos HTTP utilizados por la aplicación y habilita el envío de credenciales. Las peticiones que requieren una sesión deben enviar la cookie de sesión mediante `credentials: 'include'`.

Para utilizar el sistema, el backend y el servidor del frontend deben permanecer ejecutándose en sus respectivas terminales.

## 11. Ejecutar las pruebas automatizadas

Las pruebas automatizadas del backend se ejecutan desde la carpeta `backend`.

Si se utiliza una base de datos o configuración local para las pruebas, deben estar preparados los requisitos correspondientes antes de ejecutarlas.

Comando:

```bash
mvn test
```

Los reportes generados por Maven se encuentran en:

```text
backend/target/surefire-reports/
```

## 12. Base de datos y archivo de esquema

La aplicación utiliza MySQL y JPA para gestionar la persistencia. Cada instalación debe contar con una base de datos llamada `luzioteamtime`.

Hibernate administra la creación y actualización de las tablas a partir de las entidades JPA mediante la propiedad `spring.jpa.hibernate.ddl-auto=update`.

El archivo `database/esquema.sql` documenta la estructura de la base de datos, sin incluir registros. Para actualizarlo desde la raíz del repositorio, con MySQL iniciado y la base de datos actualizada por Hibernate, ejecutar:

```bash
mysqldump -u usuario_luzio -p --no-data --no-tablespaces luzioteamtime > database/esquema.sql
```

El comando solicitará la contraseña del usuario de MySQL. La opción `--no-tablespaces` evita que `mysqldump` solicite el privilegio `PROCESS` para consultar información de tablespaces.

Este archivo es una referencia de la estructura; la creación y actualización de las tablas durante la ejecución de la aplicación se realiza mediante Hibernate.

## 13. Estructura general del repositorio

```text
desarrollo-web/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/mx/uv/luzioteamtime/
│   │   │   │   ├── controlador/
│   │   │   │   ├── modelo/
│   │   │   │   ├── repositorio/
│   │   │   │   └── servicio/
│   │   │   └── resources/
│   │   └── test/
│   └── pom.xml
├── database/
│   └── esquema.sql
├── documentacion/
│   ├── diseno/
│   ├── IAG/
│   └── reportes/
├── frontend/
│   ├── css/
│   ├── js/
│   └── archivos HTML
└── README.md
```

## 14. Consideraciones para ejecutar el proyecto

1. Iniciar MySQL dentro de WSL.
2. Crear la base de datos y el usuario local.
3. Configurar `BASE_DATOS`, `DB_USUARIO` y `DB_CONTRASENA` en la terminal del backend.
4. Ejecutar el backend en el puerto `8081`.
5. Ejecutar el servidor del frontend en el puerto `5500`.
6. Abrir `http://localhost:5500` en el navegador.

Cada integrante debe mantener sus credenciales locales fuera del repositorio. El repositorio contiene el código fuente y la documentación necesaria para preparar una instalación independiente.
