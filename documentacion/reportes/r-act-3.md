# Reporte de Avance del Proyecto

**Equipo:** Sara Ortega Hernández, María de Lourdes Méndez Prigadaá y Aradan Schwartz Rocha  
**Fecha:** 16/11/2026  
**Número de reporte:** 3  
**Repositorio GitHub:** https://github.com/SaraOrtegaHdez13/desarrollo-web

---

# 1. Actividades realizadas

Durante esta actividad se trabajó en la implementación y validación de las operaciones del módulo de tareas. Se incorporaron modificaciones mediante `PUT`, eliminación mediante `DELETE`, filtros de consulta, observaciones para el rechazo de tareas y validaciones de permisos según el rol del integrante del equipo.

También se prepararon y ejecutaron pruebas automatizadas del controlador de tareas con JUnit, Mockito y MockMvc. Se realizaron pruebas manuales mediante CURL para comprobar el registro e inicio de sesión, la creación de equipos, la incorporación de miembros, la creación y consulta de tableros y tareas, y la modificación de tareas.

Finalmente, se consultó la base de datos MySQL para comprobar que los datos creados y modificados permanecieran almacenados.

---

# 2. Evidencias de trabajo

| Actividad | Integrante responsable | Commit |
|------------|------------------------|--------|
| Reporte 03 de documentación sobre el uso de IA en la prueba de reasignación de tareas conservando el historial | María de Lourdes Méndez Prigadaá | [cb4e61b](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/cb4e61b) |
| Reporte 02 de documentación sobre el uso de IA en la prueba de restricción de permisos para iniciar una tarea ajena | María de Lourdes Méndez Prigadaá | [f0ce01d](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/f0ce01d) |
| Informe 02 de documentación sobre el uso de IA en la prueba de creación de tareas por parte de un líder con datos válidos | Aradan Schwartz Rocha | [bbb0ed0](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/bbb0ed0) |
| Reporte y evidencias de pruebas del Hito 3 | Sara Ortega Hernández | [58422dc](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/58422dc) |
| Gestión de tareas y pruebas automatizadas | Sara Ortega Hernández | [98a794b](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/98a794b) |
| Implementación de reasignación con historial | María de Lourdes Méndez Prigadaá | [2bf9596](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/2bf9596) |
| Implementación de rechazo de tareas con observación | María de Lourdes Méndez Prigadaá | [1449320](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/1449320) |
| Implementación de aprobación de tareas por líder | María de Lourdes Méndez Prigadaá | [0224da6](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/0224da6) |
| Implementación de envío de tareas a revisión | María de Lourdes Méndez Prigadaá | [4cf5cf5](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/4cf5cf5) |
| Implementación de inicio de tareas por responsable | María de Lourdes Méndez Prigadaá | [22a0acd](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/22a0acd) |
| Implementación de eliminación segura de tareas | María de Lourdes Méndez Prigadaá | [62c0db8](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/62c0db8) |
| Implementación de modificaciones de tareas con permisos | María de Lourdes Méndez Prigadaá | [4541dd3](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/4541dd3) |
| Agregación de respuestas JSON para consultas de tareas | María de Lourdes Méndez Prigadaá | [ff871ea](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/ff871ea) |
| Implementación de consulta de tareas por tablero con filtros | María de Lourdes Méndez Prigadaá | [9224df4](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/9224df4) |
| Implementación de creación de tareas y registro de asignaciones | María de Lourdes Méndez Prigadaá | [81c63b0](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/81c63b0) |
| Implementación de consulta de tablero por ID | María de Lourdes Méndez Prigadaá | [c3b3294](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/c3b3294) |
| Implementación de creación de tableros con validación de permisos | María de Lourdes Méndez Prigadaá | [c53feaf](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/c53feaf) |
---

# 3. Problemas encontrados

Durante las pruebas se encontraron algunos problemas técnicos. Al insertar manualmente un miembro en MySQL, la operación fue rechazada porque la columna `id` de `miembros_equipo` requería un valor. Se continuó utilizando el endpoint de la API para agregar integrantes.

También se obtuvo una respuesta HTTP 404 al consultar `/obtenerTablero/2`, debido a que se utilizó el identificador del equipo en lugar del identificador del tablero. Al consultar el tablero con su ID correcto, el endpoint respondió HTTP 200.

