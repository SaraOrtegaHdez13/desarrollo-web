# Reporte de Avance del Proyecto

**Equipo:** Sara Ortega Hernández, María de Lourdes Méndez Prigadaá y Aradan Schwartz Rocha  
**Fecha:** 23/11/2026  
**Número de reporte:** 5  
**Repositorio GitHub:** https://github.com/SaraOrtegaHdez13/desarrollo-web

---

# 1. Actividades realizadas

En esta entrega se preparó la versión candidata de LuzioTeamTime. Se actualizaron las pantallas de acceso, registro, equipos y tableros, con estilos compartidos y ajustes en las acciones que ofrece la interfaz durante el flujo de tareas.

En el backend se incorporó la validación de fechas límite al crear tareas y al cambiar su vencimiento. También se ajustó la consulta del historial de revisiones para contemplar registros sin autor de entrega y se modificaron las consultas usadas para coordinar actualizaciones de tareas y sesiones Pomodoro. Se ajustaron el temporizador y sus pruebas automatizadas.

Se actualizaron el README y el esquema de la base de datos. El README documenta la preparación de WSL y MySQL, las variables de conexión, el arranque del backend y frontend y la ejecución de las pruebas. Se publicó la etiqueta E5 y se incorporaron los reportes de IA y las evidencias cURL finales de los módulos A y B.

---

# 2. Evidencias de trabajo

| Actividad registrada en GitHub | Integrante que publicó el commit | Enlace |
| --- | --- | --- |
| Ajuste de las validaciones y el flujo de revisión | Sara Ortega Hernández | [888ee93](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/888ee93500ebfc50e85df5331cc1d7e48512bd3a) |
| Actualización del acceso y la gestión de integrantes | Sara Ortega Hernández | [779752f](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/779752f7e8be972d27e9a9610c88622365b2e8f9) |
| Reporte 04 sobre la validación de fechas límite | Sara Ortega Hernández | [66760bf](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/66760bf14435f985190bac6724715b73e1ca30f1) |
| Integración de pruebas CURL finales del módulo A | Sara Ortega Hernández | [818f9d2](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/818f9d2092d68d956a976306d4ca221700423dd8) |
| ajuste del flujo de tareas desde la interfaz | María de Lourdes Méndez Prigadaá | [c5a3429](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/c5a342973af885410d710845736173c1ef28605e) |
| actualización de la vista de equipos y los estilos compartidos | María de Lourdes Méndez Prigadaá | [49a02e2](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/49a02e2a78c380dc39ce2a0048de36096ba2ce9d) |
| Reporte 04 sobre el conteo de tareas aprobadas por responsable | María de Lourdes Méndez Prigadaá | [cfbfadc](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/cfbfadcdd2c1b5ef6f9a6ae84f4cce9ff9921c0e) |
| Integración de pruebas CURL finales del módulo B | María de Lourdes Méndez Prigadaá | [b44f25a](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/b44f25aba0a57c1e8f7d3064833b6061e7d7eb10) |
| ajuste del temporizador y el registro de sesiones | Aradan Schwartz Rocha | [490ee77](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/490ee7735414e1ac08cfa956a589bc519023fdc0) |
| Versión final del readme y el esquema de la BD | Sara Ortega Hernández | [b7f491a](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/b7f491adf2e4e74914424dff3c5143b9494e1372) |

---

# 3. Problemas encontrados

| Problema | Solución o estado |
| --- | --- |
| El tablero permitió guardar «Revisar épicas» con una fecha límite de 2015. | El backend rechaza fechas anteriores al día actual de México al crear una tarea o cambiar su fecha límite. El reporte de IA registra resultados satisfactorios para las fechas evaluadas. |
| El historial podía contener una entrega sin autor asociado. | La transformación del historial ahora admite el identificador de autor ausente. |
| El líder revisor aparecía con cero tareas aprobadas. | La evidencia funcional del equipo muestra que el crédito se asigna al responsable de la tarea. El rol de revisor por sí solo no suma una tarea aprobada. |

---

# 4. Avance funcional

| Módulo | Cobertura documentada | Evidencia |
| --- | --- | --- |
| A. Acceso y equipos | Registro, autenticación, cierre de sesión, creación de equipos y permisos de acceso. | [31 casos cURL](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/818f9d2) |
| B. Tableros y tareas | Creación y consulta de tableros, asignación de tareas, validaciones, permisos y transiciones del flujo de revisión. | [30 casos cURL](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/b44f25a) |
| C. Pomodoro y cumplimiento | Registro y consulta de sesiones, permisos y reporte de cumplimiento. | [Pruebas cURL de Pomodoro](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/f0019dc) y [pruebas automatizadas](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/2f61ccd) |

La cobertura refleja las pruebas documentadas en los commits. Para este reporte se revisaron los archivos y el historial de Git, sin una nueva ejecución de la aplicación ni de la suite automatizada.

---

# 5. Uso de IA generativa

- **Fechas límite.** Se usó IA para localizar la validación ausente y diseñar casos de rechazo y aceptación. El equipo implementó el ajuste y consignó resultados en el [reporte publicado por Sara](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/66760bf).
- **Tareas aprobadas por responsable.** Se analizaron las reglas RN-06 y RN-07 y se documentaron casos de asignación, revisión y conteo para líderes y colaboradores. [Reporte del conteo](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/cfbfadc).
- **Pruebas finales.** Se integraron reportes de IA con comandos CURL y evidencias para los módulos A y B. La sección 4 enlaza también la documentación de pruebas del módulo C disponible en E5.

---

# 6. Participación individual

- **Sara Ortega Hernández.** Publicó las validaciones y ajustes del flujo de tareas, las pantallas de acceso y gestión de integrantes, la evidencia del módulo A, el reporte sobre fechas límite y la actualización final del README y del esquema SQL.
- **María de Lourdes Méndez Prigadaá.** Publicó cambios en la interfaz del tablero, la vista de equipos y los estilos compartidos. Integró el reporte de conteo de tareas aprobadas y la evidencia cURL del módulo B.
- **Aradan Schwartz Rocha.** Publicó el ajuste del temporizador y del registro de sesiones, junto con los cambios de repositorio y servicio que forman parte de ese commit.

---

# 7. Próximas actividades

- Grabar el video de demostración con la participación de los tres integrantes y agregar el enlace al PDF de entrega final.

## Observaciones adicionales

Los títulos de algunos commits y la numeración interna de los reportes de IA difieren, por lo que este reporte identifica las evidencias por tema y enlace.
