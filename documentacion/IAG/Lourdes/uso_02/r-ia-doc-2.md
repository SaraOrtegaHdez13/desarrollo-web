# Reporte de Uso Documental en el Proyecto

**Fecha:** 08/11/2026
**Número de reporte: 2**

\---

# 1\. Breve descripción de uso

Modificar el borrador del archivo base\_de\_datos.md adaptándolo al archivo modificado en su versión final de requerimientos.md

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

Evalúa ahora como está reportado el documento para la estructura de la base de datos y si va de la mano con los requerimientos finales. Se dejó libre el espacio de "Diagrama entidad - relación" ya que, se está considerando no ponerlo dado que las demás secciones describen explícitamente las tablas, atributos y relaciones

##### Respuesta PROMT 1

En general la base de datos va de la mano con los requerimientos y las 8 tablas cubren todo lo necesario, dejando los permisos y reglas de negocio en la capa de servicio. Encontré algunos desajustes que conviene corregir en el diseño de la base de datos y de su documentación:



Lo que está bien alineado:



El rol vive en miembros\_equipo, y la pareja (usuario, equipo) es única (RN-02).



observacion, fecha\_completada e id\_responsable guardan el estado actual, y el historial vive en revisiones\_tarea y asignaciones\_tarea (RN-11, RN-15).



sesiones.id\_usuario conserva quién hizo cada sesión aunque la tarea se reasigne, y la duración se guarda en segundos (RN-18).



equipos.archivado cubre RF-11 y RN-17.



Desajustes e inconsistencias a corregir en base-de-datos.md:



Reporte sin resumen de equipo (sección de reportes): Falta indicar la fila de "Resumen del equipo" sumando los cálculos de todas las tareas del tablero.



Faltan reglas en la sección correspondiente:



RN-03 (solo miembros ven tableros y tareas, si no 403).



RN-10 (duración mayor que 0; la base no lo garantiza por restricciones de Hibernate, debe validarse en servicio).



RF-09 (al agregar un miembro, comprobar correo registrado y que no esté duplicado).



usuarios.nombre sin NOT NULL: Debe incluirlo acorde al RF-01.



Detalles menores: Ajustar cómo Spring Boot nombra las tablas a snake\_case (requiere @Table), aclarar el uso opcional de ?tablero=, y precisar id\_responsable.



Sobre quitar el diagrama entidad-relación, si deciden omitirlo para evitar redundancias, hay que borrar su sección y renumerar. Si prefieren dejarlo, se puede escribir como bloque de texto en Mermaid. ¿Aplicamos las correcciones necesarias al archivo base-de-datos.md?



##### PROMT 2

Explícate mejor para cada hueco encontrado. Qué es lo encontrado y lo propuesto





##### RESPUESTA PROMT 2

Aquí va cada hueco técnico de la base de datos con un ejemplo para que puedan decidir:



Hueco 1: Eliminar tareas borra la evidencia del reporte.



Lo encontrado: La BD no permite borrar una tarea con filas hijas (sesiones, revisiones, asignaciones), por lo que se propuso borrarlas en cascada. El problema es que si un líder borra una tarea HECHA, el reporte pierde los minutos de pomodoro y la evidencia objetiva.



Lo propuesto: Limitar el borrado únicamente a tareas en estado POR\_HACER que no tengan avance.



Hueco 2: Un miembro removido desaparece del reporte.



Lo encontrado: Al quitar a un colaborador (RN-14), se borra su fila de miembros\_equipo, haciendo que sus tareas e historial de minutos desaparezcan del reporte individual y descuadren el resumen del equipo.



Lo propuesto: Opción B: Armar el reporte desde las asignaciones históricas, mostrando al exintegrante etiquetado como "ex integrante" sin necesidad de agregar columnas nuevas en la BD.



Hueco 3: El responsable de una tarea está guardado en dos lugares.



Lo encontrado: Se usa tareas.id\_responsable para filtros y la tabla asignaciones\_tarea para el reporte. Al reasignar, el servicio hace varios guardados separados, lo que puede generar inconsistencias si la operación falla a la mitad.



