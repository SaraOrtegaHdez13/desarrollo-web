# Reporte de Avance del Proyecto

**Equipo:** Sara Ortega Hernández, María de Lourdes Méndez Prigadaá y Aradan Schwartz Rocha  
**Fecha:** 20/11/2026  
**Número de reporte:** 4  
**Repositorio GitHub:** https://github.com/SaraOrtegaHdez13/desarrollo-web

---

# 1. Actividades realizadas

Durante esta entrega se integraron las pantallas de registro, inicio de sesión, equipos y tableros con la API. Se ajustó el interceptor de sesión para permitir el preflight de CORS requerido por las solicitudes del frontend.

En el tablero se incorporaron la creación de tareas y las acciones para iniciarlas, enviarlas a revisión, aprobarlas o rechazarlas con observaciones. En el detalle de tarea se incorporaron los historiales de revisiones y asignaciones.

En la versión local también se integró el temporizador Pomodoro y se implementó el guardado de sesiones en segundos. Se desarrolló el reporte de cumplimiento por equipo con un filtro por tablero y se habilitó su acceso desde la pantalla de equipo, donde también se incorporó el selector de rol al agregar integrantes.


Se añadieron la consulta y validación de sesiones Pomodoro, pruebas automatizadas de sesiones y cumplimiento, el esquema SQL y las instrucciones de instalación. También se publicaron las evidencias de pruebas cURL, el prompt de análisis UX con ChatGPT y el prototipo comparativo de Gemini con la selección de UV Nocturno.

---

# 2. Evidencias de trabajo

| Actividad | Integrante responsable | Commit |
|------------|------------------------|--------|
| permitir solicitudes preflight de CORS | María de Lourdes Méndez Prigadaá | [64994b3](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/64994b3) |
| integración de acceso y gestión básica de equipos | María de Lourdes Méndez Prigadaá | [43e6f35](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/43e6f35) |
| agregar creación y flujo de revisión de tareas | María de Lourdes Méndez Prigadaá | [074292c](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/074292c) |
| exposición de historial de revisiones y asignaciones | María de Lourdes Méndez Prigadaá | [16c2293](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/16c2293) |
| guardado de sesiones de pomodoro en segundos | María de Lourdes Méndez Prigadaá | [6bbdebd](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/6bbdebd) |
| generación de reporte de cumplimiento por equipo y tablero | María de Lourdes Méndez Prigadaá | [e143ace](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/e143ace) |
| implementación de temporizador pomodoro y guardado de sesiones | Aradan Schwartz Rocha | [bc7f7af](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/bc7f7af) |
| Completar gestión de integrantes y enlazar reporte | Sara Ortega Hernández | [2d99c81](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/2d99c81) |
| Adaptar el historial de entregas y el reporte de cumplimiento | Sara Ortega Hernández | [5d36273](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/5d362738e2df4e0b77c9ee2a5aa9ecf27af0fe86) |
| Agregar la consulta de sesiones Pomodoro | Sara Ortega Hernández | [2c626f4](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/2c626f442ea1c3c71c97d78149aa95bf24f29578) |
| Validar permisos y registro de sesiones Pomodoro | Sara Ortega Hernández | [9fe4b0a](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/9fe4b0a9968bede2d9d2e56a59159df8f3441130) |
| Agregar pruebas de cumplimiento y sesiones Pomodoro | Sara Ortega Hernández | [2f61ccd](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/2f61ccd7b17cd1f47517b84d0d555d4e3985aaa0) |
| Corregir la referencia al archivo de acciones de tareas | Sara Ortega Hernández | [93ecf90](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/93ecf904c464cf56cc035ebc8fd752264eab0a55) |
| Documentar la instalación y ejecución del proyecto | Sara Ortega Hernández | [674c5b5](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/674c5b5a3066d3521ddf7d82a04dcd64a56e5886) |
| Agregar el esquema SQL | Sara Ortega Hernández | [b95e1d9](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/b95e1d9e65805a417892cbb3a99c76018b27d994) |
| Documentar pruebas cURL del módulo Pomodoro | Sara Ortega Hernández | [f0019dc](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/f0019dc3dbfd6e0d3e0d53f6d7288d5b0ef2d11a) |
| Publicar el análisis UX y el prompt de diseño con ChatGPT | Sara Ortega Hernández | [3f77f90](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/3f77f906ae7c9691ca8a8f8f951935a28eb80a95) |
| Publicar el prototipo de Gemini y la elección de UV Nocturno | María de Lourdes Méndez Prigadaá | [0d46ca2](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/0d46ca214055b651575d9394462bf32c7f7f17ba) |
| Publicar el reporte de la prueba cURL de reasignación e historial | Aradan Schwartz Rocha | [c01265c](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/c01265c92fc947387740345f66c2fc2f5c6146a0) |

