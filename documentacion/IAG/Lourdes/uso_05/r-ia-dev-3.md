# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 16/11/2026  
**Número de reporte:** 2

---

# 1. Breve descripción de uso

Generar una prueba CURL para `PUT /iniciarTarea/{id}` que compruebe que un usuario distinto del responsable no puede iniciar la tarea. Se solicitó a ChatGPT analizar el código y proponer un comando CURL con precondiciones y respuesta esperada. Se documentó un escenario ilustrativo con la respuesta HTTP que debería obtenerse.

---

# 2. Evidencias de uso

| Evidencia | Formato |
|------------|------------------------|
| Herramienta | ChatGPT (OpenAI) |
| Modelo/temperatura | GPT-6 / temperatura no disponible |
| Prompt original | [PROMT 1](#promt-1) — [Conversación compartida](https://chatgpt.com/s/t_6afbdb1138a881918196063bc4b76424) |
| Respuesta de IA | [RESPUESTA PROMT 1](#respuesta-promt-1) — texto proporcionado por la estudiante |
| Código anterior | No aplica: la solicitud fue generar una prueba CURL, no modificar código fuente. |
| Código posterior | No aplica: no se generó ni modificó código fuente en esta interacción. |
| Diferencias entre versiones | No aplica: no hubo versiones de código que comparar. Los identificadores y la cookie son datos ilustrativos. |
| Prueba de funcionamiento | [PRUEBA 1](#prueba-1) y [RESPUESTA 1](#respuesta-1). Respuesta HTTP ilustrativa, no capturada de una ejecución. |
| Código de uso | Pruebas |
| Reflexión del estudiante | En el escenario ilustrativo, el sistema rechaza con 403 el intento de iniciar una tarea ajena y devuelve el mensaje de permiso correspondiente. Según la lógica analizada, la tarea conserva el estado POR_HACER. La respuesta presentada es una simulación y no demuestra que se haya ejecutado CURL. |

##### PROMT 1

Analiza TareaControlador.java, TareaServicio.java y las entidades/repositorios relacionados. Genera únicamente una prueba CURL para PUT /iniciarTarea/{id}: impedir que otro usuario inicie una tarea. Incluye comando completo, precondiciones, código HTTP esperado y explicación. Usa los nombres y validaciones reales del código. No modifiques archivos ni inventes resultados ejecutados.

##### RESPUESTA PROMT 1

**Prueba única: impedir que otro usuario inicie una tarea**

**Precondiciones**

- Backend disponible en `http://localhost:8081`, con conexión a la base de datos.
- Existe la tarea con ID `1`, en estado `POR_HACER`, cuyo `responsable` tiene ID `2`.
- El equipo del tablero de esa tarea tiene `archivado = false`.
- Se dispone de una cookie `JSESSIONID` vigente obtenida mediante `/iniciarSesion`, correspondiente a **otro usuario**, por ejemplo, ID `3`.

Los IDs son ejemplos: sustitúyelos y reemplaza la cookie con valores reales que cumplan estas condiciones.

**Comando completo — Bash**

```bash
curl --include \
  --request PUT 'http://localhost:8081/iniciarTarea/1' \
  --cookie 'JSESSIONID=REEMPLAZAR_CON_JSESSIONID_DEL_OTRO_USUARIO'
```

El endpoint no recibe cuerpo: obtiene el ID de la tarea desde la URL y `usuarioId` desde la sesión.

**Código HTTP esperado:** `403 Forbidden`.

**Cuerpo esperado:**

```text
Solo el responsable puede iniciar esta tarea
```

**Explicación**

`TareaServicio.iniciarTarea` busca la tarea mediante `ITarea.findById` y comprueba que su equipo no esté archivado. Después compara el usuario autenticado con el responsable:

```java
if (idUsuario == null ||
    !idUsuario.equals(tarea.getResponsable().getId())) {
    throw new SecurityException(
        "Solo el responsable puede iniciar esta tarea"
    );
}
```

Como los IDs son distintos, `TareaControlador` captura la excepción y responde `403` con ese mensaje. El rechazo ocurre antes de cambiar el estado a `EN_PROGRESO` y llamar a `itarea.save`; por tanto, se espera que la tarea permanezca en `POR_HACER`.

*Prueba propuesta a partir del código; no ejecutada.*

---

##### PRUEBA 1. Impedir que otro usuario inicie una tarea

**Escenario de ejemplo:** el comando reproduce la prueba propuesta por la IA; la cookie y los IDs son ilustrativos.

```bash
curl --include \
  --request PUT 'http://localhost:8081/iniciarTarea/1' \
  --cookie 'JSESSIONID=REEMPLAZAR_CON_JSESSIONID_DEL_OTRO_USUARIO'
```

##### RESPUESTA 1

**Respuesta HTTP simulada según el resultado esperado (no capturada de CURL):**

```http
HTTP/1.1 403
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Content-Type: text/plain;charset=UTF-8

Solo el responsable puede iniciar esta tarea
```

---
