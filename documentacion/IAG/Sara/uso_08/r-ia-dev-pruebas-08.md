# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 22/11/2026
**Número de reporte:** 08

\---

# 1\. Breve descripción de uso

Se utilizó ChatGPT para proponer 31 pruebas CURL del módulo de usuarios, equipos y miembros, indicando los comandos y resultados HTTP esperados. Las pruebas se ejecutaron en WSL y sus respuestas reales se documentaron para comprobar el funcionamiento del sistema.

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
|Prueba de funcionamiento|[PRUEBA 1](#prueba-1-registrar-un-usuario-con-datos-válidos) · [RESPUESTA 1](#resultado-obtenido-prueba-1)<br>[PRUEBA 2](#prueba-2-iniciar-sesión-con-credenciales-correctas) · [RESPUESTA 2](#resultado-obtenido-prueba-2)<br>[PRUEBA 3](#prueba-3-consultar-el-usuario-autenticado) · [RESPUESTA 3](#resultado-obtenido-prueba-3)<br>[PRUEBA 4](#prueba-4-consultar-usario-actual-sin-iniciar-sesión) · [RESPUESTA 4](#resultado-obtenido-prueba-4)<br>[PRUEBA 5](#prueba-5-cerrar-sesión-con-una-sesión-activa) · [RESPUESTA 5](#resultado-obtenido-prueba-5)<br>[PRUEBA 6](#prueba-6-acceder-a-un-recurso-protegido-después-de-cerrar-sesión) · [RESPUESTA 6](#resultado-obtenido-prueba-6)<br>[PRUEBA 7](#prueba-7-crear-un-equipo) · [RESPUESTA 7](#resultado-obtenido-prueba-7)<br>[PRUEBA 8](#prueba-8-intentar-crear-un-equipo-sin-iniciar-sesión) · [RESPUESTA 8](#resultado-obtenido-prueba-8)<br>[PRUEBA 9](#prueba-9-consultar-equipos-del-usuario-autenticado) · [RESPUESTA 9](#resultado-obtenido-prueba-9)<br>[PRUEBA 10](#prueba-10-consultar-un-equipo-existente) · [RESPUESTA 10](#resultado-obtenido-prueba-10)<br>[PRUEBA 11](#prueba-11-consultar-un-equipo-inexistente) · [RESPUESTA 11](#resultado-obtenido-prueba-11)<br>[PRUEBA 12](#prueba-12-consultar-los-miembros-de-un-equipo) · [RESPUESTA 12](#resultado-obtenido-prueba-12)<br>[PRUEBA 13](#prueba-13-consultar-integrantes-de-un-equipo-inexistente) · [RESPUESTA 13](#resultado-obtenido-prueba-13)<br>[PRUEBA 14](#prueba-14-preparar-un-segundo-usuario-para-agregarlo-al-equipo) · [RESPUESTA 14](#resultado-obtenido-prueba-14)<br>[PRUEBA 15](#prueba-15-intentar-agregar-nuevamente-el-mismo-colaborador) · [RESPUESTA 15](#resultado-obtenido-prueba-15)<br>[PRUEBA 16](#prueba-16-intentar-agregar-un-usuario-inexistente) · [RESPUESTA 16](#resultado-obtenido-prueba-16)<br>[PRUEBA 17](#prueba-17-consultar-todos-los-integrantes-después-de-agregar-a-sofía) · [RESPUESTA 17](#resultado-obtenido-prueba-17)<br>[PRUEBA 18](#prueba-18-intentar-agregar-un-integrante-sin-ser-líder) · [RESPUESTA 18](#resultado-obtenido-prueba-18)<br>[PRUEBA 19](#prueba-19-modificar-el-rol-de-un-integrante) · [RESPUESTA 19](#resultado-obtenido-prueba-19)<br>[PRUEBA 20](#prueba-20-intentar-degradar-a-un-líder) · [RESPUESTA 20](#resultado-obtenido-prueba-20)<br>[PRUEBA 21](#prueba-21-intentar-asignar-un-tercer-líder-al-equipo) · [RESPUESTA 21](#resultado-obtenido-prueba-21)<br>[PRUEBA 22](#prueba-22-agregar-al-tercer-integrante-como-colaborador) · [RESPUESTA 22](#resultado-obtenido-prueba-22)<br>[PRUEBA 23](#prueba-23-intentar-promover-a-un-colaborador-cuando-ya-existen-dos-líderes) · [RESPUESTA 23](#resultado-obtenido-prueba-23)<br>[PRUEBA 24](#prueba-24-intentar-eliminar-a-un-líder-del-equipo) · [RESPUESTA 24](#resultado-obtenido-prueba-24)<br>[PRUEBA 25](#prueba-25-eliminar-a-un-colaborador) · [RESPUESTA 25](#resultado-obtenido-prueba-25)<br>[PRUEBA 26](#prueba-26-intentar-eliminar-un-integrante-que-no-existe) · [RESPUESTA 26](#resultado-obtenido-prueba-26)<br>[PRUEBA 27](#prueba-27-intentar-agregar-un-integrante-con-un-rol-de-invitado) · [RESPUESTA 27](#resultado-obtenido-prueba-27)<br>[PRUEBA 28](#prueba-28-consultar-equipos-no-archivados) · [RESPUESTA 28](#resultado-obtenido-prueba-28)<br>[PRUEBA 29](#prueba-29-preparar-un-usuario-que-no-pertenece-al-equipo) · [RESPUESTA 29](#resultado-obtenido-prueba-29)<br>[PRUEBA 30](#prueba-30-consultar-equipos-archivados) · [RESPUESTA 30](#resultado-obtenido-prueba-30)<br>[PRUEBA 31](#prueba-31-modificar-un-integrante-inexistente) · [RESPUESTA 31](#resultado-obtenido-prueba-31)|
|Código de uso|Pruebas|
|Reflexión del estudiante|La IA me ayudó a organizar las pruebas CURL y conocer las respuestas esperadas. Al ejecutarlas, pude comprobar el funcionamiento de los endpoints y comprender mejor las sesiones, los permisos y las validaciones del sistema.|



##### PROMT 1



Estoy desarrollando el módulo de usuarios, equipos y miembros de un proyecto de gestión colaborativa de tareas con Spring Boot y MySQL.



Necesito realizar pruebas de funcionamiento utilizando exclusivamente CURL desde la terminal de WSL. Quiero comprobar que los endpoints funcionen correctamente tanto en casos exitosos como en casos de error.



Ayúdame a diseñar las pruebas paso a paso. Para cada prueba, explica brevemente su objetivo, proporciona el comando CURL completo y señala cuál es el código HTTP y el mensaje o respuesta que debería obtener.



Es importante que las pruebas respeten el funcionamiento real del proyecto: inicio de sesión mediante HttpSession, uso de cookies para conservar la sesión y permisos según el rol del integrante en el equipo.



No me des por aprobada una prueba hasta que te comparta la respuesta real de mi terminal. Necesito ejecutar personalmente cada comando y comprobar sus resultados.



##### RESPUESTA PROMT 1



Para comprobar el funcionamiento del módulo de usuarios, equipos y miembros, realizaremos pruebas CURL directamente desde la terminal de WSL.



Las pruebas se dividirán en diferentes operaciones del sistema:



Usuarios y sesiones: registro, inicio de sesión, consulta del usuario autenticado y cierre de sesión.



Equipos: creación, consulta y filtrado de equipos.



Integrantes: consulta, registro, modificación de roles y eliminación de miembros.



Permisos: comprobación de las restricciones correspondientes a los roles LIDER y COLABORADOR.



Validaciones: comprobación de respuestas ante usuarios duplicados, datos inválidos, identificadores inexistentes y accesos no autorizados.



Para cada caso proporcionaré un comando CURL listo para ejecutar y señalaré el código HTTP y la respuesta que debería obtenerse.



Comenzaremos con las pruebas de registro e inicio de sesión y continuaremos con las operaciones de equipos y miembros. Después de cada ejecución, revisaremos la respuesta de la terminal para determinar si el resultado coincide con el esperado.



\---

##### PRUEBA 1. REGISTRAR UN USUARIO CON DATOS VÁLIDOS



curl -i -X POST http://localhost:8081/crearUsuario \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"nombre":"Usuario Prueba","correo":"prueba.curl.2026@ejemplo.com","password":"Prueba123"}'





##### PRUEBA 2. INICIAR SESIÓN CON CREDENCIALES CORRECTAS



curl -i -c cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/iniciarSesion \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"SaraOrtegaHdez13@gmail.com","password":"TU\_CONTRASEÑA"}'





