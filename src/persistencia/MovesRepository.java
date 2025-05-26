package persistencia;

import domain.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Clase encargada de cargar y gestionar la información de ataques desde un archivo CSV.
 */
public class MovesRepository {
    private static final String ATACKS_ARCHIVE = "resources/csv/Moves.csv"; // Ruta al archivo de ataques
    private static TreeMap<Integer, String[]> moves = new TreeMap<>(); // Mapa que asocia ID con datos del ataque

    /**
     * Constructor que lee el archivo de ataques y carga los datos en un TreeMap.
     * Cada línea del archivo representa un ataque con múltiples atributos separados por comas.
     */
    public MovesRepository() {
        List<String> pokemonsIput = null;
        try {
            pokemonsIput = Files.readAllLines(Paths.get(ATACKS_ARCHIVE));
        } catch (IOException e) {
            Log.record(e); // Registra la excepción en el log
        }

        for (int i = 1; i < pokemonsIput.size(); i++) { // Se omite la cabecera (índice 0)
            String[] valores = pokemonsIput.get(i).split(",");
            this.moves.put(Integer.parseInt(valores[0]), valores);
        }
    }

    /**
     * Obtiene el arreglo de Strings que representa un ataque específico por ID.
     * @param id ID del ataque
     * @return Arreglo con los datos del ataque, o vacío si no se encuentra
     */
    public String[] getAttacksId(int id) {
        if (moves.containsKey(id)) {
            return moves.get(id);
        } else {
            return new String[] {};
        }
    }

    /**
     * Retorna una representación en texto del ataque para propósitos visuales o selección.
     * Incluye nombre, tipo, categoría, poder y PP.
     * @param id ID del ataque
     * @return Texto con información formateada del ataque o null si no existe
     */
    public String getAttackForChoose(int id) {
        if (moves.containsKey(id)) {
            String[] attack = getAttacksId(id);
            return attack[0] + " | " + attack[1] + "\n" +
                   "T." + attack[3] + " | C." + attack[4] + "\n" +
                   "PD " + attack[5] + " | PP " + attack[7];
        } else {
            return null;
        }
    }

    /**
     * Devuelve el tipo de un ataque en específico.
     * @param id ID del ataque
     * @return Tipo del ataque o null si no se encuentra
     */
    public String getAttackType(int id) {
        if (moves.containsKey(id)) {
            String[] attack = getAttacksId(id);
            return attack[3];
        } else {
            return null;
        }
    }
}
