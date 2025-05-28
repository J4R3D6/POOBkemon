package domain;

import java.io.Serializable;

/**
 * La clase Attack representa un ataque que puede realizar un Pokémon en combate.
 * Contiene atributos como nombre, tipo, poder, precisión y puntos de poder (PP).
 * También incluye lógica para aplicar daño, calcular probabilidad de impacto y manejar efectos.
 */
public class Attack implements Serializable {

	protected String name; // Nombre del ataque
	private String type; // Tipo del ataque (por ejemplo, "Normal")
	private int power; // Poder del ataque
	private int presition; // Precisión del ataque (0-100)
	private int maxPp; // Máximo de puntos de poder (PP)
	protected int currentPp; // PP actuales
	private int idRepository; // ID del ataque según el repositorio (CSV)
	private int idInGame; // ID interno del juego
	private String description; // Descripción textual del ataque

	/**
	 * Constructor del ataque. Asigna los datos recibidos desde un arreglo de Strings.
	 * @param idInGame ID único en el juego
	 * @param infoAttack Arreglo con los datos del ataque
	 * @throws POOBkemonException si hay error en el formato del arreglo
	 */
	public Attack(int idInGame, String[] infoAttack) throws POOBkemonException {
		try {
			this.idInGame = idInGame;
			this.idRepository = Integer.parseInt(infoAttack[0]);
			this.name = infoAttack[1];
			this.type = infoAttack[3];
			this.power = Integer.parseInt(infoAttack[5]);
			this.presition = Integer.parseInt(infoAttack[6]);
			this.maxPp = Integer.parseInt(infoAttack[7]);
			this.currentPp = this.maxPp;
			this.description = infoAttack[2];
		} catch (Exception e) {
			throw new POOBkemonException(POOBkemonException.ATTACK_ERROR + e.getMessage());
		}
	}

	/**
	 * Reduce en uno el PP actual del ataque. Nunca baja de 0.
	 */
	public void usePP(){
		this.currentPp--;
		if(this.currentPp <= 0){
			this.currentPp = 0;
		}
	}

	/**
	 * Método vacío, reservado para redefinir cuando sea necesario por herencia.
	 */
	public void timeOver(){}

	/**
	 * Devuelve un arreglo con la información completa del ataque.
	 * @return Arreglo con nombre, tipo, poder, precisión, PP, IDs y descripción.
	 */
	public String[] getInfo() {
		String[] info = new String[9];
		info[0] = this.name;
		info[1] = this.type;
		info[2] = String.valueOf(this.power);
		info[3] = String.valueOf(this.presition);
		info[4] = String.valueOf(this.currentPp);
		info[5] = String.valueOf(this.maxPp);
		info[6] = String.valueOf(this.idRepository);
		info[7] = this.description;
		info[8] = String.valueOf(this.idInGame);
		return info;
	}

	/**
	 * Representación del ataque en forma de texto.
	 * @return Cadena con nombre, tipo, poder, precisión y PP.
	 */
	@Override
	public String toString() {
		return String.format("%s (Type: %s, Power: %d, Accuracy: %d%%, PP: %d/%d)",
				name, type, power, presition, currentPp, maxPp);
	}

	// Métodos de acceso (getters y setters)

	public int getIdInGame() {
		return this.idInGame;
	}

	public int getIdRepository(){
		return this.idRepository;
	}

	public int getCurrentPP(){
		return this.currentPp;
	}

	public int getMaxPP(){
		return this.maxPp;
	}

	public int getAccuracy() {
		return this.presition;
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public void setCurrentPP(int ppActual) {
		this.currentPp = ppActual;
	}

	public int getPower() {
		return this.power;
	}

	/**
	 * Aplica el efecto del ataque sobre un objetivo.
	 * @param attacker El Pokémon atacante
	 * @param target El Pokémon objetivo
	 * @return Mensaje con el resultado del ataque
	 * @throws POOBkemonException si ocurre un error en el ataque
	 */
	public String applyEffect(Pokemon attacker, Pokemon target){
		if (!target.canReceiveDamage(attacker)) return "";
		if (!this.probabilityToHits(attacker, target)) return attacker.getName() + " falló el ataque!";

		int damage = calculateDamage(attacker, target);
		target.takeDamage(damage);

		return this.name + " causó " + damage + " puntos de daño!";
	}

	/**
	 * Determina si el ataque impacta al objetivo basado en precisión y evasión.
	 * @param attacker El Pokémon atacante
	 * @param target El Pokémon objetivo
	 * @return true si el ataque acierta, false si falla
	 */
	public boolean probabilityToHits(Pokemon attacker, Pokemon target) {
		if (this.presition >= 100) return true;
		double hitProbability = (this.presition / 100.0) *
				(Math.pow(1.3, attacker.getAccuracyStage()) / Math.pow(1.3, -target.getEvasionStage()));
		return Math.random() < Math.max(0.1, Math.min(1.0, hitProbability));
	}

	/**
	 * Calcula el daño infligido por el ataque al objetivo.
	 * Incluye factores como poder, nivel, estadísticas, crítico y aleatoriedad.
	 * @param attacker El Pokémon que ataca
	 * @param target El Pokémon que recibe el ataque
	 * @return Cantidad de daño entero a infligir
	 */
	public int calculateDamage(Pokemon attacker, Pokemon target) {
		int power = this.power;
		int level = attacker.getLevel();
		double randomFactor = 0.85 + (Math.random() * 0.15);
		double critical = (Math.random() < 0.0417) ? 2 : 1.0;

		double attackStat = attacker.getPokemonAttack();
		double defenseStat = target.getDefense();

		double damage = (((2 * level / 5 + 2) * power * attackStat / defenseStat) / 50 + 2);
		damage *= critical * randomFactor;

		return (int)Math.max(1, Math.round(damage));
	}
}
