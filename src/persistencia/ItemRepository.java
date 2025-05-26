package persistencia;

import domain.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de leer y almacenar la información de los ítems desde un archivo CSV.
 */
public class ItemRepository {

    private static final String ITEMS_ARCHIVE = "resources/csv/Items.csv"; // Ruta del archivo CSV
    private static ArrayList<ArrayList<String>> items = new ArrayList<>(); // Lista de ítems cargados

    /**
     * Constructor que carga los ítems desde el archivo CSV especificado.
     * Cada línea del archivo se divide por comas y se almacena como una lista de Strings.
     */
    public ItemRepository() {
        ArrayList<ArrayList<String>> items = new ArrayList<>();
        try {
            List<String> lineas = Files.readAllLines(Paths.get(ITEMS_ARCHIVE));

            for (String s : lineas) {
                String[] valores = s.split(",");
                ArrayList<String> informacion = new ArrayList<>();
                informacion.add(valores[0]);
                informacion.add(valores[1]);
                items.add(informacion);
            }
        } catch (IOException e) {
            Log.record(e); // Registra el error en el log
        }
        this.items = items; // Asigna la lista cargada a la variable de instancia
    }

    /**
     * Devuelve la lista de ítems leída desde el archivo.
     * @return Lista de listas que contiene la información de cada ítem
     */
    public ArrayList<ArrayList<String>> getItems() {
        return this.items;
    }
}
