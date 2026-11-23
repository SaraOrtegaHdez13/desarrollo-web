# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 22/11/2026
**Número de reporte:**

\---

# 1\. Breve descripción de uso

Se utilizó ChatGPT para proponer y guiar 30 pruebas CURL del módulo de tableros y tareas, indicando los comandos y resultados HTTP esperados. Las pruebas se ejecutaron en WSL y sus respuestas reales se documentaron para comprobar el funcionamiento del sistema.

\---

# 2\. Evidencias de uso

|Evidencia|Formato|
|-|-|
|Herramienta|ChatGPT|
|Modelo/temperatura|GPT-6. La temperatura no se configura ni se muestra en esta conversación.|
|Prompt original|[PROMT 1](#promt-1)|
|Respuesta de IA|[RESPUESTA A PROMT 1](#respuesta-promt-1)|
|Código anterior|No aplica. En esta actividad no se modificó el código fuente; se diseñaron y ejecutaron pruebas de funcionamiento.|
|Código posterior|No aplica. Se conservaron los archivos del proyecto y se utilizaron comandos CURL desde la terminal.|
|Diferencias entre versiones|No aplica. El objetivo fue comprobar el comportamiento del módulo mediante pruebas, no generar una nueva versión del código.|
|Prueba de funcionamiento|[PRUEBA 1](#prueba-1-crear-un-tablero-correctamente) · [RESPUESTA 1](#resultado-obtenido-prueba-1)<br>[PRUEBA 2](#prueba-2-intentar-crear-un-tablero-como-colaborador) · [RESPUESTA 2](#resultado-obtenido-prueba-2)<br>[PRUEBA 3](#prueba-3-crear-tablero-con-nombre-vacio) · [RESPUESTA 3](#resultado-obtenido-prueba-3)<br>[PRUEBA 4](#prueba-4-consultar-los-tableros-de-un-equipo) · [RESPUESTA 4](#resultado-obtenido-prueba-4)<br>[PRUEBA 5](#prueba-5-consultar-tableros-de-un-equipo-al-que-no-pertenece) · [RESPUESTA 5](#resultado-obtenido-prueba-5)<br>[PRUEBA 6](#prueba-6-consultar-un-tablero-por-su-id) · [RESPUESTA 6](#resultado-obtenido-prueba-6)<br>[PRUEBA 7](#prueba-7-consultar-un-tablero-inexistente) · [RESPUESTA 7](#resultado-obtenido-prueba-7)<br>[PRUEBA 8](#prueba-8-crear-una-tarea-como-lider) · [RESPUESTA 8](#resultado-obtenido-prueba-8)<br>[PRUEBA 9](#prueba-9-intentar-crear-una-tarea-como-colaborador) · [RESPUESTA 9](#resultado-obtenido-prueba-9)<br>[PRUEBA 10](#prueba-10-crear-tarea-con-datos-invalidos) · [RESPUESTA 10](#resultado-obtenido-prueba-10)<br>[PRUEBA 11](#prueba-11-consultar-las-tareas-de-un-tablero) · [RESPUESTA 11](#resultado-obtenido-prueba-11)<br>[PRUEBA 12](#prueba-12-consultar-tareas-aplicando-filtros) · [RESPUESTA 12](#resultado-obtenido-prueba-12)<br>[PRUEBA 13](#prueba-13-consultar-tareas-con-un-estado-invalido) · [RESPUESTA 13](#resultado-obtenido-prueba-13)<br>[PRUEBA 14](#prueba-14-consultar-el-detalle-de-una-tarea) · [RESPUESTA 14](#resultado-obtenido-prueba-14)<br>[PRUEBA 15](#prueba-15-consultar-una-tarea-inexistente) · [RESPUESTA 15](#resultado-obtenido-prueba-15)<br>[PRUEBA 16](#prueba-16-modificar-una-tarea-permitida) · [RESPUESTA 16](#resultado-obtenido-prueba-16)<br>[PRUEBA 17](#prueba-17-intentar-modificar-una-tarea-como-colaborador) · [RESPUESTA 17](#resultado-obtenido-prueba-17)<br>[PRUEBA 18](#prueba-18-intentar-modificar-una-tarea-en-revision-o-terminada) · [RESPUESTA 18](#resultado-obtenido-prueba-18)<br>[PRUEBA 19](#prueba-19-iniciar-una-tara-que-no-esta-asignada-al-colaborador) · [RESPUESTA 19](#resultado-obtenido-prueba-19)<br>[PRUEBA 20](#prueba-20-intentar-eliminar-una-tarea-como-colaborador) · [RESPUESTA 20](#resultado-obtenido-prueba-20)<br>[PRUEBA 21](#prueba-21-eliminar-una-tarea-con-un-estado-que-no-lo-permite) · [RESPUESTA 21](#resultado-obtenido-prueba-21)<br>[PRUEBA 22](#prueba-22-iniciar-una-tarea-propia) · [RESPUESTA 22](#resultado-obtenido-prueba-22)<br>[PRUEBA 23](#prueba-23-intentar-iniciar-una-tarea-asignada-a-otro-integrante) · [RESPUESTA 23](#resultado-obtenido-prueba-23)<br>[PRUEBA 24](#prueba-24-intentar-iniciar-una-tarea-que-no-esta-en-por_hacer) · [RESPUESTA 24](#resultado-obtenido-prueba-24)<br>[PRUEBA 25](#prueba-25-enviar-una-tarea-propia-a-revision) · [RESPUESTA 25](#resultado-obtenido-prueba-25)<br>[PRUEBA 26](#prueba-26-intentar-enviar-a-revision-una-tarea-ajena) · [RESPUESTA 26](#resultado-obtenido-prueba-26)<br>[PRUEBA 27](#prueba-27-intentar-enviar-a-revision-una-tarea-en-un-estado-incorrecto) · [RESPUESTA 27](#resultado-obtenido-prueba-27)<br>[PRUEBA 28](#prueba-28-aprobar-una-tarea-enviada-a-revision-como-lider) · [RESPUESTA 28](#resultado-obtenido-prueba-28)<br>[PRUEBA 29](#prueba-29-intentar-aprobar-una-tarea-como-colaborador) · [RESPUESTA 29](#resultado-obtenido-prueba-29)<br>[PRUEBA 30](#prueba-30-rechazar-una-tarea-como-lider) · [RESPUESTA 30](#resultado-obtenido-prueba-30)|
|Código de uso|Pruebas|
|Reflexión del estudiante|La IA me ayudó a organizar las pruebas CURL y conocer las respuestas esperadas. Al ejecutarlas, pude comprobar el funcionamiento de los endpoints y comprender mejor los permisos, las validaciones y los cambios de estado de las tareas.|



##### PROMT 1



Estoy desarrollando el módulo de tableros y tareas de un proyecto de gestión colaborativa de tareas con Spring Boot y MySQL.



Necesito realizar pruebas de funcionamiento utilizando CURL desde la terminal de WSL. Quiero comprobar que los endpoints funcionen correctamente tanto en casos exitosos como en casos de error.



Ayúdame a diseñar y ejecutar las pruebas paso a paso. Para cada prueba, explica brevemente su objetivo, proporciona el comando CURL completo y señala cuál es el código HTTP y el mensaje o respuesta que debería obtener.



Es importante que las pruebas respeten el funcionamiento real del proyecto: inicio de sesión mediante HttpSession, uso de cookies para conservar la sesión y permisos según el rol del integrante en el equipo. También deben comprobarse las restricciones relacionadas con el responsable de cada tarea y sus estados.



No me des por aprobada una prueba hasta que te comparta la respuesta real de mi terminal. Necesito ejecutar personalmente cada comando y comprobar sus resultados.



##### RESPUESTA PROMT 1



Para comprobar el funcionamiento de los módulos de tableros y tareas, realizaremos pruebas CURL directamente desde la terminal de WSL.



Las pruebas permitirán verificar las siguientes operaciones:



Tableros: creación y consulta de tableros, validación de nombres y permisos según el rol del integrante.



Tareas: creación, consulta, filtrado, modificación y eliminación de tareas.



Estados y asignaciones: inicio de tareas propias, restricciones para tareas ajenas y validación de los estados permitidos.



Revisión de entregas: envío de tareas a revisión, aprobación por parte del líder y rechazo con una observación.



Permisos y validaciones: comprobación de respuestas ante usuarios sin autorización, datos inválidos, estados incorrectos e identificadores inexistentes.



Para cada caso proporcionaré un comando CURL listo para ejecutar y señalaré el código HTTP y la respuesta que debería obtenerse.



Comenzaremos con las pruebas de tableros y continuaremos con las operaciones de tareas. Después de cada ejecución, revisaremos la respuesta real de la terminal para determinar si el resultado coincide con el esperado.



\---



##### PRUEBA 1. CREAR UN TABLERO CORRECTAMENTE



curl -i -b cookies-sara.txt \\

\-X POST http://localhost:8081/crearTablero/52 \\

\-H "Content-Type: application/json" \\

\-d '{"nombre":"Tablero de pruebas CURL Lourdes"}'



##### PRUEBA 2. INTENTAR CREAR UN TABLERO COMO COLABORADOR



curl -i -b cookies-tercero.txt \\

\-X POST http://localhost:8081/crearTablero/52 \\

\-H "Content-Type: application/json" \\

\-d '{

&#x20;   "nombre": "Tablero no autorizado del colaborador"

}'



##### PRUEBA 3. CREAR TABLERO CON NOMBRE VACÍO



curl -i -b cookies-sara.txt \\

\-X POST http://localhost:8081/crearTablero/52 \\

\-H "Content-Type: application/json" \\

\-d '{

&#x20;   "nombre": ""

}'



##### PRUEBA 4. CONSULTAR LOS TABLEROS DE UN EQUIPO



curl -i -b cookies-sara.txt \\

\-X GET http://localhost:8081/obtenerTableros/52



##### PRUEBA 5. CONSULTAR TABLEROS DE UN EQUIPO AL QUE NO PERTENECE



curl -i -b cookies-externo.txt \\

\-X GET http://localhost:8081/obtenerTableros/52



##### PRUEBA 6. CONSULTAR UN TABLERO POR SU ID



curl -i -b cookies-sara.txt \\

\-X GET http://localhost:8081/obtenerTablero/52



##### PRUEBA 7. CONSULTAR UN TABLERO INEXISTENTE



curl -i -b cookies-sara.txt \\

\-X GET http://localhost:8081/obtenerTablero/999999



##### PRUEBA 8. CREAR UNA TAREA COMO LÍDER



curl -i -b cookies-sara.txt \\

\-X POST http://localhost:8081/crearTarea/52 \\

\-H "Content-Type: application/json" \\

\-d '{

&#x20;   "idResponsable": 304,

&#x20;   "titulo": "Tarea de prueba CURL Lourdes",

&#x20;   "descripcion": "Tarea creada para comprobar el funcionamiento del módulo.",

&#x20;   "fechaLimite": null

}'



