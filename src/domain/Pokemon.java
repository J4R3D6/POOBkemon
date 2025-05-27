package domain;

import persistencia.MovesRepository;
import persistencia.StatusRepository;

import java.io.Serializable;
import java.util.Random;
import java.util.ArrayList;

/**
 * Represents a Pokemon creature with all its attributes and battle capabilities.
 * This class handles Pokemon creation, stats calculation, and battle mechanics.
 */
/**
 * Representa una criatura Pokémon con atributos de combate, efectos de estado, ataques y mecánicas de batalla.
 *
 * Esta clase forma parte del sistema de combate del juego POOBkemon.
 */
public final class Pokemon implements Serializable {

	// Atributos basicos
	public String name;
	public String idPokedex;
	private int id;
	public String type;

	// Estadisticas de combate
	public int maxHealth;
	public int currentHealth;
	public int attack;
	public int defense;
	public int specialAttack;
	public int specialDefense;
	public int speed;
	public int level;

	private boolean active;
	private boolean weak;
	private boolean shiny;

	// Avances
	public int levelRequirement;
	public int xp;
	public int ivs;

	// Modificadores de batalla
	private static boolean random;
	private static int attackId = 0;
	private ArrayList<Attack> attacks;
	private ArrayList<State> secundaryStates;

	private State principalState;
	private int accuracyStage = (int)(Math.random() * 13) - 6;
	private int evasionStage = (int)(Math.random() * 13) - 6;

	// Constantes para modificador de batalla
	private static double criticalHitChance; // 4.17% standar = 0.0417

	//Variables aplicacion de algunos estados.
	private boolean canAttack = true;
	private boolean isProtected = false;
	private boolean free = true;

	/**
	 * Constructor parametrizado para Pokémon.
	 * @param id ID del Pokémon
	 * @param info Array con información del Pokémon
	 * @param attacksIds Lista de IDs de ataques
	 * @param random Indica si se generan estadísticas aleatorias
	 * @param pokemonLvl Nivel inicial del Pokémon
	 * @throws POOBkemonException si hay error durante la creación
	 */
	public Pokemon(int id, String[] info, ArrayList<Integer> attacksIds, boolean random, int pokemonLvl) throws POOBkemonException {
		try {
			if (info.length < 11) throw new POOBkemonException(POOBkemonException.LESS_INFORMACION_POKEMON);
			this.initStats(id, info, attacksIds, random, pokemonLvl);
		} catch (POOBkemonException | NumberFormatException e) {
			Log.record(e);
		}
		this.probShiny();
	}

	/**
	 * Initializes Pokemon from parameters.
	 * @param id Pokemon ID
	 * @param info Pokemon information array
	 * @param attacksIds List of attack IDs
	 * @param random Whether to generate random stats
	 * @param pokemonLvl Initial level
	 * @throws POOBkemonException if there's an error during creation
	 */
	private void initStats(int id, String[] info, ArrayList<Integer> attacksIds, boolean random, int pokemonLvl) throws POOBkemonException {
		this.id = id;
		this.name = info[1];
		this.idPokedex = info[0];
		this.type = info[2];

		// Level handling
		this.level = random ? (int)(Math.random() * 31) + 25 : pokemonLvl;

		this.levelRequirement = 100;
		this.xp = 0;
		this.active = false;
		this.weak = false;
		this.random = random;
		this.attacks = new ArrayList<>(this.createAttacks(attacksIds));
		this.secundaryStates = new ArrayList<State>();
		this.ivs = createRandom(32);

		// Base stats
		int baseHP = Integer.parseInt(info[5]);
		int baseAttack = Integer.parseInt(info[6]);
		int baseDefense = Integer.parseInt(info[7]);
		int baseSpAttack = Integer.parseInt(info[8]);
		int baseSpDefense = Integer.parseInt(info[9]);
		int baseSpeed = Integer.parseInt(info[10]);

		// Calculate scaled stat
		if (random) {
			this.maxHealth = calculateHPStat(baseHP, this.level, true);
			this.attack = calculateOtherStat(baseAttack, this.level, true);
			this.defense = calculateOtherStat(baseDefense, this.level, true);
			this.specialAttack = calculateOtherStat(baseSpAttack, this.level, true);
			this.specialDefense = calculateOtherStat(baseSpDefense, this.level, true);
			this.speed = calculateOtherStat(baseSpeed, this.level, true);
		} else {
			this.maxHealth = calculateHPStat(baseHP, this.level, false);
			this.attack = calculateOtherStat(baseAttack, this.level, false);
			this.defense = calculateOtherStat(baseDefense, this.level, false);
			this.specialAttack = calculateOtherStat(baseSpAttack, this.level, false);
			this.specialDefense = calculateOtherStat(baseSpDefense, this.level, false);
			this.speed = calculateOtherStat(baseSpeed, this.level, false);
		}

		this.currentHealth = this.maxHealth;
		if(this.attacks.size() == 0) {
			this.lastAttack();
		}
	}