##### PRUEBA 3. CONSULTAR EL USUARIO AUTENTICADO



curl -i -b cookies-sara.txt \\

&#x20; -X GET http://localhost:8081/obtenerUsuarioActual



##### PRUEBA 4. CONSULTAR USARIO ACTUAL SIN INICIAR SESIÓN



curl -i -X GET http://localhost:8081/obtenerUsuarioActual



##### PRUEBA 5. CERRAR SESIÓN CON UNA SESIÓN ACTIVA



curl -i -b cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/cerrarSesion



##### PRUEBA 6. ACCEDER A UN RECURSO PROTEGIDO DESPUÉS DE CERRAR SESIÓN



curl -i -b cookies-sara.txt \\

&#x20; -X GET http://localhost:8081/obtenerUsuarioActual



##### PRUEBA 7. CREAR UN EQUIPO



curl -i -c cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/iniciarSesion \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"SaraOrtegaHdez13@gmail.com","password":"TU\_CONTRASEÑA"}'



curl -i -b cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/crearEquipo \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"nombre":"Equipo Prueba Sara","descripcion":"Equipo creado mediante prueba CURL"}'



##### PRUEBA 8. INTENTAR CREAR UN EQUIPO SIN INICIAR SESIÓN



curl -i -X POST http://localhost:8081/crearEquipo \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"nombre":"Equipo Sin Sesion","descripcion":"Prueba de acceso no autorizado"}'



