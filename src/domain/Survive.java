package domain;

import persistencia.PokemonRepository;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Variante del juego POOBkemon que fija el nivel de todos los Pokémon a 100.
 * Sigue el patrón Singleton para asegurar una única instancia.
 */
public final class Survive extends POOBkemon {
    private static Survive instance;
    private int pokemonLvlFixed = 100;

    /**
     * Constructor privado para evitar instanciación externa.
     * Utiliza el patrón Singleton.
     */
    private Survive() {
        super();
    }

    /**
     * Obtiene la instancia única de la clase Survive.
     * @return Instancia única de Survive.
     */
    public static Survive getInstance() {
        if (instance == null) {
            instance = new Survive();
        }
        return instance;
    }

    /**
     * Crea un Pokémon con nivel fijo (100) sin importar el valor aleatorio o manual.
     * @param id ID del Pokémon a crear.
     * @param attackIds IDs de los ataques asignados.
     * @return Instancia del Pokémon creado con nivel 100.
     * @throws POOBkemonException Si hay errores al obtener la información del Pokémon.
     */
    @Override
    public Pokemon createPokemon(int id, ArrayList<Integer> attackIds) throws POOBkemonException {
        PokemonRepository info = new PokemonRepository();
        String[] infoPokemon = info.getPokemonById(id);
        Pokemon pokemon = new Pokemon(pokemonId, infoPokemon, attackIds, this.random, this.pokemonLvlFixed);
        this.pokemonId++;
        return pokemon;
    }

    /**
     * Inicializa el juego con los entrenadores, Pokémon, ítems y ataques especificados.
     * Todos los Pokémon serán creados con nivel fijo de 100.
     *
     * @param trainers Lista de nombres de entrenadores.
     * @param pokemons Mapa con los Pokémon asignados a cada entrenador.
     * @param items Mapa con los ítems asignados a cada entrenador.
     * @param attacks Mapa con los ataques de cada Pokémon.
     * @param random Bandera que indica si se deben generar datos aleatorios.
     * @param idTrainers IDs asignados a los entrenadores.
     * @param namesTrainers Nombres visibles de los entrenadores.
     * @throws POOBkemonException Si la inicialización falla por datos incorrectos o incompletos.
     */
    @Override
    public void initGame(ArrayList<String> trainers,
                         HashMap<String, ArrayList<Integer>> pokemons,
                         HashMap<String, String[][]> items,
                         HashMap<String, ArrayList<Integer>> attacks,
                         boolean random, int[] idTrainers, String[] namesTrainers) throws POOBkemonException {
        this.pokemonLvlFixed = 100;
        super.initGame(trainers, pokemons, items, attacks, random, idTrainers, namesTrainers);
    }

    /**
     * Retorna una matriz vacía, ya que esta variante ignora ítems por diseño.
     * @param trainerId ID del entrenador.
     * @return Matriz vacía.
     */
    @Override
    public String[][] getInfoItemsPerTrainer(int trainerId) {
        return new String[0][0];
    }
}
