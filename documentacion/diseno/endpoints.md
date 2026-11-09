# Endpoints de LuzioTeamTime

**Equipo:** Sara Ortega Hernández, María de Lourdes, Aradan
**Entregable:** E1 - Diseño inicial
**Versión:** 1.0 

\---

## 1\. Módulo A: usuarios, equipos y roles (Sara)

Controladores `@RestController`.

|#|Método y ruta|Req.|Quién puede|Entrada|Respuestas|
|-|-|-|-|-|-|
|A1|POST `/crearUsuario`|RF-01, RF-02|Sin sesión|Cuerpo: `nombre`, `correo`, `password`|201 usuario creado; 400 campo vacío o correo ya registrado|
|A2|POST `/iniciarSesion`|RF-03|Sin sesión|Cuerpo: `correo`, `password`|200 usuario (`id`, `nombre`, `correo`) y se crea la sesión; 401 correo o contraseña incorrectos|
|A3|POST `/cerrarSesion`|RF-04|Con sesión|Sin cuerpo|200; 401 sin sesión|
|A4|GET `/obtenerUsuarioActual`|Apoyo del frontend|Con sesión|Sin parámetros|200 usuario de la sesión; 401. Sirve para saber quién es "yo" (por ejemplo, para mostrar solo mis botones)|
|A5|POST `/crearEquipo`|RF-06|Con sesión|Cuerpo: `nombre`, `descripcion` (opcional)|201 equipo; el creador queda como LIDER; 400 nombre vacío|
|A6|GET `/obtenerEquipos`|RF-07|Con sesión|Opcional `?archivado=true` o `false`|200 lista de mis equipos con mi `rol` y `archivado`|
|A7|GET `/obtenerEquipo/{id}`|RF-08|Miembro|Ruta: id del equipo|200 equipo con mi `rol`; 403 no soy miembro; 404 no existe|
|A8|GET `/obtenerMiembros/{idEquipo}`|RF-08|Miembro|Ruta: id del equipo|200 lista de miembros con `rol`; 403; 404|
|A9|POST `/agregarMiembro/{idEquipo}`|RF-09|LIDER|Cuerpo: `correo`, `rol`|201 miembro; 404 el correo no está registrado; 400 ya es miembro, rol inválido o ya hay dos LIDER; 403 no soy LIDER o equipo archivado|
|A10|PUT `/modificarMiembro/{idMiembro}`|RF-10|LIDER|Ruta: id de la membresía. Cuerpo: `rol`|200 miembro; 400 rompe RN-05 (tercer LIDER o degradar a un LIDER); 403; 404|
|A11|DELETE `/eliminarMiembro/{idMiembro}`|RF-10|LIDER|Ruta: id de la membresía|200; 400 es LIDER (RN-05) o tiene tareas sin terminar (RN-14); 403; 404|
|A12|PUT `/archivarEquipo/{id}`|RF-11|LIDER|Ruta: id del equipo|200 equipo con `archivado: true`; 403; 404|

`idMiembro` es el `id` de la tabla `miembros\_equipo`; el frontend lo obtiene con A8.

## 2\. Módulo B: tableros y tareas (María de Lourdes)

Controladores `@RestController`.

