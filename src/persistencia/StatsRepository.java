package persistencia;

import domain.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase encargada de cargar y gestionar la tabla de efectividades entre tipos de Pokémon
 * a partir de un archivo CSV. Utilizada para calcular multiplicadores de daño según tipo.
 */
public class StatsRepository {

    private static final String ROOT_STATS_LOCATION = "resources/csv/Stats.csv"; // Ruta del archivo de estadísticas
    private final Map<String, Map<String, Double>> typeChart = new HashMap<>(); // Tabla de efectividad de tipos

    /**
     * Constructor que carga la tabla de tipos desde el archivo CSV.
     */
    public StatsRepository() {
        try {
            loadTypeChart();
        } catch (IOException e) {
            e.printStackTrace();
            Log.record(e); // Registra el error en el log
        }
    }

    /**
     * Carga los multiplicadores de daño entre tipos desde el archivo CSV.
     * La primera línea define los tipos ofensivos, y cada fila representa un tipo defensivo.
     * @throws IOException si hay errores al leer el archivo
     */
    private void loadTypeChart() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(ROOT_STATS_LOCATION));
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío: " + ROOT_STATS_LOCATION);
        }

        String[] types = lines.get(0).split(",");

        for (int i = 1; i < lines.size(); i++) {
            String[] values = lines.get(i).split(",");
            String defendingType = values[0];
            Map<String, Double> multipliers = new HashMap<>();

            for (int j = 1; j < values.length; j++) {
                multipliers.put(types[j], Double.parseDouble(values[j]));
            }

            typeChart.put(defendingType, multipliers);
        }
    }

    /**
     * Obtiene el multiplicador de daño para un ataque entre dos tipos.
     * @param attackingType Tipo del ataque
     * @param defendingType Tipo del Pokémon defensor
     * @return Multiplicador de efectividad (por ejemplo, 2.0, 1.0, 0.5)
     */
    public double getMultiplier(String attackingType, String defendingType) {
        return typeChart.get(capitalizar(defendingType)).get(capitalizar(attackingType));
    }

    /**
     * Convierte una cadena a formato capitalizado (primera letra en mayúscula, el resto en minúscula).
     * @param s Cadena a capitalizar
     * @return Cadena capitalizada
     */
    public static String capitalizar(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
