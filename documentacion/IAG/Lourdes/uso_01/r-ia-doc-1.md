# Reporte de Uso Documental en el Proyecto

**Fecha:** 08/11/2026
**Número de reporte: 1**

\---

# 1\. Breve descripción de uso

Modificaciones al borrador de requerimientos.md en base a las últimas decisiones tomadas como equipo. 



\---

# 2\. Evidencias de uso

|Fecha|Herramienta|Enlace a prompt|Enlace a respuesta|
|-|-|-|-|
|Día 1|Claude|[PROMT 1](#promt-1)|[RESPUESTA A PROMT 1](#respuesta-promt-1)|
|Día 1|Claude|[PROMT 2](#promt-2)|[RESPUESTA A PROMT 2](#respuesta-promt-2)|

## Observaciones adicionales

La respuesta al PROMT 2 generó el archivo requerimientos.md que fue subido en el commit 0bdf3d506afd4a955d3b60d19fb127a41f85e014 "Requerimientos funcionales, reglas de negocio y matriz de permisos"



##### PROMT 1

Ajusta las secciones del documento para que coincidan con las últimas decisiones tomadas por el equipo. Ya que hay inconsistencias, por mencionar algunas: un solo líder cuando son 2, entre líderes se revisan las tareas asignadas, si en el equipo solo hay un líder a este no se le pueden asignar tareas, etc.



##### Respuesta PROMT 1

Voy a revisar el archivo real y lo ya guardado del proyecto antes de corregir.



Ya ajusté requerimientos.md. Estos son los cambios, por sección.



Contradicciones que corregí



Sección 1 (Descripción): decía “un líder revisa”. Ahora dice que hay dos líderes, que uno revisa al otro y a los colaboradores, y que nadie aprueba su propia tarea.

Sección 4 (Roles): decía “Todo equipo funciona con dos LIDER”, pero RN-05 dice que al crearlo hay uno. Ahora explica que el máximo es dos, que se nace con uno y que el segundo se agrega o se promueve. Añadí una tabla corta que compara qué cambia con un LIDER y con dos.

RF-14 y RF-18: decían “cualquier miembro, incluido un LIDER” sin condición. Ahora un LIDER solo puede ser responsable cuando el equipo ya tiene dos (RN-06). RF-18 además menciona que no se reasigna una tarea EN\_REVISION (RN-15).

RF-10 y RN-05: con un solo LIDER no quedaba claro si se podía degradar o quitar. Ahora se prohíbe, porque si no el equipo se quedaría sin líder. También aclaré que el segundo LIDER puede venir de promover a un COLABORADOR.

RN-06: ahora cubre también la reasignación y dice que con dos LIDER ya se les puede asignar tareas.

Sección 7 (Matriz): las columnas LIDER y COLABORADOR ahora se explican como rol en ese equipo, y quien no es miembro recibe 403. Las filas de miembros y de tareas incluyen las reglas de máximo dos LIDER y de asignación a un LIDER.

Sección 10 (Decisiones): las decisiones 1 y 2 reflejan la etapa de un solo líder.



Cambios menores



Quité las barras invertidas raras (miembros\\\\\\\_equipo, POR\\\\\_HACER, 1\\.) que se veían en el texto.

Puse el nombre completo de Sara y “María de Lourdes” en el encabezado, y agregué la versión.

RF-26 ahora dice que el reporte lo ve cualquier miembro.



Dos puntos que no cambié porque necesitan decisión del equipo



Equipo archivado: RN-17 dice que las escrituras “se rechazan”, pero no con qué código. Propongo 403. ¿Lo confirman?

Reasignar una tarea HECHA: RN-15 solo prohíbe reasignar EN\_REVISION. Reasignar una HECHA cambiaría a quién se le acredita la aprobación en el reporte. Yo la prohibiría también, pero es decisión de ustedes.



Como requerimientos.md es base de sus pruebas y de lo que defienden, actualicen también base-de-datos.md y distribucion-responsabilidades.md si mencionan estas reglas, para que los tres documentos no se contradigan.



##### PROMT 2

Te corrijo:



DESCRIPCIÓN

Equipos pequeños pero enfocado en el ámbito escolar, no solo a grupos de forma general. 

El reporte se hace tanto individual como por equipos.

Si no hay dos líderes, entonces al líder no se le asignan tareas

Aunque siento que ese tipo de especificaciones más técnicas no van en descripción. Bastaría con decir el enfoque del proyecto, o el propósito. Porque en las demás secciones si se está especificando, pero en esta sección de descripción parece que se sigue contradiciendo



##### RESPUESTA PROMT 2 

Tienen razón: la descripción debe decir solo el propósito y el enfoque. Reescribí la sección 1 con enfoque escolar y reporte individual y por equipo, y quité de ahí todo lo técnico sobre líderes. Las reglas de líderes quedan solo en Roles, RN-05, RN-06 y la matriz.



La descripción ahora dice:



LuzioTeamTime es un sistema web para que equipos pequeños del ámbito escolar (por ejemplo, equipos que desarrollan un proyecto de clase) organicen y den seguimiento a su trabajo. Permite repartir tareas en tableros, revisarlas antes de darlas por terminadas y medir el tiempo de trabajo con la técnica pomodoro. Además genera un reporte de cumplimiento, tanto individual como por equipo.



Para que el resto del documento coincida con lo que corrigieron, hice estos cambios:



Sección 2 (Problema): decía “estudiantes o de trabajo”. Ahora habla de equipos de estudiantes en proyectos escolares y de evidencia del cumplimiento “de cada integrante y del equipo”.

RF-27 y reglas del reporte: el reporte solo tenía indicadores por integrante. Agregué un “Resumen del equipo” con los mismos cinco indicadores sumados. Esto es un requisito nuevo, no solo redacción. Si por “por equipos” querían algo distinto, díganme y lo ajusto.



Como el resumen de equipo cambia el reporte, afecta a Aradan (módulo C). Si ya tienen consultas o la plantilla Thymeleaf hechas, hay que sumar ese bloque.





