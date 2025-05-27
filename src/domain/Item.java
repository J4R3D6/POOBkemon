package domain;

import java.io.Serializable;

/**
 * Clase abstracta que representa un objeto (ítem) dentro del juego.
 * Puede ser usado por un Pokémon para producir un efecto y tiene una cantidad disponible.
 */
public abstract class Item implements Serializable {
	protected int amount; // Cantidad disponible del ítem
	protected String name; // Nombre del ítem

	/**
	 * Constructor base del ítem.
	 * @param number Cantidad inicial del ítem
	 */
	public Item(int number){
		this.amount = number;
	}

	/**
	 * Devuelve la cantidad disponible del ítem.
	 * @return Cantidad restante
	 */
	public int number(){
		return amount;
	}

	/**
	 * Aplica el efecto del ítem al Pokémon si aún no ha sido usado.
	 * @param pokemon Pokémon al cual se aplica el efecto
	 */
	public abstract void effect(Pokemon pokemon);

	/**
	 * Reduce la cantidad de ítems en uno al ser usado.
	 * @return Nueva cantidad restante
	 */
	private int usedItem(){
		this.amount = this.amount - 1;
		return this.amount;
	}

	/**
	 * Verifica si el ítem ya se ha usado completamente.
	 * @return true si no queda cantidad disponible
	 */
	public boolean isUsed(){
		return this.amount == 0;
	}

	/**
	 * Obtiene el nombre del ítem.
	 * @return Nombre del ítem o "Unknown" si no está definido
	 */
	public String getName() {
		if(this.name == null) return "Unknown";
		return this.name;
	}

	/**
	 * Retorna la información del ítem en formato de arreglo de Strings.
	 * @return Información representativa del ítem
	 */
	public abstract String[] getItemInfo();
}
