package domain;

import persistencia.*;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * Clase principal que gestiona la lógica del juego Pokémon.
 * Maneja la creación de equipos, entrenadores, batallas y persistencia del juego.
 */

public class POOBkemon implements Serializable {

	protected ArrayList<String> movesInGame;
	private boolean finishBattle = false;
	protected ArrayList<Team> teams; //Lista de equipos en el juego
	protected int pokemonId = 0;
	protected int trainerIdPosition = 0;
	protected int trainerNamePosition = 0;
	protected int[] trainersId;
	protected boolean random;
	protected boolean ok;
	protected static POOBkemon game;
	private int pokemonLv = 1;
	private int winner = -1;
	private int counter = 1;
	public static int shinyProbability = 10;
	private int numPokemon = 386;
	private int numMovements = 356;
	protected static int criticalHitChance = 4;
	private String[] namesTrainers;

	protected POOBkemon() {
	}

	/**
	 * Obtiene una unica instancia del juego(singleton)
	 */
	public static POOBkemon getInstance() {
		if (game == null) {
			game = new POOBkemon();
		}
		return game;
	}

	/**
	 * Inicializa el juego con los datos proporcionados.
	 *
	 * @param trainers      Lista de nombres de entrenadores
	 * @param pokemons      Mapa de Pokémon por entrenador
	 * @param items         Mapa de ítems por entrenador
	 * @param attacks       Mapa de ataques por entrenador
	 * @param random        Indica si usar valores aleatorios
	 * @param idTrainers    IDs de los entrenadores
	 * @param namesTrainers Nombres de los entrenadores
	 * @throws POOBkemonException si hay error en los datos iniciales
	 */
	public void initGame(ArrayList<String> trainers, HashMap<String, ArrayList<Integer>> pokemons, HashMap<String, String[][]> items, HashMap<String, ArrayList<Integer>> attacks, boolean random, int[] idTrainers, String[] namesTrainers) throws POOBkemonException {
		resetGame();
		validateInitialData(trainers, pokemons, items);
		resetGameState(random, idTrainers, namesTrainers);
		buildTeams(trainers, pokemons, items, attacks);
		startBattleRecording();
	}

	/**
	 * Valida que la informacion inicial sea correcta.
	 *
	 * @param trainers Lista de nombres de entrenadores
	 * @param pokemons Mapa de Pokémon por entrenador
	 * @param items    Mapa de ítems por entrenador
	 * @throws POOBkemonException si hay error en los datos iniciales
	 */
	private void validateInitialData(ArrayList<String> trainers, HashMap<String, ArrayList<Integer>> pokemons, HashMap<String, String[][]> items) throws POOBkemonException {
		if (trainers == null || trainers.isEmpty())
			throw new POOBkemonException(POOBkemonException.MISSING_TRAINER_DATA);
		if (pokemons == null || pokemons.isEmpty())
			throw new POOBkemonException(POOBkemonException.MISSING_POKEMON_DATA);
		if (items == null || items.isEmpty()) throw new POOBkemonException(POOBkemonException.MISSING_ITEMS_DATA);
	}


	private void resetGameState(boolean random, int[] idTrainers, String[] namesTrainers) {
		this.teams = new ArrayList<>();
		this.finishBattle = false;
		this.random = random;
		this.trainersId = idTrainers;
		this.namesTrainers = namesTrainers;
		this.movesInGame = new ArrayList<>();
	}

	/**
	 * Restablece el juego
	 */
	public void resetGame() {
		this.game = null;
		this.trainerIdPosition = 0;
		this.finishBattle = false;
		this.random = false;
		this.teams = null;
		this.movesInGame = null;
		this.ok = true;
		this.finishBattle = false;
		this.winner = -1;
		this.trainerNamePosition = 0;
	}