##### PRUEBA 9. INTENTAR CREAR UNA TAREA COMO COLABORADOR



curl -i -b cookies-tercero.txt \\

\-X POST http://localhost:8081/crearTarea/52 \\

\-H "Content-Type: application/json" \\

\-d '{

&#x20;   "idResponsable": 304,

&#x20;   "titulo": "Tarea no autorizada",

&#x20;   "descripcion": "Intento de creación por un colaborador.",

&#x20;   "fechaLimite": null

}'



##### PRUEBA 10. CREAR TAREA CON DATOS INVÁLIDOS



curl -i -b cookies-sara.txt \\

\-X POST http://localhost:8081/crearTarea/52 \\

\-H "Content-Type: application/json" \\

\-d '{

&#x20;   "idResponsable": 304,

&#x20;   "titulo": "",

&#x20;   "descripcion": "Tarea de prueba con título vacío.",

&#x20;   "fechaLimite": null

}'



##### PRUEBA 11. CONSULTAR LAS TAREAS DE UN TABLERO



curl -i -b cookies-sara.txt \\

\-X GET http://localhost:8081/obtenerTareas/52



##### PRUEBA 12. CONSULTAR TAREAS APLICANDO FILTROS



curl -i -b cookies-sara.txt \\

\-X GET "http://localhost:8081/obtenerTareas/52?estado=POR\_HACER"