---

# 3. Problemas encontrados

El interceptor de sesión devolvía HTTP 401 a la solicitud preflight del navegador. Se permitió esa solicitud y se mantuvo la validación de sesión en las rutas protegidas.

Al abrir el detalle de una tarea, la respuesta incluía sus datos generales pero faltaban los historiales. Se amplió la respuesta de la API para exponer las revisiones y asignaciones.

Al finalizar el temporizador, la solicitud de guardado devolvía HTTP 404. Se incorporó la ruta de sesiones y se comprobó el registro al consultar el reporte de cumplimiento después de completar un Pomodoro.


El tablero intentaba cargar `acciones-tarea.js`. Se corrigió la referencia a `acciones-tareas.js` en la versión publicada.

---

# 4. Avance funcional

Se comprobó desde la interfaz la gestión de integrantes, incluido el límite de dos líderes por equipo. También se verificó el acceso al reporte de cumplimiento desde la pantalla de equipo y su filtro por tablero.

En el detalle se verificó una tarea con una entrega rechazada con observación y una segunda entrega aprobada. En otra tarea se comprobó que el cambio de responsable cerró la asignación anterior y dejó una nueva asignación vigente. La tarea se encontraba en estado `POR_HACER` al reasignarla. El reporte de prueba cURL documenta una respuesta `200 OK` y la conservación del estado y del historial según el resultado observado por la estudiante.

En la versión local se comprobó la cancelación del temporizador y el cambio de trabajo a descanso. Al terminar una sesión de trabajo, el reporte reflejó 25 minutos en la fila de la integrante que completó la sesión y en el total del equipo.

---

# 5. Uso de IA generativa

- **Herramientas utilizadas:** ChatGPT y Gemini.
- **Actividad para la que se utilizó:** apoyo en la integración de pantallas, diagnóstico del error de guardado del Pomodoro preparación de pruebas manuales y análisis UX. Gemini se utilizó para generar el prototipo comparativo. Se eligió UV Nocturno como dirección visual.
- **Código o contenido generado:** propuestas de ajustes en Java, HTML y JavaScript, y pasos para verificar la integración.
- **Modificaciones realizadas por el equipo:** adaptación de las rutas y clases al proyecto y corrección del enlace al reporte.
- **Forma en que se verificó el resultado:** ejecución de las operaciones en el navegador y comprobación de los cambios en el detalle de tareas y en el reporte de cumplimiento.

---

# 6. Participación individual

- **Sara Ortega Hernández:** completó la gestión de integrantes en la pantalla de equipo. Incorporó el selector de rol al agregar integrantes y el enlace para consultar el reporte de cumplimiento desde esa misma pantalla. También publicó la consulta y validación de sesiones, las pruebas automatizadas, las instrucciones de instalación, el esquema SQL y los reportes de Pomodoro y del prompt de diseño.
- **María de Lourdes Méndez Prigadaá:** ajustó el preflight de CORS, integró las pantallas con la API, expuso los historiales y registró los cambios de guardado de sesiones y generación del reporte. Realizó las pruebas funcionales de la integración y publicó el reporte del prototipo comparativo y la selección de UV Nocturno.
- **Aradan Schwartz Rocha:** implementó el temporizador Pomodoro en el frontend, con fases de trabajo y descanso y una opción para cancelar la cuenta regresiva. Al completar una fase de trabajo, el temporizador envía a la API la solicitud para guardar la sesión. Publicó el reporte de la prueba cURL de reasignación.

---

# 7. Próximas actividades

- Verificar que una tarea en progreso conserve su estado cuando cambie de responsable.
- Verificar que una entrega registrada a tiempo siga contándose como puntual si se aprueba después del vencimiento.
- Completar los ajustes visuales de la interfaz y repetir las pruebas de integración sobre la versión de entrega.

---

## Observaciones adicionales

Las pruebas de integración descritas se realizaron en un entorno local. El reporte toma como corte de `main` el commit [c01265c](https://github.com/SaraOrtegaHdez13/desarrollo-web/commit/c01265c92fc947387740345f66c2fc2f5c6146a0). Se constató la presencia de las pruebas automatizadas en el repositorio. Su ejecución queda por verificar. La integración del detalle y el temporizador al tablero aún está pendiente de publicar en `main`. Al revisar el repositorio, la última etiqueta disponible era `E3`.

**Release E4:** Pendiente de publicación.
