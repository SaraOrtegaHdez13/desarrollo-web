# Requerimientos de LuzioTeamTime

**Proyecto:** Gestor colaborativo de tareas con pomodoro
**Equipo:** Sara Ortega Hernández, María de Lourdes, Aradan
**Versión:** 1.1

\---

## 1\. Descripción

LuzioTeamTime es un sistema web para que equipos pequeños del ámbito escolar (por ejemplo, equipos que desarrollan un proyecto de clase) organicen y den seguimiento a su trabajo. Permite repartir tareas en tableros, revisarlas antes de darlas por terminadas y medir el tiempo de trabajo con la técnica pomodoro. Además genera un reporte de cumplimiento, tanto individual como por equipo.

## 2\. Problema que resuelve

Los equipos de estudiantes que trabajan en proyectos escolares suelen repartir tareas de palabra o en chats, sin saber con claridad quién hace qué, en qué estado va cada cosa ni si una entrega ya fue revisada. LuzioTeamTime centraliza la asignación de tareas, deja un flujo de revisión donde solo un líder da por terminada una tarea (y nunca la propia), y registra el tiempo trabajado y las entregas para tener evidencia objetiva del cumplimiento de cada integrante y del equipo.

## 3\. Alcance

**Incluye:** registro e inicio de sesión, equipos con roles (dos líderes por equipo), tableros, tareas con flujo de revisión, reasignación de tareas con historial, sesiones de pomodoro, reporte de cumplimiento y archivado de equipos.



**No incluye:** WebSockets, chat, notificaciones ni drag and drop. Tampoco se eliminan equipos: se archivan.

## 4\. Roles

El rol se define **por equipo** (tabla `miembros\_equipo`), no en el usuario. Una misma persona puede ser LIDER en un equipo y COLABORADOR en otro. Las columnas de este documento (LIDER, COLABORADOR) siempre se refieren al rol que la persona tiene **en ese equipo**.

|Rol|Qué puede hacer|
|-|-|
|LIDER|Crea tableros, crea, asigna y reasigna tareas, administra miembros, archiva el equipo, y revisa (aprueba o rechaza) las tareas de otros. También puede tener tareas asignadas y trabajarlas como cualquier integrante|
|COLABORADOR|Trabaja solo en sus tareas: las mueve, hace pomodoros y las manda a revisión|

**Dos LIDER por equipo.** Un equipo admite como máximo dos LIDER. Al crearlo hay uno (el creador) y el segundo se agrega después (RF-09 o RF-10). Mientras el equipo tiene un solo LIDER está en una etapa de arranque con estas diferencias:

|Situación|Un solo LIDER|Dos LIDER|
|-|-|-|
|Asignar tareas a un LIDER|No (RN-06)|Sí|
|Revisar tareas de colaboradores|Sí, el único LIDER|Sí, cualquiera de los dos|
|Revisar tareas de un LIDER|No aplica (no puede tener tareas)|Las revisa el otro LIDER|
|Quitar o degradar a un LIDER|No (RN-05)|No (RN-05)|

## 5\. Requerimientos funcionales

### Módulo A: usuarios, equipos y roles (responsable: Sara)

|ID|Requerimiento|
|-|-|
|RF-01|El sistema permite registrar un usuario con nombre, correo y contraseña. El correo es único.|
|RF-02|La contraseña se guarda cifrada con BCrypt, nunca en texto plano.|
|RF-03|El sistema permite iniciar sesión con correo y contraseña. Si son correctos, se crea una sesión (HttpSession con cookie).|
|RF-04|El sistema permite cerrar sesión.|
|RF-05|Toda petición, excepto registro e inicio de sesión, requiere sesión activa. Sin sesión se responde 401.|
|RF-06|Un usuario con sesión puede crear un equipo y queda registrado como LIDER de ese equipo (el primero de los dos).|
|RF-07|Un usuario con sesión puede listar los equipos a los que pertenece, con su rol en cada uno, distinguiendo los activos de los archivados.|
|RF-08|Un miembro puede consultar los datos de su equipo y la lista de sus miembros con su rol.|
|RF-09|Un LIDER puede agregar al equipo a un usuario ya registrado, indicando su correo y su rol. El equipo admite como máximo dos LIDER (RN-05).|
|RF-10|Un LIDER puede cambiar el rol de un miembro (por ejemplo, promover a un COLABORADOR para que sea el segundo LIDER) o quitarlo del equipo, respetando RN-05 y RN-14.|
|RF-11|Un LIDER puede archivar el equipo cuando el proyecto termina. Un equipo archivado queda de solo lectura y sus reportes siguen disponibles. *(Prioridad baja.)*|

