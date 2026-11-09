# Reporte de Uso Documental en el Proyecto

**Fecha:** 09/11/2026
**Número de reporte: 1**

\---

# 1\. Breve descripción de uso

Creación del archivo endpoints.md en base a los archivos de: requerimientos.md, base\_de\_datos.md y distribución\_responsabilidades.md



\---

# 2\. Evidencias de uso

|Fecha|Herramienta|Enlace a prompt|Enlace a respuesta|
|-|-|-|-|
|Día 2|Claude|[PROMT 1](#promt-1)|[RESPUESTA A PROMT 1](#respuesta-promt-1)|



##### PROMT 1

Con los archivos de diseño listos, genérame el archivo de endpoints.md. Esto servirá para esta primera entrega y sobre la marcha se podrán ir modificando.



##### Respuesta PROMT 1

Listo, el archivo generado es endpoints.md cuenta con los endpoints repartidos por módulos (Sara para gestión de usuarios/equipos, María de Lourdes para tableros/tareas y Aradan para reportes/sesiones), alineados con las reglas de negocio y permisos del sistema.



Qué contiene:



Convenciones: Nombres de ruta orientados a recursos y acciones, uso de verbos HTTP estándar, formato JSON en camelCase, y la regla de no recibir el ID del usuario autenticado en la petición.



Tablas por módulo: Detalle de método, ruta, requerimiento que cubre, roles con acceso, datos de entrada/salida y códigos de respuesta (200, 201, 400, 401, 403, 404) conforme a la matriz de permisos.



Contratos de datos: Definición de las llaves exactas para los objetos JSON entre frontend y backend.



Relación con pantallas y pruebas: Asignación de endpoints a vistas del sistema y ejemplos de flujo mediante comandos cURL.



Registro de cambios: Espacio para documentar modificaciones posteriores sobre la marcha.



Decisiones de diseño integradas:



Transiciones de estado separadas: Endpoints independientes para acciones específicas (iniciar, enviar a revisión, aprobar, rechazar) facilitando el control de permisos y validaciones particulares.



Orden de comprobaciones estandarizado: Secuencia predecible de códigos de error (401 autenticación, 404 existencia, 403 autorización y 400 validación).



Formatos de error: Uso estándar de { "mensaje": "..." } para cumplir con la notificación en interfaz (RNF-09).

