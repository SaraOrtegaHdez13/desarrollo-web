# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 22/11/2026  
**Número de reporte:** 4

---

# 1. Breve descripción de uso

Al revisar el reporte de cumplimiento de LuzioTeamTime, observé que un líder aparecía con cero tareas hechas. Consulté a ChatGPT para saber cómo se asignan y aprueban las tareas de un líder y a quién se le acredita cada tarea terminada en el reporte.

---

# 2. Evidencias de uso

| Evidencia | Formato |
| :--- | :--- |
| **Herramienta** | ChatGPT (OpenAI) |
| **Modelo/temperatura** | GPT-6.2 / temperatura no disponible |
| **Prompt original** | [PROMT 1](#promt-1) |
| **Respuesta de IA** | [RESPUESTA PROMT 1](#respuesta-promt-1) |
| **Código anterior** | Se revisaron los requisitos y el código de asignación, aprobación y cumplimiento del ZIP enviado. |
| **Código posterior** | La consulta fue de análisis y diseño de pruebas. Los archivos se conservaron tal como estaban. |
| **Diferencias entre versiones** | Se revisó una sola versión del proyecto. |
| **Prueba de funcionamiento** | [PRUEBA 1. Conteo de tareas aprobadas por responsable](#prueba-1-conteo-de-tareas-aprobadas-por-responsable)<br><br>[RESPUESTA 1](#respuesta-1)<br><br>[EVIDENCIA DE EJECUCIÓN](#evidencia-de-ejecución) |
| **Código de uso** | Pruebas |
| **Reflexión del estudiante** | La consulta partió del cero que observé en el reporte. El análisis me ayudó a distinguir el trabajo del responsable de la revisión del líder. Tras ejecutar las pruebas funcionales en el entorno local (`127.0.0.1`), comprobé que todas las reglas de negocio (RN-06 y RN-07) y el conteo en el reporte operan de manera satisfactoria y coinciden con lo esperado. |

##### PROMT 1

> Al revisar el reporte de cumplimiento de LuzioTeamTime, vi que un líder aparecía con cero tareas hechas. Quiero entender si ese resultado es correcto según los requisitos y el código.
>
> Revisa las reglas RN-06 y RN-07 y la lógica de `CumplimientoServicio.java`. Explica cuándo se le puede asignar una tarea a un líder, quién puede aprobarla y a quién se le acredita en la columna «Tareas hechas». Indica los archivos y métodos que respaldan tu explicación.
>
> Propón tres casos de prueba: un equipo con un solo líder, un líder que aprueba la tarea de un colaborador y un líder cuya propia tarea es aprobada por el otro líder. Para cada caso, indica el resultado esperado. Deja el resultado observado como pendiente si no ejecutaste las pruebas. No modifiques los archivos.
>
> Hazlo con el mismo archivo que te envié

##### RESPUESTA PROMT 1

**Resumen de la respuesta de IA.** El conteo de cero es correcto cuando ninguna tarea en estado `HECHA` del alcance consultado tiene al líder como responsable. La IA indicó que, para confirmar el caso observado, se debe revisar el tablero seleccionado y el responsable y estado de las tareas incluidas.

| Regla | Comportamiento | Archivo y método |
| :--- | :--- | :--- |
| **RN-06** | Con un solo líder, el equipo puede asignar y reasignar tareas a colaboradores. Para asignarle una tarea a un líder debe existir otro líder en el equipo. | `documentacion/diseno/requerimientos.md:135` y `TareaServicio.java`, métodos `crearTarea()` (142–161) y `reasignarTarea()` (904–929). |
| **RN-07** | Cualquier líder del equipo puede revisar una tarea de un colaborador. La tarea de un líder la revisa el otro líder. El equipo debe estar activo y la tarea debe estar `EN_REVISION`. | `documentacion/diseno/requerimientos.md:136` y `TareaServicio.java`, métodos `aprobarTarea()` (618–648) y `rechazarTarea()` (719–749). |

Según la respuesta, `aprobarTarea()` registra al revisor, marca la tarea como `HECHA` y conserva al responsable (680–695). `CumplimientoServicio.generar()` usa `tarea.getResponsable()` para elegir la fila del reporte (74–84 y 113–127), y `Fila.contar()` incrementa `aprobadas` cuando la tarea está `HECHA` (234–239). En `cumplimiento.html`, esa cifra aparece en **«Aprobadas»** y el porcentaje en **«% Hechas»** (152–169). Las referencias de línea corresponden al ZIP consultado.

---

##### PRUEBA 1. Conteo de tareas aprobadas por responsable

**Precondiciones.** Aislar cada caso en un equipo activo, filtrar el reporte por el tablero de prueba y partir de cero aprobadas para las personas involucradas. Para aprobar una tarea, esta debe estar `EN_REVISION` con una entrega pendiente.

1. **Un solo líder.** Intentar crear una tarea asignada al líder y reasignarle una tarea `POR_HACER` de un colaborador.
2. **Aprobación de una tarea ajena.** El colaborador envía su tarea a revisión y el líder la aprueba.
3. **Aprobación entre dos líderes.** Asignar una tarea al líder A y enviarla a revisión. Intentar aprobarla primero como A y después como B.

##### RESPUESTA 1

| Caso | Resultado esperado según la lectura del código | Resultado observado |
| :--- | :--- | :--- |
| **Un solo líder** | El servidor rechaza ambas operaciones. La primera tarea no se crea y la segunda conserva al colaborador como responsable. El líder sigue con **0 aprobadas**. | **Satisfactorio.** En el equipo activo *«Equipo dinamita»* (con **Lou** como único líder), el sistema restringió las asignaciones únicamente a los colaboradores (**Sara** y **Aradan**). Al reasignar la tarea *«Revisar épicas»*, la responsabilidad se mantuvo entre colaboradores y **Lou** permaneció con **0 aprobadas**. |
| **El líder aprueba la tarea del colaborador** | La tarea pasa a `HECHA`. El colaborador queda con **1 aprobada** y el líder con **0 aprobadas**. | **Satisfactorio.** En el tablero *«sprint de prueba»*, el líder **Lou** aprobó las tareas enviadas por los colaboradores (**Sara** y **Aradan**), mostrando la notificación *"Tarea aprobada"* y marcando el estado en `Hecha`. El reporte acreditó las tareas hechas a **Sara** y **Aradan**, mientras **Lou** conservó **0 aprobadas**. |
| **El líder B aprueba la tarea del líder A** | El intento de A se rechaza y la tarea permanece `EN_REVISION`. B puede aprobarla y la tarea pasa a `HECHA`. A queda con **1 aprobada** y B con **0 aprobadas**. | **Satisfactorio.** En el equipo *«Sonic team»* (con **Lou** y **Sara** como líderes) y tablero *«comprar Sonic Crossworld»*, la tarea *«Ahorrar»* asignada a **Lou** en estado `En revisión` no pudo ser autoaprobada por él. Al iniciar sesión como la líder **Sara**, aprobó la tarea satisfactoriamente (`Hecha`), acreditando **1 aprobada** a **Lou** y **0 aprobadas** a **Sara**. |

---

##### EVIDENCIA DE EJECUCIÓN

Las pruebas se ejecutaron el **22/11/2026** sobre el servidor local (`127.0.0.1`), respaldadas por capturas de pantalla de la interfaz y registros del servidor:

1. **Validación de equipo con un solo líder (`equipo.html` y `tablero.html`):**
   - **Equipo:** *Equipo dinamita* (`Equipo activo`).
   - **Integrantes:** Lou (`Líder`), Sara (`Colaborador`), Aradan (`Colaborador`).
   - **Comportamiento verificado:** La interfaz y el servidor impiden asignar o reasignar tareas al único líder (RN-06). La reasignación de *«Revisar épicas»* se realizó entre Aradan y Sara con éxito.

2. **Aprobación de tareas de colaboradores (`tablero.html` - *sprint de prueba*):**
   - **Tareas verificadas:** *«crear backlog»* (Sara), *«si»* (Aradan) y *«Revisar épicas»* (Sara).
   - **Resultado en interfaz:** Banner de confirmación *"Tarea aprobada."* y transición a estado `Hecha` con fecha `22 nov 2026`. En el reporte de cumplimiento, los puntos se sumaron exclusivamente a las colaboradoras responsables.

3. **Aprobación cruzada entre dos líderes (`equipo.html` y `tablero.html` - *Sonic team*):**
   - **Integrantes:** Lou (`Líder`), Sara (`Líder`), Aradan (`Colaborador`).
   - **Tarea:** *«Ahorrar»* (Responsable: **Lou**, Fecha límite: `20 dic 2026`).
   - **Flujo verificado:** Con la tarea en estado `En revisión`, se bloqueó la autoaprobación de Lou (RN-07). Posteriormente, desde la sesión de **Sara** (`Sonic team · Líder`), se ejecutó la aprobación mostrando *"Tarea aprobada."* y pasando la tarea a estado `Hecha` (`Aprobada: 22 nov 2026`), acreditando la tarea terminada a **Lou**.

4. **Bitácora de servidor local (`127.0.0.1` - 22/Nov/2026):**
   - Se registraron las peticiones HTTP exitosas (`200` / `304`) correspondientes a las vistas de gestión de equipos, tableros y acciones de tareas (`/equipo.html`, `/tablero.html`, `/js/equipo.js`, `/js/tablero.js`, `/js/acciones-tareas.js`).