	/**
	 * Crea los equipos para el juego.
	 *
	 * @param trainers Lista de nombres de entrenadores
	 * @param pokemons Mapa de Pokémon por entrenador
	 * @param items    Mapa de ítems por entrenador
	 * @param attacks  Mapa de ataques por entrenador
	 * @throws POOBkemonException si hay error en los datos iniciales
	 */
	private void buildTeams(ArrayList<String> trainers, HashMap<String, ArrayList<Integer>> pokemons, HashMap<String, String[][]> items, HashMap<String, ArrayList<Integer>> attacks) throws POOBkemonException {
		for (String trainerName : trainers) {
			if (!pokemons.containsKey(trainerName)) throw new POOBkemonException(POOBkemonException.INCOMPLETE_DATA);

			ArrayList<Item> trainerItems = createItems(items.get(trainerName));
			BagPack bagPack = new BagPack(trainerItems);
			ArrayList<Pokemon> trainerPokemons = createPokemons(pokemons.get(trainerName), attacks.get(trainerName));

			Trainer trainer = createTrainers(trainerName, bagPack, trainerPokemons);
			Team team = new Team(trainer);
			this.teams.add(team);
		}
	}

	/**
	 * Crea los items para el juego.
	 *
	 * @param itemsData informacion de los items.
	 * @return lista de items creados.
	 */
	private ArrayList<Item> createItems(String[][] itemsData) {
		ArrayList<Item> items = new ArrayList<>();
		for (String[] itemData : itemsData) {
			items.add(createItem(itemData));
		}
		return items;
	}

	/**
	 * Crea un item de acuerdo a su tipo.
	 *
	 * @param itemData informacion del item.
	 * @return El objeto tipo Item.
	 */
	public Item createItem(String[] itemData) {
		return switch (itemData[0]) {
			case "Potion" -> new Potion(Integer.parseInt(itemData[1]), Integer.parseInt(itemData[2]));
			case "Revive" -> new Revive(Integer.parseInt(itemData[1]));
			case "MaxRevive" -> new MaxRevive(Integer.parseInt(itemData[1]));
			default -> throw new IllegalArgumentException(POOBkemonException.ITEM_ERROR + itemData[0]);
		};
	}

	/**
	 * Crea los pokemones para el juego.
	 *
	 * @param pokemonIds los ids de los pokemones
	 * @param attackIds  los ids de los ataques que tendra el Pokemon.
	 * @return lista de Pokemones creados.
	 */
	private ArrayList<Pokemon> createPokemons(ArrayList<Integer> pokemonIds, ArrayList<Integer> attackIds) throws POOBkemonException {
		ArrayList<Pokemon> pokemons = new ArrayList<>();
		int attackIndex = 0;
		for (Integer pokemonId : pokemonIds) {
			List<Integer> attacksForPokemon = attackIds.subList(attackIndex, attackIndex + 4);
			pokemons.add(createPokemon(pokemonId, new ArrayList<>(attacksForPokemon)));
			attackIndex += 4;
		}
		return pokemons;
	}

	/**
	 * Crea un Pokémon con los parámetros dados.
	 *
	 * @param id        ID del Pokémon
	 * @param attackIds Lista de IDs de ataques
	 * @return Pokémon creado
	 * @throws POOBkemonException si no se encuentra el Pokémon
	 */
	public Pokemon createPokemon(int id, ArrayList<Integer> attackIds) throws POOBkemonException {
		String[] info = new PokemonRepository().getPokemonById(id);
		Pokemon pokemon = null;
		if (!(info.length == 0)) {
			pokemon = new Pokemon(pokemonId, info, attackIds, this.random, this.pokemonLv, this.criticalHitChance);
		} else {
			throw new POOBkemonException(POOBkemonException.POKEMON_ID_NOT_FOUND);
		}
		this.pokemonId++;
		return pokemon;
	}

