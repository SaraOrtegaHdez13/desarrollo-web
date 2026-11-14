# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 13/11/2026
**Número de reporte: 2**

\---

# 1\. Breve descripción de uso

Generar pruebas CURL del endpoint `POST /iniciarSesion`, a partir de los archivos del controlador, servicio, modelo y repositorio de usuario. El objetivo es verificar que el sistema rechace el acceso cuando el correo existe pero la contraseña no coincide con el hash guardado. 

\---

# 2\. Evidencias de uso

|Evidencia|Formato|
|-|-|
|Herramienta|Claude|
|Modelo/temperatura|Sonnet 5.5|
|Prompt original|[PROMT 1](#promt-1)|
|Respuesta de IA|[RESPUESTA A PROMT 1](#respuesta-promt-1)|
|Código anterior|No hay una generación de código como tal, dado que la respuesta de la IA solo devuelve el CURL a probar. |
|Código posterior|No hay una generación de código como tal, dado que la respuesta de la IA solo devuelve el CURL a probar.|
|Diferencias entre versiones|No aplica|
|Prueba de funcionamiento|[PRUEBA 1. Registro exitoso con datos válidos](#prueba-1-registro-exitoso-con-datos-válidos)<br /><br />[RESPUESTA 1](#respuesta-1)|
|Código de uso|Pruebas|
|Reflexión del estudiante|La prueba confirma que el endpoint funciona, rechazando el acceso con un código 401 y el mensaje "Correo o contraseña incorrectos" cuando la contraseña no coincide con la del usuario registrado.|



##### PROMT 1



Analiza los archivos adjuntos y genera únicamente 1 prueba CURL para POST /iniciarSesion:



Inicio de sesión con contraseña incorrecta.



Incluye:

Comando CURL completo.

Código HTTP esperado.

Breve explicación del resultado esperado.

Usa exclusivamente los nombres de campos, rutas y validaciones que aparecen en el código. No inventes validaciones ni respuestas. No agregues pruebas adicionales ni modifiques archivos.



##### RESPUESTA PROMT 1



Prueba: Contraseña incorrecta



curl -i -X POST http://localhost:8081/iniciarSesion \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"sara@ejemplo.com","password":"contraseñaIncorrecta"}'



Código esperado: 401 Unauthorized

Cuerpo: Correo o contraseña incorrectos



El correo existe, pero matches falla, así que validarCredenciales devuelve null y el controlador responde 401.



\---



##### PRUEBA 1. Contraseña incorrecta



curl -i -X POST http://localhost:8081/iniciarSesion \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"sara@ejemplo.com","password":"contraseñaIncorrecta"}'



##### RESPUESTA 1



HTTP/1.1 401

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Set-Cookie: JSESSIONID=09E4110AB5B9EFFA49F5E65217AB20E4; Path=/; HttpOnly

Content-Type: text/plain;charset=UTF-8

Content-Length: 32

Date: Sat, 14 Nov 2026 03:28:49 GMT



Correo o contraseña incorrectos



\---