##### PRUEBA 13. CONSULTAR TAREAS CON UN ESTADO INVÁLIDO



curl -i -b cookies-sara.txt \\

\-X GET "http://localhost:8081/obtenerTareas/52?estado=INVALIDO"



##### PRUEBA 14. CONSULTAR EL DETALLE DE UNA TAREA



curl -i -b cookies-sara.txt \\

\-X GET http://localhost:8081/obtenerTarea/52



##### PRUEBA 15. CONSULTAR UNA TAREA INEXISTENTE 



curl -i -b cookies-sara.txt \\

\-X GET http://localhost:8081/obtenerTarea/999999



##### PRUEBA 16. MODIFICAR UNA TAREA PERMITIDA



curl -i -b cookies-sara.txt \\

\-X PUT http://localhost:8081/modificarTarea/52 \\

\-H "Content-Type: application/json" \\

\-d '{

&#x20;   "titulo": "Tarea modificada CURL Lourdes",

&#x20;   "descripcion": "Descripción actualizada mediante prueba CURL.",

&#x20;   "fechaLimite": null

}'





##### PRUEBA 17. INTENTAR MODIFICAR UNA TAREA COMO COLABORADOR



curl -i -b cookies-tercero.txt \\

\-X PUT http://localhost:8081/modificarTarea/52 \\

