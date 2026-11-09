# Reporte de Uso Documental en el Proyecto

**Fecha:** 08/11/2026
**Número de reporte: 1**

\---

# 1\. Breve descripción de uso

Modificar el borrador del archivo distribucion\_responsabiidades.md adaptándolo a los archivos modificados en su versión final de: requerimientos.md y base\_de\_datos.md

\---

# 2\. Evidencias de uso

|Fecha|Herramienta|Enlace a prompt|Enlace a respuesta|
|-|-|-|-|
|Día 1|Claude|[PROMT 1](#promt-1)|[RESPUESTA A PROMT 1](#respuesta-promt-1)|
|Día 1|Claude|[PROMT 2](#promt-2)|[RESPUESTA A PROMT 2](#respuesta-promt-2)|
|Día 1|Claude|[PROMT 3](#promt-3)|[RESPUESTA A PROMT 3](#respuesta-promt-3)|
|Día 1|Claude|[PROMT 4](#promt-4)|[RESPUESTA A PROMT 4](#respuesta-promt-4)|
|Día 1|Claude|[PROMT 5](#promt-5)|[RESPUESTA A PROMT 5](#respuesta-promt-5)|





##### PROMT 1

Aplica las correcciones necesarias, en base a los archivos finales de requerimientos y base de datos



##### Respuesta PROMT 1

Actualicé distribucion-responsabilidades.md a su nueva versión, alineándolo con el reparto de módulos y los cambios recientes de la base de datos y la lógica del sistema:



Módulo A (Sara): Se detallan las reglas de líderes (máximo dos, el único líder no se quita ni degrada), las validaciones de correo al agregar miembros (404 si no existe, 400 si ya está en el equipo), la remoción de miembros con tareas pendientes (RN-14) y el control de archivado de equipos (RN-17).



Módulo B (María de Lourdes): Se asigna la validación de asignar un líder solo si el equipo tiene dos (RN-06), la restricción de no reasignar tareas en revisión o hechas, la eliminación de tareas exclusivamente en POR\_HACER sin avance (RN-19), y el uso de transacciones (@Transactional) en los métodos que realizan múltiples guardados.



Módulo C (Aradan): Se contempla el reporte por integrante y por equipo (con tablero opcional y manejo de exintegrantes), así como la validación de sesiones con duración mayor a 0 en equipos no archivados.



Organización y dependencias: Se ajustó la nomenclatura de las carpetas de IA (integrante\_01, 02, 03), se asignaron responsables para cada requerimiento no funcional (RNF), y se establecieron claramente las dependencias técnicas cruzadas entre los módulos para evitar bloqueos durante el desarrollo.



##### PROMT 2

Las fechas deben quedarse iniciando a partir del 7 de noviembre.



Aprobamos las entidades en el Hito 2.



Que Lourdes cree las entidades primero.



Confírmame cómo estás manejando esa parte del esqueleto, ¿para ti qué está siendo el esqueleto?



##### RESPUESTA PROMT 2

Se aplicaron los puntos 1 al 3 en el documento de distribución. Respecto al punto 4, defino el esqueleto como la base mínima indispensable para que el proyecto compile, arranque y se conecte a la base de datos antes de escribir lógica de negocio:



Incluye el pom.xml con las dependencias base y de criptografía, la clase principal de Spring Boot, el archivo application.properties (puerto 8081, conexión por variable de entorno, ddl-auto=update), la configuración de CORS, el .gitignore y los subpaquetes bajo mx.uv.luzioteamtime.



No incluye: el SesionInterceptor de Sara, las entidades/servicios de cada módulo específico, ni el README final.



Definición de responsabilidades: Sara lo crea y lo deja listo en el repositorio, mientras que Aradan se encarga de mantenerlo y gestionarlo a partir del Hito 2.



##### PROMT 3

Ya te entendí, sí fue Sara quien hizo el esqueleto y los demás deben empezar sobre él.



##### RESPUESTA PROMT 3

Queda registrado y actualizado en el documento de distribución:



Módulo A: Sara creó el esqueleto inicial y lo dejó disponible en la rama main para que todos partan de él.



Mantenimiento: Aradan asume el mantenimiento del esqueleto a partir del Hito 2, recomendándose una breve revisión conjunta con Sara para facilitar su defensa técnica.



Se recuerda la importancia de que las ramas de desarrollo (modulo-b, modulo-c) se creen a partir de este main ya estructurado para prevenir conflictos de fusión.



##### PROMT 4

El esqueleto está en main.



El webhook fue configurado antes de commitear la estructura del repositorio.



No usó IA para el esqueleto, se hizo en base a lo visto en clase y el pom fue generado con las dependencias usando Spring Initializr.



##### RESPUESTA PROMT 4

Con estas confirmaciones, el apartado del esqueleto queda validado formalmente en el proyecto.



Dado que se utilizó Spring Initializr y edición manual (para dependencias de seguridad/criptografía añadidas a mano), se documenta que no se empleó IA generativa para esta estructura base.



Los integrantes ya pueden operar de forma segura sobre las ramas correspondientes para iniciar el desarrollo del Hito 2 conforme al calendario establecido a partir del 7 de noviembre.



##### PROMT 5

Lo agregó a mano. Confirmado esto, dame el archivo final de distribución de responsabilidades.



##### RESPUESTA PROMT 5

Archivo subido con el commit 6cac9f1edcbebcf6100d46255b47a24b672d2c76 "Asignación de módulos, dependencias y calendarización". 

