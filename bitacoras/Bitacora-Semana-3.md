# Bitácora Semana 3 — a partir del punto 36

## 37. Casos de prueba mínimos

Probé `busquedaBinariaPorTimestamp` con estos 7 casos:

| Caso | Objetivo | Posición encontrada | ¿Correcto? |
|---|---|---|---|
| Primer elemento | 0000000000 | 0 | Sí |
| Elemento intermedio | 0000500000 | 500000 | Sí |
| Último elemento | 0000999999 | 999999 | Sí |
| Elemento que sí existe (aleatorio) | 0000250000 | 250000 | Sí |
| Elemento que no existe | 9999999999 | -1 | Sí (correcto que no lo encuentre) |
| Arreglo pequeño (n=10) | 0000000005 | 5 | Sí |
| Arreglo grande (n=1.000.000) | 0000999998 | 999998 | Sí |

**Conclusión:** en los 7 casos la posición encontrada coincidió exactamente con la esperada, incluyendo el caso del dato inexistente, que devolvió `-1` correctamente en vez de encontrar algo por error. Esto confirma que la búsqueda funciona bien tanto en los extremos del arreglo (primero/último) como en el medio, y que se comporta igual de bien con un arreglo chico que con uno de un millón de datos.

---

## 38. Tabla de mediciones

| Tamaño | Tiempo lineal (ms) | Tiempo binaria (ms) |
|---:|---:|---:|
| 1.000 | 0,073 | 0,050 |
| 100.000 | 3,822 | 0,563 |
| 1.000.000 | 9,462 | 0,118 |

Se ve clarísimo que entre más grande el arreglo, más se dispara la diferencia: con 1.000 datos casi no se nota (0,073 ms vs 0,050 ms), pero con 1.000.000 la lineal ya se demora como 80 veces más que la binaria (9,462 ms vs 0,118 ms). La lineal crece casi proporcional al tamaño del arreglo, mientras que la binaria casi ni se mueve aunque el arreglo se haga 1000 veces más grande, justo la idea de O(n) contra O(log n).

---

## 39. Traza de búsqueda binaria

Traza real con n=20, buscando el timestamp en la posición 13 (`0000000013`):

| Paso | inicio | fin | medio |
|---|---|---|---|
| 1 | 0 | 19 | 9 |
| 2 | 10 | 19 | 14 |
| 3 | 10 | 13 | 11 |
| 4 | 12 | 13 | 12 |
| 5 | 13 | 13 | 13 |

Resultado: posición encontrada = 13 (5 comparaciones para un arreglo de 20 elementos)

En cada paso el algoritmo compara el timestamp del `medio` con el objetivo: si es menor, descarta toda la mitad izquierda (`inicio = medio + 1`); si es mayor, descarta la mitad derecha (`fin = medio - 1`). Por eso el rango se va partiendo a la mitad cada vez (19 → 9 → 3 → 1 → 0 elementos posibles) hasta cerrar exactamente en la posición 13, y el ciclo termina apenas `inicio` y `fin` se cruzan en el dato correcto.

---

## 44. Preguntas de pensamiento crítico

**Pregunta 1 — ¿vale la pena una búsqueda binaria si solo se busca 5 veces al día?**

No mucho: con tan pocas búsquedas, hasta la lineal es rapidísima. El costo de mantener el arreglo ordenado (reordenar cada vez que llega un dato nuevo) no se compensa con tan poco uso.

**Pregunta 2 — ¿por qué la corrección va antes que la eficiencia?**

Porque un algoritmo rápido pero incorrecto es peor que uno lento: da respuestas equivocadas con confianza. Se ve en el experimento 4, donde la binaria "fallaba" no por lenta, sino por aplicarse sobre datos desordenados.

**Pregunta 3 — organizar los datos pensando solo en timestamp o solo en PM2.5**

Si ordeno por un campo, las consultas por el otro quedan condenadas a lineal (o a fallar, como en el experimento 4). Toca elegir cuál se consulta más seguido, o aceptar el costo de mantener el orden en ambos.

**Pregunta 4 — riesgos de seguir usando binaria sin verificar el orden**

Puede decir "no existe" un dato que sí está, solo porque el arreglo dejó de estar ordenado. No avisa que algo salió mal, simplemente responde mal.

**Pregunta 5 — "que funcione no significa que sea buena solución"**

En la semana 1 bastaba con que corriera; en la 2 pensé en cómo se organizaban los datos; ahora en la 3 toca pensar también en cuánto cuesta encontrarlos y bajo qué condiciones funciona.

## Uso de IA

Usé IA (Claude) como apoyo en dos cosas puntuales: para darle formato y buena redacción a este archivo `.md` (que se viera ordenado en el preview, con tablas y encabezados), y para ayudarme a diseñar los casos de prueba de la búsqueda binaria (qué escenarios probar: primer/último/intermedio elemento, dato inexistente, arreglo chico y grande) y a interpretar los resultados que arrojó mi propio código al correrlo