##### PRUEBA 9. CONSULTAR EQUIPOS DEL USUARIO AUTENTICADO



curl -i -b cookies-sara.txt \\

&#x20; -X GET http://localhost:8081/obtenerEquipos



##### PRUEBA 10. CONSULTAR UN EQUIPO EXISTENTE



curl -i -b cookies-sara.txt \\

&#x20; -X GET http://localhost:8081/obtenerEquipo/52



##### PRUEBA 11. CONSULTAR UN EQUIPO INEXISTENTE



curl -i -b cookies-sara.txt \\

&#x20; -X GET http://localhost:8081/obtenerEquipo/999999



##### PRUEBA 12. CONSULTAR LOS MIEMBROS DE UN EQUIPO



curl -i -b cookies-sara.txt \\

&#x20; -X GET http://localhost:8081/obtenerMiembros/52



##### PRUEBA 13. CONSULTAR INTEGRANTES DE UN EQUIPO INEXISTENTE



curl -i -b cookies-sara.txt \\

&#x20; -X GET http://localhost:8081/obtenerMiembros/999999



##### PRUEBA 14. PREPARAR UN SEGUNDO USUARIO PARA AGREGARLO AL EQUIPO



curl -i -X POST http://localhost:8081/crearUsuario \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"nombre":"Colaborador Prueba","correo":"colaborador.curl.2026@ejemplo.com","password":"Colaborador123"}'



curl -i -b cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/agregarMiembro/52 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"chofguzhdez13@gmail.com","rol":"COLABORADOR"}'



***Nota:*** para el segundo CURL, reemplazó los datos del primer CURL por los que realmente se usaron en la prueba, en base a examinar el resultado enviado para el primer resultado.



##### PRUEBA 15. INTENTAR AGREGAR NUEVAMENTE EL MISMO COLABORADOR



curl -i -b cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/agregarMiembro/52 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"chofguzhdez13@gmail.com","rol":"COLABORADOR"}'



##### PRUEBA 16. INTENTAR AGREGAR UN USUARIO INEXISTENTE



curl -i -b cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/agregarMiembro/52 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"usuario.inexistente.curl@ejemplo.com","rol":"COLABORADOR"}'



##### PRUEBA 17. CONSULTAR TODOS LOS INTEGRANTES DESPUÉS DE AGREGAR A SOFÍA



curl -i -b cookies-sara.txt \\

&#x20; -X GET http://localhost:8081/obtenerMiembros/52



##### PRUEBA 18. INTENTAR AGREGAR UN INTEGRANTE SIN SER LÍDER



curl -i -c cookies-sofia.txt \\

&#x20; -X POST http://localhost:8081/iniciarSesion \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"chofguzhdez13@gmail.com","password":"14130606"}'





curl -i -b cookies-sofia.txt \\