	/**
	 * Calcula la estadística de HP usando la fórmula de Pokémon.
	 * @param baseStat HP base
	 * @param level Nivel del Pokémon
	 * @param random Indica si usar IVs/EVs aleatorios
	 * @return HP calculado
	 */
	private int calculateHPStat(int baseStat, int level, boolean random) {
		int iv = random ? (int)(Math.random() * 32) : this.ivs;
		int ev = random ? (int)(Math.random() * 256) : 0;
		return (int)(((2 * baseStat + iv + (ev / 4)) * level) / 100) + level + 10;
	}

	/**
	 * Calcula otras estadísticas usando la fórmula de Pokémon.
	 * @param baseStat Valor base de la estadística
	 * @param level Nivel del Pokémon
	 * @param random Indica si usar IVs/EVs aleatorios
	 * @return Valor de la estadística calculada
	 */
	private int calculateOtherStat(int baseStat, int level, boolean random) {
		int iv = random ? (int)(Math.random() * 32) : this.ivs;
		int ev = random ? (int)(Math.random() * 256) : 0;
		return (int)(((2 * baseStat + iv + (ev / 4)) * level / 100) + 5);
	}

	/**
	 * Genera un numero random hasta un limite.
	 * @param limit Limite.
	 * @return numero aleatorio.
	 */
	public int createRandom(int limit) {
		return new Random().nextInt(limit);
	}

	// Getters and setters


	public boolean getActive() { return this.active; }

	public void setActive(boolean active) { this.active = active; }

	public int getId() { return this.id; }

	public String getName() { return this.name; }

	public boolean getWeak() { return this.weak; }

	public ArrayList<Attack> getAttacks() { return this.attacks; }


	/**
	 * Obtener un ataque especifico dado su ID.
	 * @param id ID del ataque.
	 * @return El ataque como objeto o null si no lo encuentra.
	 */
	public Attack getAttackById(int id) {
		for(Attack ataque : attacks) {
			if(ataque.getIdInGame() == id) {
				return ataque;
			}
		}
		return null;
	}

	/**
	 * Crear ataques con sus id's.
	 * @param attacksIds Lista de id's de ataques.
	 * @return Lista de objetos de tipo Ataque.
	 * @throws POOBkemonException si hay fallas en la creación de los ataques.
	 */
	private ArrayList<Attack> createAttacks(ArrayList<Integer> attacksIds) throws POOBkemonException {
		ArrayList<Attack> attacks = new ArrayList<>();
		MovesRepository movesRepository = new MovesRepository();
		StatusRepository statusRepository = new StatusRepository();

		for(Integer id : attacksIds) {
			String[] infoAttack = movesRepository.getAttacksId(id);

			if(infoAttack[4].equalsIgnoreCase("physical")) {
				attacks.add(new Attack(this.nextAttackId(), infoAttack));
			} else if(infoAttack[4].equalsIgnoreCase("special")) {
				attacks.add(new Special(this.nextAttackId(), infoAttack));

			} else if(infoAttack[4].equalsIgnoreCase("status")) {
				String[] infoStatus = statusRepository.getStatusByName(infoAttack[9].toUpperCase());
				if(infoStatus == null || infoStatus.length == 0) {
					infoStatus = statusRepository.getStatusByName("DEFENSE_UP");
				}
				attacks.add(new StateAttack(this.nextAttackId(), infoAttack, infoStatus));
			}
		}
		return attacks;
	}

	private int nextAttackId() {
		return this.attackId++;
	}

	/**
	 * Calcula el daño recibido por un ataque.
	 * @param attack Ataque recibido
	 * @param attacker Pokémon que ataca
	 * @return Mensaje con el efecto del ataque
	 * @throws POOBkemonException si falla el cálculo de daño
	 */
	public String getDamage(Attack attack, Pokemon attacker) throws POOBkemonException {
		String effect = attack.applyEffect(attacker, this);
		attack.usePP();
		attacker.lastAttack();
		return effect;
	}