	/**
	 * Crea los entrenadores para el juego de acuerdo a su tipo.
	 *
	 * @param trainerName el nombre del entrenador.
	 * @param bagPack     la mochila del entrenador.
	 * @param pokemons    los Pokemones del entrenador
	 * @return El objeto tipo Entrenador.
	 * @throws POOBkemonException al crear el entrenador.
	 */
	private Trainer createTrainers(String trainerName, BagPack bagPack, ArrayList<Pokemon> pokemons) throws POOBkemonException {
		String trainerType = trainerName.replaceAll("\\d+$", "");
		int id = trainersId[trainerIdPosition++];
		String name = namesTrainers[trainerNamePosition++];

		return switch (trainerType) {
			case "Player" -> new Trainer(id, bagPack, name, pokemons);
			case "Offensive" -> new Offensive(id, bagPack, name, pokemons);
			case "Defensive" -> new Defensive(id, bagPack, name, pokemons);
			//case "Expert" -> new Expert(id, bagPack, name, pokemons);
			default -> new Switcher(id, bagPack, name, pokemons);
		};
	}

	private void startBattleRecording() {
		this.movesInGame.add("Start Game");
		this.ok = true;
	}

	/**
	 * Procesa la decisión tomada por un entrenador.
	 *
	 * @param decisionTrainer Arreglo con la decisión del entrenador
	 * @throws POOBkemonException si la decisión es inválida
	 */
	public void takeDecision(String[] decisionTrainer) throws POOBkemonException {
		if (this.finishBattle) return;
		if (this.counter % 2 == 0) {
			this.applyStates();
		}
		this.counter++;
		this.checkBattleStatus();
		// Validar que haya dos entrenadores
		if (this.teams.size() < 2) {
			throw new POOBkemonException(POOBkemonException.INCOMPLETE_DATA);
		}
		String[] decision = decisionTrainer;

		if (decision == null || decision.length == 0) {
			throw new POOBkemonException(POOBkemonException.INVALID_FORMAT);
		}

		if (this.winner == -1) {

			String action = decision[0];
			try {
				switch (action) {
					case "Attack":
						if (decision.length < 3) throw new POOBkemonException(POOBkemonException.INCOMPLETE_DATA);
						int attackId = Integer.parseInt(decision[1]);
						int pokemonId1 = Integer.parseInt(decision[2]);
						int trainerId = Integer.parseInt(decision[3]);
						this.attack(attackId, trainerId, pokemonId1);
						checkBattleStatus();
						break;

					case "UseItem":
						if (decision.length < 2) throw new POOBkemonException(POOBkemonException.INCOMPLETE_DATA);
						int idTrainer = Integer.parseInt(decision[1]);
						int pokemonId = Integer.parseInt(decision[2]);
						String datoItem = decision[3];
						this.useItem(idTrainer, pokemonId, datoItem);
						break;

					case "ChangePokemon":
						if (decision.length < 3) throw new POOBkemonException(POOBkemonException.INCOMPLETE_DATA);
						int newPokemonId = Integer.parseInt(decision[2]);
						this.changePokemon(Integer.parseInt(decision[1]), newPokemonId);
						String pokemonName = "";

						pokemonName = this.searchPokemon(newPokemonId).getName();
						movesInGame.add("Player " + decision[1] + " cambió a Pokémon " + pokemonName);

						break;

					case "Run":
						this.run(Integer.valueOf(decision[1]));
						movesInGame.add("Player " + decision[1] + " huyó de la batalla");
						this.finishBattle = true;
						break;

					case "timeOver":
						if (decision.length < 2) throw new POOBkemonException(POOBkemonException.INCOMPLETE_DATA);
						int trainerid = Integer.parseInt(decision[1]);
						int pokemonid = Integer.parseInt(decision[2]);
						this.timeOver(trainerid, pokemonid);
						movesInGame.add("Player se le acabo el tiempo");
						break;
					default:
						throw new POOBkemonException(POOBkemonException.ACTION_NOT_FOUND + action);
				}
				checkBattleStatus();
			} catch (NumberFormatException e) {
				throw new POOBkemonException(POOBkemonException.INVALID_FORMAT);
			}
			// Verificar estado de la batalla después de ambos turnos
			checkBattleStatus();
		}
	}


