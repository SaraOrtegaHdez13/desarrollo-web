# Reporte de Avance del Proyecto

**Equipo:** Sara, Lourdes, Aradan
**Fecha:** 09/11/2026
**Número de reporte:** 1
**Repositorio GitHub:** https://github.com/SaraOrtegaHdez13/desarrollo-web

\---

# 1\. Actividades realizadas

* Creación de la estructura inicial del repositorio y del proyecto Spring Boot base (esqueleto con los subpaquetes de las tres capas en mx.uv.luzioteamtime), del cual parten las tres ramas de trabajo.
* Creación de la estructura de carpetas de la documentación.
* Redacción de requerimientos.md: requerimientos funcionales, reglas de negocio y matriz de permisos.
* Diseño de la base de datos (base\_de\_datos.md): diccionario de datos de 8 tablas (usuarios, equipos, miembros\_equipo, tableros, tareas, revisiones\_tarea, asignaciones\_tarea, sesiones), relaciones, reglas validadas en la capa de servicio, correspondencia con las entidades Java y origen de cada dato del reporte de cumplimiento.
* Definición de los endpoints iniciales de la API (endpoints.md): 12 endpoints del módulo A, 13 del módulo B y 3 del módulo C, contratos JSON, endpoints por pantalla y flujo de pruebas con CURL.
* Asignación de módulos, dependencias entre módulos y calendarización por hito (distribucion\_responsabilidades.md).
* Documentación del uso de IA generativa en la creación de cada archivo de diseño (reportes de IA de endpoints.md, requerimientos.md, base\_de\_datos.md y distribucion\_responsabilidades.md).

\---

# 2\. Evidencias de trabajo

|Actividad|Integrante responsable|Enlace a commit|
|-|-|-|
|Creación del repositorio y del esqueleto Spring Boot del que parten las tres ramas|Sara Ortega Hernández|b7609bf|
|Organización de las carpetas donde se guarda la documentación del proyecto|Sara Ortega Hernández|0a0bb20|
|Análisis del sistema: definición de requerimientos, reglas de negocio y permisos por rol|María de Lourdes|0bdf3d5|
|Modelado de la base de datos: tablas, relaciones y reglas de validación|María de Lourdes|05b6700|
|Reparto del trabajo entre los integrantes, dependencias entre módulos y calendario por hito|Aradan|6cac9f1|
|Diseño de los endpoints de la API y de los formatos JSON de entrada y salida|Sara Ortega Hernández|a28012a|
|Registro del uso de IA en la elaboración del documento de endpoints|Sara Ortega Hernández|3e639ed|
|Registro del uso de IA en la elaboración del documento de requerimientos|María de Lourdes|d4c57d2|
|Registro del uso de IA en la elaboración del documento de la base de datos|María de Lourdes|8d7abd5|
|Registro del uso de IA en la elaboración del documento de distribución de responsabilidades|Aradan|77348c9|

\---

# 3\. Problemas encontrados

* Nombres de tablas de Spring Boot que por defecto genera nombres en singular (p. ej. miembro\_equipo). Se resolvió con @Table(name = "...") en cada entidad.
* Consistencia entre tablas al reasignar o entregar tareas: tareas.id\_responsable y la asignación vigente podían contradecirse. Se resolvió con @Transactional en los métodos que escriben en varias tablas (RNF-13). Esta anotación no se vio en clase, pero se comprendió la importancia de implementarla para los fines aplicados en nuestro proyecto.
* Reglas que la base de datos no garantiza (máximo dos LIDER, equipo archivado, transiciones de estado): se definieron para validarse en la capa de servicio.
* Precisión del tiempo de pomodoro: se guarda la duración en segundos para evitar errores de redondeo al acumular sesiones.
* Dependencias entre módulos: cada módulo ofrece un servicio que los demás consultan para no bloquearse.

\---

# 4\. Avance funcional

**Operativo**



* Esqueleto del proyecto Spring Boot base en main, con la estructura de carpetas de la documentación.



**Completado en diseño, aún sin implementar**



* Requerimientos, reglas de negocio y matriz de permisos.
* Diseño de la base de datos.
* Definición de endpoints y contratos JSON.
* Distribución de responsabilidades y calendario.



**Pendiente**



* Todo el backend (módulos A, B y C), el frontend, el reporte de cumplimiento, database/esquema.sql, README.md y las pruebas.

\---

# 5\. Uso de IA generativa



* **Herramienta utilizada:** Claude
* **Actividad para la que se utilizó:** Apoyo en la redacción de los documentos de diseño: requerimientos.md, base\_de\_datos.md, endpoints.md y distribucion\_responsabilidades.md.
* **Código o contenido generado:** Documentos finales de diseño, basados en las correcciones aplicadas a los borradores proporcionados.
* **Modificaciones realizadas por el equipo:** Corrección de rutas proporcionadas, reordenamiento de secciones, eliminación de datos extras innecesarios.
* **Forma en que se verificó el resultado:** Revisión cruzada entre documentos (reglas RN, requerimientos RF y endpoints) y revisión de consistencia entre los tres integrantes.

\---

# 6\. Participación individual

* **Sara Ortega Hernández:** Creación de la estructura inicial del repositorio y el proyecto Spring Boot base, la estructura de carpetas de la documentación, los endpoints iniciales de la API y el reporte de uso de IA de este mismo.
* **María de Lourdes Méndez Prigadaá:** Redacción de los requerimientos funcionales, reglas de negocio y matriz de permisos, el diseño de la base de datos y los reportes de uso de IA de ambos archivos.
* **Aradan Schwartz Rocha:** Redacción de la asignación de módulos, dependencias y calendarización, con su respectivo reporte de uso de IA.

\---

# 7\. Próximas actividades

* Lourdes creará primero las entidades y repositorios de sus 4 tablas: Tablero, Tarea, RevisionTarea, AsignacionTarea
* Sara creará las entidades y repositorios de Usuario, Equipo y MiembroEquipo. Aradan los de SesionPomodoro, todas con @Table.
* Sara implementará crearUsuario, iniciarSesion, cerrarSesion y el SesionInterceptor
* Aradan configurará CORS y verificará la conexión a MySQL con la variable de entorno BASE\_DATOS
* Lourdes implementará crearTablero y crearTarea.

\---

## Observaciones adicionales

* Los documentos de diseño están versionados y se actualizan sobre la marcha; endpoints.md tiene un registro de cambios.
* **Release del hito:** https://github.com/SaraOrtegaHdez13/desarrollo-web/releases/tag/E1

