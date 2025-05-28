package domain;

import java.util.*;

/**
 * Entrenador tipo máquina que toma decisiones ofensivas durante la batalla.
 */
public class Offensive extends Machine {

    /**
     * Constructor del entrenador ofensivo.
     * @param id Identificador del entrenador.
     * @param bagPack Mochila con ítems del entrenador.
     * @param name Nombre del entrenador.
     * @param pokemons Lista de Pokémon del entrenador.
     * @throws POOBkemonException si ocurre un error en la creación del entrenador.
     */
    public Offensive(int id, BagPack bagPack, String name, ArrayList<Pokemon> pokemons) throws POOBkemonException {
        super(id, bagPack, name, pokemons);
    }

    /**
     * Toma una decisión automática para atacar con el primer ataque disponible.
     * @param game Instancia actual del juego POOBkemon.
     * @return Un arreglo de Strings que representa la acción "Attack".
     * @throws POOBkemonException si no hay Pokémon activo o ataques disponibles.
     */
    @Override
    public String[] machineDecision(POOBkemon game) throws POOBkemonException {
        Pokemon myActivePokemon = this.getActivePokemon();

        if (myActivePokemon == null) {
            throw new POOBkemonException(POOBkemonException.POKEMON_INACTIVE);
        }

        Attack selectedAttack = null;
        for (Attack a : myActivePokemon.getAttacks()) {
            if (a.getCurrentPP() > 0) {
                selectedAttack = a;
                break;
            }
        }

        if (selectedAttack == null) {
            throw new POOBkemonException(POOBkemonException.ATTACK_NOT_FOUND);
        }

        return new String[] {
            "Attack",
            String.valueOf(selectedAttack.getIdInGame()),
            String.valueOf(myActivePokemon.getId()),
            String.valueOf(this.getId())
        };
    }
}