	/**
	 * Verifica el estado de la batalla y determina si ha terminado.
	 */
	public void checkBattleStatus() {
		if (this.finishBattle) return;
		for (Team team : teams) {
			if (team.allFainted()) {
				this.finishBattle = true;
				movesInGame.add("¡Batalla terminada! " + team.getTrainer().getName() + " ha sido derrotado");
				this.searchWinner(team);
				break;
			}
		}
	}

	private void searchWinner(Team team) {
		if (this.winner != -1) return;
		for (Team t : teams) {
			if (t.getTrainer().getId() != team.getTrainer().getId()) {
				this.setWinner(t);
			}
		}
	}

	/**
	 * Metodo para obtener el ganador de la pelea
	 *
	 * @param team el equipo donde se encuentra el entrenador ganador.
	 */
	private void setWinner(Team team) {
		this.winner = team.getTrainer().getId();
	}

	/**
	 * Nos dice que entrenador corre de la partida
	 *
	 * @Param trainer Id del entrendador a buscar
	 */
	public void run(int trainer) throws POOBkemonException {
		if (this.finishBattle) return;
		Team team = this.getTeamByTrainerId(trainer);
		if (team != null) {
			this.movesInGame.add("GameOver para el jugador " + team.getTrainer().getId());
		} else {
			throw new POOBkemonException(POOBkemonException.TRAINER_NOT_FOUND + trainer);
		}
		this.setWinner(team);
		this.finishBattle = true;
	}

	/**
	 * Cambia el Pokémon activo del equipo del entrenador especificado.
	 *
	 * @param trainerId ID del entrenador que desea cambiar su Pokémon activo.
	 * @param pokemonId ID del nuevo Pokémon que se desea activar.
	 * @throws POOBkemonException si el entrenador no existe o el cambio no puede realizarse.
	 */
	public void changePokemon(int trainerId, int pokemonId) throws POOBkemonException {
		if (this.finishBattle) return;

		// Obtener el equipo del entrenador
		Team team = getTeamByTrainerId(trainerId);

		// Validar que el equipo del entrenador existe
		if (team == null) {
			throw new POOBkemonException(POOBkemonException.TRAINER_NOT_FOUND + trainerId);
		}

		// Solicitar el cambio de Pokémon dentro del equipo
		team.changePokemon(pokemonId);
	}

	/**
	 * Busca un Pokémon por su ID en todos los equipos.
	 *
	 * @param id ID del Pokémon a buscar
	 * @return Pokémon encontrado o null si no existe
	 */
	public Pokemon searchPokemon(int id) {
		for (Team team : teams) {
			Pokemon pokemon = team.getPokemonById(id);
			if (pokemon != null) {
				return pokemon;
			}
		}
		return null;
	}

	/**
	 * Decision auntomatica si se le acaba el tiempo al entrenador
	 *
	 * @param trainerId Id del entrador al que se le ejecuta la accion
	 * @param pokemonId Id el pokemon al que se le ejecuta la accion
	 */
	private void timeOver(int trainerId, int pokemonId) throws POOBkemonException {
		if (this.finishBattle) return;
		Team team = this.getTeamByTrainerId(trainerId);
		if (team == null) {
			throw new POOBkemonException(POOBkemonException.TRAINER_NOT_FOUND + trainerId);
		}
		team.timeOver(pokemonId);
	}

	/**
	 * Usa un ítem en un Pokémon específico.
	 *
	 * @param trainer   ID del entrenador
	 * @param idPokemon ID del Pokémon objetivo
	 * @param itemData  Datos del ítem a usar
	 * @throws POOBkemonException si no se encuentra el equipo o Pokémon
	 */
	public void useItem(int trainer, int idPokemon, String itemData) throws POOBkemonException {
		if (this.finishBattle) return;
		checkBattleStatus();
		Team team = this.getTeamByTrainerId(trainer);
		if (team == null) throw new POOBkemonException(POOBkemonException.TEAM_NOT_FOUND);
		team.useItem(idPokemon, itemData);
		String message = team.getTrainer().getName() + " ha usado " + itemData + " en " + team.getPokemonById(idPokemon).getName();
		this.movesInGame.add(message);
	}

