# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 16/11/2026  
**Número de reporte:** 3

---

# 1. Breve descripción de uso

Generar una prueba CURL para `PUT /reasignarTarea/{id}` que compruebe el cambio de responsable y la conservación del historial de asignaciones. Se solicitó a ChatGPT analizar el código y proponer un comando CURL con precondiciones y respuesta esperada. Se documentó un escenario ilustrativo con la respuesta HTTP que debería obtenerse.

---

# 2. Evidencias de uso

| Evidencia | Formato |
|------------|------------------------|
| Herramienta | ChatGPT (OpenAI) |
| Modelo/temperatura | GPT-6 / temperatura no disponible |
| Prompt original | [PROMT 1](#promt-1) — [Conversación compartida](https://chatgpt.com/s/t_6afbdb638b148191bb2a403ad02989a0) |
| Respuesta de IA | [RESPUESTA PROMT 1](#respuesta-promt-1) — texto proporcionado por la estudiante |
| Código anterior | No aplica: la solicitud fue generar una prueba CURL, no modificar código fuente. |
| Código posterior | No aplica: no se generó ni modificó código fuente en esta interacción. |
| Diferencias entre versiones | No aplica: no hubo versiones de código que comparar. Los identificadores y la cookie son datos ilustrativos. |
| Prueba de funcionamiento | [PRUEBA 1](#prueba-1) y [RESPUESTA 1](#respuesta-1). Respuesta HTTP ilustrativa, no capturada de una ejecución. |
| Código de uso | Pruebas |
| Reflexión del estudiante | En el escenario ilustrativo, la reasignación devuelve 200 y la tarea cambia al responsable 3 con estado POR_HACER. El historial se conserva según la lógica del servicio, pero no puede comprobarse solo con el JSON de respuesta. La salida presentada es una simulación y no demuestra que se haya ejecutado CURL. |

##### PROMT 1

Analiza TareaControlador.java, TareaServicio.java y las entidades/repositorios relacionados. Genera únicamente una prueba CURL para PUT /reasignarTarea/{id}: reasignar tarea y conservar historial. Incluye comando completo, precondiciones, código HTTP esperado y explicación. Usa los nombres y validaciones reales del código. No modifiques archivos ni inventes resultados ejecutados.

##### RESPUESTA PROMT 1

**Prueba única: reasignar una tarea conservando su historial**

**Precondiciones**

- Backend disponible en `http://localhost:8081`, con conexión a la base de datos.
- Existe la tarea con ID `1`, en estado `EN_PROGRESO`, asignada al usuario `2`.
- El equipo de su tablero tiene `archivado = false`.
- La cookie `JSESSIONID` corresponde a una sesión vigente de un usuario con rol `LIDER` en ese equipo.
- El nuevo responsable, usuario `3`, existe y pertenece al mismo equipo con rol `COLABORADOR`.
- La tarea tiene exactamente una `AsignacionTarea` activa (`fechaFin = null`), correspondiente al responsable actual. Las asignaciones anteriores, si existen, están cerradas.

Los IDs son ejemplos; reemplázalos y utiliza una cookie real que cumpla estas condiciones.

**Comando completo — Bash**

```bash
curl --include \
  --request PUT 'http://localhost:8081/reasignarTarea/1' \
  --header 'Content-Type: application/json' \
  --cookie 'JSESSIONID=REEMPLAZAR_CON_JSESSIONID_DEL_LIDER' \
  --data-raw '{
    "idResponsable": 3
  }'
```

**Código HTTP esperado:** `200 OK`.

La respuesta es un JSON de `TareaResumen`. Para este caso se esperan `idResponsable = 3`, `estado = "POR_HACER"`, `observacion = null` y `fechaCompletada = null`.

**Explicación**

`TareaControlador` recibe `idResponsable` mediante `TareaReasignacion` y obtiene `usuarioId` de la sesión. `TareaServicio.reasignarTarea` comprueba que:

- El solicitante sea `LIDER` del equipo y este no esté archivado.
- La tarea esté en `POR_HACER` o `EN_PROGRESO`.
- El nuevo responsable esté indicado, sea distinto del actual, exista y pertenezca al equipo.
- Exista exactamente una asignación activa.

Si el nuevo responsable fuera `LIDER`, también exigiría otro líder en el mismo equipo.

El método utiliza `@Transactional` y consulta el historial mediante `IAsignacionTarea.findByTarea_Id`. Se esperan estos cambios:

| Registro | Resultado esperado |
|---|---|
| Asignación del usuario `2` | Conserva su registro; establece `fechaFin` y `estadoAlSalir = "EN_PROGRESO"`. |
| Nueva asignación del usuario `3` | Registra `fechaInicio` igual al cierre anterior, `fechaFin = null`, `estadoAlRecibir = "POR_HACER"` y `estadoAlSalir = null`. |
| Asignaciones anteriores | Se conservan; el total aumenta en un registro. |

La respuesta no incluye el historial; su conservación debe verificarse en `asignaciones_tarea`.

*Prueba propuesta a partir del código; no ejecutada.*

---

##### PRUEBA 1. Reasignar tarea y conservar historial

**Escenario de ejemplo:** el comando reproduce la prueba propuesta por la IA; la cookie y los IDs son ilustrativos.

```bash
curl --include \
  --request PUT 'http://localhost:8081/reasignarTarea/1' \
  --header 'Content-Type: application/json' \
  --cookie 'JSESSIONID=REEMPLAZAR_CON_JSESSIONID_DEL_LIDER' \
  --data-raw '{
    "idResponsable": 3
  }'
```

##### RESPUESTA 1

**Respuesta HTTP simulada según el resultado esperado (no capturada de CURL):**

```http
HTTP/1.1 200
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Content-Type: application/json

{
  "id": 1,
  "idResponsable": 3,
  "estado": "POR_HACER",
  "observacion": null,
  "fechaCompletada": null
}
```

---
