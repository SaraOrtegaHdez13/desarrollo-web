# Distribución de responsabilidades de LuzioTeamTime

**Equipo:** Sara Ortega Hernández, María de Lourdes, Aradan
**Entregable:** E1 - Diseño inicial
**Versión del documento:** 1.1

\---

## 1\. Resumen del reparto

|Integrante|Módulo|Rama|Reportes que redacta|Carpeta de IA|
|-|-|-|-|-|
|Sara Ortega Hernández|A: usuarios, equipos y roles|`modulo-a`|R1 y R4|`documentacion/IAG/Sara/`|
|María de Lourdes|B: tableros y tareas|`modulo-b`|R2|`documentacion/IAG/Lourdes/`|
|Aradan|C: infraestructura, pomodoro y reporte|`modulo-c`|R3|`documentacion/IAG/Aradan/`|



## 2\. Módulo A: usuarios, equipos y roles (Sara)

|Rubro|Detalle|
|-|-|
|Requerimientos|RF-01 a RF-11|
|Reglas de negocio|RN-01, RN-02, RN-05, RN-14 (parte de miembros), RN-17 (archivar el equipo)|
|Tablas y entidades|`usuarios` (Usuario), `equipos` (Equipo), `miembros\_equipo` (MiembroEquipo)|
|Backend|Registro, inicio y cierre de sesión con BCrypt; `SesionInterceptor` (401 sin sesión); crear equipo (el creador queda como primer LIDER, en una sola transacción); listar equipos activos y archivados; miembros y roles; agregar miembros por correo (404 si no está registrado, 400 si ya es miembro); máximo dos LIDER, y el único LIDER no se quita ni se degrada; no quitar a un miembro con tareas sin terminar; archivar equipos|
|Esqueleto inicial|Creó el esqueleto del proyecto (definición en la sección 4) y lo dejó en `main`; las tres ramas parten de él|
|Servicios que ofrece a los demás módulos|Comprobar que el usuario de la sesión es miembro de un equipo y qué rol tiene (RN-03); saber si un equipo está archivado (RN-17); saber cuántos LIDER tiene (RN-06)|
|Pantallas|`index` (login), `registro`, `equipos`, `equipo`|
|Pruebas|Registro, login y logout; interceptor; equipos, miembros y reglas de roles (máximo dos LIDER, único LIDER, quitar con tareas pendientes, equipo archivado con 403). JUnit y MockMvc, casos de éxito y de error|
|Documentos|Parte de `requerimientos.md` y de `base-de-datos.md` correspondiente al módulo A; `r-act-1.md` y `r-act-4.md`|

## 3\. Módulo B: tableros y tareas (María de Lourdes)

|Rubro|Detalle|
|-|-|
|Requerimientos|RF-12 a RF-22|
|Reglas de negocio|RN-04, RN-06, RN-07, RN-08, RN-09, RN-11, RN-15, RN-19|
|Tablas y entidades|`tableros` (Tablero), `tareas` (Tarea), `revisiones\_tarea` (RevisionTarea), `asignaciones\_tarea` (AsignacionTarea)|
|Backend|Primero, entidades y repositorios de sus 4 tablas (los demás módulos dependen de ellos); tableros; tareas con filtros por estado y responsable; cambios de estado; asignar a un LIDER solo si el equipo tiene dos; reasignación (no EN\_REVISION ni HECHA); eliminar solo tareas en POR\_HACER sin avance; entrega a revisión; aprobación y rechazo del líder distinto del responsable; historial de entregas y asignaciones. Los métodos que guardan en varias tablas usan `@Transactional` (crear tarea, reasignar, entregar, aprobar o rechazar)|
|Servicios que ofrece a los demás módulos|Saber si un usuario tiene tareas sin terminar en un equipo (RN-14, lo usa Sara); saber si una tarea es del usuario y está EN\_PROGRESO (RN-10, lo usa Aradan)|
|Pantallas|`tablero` (botones para mover tareas) y `tarea`|
|Pruebas|Transiciones de estado permitidas y rechazadas; permisos por rol y por responsable; aprobación y rechazo; reasignación (incluida la rechazada en EN\_REVISION y HECHA); asignar a un LIDER con un solo LIDER en el equipo; eliminar con y sin avance; equipo archivado con 403|
|Documentos|Parte de `requerimientos.md` y de `base-de-datos.md` correspondiente al módulo B; `r-act-2.md`|