	/**
	 * Realiza un ataque entre Pokémon.
	 *
	 * @param idAttack  ID del ataque a usar
	 * @param idTrainer ID del entrenador atacante
	 * @param idThrower ID del Pokémon atacante
	 * @throws POOBkemonException si el ataque falla
	 */
	public void attack(int idAttack, int idTrainer, int idThrower) throws POOBkemonException {
		applyStates();
		Team attackerTeam = getTeamByTrainerId(idTrainer);
		if (attackerTeam == null) throw new POOBkemonException(POOBkemonException.TEAM_NOT_FOUND);

		Pokemon attacker = attackerTeam.getTrainer().getPokemonById(attackerTeam.getTrainer().getCurrentPokemonId());
		if (!attacker.getActive()) {
			throw new POOBkemonException(POOBkemonException.POKEMON_INACTIVE);
		}
		if (!attacker.canReceiveDamage(attacker)) throw new POOBkemonException(POOBkemonException.POKEMON_INACTIVE);

		int currentPokemonId = getTeamByTrainerId(idTrainer).getTrainer().getCurrentPokemonId();
		if (currentPokemonId != idThrower) {
			return;
		}

		Attack attack = attacker.getAttackById(idAttack);
		if (attack == null) {
			throw new POOBkemonException(POOBkemonException.ATTACK_NOT_FOUND + idAttack);
		}

		Team opponentTeam = teams.stream().filter(t -> t.getTrainer().getId() != idTrainer).findFirst().orElse(null);
		if (opponentTeam == null) throw new POOBkemonException(POOBkemonException.TEAM_NOT_FOUND);

		Pokemon target = opponentTeam.getTrainer().getPokemonById(opponentTeam.getTrainer().getCurrentPokemonId());
		if (target == null || !target.getActive()) {
			for (Pokemon p : opponentTeam.getTrainer().getPokemons()) {
				if (p.getActive()) {
					target = p;
					break;
				}
			}
		}

		if (target == null) throw new POOBkemonException(POOBkemonException.POKEMON_ID_NOT_FOUND);

		String effect = target.getDamage(attack, attacker);
		target.isWeak();
		attacker.isWeak();

		if (!effect.isEmpty()) {
			movesInGame.add(attacker.getName() + " atacó a " + target.getName() + ": " + effect);
		}

		autoChangePokemon();
	}

	/**
	 * Obtiene información de los Pokémon activos de todos los entrenadores.
	 *
	 * @return Mapa con ID de entrenador como clave y información del Pokémon activo como valor
	 * @throws POOBkemonException
	 * @throws NullPointerException si no hay equipos cargados
	 */
	public HashMap<Integer, String[]> getCurrentPokemons() throws POOBkemonException {
		if (this.teams == null) throw new POOBkemonException(POOBkemonException.TEAMS_NULL);
		HashMap<Integer, String[]> activePokemons = new HashMap<>();
		for (Team team : this.teams) {
			activePokemons.put(team.getTrainer().getId(), team.getActivePokemonInfo());
		}
		return activePokemons;
	}

	/**
	 * Metodo para verificar si un entrenador es una maquina.
	 *
	 * @param trainerId el id del entrenador a verificar condición.
	 * @return si el entrenador es o no una maquina.
	 */
	public boolean isMachine(int trainerId) {
		Team team = this.getTeamByTrainerId(trainerId);
		if (team.getTrainer() instanceof Machine) {
			return true;
		}
		return false;
	}

	/**
	 * Metodo para obtener los id de Pokeones por entrenador especifico.
	 *
	 * @param idTrainer el id del entrenador a verificar condición.
	 * @return arreglo con los ids de sus Pokemones.
	 */
	public int[] getPokemonsPerTrainer(int idTrainer) {
		Team team = getTeamByTrainerId(idTrainer);
		if (team == null) return new int[0];

		return team.getAllPokemonIds();
	}

