/**
 * Contiene los experimentos de la Semana 3.
 *
 * Esta clase NO tiene main().
 * Los experimentos son llamados desde IngestaSensores.
 */
public class BancoDePruebas {

    private static final int[] TAMANOS = {
            1_000,
            100_000,
            1_000_000
    };

    /**
     * Experimento 1:
     * búsqueda lineal en el peor caso.
     */
    public static void experimentoUno() {

        System.out.println(
                "=== EXPERIMENTO 1: BUSQUEDA LINEAL ==="
        );

        System.out.printf(
                "%12s %16s %14s%n",
                "lecturas",
                "comparaciones",
                "tiempo (ms)"
        );

        for (int n : TAMANOS) {

            LecturaSensor[] datos =
                    GeneradorDatos.generar(n);

            String objetivo =
                    GeneradorDatos.timestampEnPosicion(n - 1);

            long inicio = System.nanoTime();

            int posicion =
                    BuscadorLecturas
                            .busquedaLinealPorTimestamp(
                                    datos,
                                    objetivo
                            );

            long fin = System.nanoTime();

            System.out.printf(
                    "%12d %16d %14.3f%n",
                    n,
                    BuscadorLecturas.getComparaciones(),
                    (fin - inicio) / 1_000_000.0
            );

            if (posicion < 0) {
                System.out.println(
                        "ADVERTENCIA: no encontro una lectura existente."
                );
            }
        }

        System.out.println();
    }

    /**
     * Experimento 2:
     * compara búsqueda lineal y búsqueda binaria.
     */
    public static void experimentoDos() {

        System.out.println(
                "=== EXPERIMENTO 2: LINEAL vs BINARIA ==="
        );

        System.out.printf(
                "%12s %14s %14s %12s%n",
                "lecturas",
                "lineal",
                "binaria",
                "relacion"
        );

        for (int n : TAMANOS) {

            LecturaSensor[] datos =
                    GeneradorDatos.generar(n);

            String objetivo =
                    GeneradorDatos.timestampEnPosicion(n - 1);

            BuscadorLecturas.busquedaLinealPorTimestamp(
                    datos,
                    objetivo
            );

            int lineal =
                    BuscadorLecturas.getComparaciones();

            BuscadorLecturas.busquedaBinariaPorTimestamp(
                    datos,
                    objetivo
            );

            int binaria =
                    BuscadorLecturas.getComparaciones();

            System.out.printf(
                    "%12d %14d %14d %12.1f%n",
                    n,
                    lineal,
                    binaria,
                    (double) lineal / binaria
            );
        }

        System.out.println();
    }

    /**
     * Compara búsqueda de un timestamp inexistente.
     */
    public static void experimentoTres() {

        System.out.println(
                "=== EXPERIMENTO 3: DATO INEXISTENTE ==="
        );

        LecturaSensor[] datos =
                GeneradorDatos.generar(100_000);

        String objetivo =
                GeneradorDatos.timestampInexistente();

        BuscadorLecturas.busquedaLinealPorTimestamp(
                datos,
                objetivo
        );

        int lineal =
                BuscadorLecturas.getComparaciones();

        BuscadorLecturas.busquedaBinariaPorTimestamp(
                datos,
                objetivo
        );

        int binaria =
                BuscadorLecturas.getComparaciones();

        System.out.println(
                "Lineal  -> comparaciones: " + lineal
        );

        System.out.println(
                "Binaria -> comparaciones: " + binaria
        );

        System.out.println();
    }

    /**
     * Demuestra qué ocurre cuando la búsqueda binaria
     * se aplica sobre un campo que no está ordenado.
     */
    public static void experimentoCuatro() {

        System.out.println(
                "=== EXPERIMENTO 4: BINARIA POR PM2.5 ==="
        );

        LecturaSensor[] datos =
                GeneradorDatos.generar(10_000);

        int aciertosLineal = 0;
        int aciertosBinaria = 0;

        for (int i = 0; i < 20; i++) {

            double valor =
                    datos[i * 137].getPm25();

            int posLineal = -1;

            for (int j = 0; j < datos.length; j++) {

                if (datos[j].getPm25() == valor) {
                    posLineal = j;
                    break;
                }
            }

            int posBinaria =
                    BuscadorLecturas
                            .busquedaBinariaPorPm25(
                                    datos,
                                    valor
                            );

            if (posLineal >= 0) {
                aciertosLineal++;
            }

            if (posBinaria >= 0) {
                aciertosBinaria++;
            }
        }

        System.out.println(
                "Valores buscados que SI existen: 20"
        );

        System.out.println(
                "Encontrados por búsqueda lineal:  "
                        + aciertosLineal
        );

        System.out.println(
                "Encontrados por búsqueda binaria: "
                        + aciertosBinaria
        );

        System.out.println();
    }

