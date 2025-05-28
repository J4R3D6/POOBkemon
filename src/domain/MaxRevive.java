package domain;

public class MaxRevive extends Revive {

    /**
     * Crea un revivir con cierta cantidad de usos.
     * @param amount Cantidad de ítems disponibles.
     */
    public MaxRevive(int amount) {
        super(amount);
        this.name = "maxRevive";
    }
    /**
     * Aplica el efecto de revivir al Pokémon.
     * @param pokemon Pokémon objetivo a revivir.
     */
    @Override
    public void effect(Pokemon pokemon) {
        super.effect(pokemon);
        pokemon.revive();
        int maxHealth =  pokemon.getMaxHealth();
        pokemon.setCurrentHealth(maxHealth);
    }
}
