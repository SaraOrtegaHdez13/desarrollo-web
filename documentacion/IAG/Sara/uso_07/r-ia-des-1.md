# Reporte de IA Desarrollo en el Proyecto

**Fecha:** 20/11/2026  
**Número de reporte:** 01 - Diseño

\---

# 1\. Breve descripción de uso

Generar un prototipo navegable de LuzioTeamTime para comparar el tablero, el detalle de tarea, el temporizador Pomodoro y el reporte de cumplimiento con los mismos datos de ejemplo. Gemini generó tres propuestas iniciales (A, B y C). Después le pedí que combinara la distribución de C · Enfoque nocturno con los tonos azul y verde de la Universidad Veracruzana. Elegí **D · UV Nocturno (Combinada)** como base visual para el frontend.

\---

# 2\. Evidencias de uso

|Evidencia|Formato|
|-|-|
|Herramienta|Gemini|
|Modelo/temperatura|Hydrogen 4. Temperatura sin especificar.|
|Prompt original|[PROMPT 1](#prompt-1) y [PROMPT 2](#prompt-2)|
|Respuesta de IA|[RESPUESTA PROMPT 1](#respuesta-prompt-1) y [RESPUESTA PROMPT 2](#respuesta-prompt-2)|
|Código anterior|Commit pendiente de registrar.|
|Código posterior|Gemini generó el prototipo HTML final con D como tema predeterminado y A, B y C disponibles para comparación. Commit pendiente de registrar.|
|Diferencias entre versiones|Pendiente registrar la salida de `git diff --numstat` cuando se cuente con ambos commits.|
|Prueba de funcionamiento|[PRUEBA 1. Verificación de la propuesta D](#prueba-1-verificación-de-la-propuesta-d) y [RESPUESTA 1](#respuesta-1).|
|Código de uso|Diseño|
|Reflexión del estudiante|Elegí UV Nocturno porque mantiene la identidad azul y verde de la UV en una interfaz oscura. La barra lateral compacta deja espacio para el tablero y el reloj grande permite consultar el tiempo mientras reviso la tarea. Al comparar las mismas pantallas pude valorar la legibilidad de las tablas, la jerarquía de las acciones y la presencia del Pomodoro.|

##### PROMPT 1

````text
Actúa como diseñador de producto digital y especialista en UX/UI. Diseña tres
propuestas visuales de alta fidelidad para LuzioTeamTime, una aplicación web
académica que combina equipos, tableros, tareas, entregas a revisión y sesiones
Pomodoro vinculadas a una tarea.

OBJETIVO

Quiero ver y comparar tres propuestas antes de elegir una para el frontend real.
Entrega un prototipo visual navegable con datos de ejemplo y un selector
«A · Azul y verde UV / B · Estudio cálido / C · Enfoque nocturno». Al cambiar
de propuesta conserva la pantalla, el rol, el equipo y la tarea seleccionados.
Las tres deben tener los mismos datos y alcance. Diferéncialas mediante
composición, jerarquía, tipografía, densidad y componentes, además del color.

Si el entorno permite generar una vista previa interactiva, úsala. Si solo
permite mockups estáticos, entrega las mismas pantallas para las tres propuestas,
con una vista por pantalla y resolución suficiente para leer toda la interfaz.

FUENTE Y ALCANCE

1. Inspecciona el ZIP adjunto. Parte de frontend/\\\*.html, frontend/js/,
   documentacion/diseno/requerimientos.md,
   documentacion/diseno/endpoints.md y la plantilla cumplimiento.html.
2. Consulta TareaServicio, SesionServicio y CumplimientoServicio cuando
   necesites confirmar reglas de flujo. Si documentación y código difieren,
   usa la distinción en una nota breve junto a la entrega visual.
3. El entregable es un prototipo de comparación. Simula interacciones con
   datos ficticios. Evita modificar el repositorio, conectarte al backend
   o presentar una operación simulada como integración real.
4. Usa «LuzioTeamTime» como marca y español de México en toda la interfaz.
   Conserva alcance adecuado para HTML, CSS y JavaScript, con Thymeleaf
   en la pantalla de reporte.

DIRECCIONES VISUALES

A · AZUL Y VERDE UV
- Paleta inspirada en la Universidad Veracruzana. Azul #005A9C, azul profundo
  #003B66, verde #007D45, fondo #F2F6FA, superficie #FFFFFF, texto #15283B.
  Usa estos valores como paleta de diseño y conserva la marca LuzioTeamTime.
- Tipografía Source Sans 3, barra superior azul y navegación por migas de pan.
  Paneles de bordes discretos, esquinas aproximadas de 8 px y tabla compacta.
- Tablero con título, filtros y tabla alineados a una misma retícula. En el
  detalle, descripción e historial a la izquierda, Pomodoro a la derecha.
  Usa verde para el progreso del reloj y confirmaciones con texto explícito.

B · ESTUDIO CÁLIDO
- Fondo #FBF7EF, superficie #FFFFFF, terracota #A23D2B, verde bosque #365C4A,
  texto #27241F. Tipografía DM Sans, esquinas aproximadas de 16 px, espacios
  generosos y bordes cálidos.
- Menú lateral claro y tarjetas para equipos. Mantén una tabla en escritorio
  para el tablero, con filas más espaciadas y acciones secundarias agrupadas.
- Detalle con un panel de concentración amplio. Sitúa el reloj junto a la
  tarea y su próxima acción. Usa terracota para la acción primaria y verde
  bosque para el descanso. Mantén visibles la fecha límite y la observación
  de rechazo cuando corresponda.

C · ENFOQUE NOCTURNO
- Fondo #101521, superficie #192131, bordes #39465E, texto #F1F5FC,
  lavanda #C4B5FD y turquesa #5EEAD4. Usa Inter y números tabulares en
  IBM Plex Mono para el temporizador.
- Navegación lateral compacta, superficies oscuras planas, tabla de alta
  legibilidad y poco adorno. Usa lavanda para acciones y turquesa para
  el progreso del reloj. Emplea texto oscuro sobre botones de color claro.
- Detalle con reloj tipográfico de gran tamaño y línea de tiempo compacta.
  El reporte conserva una tabla clara, con cifras alineadas y totales visibles.

PANTALLAS OBLIGATORIAS EN LAS TRES PROPUESTAS

1. Inicio de sesión y registro.
   Inicio con correo, contraseña y acceso al registro. Registro con nombre,
   correo y contraseña. Incluye etiquetas, validación junto al campo y mensajes
   de envío o error. Usa el nombre de la aplicación como identidad principal.

2. Mis equipos.
   Distingue equipos activos y archivados. En cada equipo muestra nombre,
   descripción cuando exista y rol de la persona. Incluye «Crear equipo» y
   «Cerrar sesión». En un equipo archivado, muestra «Solo lectura» y conserva
   el acceso a sus tableros y reporte.

3. Detalle de equipo.
   Incluye nombre, descripción, integrantes con rol, tableros y «Ver reporte
   de cumplimiento». Para el líder, muestra acciones de agregar integrantes,
   gestionar roles, crear tablero y archivar cuando correspondan. Si el equipo
   tiene un solo líder, explica la restricción de asignar tareas a un líder
   junto al selector de responsable cuando afecte la acción.

4. Tablero.
   Incluye filtros por estado y responsable y una opción para limpiarlos.
   Muestra título, responsable, estado, fecha límite y acciones. El título
   abre el detalle de tarea. Usa los cuatro estados exactos: Por hacer,
   En progreso, En revisión y Hecha.
   La creación de tarea es para líder de un equipo activo. El formulario
   incluye título, descripción, fecha límite y responsable. Usa una tabla
   en escritorio y filas apiladas o tarjetas de lectura en móvil. Las
   transiciones se hacen con acciones explícitas, sin arrastrar tarjetas.

5. Detalle de tarea con Pomodoro.
   Muestra título, descripción, responsable, estado, fecha límite, acciones
   e historial de entregas y asignaciones. Distingue envío y revisión en
   eventos separados. Si hay rechazo, muestra la observación con su entrega.
   El Pomodoro debe mostrar siempre el nombre de la tarea, la fase y el tiempo
   restante. Usa trabajo de 25:00 y descanso de 05:00. Incluye «Iniciar
   trabajo», «Cancelar temporizador» durante el conteo e «Iniciar descanso»
   cuando se habilite. El descanso comienza por acción de la persona.
   Representa sesión en curso, guardando, sesión de trabajo guardada, descanso
   terminado, cancelación y error al guardar. Solo simula el registro del
   bloque de trabajo completado. Usa un modo de demostración claramente
   etiquetado para mostrar el final sin esperar 25 minutos.
   Habilita el reloj solo para la persona responsable de una tarea En progreso
   en un equipo activo. El resto puede consultar la tarea.

6. Reporte de cumplimiento.
   Incluye filtro «Todos los tableros» o tablero específico. Presenta tareas
   asignadas, porcentaje de hechas, entregas a tiempo, minutos de Pomodoro,
   tareas aprobadas y total del equipo. El porcentaje usa las tareas aprobadas
   sobre las asignadas. Conserva el historial de entregas, observaciones y
   traspasos, y la etiqueta «Ex integrante» cuando corresponda. Los minutos
   se atribuyen a la persona que realizó la sesión. Usa los mismos datos
   ficticios en los tres diseños.

REGLAS DE NEGOCIO Y ESTADOS

- El rol es por equipo. Solo el responsable puede iniciar su tarea, trabajar
  con Pomodoro y enviarla a revisión. Solo un líder distinto del responsable
  puede aprobar o rechazar una tarea En revisión.
- Rechazar exige una observación y devuelve la tarea a En progreso. Cada
  reentrega genera otro evento de entrega. Aprobar cambia la tarea a Hecha.
- Hay como máximo dos líderes. Con un solo líder se pueden asignar tareas a
  colaboradores. Con dos, también se pueden asignar a los líderes.
- Muestra editar, eliminar o reasignar únicamente en contextos permitidos.
  Eliminar exige Por hacer sin avance. Reasignar queda fuera de En revisión
  y Hecha. RF-18/RN-15 pide conservar el estado al reasignar, pero el ZIP
  lo reinicia. Presenta esa diferencia en la nota de alcance y evita simular
  la continuidad como comportamiento validado.
- RN-12 pide medir puntualidad por la fecha de entrega de una tarea aprobada.
  El ZIP establece fechaCompletada al aprobar. Separa ambos momentos en la
  interfaz y marca cualquier cifra de puntualidad como dato ilustrativo
  mientras se verifica la regla en el backend.
- Incluye estados de carga, lista vacía, filtros sin resultados, error de
  guardado y equipo archivado. Cada error debe explicar qué pasó y ofrecer
  una acción de recuperación cuando corresponda.

DATOS COMUNES PARA COMPARAR

Usa el equipo ficticio «Proyecto LISTI» y el tablero «Entrega 4». Incluye
dos líderes, Valeria Torres y Diego Ruiz, y dos colaboradores, Alicia Ríos
y Mateo Vega. Usa las mismas tareas en los tres diseños:

- «Preparar casos de prueba» · Por hacer · Mateo.
- «Validar registro de sesiones» · En progreso · Alicia.
- «Revisar formulario de acceso» · En revisión · Mateo.
- «Documentar navegación» · Hecha · Alicia.

En las tres variantes abre el tablero con la misma selección. El flujo demo
parte de «Validar registro de sesiones» como Alicia y permite mostrar un
Pomodoro en curso, su guardado simulado y el envío a revisión. Añade un selector
de persona de demostración, fuera de la navegación del producto, para mostrar
cómo Valeria revisa la entrega. Incluye al menos una variante con observación
de rechazo y un caso con fecha de entrega anterior a la fecha de aprobación.

RESPONSIVIDAD, ACCESIBILIDAD Y ENTREGA

- Prepara escritorio a 1440 px y móvil alrededor de 390 px. En móvil valida
  como mínimo el tablero y el detalle de tarea. La acción del temporizador
  debe caber en pantalla sin tapar la descripción ni el historial.
- Mantén contraste de al menos 4.5:1 para texto normal, foco visible,
  navegación por teclado, etiquetas asociadas a campos y estados expresados
  con texto además de color. El contador cambia visualmente cada segundo.
  Anuncia a tecnologías de asistencia los cambios de fase y de guardado
  sin anunciar cada segundo.
- Si usas un diálogo para crear tareas o rechazar entregas, conserva el foco
  dentro del diálogo mientras esté abierto y devuélvelo a su activador al
  cerrar. Muestra validación de observación obligatoria junto al campo.
- Usa iconos consistentes, fechas legibles, cifras alineadas y texto real.
  Prioriza la información de usuario y las acciones. Evita ilustraciones
  decorativas, métricas inventadas del producto y pantallas fuera del alcance.
- Entrega primero la vista previa completa. Después incluye una tabla breve
  con las diferencias visuales entre A, B y C y una nota con las funciones
  simuladas. Antes de finalizar, comprueba que sea posible comparar la misma
  tarea, probar el temporizador y abrir el reporte en las tres propuestas.

````

##### RESPUESTA PROMPT 1

A continuación se presenta el prototipo interactivo navegable de **LuzioTeamTime**, diseñado para comparar en tiempo real las tres direcciones visuales (**A · Azul y verde UV**, **B · Estudio cálido** y **C · Enfoque nocturno**) manteniendo intacto el estado de navegación, rol de demostración, equipo, tablero, tarea activa y temporizador Pomodoro al alternar entre ellas.

###### Comparativa visual entre las tres propuestas

|Criterio|A · Azul y verde UV|B · Estudio cálido|C · Enfoque nocturno|
|-|-|-|-|
|**Paleta y contraste**|Institucional UV: Azul `#005A9C`, azul profundo `#003B66`, verde `#007D45` sobre fondo claro `#F2F6FA` y texto `#15283B`.|Editorial cálida: Fondo crema `#FBF7EF`, superficies `#FFFFFF`, terracota `#A23D2B`, verde bosque `#365C4A` y texto `#27241F`.|Oscura plana de baja fatiga visual: Fondo `#101521`, superficie `#192131`, texto `#F1F5FC`, botones lavanda `#C4B5FD` (con texto oscuro `#101521`) y progreso turquesa `#5EEAD4`.|
|**Tipografía**|`Source Sans 3` en toda la interfaz, jerarquía sobria y académica.|`DM Sans` con títulos amplios y espaciado generoso.|`Inter` para interfaz y `IBM Plex Mono` con cifras tabulares (`tabular-nums`) para el reloj y el reporte.|
|**Navegación**|Barra superior institucional azul con ruta por migas de pan (*breadcrumbs*) debajo.|Menú lateral izquierdo claro con navegación editorial y tarjeta de rol abajo.|Barra lateral (*rail/sidebar*) oscura compacta con indicador activo en turquesa.|
|**Densidad y geometría**|Compacta, paneles con bordes discretos de `1px` y esquinas de `8px`.|Amplia, bordes cálidos `#E5DDD0` y esquinas redondeadas de `16px`.|Plana sin sombras, bordes `#39465E`, esquinas de `6px–10px` y alta legibilidad en tablas.|
|**Detalle y Pomodoro**|Dos columnas (`62%` descripción e historial a la izquierda, `38%` Pomodoro a la derecha).|Panel de concentración amplio integrado arriba con el reloj junto al título, fecha límite, observación de rechazo y acción primaria.|Reloj tipográfico de gran formato (`4rem`) a la izquierda y línea de tiempo compacta a la derecha.|

\---

###### Nota sobre alcance, diferencias entre documentación/código y funciones simuladas

1. **Diferencia RF-18 / RN-15 (Reasignación de tareas):** La documentación funcional plantea conservar el estado actual de la tarea cuando un líder la reasigna, mientras que la implementación actual en `TareaServicio` reinicia la tarea a `Por hacer`. En este prototipo se muestra una nota explícita dentro del diálogo de reasignación y se registra el traspaso en el historial sin presentarlo como comportamiento cerrado del backend.
2. **Diferencia RN-12 (Puntualidad: entrega vs. aprobación):** La regla RN-12 mide la puntualidad según la fecha en que el responsable entrega la tarea, mientras que `TareaServicio` registra `fechaCompletada` al momento en que el líder la aprueba. En el detalle de tarea («Documentar navegación») y en el reporte Thymeleaf se muestran ambos momentos por separado (`Entregada: 17 nov 2026 · Aprobada: 18 nov 2026`) y la columna *Entregas a tiempo* está marcada como **dato ilustrativo**.
3. **Operaciones simuladas:** El cambio de persona demo, el envío de formularios, los estados de carga/vacío/error/archivado y los botones del *Modo de demostración* del Pomodoro simulan las respuestas de los servicios (`TareaServicio`, `SesionServicio`, `CumplimientoServicio`) en memoria sin realizar peticiones de red.

[Conversación original en Gemini](https://share.gemini.google/lYhaGvlV5Mit)

##### PROMPT 2

> podrías combinar el modo oscuro y el estilo de diseño, pero con los tonos azul y verde de la UV?

En mensajes posteriores pedí agregar iconos, ajustar el tamaño de los componentes para aprovechar el espacio en escritorio e incorporar efectos visuales.

##### RESPUESTA PROMPT 2

Gemini añadió **D · UV Nocturno (Combinada)** y la activó por defecto en el prototipo comparativo. La propuesta toma la barra lateral compacta y la distribución del detalle de tarea de C · Enfoque nocturno y adapta la paleta institucional a un fondo azul noche.

|Elemento|Diseño final de la propuesta D|
|-|-|
|Paleta|Fondo azul noche `#0B1420`, tarjetas `#111F30`, bordes `#264161` y barra lateral `#16283E`. Azul `#4DA3FF` para acciones principales y verde `#2EC47A` para el progreso del Pomodoro y los estados aprobados.|
|Tipografía y Pomodoro|Inter para la interfaz e IBM Plex Mono con cifras tabulares para el reloj y los datos numéricos. En escritorio, el panel del temporizador ocupa una columna de 410 px y el reloj usa cifras de 72 px.|
|Iconografía|Iconos SVG acompañan etiquetas en la navegación, las acciones de tareas y Pomodoro, los filtros, las fechas y los avisos de solo lectura.|
|Tamaño e interacción|Área principal de hasta 1440 px en escritorio y vista de demostración móvil de 390 px. Transiciones de pantalla, elevación al pasar el cursor y pulso visual durante el funcionamiento del temporizador.|

[Conversación de seguimiento en Gemini](https://share.gemini.google/MWqPrDzMIiLl) · [Prototipo HTML final](https://chatgpt.com/library?file_id=libfile_07b1c306fc8481919b58e5ab6fa5c5d1&account_id=personal)

##### PRUEBA 1. Verificación de la propuesta D

Abrir el HTML final y comprobar que el selector muestre **D · UV Nocturno (Combinada)** al cargar «Mis equipos». Abrir «Detalle y Pomodoro» para comprobar la distribución del reloj y alternar entre D, A, B y C para verificar que el cambio de tema conserve la vista y el temporizador.

##### RESPUESTA 1

La captura de Gemini muestra la propuesta D activa en «Mis equipos», con la barra lateral oscura, los iconos de navegación y los botones azules. El HTML final confirma que el estado inicial es `theme: 'D'` y la comprobación de sintaxis de JavaScript pasó. Queda pendiente ejecutar en navegador el recorrido de «Detalle y Pomodoro» y verificar la continuidad del temporizador al cambiar de tema.

[Captura de «Mis equipos» con la propuesta D activa](https://chatgpt.com/library?file_id=libfile_bf1605e8e82c8191ae0fa98f8a087d46&account_id=personal)