Lo propuesto: Utilizar transacciones atómicas (@Transactional) en los métodos del servicio para asegurar que los cambios en ambas tablas ocurran "todo o nada".



##### PROMT 3

1\. Me parece que aquí va algo faltante. Se pueden eliminar tareas por hacer, pero siempre y cuando no haya ningún avance. Y otra cosa diferente es la reasignación de tareas

2\. La B

3\. Sigue sin quedar claro cuál es el hueco



##### RESPUESTA PROMT 3

Eliminar tareas y reasignación: Quedan separadas correctamente. Un líder puede eliminar una tarea solo si está en POR\_HACER y no tiene ningún avance (ni sesiones ni entregas). La reasignación es distinta: una tarea en POR\_HACER o EN\_PROGRESO puede reasignarse conservando su historial y avance en las tablas correspondientes, y eso no cuenta como avance bloqueante para eliminar si sigue en su estado inicial.



Miembros removidos: Decidido por la Opción B. El reporte listará a los miembros actuales más cualquier persona con asignaciones históricas en el equipo, marcándolos como "ex integrante" sin modificar la estructura de tablas.



El tercer hueco (explicado con tablas): El riesgo radica en que la base de datos divide el responsable en dos sitios (tareas.id\_responsable y asignaciones\_tarea). Si un proceso de reasignación realiza dos guardados independientes y falla el segundo, la tarea queda desincronizada entre el tablero y el reporte. Se requiere asegurar la atomicidad a nivel de servicio.



##### PROMT 4

1\. Finalizando, recuerda que al traspasarse una tarea que ya tenía avance, ese avance se conserva y desde ahí sigue trabajando la nueva persona asignada

2\. Sí, se queda así

3\. Ya entendimos, si úsalo



##### RESPUESTA PROMT 4

Se ha actualizado el archivo base-de-datos.md integrando las tres respuestas y las correcciones pendientes en la estructura:



Reasignación y avance: Se documenta que al reasignar una tarea, el avance previo (sesiones y entregas) se conserva y la nueva persona continúa desde ahí. La eliminación queda restringida estrictamente a tareas en POR\_HACER sin historial de trabajo.



Ex integrantes: Implementado mediante la lógica del reporte (Opción B) consultando las asignaciones históricas.



Transaccionalidad: Se incorpora @Transactional (como regla no funcional/técnica en la documentación de la BD) para garantizar la consistencia en las operaciones de escritura múltiple (crear, entregar, aprobar/rechazar y reasignar).



Correcciones aplicadas en base-de-datos.md: Se ajustó usuarios.nombre a NOT NULL, se añadieron las reglas de validación en la capa de servicio (RN-03, RN-05, RN-10, RF-09), se corrigió el nombramiento de tablas con @Table para Hibernate, y se detallaron los filtros del reporte y la fila de resumen de equipo.



##### PROMT 5

Se quita el diagrama entidad-relación. Tareas hechas no se reasignan, no tiene sentido. Se queda el código propuesto (403 para equipos archivados). La explicación de @Transactional se hará hasta que se comience a escribir código. Después de que queden las correcciones aplicadas en este archivo, pasaremos a revisar la distribución de responsabilidades.



##### RESPUESTA PROMT 5

Se han aplicado todas las decisiones directamente en el archivo base-de-datos.md



Diagrama E-R eliminado: Se suprimió la sección del diagrama y se renumeraron las secciones restantes de la base de datos (Diccionario de datos, Relaciones, Reglas que la base no garantiza, Correspondencia con entidades Java y Reporte de cumplimiento).



Restricción de tareas HECHAS: Se actualizó la lógica para prohibir explícitamente la reasignación de tareas en estado EN\_REVISION y HECHA, ya que al estar concluidas el mérito corresponde a quien las realizó.



Código de respuesta: Se fija el código 403 para rechazar operaciones de escritura en equipos archivados (RN-17).



Transaccionalidad: Se deja documentada su aplicación en la BD, aplazando su explicación práctica para el momento de codificación.



Con esto, el archivo base-de-datos.md queda completamente limpio y actualizado.