\-H "Content-Type: application/json" \\

\-d '{

&#x20;   "titulo": "Modificación no autorizada",

&#x20;   "descripcion": "Intento de modificación por un colaborador.",

&#x20;   "fechaLimite": null

}'



##### PRUEBA 18. INTENTAR MODIFICAR UNA TAREA EN REVISIÓN O TERMINADA



curl -i -b cookies-sara.txt \\

\-X PUT http://localhost:8081/modificarTarea/53 \\

\-H "Content-Type: application/json" \\

\-d '{

&#x20;   "titulo": "Intento de modificación durante revisión",

&#x20;   "descripcion": "Esta modificación no debería permitirse.",

&#x20;   "fechaLimite": null

}'



##### PRUEBA 19. INICIAR UNA TARA QUE NO ESTÁ ASIGNADA AL COLABORADOR



curl -i -b cookies-tercero.txt \\

\-X PUT http://localhost:8081/iniciarTarea/102



##### PRUEBA 20. INTENTAR ELIMINAR UNA TAREA COMO COLABORADOR



curl -i -b cookies-tercero.txt \\

\-X DELETE http://localhost:8081/eliminarTarea/102



##### PRUEBA 21. ELIMINAR UNA TAREA CON UN ESTADO QUE NO LO PERMITE



curl -i -b cookies-sara.txt \\

\-X DELETE http://localhost:8081/eliminarTarea/53



##### PRUEBA 22. INICIAR UNA TAREA PROPIA



curl -i -b cookies-tercero.txt \\

\-X PUT http://localhost:8081/iniciarTarea/52



##### PRUEBA 23. INTENTAR INICIAR UNA TAREA ASIGNADA A OTRO INTEGRANTE



curl -i -b cookies-tercero.txt \\

\-X PUT http://localhost:8081/iniciarTarea/52



##### PRUEBA 24. INTENTAR INICIAR UNA TAREA QUE NO ESTÁ EN POR\_HACER



curl -i -b cookies-tercero.txt \\

\-X PUT http://localhost:8081/iniciarTarea/52



##### PRUEBA 25. ENVIAR UNA TAREA PROPIA A REVISIÓN



curl -i -b cookies-tercero.txt \\

\-X PUT http://localhost:8081/enviarTareaRevision/52

##### 

##### PRUEBA 26. INTENTAR ENVIAR A REVISIÓN UNA TAREA AJENA



curl -i -b cookies-tercero.txt \\

\-X PUT http://localhost:8081/enviarTareaRevision/102



##### PRUEBA 27. INTENTAR ENVIAR A REVISIÓN UNA TAREA EN UN ESTADO INCORRECTO



curl -i -b cookies-tercero.txt \\

\-X PUT http://localhost:8081/enviarTareaRevision/52



##### PRUEBA 28. APROBAR UNA TAREA ENVIADA A REVISIÓN COMO LÍDER



curl -i -b cookies-sara.txt \\

\-X PUT http://localhost:8081/aprobarTarea/52



##### PRUEBA 29. INTENTAR APROBAR UNA TAREA COMO COLABORADOR



curl -i -b cookies-tercero.txt \\

\-X PUT http://localhost:8081/aprobarTarea/52



##### PRUEBA 30. RECHAZAR UNA TAREA COMO LÍDER



curl -i -b cookies-sara.txt \\

\-X PUT http://localhost:8081/rechazarTarea/103 \\

\-H "Content-Type: application/json" \\

\-d '{

&#x20;   "observacion": "La entrega necesita correcciones."

}'



