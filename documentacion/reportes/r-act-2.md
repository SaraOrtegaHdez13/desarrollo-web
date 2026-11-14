# Reporte de Avance del Proyecto

**Equipo:** Sara Ortega Hernández, María de Lourdes Méndez Prigadaá y Aradan Schwartz Rocha  
**Fecha:** 13/11/2026  
**Número de reporte:** 2  
**Repositorio GitHub:** https://github.com/SaraOrtegaHdez13/desarrollo-web

\---

# 1\. Actividades realizadas

* Implementación de las entidades y repositorios correspondientes a los módulos de usuarios, equipos, tableros y tareas.
* Implementación del registro de usuarios, inicio y cierre de sesión, y consulta del usuario autenticado.
* Implementación del interceptor de sesión para proteger las rutas que requieren autenticación.
* Configuración de CORS para permitir solicitudes desde el frontend en `http://localhost:5500`, con envío de credenciales.
* Implementación de operaciones para crear equipos, consultar equipos, consultar sus integrantes, agregar miembros, modificar sus roles, eliminar miembros y archivar equipos.
* Implementación de la consulta de tableros asociados a un equipo mediante `GET /obtenerTableros/{idEquipo}`.
* Corrección del repositorio `ITablero` para permitir obtener múltiples tableros asociados a un mismo equipo.
* Realización y documentación de las primeras pruebas CURL de los módulos de usuarios, equipos y CORS.
* Documentación del uso de IA generativa para apoyar la revisión del código y la elaboración de pruebas.

\---

# 2\. Evidencias de trabajo

|Actividad|Integrante responsable|Enlace a commit|
|-|-|-|
|Implementación de entidades y repositorios de usuarios y equipos|Sara|[50c37e0](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/50c37e0)|
|Implementación del registro e inicio de sesión de usuarios|Sara|[33e1c54](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/33e1c54)|
|Implementación del interceptor para validar sesiones|Sara|[5dad61f](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/5dad61f)|
|Implementación de operaciones para la gestión de equipos y miembros|Sara|[790cebd](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/790cebd)|
|Actualización de archivos excluidos del repositorio|Sara|[db62687](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/db62687)|
|Implementación de entidades y repositorios de tableros y tareas|Lourdes|[c384e0d](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/c384e0d)|
|Corrección para obtener múltiples tableros por equipo|Lourdes|[6b0d004](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/6b0d004)|
|Implementación del endpoint para obtener tableros de un equipo|Lourdes|[644aa33](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/644aa33)|
|Configuración de CORS para el acceso desde el frontend|Aradan|[edd82ca](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/edd82ca)|
|Entrega de la documentación de las primeras pruebas CURL de los módulos de usuario, equipos y CORS. Cada integrante realizó su documentación, y un solo integrante subió todo junto. |Sara|[c5c50e8](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/c5c50e8)|

\---

# 3\. Problemas encontrados

* **Consulta de múltiples tableros:** inicialmente, el método `findByEquipoId` del repositorio `ITablero` devolvía un solo tablero. Se identificó que un equipo puede tener varios tableros, por lo que se modificó el retorno a `List<Tablero>`.
* **Solicitudes OPTIONS y control de sesiones:** durante las pruebas de CORS, la solicitud OPTIONS desde el origen permitido recibió HTTP 401 con el mensaje "No hay una sesión activa". Se identificó que el interceptor de sesión también está interceptando las solicitudes OPTIONS. Es necesario revisar este comportamiento para permitir el preflight sin eliminar la protección de las demás rutas.
* **Origen no autorizado:** al realizar la solicitud desde `http://localhost:9999`, el servidor respondió HTTP 403 con el mensaje "Invalid CORS request", lo que permitió comprobar que no se acepta ese origen.
* **Pruebas de autenticación:** para comprobar el inicio de sesión se necesitó utilizar una contraseña que coincidiera con el hash BCrypt almacenado. El hash no permite recuperar la contraseña original, por lo que se requiere utilizar credenciales de prueba conocidas.

\---

# 4\. Avance funcional

**Operativo**

* Registro de usuarios mediante `POST /crearUsuario`.
* Inicio de sesión mediante `POST /iniciarSesion`.
* Cierre de sesión mediante `POST /cerrarSesion`.
* Consulta del usuario autenticado mediante `GET /obtenerUsuarioActual`.
* Creación y consulta de equipos.
* Consulta de integrantes de un equipo.
* Incorporación de integrantes y administración de sus roles.
* Eliminación de integrantes y archivado de equipos.
* Consulta de tableros asociados a un equipo mediante `GET /obtenerTableros/{idEquipo}`.
* Protección de las rutas mediante el interceptor de sesión.
* Configuración de CORS para el origen autorizado `http://localhost:5500`.