## 4\. Módulo C: infraestructura, pomodoro y reporte (Aradan)

|Rubro|Detalle|
|-|-|
|Requerimientos|RF-23 a RF-28, y RNF-02, RNF-03, RNF-07, RNF-10 y RNF-11|
|Reglas de negocio|RN-10, RN-12, RN-18 y las reglas del reporte de cumplimiento|
|Tablas y entidades|`sesiones` (SesionPomodoro)|
|Backend|Mantenimiento del esqueleto Spring Boot y de la conexión a MySQL; CORS; sesiones de pomodoro (solo en tarea propia EN\_PROGRESO, duración mayor que 0, equipo no archivado); reporte de cumplimiento con plantilla Thymeleaf, por integrante y por equipo, con tablero opcional, historial y "ex integrantes"|
|Frontend|`js/pomodoro.js` y el bloque del temporizador que se integra en la pantalla `tarea`; plantilla `cumplimiento`|
|Pruebas|Registro de sesiones solo en tareas propias EN\_PROGRESO y con duración válida; cálculo del reporte (individual, resumen del equipo, ex integrantes, reasignaciones); CORS|
|Documentos|`database/esquema.sql` (con `mysqldump --no-data`), `README.md`, prueba de clonado en un WSL limpio; parte de `requerimientos.md` y de `base-de-datos.md` correspondiente al módulo C; `r-act-3.md`|

## 5\. Requerimientos no funcionales compartidos

|Requerimiento|Quién|
|-|-|
|RNF-01 (3 capas, paquete `mx.uv.luzioteamtime`)|Sara dejó el esqueleto con los subpaquetes y Aradan lo mantiene; cada quien lo respeta en su módulo|
|RNF-04 (BCrypt con `spring-security-crypto`)|Sara|
|RNF-04 (credenciales de la base en variables de entorno)|Aradan|
|RNF-05 (HttpSession e interceptor)|Sara|
|RNF-06 (`ResponseEntity` y códigos 200, 201, 400, 401, 403, 404)|Los tres, en sus controladores|
|RNF-08 y RNF-09 (frontend sin librerías, `credentials: 'include'`, errores visibles)|Cada quien en sus pantallas|
|RNF-12 (pruebas con JUnit y MockMvc)|Cada quien en su módulo|
|RNF-13 (`@Transactional` en operaciones de varias tablas)|Sara (crear equipo) y María de Lourdes (tareas y revisiones)|

## 6\. Dependencias entre módulos

Algunas reglas de un módulo necesitan datos de otro. Para no bloquearse, cada módulo ofrece un servicio que los demás consultan (los nombres de los métodos se definen al codificar).

|Quién necesita|A quién se lo pide|Para qué|
|-|-|-|
|María y Aradan|Sara|Verificar membresía y rol del usuario (RN-03, RN-16), equipo archivado (RN-17) y número de LIDER (RN-06)|
|Sara|María de Lourdes|Saber si un miembro tiene tareas sin terminar antes de quitarlo (RN-14)|
|Aradan|María de Lourdes|Saber si la tarea es del usuario y está EN\_PROGRESO (RN-10); leer tareas, asignaciones y revisiones para el reporte|



## 7\. Calendario por hito

|Hito|Fechas|Entregable|Reporte (responsable)|
|-|-|-|-|
|1. Análisis y diseño|7 y 8 de noviembre|E1 Diseño inicial|R1 (Sara)|
|2. Backend básico|9 al 11 de noviembre|E2 API funcional inicial|R2 (María de Lourdes)|
|3. Backend completo|12 al 14 de noviembre|E3 API REST completa|R3 (Aradan)|
|4. Frontend|15 al 17 de noviembre|E4 Sistema integrado||
|5. Pruebas y documentación|18 al 19 de noviembre|E5 Versión candidata|R4 (Sara)|



