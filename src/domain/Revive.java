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
	public void itemEffect(Pokemon pokemon) {
		String[] info = new String[4];
		info[0] = "Potion"; // nombre del ítem
		info[1] = "Revive"; // efecto de revivir
		pokemon.itemEffect(info);
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