Al intentar iniciar una tarea con una cuenta que no era su responsable, el servidor respondió HTTP 403. Esta respuesta fue correcta porque las reglas del proyecto establecen que solamente el responsable puede iniciar su tarea.

En las pruebas automatizadas, Mockito reportó inicialmente errores `UnfinishedStubbing`. Se ajustó el archivo de pruebas y posteriormente la ejecución de Maven terminó con `BUILD SUCCESS`.

---

# 4. Avance funcional

Se comprobó mediante CURL el registro de usuarios, el inicio de sesión, la creación de equipos, la incorporación de miembros, la creación y consulta de tableros, y la creación y consulta de tareas. También se comprobó la modificación de una tarea mediante `PUT`, que respondió HTTP 200.

La persistencia se verificó mediante consultas SQL en MySQL. Se consultó la tarea antes y después de modificarla, comprobando que el título, la descripción y la fecha límite actualizados permanecieran almacenados.

Se cuenta con implementación en el código para la eliminación segura, los filtros de consulta, el inicio de tareas por responsable, el envío a revisión, la aprobación, el rechazo con observación y la reasignación con historial. Sin embargo, las capturas reunidas para este reporte no acreditan la ejecución exitosa de todas esas operaciones.

Las pruebas automatizadas del controlador de tareas finalizaron con `BUILD SUCCESS`. Las pruebas de persistencia real se realizaron por separado mediante CURL y consultas directas a MySQL.

---

# 5. Uso de IA generativa

- **Herramienta utilizada:** ChatGPT.
- **Actividad para la que se utilizó:** apoyo en la preparación y corrección de pruebas automatizadas, explicación de errores, elaboración de comandos CURL y consultas SQL para comprobar las operaciones del backend.
- **Código o contenido generado:** ejemplos de pruebas con MockMvc y Mockito, comandos CURL para consumir los endpoints y consultas SQL para revisar los registros almacenados.
- **Modificaciones realizadas por el equipo:** adaptación de las propuestas a las clases, rutas, DTO y datos reales del proyecto; corrección de errores detectados durante la ejecución y revisión de las respuestas obtenidas.
- **Forma en que se verificó el resultado:** ejecución de Maven, consumo de endpoints mediante CURL y consultas directas a MySQL.

> El equipo revisó y adaptó las propuestas antes de incorporarlas al proyecto. Las capturas y explicaciones de las pruebas se incluyen en el documento `Evidencias_Pruebas_Hito_3.docx`.

---

# 6. Participación individual

- **Sara Ortega Hernández:** actualización del repositorio de asignaciones y del servicio de tareas; preparación y ajuste de pruebas automatizadas para `TareaControlador`; ejecución de pruebas CURL de registro, inicio de sesión, creación de equipo, incorporación de miembros, creación y consulta de tableros y tareas, modificación mediante `PUT` y comprobación de persistencia en MySQL.
- **María de Lourdes Méndez Prigadaá:** implementación de operaciones del módulo de tareas, incluyendo inicio por responsable, envío a revisión, aprobación, rechazo con observación, reasignación con historial, eliminación segura, modificación con permisos y filtros de consulta.
- **Aradan Schwartz Rocha:** no se cuenta con evidencia específica de nuevas contribuciones de Aradan en los commits revisados para este reporte.

---

# 7. Próximas actividades

- Completar y documentar las pruebas de eliminación, filtros, envío a revisión, aprobación, rechazo y reasignación.
- Comprobar en MySQL la persistencia de observaciones, revisiones e historial de asignaciones.
- Implementar y probar las sesiones Pomodoro.
- Implementar el reporte de cumplimiento por integrante.
- Completar la integración del frontend y revisar la documentación para reproducir el proyecto desde WSL.

---

## Observaciones adicionales

Las respuestas HTTP y consultas SQL descritas como comprobadas corresponden a las evidencias reunidas para este reporte. No se presentan como pruebas exitosas las operaciones que únicamente aparecen implementadas en el código.

La respuesta HTTP 403 al intentar iniciar una tarea con un usuario que no era responsable se considera una validación correcta de permisos. Las contraseñas temporales y cookies de sesión deben mantenerse fuera del repositorio y de la documentación pública.