|#|Método y ruta|Req.|Quién puede|Entrada|Respuestas|
|-|-|-|-|-|-|
|B1|POST `/crearTablero/{idEquipo}`|RF-12|LIDER|Cuerpo: `nombre`|201 tablero; 400 nombre vacío; 403 no soy LIDER o equipo archivado; 404|
|B2|GET `/obtenerTableros/{idEquipo}`|RF-13|Miembro|Ruta: id del equipo|200 lista de tableros; 403; 404|
|B3|GET `/obtenerTablero/{id}`|RF-13|Miembro|Ruta: id del tablero|200 tablero; 403; 404|
|B4|POST `/crearTarea/{idTablero}`|RF-14|LIDER|Cuerpo: `titulo`, `descripcion`, `fechaLimite`, `idResponsable`|201 tarea en POR\_HACER con su asignación inicial; 400 campo vacío, responsable que no es miembro (RN-04) o LIDER con un solo LIDER en el equipo (RN-06); 403; 404|
|B5|GET `/obtenerTareas/{idTablero}`|RF-15|Miembro|Opcionales `?estado=` y `?responsable=` (id del usuario responsable)|200 lista de tareas; 400 estado inválido; 403; 404|
|B6|GET `/obtenerTarea/{id}`|RF-16|Miembro|Ruta: id de la tarea|200 tarea con `revisiones` y `asignaciones`; 403; 404|
|B7|PUT `/modificarTarea/{id}`|RF-17|LIDER|Cuerpo: `titulo`, `descripcion`, `fechaLimite`|200 tarea; 400; 403 (incluye equipo archivado); 404|
|B8|DELETE `/eliminarTarea/{id}`|RF-17, RN-19|LIDER|Ruta: id de la tarea|200; 400 no está en POR\_HACER o tiene avance; 403; 404|
|B9|PUT `/reasignarTarea/{id}`|RF-18|LIDER|Cuerpo: `idResponsable`|200 tarea con su nuevo responsable; 400 la tarea está EN\_REVISION o HECHA, el nuevo responsable no es miembro, o es LIDER con un solo LIDER; 403; 404|
|B10|PUT `/iniciarTarea/{id}`|RF-19|Responsable|Ruta: id de la tarea|200 tarea en EN\_PROGRESO; 400 la tarea no está en POR\_HACER; 403 no es mi tarea; 404|
|B11|PUT `/enviarTareaRevision/{id}`|RF-20|Responsable|Ruta: id de la tarea|200 tarea en EN\_REVISION con `fechaCompletada` y revisión PENDIENTE; 400 la tarea no está EN\_PROGRESO; 403; 404|
|B12|PUT `/aprobarTarea/{id}`|RF-21|LIDER distinto del responsable|Ruta: id de la tarea|200 tarea HECHA y revisión APROBADA; 400 la tarea no está EN\_REVISION; 403 no soy LIDER o es mi propia tarea; 404|
|B13|PUT `/rechazarTarea/{id}`|RF-22|LIDER distinto del responsable|Cuerpo: `observacion` (obligatoria)|200 tarea en EN\_PROGRESO y revisión RECHAZADA; 400 observación vacía o la tarea no está EN\_REVISION; 403; 404|

Los cambios de estado son endpoints separados (B10 a B13), y no un solo `modificarEstadoTarea`, porque cada transición tiene permisos y efectos distintos (RN-07, RN-08, RN-11). Así cada regla se prueba por separado.

## 3\. Módulo C: pomodoro y reporte (Aradan)

|#|Método y ruta|Req.|Quién puede|Entrada|Respuestas|
|-|-|-|-|-|-|
|C1|POST `/crearSesion/{idTarea}`|RF-24, RF-25|Responsable de la tarea|Cuerpo: `inicio`, `fin`, `duracionSegundos`|201 sesión; 400 duración menor o igual a 0 (RN-10) o la tarea no está EN\_PROGRESO; 403 no es mi tarea o equipo archivado; 404|
|C2|GET `/obtenerSesiones/{idTarea}`|Opcional (tiempo acumulado en la pantalla `tarea`)|Miembro|Ruta: id de la tarea|200 lista de sesiones con su autor; 403; 404|
|C3|GET `/equipos/{id}/cumplimiento`|RF-26 a RF-28|Miembro|Ruta: id del equipo. Opcional `?tablero=` (id de un tablero del equipo)|200 página HTML con el reporte; 403; 404|

C3 es la única ruta con plantilla Thymeleaf: la clase es `@Controller`, recibe `Model modelo` y devuelve el nombre de la plantilla `cumplimiento`. La ruta no sigue el estilo "verbo + entidad" porque RF-26 fija esa dirección. La pantalla `equipo` la abre con un enlace. La cookie de sesión viaja sola porque el navegador abre la página directamente en `localhost`. Sin `?tablero=` el reporte cubre todos los tableros del equipo. Cómo se muestran los errores 403 y 404 en esta página (código HTTP o mensaje en la plantilla) se decide al codificar y se definirá en próximas entregas. 

## 4\. Contratos de datos (JSON)