&#x20; -X POST http://localhost:8081/agregarMiembro/52 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"tercer.usuario@ejemplo.com","rol":"COLABORADOR"}'



##### PRUEBA 19. MODIFICAR EL ROL DE UN INTEGRANTE



curl -i -b cookies-sara.txt \\

&#x20; -X PUT http://localhost:8081/modificarMiembro/103 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"rol":"LIDER"}'



##### PRUEBA 20. INTENTAR DEGRADAR A UN LÍDER



curl -i -b cookies-sara.txt \\

&#x20; -X PUT http://localhost:8081/modificarMiembro/103 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"rol":"COLABORADOR"}'



##### PRUEBA 21. INTENTAR ASIGNAR UN TERCER LÍDER AL EQUIPO



curl -i -X POST http://localhost:8081/crearUsuario \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"nombre":"Tercer Integrante","correo":"tercer.integrante.curl@ejemplo.com","password":"Tercer123"}'



curl -i -b cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/agregarMiembro/52 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"tercer.integrante.curl@ejemplo.com","rol":"LIDER"}'



##### PRUEBA 22. AGREGAR AL TERCER INTEGRANTE COMO COLABORADOR



curl -i -b cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/agregarMiembro/52 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"tercer.integrante.curl@ejemplo.com","rol":"COLABORADOR"}'



##### PRUEBA 23. INTENTAR PROMOVER A UN COLABORADOR CUANDO YA EXISTEN DOS LÍDERES



curl -i -b cookies-sara.txt \\

&#x20; -X PUT http://localhost:8081/modificarMiembro/104 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"rol":"LIDER"}'



##### PRUEBA 24. INTENTAR ELIMINAR A UN LÍDER DEL EQUIPO



curl -i -b cookies-sara.txt \\

&#x20; -X DELETE http://localhost:8081/eliminarMiembro/103



##### PRUEBA 25. ELIMINAR A UN COLABORADOR



curl -i -b cookies-sara.txt \\

&#x20; -X DELETE http://localhost:8081/eliminarMiembro/104



##### PRUEBA 26. INTENTAR ELIMINAR UN INTEGRANTE QUE NO EXISTE



curl -i -b cookies-sara.txt \\

&#x20; -X DELETE http://localhost:8081/eliminarMiembro/999999



##### PRUEBA 27. INTENTAR AGREGAR UN INTEGRANTE CON UN ROL DE INVITADO



curl -i -b cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/agregarMiembro/52 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"tercer.integrante.curl@ejemplo.com","rol":"ADMINISTRADOR"}'



##### PRUEBA 28. CONSULTAR EQUIPOS NO ARCHIVADOS



curl -i -b cookies-sara.txt \\

&#x20; -X GET "http://localhost:8081/obtenerEquipos?archivado=false"



##### PRUEBA 29. PREPARAR UN USUARIO QUE NO PERTENECE AL EQUIPO



curl -i -c cookies-tercero.txt \\

&#x20; -X POST http://localhost:8081/iniciarSesion \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"tercer.integrante.curl@ejemplo.com","password":"Tercer123"}'



curl -i -b cookies-tercero.txt \\

&#x20; -X GET http://localhost:8081/obtenerEquipo/52



##### PRUEBA 30. CONSULTAR EQUIPOS ARCHIVADOS



curl -i -b cookies-sara.txt \\

&#x20; -X GET "http://localhost:8081/obtenerEquipos?archivado=true"



##### PRUEBA 31. MODIFICAR UN INTEGRANTE INEXISTENTE



curl -i -b cookies-sara.txt \\

&#x20; -X PUT http://localhost:8081/modificarMiembro/999999 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"rol":"COLABORADOR"}'



##### RESULTADO OBTENIDO PRUEBA 1



&#x20;curl -i -X POST http://localhost:8081/crearUsuario \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"nombre":"Usuario Prueba Sara","correo":"SaraOrtegaHdez13@gmail.com","password":"14130606"}'



HTTP/1.1 201

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 32

Date: Mon, 23 Nov 2026 00:28:25 GMT



Usuario registrado correctamente



##### RESULTADO OBTENIDO PRUEBA 2



&#x20;curl -i -c cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/iniciarSesion \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"SaraOrtegaHdez13@gmail.com","password":"14130606"}'



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Set-Cookie: JSESSIONID=378E4C79041844FFC51AEB250BEF5BCD; Path=/; HttpOnly