    public static void casosDePrueba() {

        System.out.println("=== CASOS DE PRUEBA MINIMOS ===");

        LecturaSensor[] datos = GeneradorDatos.generar(1_000_000);

        // Caso 1: primer elemento
        String objetivo1 = GeneradorDatos.timestampEnPosicion(0);
        int pos1 = BuscadorLecturas.busquedaBinariaPorTimestamp(datos, objetivo1);
        System.out.println("Primer elemento -> objetivo: " + objetivo1 + " | posicion encontrada: " + pos1);

        // Caso 2: elemento intermedio
        String objetivo2 = GeneradorDatos.timestampEnPosicion(500_000);
        int pos2 = BuscadorLecturas.busquedaBinariaPorTimestamp(datos, objetivo2);
        System.out.println("Intermedio -> objetivo: " + objetivo2 + " | posicion encontrada: " + pos2);

        // Caso 3: último elemento
        String objetivo3 = GeneradorDatos.timestampEnPosicion(999_999);
        int pos3 = BuscadorLecturas.busquedaBinariaPorTimestamp(datos, objetivo3);
        System.out.println("Ultimo elemento -> objetivo: " + objetivo3 + " | posicion encontrada: " + pos3);

        // Caso 4: elemento que sí existe (uno cualquiera al azar, ej. posicion 250000)
        String objetivo4 = GeneradorDatos.timestampEnPosicion(250_000);
        int pos4 = BuscadorLecturas.busquedaBinariaPorTimestamp(datos, objetivo4);
        System.out.println("Existente (250000) -> objetivo: " + objetivo4 + " | posicion encontrada: " + pos4);

        // Caso 5: elemento que NO existe
        String objetivo5 = GeneradorDatos.timestampInexistente();
        int pos5 = BuscadorLecturas.busquedaBinariaPorTimestamp(datos, objetivo5);
        System.out.println("Inexistente -> objetivo: " + objetivo5 + " | posicion encontrada: " + pos5);

        // Caso 6: arreglo pequeño (10 datos)
        LecturaSensor[] datosChico = GeneradorDatos.generar(10);
        String objetivo6 = GeneradorDatos.timestampEnPosicion(5);
        int pos6 = BuscadorLecturas.busquedaBinariaPorTimestamp(datosChico, objetivo6);
        System.out.println("Arreglo pequeño (n=10) -> objetivo: " + objetivo6 + " | posicion encontrada: " + pos6);

        // Caso 7: arreglo grande (1.000.000 datos) — ya usado arriba, confirmamos con otro punto
        String objetivo7 = GeneradorDatos.timestampEnPosicion(999_998);
        int pos7 = BuscadorLecturas.busquedaBinariaPorTimestamp(datos, objetivo7);
        System.out.println("Arreglo grande (n=1000000) -> objetivo: " + objetivo7 + " | posicion encontrada: " + pos7);
    }

    public static void tablaDeTiempos() {

        System.out.println("=== TABLA DE MEDICIONES: LINEAL vs BINARIA ===");
        System.out.printf("%12s %14s %14s%n", "lecturas", "tiempo lineal", "tiempo binaria");

        for (int n : TAMANOS) {

            LecturaSensor[] datos = GeneradorDatos.generar(n);
            String objetivo = GeneradorDatos.timestampEnPosicion(n - 1);

            long inicioL = System.nanoTime();
            BuscadorLecturas.busquedaLinealPorTimestamp(datos, objetivo);
            long finL = System.nanoTime();

            long inicioB = System.nanoTime();
            BuscadorLecturas.busquedaBinariaPorTimestamp(datos, objetivo);
            long finB = System.nanoTime();

            System.out.printf(
                    "%12d %14.3f %14.3f%n",
                    n,
                    (finL - inicioL) / 1_000_000.0,
                    (finB - inicioB) / 1_000_000.0
            );
        }

        System.out.println();
    }

    public static void trazaBinaria() {

        System.out.println("=== TRAZA BUSQUEDA BINARIA (n=20) ===");

        LecturaSensor[] datos = GeneradorDatos.generar(20);
        String objetivo = GeneradorDatos.timestampEnPosicion(13);

        System.out.println("Buscando objetivo: " + objetivo);

        int pos = BuscadorLecturas.busquedaBinariaPorTimestamp(datos, objetivo);

        System.out.println("Posicion encontrada: " + pos);
        System.out.println();
    }




}