	/**
	 * Obtiene la decisión automatizada de un entrenador máquina.
	 *
	 * @param trainerId ID del entrenador máquina
	 * @return Array con la decisión tomada por la IA
	 * @throws POOBkemonException si el entrenador no existe o no es máquina
	 */
	public String[] machineDecision(int trainerId) throws POOBkemonException {
		Team team = getTeamByTrainerId(trainerId);
		if (team == null) {
			throw new POOBkemonException(POOBkemonException.TRAINER_NOT_FOUND + trainerId);
		}
		return team.getMachineDecision(this);
	}

	/**
	 * Cabia automaticamente un Pokemon cuando se encuentra debilitado
	 *
	 * @throws POOBkemonException
	 */
	public void autoChangePokemon() throws POOBkemonException {
		for (Team team : teams) {
			Trainer trainer = team.getTrainer();
			Pokemon current = trainer.getPokemonById(trainer.getCurrentPokemonId());

			if (current.getWeak()) {
				int replacementId = trainer.getFirstAlivePokemonId();
				if (replacementId != -1) {
					team.changePokemon(replacementId);
					movesInGame.add(current.getName() + " ha sido debilitado, se ha cambiado a " + trainer.getPokemonById(replacementId).getName());
				}
			}
		}
	}

	/**
	 * Obtiene los ataques de los Pokémon activos.
	 *
	 * @return Mapa con ID de entrenador y matriz de ataques
	 */
	public HashMap<Integer, String[][]> getAttacksPerActivePokemon() {
		HashMap<Integer, String[][]> attacks = new HashMap<>();
		teams.forEach(team ->
				attacks.put(team.getTrainer().getId(), team.getActivePokemonAttacks())
		);
		return attacks;
	}

	/**
	 * Aplica los estados de los Pokemones para cada equipo.
	 */
	public void applyStates() {
		for (Team team : teams) {
			team.applyEffect();
		}
	}

	/**
	 * Obtiene el equipo al que pertenece un entrenador.
	 *
	 * @param id el id del entrenador.
	 * @return El equipo al que pertenece.
	 */
	public Team getTeamByTrainerId(int id) {
		for (Team team : teams) {
			if (team.getTrainer().getId() == id) {
				return team;
			}
		}
		return null;
	}

	/**
	 * Obtiene el nombre del entreador ganador.
	 *
	 * @return El nombre del ganador.
	 * @throws POOBkemonException si no hay ganador.
	 */
	public String getWinner() throws POOBkemonException {
		if (this.winner == -1) {
			throw new POOBkemonException(POOBkemonException.WITHOUT_WINER);
		} else {
			for (Team t : this.teams) {
				if (t.getTrainer().getId() == this.winner) {
					return t.getTrainer().getName();
				}
			}
			return "";
		}
	}

	public ArrayList<Team> getTeams() {
		return teams;
	}

	public boolean isOk() {
		return this.ok;
	}

	/**
	 * Obtiene el ultimo movimiento realizado en el juego.
	 *
	 * @return el ultimo movimiento realizado en el juego.
	 */
	public String getLastMove() {
		return this.movesInGame.getLast();
	}

	/**
	 * Verifica si la batalla ya acabo o no.
	 *
	 * @return Si la batalla ya acabo o no.
	 */
	public boolean getFinishBattle() {
		return this.finishBattle;
	}

	/**
	 * Obtiene la información de un Pokémon específico de un entrenador.
	 *
	 * @param idTrainer ID del entrenador
	 * @param idPokemon ID del Pokémon
	 * @return Array con la información del Pokémon
	 * @throws POOBkemonException Si no se encuentra el entrenador o el Pokémon
	 */
	public String[] getPokemonInfo(int idTrainer, int idPokemon) throws POOBkemonException {
		Team team = getTeamByTrainerId(idTrainer);
		if (team == null) {
			throw new POOBkemonException(POOBkemonException.TRAINER_NOT_FOUND + idTrainer);
		}

		return team.getPokemonById(idPokemon).getInfo();
	}

