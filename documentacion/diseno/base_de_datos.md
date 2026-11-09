# Diseño de la base de datos de LuzioTeamTime

**Equipo:** Sara Ortega Hernández, María de Lourdes, Aradan
**Entregable:** E1 - Diseño inicial
**Versión:** 1.1

\---

## 1\. Diccionario de datos

### 1.1 usuarios (responsable: Sara)

|Columna|Tipo|Restricciones|Descripción|
|-|-|-|-|
|id|INT|PK, autogenerado|Identificador del usuario|
|nombre|VARCHAR(255)|NOT NULL|Nombre visible. El servicio valida además que no venga vacío|
|correo|VARCHAR(255)|UNIQUE, NOT NULL|Correo para iniciar sesión. Un correo, una cuenta|
|password\_hash|VARCHAR(255)|NOT NULL|Hash BCrypt de la contraseña. Nunca se guarda la contraseña en texto plano|

### 1.2 equipos (responsable: Sara)

|Columna|Tipo|Restricciones|Descripción|
|-|-|-|-|
|id|INT|PK, autogenerado|Identificador del equipo|
|nombre|VARCHAR(100)|NOT NULL|Nombre del equipo|
|descripcion|VARCHAR(255)|NULL|Descripción opcional|
|archivado|BOOLEAN|NOT NULL, por defecto falso|Verdadero cuando el proyecto terminó. El equipo queda de solo lectura|

### 1.3 miembros\_equipo (responsable: Sara)

Tabla intermedia entre `usuarios` y `equipos`. Un usuario puede estar en varios equipos y un equipo tiene varios usuarios (muchos a muchos), y la relación necesita un dato propio: el **rol**. Por eso el rol vive aquí y no en `usuarios`.

|Columna|Tipo|Restricciones|Descripción|
|-|-|-|-|
|id|INT|PK, autogenerado|Identificador de la membresía|
|id\_usuario|INT|FK a usuarios.id, NOT NULL|Usuario que pertenece al equipo|
|id\_equipo|INT|FK a equipos.id, NOT NULL|Equipo al que pertenece|
|rol|VARCHAR(20)|NOT NULL|`LIDER` o `COLABORADOR`|

Restricción adicional: la pareja (`id\_usuario`, `id\_equipo`) es **única** (RN-02).

### 1.4 tableros (responsable: María de Lourdes)

|Columna|Tipo|Restricciones|Descripción|
|-|-|-|-|
|id|INT|PK, autogenerado|Identificador del tablero|
|id\_equipo|INT|FK a equipos.id, NOT NULL|Equipo dueño del tablero|
|nombre|VARCHAR(100)|NOT NULL|Nombre del tablero|

### 1.5 tareas (responsable: María de Lourdes)

|Columna|Tipo|Restricciones|Descripción|
|-|-|-|-|
|id|INT|PK, autogenerado|Identificador de la tarea|
|id\_tablero|INT|FK a tableros.id, NOT NULL|Tablero al que pertenece|
|id\_responsable|INT|FK a usuarios.id, NOT NULL|Responsable actual. Puede ser COLABORADOR o LIDER (un LIDER solo si el equipo ya tiene dos, RN-06)|
|titulo|VARCHAR(150)|NOT NULL|Título corto|
|descripcion|VARCHAR(1000)|NULL|Detalle de la tarea|
|estado|VARCHAR(20)|NOT NULL|`POR\_HACER`, `EN\_PROGRESO`, `EN\_REVISION` o `HECHA`|
|fecha\_limite|DATE|NOT NULL|Día límite de entrega|
|observacion|VARCHAR(500)|NULL|Motivo del último rechazo (el historial completo está en `revisiones\_tarea`)|
|fecha\_completada|DATETIME|NULL|Fecha y hora de la última entrega (cuando se mandó a revisión)|

`observacion`, `fecha\_completada` e `id\_responsable` guardan el **estado actual** para consultarlo de forma sencilla. El historial completo vive en `revisiones\_tarea` y `asignaciones\_tarea`.

### 1.6 revisiones\_tarea (responsable: María de Lourdes)

Cada fila es una entrega de la tarea a revisión. Si el líder rechaza y el responsable vuelve a entregar, se crea otra fila. Así queda el historial de versiones hasta la aprobación final.