Content-Type: text/plain;charset=UTF-8

Content-Length: 26

Date: Mon, 23 Nov 2026 00:32:23 GMT



Inicio de sesión correcto



##### RESULTADO OBTENIDO PRUEBA 3



curl -i -b cookies-sara.txt \\

&#x09;-X GET http://localhost:8081/obtenerUsuarioActual



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 00:34:31 GMT



{"id":302,"nombre":"Usuario Prueba Sara","correo":"SaraOrtegaHdez13@gmail.com"}



##### RESULTADO OBTENIDO PRUEBA 4



curl -i -X GET http://localhost:8081/obtenerUsuarioActual



HTTP/1.1 401

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json;charset=UTF-8

Content-Length: 39

Date: Mon, 23 Nov 2026 00:36:06 GMT



{"mensaje":"No hay una sesión activa"}



##### RESULTADO OBTENIDO PRUEBA 5



curl -i -b cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/cerrarSesion



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 29

Date: Mon, 23 Nov 2026 00:37:12 GMT



Sesión cerrada correctamente



##### RESULTADO OBTENIDO PRUEBA 6



curl -i -b cookies-sara.txt \\

&#x20; -X GET http://localhost:8081/obtenerUsuarioActual



HTTP/1.1 401

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json;charset=UTF-8

Content-Length: 39

Date: Mon, 23 Nov 2026 00:38:15 GMT



{"mensaje":"No hay una sesión activa"}



##### RESULTADO OBTENIDO PRUEBA 7



&#x20;curl -i -c cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/iniciarSesion \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"SaraOrtegaHdez13@gmail.com","password":"14130606"}'



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Set-Cookie: JSESSIONID=62A5E266A70CF1D4CDB92FDB1659CBA1; Path=/; HttpOnly

Content-Type: text/plain;charset=UTF-8

Content-Length: 26

Date: Mon, 23 Nov 2026 00:39:25 GMT



Inicio de sesión correcto



&#x20;curl -i -b cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/crearEquipo \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"nombre":"Equipo Prueba Sara","descripcion":"Equipo creado mediante prueba CURL"}'



HTTP/1.1 201

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 00:41:08 GMT



{"nombre":"Equipo Prueba Sara","descripcion":"Equipo creado mediante prueba CURL","archivado":false,"id":52}



##### RESULTADO OBTENIDO PRUEBA 8



curl -i -X POST http://localhost:8081/crearEquipo \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"nombre":"Equipo Sin Sesion","descripcion":"Prueba de acceso no autorizado"}'



HTTP/1.1 401

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json;charset=UTF-8

Content-Length: 39

Date: Mon, 23 Nov 2026 00:42:31 GMT



{"mensaje":"No hay una sesión activa"}



##### RESULTADO OBTENIDO PRUEBA 9



curl -i -b cookies-sara.txt \\

&#x20; -X GET http://localhost:8081/obtenerEquipos



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 00:44:48 GMT



\[{"id":52,"nombre":"Equipo Prueba Sara","descripcion":"Equipo creado mediante prueba CURL","archivado":false,"rol":"LIDER"}]



##### RESULTADO OBTENIDO PRUEBA 10



curl -i -b cookies-sara.txt \\

&#x20; -X GET http://localhost:8081/obtenerEquipo/52



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 00:47:04 GMT



{"id":52,"nombre":"Equipo Prueba Sara","descripcion":"Equipo creado mediante prueba CURL","archivado":false,"rol":"LIDER"}



##### RESULTADO OBTENIDO PRUEBA 11



curl -i -b cookies-sara.txt \\

&#x20; -X GET http://localhost:8081/obtenerEquipo/999999



HTTP/1.1 404

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Length: 0

Date: Mon, 23 Nov 2026 00:48:05 GMT



##### RESULTADO OBTENIDO PRUEBA 12



curl -i -b cookies-sara.txt \\

&#x20; -X GET http://localhost:8081/obtenerMiembros/52



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 00:49:22 GMT



\[{"idMiembro":102,"idUsuario":302,"nombre":"Usuario Prueba Sara","correo":"SaraOrtegaHdez13@gmail.com","rol":"LIDER"}]



##### RESULTADO OBTENIDO PRUEBA 13



curl -i -b cookies-sara.txt \\

&#x20; -X GET http://localhost:8081/obtenerMiembros/999999