### Módulo B: tableros y tareas (responsable: María de Lourdes)

|ID|Requerimiento|
|-|-|
|RF-12|Un LIDER puede crear tableros dentro de su equipo.|
|RF-13|Los miembros del equipo pueden listar y consultar los tableros del equipo.|
|RF-14|Un LIDER puede crear una tarea en un tablero con título, descripción, fecha límite y responsable (cualquier miembro del equipo; un LIDER solo puede ser responsable cuando el equipo ya tiene dos LIDER, RN-06). La tarea nace en POR\_HACER y se registra la asignación inicial.|
|RF-15|Los miembros pueden listar las tareas de un tablero, con filtros opcionales por estado y por responsable.|
|RF-16|Los miembros pueden consultar el detalle de una tarea, con su historial de entregas y de asignaciones.|
|RF-17|Un LIDER puede modificar tareas y eliminar las que estén en POR\_HACER sin ningún avance (RN-19).|
|RF-18|Un LIDER puede reasignar una tarea a otro miembro del equipo (a un LIDER solo si el equipo ya tiene dos, RN-06). La tarea conserva su avance (estado, sesiones de pomodoro y entregas); el nuevo responsable continúa trabajando desde ahí. Se cierra la asignación anterior y se abre una nueva. No se puede reasignar una tarea EN\_REVISION ni una HECHA (RN-15).|
|RF-19|El responsable de una tarea (COLABORADOR o LIDER) puede pasarla de POR\_HACER a EN\_PROGRESO.|
|RF-20|El responsable puede mandar su tarea de EN\_PROGRESO a EN\_REVISION. Se guarda la fecha de entrega en `fecha\_completada` y se crea una revisión pendiente con su número de entrega.|
|RF-21|Un LIDER distinto del responsable puede aprobar una tarea EN\_REVISION: pasa a HECHA y la revisión queda como APROBADA.|
|RF-22|Un LIDER distinto del responsable puede rechazar una tarea EN\_REVISION con una observación obligatoria: vuelve a EN\_PROGRESO y la revisión queda como RECHAZADA.|

### Módulo C: pomodoro y reporte (responsable: Aradan)

|ID|Requerimiento|
|-|-|
|RF-23|La pantalla de tarea incluye un temporizador pomodoro (25 minutos de trabajo y 5 de descanso) que corre en el frontend (`js/pomodoro.js`).|
|RF-24|Al terminar una sesión, el frontend la guarda en el backend con su inicio, fin y duración **en segundos**.|
|RF-25|Solo se puede registrar una sesión en una tarea propia que esté EN\_PROGRESO.|
|RF-26|El sistema genera, con una plantilla Thymeleaf, el reporte de cumplimiento por integrante y por equipo en `/equipos/{id}/cumplimiento?tablero=`. Cualquier miembro del equipo puede verlo. El parámetro `tablero` es opcional: sin él, el reporte cubre todos los tableros del equipo.|
|RF-27|El reporte muestra por integrante: tareas asignadas, porcentaje de tareas hechas, entregas a tiempo, minutos de pomodoro y tareas aprobadas. Muestra también un resumen del equipo con esos mismos indicadores sumados. Si una persona ya no es miembro del equipo pero tuvo tareas asignadas, aparece marcada como "ex integrante".|
|RF-28|El reporte muestra también el historial: fecha de cada entrega, observaciones de rechazo, número de entregas hasta la aprobación y las tareas que el integrante traspasó a otra persona, con el estado en que las dejó. *(Prioridad media.)*|

