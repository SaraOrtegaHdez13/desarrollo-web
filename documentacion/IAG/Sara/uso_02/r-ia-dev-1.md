# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 13/11/2026
**Número de reporte: 1**

\---

# 1\. Breve descripción de uso

Generar pruebas CURL del endpoint `POST /crearUsuario`, a partir de los archivos del controlador, servicio, modelo y repositorio de usuario. El objetivo fue verificar el registro de usuarios en tres casos: registro exitoso con datos válidos, campos obligatorios vacíos y correo ya registrado. Los comandos se ajustaron al puerto y a la contraseña de prueba del entorno local. 

\---

# 2\. Evidencias de uso

|Evidencia|Formato|
|-|-|
|Herramienta|Claude|
|Modelo/temperatura|Sonnet 5.5|
|Prompt original|[PROMT 1](#promt-1)|
|Respuesta de IA|[RESPUESTA A PROMT 1](#respuesta-promt-1)|
|Código anterior|No hay una generación de código como tal, dado que la respuesta de la IA solo devuelve los CURL a probar. Lo que si se hizo fue reemplazar los datos genéricos que dio la respuesta para el puerto y la clave.|
|Código posterior|No hay una generación de código como tal, dado que la respuesta de la IA solo devuelve los CURL a probar. Lo que si se hizo fue reemplazar los datos genéricos que dio la respuesta para el puerto y el password.|
|Diferencias entre versiones|Puerto 8080 se reemplaza por 8081<br />Password "Clave123" se remplaza por '14130606'|
|Prueba de funcionamiento|[PRUEBA 1. Registro exitoso con datos válidos](#prueba-1-registro-exitoso-con-datos-válidos)<br /><br />[RESPUESTA 1](#respuesta-1)<br /><br />[PRUEBA 2. Campos obligatorios vacíos](#prueba-2-campos-obligatorios-vacíos)<br /><br />[RESPUESTA 2](#respuesta-2)<br /><br />[PRUEBA 3. Correo que ya existe](#prueba-3-correo-que-ya-existe)<br /><br />[RESPUESTA 3](#respuesta-3)|
|Código de uso|Pruebas|
|Reflexión del estudiante|Las tres pruebas confirman que el endpoint funciona en lo esencial, devolviendo éxito (201) en la creación de usuario, campos vacíos (400) y correo duplicado (400).|



##### PROMT 1



Analiza los archivos adjuntos y genera únicamente estas 4 pruebas CURL para `POST /crearUsuario`:



1\. Registro exitoso con datos válidos.

2\. Registro con campos obligatorios vacíos.

3\. Registro con un correo que ya existe.



Para cada prueba incluye el comando CURL y el código HTTP esperado. Usa los nombres de campos y validaciones reales del código. No agregues pruebas adicionales ni modifiques archivos.



##### RESPUESTA PROMT 1



Los campos reales del @RequestBody (UsuarioRegistro) son nombre, correo y password. No sé en qué puerto corre tu app, así que usé 8080 (el predeterminado de Spring Boot). Cámbialo si usas otro.



Prueba 1: Registro exitoso con datos válidos



curl -i -X POST http://localhost:8080/crearUsuario \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"nombre":"Sara Ortega","correo":"sara@ejemplo.com","password":"Clave123"}'



Código esperado: 201 Created

Cuerpo: Usuario registrado correctamente



Prueba 2: Campos obligatorios vacíos



curl -i -X POST http://localhost:8080/crearUsuario \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"nombre":"","correo":"","password":""}'



Código esperado: 400 Bad Request

Cuerpo: Todos los campos son obligatorios



Esta validación está en registrarUsuario: lanza IllegalArgumentException si nombre o correo están nulos o en blanco, o si password está nulo o vacío. El controlador la captura y responde 400.



Prueba 3: Correo que ya existe



Ejecuta primero la Prueba 1 (para que sara@ejemplo.com ya esté guardado) y luego repite el mismo comando:



curl -i -X POST http://localhost:8080/crearUsuario \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"nombre":"Otra Persona","correo":"sara@ejemplo.com","password":"OtraClave456"}'



Código esperado: 400 Bad Request

Cuerpo: El correo ya está registrado



Si ejecutas las pruebas más de una vez, la Prueba 1 devolverá 400 (correo duplicado) en las siguientes ocasiones, porque el correo ya quedó guardado. Para repetirla, cambia el correo o borra el registro de la tabla usuarios.

\---



##### PRUEBA 1. Registro exitoso con datos válidos



curl -i -X POST http://localhost:8081/crearUsuario \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"nombre":"Sara Ortega","correo":"sara@ejemplo.com","password":"14130606"}'





##### RESPUESTA 1



HTTP/1.1 201

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 32

Date: Sat, 14 Nov 2026 02:50:37 GMT



Usuario registrado correctamente





##### PRUEBA 2. Campos obligatorios vacíos



curl -i -X POST http://localhost:8081/crearUsuario \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"nombre":"","correo":"","password":""}'



##### RESPUESTA 2



HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 33

Date: Sat, 14 Nov 2026 02:54:47 GMT

Connection: close



Todos los campos son obligatorios



##### PRUEBA 3. Correo que ya existe



curl -i -X POST http://localhost:8081/crearUsuario \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"nombre":"Sara Ortega","correo":"sara@ejemplo.com","password":"14130606"}'





##### RESPUESTA 3



HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 29

Date: Sat, 14 Nov 2026 02:55:53 GMT

Connection: close



El correo ya está registrado



\---

