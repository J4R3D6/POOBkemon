package domain;

import java.util.ArrayList;

/**
 * Estrategia de máquina defensiva que prioriza ataques de tipo defensivo.
 */
public class Defensive extends Machine {

    /**
     * Crea una máquina defensiva con sus atributos iniciales.
     * @param id ID de la máquina
     * @param bagPack Mochila con ítems disponibles
     * @param name Nombre de la máquina
     * @param pokemons Lista de Pokémon que tiene la máquina
     * @throws POOBkemonException Si ocurre un error al inicializar
     */
    public Defensive(int id, BagPack bagPack, String name, ArrayList<Pokemon> pokemons) throws POOBkemonException {
        super(id, bagPack, name, pokemons);
    }

    /**
     * Toma la decisión de ataque basada en movimientos defensivos.
     * Si no hay movimientos defensivos, selecciona el primer ataque disponible.
     * @param game Instancia del juego
     * @return Arreglo con la decisión de acción a tomar
     * @throws POOBkemonException Si no hay Pokémon activo o no hay ataques disponibles
     */
    @Override
    public String[] machineDecision(POOBkemon game) throws POOBkemonException {
        Pokemon active = this.getActivePokemon();
        if (active == null) {
            throw new POOBkemonException(POOBkemonException.POKEMON_ACTIVE_NO_FOUND);
        }

        ArrayList<Attack> attacks = active.getAttacks();
        Attack bestDefensive = null;

        // Buscar movimiento con prioridad defensiva
        for (Attack atk : attacks) {
            if (atk instanceof StateAttack) {
                State state = ((StateAttack)atk).getState();
                String type = state.getType().name();

                // Verifica si es un movimiento defensivo
                if (type.contains("DEFENSE_UP") || type.contains("PROTECT") ||
                        type.contains("SUBSTITUTE") || type.contains("MAGIC_COAT") ||
                        type.contains("SP_DEFENSE_UP") || type.contains("ATTACK_DOWN") ||
                        type.contains("SP_ATTACK_DOWN")) {

                    if (atk.getCurrentPP() > 0) {
                        bestDefensive = atk;
                        break;
                    }
                }
            }
        }

        // Si no hay movimientos defensivos disponibles, usar el primero que tenga PP
        if (bestDefensive == null) {
            for (Attack atk : attacks) {
                if (atk.getCurrentPP() > 0) {
                    bestDefensive = atk;
                    break;
                }
            }
        }

        if (bestDefensive == null) {
            throw new POOBkemonException(POOBkemonException.MACHINE_ERROR);
        }

        return new String[]{
                "Attack",
                String.valueOf(bestDefensive.getIdInGame()),
                String.valueOf(active.getId()),
                String.valueOf(this.getId())
        };
    }
}
