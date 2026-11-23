# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 22/11/2026  
**Número de reporte:** 09

\---

## 1\. Breve descripción de uso

Durante una prueba manual del tablero de LuzioTeamTime observé que pude guardar la tarea «Revisar épicas» con fecha límite de 2015. Consulté a ChatGPT para localizar la validación en el formulario y el servidor y definir cómo comprobar que al crear tareas se acepte hoy o una fecha posterior.

\---

## 2\. Evidencias de uso

|Evidencia|Formato|
|-|-|
|**Herramienta**|ChatGPT (OpenAI)|
|**Modelo/temperatura**|GPT-6.2 / temperatura no disponible|
|**Prompt original**|[PROMPT 1](#prompt-1)|
|**Respuesta de IA**|[RESPUESTA PROMPT 1](#respuesta-prompt-1)|
|**Código anterior**|El formulario exigía una fecha válida y el servidor la recibía como `LocalDate`. El flujo de creación carecía de una comparación con el día actual.|
|**Código posterior**|Apliqué la validación de fecha mínima en el formulario y en el servidor.|
|**Diferencias entre versiones**|El formulario establece el mínimo al día actual y el servidor rechaza fechas anteriores antes del guardado. Hoy sigue siendo válido.|
|**Prueba de funcionamiento**|[PRUEBA 1. Validación de la fecha límite al crear tareas](#prueba-1-validación-de-la-fecha-límite-al-crear-tareas)<br><br>[RESPUESTA 1](#respuesta-1)|
|**Código de uso**|Desarrollo y pruebas|
|**Reflexión del estudiante**|Yo detecté el problema en el tablero, definí la regla y apliqué el cambio. GPT-6.2 me ayudó a localizar la validación pendiente y a proponer casos para comprobar la corrección. Tras ejecutar las pruebas, los resultados observados fueron satisfactorios y coincidieron exactamente con los resultados esperados.|

### PROMPT 1

> Mientras probaba el tablero de LuzioTeamTime, observé que pude guardar la tarea «Revisar épicas» con una fecha límite de 2015. Quiero que al crear una tarea solo se acepten fechas límite a partir de hoy, incluido el día actual.
>
> Revisa el código del formulario y del servidor. Explica qué validaciones existen, en qué archivo y método se aplican y qué pudo permitir que se guardara esa fecha. Si la versión adjunta ya incluye la corrección, señala cómo funciona. Propón cambios únicamente donde detectes que falta una validación.
>
> Dame tres casos de prueba con su resultado esperado: fecha de ayer, de hoy y de mañana. Indica cuáles son conclusiones de la lectura del código y cuáles requieren ejecutar el sistema. Deja el resultado observado como pendiente si no se ejecutaron las pruebas.

### RESPUESTA PROMPT 1

**Resumen de la respuesta de GPT-6.2.** La versión adjunta permitía guardar fechas límite pasadas. En `frontend/tablero.html`, la línea 143 usa `type="date"` y `required` en `#fecha-limite`. El manejador `submit` de `frontend/js/tablero.js` envía `fechaLimite` a `POST /crearTarea/{idTablero}`. `TareaRegistro.java` recibe el campo como `LocalDate`, `TareaControlador.crearTarea()` lo pasa al servicio y `TareaServicio.crearTarea()` lo guarda sin compararlo con hoy.

GPT-6.2 propuso aplicar un mínimo dinámico al campo de fecha, comprobarlo al enviar el formulario y validar la fecha en el servicio. También sugirió rechazar fechas omitidas o anteriores a hoy y aprovechar el manejo existente de `IllegalArgumentException` para devolver HTTP `400`. Propuso probar ayer, hoy y mañana y dejó los resultados observados como pendientes.

\---

### PRUEBA 1. Validación de la fecha límite al crear tareas

**Precondiciones.** Usar un tablero activo, una sesión de líder, un responsable elegible y datos válidos en los otros campos. Tomar ayer, hoy y mañana con la misma referencia de día en el navegador y el servidor.

1. Intentar crear una tarea con fecha de ayer desde el formulario y comprobar también la validación del servidor mediante `POST /crearTarea/{idTablero}`.
2. Crear una tarea con fecha de hoy.
3. Crear una tarea con fecha de mañana.

### RESPUESTA 1

|Fecha límite|Resultado esperado con el cambio aplicado|Resultado observado|
|-|-|-|
|**Ayer**|El formulario bloquea el envío. Por API se espera HTTP `400` y que la tarea no se guarde.|**Satisfactorio.** Coincide con lo esperado: el formulario bloqueó el envío y la API devolvió HTTP `400` sin guardar la tarea.|
|**Hoy**|HTTP `201` y tarea guardada.|**Satisfactorio.** Coincide con lo esperado: se obtuvo HTTP `201` y la tarea se guardó correctamente.|
|**Mañana**|HTTP `201` y tarea guardada.|**Satisfactorio.** Coincide con lo esperado: se obtuvo HTTP `201` y la tarea se guardó correctamente.|

\---