**Pruebas realizadas**

* Registro de usuario: respuesta 201 con datos válidos y 400 ante campos obligatorios vacíos o correo duplicado.
* Inicio de sesión: respuesta 401 ante contraseña incorrecta.
* Cierre de sesión: respuesta 200 con sesión activa y 401 sin sesión.
* Consulta del usuario autenticado: pruebas anteriores con sesión y sin sesión.
* Operaciones de equipos y miembros: pruebas de creación, consulta, incorporación y validaciones de permisos.
* CORS: origen permitido con respuesta 401 en OPTIONS y origen no autorizado con respuesta 403.

**Completado en diseño, aún sin implementar**

* El conjunto completo de endpoints del módulo B para la gestión de tableros, tareas, asignaciones y revisiones.
* El módulo C de sesiones Pomodoro y reporte de cumplimiento.

**Pendiente**

* Implementar las operaciones restantes del módulo B, incluyendo la creación de tableros y tareas, sus modificaciones y transiciones de estado.
* Implementar el registro y consulta de sesiones Pomodoro.
* Implementar el reporte de cumplimiento individual y por equipo con Thymeleaf.
* Corregir el tratamiento de las solicitudes OPTIONS en el interceptor.
* Verificar y documentar la conexión con MySQL mediante las variables de entorno.
* Completar las pruebas CURL pendientes de los tres módulos.
* Desarrollar el frontend.
* Completar `README.md` y `database/esquema.sql`.
* Ampliar las pruebas automatizadas con JUnit y MockMvc para cada módulo.

\---

# 5\. Uso de IA generativa

* **Herramientas utilizadas:** ChatGPT y Claude.
* **Actividad para la que se utilizó:** revisión de código, identificación de inconsistencias entre la implementación y el diseño, y generación de comandos CURL para probar los endpoints de los módulos de usuarios, equipos y CORS.
* **Código o contenido generado:** comandos CURL, explicaciones de resultados HTTP, recomendaciones de modificación del repositorio `ITablero` e implementación del endpoint para consultar tableros de un equipo.
* **Modificaciones realizadas por el equipo:** adaptación de los comandos CURL al puerto 8081 y a las credenciales del entorno local; aplicación del cambio en el tipo de retorno de `ITablero`; revisión y adaptación de la implementación propuesta.
* **Forma en que se verificó el resultado:** ejecución de los comandos CURL contra el backend local, revisión de los códigos HTTP y encabezados de respuesta, y comprobación de los cambios mediante commits.

El uso de IA se documentó individualmente en los archivos `r-ia-doc` y `r-ia-dev` de cada integrante.

\---

# 6\. Participación individual

* **Sara Ortega Hernández:** implementación de las entidades y repositorios de usuarios y equipos, registro e inicio de sesión, cierre de sesión, interceptor de autenticación y operaciones de gestión de equipos y miembros. Realización y documentación de las primeras pruebas CURL del módulo de usuarios, incluyendo los endpoints A1, A2 y A3.
* **María de Lourdes Méndez Prigadaá:** implementación de las entidades y repositorios de tableros y tareas. Revisión y corrección del repositorio `ITablero` para permitir consultar múltiples tableros por equipo. Implementación del endpoint B2 para obtener los tableros de un equipo y documentación del uso de IA correspondiente.
* **Aradan Schwartz Rocha:** configuración de CORS para permitir el acceso desde el frontend y realización de pruebas CURL para verificar el comportamiento ante orígenes permitidos y no autorizados. Identificación del problema de autenticación en las solicitudes OPTIONS y documentación de las pruebas realizadas.

\---

# 7\. Próximas actividades

* Resolver el bloqueo de las solicitudes OPTIONS en el interceptor y repetir las pruebas de CORS.
* Verificar y documentar la conexión de Spring Boot con MySQL desde WSL.
* Continuar la implementación del módulo B: creación y administración de tableros y tareas, asignaciones y revisiones.
* Implementar el módulo C: sesiones Pomodoro y reporte de cumplimiento.
* Completar las pruebas CURL pendientes y desarrollar las pruebas automatizadas con JUnit y MockMvc.
* Desarrollar las pantallas del frontend e integrar sus solicitudes con el backend.
* Completar el README, el esquema de la base de datos y la documentación necesaria para reproducir el proyecto.

\---

## Observaciones adicionales

* Los documentos de diseño continúan siendo la referencia para la implementación y se actualizan conforme se toman decisiones durante el desarrollo.
* Las pruebas realizadas hasta este reporte corresponden a una primera validación de los módulos. No representan la cobertura completa de la API.
* La implementación y las pruebas restantes continuarán en la siguiente etapa del proyecto.

