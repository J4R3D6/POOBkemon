package persistencia;

import domain.Log;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Repositorio encargado de leer y consultar información sobre estados alterados
 * desde un archivo CSV ubicado en "resources/csv/States.csv".
 *
 * Cada línea del archivo representa un estado y sus atributos, separados por comas.
 * El nombre del estado debe estar en la primera columna.
 */
public class StatusRepository implements Serializable {

    // Ruta al archivo CSV que contiene los estados
    private static final String STATUS_CSV = "resources/csv/States.csv";

    // Estructura de datos que almacena los estados leídos del archivo
    // La clave es el número de línea (comenzando desde 1), y el valor es un arreglo con los campos del estado
    private static TreeMap<Integer, String[]> estados = new TreeMap<>();

    /**
     * Constructor que lee el archivo CSV e inicializa el repositorio de estados.
     * Cada línea se divide por comas (respetando comillas) y se almacena en un mapa.
     */
    public StatusRepository() {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(STATUS_CSV));
        } catch (IOException e) {
            Log.record(e); // Registra el error en el sistema de logs
            return;
        }

        // Se omite la primera línea (encabezado)
        for (int i = 1; i < lines.size(); i++) {
            String[] valores = splitCSVLine(lines.get(i));
            estados.put(i, valores); // Se usa el número de línea como clave
        }
    }

    /**
     * Busca un estado por su nombre (ignorando mayúsculas y minúsculas).
     *
     * @param nombre Nombre exacto del estado (por ejemplo: "Paralisis")
     * @return Un arreglo de Strings con los datos del estado, o un arreglo vacío si no se encuentra
     */
    public String[] getStatusByName(String nombre) {
        for (String[] estado : estados.values()) {
            if (estado[0].equalsIgnoreCase(nombre)) {
                return estado;
            }
        }
        return new String[] {};
    }

    /**
     * Divide una línea de texto del CSV en sus campos individuales,
     * teniendo en cuenta comas dentro de comillas.
     *
     * @param line Línea de texto del archivo CSV
     * @return Arreglo de Strings con los valores separados
     */
    private static String[] splitCSVLine(String line) {
        List<String> values = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '\"') {
                inQuotes = !inQuotes; // Cambia el estado si encuentra comillas
            } else if (c == ',' && !inQuotes) {
                // Separa el valor si encuentra coma fuera de comillas
                values.add(current.toString().trim().replaceAll("^\"|\"$", ""));
                current.setLength(0);
            } else {
                current.append(c); // Agrega el carácter actual al valor en construcción
            }
        }

        // Agrega el último valor después del bucle
        values.add(current.toString().trim().replaceAll("^\"|\"$", ""));

        return values.toArray(new String[0]);
    }
}
