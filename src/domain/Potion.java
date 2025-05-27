package domain;

/**
 * Representa una poción que cura puntos de salud a un Pokémon.
 * La cantidad de curación determina el tipo de poción (potion, superPotion, hyperPotion o Mega).
 */
public class Potion extends Item {
	private int healthPoints;

	/**
	 * Crea una nueva poción con una cantidad de usos y puntos de curación.
	 * @param number Cantidad de ítems disponibles.
	 * @param health Cantidad de puntos de vida que cura.
	 */
	public Potion(int number, int health) {
		super(number);
		this.healthPoints = health;
		this.createName();
	}

	/**
	 * Aplica el efecto de curación al Pokémon especificado.
	 * @param pokemon Pokémon objetivo de la curación.
	 */
	@Override
	public void effect(Pokemon pokemon) {
		int maxHealth = pokemon.getMaxHealth();
		int currentHealth = pokemon.getCurrentHealth();
		if(currentHealth+this.healthPoints > maxHealth) {
			pokemon.setCurrentHealth(maxHealth);
		}else {
			pokemon.setCurrentHealth(currentHealth + this.healthPoints);
		}
	}

	/**
	 * Asigna el nombre de la poción según la cantidad de salud que cura.
	 */
	private void createName() {
		if(this.healthPoints >= 1 && this.healthPoints <= 25) this.name = "potion";
		if(this.healthPoints > 25 && this.healthPoints <= 50) this.name = "superPotion";
		if(this.healthPoints > 50 && this.healthPoints <= 100) this.name = "hyperPotion";
		if(this.healthPoints > 100) this.name = "Mega";
	}

	/**
	 * Devuelve el nombre del ítem.
	 * @return Nombre de la poción.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Retorna la información básica del ítem para visualización o exportación.
	 * @return Arreglo con el nombre y cantidad restante.
	 */
	public String[] getItemInfo(){
		String[] info = new String[2];
		info[0] = "" + this.name;
		info[1] = "" + this.amount;
		return info;
	}
}
