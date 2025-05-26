package persistencia;

import domain.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Clase que gestiona el acceso a los datos de los Pokémon almacenados en un archivo CSV.
 * Permite obtener todos los Pokémon o buscar uno por su ID.
 */
public class PokemonRepository {

    private static final String POKEMONS_ARCHIVE = "resources/csv/Pokemons.csv"; // Ruta al archivo de Pokémon
    private static TreeMap<Integer, String[]> pokemones = new TreeMap<>(); // Mapa de ID a datos de Pokémon

    /**
     * Constructor que carga la información de los Pokémon desde el archivo CSV.
     */
    public PokemonRepository() {
        List<String> pokemonsIput = null;
        try {
            pokemonsIput = Files.readAllLines(Paths.get(POKEMONS_ARCHIVE));
        } catch (IOException e) {
            Log.record(e); // Registra errores en el log
        }

        for (int i = 1; i < pokemonsIput.size(); i++) { // Omitimos la cabecera
            String[] valores = pokemonsIput.get(i).split(",");
            this.pokemones.put(Integer.parseInt(valores[0]), valores);
        }
    }

    /**
     * Retorna una lista con todos los Pokémon cargados.
     * @return Lista de arreglos con la información de cada Pokémon
     */
    public ArrayList<String[]> getPokemons() {
        ArrayList<String[]> pokemones = new ArrayList<>();
        for (String[] s : this.pokemones.values()) {
            pokemones.add(s);
        }
        return pokemones;
    }

    /**
     * Retorna un Pokémon específico por su ID.
     * @param id Identificador del Pokémon
     * @return Arreglo con los datos del Pokémon o vacío si no existe
     */
    public String[] getPokemonById(int id) {
        if (pokemones.containsKey(id)) {
            return pokemones.get(id);
        } else {
            return new String[] {};
        }
    }
}
