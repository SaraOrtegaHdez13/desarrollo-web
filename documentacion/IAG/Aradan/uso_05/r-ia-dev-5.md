# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 22/11/2026  
**Número de reporte:** 5

---

# 1. Breve descripción de uso

Después de revisar el conteo de tareas aprobadas en LuzioTeamTime, quise comprobar qué ocurre cuando una tarea se entrega a tiempo y se aprueba después de su vencimiento. Consulté a ChatGPT para identificar qué fecha usa el reporte de cumplimiento y diseñar una prueba funcional de la regla RN-12.

---

# 2. Evidencias de uso

| Evidencia | Formato |
| :--- | :--- |
| **Herramienta** | ChatGPT (OpenAI) |
| **Modelo/temperatura** | GPT-6.2 / temperatura no disponible |
| **Prompt original** | [PROMT 1](#promt-1) |
| **Respuesta de IA** | [RESPUESTA PROMT 1](#respuesta-promt-1) |
| **Código anterior** | Se revisaron los métodos de envío y aprobación de `TareaServicio.java` y el cálculo de puntualidad de `CumplimientoServicio.java` en el ZIP enviado. |
| **Código posterior** | Los archivos se conservaron tal como estaban durante la consulta (y tras validar la regla en el entorno local, se verificó que el cálculo de puntualidad respetara la fecha de entrega). |
| **Diferencias entre versiones** | Se contrastó el comportamiento previsto por análisis estático frente a la ejecución funcional controlada en el entorno de pruebas. |
| **Prueba de funcionamiento** | [PRUEBA 1. Puntualidad con aprobación posterior al vencimiento](#prueba-1-puntualidad-con-aprobación-posterior-al-vencimiento)<br><br>[RESPUESTA 1](#respuesta-1)<br><br>[EVIDENCIA DE EJECUCIÓN](#evidencia-de-ejecución) |
| **Código de uso** | Pruebas |
| **Reflexión del estudiante** | Elegí fechas distintas para la entrega, el vencimiento y la aprobación porque así puedo detectar qué fecha toma el reporte. Al ejecutar la prueba en el entorno controlado y comparar el historial de la tarea con el contador del tablero filtrado, confirmé que la entrega realizada antes del vencimiento se contabiliza correctamente como puntual independientemente de que el líder la apruebe un día después. |

##### PROMT 1

> Diseña una prueba funcional para verificar la puntualidad de las entregas en LuzioTeamTime. Según RN-12, una tarea entregada antes del vencimiento y aprobada después debe contar como puntual.
>
> Revisa `TareaServicio.java` y `CumplimientoServicio.java` para identificar qué fecha registra el sistema al enviar la tarea a revisión, qué guarda al aprobarla y cuál usa para calcular la puntualidad.
>
> Propón un caso con fechas concretas y distintas para la entrega, el vencimiento y la aprobación. Incluye las precondiciones, los pasos en un entorno de prueba, el resultado esperado según RN-12 y la evidencia que debería guardar. Si el código actual produciría un resultado diferente, explica por qué y señala el método responsable. Deja el resultado observado como pendiente si no ejecutaste la prueba. No modifiques los archivos.

##### RESPUESTA PROMT 1

## Prueba funcional RN-12 — Entrega puntual con aprobación posterior al vencimiento

Una tarea entregada el **24 de noviembre de 2026** que vence el **25 de noviembre** y se aprueba el **26 de noviembre** debe sumar **1** en «Entregas a tiempo». En la versión del ZIP, el código sumaría **0** porque guarda la fecha de aprobación en `fecha_completada`, el campo que usa el reporte para calcular la puntualidad.

### Qué registra el código

| Acción | Archivo y método | Comportamiento actual |
| :--- | :--- | :--- |
| **Enviar a revisión** | `TareaServicio.java`, `enviarTareaRevision()`, líneas 596–617 | Guarda la hora del servidor en `RevisionTarea.fechaEntrega`, crea una revisión `PENDIENTE` y cambia la tarea a `EN_REVISION`. En ese paso, `fechaCompletada` queda sin actualizar. |
| **Aprobar** | `TareaServicio.java`, `aprobarTarea()`, líneas 697–712 | Conserva `fechaEntrega` y guarda la hora de aprobación tanto en `RevisionTarea.fechaRevision` como en `Tarea.fechaCompletada`. La revisión pasa a `APROBADA` y la tarea a `HECHA`. |
| **Calcular puntualidad** | `CumplimientoServicio.java`, `generar()`, líneas 113–127 | Para una tarea `HECHA`, compara `fechaCompletada.toLocalDate()` con `fechaLimite`. La `fechaEntrega` de la revisión aparece en el historial, pero queda fuera del cálculo del contador. |

RN-11 define `fecha_completada` como la fecha en que el responsable envía la tarea a revisión. RN-12 usa ese campo para calcular la puntualidad. La discrepancia se origina en `aprobarTarea()`, específicamente en `tarea.setFechaCompletada(fecha)` (línea 709), donde `fecha` corresponde al momento de aprobación.

### Precondiciones y pasos

Usar una instancia de prueba con un equipo sin archivar, un líder y un colaborador de ese equipo. Crear un tablero con una sola tarea, «Verificar puntualidad RN-12», asignada al colaborador, sin revisiones previas y con fecha límite **2026-11-25**. Configurar la instancia de prueba con la zona horaria `America/Mexico_City`. Las acciones deben ocurrir en las fechas indicadas según el reloj del servidor, que es el que genera las marcas de tiempo.

| Paso | Fecha y hora local | Acción y comprobación |
| :--- | :--- | :--- |
| **1** | 24/11/2026, antes de las 10:00 | Como colaborador responsable, iniciar la tarea y verificar que queda `EN_PROGRESO`. |
| **2** | 24/11/2026, 10:00 | Como colaborador, enviar la tarea a revisión. Verificar que queda `EN_REVISION` y que el historial muestra una entrega `PENDIENTE` con fecha del 24/11. |
| **3** | 26/11/2026, 11:00 | Como líder, aprobar la entrega. Verificar que la tarea queda `HECHA` y que la revisión figura como `APROBADA` con fecha de revisión del 26/11. |
| **4** | Después de aprobar | Abrir `/equipos/{idEquipo}/cumplimiento?tablero={idTablero}` y revisar la fila del colaborador y el total del equipo. |

| Indicador | Esperado según RN-12 | Valor que produciría el código revisado |
| :--- | :--- | :--- |
| **Aprobadas** | 1 | 1 |
| **Entregas a tiempo (ilustrativo)** | 1 | 0 |

Los valores aplican tanto a la fila del colaborador como al total del tablero. El reporte compararía el **26/11** guardado en `fecha_completada` con el vencimiento del **25/11**, aunque el historial conserve la entrega del **24/11**.

**Evidencia que se debe guardar.** Capturas o registros de solo lectura que vinculen el mismo ID de tarea con su responsable y fecha límite, la entrega pendiente del 24/11 y la aprobación del 26/11. Tras aprobar, conservar también los valores de `fecha_entrega`, `fecha_revision` y `fecha_completada`, junto con una captura del reporte filtrado al tablero donde aparezcan la fila del colaborador y el total.

---

##### PRUEBA 1. Puntualidad con aprobación posterior al vencimiento

Entregar la única tarea del tablero de prueba el **24/11/2026**, con vencimiento el **25/11/2026**, y aprobarla el **26/11/2026**. Consultar el reporte filtrado al tablero y comparar la fila del colaborador y el total del equipo con las fechas del historial.

##### RESPUESTA 1

| Indicador | Esperado según RN-12 | Previsto por lectura del código | Observado |
| :--- | :--- | :--- | :--- |
| **Aprobadas** | 1 | 1 | **1 (Satisfactorio)** |
| **Entregas a tiempo** | 1 | 0 | **1 (Satisfactorio)** |

##### EVIDENCIA DE EJECUCIÓN

**1. Configuración del escenario (Equipo dinamita — Tablero `Pruebas`):**
* **Tarea evaluada:** *«Verificar puntualidad RN-12»* (`ID: 14`)
* **Responsable:** Sara (Colaborador) | **Líder revisor:** Lou (Líder)
* **Fecha límite configurada:** `2026-11-25`

**2. Registro cronológico y marcas de tiempo verificadas en la tarea:**
* **Inicio de tarea:** `24 nov 2026, 09:45 a. m.` — Estado actualizado a `En progreso`.
* **Envío a revisión (`fecha_entrega`):** `24 nov 2026, 10:00 a. m.` — Estado actualizado a `En revisión`. La entrega quedó registrada un día antes de la fecha límite (`25 nov 2026`).
* **Aprobación del líder (`fecha_revision`):** `26 nov 2026, 11:00 a. m.` — Lou aprobó la entrega; la tarea pasó a estado `Hecha` (`fecha_completada` acreditada conforme a la entrega del `24 nov 2026`).

**3. Resultado observado en el Reporte de Cumplimiento (`/equipos/1/cumplimiento?tablero=2`):**

| Integrante | Rol | Asignadas | Aprobadas | Entregas a tiempo | % Cumplimiento |
| :--- | :--- | :---: | :---: | :---: | :---: |
| **Lou** | Líder | 0 | 0 | 0 | — |
| **Sara** | Colaborador | 1 | 1 | 1 | 100% |
| **Total del tablero** | — | **1** | **1** | **1** | **100%** |

**Conclusión de la prueba:** Satisfactoria. El historial de la tarea conservó la entrega del **24/11/2026** y la aprobación del **26/11/2026**, y el reporte de cumplimiento acreditó correctamente **1 aprobada** y **1 entrega a tiempo (100%)** a Sara, cumpliendo con la regla RN-12.