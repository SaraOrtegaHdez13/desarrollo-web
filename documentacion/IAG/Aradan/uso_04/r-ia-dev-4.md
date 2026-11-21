# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 20/11/2026  
**Número de reporte:** 4

---

# 1. Breve descripción de uso

Solicité a ChatGPT una prueba cURL para `PUT /reasignarTarea/{id}` de LuzioTeamTime. El objetivo fue comprobar que el cambio de responsable conserve el estado de la tarea y el historial de asignaciones. La prueba incluye consultas a `GET /obtenerTarea/{id}` antes y después de la reasignación.

---

# 2. Evidencias de uso

| Evidencia | Formato |
|---|---|
| Herramienta | ChatGPT |
| Modelo y temperatura | GPT-6.2 Sol. Temperatura sin especificar. |
| Prompt original | [PROMPT 1](#prompt-1) · [Conversación compartida](https://chatgpt.com/share/6b01162b-4d98-83e8-884a-0cfe1a542c7c) |
| Respuesta de IA | [RESPUESTA AL PROMPT 1](#respuesta-al-prompt-1) |
| Código anterior | `TareaControlador.java`, `TareaServicio.java` y las entidades y repositorios relacionados. |
| Código posterior | Se generó un comando cURL y un procedimiento de verificación. El código fuente no se modificó. |
| Diferencias entre versiones | No aplica. La actividad consistió en preparar una prueba. |
| Prueba de funcionamiento | [PRUEBA 1](#prueba-1) · [RESPUESTA 1](#respuesta-1). Prueba ejecutada. |
| Código de uso | Pruebas |
| Reflexión del estudiante | La propuesta me ayudó a identificar qué revisar además del HTTP 200. Al comparar el detalle antes y después puedo comprobar si se conservan las asignaciones previas, si se cierra la anterior y si la nueva comienza en ese mismo momento. |

##### PROMPT 1

Analiza TareaControlador.java, TareaServicio.java y las entidades/repositorios relacionados. Genera únicamente una prueba CURL para PUT /reasignarTarea/{id}: reasignar tarea y conservar historial. Incluye comando completo, precondiciones, código HTTP esperado y explicación. Usa los nombres y validaciones reales del código. No modifiques archivos ni inventes resultados ejecutados.

##### RESPUESTA AL PROMPT 1

### Prueba cURL — Reasignar una tarea y conservar su historial

**Precondiciones**

- El backend está disponible en `http://localhost:8081`. Sustituye `VALOR_REAL_DE_LA_COOKIE` por el `JSESSIONID` de una sesión activa de un usuario con rol `LIDER` en el equipo del tablero.
- Usa una tarea existente de un equipo sin archivar. Debe estar en estado `POR_HACER` o `EN_PROGRESO` y tener exactamente una asignación vigente con `fechaFin = null`.
- El nuevo responsable debe existir, pertenecer al mismo equipo y ser distinto del responsable actual. Si también tiene rol `LIDER`, debe existir otro líder en el equipo.
- Los IDs `7` y `30` son ejemplos. Sustitúyelos por el ID de la tarea y el del nuevo responsable, respectivamente.

```bash
curl -i -X PUT "http://localhost:8081/reasignarTarea/7" -b "JSESSIONID=VALOR_REAL_DE_LA_COOKIE" -H "Content-Type: application/json" --data-raw '{"idResponsable":30}'
```

**HTTP esperado** `200 OK`. El cuerpo devuelve un `TareaResumen` con `idResponsable: 30` y el mismo `estado` que tenía la tarea antes de la solicitud.

**Verificación del historial.** Consulta `GET /obtenerTarea/7` con la misma sesión antes y después del `PUT`. En `asignaciones`, comprueba que los registros previos se conservan, que la asignación del responsable anterior queda cerrada con `fechaFin` y `estadoAlSalir`, y que aparece una nueva asignación con `idUsuario: 30` y `fechaFin: null`. La `fechaInicio` de la nueva asignación debe coincidir con la `fechaFin` de la anterior, y su `estadoAlRecibir` debe coincidir con el estado de la tarea. El servicio guarda estos cambios en una sola transacción.

##### PRUEBA 1

1. Consultar `GET /obtenerTarea/7` y guardar el estado y las asignaciones iniciales.
2. Ejecutar el `PUT /reasignarTarea/7` indicado arriba con los datos del entorno de prueba.
3. Volver a consultar el detalle con la misma sesión y comparar el estado y las asignaciones.

##### RESPUESTA 1

**Resultado esperado.** HTTP `200 OK`, cambio de responsable y conservación del estado. La asignación anterior debe quedar cerrada, la nueva debe quedar vigente y las asignaciones previas deben conservarse.

**Resultado observado.** Según lo reportado por la estudiante, la prueba devolvió HTTP `200 OK`. La tarea cambió de responsable y conservó su estado. La asignación anterior quedó cerrada, la nueva quedó vigente y las asignaciones previas permanecieron en el historial. Queda pendiente adjuntar la salida del `PUT` y de las dos consultas al detalle.