		/**
	 * Verifica si el Pokémon puede recibir daño.
	 * @param attacker Pokémon atacante
	 * @return true si puede recibir daño, false en caso contrario
	 */
	public boolean canReceiveDamage(Pokemon attacker) {
		return attacker.currentHealth > 0 && this.currentHealth > 0;
	}

	/**
	 * Aplica los estados al Pokémon.
	 */
	public void isWeak(){
		if (this.currentHealth <= 0) {
			this.currentHealth = 0;
			this.weak = true;
		}
	}

	/**
	 * Aplica un estado secundrio al Pokemon.
	 * @param state el estado a aplicar(secundario)
	 */
	public void setSecundaryState(State state){
		this.secundaryStates.add(state);
	}

	/**
	 * Aplica un estado principal al Pokemon.
	 * @param state el estado a aplicar(principal)
	 */
	public void setPrincipalState(State state) {
		this.principalState = state;
	}

	/**
	 * Obtiene la información del Pokémon en un arreglo de Strings.
	 * @return Arreglo con toda la información del Pokémon
	 */
	public String[] getInfo() {
		return new String[] {
				String.valueOf(this.id),           // 0 - ID
				this.name,                         // 1 - Nombre
				this.idPokedex,                    // 2 - ID Pokédex
				this.type,                         // 3 - Tipo
				String.valueOf(this.level),        // 4 - Nivel
				String.valueOf(this.maxHealth),    // 5 - Vida máxima
				String.valueOf(this.currentHealth), // 6 - Vida actual
				String.valueOf(this.attack),        // 7 - Ataque
				String.valueOf(this.defense),      // 8 - Defensa
				String.valueOf(this.specialAttack), // 9 - Ataque Especial
				String.valueOf(this.specialDefense),// 10 - Defensa Especial
				String.valueOf(this.speed),         // 11 - Velocidad
				String.valueOf(this.xp),            // 12 - XP actual
				String.valueOf(this.levelRequirement), // 13 - XP requerido
				String.valueOf(this.active),        // 14 - Estado (activo)
				String.valueOf(this.weak),          // 15 - Pokemon debilitado
				String.valueOf(this.shiny),          // 16 - Pokemon shiny
				String.valueOf(this.principalState)  //17 - Estado principal del pokemon.
		};
	}

	/**
	 * Obtiene la información de todos los ataques del Pokémon.
	 * @return Matriz con la información de cada ataque
	 */
	public String[][] getAttacksInfo() {
		int attacksSize = attacks.size();
		String[][] attacksInfo = new String[attacksSize][9];

		for (int i = 0; i < attacksSize; i++) {
			Attack attack = attacks.get(i);
			if (attack != null) {
				attacksInfo[i] = attack.getInfo();
			} else {
				attacksInfo[i] = new String[]{
						"Desconocido",  // nombre
						"Normal",       // tipo
						"0",            // poder
						"0",            // precisión
						"0",            // pp
						"0",            // id
						""              // descripción
				};
			}
		}
		return attacksInfo;
	}

	/**
	 * Cura al Pokémon una cantidad específica de HP.
	 * @param heal Cantidad de HP a curar
	 */
	public void heals(int heal) {
		if(!this.weak){
			this.currentHealth = this.currentHealth + heal;
			if(this.currentHealth > this.maxHealth) {
				this.currentHealth = this.maxHealth;
			}
		}
	}

	/**
	 * Revive al Pokémon con la mitad de su vida máxima.
	 */
	public void revive() {
		this.weak = false;
		this.principalState = null;
	}


	/**
	 * Genera el ultio ataque cuando todos los ataques tiene 0 PP.
	 * @throws POOBkemonException si hay error al crear ataque
	 */
	public void lastAttack() throws POOBkemonException {
		if(allAttackWith0PP()){
			ArrayList<Integer> unickAttack = new ArrayList<>();
			unickAttack.add(357);
			this.attacks = this.createAttacks(unickAttack);
		}
	}