	/**
	 * Obtiene los IDs de los Pokémon inactivos de un entrenador.
	 *
	 * @param idTrainer ID del entrenador
	 * @return Arreglo con IDs de Pokémon inactivos
	 */
	public int[] getInactivePokemons(int idTrainer) {
		Team team = getTeamByTrainerId(idTrainer);
		if (team == null) return new int[0];
		return team.getInactivePokemonIds();
	}

	public ArrayList<String[]> getPokemonsInfo() {
		return new PokemonRepository().getPokemons();
	}

	public ArrayList<ArrayList<String>> getItemsInfo() {
		return new ItemRepository().getItems();
	}

	public String getAttackForChoose(int id) {
		String move = new MovesRepository().getAttackForChoose(id);
		return move;
	}

	/**
	 * Obtiene el tipo de un ataque.
	 *
	 * @param id el id del ataque.
	 * @return El tipo del ataque.
	 */
	public String getAttackType(int id) {
		String move = new MovesRepository().getAttackType(id);
		return move;
	}

	/**
	 * Obtiene la informacion de los items de un entrenador.
	 *
	 * @param trainerId el id del entrenador del que se necesita la informacion.
	 * @return La informacion de los items del entrenador.
	 * @throws POOBkemonException si no encuentra equipo.
	 */
	public String[][] getInfoItemsPerTrainer(int trainerId) throws POOBkemonException {
		Team team = getTeamByTrainerId(trainerId);
		if (team == null) throw new POOBkemonException(POOBkemonException.TEAM_NOT_FOUND);
		return team.getTrainerItemsInfo();
	}

	/**
	 * Guarda una partida
	 *
	 * @throws IOException si ocurre un error al guardar la partida.
	 */
	public void save(File archivo) throws POOBkemonException {
		try {
			ObjectOutputStream writer = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(archivo.toPath())));
			writer.writeObject(this);
		} catch (IOException e) {
			throw new POOBkemonException("Error al guardar el archivo " + e.getMessage());
		}
	}

	/**
	 * Abre una partida
	 *
	 * @throws IOException            si ocurre un error al abrir la partida.
	 * @throws ClassNotFoundException si se encuentra alguna clase desconocida en el archivo de entrada.
	 */
	public static POOBkemon open(File archivo) throws POOBkemonException {
		try {
			ObjectInputStream reader = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(archivo.toPath())));
			return (POOBkemon) reader.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new POOBkemonException("Error al abrir: " + e.getMessage());
		}
	}

	/**
	 * Para saber cuantos Pokemones hay.
	 *
	 * @return La cantidad de pokemones existente.
	 */
	public int getNumPokemons() {
		return this.numPokemon;
	}

	/**
	 * Para saber cuantos ataques(movimientos) hay.
	 *
	 * @return La cantidad de ataques existente.
	 */
	public int getNumMovements() {
		return this.numMovements;
	}

	/**
	 * Obtiene la probabilidad de que el Pokemon sea shiny.
	 *
	 * @return La probabilidad de que el Pokemon sea shiny.
	 */
	public int getProbShiny() {
		return this.shinyProbability;
	}

	/**
	 * Asigna la probabilidad de que el Pokemon sea shiny.
	 *
	 * @param userProbability la probabilidad que el usuaruo quiera para que un Pokemon sea shiny.
	 */
	public void setProbShiny(int userProbability) {
		this.shinyProbability = userProbability;
	}

	/**
	 * Obtiene el Id del repositorio de un ataque.
	 *
	 * @param attackId el id interno del ataque,
	 * @return La información del ataque desde el repositortio.
	 */
	public String[] getAttackId(int attackId) {
		String[] attackInfo = new MovesRepository().getAttacksId(attackId);
		return attackInfo;

	}

	/**
	 * para dar un valro a la probabilidad del critico
	 *
	 * @param criticalHitChance probabilidad del critico
	 */
	public void setCriticalHitChance(int criticalHitChance) {
		this.criticalHitChance = criticalHitChance;
	}

	public int getCriticalHitChance(){
		return this.criticalHitChance;
	}
}
