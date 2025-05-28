package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class PokemonTest {
    
    private Pokemon pokemon;
    private static final String[] TEST_POKEMON_INFO = {
        "001", "Bulbasaur", "Grass/Poison", "1", "5", // idPokedex, name, type, ?, ?
        "45", "49", "49", "65", "65", "45"     // HP, Attack, Defense, SpAtk, SpDef, Speed
    };
    
    @BeforeEach
    void setUp() throws POOBkemonException {
        // IDs de ataques reales que deberían existir en tu CSV
        ArrayList<Integer> attackIds = new ArrayList<>();
        attackIds.add(1);  // Suponiendo que 1 es el ID de "Tackle" en tu CSV
        attackIds.add(2);  // Suponiendo que 2 es el ID de "Vine Whip" en tu CSV
        
        pokemon = new Pokemon(1, TEST_POKEMON_INFO, attackIds, false, 5);
    }
    
    @Test
    void testPokemonCreation() {
        assertEquals("Bulbasaur", pokemon.getName());
        assertEquals("001", pokemon.idPokedex);
        assertEquals("Grass/Poison", pokemon.getType());
        assertEquals(5, pokemon.getLevel());
        assertFalse(pokemon.getWeak());
    }
    
    @Test
    void testStatsCalculation() {
        // Verificar que las stats se calculan correctamente según el nivel
        // Estos valores pueden variar según tu fórmula de cálculo exacta
        assertTrue(pokemon.maxHealth > 0);
        assertTrue(pokemon.attack > 0);
        assertTrue(pokemon.defense > 0);
        assertTrue(pokemon.specialAttack > 0);
        assertTrue(pokemon.specialDefense > 0);
        assertTrue(pokemon.speed > 0);
        
        assertEquals(pokemon.maxHealth, pokemon.currentHealth);
    }
    
    @Test
    void testAttacksLoading() {
        ArrayList<Attack> attacks = pokemon.getAttacks();
        assertNotNull(attacks);
        assertFalse(attacks.isEmpty());
        
        // Verificar que los ataques se cargaron correctamente desde el CSV
        Attack firstAttack = attacks.get(0);
        assertNotNull(firstAttack);
        assertEquals("Pound", firstAttack.getName()); // Asumiendo que el CSV tiene este ataque
    }
    
    @Test
    void testTakeDamage() {
        int initialHealth = pokemon.currentHealth;
        int damage = 10;
        
        pokemon.takeDamage(damage);
        assertEquals(initialHealth - damage, pokemon.currentHealth);
        
        // Probar daño que debilitaría al Pokémon
        pokemon.takeDamage(pokemon.currentHealth + 10);
        assertEquals(0, pokemon.currentHealth);
        assertTrue(pokemon.getWeak());
    }
    
    @Test
    void testHealing() {
        pokemon.takeDamage(10);
        int damagedHealth = pokemon.currentHealth;
        
        pokemon.heals(5);
        assertEquals(damagedHealth + 5, pokemon.currentHealth);
        
        // Curar más de la vida máxima
        pokemon.heals(pokemon.maxHealth * 2);
        assertEquals(pokemon.maxHealth, pokemon.currentHealth);
    }
    
    @Test
    void testRevive() {
        pokemon.takeDamage(pokemon.maxHealth + 10); // Debilitar
        Item item = new Revive(1);
        item.effect(pokemon);
        assertFalse(pokemon.getWeak());
        assertEquals(pokemon.maxHealth / 2, pokemon.currentHealth);
    }
    @Test

    void testMaxRevive() {
        pokemon.takeDamage(pokemon.maxHealth + 10); // Debilitar
        Item item = new MaxRevive(1);
        item.effect(pokemon);
        assertFalse(pokemon.getWeak());
        assertEquals(pokemon.maxHealth, pokemon.currentHealth);
    }

    @Test
    void testShinyProbability() {
        POOBkemon game = POOBkemon.getInstance();
        game.setProbShiny(100);
        Pokemon testPokemon;
        ArrayList ataques = new ArrayList<>();
        ataques.add(1);
        ataques.add(1);
        ataques.add(1);
        ataques.add(1);
        try {
            testPokemon = new Pokemon(1, TEST_POKEMON_INFO, ataques , false, 5);
        } catch (POOBkemonException e) {
            throw new RuntimeException(e);
        }
        assertTrue(testPokemon.getShiny()); // Estadísticamente debería haber al menos un shiny
    }
}