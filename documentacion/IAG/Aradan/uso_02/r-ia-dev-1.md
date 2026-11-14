# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 13/11/2026
**Número de reporte: 1**

\---

# 1\. Breve descripción de uso

Generar y ejecutar pruebas CURL para verificar la configuración CORS del backend, comprobando el comportamiento ante un origen permitido y uno no autorizado, así como identificar los encabezados HTTP devueltos por el servidor.

\---

# 2\. Evidencias de uso

|Evidencia|Formato|
|-|-|
|Herramienta|Claude|
|Modelo/temperatura|Sonnet 5.5|
|Prompt original|[PROMT 1](#promt-1)|
|Respuesta de IA|[RESPUESTA A PROMT 1](#respuesta-promt-1)|
|Código anterior|No hay una generación de código como tal, dado que la respuesta de la IA solo devuelve los CURL a probar.|
|Código posterior|No hay una generación de código como tal, dado que la respuesta de la IA solo devuelve los CURL a probar.|
|Diferencias entre versiones|No aplica|
|Prueba de funcionamiento|[PRUEBA 1. Origen permitido](#prueba-1-origen-permitido)<br /><br />[RESPUESTA 1](#respuesta-1)<br /><br />[PRUEBA 2. Origen no autorizado](#prueba-2-origen-no-autorizado)<br /><br />[RESPUESTA 2](#respuesta-2)|
|Código de uso|Pruebas|
|Reflexión del estudiante|Las pruebas permitieron comprobar que el servidor reconoce el origen autorizado http://localhost:5500 y devuelve los encabezados CORS configurados. Sin embargo, la solicitud OPTIONS recibió un código 401 porque el interceptor exige una sesión activa. Al probar un origen no autorizado, el servidor respondió con 403 y el mensaje "Invalid CORS request". Esto permitió identificar que es necesario revisar el tratamiento de las solicitudes OPTIONS en el interceptor. También se identificó la diferencia entre permitir un origen mediante CORS y autorizar una petición mediante el control de sesiones.|



##### PROMT 1



Analiza los archivos adjuntos y ayúdame a realizar pruebas CURL de CORS y a verificar la conexión con MySQL en mi proyecto Spring Boot.

No modifiques código ni inventes endpoints. Usa las rutas y configuraciones reales de los archivos.

Dame los comandos CURL para probar el origen permitido `http://localhost:5500` y uno no permitido. Explica brevemente qué resultado y encabezados debo revisar.

Para MySQL, indica cómo iniciar la aplicación desde WSL y qué mensajes debo comprobar para confirmar la conexión.

Incluye los resultados esperados, pero distingue claramente lo esperado de lo que todavía debo ejecutar.



##### RESPUESTA PROMT 1

Para la prueba de preflight el comando es:



curl -i -X OPTIONS http://localhost:8081/obtenerEquipos \\

\-H "Origin: http://localhost:5500" \\

\-H "Access-Control-Request-Method: GET"



Después ejecuta la prueba desde un origen no autorizado:



curl -i -X OPTIONS http://localhost:8081/obtenerEquipos \\

\-H "Origin: http://localhost:9999" \\

\-H "Access-Control-Request-Method: GET"



El segundo comando debe permitir comprobar que el servidor no autoriza ese origen. No basta con que responda algún código HTTP: hay que revisar los encabezados Access-Control-Allow-Origin y Access-Control-Allow-Credentials.





\---



##### PRUEBA 1. Origen permitido



curl -i -X OPTIONS http://localhost:8081/obtenerEquipos \\

\-H "Origin: http://localhost:5500" \\

\-H "Access-Control-Request-Method: GET"



##### RESPUESTA 1



HTTP/1.1 401

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Access-Control-Allow-Origin: http://localhost:5500

Access-Control-Allow-Methods: GET,POST,PUT,DELETE,OPTIONS

Access-Control-Allow-Credentials: true

Access-Control-Max-Age: 1800

Allow: GET, HEAD, POST, PUT, DELETE, OPTIONS, PATCH

Content-Type: application/json;charset=UTF-8

Content-Length: 39

Date: Sat, 14 Nov 2026 04:55:05 GMT



{"mensaje":"No hay una sesión activa"}





##### PRUEBA 2. Origen no autorizado



curl -i -X OPTIONS http://localhost:8081/obtenerEquipos \\

\-H "Origin: http://localhost:9999" \\

\-H "Access-Control-Request-Method: GET"



##### RESPUESTA 2



HTTP/1.1 403

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Transfer-Encoding: chunked

Date: Sat, 14 Nov 2026 04:56:19 GMT



Invalid CORS request



\---