## 6\. Estados de tarea y transiciones permitidas

Estados: `POR\_HACER`, `EN\_PROGRESO`, `EN\_REVISION`, `HECHA`.

|De|A|Quién puede hacerlo|Condición|
|-|-|-|-|
|(nueva)|POR\_HACER|LIDER|Al crear la tarea|
|POR\_HACER|EN\_PROGRESO|Responsable de la tarea (COLABORADOR o LIDER)|Solo en su tarea|
|EN\_PROGRESO|EN\_REVISION|Responsable de la tarea (COLABORADOR o LIDER)|Solo en su tarea; se guarda la fecha de entrega y se crea la revisión|
|EN\_REVISION|HECHA|LIDER distinto del responsable|Aprobación|
|EN\_REVISION|EN\_PROGRESO|LIDER distinto del responsable|Rechazo; la observación es obligatoria|

Cualquier otra transición se rechaza con 400. **Solo un LIDER puede poner una tarea en HECHA, y nunca la suya.**

## 7\. Matriz rol × acción

Las columnas COLABORADOR y LIDER se refieren al rol de la persona **en ese equipo**. Quien no es miembro del equipo recibe 403 en cualquier acción sobre él (RN-03).

|Acción|Sin sesión|COLABORADOR|LIDER|
|-|-|-|-|
|Registrarse e iniciar sesión|Sí|Sí|Sí|
|Cerrar sesión|No (401)|Sí|Sí|
|Crear un equipo|No (401)|Sí (queda como LIDER)|Sí (queda como LIDER)|
|Ver mis equipos|No (401)|Sí|Sí|
|Ver datos y miembros de un equipo|No (401)|Sí, si es miembro|Sí|
|Agregar, cambiar rol o quitar miembros|No (401)|No (403)|Sí (máximo dos LIDER; no se quita ni degrada a un LIDER)|
|Archivar el equipo|No (401)|No (403)|Sí|
|Crear tablero|No (401)|No (403)|Sí|
|Ver tableros y tareas del equipo|No (401)|Sí, si es miembro|Sí|
|Crear, modificar, reasignar o eliminar tarea|No (401)|No (403)|Sí (asignar a un LIDER solo con dos LIDER en el equipo)|
|Mover tarea POR\_HACER a EN\_PROGRESO|No (401)|Solo su tarea|Solo su tarea|
|Mandar tarea a EN\_REVISION|No (401)|Solo su tarea|Solo su tarea|
|Aprobar o rechazar una entrega|No (401)|No (403)|Sí, pero no la propia (403)|
|Registrar sesión de pomodoro|No (401)|Solo en su tarea EN\_PROGRESO|Solo en su tarea EN\_PROGRESO|
|Ver reporte de cumplimiento|No (401)|Sí, si es miembro|Sí|

Eliminar una tarea solo es posible en POR\_HACER sin avance (RN-19). Mover, mandar a revisión y hacer pomodoro dependen de ser el **responsable** de la tarea, no del rol. Revisar depende de tener el rol LIDER y de no ser el responsable. En un equipo archivado todas las acciones de escritura se rechazan con 403.

## 8\. Reglas de negocio

