package domain;

/**
 * Representa un ítem que revive a un Pokémon debilitado.
 * Al usarlo, restaura parcialmente su salud.
 */
public class Revive extends Item {

	/**
	 * Crea un revivir con cierta cantidad de usos.
	 * @param number Cantidad de ítems disponibles.
	 */
	public Revive(int number) {
		super(number);
		this.name = "revive";
	}

	/**
	 * Aplica el efecto de revivir al Pokémon.
	 * @param pokemon Pokémon objetivo a revivir.
	 */
	@Override
	public void effect(Pokemon pokemon) {
		pokemon.revive();
		int maxHealth =  pokemon.getMaxHealth();
		pokemon.setCurrentHealth((int)maxHealth/2);
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
