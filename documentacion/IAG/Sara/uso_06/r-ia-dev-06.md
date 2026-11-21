# Reporte de IA Desarrollo en el Proyecto

**Fecha:**  20/11/2026
**Número de reporte:** 06

\---

# 1\. Breve descripción de uso

Se utilizó IA generativa para preparar, revisar y organizar pruebas de funcionamiento mediante CURL para los endpoints del módulo de sesiones Pomodoro:

* `POST /crearSesion/{idTarea}`
* `GET /obtenerSesiones/{idTarea}`

El propósito fue comprobar el comportamiento de la API con sesión autenticada, sin sesión y con diferentes permisos de acceso, además de registrar una sesión Pomodoro y consultar posteriormente el registro guardado.

Las pruebas se ejecutaron contra el backend local en el puerto `8081`. La autenticación utiliza una cookie de sesión (`JSESSIONID`), guardada y enviada mediante un archivo `cookies.txt`.

**Alcance:** este reporte documenta únicamente las pruebas CURL que sí fueron ejecutadas y cuyos resultados quedaron registrados. Las pruebas negativas adicionales para `POST /crearSesion/{idTarea}` se mantienen pendientes y no se presentan como ejecutadas.

\---

# 2\. Evidencias de uso

|Concepto|Descripción|
|-|-|
|Herramienta|ChatGPT|
|Modelo/temperatura|GPT-6 / temperatura no disponible|
|Prompt original|Solicitud de apoyo para generar y documentar pruebas CURL del módulo Pomodoro, tomando como base los endpoints, el controlador, el servicio y el formato de reporte proporcionado por el estudiante.|
|Respuesta de IA|Se propusieron comandos CURL para iniciar sesión, guardar la cookie, consultar sesiones, preparar datos de prueba, iniciar una tarea y registrar y consultar una sesión Pomodoro. También se identificaron escenarios negativos que todavía requieren ejecución.|
|Código anterior|No se generó código de aplicación para esta actividad. Se utilizaron los endpoints existentes y comandos CURL para probarlos.|
|Código posterior|Comandos CURL adaptados al puerto `8081`, a las rutas reales del proyecto y al uso de la cookie de sesión `cookies.txt`.|
|Diferencias entre versiones|Se sustituyeron los valores genéricos por `localhost:8081`, las rutas reales y los identificadores de los registros creados durante las pruebas. Las credenciales se reemplazaron por marcadores en la documentación.|
|Prueba de funcionamiento|Se documentaron respuestas reales `200`, `201`, `401` y `403`. Los comandos y respuestas están en el archivo `pruebas-curl-pomodoro.md`.|
|Código de uso|**Pruebas**|
|Reflexión del estudiante|Las pruebas permitieron comprobar que el backend protege las rutas mediante sesión, valida que el usuario tenga acceso a la tarea y permite registrar y consultar sesiones Pomodoro. También se observó que iniciar sesión no garantiza acceso a cualquier tarea: el usuario debe pertenecer al equipo correspondiente y, para registrar una sesión, debe ser responsable de una tarea en progreso.|

\---

# 3\. Solicitud utilizada

Se solicitó analizar el controlador y el servicio de sesiones Pomodoro y preparar pruebas CURL para los endpoints de creación y consulta de sesiones. Se pidió utilizar las rutas, los campos JSON y los códigos HTTP reales del proyecto, conservar la cookie de sesión y documentar los comandos y resultados observados.

La solicitud se fue ajustando con los resultados reales obtenidos en la terminal. No se consideraron ejecutadas las pruebas que solo se propusieron como escenarios adicionales.

\---

# 4\. Resumen de pruebas CURL ejecutadas