|ID|Regla|
|-|-|
|RN-01|Un correo corresponde a una sola cuenta.|
|RN-02|La pareja (usuario, equipo) es única en `miembros\_equipo`.|
|RN-03|Solo los miembros de un equipo pueden ver sus tableros y tareas. Quien no sea miembro recibe 403.|
|RN-04|El responsable de una tarea debe ser miembro del equipo al que pertenece el tablero.|
|RN-05|Un equipo admite como máximo dos LIDER. Al crearlo hay uno (el creador) y el segundo se agrega con RF-09 o se promueve con RF-10. No se admite un tercero. Mientras haya un solo LIDER, ese LIDER no puede ser degradado ni quitado (el equipo no puede quedarse sin líder). Una vez que hay dos, no se quita ni se degrada a ninguno.|
|RN-06|Mientras el equipo tenga un solo LIDER, no se pueden asignar ni reasignar tareas a un LIDER. Sí se pueden asignar y revisar tareas de colaboradores. Con dos LIDER ya se pueden asignar tareas a cualquiera de los dos.|
|RN-07|Nadie aprueba ni rechaza su propia tarea. Las tareas de un LIDER las revisa el otro LIDER. Cualquier LIDER puede revisar las tareas de los colaboradores.|
|RN-08|Solo un LIDER puede poner HECHA a una tarea.|
|RN-09|Rechazar una entrega exige una observación no vacía.|
|RN-10|La sesión de pomodoro se registra solo en una tarea propia en estado EN\_PROGRESO, con duración mayor que cero segundos.|
|RN-11|`fecha\_completada` guarda la fecha en que el responsable manda la tarea a revisión. Cada entrega crea una fila en `revisiones\_tarea`; si hay rechazo y reentrega, se crea otra fila y `fecha\_completada` se actualiza.|
|RN-12|Una entrega es "a tiempo" si la tarea está HECHA y la fecha de su `fecha\_completada` es menor o igual a su `fecha\_limite`. La demora del líder en revisar no afecta la puntualidad del responsable.|
|RN-13|El usuario que realiza una acción se obtiene siempre de la sesión. Ningún endpoint recibe el id del usuario desde el frontend.|
|RN-14|No se puede quitar a un miembro que tenga tareas sin terminar (estado distinto de HECHA). Primero se reasignan.|
|RN-15|Al reasignar, la tarea conserva su avance: estado, sesiones y entregas. El nuevo responsable continúa desde ahí. Las sesiones ya hechas siguen acreditadas a quien las hizo. No se puede reasignar una tarea EN\_REVISION ni una HECHA, porque ya está terminada y su mérito pertenece a quien la hizo.|
|RN-16|Los permisos por rol se validan en la capa de servicio.|
|RN-17|Un equipo archivado es de solo lectura: no se crean ni modifican tableros, tareas, miembros ni sesiones, y esas acciones se rechazan con 403. Sus reportes siguen disponibles. Los equipos no se eliminan.|
|RN-18|La duración de las sesiones se guarda en segundos. El reporte la convierte a minutos.|
|RN-19|Solo se puede eliminar una tarea que esté en POR\_HACER y no tenga ningún avance (ninguna sesión de pomodoro ni entrega). Cualquier otra se rechaza con 400. Reasignar una tarea no cuenta como avance.|

### Reglas del reporte de cumplimiento

|Dato|Regla|
|-|-|
|Tareas asignadas|Tareas cuya asignación vigente es de la persona.|
|Porcentaje de tareas hechas|Tareas HECHA entre tareas asignadas, por 100 (0 si no tiene asignadas).|
|Entregas a tiempo|Tareas HECHA de la persona que cumplen RN-12.|
|Minutos de pomodoro|Suma de `duracion\_segundos` de sus sesiones, entre 60. Se mantienen aunque la tarea se haya reasignado.|
|Tareas aprobadas|Tareas HECHA de la persona.|
|Resumen del equipo|Mismos indicadores sumados para todo el equipo (o el tablero filtrado): tareas asignadas, porcentaje de HECHA entre el total de tareas, entregas a tiempo, minutos de pomodoro y tareas aprobadas.|
|Integrantes del reporte|Los miembros actuales del equipo y cualquier persona con asignaciones en el equipo. Quien ya no es miembro se muestra como "ex integrante" y conserva sus cifras.|
|Tareas traspasadas|Se listan aparte, con el estado en que las dejó. No cuentan en su porcentaje; las cuenta como asignadas quien las recibe.|