	/**
	 * Verifica que todos los Ataques tengan PP 0
	 */
	private boolean allAttackWith0PP(){
		for(Attack attack: this.attacks) {
			if(attack.getCurrentPP() > 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Aplica daño recibido al Pokemon
	 * @param  damage el daño(valor) a recibir.
	 */
	public void takeDamage( int damage ) {
		this.currentHealth -= damage;
		if(this.currentHealth <= 0) {
			this.currentHealth = 0;
			this.weak = true;
		}
	}

	/**
	 * Determina si el pokemon es Shiny.
	 */
	private void probShiny() {
		this.shiny = Math.random() <= new POOBkemon().getProbShiny()/100;
	}

	/**
	 * Decision auntomatica si se le acaba el tiempo al entrenador
	 */
	public void timeOver() {
		for (Attack at : this.attacks) {
			at.timeOver();
		}
		try{
			this.lastAttack();
		}catch(POOBkemonException e){
		}
	}

	/**
	 * Verifica que el Pokemon tenga un estado secundario.
	 * @param stateName el estado que se quiere consultar.
	 */
	public boolean hasState(String stateName) {
		for (State s : secundaryStates) {
			if (s.getName().equalsIgnoreCase(stateName)) {
				return true;
			}
		}
		return false;
	}

	public void setProtected(boolean protect){
		this.isProtected = protect;
	}
	
	public String getType() {
		return type;
	}

	public void setCanAttack(boolean active){
		this.canAttack = active;
	}

	
	/**
	 * Modifca las estadisticas del Pokemon(para aplicacion de Estados)
	 * @param stat el nombre de la estadistica que se quiere modificar
	 * @param multiplicator multiplicador de que se hara con la estadisica(subir o bajar).
	 */
	public void modifyStat(String stat, double multiplicator){
		switch (stat){
			case "attack":
				this.attack = (int)(this.attack*multiplicator);
				break;
			case "defense":
				this.defense = (int)(this.defense*multiplicator);
				break;
			case "speed":
				this.speed = (int)(this.speed*multiplicator);
				break;
			case "SP_defense":
				this.specialDefense = (int)(this.specialDefense*multiplicator);
				break;
			case "SP_attack":
				this.specialAttack = (int)(this.specialAttack*multiplicator);
				break;
			case "Critico":
				this.criticalHitChance = this.criticalHitChance*multiplicator;
				break;
			case "evasion":
				this.evasionStage = (int)(this.evasionStage*multiplicator);
				break;
		}
	}
	
	/**
	 * Desactiva el ultimo movimiento usado(para estados).
	 */
	public void disableLastMove(){
		this.attacks.get(this.attacks.size()-1).setCurrentPP(0);
	}
	public void setTrapped(boolean tramp){
		this.free = tramp;
	}
	public void setNewPs(int ps){
		this.maxHealth = this.maxHealth + ps;
	}


	/**
	 * Aplicar los estados al Pokemon.
	 */
	public void applyState(){
		if(!(this.principalState == null)) {
			this.principalState.applyEffect(this);
		}else if(this.principalState != null && this.principalState.getDuration() == 0){
			principalState = null;
		}
		deleteSecundaryStates();
		for (State s : secundaryStates) {
			s.applyEffect(this);
		}
	}

	/**
	 * Elimina el estado principal si coincide con el estado que recibe
	 * @param state el estado que se quiere verificar.
	 */
	public void deletePricipalState(State state){
		if(this.principalState == state){
			principalState = null;
		}
	}

	/**
	 * Elimina los estados secundarios que tengan duración 0(finalizaron)
	 */
	private void deleteSecundaryStates(){
		int stateCount = 0;
		while(stateCount < this.secundaryStates.size()){
			if(this.secundaryStates.get(stateCount).getDuration() == 0){
				this.secundaryStates.remove(stateCount);
			}else{
				stateCount ++;
			}
		}
	}

	public double getDefense() {
		return defense;
	}

	public int getLevel() {
		return level;
	}
	public int getPokemonAttack() {
		return attack;
	}

	public double getSpecialAttack() {
		return specialAttack;
	}

	public double getSpecialDefense() {
		return specialDefense;
	}

	public double getAccuracyStage() {
		return accuracyStage;
	}

	public double getEvasionStage() {
		return evasionStage;
	}

	public State getPrincipalState() {
		return principalState;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}
	
	public int getSpeed() {
		return this.speed;
    }

    public boolean getShiny(){
        return this.shiny;
    }

	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}
    public void setCriticalHitChance(int criticalHitChance){
        this.criticalHitChance = criticalHitChance/100;
    }
}