\---

##### RESULTADO OBTENIDO PRUEBA 1



curl -i -b cookies-sara.txt \\

\-X POST http://localhost:8081/crearTablero/52 \\

\-H "Content-Type: application/json" \\

\-d '{"nombre":"Tablero de pruebas CURL Lourdes"}'



HTTP/1.1 201

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 02:16:55 GMT



{"id":52,"idEquipo":52,"nombre":"Tablero de pruebas CURL Lourdes"}



##### RESULTADO OBTENIDO PRUEBA 2



curl -i -b cookies-tercero.txt \\

\-X POST http://localhost:8081/crearTablero/52 \\

\-H "Content-Type: application/json" \\

\-d '{

&#x20;   "nombre": "Tablero no autorizado del colaborador"

}'



HTTP/1.1 403

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 35

Date: Mon, 23 Nov 2026 02:20:13 GMT



Solo un líder puede crear tableros



##### RESULTADO OBTENIDO PRUEBA 3



HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 36

Date: Mon, 23 Nov 2026 02:20:57 GMT

Connection: close



El nombre del tablero es obligatorio



##### RESULTADO OBTENIDO PRUEBA 4



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 02:21:46 GMT



\[{"id":52,"idEquipo":52,"nombre":"Tablero de pruebas CURL Lourdes"},{"id":53,"idEquipo":52,"nombre":"Tablero no autorizado de Sofía"}]



##### RESULTADO OBTENIDO PRUEBA 5



HTTP/1.1 403

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 30

Date: Mon, 23 Nov 2026 02:23:13 GMT



No tienes acceso a este equipo



##### RESULTADO OBTENIDO PRUEBA 6



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 02:23:57 GMT



{"id":52,"idEquipo":52,"nombre":"Tablero de pruebas CURL Lourdes"}



##### RESULTADO OBTENIDO PRUEBA 7



HTTP/1.1 404

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Length: 0

Date: Mon, 23 Nov 2026 02:24:31 GMT



##### RESULTADO OBTENIDO PRUEBA 8



HTTP/1.1 201

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 23

Date: Mon, 23 Nov 2026 02:25:20 GMT



Tarea creada con ID: 52



##### RESULTADO OBTENIDO PRUEBA 9



HTTP/1.1 403

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 33

Date: Mon, 23 Nov 2026 02:35:21 GMT



Solo un líder puede crear tareas



##### RESULTADO OBTENIDO PRUEBA 10



HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 25

Date: Mon, 23 Nov 2026 02:35:51 GMT

Connection: close



El título es obligatorio



##### RESULTADO OBTENIDO PRUEBA 11



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 02:36:29 GMT



\[{"descripcion":"Tarea creada para comprobar el funcionamiento del módulo.","estado":"POR\_HACER","fechaCompletada":null,"fechaLimite":null,"id":52,"idResponsable":304,"idTablero":52,"observacion":null,"titulo":"Tarea de prueba CURL Lourdes"}]



##### RESULTADO OBTENIDO PRUEBA 12



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 02:37:05 GMT



\[{"descripcion":"Tarea creada para comprobar el funcionamiento del módulo.","estado":"POR\_HACER","fechaCompletada":null,"fechaLimite":null,"id":52,"idResponsable":304,"idTablero":52,"observacion":null,"titulo":"Tarea de prueba CURL Lourdes"}]



##### RESULTADO OBTENIDO PRUEBA 13



HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 32

Date: Mon, 23 Nov 2026 02:37:37 GMT

Connection: close



El estado indicado no es válido



##### RESULTADO OBTENIDO PRUEBA 14



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 02:38:11 GMT



{"revisiones":\[],"asignaciones":\[{"idUsuario":304,"nombre":"Tercer Integrante","fechaInicio":"2026-11-22T20:25:20.700894","fechaFin":null,"estadoAlRecibir":"POR\_HACER","estadoAlSalir":null}],"descripcion":"Tarea creada para comprobar el funcionamiento del módulo.","estado":"POR\_HACER","fechaCompletada":null,"fechaLimite":null,"id":52,"idResponsable":304,"idTablero":52,"observacion":null,"titulo":"Tarea de prueba CURL Lourdes"}



##### RESULTADO OBTENIDO PRUEBA 15



HTTP/1.1 404

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Length: 0

