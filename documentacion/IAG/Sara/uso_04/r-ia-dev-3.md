# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 13/11/2026
**Número de reporte: 3**

\---

# 1\. Breve descripción de uso

Generar pruebas CURL del endpoint `POST /cerrarSesion`, a partir de los archivos del controlador, servicio e interceptor de sesión. El objetivo es verificar que el sistema cierra correctamente la sesión cuando el usuario ya inició sesión, y observar cómo responde cuando se intenta cerrar sesión sin haber iniciado una.

\---

# 2\. Evidencias de uso

|Evidencia|Formato|
|-|-|
|Herramienta|Claude|
|Modelo/temperatura|Sonnet 5.5|
|Prompt original|[PROMT 1](#promt-1)|
|Respuesta de IA|[RESPUESTA A PROMT 1](#respuesta-promt-1)|
|Código anterior|No hay una generación de código como tal, dado que la respuesta de la IA solo devuelve los CURL a probar. Lo que si se hizo fue reemplazar los datos genéricos que dio la respuesta para el puerto y el password.|
|Código posterior|No hay una generación de código como tal, dado que la respuesta de la IA solo devuelve los CURL a probar. Lo que si se hizo fue reemplazar los datos genéricos que dio la respuesta para el puerto y el password.|
|Diferencias entre versiones|Puerto 8080 se reemplaza por 8081<br />Password "Clave123" se remplaza por '14130606'|
|Prueba de funcionamiento|[PRUEBA 1. Cerrar sesión con una sesión iniciada](#prueba-1-cerrar-sesión-con-una-sesión-iniciada)<br /><br />[RESPUESTA 1](#respuesta-1)<br /><br />[PRUEBA 2. Cerrar sesión sin haber iniciado sesión](#prueba-2-cerrar-sesión-sin-haber-iniciado-sesión)<br /><br />[RESPUESTA 2](#respuesta-2)|
|Código de uso|Pruebas|
|Reflexión del estudiante|Las pruebas confirman que el endpoint funciona. Con una sesión iniciada responde con un código 200 y el mensaje "Sesión cerrada correctamente". Sin haber iniciado sesión responde con un código 401 y el mensaje "No hay una sesión activa", porque el interceptor bloquea la petición.|



##### PROMT 1



Analiza los archivos adjuntos y genera únicamente estas pruebas CURL para `POST /cerrarSesion`:



1\. Cerrar sesión con una sesión previamente iniciada.

2\. Intentar cerrar sesión sin haber iniciado sesión.



Para cada prueba incluye:



\* Comando CURL completo.

\* Código HTTP esperado.

\* Breve explicación del resultado esperado.



Considera cómo se conserva la cookie de sesión entre peticiones. Usa exclusivamente las rutas y respuestas que aparecen en el código. No agregues pruebas adicionales ni modifiques archivos.





##### RESPUESTA PROMT 1

Asumo que la app corre en http://localhost:8080 (ajusta el puerto si es otro). Para conservar la cookie de sesión (JSESSIONID) entre peticiones, uso un archivo de cookies de curl: -c la guarda y -b la envía.



Prueba 1: Cerrar sesión con una sesión iniciada



Primero inicia sesión y guarda la cookie. Esto es solo el requisito previo, no una prueba adicional:



curl -i -c cookies.txt -X POST http://localhost:8080/iniciarSesion \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"usuario@ejemplo.com","password":"MiPassword123"}'



Luego la prueba, enviando esa cookie:



curl -i -b cookies.txt -X POST http://localhost:8080/cerrarSesion



Código HTTP esperado: 200 OK



Resultado esperado: el cuerpo es Sesión cerrada correctamente. La cookie enviada corresponde a una sesión que tiene usuarioId, así que SesionInterceptor la deja pasar. Después, session.invalidate() destruye la sesión en el servidor.



Prueba 2: Cerrar sesión sin haber iniciado sesión



Esta petición no envía cookie alguna (sin -b):



curl -i -X POST http://localhost:8080/cerrarSesion



Código HTTP esperado: depende de si SesionInterceptor está registrado para esta ruta. Esa configuración (normalmente una clase WebMvcConfigurer) no está en los archivos adjuntos, así que no puedo confirmarlo.



Si el interceptor protege /cerrarSesion: responde 401 Unauthorized con el cuerpo {"mensaje":"No hay una sesión activa"}. solicitud.getSession(false) devuelve null porque no hay cookie, así que el interceptor corta la petición y no llega al controlador.

Si el interceptor no se aplica a esa ruta (por ejemplo, está excluida): responde 200 OK con el cuerpo Sesión cerrada correctamente. El parámetro HttpSession session hace que Spring cree una sesión nueva vacía, y invalidate() la destruye sin error.



Para saber cuál de los dos casos te va a salir, revisa si en tu clase de configuración /cerrarSesion aparece en addPathPatterns(...) o en excludePathPatterns(...).



\---



##### PRUEBA 1. Cerrar sesión con una sesión iniciada



curl -i -c cookies.txt -X POST http://localhost:8081/iniciarSesion \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"sara@ejemplo.com","password":'14130606'}'



curl -i -b cookies.txt -X POST http://localhost:8081/cerrarSesion



##### RESPUESTA 1



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 29

Date: Sat, 14 Nov 2026 03:40:55 GMT



Sesión cerrada correctamente



##### PRUEBA 2. Cerrar sesión sin haber iniciado sesión



curl -i -X POST http://localhost:8081/cerrarSesion



##### RESPUESTA 2



HTTP/1.1 401

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json;charset=UTF-8

Content-Length: 39

Date: Sat, 14 Nov 2026 03:41:22 GMT



{"mensaje":"No hay una sesión activa"}







\---

