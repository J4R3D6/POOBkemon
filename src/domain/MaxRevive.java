package domain;

public class MaxRevive extends Revive {

    /**
     * Crea un revivir con cierta cantidad de usos.
     * @param number Cantidad de ítems disponibles.
     */
    public MaxRevive(int number) {
        super(number);
        this.name = "maxRevive";
    }
    /**
     * Aplica el efecto de revivir al Pokémon.
     * @param pokemon Pokémon objetivo a revivir.
     */
    @Override
    public void effect(Pokemon pokemon) {
        pokemon.revive();
        int maxHealth =  pokemon.getMaxHealth();
        pokemon.setCurrentHealth(maxHealth);
    }
}
