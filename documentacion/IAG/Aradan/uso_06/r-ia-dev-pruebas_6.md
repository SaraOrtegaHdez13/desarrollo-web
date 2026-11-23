# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 22/11/2026
**Número de reporte:** 6

\---

# 1\. Breve descripción de uso

Se utilizó ChatGPT para planear y organizar 12 pruebas CURL del módulo de Pomodoro y del reporte de cumplimiento. Las pruebas se ejecutaron desde la terminal en WSL para comprobar el registro y consulta de sesiones, las validaciones de tareas y los permisos de acceso. Se documentaron las respuestas HTTP reales obtenidas durante la ejecución.



\---

# 2\. Evidencias de uso

|Evidencia|Formato|
|-|-|
|Herramienta|ChatGPT|
|Modelo/temperatura|GPT-6. La temperatura no se configura ni se muestra en esta conversación.|
|Prompt original|[PROMT 1](#promt-1)|
|Respuesta de IA|[RESPUESTA A PROMT 1](#respuesta-promt-1)|
|Código anterior|No aplica. En esta actividad no se modificó el código fuente; se diseñaron y ejecutaron pruebas de funcionamiento.|
|Código posterior|No aplica. Se conservaron los archivos del proyecto y se utilizaron comandos CURL desde la terminal.|
|Diferencias entre versiones|No aplica. El objetivo fue comprobar el comportamiento del módulo mediante pruebas, no generar una nueva versión del código.|
|Prueba de funcionamiento|[Prueba 1](#prueba-1-intentar-registrar-un-pomodoro-sin-iniciar-sesión) · [Respuesta 1](#resultado-obtenido-prueba-1)<br>[Prueba 2](#prueba-2-consultar-sesiones-pomodoro-sin-iniciar-sesión) · [Respuesta 2](#resultado-obtenido-prueba-2)<br>[Prueba 3](#prueba-3-impedir-registrar-sesiones-pomodoro-en-tareas-ajenas) · [Respuesta 3](#resultado-obtenido-prueba-3)<br>[Prueba 4](#prueba-4-rechazar-una-sesión-pomodoro-con-duracion-valida) · [Respuesta 4](#resultado-obtenido-prueba-4)<br>[Prueba 5](#prueba-5-rechazar-sesiones-en-tareas-que-no-estan-en-progreso) · [Respuesta 5](#resultado-obtenido-prueba-5)<br>[Prueba 6](#prueba-6-rechazar-el-registro-de-sesiones-en-tareas-inexistentes) · [Respuesta 6](#resultado-obtenido-prueba-6)<br>[Prueba 7](#prueba-7-consultar-sesiones-pomodoro-de-una-tarea-existente) · [Respuesta 7](#resultado-obtenido-prueba-7)<br>[Prueba 8](#prueba-8-consultar-sesiones-pomodoro-de-una-tarea-ineexistente) · [Respuesta 8](#resultado-obtenido-prueba-8)<br>[Prueba 9](#prueba-9-consultar-sesiones-pomodoro-de-una-tarea-ajena) · [Respuesta 9](#resultado-obtenido-prueba-9)<br>[Prueba 10](#prueba-10-consultar-el-reporte-de-cumplimiento-del-equipo) · [Respuesta 10](#resultado-obtenido-prueba-10)<br>[Prueba 11](#prueba-11-consultar-el-reporte-de-cumplimiento-sin-iniciar-sesión) · [Respuesta 11](#resultado-obtenido-prueba-11)<br>[Prueba 12](#prueba-12-impedir-que-un-usuario-ajeno-al-equipo-consulte-su-reporte) · [Respuesta 12](#resultado-obtenido-prueba-12)|
|Código de uso|Pruebas|
|Reflexión del estudiante|La IA me ayudó a organizar las pruebas y anticipar los códigos HTTP esperados. Al ejecutar cada comando en WSL y revisar las respuestas reales, comprobé las validaciones del módulo, el control de sesiones y los permisos de acceso.|



##### PROMT 1

Estoy trabajando en el módulo de Pomodoro y el reporte de cumplimiento de mi proyecto LuzioTeamTime. Necesito realizar las pruebas de funcionamiento utilizando CURL desde WSL.



Ayúdame a preparar las pruebas paso a paso. Quiero comprobar tanto los casos en los que las peticiones deben funcionar como aquellos en los que el sistema debe rechazarlas.



Para el registro de sesiones Pomodoro, toma en cuenta que el usuario debe haber iniciado sesión, debe ser el responsable de la tarea y esta debe encontrarse en estado EN\_PROGRESO. También necesito comprobar que la duración registrada coincida con el intervalo entre la hora de inicio y la hora de finalización, y que la tarea exista.



Para la consulta de sesiones, verifica el comportamiento cuando la tarea existe, cuando no existe y cuando quien consulta es otro integrante del mismo equipo.



Finalmente, necesito probar el reporte de cumplimiento: que pueda consultarlo un integrante del equipo, que se rechace el acceso cuando no hay una sesión iniciada y que se impida el acceso a un usuario que no pertenece al equipo.



En cada prueba explícame qué se quiere comprobar, dame el comando CURL completo y dime qué código HTTP debería recibir. Vamos a ejecutar los comandos uno por uno. Espera mi respuesta con el resultado antes de continuar con la siguiente prueba, para que los comandos utilicen los identificadores y las sesiones que realmente existan en el proyecto.



##### RESPUESTA PROMT 1



Para comprobar el funcionamiento del módulo de Pomodoro y del reporte de cumplimiento de LuzioTeamTime, vamos a realizar las pruebas mediante CURL desde WSL.



Organizaremos las pruebas en dos partes:



1\. Pruebas del módulo de Pomodoro



Vamos a verificar que:

\- No se puedan registrar sesiones sin iniciar sesión.

\- Un usuario solamente pueda registrar Pomodoros en tareas que tenga asignadas.

\- La tarea se encuentre en estado EN\_PROGRESO antes de registrar una sesión.

\- La duración coincida con el intervalo entre la hora de inicio y la hora de finalización.

\- No se puedan registrar sesiones en tareas inexistentes.

\- Los usuarios puedan consultar las sesiones de tareas existentes y que se respeten los permisos correspondientes.

2\. Pruebas del reporte de cumplimiento



Vamos a comprobar que:

\- Un integrante del equipo pueda consultar el reporte.

\- Un usuario sin sesión activa no pueda acceder.

\- Un usuario que no pertenece al equipo no pueda consultar su reporte.

Para cada prueba te proporcionaré:

1\. Objetivo: qué comportamiento queremos comprobar.

2\. Comando CURL: la petición que debes ejecutar en la terminal.

3\. Resultado esperado: el código HTTP y el comportamiento que debería devolver el servidor.

Importante: realizaremos las pruebas una por una. Primero ejecuta el comando que te proporcione y compárteme la respuesta completa del servidor. Con esos resultados determinaremos si la prueba fue exitosa y prepararemos la siguiente, utilizando los identificadores y datos reales de tu proyecto.



No vamos a asumir que una prueba funcionó solamente porque el resultado esperado sea correcto: lo comprobaremos con las respuestas reales del servidor.



\---



##### PRUEBA 1. INTENTAR REGISTRAR UN POMODORO SIN INICIAR SESIÓN



curl -i -X POST http://localhost:8081/crearSesion/3 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"inicio":"2026-11-22T10:00:00","fin":"2026-11-22T10:25:00","duracionSegundos":1500}'



##### PRUEBA 2. CONSULTAR SESIONES POMODORO SIN INICIAR SESIÓN



curl -i -X GET http://localhost:8081/obtenerSesiones/3



##### PRUEBA 3. IMPEDIR REGISTRAR SESIONES POMODORO EN TAREAS AJENAS



curl -i -b cookies-aradan.txt \\

&#x20; -X POST http://localhost:8081/crearSesion/103 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"inicio":"2026-11-22T12:00:00","fin":"2026-11-22T12:25:00","duracionSegundos":1500}'



##### PRUEBA 4. RECHAZAR UNA SESIÓN POMODORO CON DURACIÓN VÁLIDA



curl -i -b cookies-aradan.txt \\

&#x20; -X POST http://localhost:8081/crearSesion/104 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"inicio":"2026-11-22T13:00:00","fin":"2026-11-22T13:00:00","duracionSegundos":0}'



##### PRUEBA 5. RECHAZAR SESIONES EN TAREAS QUE NO ESTÁN EN PROGRESO



curl -i -b cookies-aradan.txt \\

&#x20; -X POST http://localhost:8081/crearSesion/105 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"inicio":"2026-11-22T14:00:00","fin":"2026-11-22T14:25:00","duracionSegundos":1500}'



##### PRUEBA 6. RECHAZAR EL REGISTRO DE SESIONES EN TAREAS INEXISTENTES



curl -i -b cookies-aradan.txt \\

&#x20; -X POST http://localhost:8081/crearSesion/999999 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"inicio":"2026-11-22T15:00:00","fin":"2026-11-22T15:25:00","duracionSegundos":1500}'



##### PRUEBA 7. CONSULTAR SESIONES POMODORO DE UNA TAREA EXISTENTE



curl -i -b cookies-aradan.txt \\

&#x20; -X GET http://localhost:8081/obtenerSesiones/104



##### PRUEBA 8. CONSULTAR SESIONES POMODORO DE UNA TAREA INEEXISTENTE



curl -i -b cookies-aradan.txt \\

&#x20; -X GET http://localhost:8081/obtenerSesiones/999999



##### PRUEBA 9. CONSULTAR SESIONES POMODORO DE UNA TAREA AJENA



curl -i -b cookies-lourdes.txt \\

&#x20; -X GET http://localhost:8081/obtenerSesiones/104



##### PRUEBA 10. CONSULTAR EL REPORTE DE CUMPLIMIENTO DEL EQUIPO



curl -i -b cookies-lourdes.txt \\

&#x20; "http://localhost:8081/equipos/52/cumplimiento?tablero=52"



##### PRUEBA 11. CONSULTAR EL REPORTE DE CUMPLIMIENTO SIN INICIAR SESIÓN



curl -i -X GET \\

&#x20; "http://localhost:8081/equipos/52/cumplimiento?tablero=52"



##### PRUEBA 12. IMPEDIR QUE UN USUARIO AJENO AL EQUIPO CONSULTE SU REPORTE





curl -i -b cookies-externo.txt \\

&#x20; -X GET "http://localhost:8081/equipos/52/cumplimiento?tablero=52"



\---

##### RESULTADO OBTENIDO PRUEBA 1



HTTP/1.1 401

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json;charset=UTF-8

Content-Length: 39

Date: Mon, 23 Nov 2026 03:43:47 GMT



{"mensaje":"No hay una sesión activa"}



##### RESULTADO OBTENIDO PRUEBA 2



GET http://localhost:8081/obtenerSesiones/3

HTTP/1.1 401

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json;charset=UTF-8

Content-Length: 39

Date: Mon, 23 Nov 2026 03:45:21 GMT



{"mensaje":"No hay una sesión activa"}



##### RESULTADO OBTENIDO PRUEBA 3



HTTP/1.1 403

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 03:53:47 GMT



{"mensaje":"Solo el responsable puede guardar un pomodoro."}



##### RESULTADO OBTENIDO PRUEBA 4



HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 03:54:32 GMT

Connection: close



{"mensaje":"El inicio, el fin y la duración no coinciden."}



##### RESULTADO OBTENIDO PRUEBA 5



HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 03:56:49 GMT

Connection: close



{"mensaje":"La tarea debe estar en progreso."}



##### RESULTADO OBTENIDO PRUEBA 6



HTTP/1.1 404

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 04:01:42 GMT



{"mensaje":"La tarea no existe."}



##### RESULTADO OBTENIDO PRUEBA 7



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 04:02:20 GMT



\[{"duracionSegundos":1500,"inicio":"2026-11-22T10:00:00","fin":"2026-11-22T10:25:00","idTarea":104,"idUsuario":352,"id":2}]



##### RESULTADO OBTENIDO PRUEBA 8



&#x20; -X GET http://localhost:8081/obtenerSesiones/999999

HTTP/1.1 404

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 04:03:23 GMT



{"mensaje":"La tarea no existe."}



##### RESULTADO OBTENIDO PRUEBA 9



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 04:07:27 GMT



\[{"duracionSegundos":1500,"inicio":"2026-11-22T10:00:00","fin":"2026-11-22T10:25:00","idTarea":104,"idUsuario":352,"id":2}]



##### RESULTADO OBTENIDO PRUEBA 10



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/html;charset=UTF-8

Content-Language: en

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 04:08:49 GMT



<!DOCTYPE html>

<html lang="es-MX">

<head>

&#x20; <meta charset="UTF-8" />

&#x20; <meta name="viewport" content="width=device-width, initial-scale=1.0" />

&#x20; <title>Reporte de cumplimiento — LuzioTeamTime</title>

&#x20; <link rel="stylesheet" href="/css/estilos.css" />

</head>

<body>

&#x20; <div class="app-shell">

&#x20;   <!-- Barra lateral compacta UV Nocturno -->

&#x20;   <aside class="sidebar" aria-label="Navegación principal">

&#x20;     <div>

&#x20;       <a href="http://localhost:5500/equipos.html" class="brand-header">

&#x20;         <span class="brand-uv-mark" aria-hidden="true">

&#x20;           <span></span>

&#x20;           <span></span>

&#x20;         </span>

&#x20;         <span>LuzioTeamTime</span>

&#x20;       </a>

&#x20;       <div class="brand-subtag">

&#x20;         <svg class="icon-svg icon-svg-sm" viewBox="0 0 24 24" aria-hidden="true">

&#x20;           <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" />

&#x20;         </svg>

&#x20;         <span>Edición UV · Modo Nocturno</span>

&#x20;       </div>



&#x20;       <nav aria-label="Secciones de la aplicación">

&#x20;         <ul class="nav-list">

&#x20;           <li>

&#x20;             <a href="http://localhost:5500/equipos.html" class="nav-link">

&#x20;               <svg class="icon-svg icon-svg-sm" viewBox="0 0 24 24" aria-hidden="true">

&#x20;                 <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2" />

&#x20;                 <circle cx="9" cy="7" r="4" />

&#x20;                 <path d="M22 21v-2a4 4 0 0 0-3-3.87" />

&#x20;                 <path d="M16 3.13a4 4 0 0 1 0 7.75" />

&#x20;               </svg>

&#x20;               <span>Mis equipos</span>

&#x20;             </a>

&#x20;           </li>

&#x20;           <li>

&#x20;             <a href="http://localhost:5500/equipo.html?id=52" class="nav-link">

&#x20;               <svg class="icon-svg icon-svg-sm" viewBox="0 0 24 24" aria-hidden="true">

&#x20;                 <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" />

&#x20;               </svg>

&#x20;               <span>Detalle de equipo</span>

&#x20;             </a>

&#x20;           </li>

&#x20;           <li>

&#x20;             <a href="#" class="nav-link is-active" aria-current="page">

&#x20;               <svg class="icon-svg icon-svg-sm" viewBox="0 0 24 24" aria-hidden="true">

&#x20;                 <line x1="18" y1="20" x2="18" y2="10" />

&#x20;                 <line x1="12" y1="20" x2="12" y2="4" />

&#x20;                 <line x1="6" y1="20" x2="6" y2="14" />

&#x20;               </svg>

&#x20;               <span>Reporte de cumplimiento</span>

&#x20;             </a>

&#x20;           </li>

&#x20;         </ul>

&#x20;       </nav>

&#x20;     </div>



&#x20;     <div class="sidebar-footer">

&#x20;       <div class="sidebar-user-name">

&#x20;         <svg class="icon-svg icon-svg-sm" viewBox="0 0 24 24" aria-hidden="true">

&#x20;           <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />

&#x20;           <circle cx="12" cy="7" r="4" />

&#x20;         </svg>

&#x20;         <span>Usuario Prueba Sara</span>

&#x20;       </div>

&#x20;       <div class="sidebar-user-meta">Equipo Prueba Sara · Líder</div>

&#x20;       <a href="http://localhost:5500/equipo.html?id=52" class="btn btn-ghost btn-sm btn-block">

&#x20;         <svg class="icon-svg icon-svg-sm" viewBox="0 0 24 24" aria-hidden="true">

&#x20;           <line x1="19" y1="12" x2="5" y2="12" />

&#x20;           <polyline points="12 19 5 12 12 5" />

&#x20;         </svg>

&#x20;         <span>Volver al equipo</span>

&#x20;       </a>

&#x20;       <button id="cerrar-sesion-reporte" type="button" class="btn btn-secondary btn-sm btn-block">

&#x20;         <svg class="icon-svg icon-svg-sm" viewBox="0 0 24 24" aria-hidden="true">

&#x20;           <path d="M9 21H5V3h4 M16 7l5 5-5 5 M21 12H9" />

&#x20;         </svg>

&#x20;         <span>Cerrar sesión</span>

&#x20;       </button>

&#x20;       <p id="error-sesion-reporte" class="field-hint" role="status" hidden></p>

&#x20;     </div>

&#x20;   </aside>



&#x20;   <!-- Área principal -->

&#x20;   <main class="main-area">

&#x20;     <header class="page-header">

&#x20;       <div>

&#x20;         <h1 class="page-title">

&#x20;           <svg class="icon-svg icon-svg-lg" viewBox="0 0 24 24" aria-hidden="true">

&#x20;             <line x1="18" y1="20" x2="18" y2="10" />

&#x20;             <line x1="12" y1="20" x2="12" y2="4" />

&#x20;             <line x1="6" y1="20" x2="6" y2="14" />

&#x20;           </svg>

&#x20;           <span>Reporte de cumplimiento — <span>Equipo Prueba Sara</span></span>

&#x20;         </h1>

&#x20;         <p class="page-subtitle">Métricas consolidadas por integrante, puntualidad de entrega y minutos Pomodoro registrados.</p>

&#x20;       </div>



&#x20;       <!-- Filtro por tablero (Todos los tableros o específico) -->

&#x20;       <form method="get" action="/equipos/52/cumplimiento" class="filters-bar" style="align-items: flex-end;">

&#x20;         <div>

&#x20;           <label class="form-label" for="tableroId" style="display: flex; align-items: center; gap: 5px; font-size: 13px; margin-bottom: 4px;">

&#x20;             <svg class="icon-svg icon-svg-sm" viewBox="0 0 24 24" aria-hidden="true">

&#x20;               <polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3" />

&#x20;             </svg>

&#x20;             <span>Filtrar por tablero</span>

&#x20;           </label>

&#x20;           <select id="tableroId" name="tablero" class="form-control" onchange="this.form.submit()">

&#x20;             <option value="">Todos los tableros</option>

&#x20;             <option value="52"

&#x20;                     selected="selected">Tablero de pruebas CURL Lourdes</option>

&#x20;             <option value="53">Tablero no autorizado de Sofía</option>

&#x20;           </select>

&#x20;         </div>

&#x20;         <noscript>

&#x20;           <button type="submit" class="btn btn-primary btn-sm">Aplicar</button>

&#x20;         </noscript>

&#x20;       </form>

&#x20;     </header>



&#x20;     <div class="banner banner-info" style="font-size: 13.5px;">

&#x20;       <span style="display: flex; align-items: flex-start; gap: 8px;">

&#x20;         <svg class="icon-svg" viewBox="0 0 24 24" aria-hidden="true">

&#x20;           <circle cx="12" cy="12" r="10" />

&#x20;           <line x1="12" y1="16" x2="12" y2="12" />

&#x20;           <line x1="12" y1="8" x2="12.01" y2="8" />

&#x20;         </svg>

&#x20;         <span>

&#x20;           <strong>Nota metodológica (RN-12):</strong> El porcentaje de cumplimiento corresponde a <em>Tareas aprobadas / Tareas asignadas</em>.

&#x20;           La columna <em>Entregas a tiempo</em> se muestra como dato ilustrativo mientras se distingue en backend la fecha de entrega frente a la fecha de aprobación.

&#x20;           Los minutos Pomodoro se atribuyen a la persona que ejecutó cada sesión.

&#x20;         </span>

&#x20;       </span>

&#x20;     </div>



&#x20;     <!-- Tabla principal de métricas por integrante -->

&#x20;     <section class="card" aria-labelledby="titulo-tabla-reporte" style="margin-bottom: 24px;">

&#x20;       <div class="card-header">

&#x20;         <h2 id="titulo-tabla-reporte" class="card-title">Resumen por integrante</h2>

&#x20;       </div>







&#x20;       <div class="table-wrap">

&#x20;         <table class="data-table">

&#x20;           <thead>

&#x20;             <tr>

&#x20;               <th>Integrante</th>

&#x20;               <th class="num-col">Asignadas</th>

&#x20;               <th class="num-col">Aprobadas</th>

&#x20;               <th class="num-col">% Hechas</th>

&#x20;               <th class="num-col">Entregas a tiempo (ilustrativo)</th>

&#x20;               <th class="num-col">Minutos Pomodoro</th>

&#x20;             </tr>

&#x20;           </thead>

&#x20;           <tbody>

&#x20;             <tr>

&#x20;               <td>

&#x20;                 <strong>Usuario Prueba Sara</strong>

&#x20;                 <span class="field-hint">(Líder)</span>



&#x20;               </td>

&#x20;               <td class="num-col">1</td>

&#x20;               <td class="num-col">0</td>

&#x20;               <td class="num-col"><strong>0%</strong></td>

&#x20;               <td class="num-col">0</td>

&#x20;               <td class="num-col">0 min</td>

&#x20;             </tr>

&#x20;             <tr>

&#x20;               <td>

&#x20;                 <strong>Colaborador Prueba Sofía</strong>

&#x20;                 <span class="field-hint">(Líder)</span>



&#x20;               </td>

&#x20;               <td class="num-col">0</td>

&#x20;               <td class="num-col">0</td>

&#x20;               <td class="num-col"><strong>0%</strong></td>

&#x20;               <td class="num-col">0</td>

&#x20;               <td class="num-col">0 min</td>

&#x20;             </tr>

&#x20;             <tr>

&#x20;               <td>

&#x20;                 <strong>Tercer Integrante</strong>

&#x20;                 <span class="field-hint">(Colaborador)</span>



&#x20;               </td>

&#x20;               <td class="num-col">3</td>

&#x20;               <td class="num-col">1</td>

&#x20;               <td class="num-col"><strong>33%</strong></td>

&#x20;               <td class="num-col">0</td>

&#x20;               <td class="num-col">0 min</td>

&#x20;             </tr>

&#x20;             <tr>

&#x20;               <td>

&#x20;                 <strong>Aradan Pruebas</strong>

&#x20;                 <span class="field-hint">(Colaborador)</span>



&#x20;               </td>

&#x20;               <td class="num-col">2</td>

&#x20;               <td class="num-col">0</td>

&#x20;               <td class="num-col"><strong>0%</strong></td>

&#x20;               <td class="num-col">0</td>

&#x20;               <td class="num-col">25 min</td>

&#x20;             </tr>

&#x20;           </tbody>

&#x20;           <tfoot class="cumplimiento-total">

&#x20;             <tr>

&#x20;               <th scope="row">Total del equipo</th>

&#x20;               <td class="num-col">6</td>

&#x20;               <td class="num-col">1</td>

&#x20;               <td class="num-col">17%</td>

&#x20;               <td class="num-col">0</td>

&#x20;               <td class="num-col">25 min</td>

&#x20;             </tr>

&#x20;           </tfoot>

&#x20;         </table>

&#x20;       </div>

&#x20;     </section>



&#x20;     <!-- Historial de entregas, observaciones y traspasos -->

&#x20;     <div class="two-col-grid cumplimiento-historial">

&#x20;       <section class="card" aria-labelledby="titulo-entregas">

&#x20;         <h2 class="card-title" id="titulo-entregas">Historial de entregas y observaciones</h2>



&#x20;         <ul class="timeline">

&#x20;           <li class="timeline-item">

&#x20;             <div class="timeline-meta">22 Nov 2026 21:05</div>

&#x20;             <div class="timeline-title">Tarea auxiliar para pruebas de estado · Tercer Integrante</div>

&#x20;             <div>Pendiente de revisión</div>





&#x20;           </li>

&#x20;           <li class="timeline-item is-approved">

&#x20;             <div class="timeline-meta">22 Nov 2026 21:13</div>

&#x20;             <div class="timeline-title">Tarea modificada CURL Lourdes · Tercer Integrante</div>

&#x20;             <div>Entrega aprobada</div>

&#x20;             <div class="timeline-meta">Revisada: 22 Nov 2026 21:18</div>



&#x20;           </li>

&#x20;           <li class="timeline-item is-rejection">

&#x20;             <div class="timeline-meta">22 Nov 2026 21:20</div>

&#x20;             <div class="timeline-title">Tarea auxiliar para prueba de rechazo · Tercer Integrante</div>

&#x20;             <div>Entrega rechazada</div>

&#x20;             <div class="timeline-meta">Revisada: 22 Nov 2026 21:20</div>

&#x20;             <div class="timeline-obs">La entrega necesita correcciones.</div>

&#x20;           </li>

&#x20;         </ul>

&#x20;       </section>

&#x20;       <section class="card" aria-labelledby="titulo-traspasos">

&#x20;         <h2 class="card-title" id="titulo-traspasos">Historial de traspasos de tareas</h2>

&#x20;         <p class="field-hint">Sin registros para el filtro actual.</p>



&#x20;       </section>

&#x20;     </div>

&#x20;   </main>

&#x20; </div>

&#x20; <script>

&#x20;   const salirReporte = document.getElementById('cerrar-sesion-reporte');

&#x20;   salirReporte.addEventListener('click', async () => {

&#x20;       salirReporte.disabled = true;

&#x20;       const aviso = document.getElementById('error-sesion-reporte');

&#x20;       aviso.hidden = true;

&#x20;       try {

&#x20;           const respuesta = await fetch('/cerrarSesion', { method: 'POST' });

&#x20;           if (!respuesta.ok \&\& respuesta.status !== 401) throw new Error();

&#x20;           location.replace('http://localhost:5500/index.html');

&#x20;       } catch {

&#x20;           aviso.textContent = 'No se pudo cerrar la sesión. Intenta de nuevo.';

&#x20;           aviso.hidden = false;

&#x20;           salirReporte.disabled = false;

&#x20;       }

&#x20;   });

&#x20; </script>

</body>

</html>



##### RESULTADO OBTENIDO PRUEBA 11



HTTP/1.1 401

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json;charset=UTF-8

Content-Length: 39

Date: Mon, 23 Nov 2026 04:10:31 GMT



{"mensaje":"No hay una sesión activa"}



##### RESULTADO OBTENIDO PRUEBA 12



HTTP/1.1 403

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 04:12:17 GMT



{"timestamp":"2026-11-23T04:12:17.401Z","status":403,"error":"Forbidden","trace":"org.springframework.web.server.ResponseStatusException: 403 FORBIDDEN \\"No perteneces a este equipo.\\"\\n\\tat mx.uv.luzioteamtime.servicio.CumplimientoServicio.generar(CumplimientoServicio.java:48)\\n\\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\\n\\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)\\n\\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\\n\\tat java.base/java.lang.reflect.Method.invoke(Method.java:569)\\n\\tat org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:359)\\n\\tat org.springframework.aop.framework.ReflectiveMethodInvocation.invokeJoinpoint(ReflectiveMethodInvocation.java:190)\\n\\tat org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:158)\\n\\tat org.springframework.transaction.interceptor.TransactionInterceptor$1.proceedWithInvocation(TransactionInterceptor.java:133)\\n\\tat org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:371)\\n\\tat org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:130)\\n\\tat org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)\\n\\tat org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:719)\\n\\tat mx.uv.luzioteamtime.servicio.CumplimientoServicio$$SpringCGLIB$$0.generar(<generated>)\\n\\tat mx.uv.luzioteamtime.controlador.CumplimientoControlador.mostrar(CumplimientoControlador.java:38)\\n\\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\\n\\tat java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)\\n\\tat java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\\n\\tat java.base/java.lang.reflect.Method.invoke(Method.java:569)\\n\\tat org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:252)\\n\\tat org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:184)\\n\\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117)\\n\\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:934)\\n\\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:853)\\n\\tat org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:86)\\n\\tat org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:963)\\n\\tat org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:866)\\n\\tat org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1000)\\n\\tat org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:892)\\n\\tat jakarta.servlet.http.HttpServlet.service(HttpServlet.java:622)\\n\\tat org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:874)\\n\\tat jakarta.servlet.http.HttpServlet.service(HttpServlet.java:710)\\n\\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:132)\\n\\tat org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:59)\\n\\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:111)\\n\\tat org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)\\n\\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\\n\\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:111)\\n\\tat org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)\\n\\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\\n\\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:111)\\n\\tat org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:199)\\n\\tat org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)\\n\\tat org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:111)\\n\\tat org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:165)\\n\\tat org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:77)\\n\\tat org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:535)\\n\\tat org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:115)\\n\\tat org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:86)\\n\\tat org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:71)\\n\\tat org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:347)\\n\\tat org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:406)\\n\\tat org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:71)\\n\\tat org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:1307)\\n\\tat org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:2036)\\n\\tat org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:74)\\n\\tat org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:949)\\n\\tat org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:483)\\n\\tat org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:74)\\n\\tat java.base/java.lang.Thread.run(Thread.java:840)\\n","message":"No perteneces a este equipo.","path":"/equipos/52/cumplimiento"}

##### 

