# LuzioTeamTime

**Sistema colaborativo de gestión de tareas con temporizador Pomodoro**

Proyecto final de Desarrollo Web  
Licenciatura en Sistemas de Información y Tecnologías Inteligentes  
Universidad Veracruzana  
Periodo: agosto 2026 – enero 2027

## 1. Descripción del proyecto

LuzioTeamTime es un sistema web para organizar tareas mediante equipos y tableros colaborativos. Permite asignar responsabilidades, dar seguimiento al avance de las tareas y registrar sesiones de trabajo con la técnica Pomodoro.

El sistema contempla dos roles por equipo:

- **Líder:** administra integrantes, crea tableros, asigna tareas y revisa las entregas.
- **Colaborador:** trabaja en sus tareas, actualiza su estado y registra sesiones Pomodoro.

También permite consultar reportes de cumplimiento por integrante.

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

Se necesita instalar:

- Java JDK 17.
- Maven.
- MySQL Server.
- Python 3.

En Windows, el proyecto debe ejecutarse desde WSL con Ubuntu, conforme a los requisitos de la asignatura.

## 4. Obtener el proyecto

Clonar el repositorio:

```bash
git clone https://github.com/SaraOrtegaHdez13/desarrollo-web.git
cd desarrollo-web
```

## 5. Preparar el entorno

### 5.1. Windows con WSL y Ubuntu

Desde la terminal de Ubuntu:

```bash
sudo apt update
sudo apt install openjdk-17-jdk maven mysql-server python3
```

Comprobar las instalaciones:

```bash
java -version
mvn -version
mysql --version
python3 --version
```

### 5.2. macOS

Con Homebrew instalado, ejecutar desde Terminal:

```bash
brew install openjdk@17 maven mysql python
```

Comprobar las instalaciones:

```bash
java -version
mvn -version
mysql --version
python3 --version
```

Si macOS no reconoce Java 17, será necesario configurar `JAVA_HOME` de acuerdo con la ubicación de instalación indicada por Homebrew.

## 6. Configurar MySQL

Iniciar el servidor MySQL.

En Ubuntu/WSL:

```bash
sudo service mysql start
```

En macOS con Homebrew:

```bash
brew services start mysql
```

Entrar a MySQL:

```bash
sudo mysql
```

En macOS, si la instalación utiliza autenticación sin `sudo`, entrar con:

```bash
mysql -u root -p
```

Crear la base de datos:

```sql
CREATE DATABASE luzioteamtime;
```

Crear un usuario para la aplicación:

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

Cada integrante y cada instalación pueden utilizar su propia base de datos local. No es necesario compartir una misma base de datos para trabajar con el repositorio.

## 7. Configurar las variables de entorno

El backend obtiene los datos de conexión mediante tres variables:

- `BASE_DATOS`
- `DB_USUARIO`
- `DB_CONTRASENA`

En la misma terminal donde se ejecutará el backend, configurar:

```bash
export BASE_DATOS=localhost
export DB_USUARIO=usuario_luzio
export DB_CONTRASENA=CAMBIAR_POR_UNA_CONTRASENA_SEGURA
```

Estas variables deben contener los datos del usuario creado en MySQL.

No se deben guardar contraseñas reales en el repositorio.

## 8. Ejecutar el backend

Desde la raíz del proyecto:

```bash
cd backend
mvn spring-boot:run
```

El backend se ejecuta en:

```text
http://localhost:8081
```

La configuración de `application.properties` utiliza:

- Puerto `8081`.
- Base de datos `luzioteamtime`.
- Variables de entorno para las credenciales.
- Hibernate con `ddl-auto=update`.

Hibernate crea o actualiza las tablas a partir de las entidades JPA del proyecto.

## 9. Ejecutar el frontend

Abrir una segunda terminal y regresar a la raíz del repositorio:

```bash
cd frontend
python3 -m http.server 5500
```

Abrir el navegador en:

```text
http://localhost:5500
```

No abrir los archivos HTML directamente mediante `file://`.

## 10. Comunicación entre frontend y backend

El frontend realiza las peticiones a:

```text
http://localhost:8081
```

El backend permite solicitudes desde el origen:

```text
http://localhost:5500
```

La configuración CORS permite los métodos GET, POST, PUT, DELETE y OPTIONS, y habilita el envío de credenciales.

Las peticiones que utilizan sesión envían la cookie mediante `credentials: 'include'`.

## 11. Ejecutar las pruebas automatizadas

Desde la carpeta `backend`, con las variables de entorno configuradas:

```bash
mvn test
```

Los resultados de las pruebas se pueden consultar en:

```text
target/surefire-reports/
```

## 12. Base de datos

La aplicación utiliza MySQL y JPA para gestionar la persistencia.

Cada instalación debe crear su propia base de datos llamada `luzioteamtime`.

La estructura de las tablas se genera y actualiza mediante Hibernate a partir de las entidades del proyecto.

El archivo `database/esquema.sql` contiene el esquema de la base de datos cuando se genera a partir de MySQL.