HTTP/1.1 404

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Length: 0

Date: Mon, 23 Nov 2026 00:50:10 GMT



##### RESULTADO OBTENIDO PRUEBA 14



curl -i -X POST http://localhost:8081/crearUsuario \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"nombre":"Colaborador Prueba Sofía","correo":"chofguzhdez13@gmail.com","password":"14130606"}'



HTTP/1.1 201

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 32

Date: Mon, 23 Nov 2026 00:51:27 GMT



Usuario registrado correctamente



&#x20;curl -i -b cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/agregarMiembro/52 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"chofguzhdez13@gmail.com","rol":"COLABORADOR"}'



HTTP/1.1 201

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 00:53:18 GMT



{"idMiembro":103,"idUsuario":303,"nombre":"Colaborador Prueba Sofía","correo":"chofguzhdez13@gmail.com","rol":"COLABORADOR"}



##### RESULTADO OBTENIDO PRUEBA 15



curl -i -b cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/agregarMiembro/52 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"chofguzhdez13@gmail.com","rol":"COLABORADOR"}'



HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 37

Date: Mon, 23 Nov 2026 00:54:30 GMT

Connection: close



El usuario ya pertenece a este equipo



##### RESULTADO OBTENIDO PRUEBA 16



curl -i -b cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/agregarMiembro/52 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"usuario.inexistente.curl@ejemplo.com","rol":"COLABORADOR"}'



HTTP/1.1 404

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 46

Date: Mon, 23 Nov 2026 00:55:55 GMT



No existe un usuario registrado con ese correo



##### RESULTADO OBTENIDO PRUEBA 17



curl -i -b cookies-sara.txt \\

&#x20; -X GET http://localhost:8081/obtenerMiembros/52



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 00:56:38 GMT



\[{"idMiembro":102,"idUsuario":302,"nombre":"Usuario Prueba Sara","correo":"SaraOrtegaHdez13@gmail.com","rol":"LIDER"},{"idMiembro":103,"idUsuario":303,"nombre":"Colaborador Prueba Sofía","correo":"chofguzhdez13@gmail.com","rol":"COLABORADOR"}]





##### RESULTADO OBTENIDO PRUEBA 18



curl -i -c cookies-sofia.txt \\

&#x20; -X POST http://localhost:8081/iniciarSesion \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"chofguzhdez13@gmail.com","password":"14130606"}'



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Set-Cookie: JSESSIONID=5FA100FA453E7E4359C804F54F0CA906; Path=/; HttpOnly

Content-Type: text/plain;charset=UTF-8

Content-Length: 26

Date: Mon, 23 Nov 2026 00:57:42 GMT



Inicio de sesión correcto



curl -i -b cookies-sofia.txt \\

&#x20; -X POST http://localhost:8081/agregarMiembro/52 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"tercer.usuario@ejemplo.com","rol":"COLABORADOR"}'



HTTP/1.1 403

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 40

Date: Mon, 23 Nov 2026 00:58:22 GMT



Solo un líder puede agregar integrantes



##### RESULTADO OBTENIDO PRUEBA 19



curl -i -b cookies-sara.txt \\

&#x20; -X PUT http://localhost:8081/modificarMiembro/103 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"rol":"LIDER"}'



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 00:59:13 GMT



{"idMiembro":103,"idUsuario":303,"nombre":"Colaborador Prueba Sofía","correo":"chofguzhdez13@gmail.com","rol":"LIDER"}



##### RESULTADO OBTENIDO PRUEBA 20



curl -i -b cookies-sara.txt \\

&#x20; -X PUT http://localhost:8081/modificarMiembro/103 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"rol":"COLABORADOR"}'



HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 32

Date: Mon, 23 Nov 2026 01:00:29 GMT

Connection: close



No se puede degradar a un líder



##### RESULTADO OBTENIDO PRUEBA 21

curl -i -X POST http://localhost:8081/crearUsuario \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"nombre":"Tercer Integrante","correo":"tercer.integrante.curl@ejemplo.com","password":"Tercer123"}'



HTTP/1.1 201

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 32

Date: Mon, 23 Nov 2026 01:02:34 GMT



Usuario registrado correctamente



curl -i -b cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/agregarMiembro/52 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"tercer.integrante.curl@ejemplo.com","rol":"LIDER"}'



HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 31