|Prueba|Endpoint o preparación|Resultado observado|
|-|-|-:|
|Inicio de sesión y guardado de cookie|`POST /iniciarSesion`|`200`|
|Consulta sin sesión|`GET /obtenerSesiones/1`|`401`|
|Consulta de tarea sin autorización|`GET /obtenerSesiones/1`|`403`|
|Consulta de tarea accesible sin sesiones|`GET /obtenerSesiones/2`|`200`, lista vacía|
|Creación de tablero para preparar datos|`POST /crearTablero/1`|`201`|
|Creación de tarea para preparar datos|`POST /crearTarea/2`|`201`|
|Creación de tarea propia para Pomodoro|`POST /crearTarea/2`|`201`|
|Inicio de la tarea propia|`PUT /iniciarTarea/3`|`200`|
|Registro de sesión Pomodoro|`POST /crearSesion/3`|`201`|
|Consulta de la sesión registrada|`GET /obtenerSesiones/3`|`200`|

Los comandos CURL completos y los cuerpos de respuesta observados se conservan en el documento complementario **`pruebas-curl-pomodoro.md`**.

\---

# 5\. Resultados representativos

## 5.1. Consulta sin sesión

**Comando:**

```bash
curl -i http://localhost:8081/obtenerSesiones/1
```

**Resultado observado:** `HTTP/1.1 401`.

**Respuesta:**

```json
{"mensaje":"No hay una sesión activa"}
```

La petición fue rechazada porque no existía una sesión autenticada.

## 5.2. Consulta sin autorización

**Comando:**

```bash
curl -i -b cookies.txt http://localhost:8081/obtenerSesiones/1
```

**Resultado observado:** `HTTP/1.1 403`.

**Respuesta:**

```json
{"mensaje":"No perteneces al equipo de esta tarea"}
```

La sesión estaba autenticada, pero el usuario no pertenecía al equipo de la tarea consultada.

## 5.3. Registro de una sesión Pomodoro

**Comando:**

```bash
curl -i -b cookies.txt -X POST http://localhost:8081/crearSesion/3 \\
  -H "Content-Type: application/json" \\
  -d '{
    "inicio": "2026-11-21T02:10:00",
    "fin": "2026-11-21T02:35:00",
    "duracionSegundos": 1500
  }'
```

**Resultado observado:** `HTTP/1.1 201 Created`.

**Respuesta:**

```json
{"idTarea":3,"id":1,"duracionSegundos":1500}
```

La sesión se registró con duración de 1500 segundos, equivalente a 25 minutos.

## 5.4. Consulta de la sesión registrada

**Comando:**

```bash
curl -i -b cookies.txt http://localhost:8081/obtenerSesiones/3
```

**Resultado observado:** `HTTP/1.1 200 OK`.

**Respuesta:**

```json
\[{"id":1,"idUsuario":1,"idTarea":3,"fin":"2026-11-21T02:35:00","inicio":"2026-11-21T02:10:00","duracionSegundos":1500}]
```

La respuesta confirmó que el registro guardado podía consultarse y que sus identificadores, intervalo y duración correspondían a la sesión creada.

\---

# 6\. Pruebas automatizadas complementarias

Además de las pruebas CURL, se implementaron pruebas automatizadas con JUnit y MockMvc:

* `SesionControladorTest`: 5 pruebas.
* `SesionServicioTest`: 11 pruebas.

También se ejecutó la suite general del backend:

**Resultado observado en Maven:** 44 pruebas ejecutadas, 0 fallos, 0 errores y 0 omitidas (`BUILD SUCCESS`).

Estas pruebas automatizadas son complementarias y no sustituyen las evidencias de ejecución CURL.

\---

# 7\. Pruebas pendientes

Quedan pendientes de ejecutar mediante CURL los siguientes escenarios negativos de `POST /crearSesion/{idTarea}`:

1. Intentar registrar una sesión en una tarea que no esté en estado `EN\_PROGRESO` (respuesta esperada según la lógica del servicio: `400`).
2. Enviar una duración que no coincida con el intervalo entre inicio y fin (`400`).
3. Intentar registrar una sesión en una tarea asignada a otro usuario (`403`).
4. Intentar registrar una sesión en un equipo archivado (`403`).

Estos escenarios no se reportan como pruebas CURL realizadas. Sus códigos indicados son los previstos por la lógica del servicio; deben confirmarse ejecutando los comandos y registrando las respuestas reales.