## 9\. Requerimientos no funcionales

|ID|Requerimiento|
|-|-|
|RNF-01|Arquitectura de 3 capas: controlador, servicio y repositorio, con las entidades en el paquete modelo. Paquete base `mx.uv.luzioteamtime`.|
|RNF-02|Backend en Java 17 con Spring Boot 4.1.1 y Maven, en el puerto 8081.|
|RNF-03|Base de datos MySQL accedida con Hibernate (`ddl-auto=update`).|
|RNF-04|Las contraseñas se cifran con BCrypt (`spring-security-crypto`). Las credenciales de la base de datos se leen de variables de entorno y nunca se suben al repositorio.|
|RNF-05|La sesión se maneja con HttpSession y cookie. Un interceptor responde 401 cuando no hay sesión.|
|RNF-06|La API responde con `ResponseEntity` y los códigos 200, 201, 400, 401, 403 y 404.|
|RNF-07|CORS con origen exacto `http://localhost:5500` y credenciales permitidas.|
|RNF-08|Frontend en HTML, CSS y JavaScript sin librerías, con `fetch` y `credentials: 'include'`. CSS y JS en archivos separados.|
|RNF-09|Los errores se muestran en la página, no solo en consola o con `alert`.|
|RNF-10|El proyecto se desarrolla y ejecuta desde WSL, con MySQL instalado dentro de WSL.|
|RNF-11|El README permite reproducir el proyecto en otro WSL: variables de entorno, creación de la base y el usuario, arranque del backend y del frontend.|
|RNF-12|Cada integrante escribe pruebas de su módulo con JUnit y MockMvc, con casos de éxito y de error.|
|RNF-13|Las operaciones que guardan en varias tablas (crear tarea con su asignación, reasignar, entregar a revisión, aprobar o rechazar) se ejecutan como una sola transacción con `@Transactional`: o se guardan todos los pasos o ninguno.|

## 10\. Decisiones tomadas por el equipo (8 de noviembre de 2026)

|#|Decisión|Resultado|
|-|-|-|
|1|Cantidad de líderes|Dos LIDER por equipo (máximo); el equipo nace con uno y el segundo se agrega o se promueve. Cualquier LIDER revisa a los colaboradores|
|2|Quién revisa las tareas de un LIDER|El otro LIDER, nunca el mismo. Con un solo LIDER no se le pueden asignar tareas|
|3|Quién ve el reporte|Cualquier miembro del equipo|
|4|Reasignación de tareas|La tarea conserva su avance; el reporte conserva lo que hizo cada persona|
|5|Duración del pomodoro|25 minutos de trabajo y 5 de descanso; se guarda en segundos|
|6|Eliminar equipos|No se eliminan; se archivan al terminar el proyecto|
|7|Agregar miembros|Solo usuarios ya registrados, por correo|
|8|Qué ve el colaborador|Todas las tareas del tablero; solo trabaja en las suyas|
|9|`fecha\_completada`|Se guarda al entregar, con historial de entregas y rechazos|
|10|Quitar a un miembro|Primero se reasignan sus tareas pendientes|
|11|Eliminar tareas|Solo en POR\_HACER y sin ningún avance. La reasignación es un asunto aparte: conserva el avance y el nuevo responsable continúa desde ahí|
|12|Miembros removidos en el reporte|El reporte incluye a quien tenga asignaciones en el equipo; los que ya no son miembros aparecen como "ex integrante"|
|13|Operaciones en varias tablas|Se ejecutan con `@Transactional` para que no queden datos a medias|
|14|Reasignar tareas HECHA|No se reasignan: una tarea terminada conserva a su responsable. Tampoco las EN\_REVISION|
|15|Escrituras en un equipo archivado|Se rechazan con 403|