|Columna|Tipo|Restricciones|Descripción|
|-|-|-|-|
|id|INT|PK, autogenerado|Identificador de la revisión|
|id\_tarea|INT|FK a tareas.id, NOT NULL|Tarea entregada|
|numero\_entrega|INT|NOT NULL|1 para la primera entrega, 2 para la segunda, etc.|
|id\_entregado\_por|INT|FK a usuarios.id, NOT NULL|Quien entregó|
|fecha\_entrega|DATETIME|NOT NULL|Momento de la entrega|
|id\_revisor|INT|FK a usuarios.id, NULL|LIDER que revisó. Nulo mientras está pendiente|
|fecha\_revision|DATETIME|NULL|Momento de la revisión|
|resultado|VARCHAR(20)|NOT NULL|`PENDIENTE`, `APROBADA` o `RECHAZADA`|
|observacion|VARCHAR(500)|NULL|Observación del líder (obligatoria si es rechazo)|

### 1.7 asignaciones\_tarea (responsable: María de Lourdes)

Cada fila es un periodo en que una persona fue responsable de una tarea. Permite que el reporte conserve lo que hizo cada quien cuando una tarea se reasigna.

|Columna|Tipo|Restricciones|Descripción|
|-|-|-|-|
|id|INT|PK, autogenerado|Identificador de la asignación|
|id\_tarea|INT|FK a tareas.id, NOT NULL|Tarea asignada|
|id\_usuario|INT|FK a usuarios.id, NOT NULL|Responsable en ese periodo|
|fecha\_inicio|DATETIME|NOT NULL|Cuándo recibió la tarea|
|fecha\_fin|DATETIME|NULL|Cuándo la dejó. Nulo si es la asignación vigente|
|estado\_al\_recibir|VARCHAR(20)|NOT NULL|Estado de la tarea cuando la recibió|
|estado\_al\_salir|VARCHAR(20)|NULL|Estado de la tarea cuando la dejó. Nulo si sigue vigente|

### 1.8 sesiones (responsable: Aradan)

Cada fila es una sesión de pomodoro terminada.

|Columna|Tipo|Restricciones|Descripción|
|-|-|-|-|
|id|INT|PK, autogenerado|Identificador de la sesión|
|id\_tarea|INT|FK a tareas.id, NOT NULL|Tarea en la que se trabajó|
|id\_usuario|INT|FK a usuarios.id, NOT NULL|Quien hizo la sesión|
|inicio|DATETIME|NOT NULL|Momento en que empezó|
|fin|DATETIME|NOT NULL|Momento en que terminó|
|duracion\_segundos|INT|NOT NULL|Duración real en segundos. Debe ser mayor que 0 (lo valida el servicio, RN-10)|

**Notas generales de decisiones tomadas:** el temporizador del navegador cuenta en segundos, así que se guarda el dato exacto. Si alguien detiene un pomodoro a los 18 minutos con 30 segundos, guardar minutos enteros obliga a redondear, y al sumar muchas sesiones el error se acumula. Se suma en segundos y solo al final, en el reporte, se divide entre 60.

`id\_usuario` parece repetir al responsable de la tarea, pero se guarda porque una tarea puede reasignarse: las sesiones ya hechas siguen siendo de quien las hizo.

## 2\. Relaciones

|Relación|Cardinalidad|Llave|
|-|-|-|
|usuarios y equipos|Muchos a muchos, mediante `miembros\_equipo`|`miembros\_equipo.id\_usuario`, `miembros\_equipo.id\_equipo`|
|equipos y tableros|Un equipo tiene varios tableros|`tableros.id\_equipo`|
|tableros y tareas|Un tablero contiene varias tareas|`tareas.id\_tablero`|
|usuarios y tareas|Un usuario es responsable de varias tareas|`tareas.id\_responsable`|
|tareas y revisiones\_tarea|Una tarea tiene varias entregas|`revisiones\_tarea.id\_tarea`|
|usuarios y revisiones\_tarea|Un usuario entrega y otro revisa (dos llaves)|`id\_entregado\_por`, `id\_revisor`|
|tareas y asignaciones\_tarea|Una tarea pasa por varias asignaciones|`asignaciones\_tarea.id\_tarea`|
|usuarios y asignaciones\_tarea|Un usuario tiene varias asignaciones|`asignaciones\_tarea.id\_usuario`|
|tareas y sesiones|Una tarea tiene varias sesiones de pomodoro|`sesiones.id\_tarea`|
|usuarios y sesiones|Un usuario realiza varias sesiones|`sesiones.id\_usuario`|

## 3\. Reglas que la base de datos no garantiza

La base de datos solo asegura tipos, llaves y unicidad. Estas reglas se validan en la **capa de servicio**:

|Regla|Dónde se valida|
|-|-|
|Un equipo tiene como máximo dos LIDER (RN-05)|Al agregar o cambiar el rol de un miembro|
|Con un solo LIDER, ese LIDER no se quita ni se degrada; con dos, tampoco a ninguno (RN-05)|Al cambiar rol o quitar miembros|
|El correo del miembro a agregar debe estar registrado (404) y no estar ya en el equipo (400) (RF-09, RN-02)|Al agregar un miembro|
|Solo los miembros ven tableros y tareas del equipo; si no, 403 (RN-03)|En todos los servicios de lectura|
|Con un solo LIDER no se asignan tareas a líderes (RN-06)|Al crear o reasignar una tarea|
|`id\_responsable` debe ser miembro del equipo del tablero (RN-04)|Al crear o reasignar una tarea|
|Solo se permiten las transiciones de estado de `requerimientos.md`, sección 6|Al cambiar el estado|
|Quien revisa es LIDER y distinto del responsable (RN-07)|Al aprobar o rechazar|
|El rechazo exige una observación no vacía (RN-09)|Al rechazar|
|Una sesión se registra solo en una tarea propia EN\_PROGRESO, con duración mayor que 0 (RN-10)|Al crear la sesión|
|No se quita a un miembro con tareas sin terminar (RN-14)|Al quitar un miembro|
|No se reasigna una tarea EN\_REVISION ni HECHA (RN-15)|Al reasignar|
|Un equipo archivado no admite escrituras y responde 403 (RN-17)|En todos los servicios de escritura|
|Solo se elimina una tarea en POR\_HACER sin sesiones ni revisiones (RN-19); al eliminarla se borran antes sus asignaciones|Al eliminar la tarea|
|Al crear una tarea, crear también su asignación inicial|Al crear la tarea|
|Al entregar, cambiar el estado, actualizar `fecha\_completada` y crear la revisión PENDIENTE|Al mandar a revisión|
|Al aprobar o rechazar, cambiar el estado de la tarea y el resultado de la revisión|Al aprobar o rechazar|
|Al reasignar, cerrar la asignación vigente, abrir la nueva y actualizar `tareas.id\_responsable`|Al reasignar|

**Transacciones (RNF-13).** Las últimas cuatro filas guardan en más de una tabla. Cada método de servicio que las hace lleva `@Transactional`, para que, si falla un paso, se deshagan los anteriores. Así `tareas.id\_responsable` y la asignación vigente de `asignaciones\_tarea` nunca quedan contradiciéndose.

## 4\. Correspondencia con las entidades Java

Spring Boot nombra la tabla a partir de la clase en minúsculas y con guion bajo en las mayúsculas internas (por ejemplo, `MiembroEquipo` daría `miembro\_equipo`, en singular). Para que se llamen como en este documento, cada entidad lleva `@Table(name = "...")`.

|Clase (paquete modelo)|Tabla|
|-|-|
|Usuario|usuarios|
|Equipo|equipos|
|MiembroEquipo|miembros\_equipo|
|Tablero|tableros|
|Tarea|tareas|
|RevisionTarea|revisiones\_tarea|
|AsignacionTarea|asignaciones\_tarea|
|SesionPomodoro|sesiones|

La clase de `sesiones` se llama `SesionPomodoro` para no confundirla con `HttpSession` ni con `SesionInterceptor`.

## 5\. Cómo sale cada dato del reporte de cumplimiento

El reporte (`/equipos/{id}/cumplimiento?tablero=`) se calcula por integrante y por equipo. El parámetro `tablero` es opcional: si no viene, se consideran todos los tableros del equipo. Para filtrar por tablero, las consultas unen `asignaciones\_tarea` o `sesiones` con `tareas` y usan `tareas.id\_tablero`.

|Dato del reporte|De dónde sale|
|-|-|
|Integrantes del reporte|Los de `miembros\_equipo` del equipo, más cualquier usuario con filas en `asignaciones\_tarea` de tareas de ese equipo. Quien ya no está en `miembros\_equipo` se muestra como "ex integrante"|
|Tareas asignadas|`asignaciones\_tarea` con `fecha\_fin` nula (asignación vigente) de ese usuario|
|Porcentaje de tareas hechas|Tareas vigentes del usuario con `estado = HECHA`, entre tareas asignadas, por 100 (0 si no tiene asignadas)|
|Entregas a tiempo|Tareas HECHA cuya fecha de `fecha\_completada` es menor o igual a `fecha\_limite`|
|Minutos de pomodoro|Suma de `sesiones.duracion\_segundos` de ese `id\_usuario`, entre 60|
|Tareas aprobadas|Tareas HECHA del usuario|
|Historial de entregas|`revisiones\_tarea`: fechas, resultados, observaciones y número de entrega|
|Tareas traspasadas|`asignaciones\_tarea` del usuario con `fecha\_fin` no nula, con `estado\_al\_salir`|
|Resumen del equipo|Los mismos cálculos sobre todas las tareas del tablero (o de todos los tableros): total de tareas, tareas HECHA, entregas a tiempo, suma de segundos de todas las sesiones entre 60, y tareas aprobadas|