Date: Mon, 23 Nov 2026 01:03:32 GMT

Connection: close



El equipo ya tiene dos líderes



##### RESULTADO OBTENIDO PRUEBA 22



curl -i -b cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/agregarMiembro/52 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"tercer.integrante.curl@ejemplo.com","rol":"COLABORADOR"}'



HTTP/1.1 201

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 01:04:15 GMT



{"idMiembro":104,"idUsuario":304,"nombre":"Tercer Integrante","correo":"tercer.integrante.curl@ejemplo.com","rol":"COLABORADOR"}



##### RESULTADO OBTENIDO PRUEBA 23



curl -i -b cookies-sara.txt \\

&#x20; -X PUT http://localhost:8081/modificarMiembro/104 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"rol":"LIDER"}'



HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 31

Date: Mon, 23 Nov 2026 01:05:01 GMT

Connection: close



El equipo ya tiene dos líderes



##### RESULTADO OBTENIDO PRUEBA 24



curl -i -b cookies-sara.txt \\

&#x20; -X DELETE http://localhost:8081/eliminarMiembro/103



HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 32

Date: Mon, 23 Nov 2026 01:06:08 GMT

Connection: close



No se puede eliminar a un líder



##### RESULTADO OBTENIDO PRUEBA 25



curl -i -b cookies-sara.txt \\

&#x20; -X DELETE http://localhost:8081/eliminarMiembro/104



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 34

Date: Mon, 23 Nov 2026 01:06:55 GMT



Integrante eliminado correctamente



##### RESULTADO OBTENIDO PRUEBA 26



curl -i -b cookies-sara.txt \\

&#x20; -X DELETE http://localhost:8081/eliminarMiembro/999999



HTTP/1.1 404

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Length: 0

Date: Mon, 23 Nov 2026 01:07:41 GMT



##### RESULTADO OBTENIDO PRUEBA 27



curl -i -b cookies-sara.txt \\

&#x20; -X POST http://localhost:8081/agregarMiembro/52 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"tercer.integrante.curl@ejemplo.com","rol":"ADMINISTRADOR"}'



HTTP/1.1 400

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 35

Date: Mon, 23 Nov 2026 01:08:32 GMT

Connection: close



El rol debe ser LIDER o COLABORADOR



##### RESULTADO OBTENIDO PRUEBA 28



curl -i -b cookies-sara.txt \\

&#x20; -X GET "http://localhost:8081/obtenerEquipos?archivado=false"



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 01:10:26 GMT



\[{"id":52,"nombre":"Equipo Prueba Sara","descripcion":"Equipo creado mediante prueba CURL","archivado":false,"rol":"LIDER"}]



##### RESULTADO OBTENIDO PRUEBA 29



curl -i -c cookies-tercero.txt \\

&#x20; -X POST http://localhost:8081/iniciarSesion \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"correo":"tercer.integrante.curl@ejemplo.com","password":"Tercer123"}'



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Set-Cookie: JSESSIONID=13A5059F1BDBF9FC9C6F4673DAE93F1B; Path=/; HttpOnly

Content-Type: text/plain;charset=UTF-8

Content-Length: 26

Date: Mon, 23 Nov 2026 01:11:25 GMT



Inicio de sesión correcto



curl -i -b cookies-tercero.txt \\

&#x20; -X GET http://localhost:8081/obtenerEquipo/52



HTTP/1.1 403

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: text/plain;charset=UTF-8

Content-Length: 30

Date: Mon, 23 Nov 2026 01:12:01 GMT



No tienes acceso a este equipo



##### RESULTADO OBTENIDO PRUEBA 30



curl -i -b cookies-sara.txt \\

&#x20; -X GET "http://localhost:8081/obtenerEquipos?archivado=true"



HTTP/1.1 200

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Type: application/json

Transfer-Encoding: chunked

Date: Mon, 23 Nov 2026 01:12:51 GMT



\[]



##### RESULTADO OBTENIDO PRUEBA 31



curl -i -b cookies-sara.txt \\

&#x20; -X PUT http://localhost:8081/modificarMiembro/999999 \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"rol":"COLABORADOR"}'



HTTP/1.1 404

Vary: Origin

Vary: Access-Control-Request-Method

Vary: Access-Control-Request-Headers

Content-Length: 0

Date: Mon, 23 Nov 2026 01:13:33 GMT

