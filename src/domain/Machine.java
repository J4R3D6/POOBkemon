package domain;

import java.util.ArrayList;

/**
 * Clase abstracta que representa un entrenador controlado por la máquina.
 * Extiende la clase Trainer y obliga a implementar la lógica de decisión automática.
 */
public abstract class Machine extends Trainer {

    /**
     * Constructor de la clase Machine.
     * @param id Identificador del entrenador.
     * @param bagPack Mochila con ítems del entrenador.
     * @param name Nombre del entrenador.
     * @param pokemons Lista de Pokémon del entrenador.
     * @throws POOBkemonException si ocurre un error en la creación del entrenador.
     */
    public Machine(int id, BagPack bagPack, String name, ArrayList<Pokemon> pokemons) throws POOBkemonException {
        super(id, bagPack, name, pokemons);
    }

    /**
     * Método abstracto que define la decisión automática que debe tomar la máquina.
     * @param game Instancia actual del juego POOBkemon.
     * @return Un arreglo de Strings que representa la acción a ejecutar.
     * @throws POOBkemonException si ocurre un error durante la toma de decisión.
     */
    public abstract String[] machineDecision(POOBkemon game) throws POOBkemonException;
}
