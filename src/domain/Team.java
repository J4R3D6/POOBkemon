package domain;

import java.io.Serializable;

/**
 * Representa un equipo de batalla compuesto por un entrenador.
 * Esta clase actúa como contenedor y puente hacia la lógica del entrenador.
 */
public class Team implements Serializable {

	private Trainer trainer;

	/**
	 * Crea un nuevo equipo con un entrenador asociado.
	 * @param trainer Entrenador que pertenece a este equipo.
	 */
	public Team(Trainer trainer) {
		this.trainer = trainer;
	}

	/**
	 * Devuelve el entrenador asociado al equipo.
	 * @return Entrenador del equipo.
	 */
	public Trainer getTrainer() {
		return trainer;
	}

	/**
	 * Cambia el Pokémon activo del entrenador.
	 * @param id ID del nuevo Pokémon a activar.
	 * @return Pokémon activado.
	 * @throws POOBkemonException si el cambio no es válido.
	 */
	public Pokemon changePokemon(int id) throws POOBkemonException {
		return trainer.changePokemon(id);
	}

	/**
	 * Verifica si todos los Pokémon del entrenador están debilitados.
	 * @return true si todos los Pokémon están debilitados, false de lo contrario.
	 */
	public boolean allFainted() {
		return trainer.allFainted();
	}

	/**
	 * Usa un ítem en un Pokémon específico del equipo.
	 * @param idPokemon ID del Pokémon objetivo.
	 * @param item Nombre del ítem a usar.
	 * @throws POOBkemonException si el Pokémon no existe o el ítem es inválido.
	 */
	public void useItem(int idPokemon, String item) throws POOBkemonException {
		Pokemon p = trainer.getPokemonById(idPokemon);
		if (p == null) throw new POOBkemonException(POOBkemonException.POKEMON_ID_NOT_FOUND + idPokemon);
		trainer.useItem(p, item);
	}

	/**
	 * Aplica el comportamiento por tiempo agotado al Pokémon especificado.
	 * @param pokemonID ID del Pokémon afectado.
	 */
	public void timeOver(int pokemonID) {
		trainer.timeOver(pokemonID);
	}

	/**
	 * Aplica los efectos de estado activos en el equipo (si los hay).
	 */
	public void applyEffect() {
		trainer.applyEffect();
	}

	/**
	 * Obtiene los IDs de los Pokémon inactivos del equipo.
	 * @return Arreglo de IDs de Pokémon no activos.
	 */
	public int[] getInactivePokemonIds() {
		return trainer.getInactivePokemonIds();
	}

	/**
	 * Devuelve el ID del primer Pokémon no debilitado del equipo.
	 * @return ID del primer Pokémon disponible, o -1 si todos están debilitados.
	 */
	public int getFirstAlivePokemonId() {
		return trainer.getFirstAlivePokemonId();
	}

	/**
	 * Retorna todos los IDs de los Pokémon del equipo.
	 * @return Arreglo de IDs de Pokémon.
	 */
	public int[] getAllPokemonIds() {
		return trainer.getAllPokemonIds();
	}

	/**
	 * Devuelve la información del Pokémon actualmente activo en batalla.
	 * @return Arreglo de Strings con la información del Pokémon activo.
	 */
	public String[] getActivePokemonInfo() {
		return trainer.getActivePokemonInfo();
	}

	/**
	 * Retorna la información de los ítems disponibles en la mochila del entrenador.
	 * @return Matriz con los ítems del entrenador.
	 * @throws POOBkemonException si el entrenador no existe o hay error de acceso.
	 */
	public String[][] getTrainerItemsInfo() throws POOBkemonException {
		if (trainer == null) throw new POOBkemonException(POOBkemonException.TRAINER_NOT_FOUND);
		return trainer.getBagPack().getItems();
	}

	/**
	 * Devuelve la información de los ataques del Pokémon activo.
	 * @return Matriz con los ataques disponibles del Pokémon activo.
	 */
	public String[][] getActivePokemonAttacks() {
		return trainer.getActivePokemonAttacks();
	}

	/**
	 * Busca un Pokémon por su ID dentro del equipo.
	 * @param idPokemon ID del Pokémon buscado.
	 * @return Pokémon correspondiente o null si no se encuentra.
	 */
	public Pokemon getPokemonById(int idPokemon) {
		return trainer.getPokemonById(idPokemon);
	}

	/**
	 * Obtiene la decisión automática del entrenador si es una instancia de Machine.
	 * @param game Instancia actual del juego POOBkemon.
	 * @return Arreglo que representa la decisión del entrenador máquina.
	 * @throws POOBkemonException si el entrenador no es una máquina.
	 */
	public String[] getMachineDecision(POOBkemon game) throws POOBkemonException {
		if (!(trainer instanceof Machine))
			throw new POOBkemonException(POOBkemonException.TRAINER_IS_NOT_MACHINE);
		return ((Machine) trainer).machineDecision(game);
	}
}