Los nombres de las llaves son los que deben usar backend y frontend. Los campos que no se listan no se envían.

|Objeto|Campos|
|-|-|
|Usuario (respuesta)|`id`, `nombre`, `correo`|
|Equipo (respuesta)|`id`, `nombre`, `descripcion`, `archivado`, `rol` (el mío, solo en A6 y A7)|
|Miembro (respuesta)|`id` (de la membresía), `idUsuario`, `nombre`, `correo`, `rol`|
|Tablero (respuesta)|`id`, `idEquipo`, `nombre`|
|Tarea (respuesta)|`id`, `idTablero`, `idResponsable`, `nombreResponsable`, `titulo`, `descripcion`, `estado`, `fechaLimite`, `observacion`, `fechaCompletada`|
|Tarea detalle (B6)|Los de Tarea, más `revisiones` y `asignaciones`|
|Revisión (dentro de `revisiones`)|`numeroEntrega`, `idEntregadoPor`, `fechaEntrega`, `idRevisor`, `fechaRevision`, `resultado`, `observacion`|
|Asignación (dentro de `asignaciones`)|`idUsuario`, `nombre`, `fechaInicio`, `fechaFin`, `estadoAlRecibir`, `estadoAlSalir`|
|Sesión (entrada y respuesta)|`inicio`, `fin`, `duracionSegundos`; en la respuesta también `id`, `idUsuario`, `nombre`|

Ejemplos de cuerpo:

```json
{ "nombre": "Ana", "correo": "ana@correo.com", "password": "miClave123" }
```

```json
{ "titulo": "Diseñar la BD", "descripcion": "Tablas y relaciones", "fechaLimite": "2026-11-20", "idResponsable": 4 }
```

```json
{ "inicio": "2026-11-12T10:00:00", "fin": "2026-11-12T10:25:00", "duracionSegundos": 1500 }
```

```json
{ "mensaje": "Solo un LIDER distinto del responsable puede aprobar esta tarea" }
```

## 5\. Qué endpoints usa cada pantalla

|Pantalla|Responsable|Endpoints|
|-|-|-|
|`index` (login)|Sara|A2|
|`registro`|Sara|A1|
|`equipos`|Sara|A6, A5, A3|
|`equipo`|Sara|A7, A8, A9, A10, A11, A12; B2 y B1 (lista y crea tableros); enlace a C3|
|`tablero`|María de Lourdes|B3, B5, B4, B10, B11, B12, B13, B9; A4 para saber cuáles son mis tareas|
|`tarea`|María de Lourdes y Aradan|B6, B7, B8, B10, B11, B12, B13, B9; C1 y C2 para el temporizador|
|`cumplimiento` (plantilla)|Aradan|C3|

Todas las pantallas llaman A4 al cargar para saber quién es el usuario de la sesión, y mandan a `index` si responde 401.

## 6\. Pruebas con CURL

Con CURL la cookie se guarda con `-c` y se reutiliza con `-b`. Este flujo sirve como evidencia de la prueba de funcionamiento en `r-ia-dev`.

```bash
# 1. Registrar un usuario (A1)
curl -i -X POST http://localhost:8081/crearUsuario \\
  -H "Content-Type: application/json" \\
  -d '{"nombre":"Ana","correo":"ana@correo.com","password":"miClave123"}'

# 2. Iniciar sesión y guardar la cookie (A2)
curl -i -c cookies.txt -X POST http://localhost:8081/iniciarSesion \\
  -H "Content-Type: application/json" \\
  -d '{"correo":"ana@correo.com","password":"miClave123"}'

# 3. Crear un equipo usando la cookie (A5)
curl -i -b cookies.txt -X POST http://localhost:8081/crearEquipo \\
  -H "Content-Type: application/json" \\
  -d '{"nombre":"Equipo 1","descripcion":"Proyecto final"}'

# 4. Consultar mis equipos (A6)
curl -i -b cookies.txt http://localhost:8081/obtenerEquipos

# 5. Sin cookie debe responder 401
curl -i http://localhost:8081/obtenerEquipos
```

## 7\. Registro de cambios

|Fecha|Cambio|Quién|
|-|-|-|
|8 nov 2026|Versión inicial|Equipo|



