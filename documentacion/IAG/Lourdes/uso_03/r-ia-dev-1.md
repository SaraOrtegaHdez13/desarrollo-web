# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 13/11/2026 
**Número de reporte: 1**

\---

# 1\. Breve descripción de uso

Se utilizó ChatGPT para revisar el repositorio ITablero.java y comprobar su correspondencia con el diseño de la base de datos y los endpoints definidos. La revisión permitió identificar que el método findByEquipoId devolvía un solo tablero, aunque un equipo puede tener varios. Se recomendó cambiar su tipo de retorno a List<Tablero> para permitir consultar todos los tableros asociados a un equipo.

\---

# 2\. Evidencias de uso

|Evidencia|Formato|
|-|-|
|Herramienta|ChatGPT|
|Modelo/temperatura|GPT-6|
|Prompt original|[PROMT 1](#promt-1)|
|Respuesta de IA|[RESPUESTA A PROMT 1](#respuesta-promt-1)|
|Código anterior|Commit (c384e0d)|
|Código posterior|Commit (6b0d004)|
|Diferencias entre versiones|git diff --numstat c384e0d 6b0d004<br /><br />1       0       .gitignore<br />275     0     backend/src/main/java/mx/uv/luzioteamtime/controlador/EquipoControlador.java<br />28      0       backend/src/main/java/mx/uv/luzioteamtime/modelo/EquipoRegistro.java<br />69      0       backend/src/main/java/mx/uv/luzioteamtime/modelo/EquipoResumen.java<br />33      0       backend/src/main/java/mx/uv/luzioteamtime/modelo/MiembroRegistro.java<br />69      0       backend/src/main/java/mx/uv/luzioteamtime/modelo/MiembroResumen.java<br />3       1       backend/src/main/java/mx/uv/luzioteamtime/repositorio/ITablero.java<br />524     0       backend/src/main/java/mx/uv/luzioteamtime/servicio/EquipoServicio.java|
|Prueba de funcionamiento|Con una sesión iniciada previamente con un usuario de rol líder, perteneciente al equipo 1, se ejecuta:<br /><br />curl -i -b cookies.txt \\<br />http://localhost:8081/obtenerTableros/1|
|Código de uso|Modificación / Explicación / Revisión|
|Reflexión del estudiante|El tipo de retorno de los métodos del repositorio debe reflejar la cardinalidad de las relaciones entre entidades.|



\---

# 3\. Promts y respuestas

##### PROMT 1

Haz una revisión de ITablero.java y su correspondencia con el diseño de la base de datos y los endpoints definidos.



##### RESPUESTA PROMT 1



Se revisó el repositorio ITablero.java y su correspondencia con el diseño de la base de datos y los endpoints definidos.



Se identificó que el método:



Tablero findByEquipoId(Integer idEquipo);



presenta una limitación, ya que devuelve un solo tablero por equipo. Sin embargo, de acuerdo con el diseño del proyecto, un equipo puede tener múltiples tableros.



Por ello, realiza el siguiente cambio:



Sustituye findByEquipoId(Integer idEquipo); por List<Tablero> findByEquipoId(Integer idEquipo);



\---