Date: Mon, 23 Nov 2026 02:38:54 GMT



##### RESULTADO OBTENIDO PRUEBA 16



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 02:39:36 GMT



{"descripcion":"Descripción actualizada mediante prueba CURL.","estado":"POR\_HACER","fechaCompletada":null,"fechaLimite":null,"id":52,"idResponsable":304,"idTablero":52,"observacion":null,"titulo":"Tarea modificada CURL Lourdes"}



##### RESULTADO OBTENIDO PRUEBA 17



HTTP/1.1 403

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 37

Date: Mon, 23 Nov 2026 02:40:14 GMT



Solo un líder puede modificar tareas



##### RESULTADO OBTENIDO PRUEBA 18



HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 56

Date: Mon, 23 Nov 2026 03:06:40 GMT

Connection: close



No se puede modificar una tarea en revisión o terminada



##### RESULTADO OBTENIDO PRUEBA 19



HTTP/1.1 403

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 44

Date: Mon, 23 Nov 2026 03:09:13 GMT



Solo el responsable puede iniciar esta tarea



##### RESULTADO OBTENIDO PRUEBA 20



HTTP/1.1 403

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 36

Date: Mon, 23 Nov 2026 03:10:57 GMT



Solo un líder puede eliminar tareas



##### RESULTADO OBTENIDO PRUEBA 21



\-X DELETE http://localhost:8081/eliminarTarea/53

HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 40

Date: Mon, 23 Nov 2026 03:12:00 GMT

Connection: close



Solo se pueden eliminar tareas POR\_HACER



##### RESULTADO OBTENIDO PRUEBA 22



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 03:12:33 GMT



{"descripcion":"Descripción actualizada mediante prueba CURL.","estado":"EN\_PROGRESO","fechaCompletada":null,"fechaLimite":null,"id":52,"idResponsable":304,"idTablero":52,"observacion":null,"titulo":"Tarea modificada CURL Lourdes"}



##### RESULTADO OBTENIDO PRUEBA 23



\-X PUT http://localhost:8081/iniciarTarea/52

HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 49

Date: Mon, 23 Nov 2026 03:13:14 GMT

Connection: close



Solo se pueden iniciar tareas en estado POR\_HACER



##### RESULTADO OBTENIDO PRUEBA 24



HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 49

Date: Mon, 23 Nov 2026 03:15:57 GMT

Connection: close



Solo se pueden iniciar tareas en estado POR\_HACER



##### RESULTADO OBTENIDO PRUEBA 25



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 03:13:47 GMT



{"descripcion":"Descripción actualizada mediante prueba CURL.","estado":"EN\_REVISION","fechaCompletada":null,"fechaLimite":null,"id":52,"idResponsable":304,"idTablero":52,"observacion":null,"titulo":"Tarea modificada CURL Lourdes"}



##### RESULTADO OBTENIDO PRUEBA 26



HTTP/1.1 403

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 55

Date: Mon, 23 Nov 2026 03:16:43 GMT



Solo el responsable puede enviar esta tarea a revisión



##### RESULTADO OBTENIDO PRUEBA 27



\-X PUT http://localhost:8081/enviarTareaRevision/52

HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 40

Date: Mon, 23 Nov 2026 03:17:32 GMT

Connection: close



Solo se pueden enviar tareas EN\_PROGRESO



##### RESULTADO OBTENIDO PRUEBA 28



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 03:18:02 GMT



{"descripcion":"Descripción actualizada mediante prueba CURL.","estado":"HECHA","fechaCompletada":"2026-11-22T21:18:02.854330897","fechaLimite":null,"id":52,"idResponsable":304,"idTablero":52,"observacion":null,"titulo":"Tarea modificada CURL Lourdes"}



##### RESULTADO OBTENIDO PRUEBA 29



HTTP/1.1 403

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 35

Date: Mon, 23 Nov 2026 03:18:42 GMT



Solo un líder puede aprobar tareas



##### RESULTADO OBTENIDO PRUEBA 30



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 03:20:55 GMT



{"descripcion":"Tarea para comprobar el rechazo por parte del líder.","estado":"EN\_PROGRESO","fechaCompletada":null,"fechaLimite":null,"id":103,"idResponsable":304,"idTablero":52,"observacion":"La entrega necesita correcciones.","titulo":"Tarea auxiliar para prueba de rechazo"}

