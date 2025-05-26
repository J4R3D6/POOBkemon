package domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * La clase BagPack representa la mochila del jugador, donde se almacenan objetos (Items).
 * Permite agregar objetos, obtener un objeto por nombre y consultar el listado de objetos.
 */
public class BagPack implements Serializable {
	private ArrayList<Item> items; // Lista de ítems en la mochila

	/**
	 * Crea una nueva mochila con una lista de ítems inicial.
	 * @param items Lista de ítems a incluir en la mochila
	 */
	public BagPack(ArrayList<Item> items){
		this.items = new ArrayList<Item>(items);
	}

	/**
	 * Busca un ítem en la mochila por su nombre.
	 * @param itemName Nombre del ítem a buscar
	 * @return El ítem correspondiente o null si no se encuentra
	 */
	public Item getItem(String itemName) {
		Item item = null;
		for(Item i: items){
			if(i.getName().equals(itemName)) item = i;
		}
		return item;
	}

	/**
	 * Retorna la información de todos los ítems contenidos en la mochila.
	 * @return Arreglo bidimensional con la información de los ítems
	 */
	public String[][] getItems(){
		String[][] items = new String[this.items.size()][2];
		for (int i = 0; i<this.items.size(); i++ ){
			items[i] = this.items.get(i).getItemInfo();
		}
		return  items;
	}

	/**
	 * Agrega un nuevo ítem a la mochila.
	 * @param item El ítem que se va a agregar
	 */
    public void addItem(Item item) {
		this.items.add(item);
    }
}
