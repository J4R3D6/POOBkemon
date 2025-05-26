package domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase que representa un entrenador en el juego.
 * Contiene información sobre su equipo de Pokémon, mochila y lógica para cambiar y usar Pokémon o ítems.
 */
public class Trainer implements Serializable {

	private int currentPokemonId = -1; // ID del Pokémon actualmente activo
	private BagPack bagPack; // Mochila del entrenador
	private int id; // Identificador del entrenador
	private String name; // Nombre del entrenador
	private Pokemon active; // Pokémon actualmente activo
	private ArrayList<Pokemon> pokemons; // Lista de Pokémon del entrenador

	/**
	 * Crea un nuevo entrenador con los atributos indicados.
	 * @param id Identificador del entrenador
	 * @param bagPack Mochila del entrenador
	 * @param name Nombre del entrenador
	 * @param pokemons Lista de Pokémon que posee
	 * @throws POOBkemonException Si la mochila es nula
	 */
	public Trainer(int id, BagPack bagPack, String name, ArrayList<Pokemon> pokemons) throws POOBkemonException {
		if (bagPack == null) throw new POOBkemonException(POOBkemonException.NULL_BAGPACK);
		this.id = id;
		this.bagPack = bagPack;
		this.name = name;
		this.pokemons = pokemons;
		this.active = this.pokemons.get(0);
		this.active.setActive(true);
		this.currentPokemonId = this.pokemons.get(0).getId();
	}

	public int getId() {
		return id;
	}

	public BagPack getBagPack() {
		return bagPack;
	}

	public int getCurrentPokemonId() {
		return currentPokemonId;
	}

	public void setCurrentPokemonId(int currentPokemonId) {
		this.currentPokemonId = currentPokemonId;
	}

	public String getName() {
		return this.name;
	}

	public ArrayList<Pokemon> getPokemons() {
		return pokemons;
	}

	/**
	 * Retorna el Pokémon por su ID.
	 * @param id ID del Pokémon
	 * @return Pokémon correspondiente o null si no existe
	 */
	public Pokemon getPokemonById(int id) {
		for (Pokemon p : pokemons) {
			if (p.getId() == id) return p;
		}
		return null;
	}

	/**
	 * Retorna el Pokémon activo actualmente.
	 * @return Pokémon activo
	 */
	public Pokemon getActivePokemon() {
		for (Pokemon p : this.pokemons) {
			if (p.getActive()) {
				return p;
			}
		}
    	return null;
	}

	/**
	 * Cambia el Pokémon activo por otro.
	 * @param id ID del nuevo Pokémon activo
	 * @return Pokémon que ha sido activado
	 * @throws POOBkemonException si el ID no es válido o el Pokémon está débil
	 */
	public Pokemon changePokemon(int id) throws POOBkemonException {
		Pokemon pokemonToActivate = null;
		Pokemon currentActive = null;

		for (Pokemon p : pokemons) {
			if (p.getActive()) currentActive = p;
			if (p.getId() == id) pokemonToActivate = p;
		}

		if (pokemonToActivate == null) throw new POOBkemonException(POOBkemonException.POKEMON_ID_NOT_FOUND + id);
		if (pokemonToActivate.getWeak()) throw new POOBkemonException(POOBkemonException.POKEMON_WEAK_CHANGE);

		if (currentActive != null) currentActive.setActive(false);
		pokemonToActivate.setActive(true);
		this.setCurrentPokemonId(pokemonToActivate.getId());
		return pokemonToActivate;
	}

	/**
	 * Usa un ítem sobre un Pokémon.
	 * @param pokemon Pokémon objetivo
	 * @param item Nombre del ítem a usar
	 * @throws POOBkemonException si el ítem no existe
	 */
	public void useItem(Pokemon pokemon, String item) throws POOBkemonException {
		Item itemUse = this.bagPack.getItem(item);
		if (itemUse == null) throw new POOBkemonException(POOBkemonException.TEAM_NOT_FOUND);
		itemUse.effect(pokemon);
	}

	/**
	 * Aplica el estado a todos los Pokémon del entrenador.
	 */
	public void applyEffect() {
		for (Pokemon p : pokemons) {
			p.applyState();
		}
	}

	/**
	 * Aplica el efecto de "time over" al Pokémon indicado.
	 * @param pokemonID ID del Pokémon afectado
	 */
	public void timeOver(int pokemonID) {
		for (Pokemon p : pokemons) {
			if (p.getId() == pokemonID) {
				p.timeOver();
			}
		}
	}

	/**
	 * Retorna los IDs de los Pokémon que no están activos.
	 * @return Arreglo de IDs de Pokémon inactivos
	 */
	public int[] getInactivePokemonIds() {
		ArrayList<Integer> ids = new ArrayList<>();
		for (Pokemon p : pokemons) {
			if (!p.getActive()) ids.add(p.getId());
		}
		return ids.stream().mapToInt(i -> i).toArray();
	}

	/**
	 * Retorna los IDs de todos los Pokémon del entrenador.
	 * @return Arreglo de todos los IDs
	 */
	public int[] getAllPokemonIds() {
		return pokemons.stream().mapToInt(Pokemon::getId).toArray();
	}

	/**
	 * Verifica si todos los Pokémon del equipo están debilitados.
	 * @return true si todos están débiles, false si al menos uno está activo
	 */
	public boolean allFainted() {
		for (Pokemon p : pokemons) {
			if (!p.getWeak()) return false;
		}
		return true;
	}

	/**
	 * Retorna el ID del primer Pokémon vivo (no débil).
	 * @return ID del Pokémon o -1 si no hay ninguno
	 */
	public int getFirstAlivePokemonId() {
		for (Pokemon p : pokemons) {
			if (!p.getWeak()) return p.getId();
		}
		return -1;
	}

	/**
	 * Retorna la información del Pokémon activo.
	 * @return Arreglo de Strings con los datos del Pokémon activo
	 */
	public String[] getActivePokemonInfo() {
		Pokemon activePokemon = getPokemonById(currentPokemonId);
		return activePokemon.getInfo();
	}

	/**
	 * Retorna la información de los ataques del Pokémon activo.
	 * @return Matriz de Strings con los datos de los ataques
	 */
	public String[][] getActivePokemonAttacks() {
		Pokemon activePokemon = getPokemonById(currentPokemonId);
		return activePokemon.getAttacksInfo();
	}
}
