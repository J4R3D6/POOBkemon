import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TeamTest{
    private Team team;
    private Trainer trainer;
    private Pokemon pokemon1;
    private Pokemon pokemon2;
    private BagPack bagPack;

    @BeforeEach
    void setUp() throws POOBkemonException {
        // Crear instancias reales con datos de prueba
        bagPack = new BagPack(new ArrayList<>());

        // Configurar Pokémon de prueba
        String[] pikachuInfo = {"25", "Pikachu", "Electric","", "534","78","84","78","109","85","100"};
        String[] squirtleInfo = {"7", "Squirtle", "Water","", "534","78","84","78","109","85","100"};

        pokemon1 = new Pokemon(0, pikachuInfo, new ArrayList<>(), false, 50);
        pokemon2 = new Pokemon(1, squirtleInfo, new ArrayList<>(), false, 50);

        // Crear entrenador con los Pokémon
        trainer = new Trainer(1, bagPack, "Ash", new ArrayList<>(Arrays.asList(pokemon1, pokemon2)));
        team = new Team(trainer);
    }

    @Test
    void testGetTrainer() {
        assertEquals(trainer, team.getTrainer());
    }

    @Test
    void testChangePokemonSuccess() throws POOBkemonException {
        Pokemon result = team.changePokemon(1);
        assertEquals(pokemon2, result);
        assertEquals(1, trainer.getCurrentPokemonId());
    }

    @Test
    void testChangePokemonToFaintedThrowsException() {
        pokemon2.takeDamage(pokemon2.getMaxHealth()); // Debilitar a Squirtle
        assertThrows(POOBkemonException.class, () -> team.changePokemon(2));
    }

    @Test
    void testAllFaintedWhenAllPokemonWeak() {
        pokemon1.takeDamage(pokemon1.getMaxHealth());
        pokemon2.takeDamage(pokemon2.getMaxHealth());
        assertTrue(team.allFainted());
    }

    @Test
    void testUseItemPotion() throws POOBkemonException {
        // Agregar poción a la mochila
        bagPack.addItem(new Potion(1, 20));

        // Reducir salud de Pikachu
        int initialHealth = pokemon1.getCurrentHealth();
        pokemon1.takeDamage(15);

        // Usar poción
        team.useItem(1, "potion");

        // Verificar que se curó
        assertFalse(pokemon1.getCurrentHealth() > initialHealth - 15);
    }

    @Test
    void testUseItemOnFaintedPokemonThrowsException() throws POOBkemonException {
        bagPack.addItem(new Potion(1, 20));
        pokemon1.takeDamage(pokemon1.getMaxHealth());
        assertThrows(POOBkemonException.class, () -> team.useItem(1, "Potion"));
    }

    @Test
    void testGetInactivePokemonIds() throws POOBkemonException {
        team.changePokemon(1); // Cambiar a Squirtle (Pikachu queda inactivo)
        int[] inactiveIds = team.getInactivePokemonIds();
        assertEquals(1, inactiveIds.length);
        assertEquals(0, inactiveIds[0]); // ID de Pikachu
    }

    @Test
    void testGetFirstAlivePokemonId() {
        assertEquals(0, team.getFirstAlivePokemonId());

        pokemon1.takeDamage(pokemon1.getMaxHealth());
        assertEquals(1, team.getFirstAlivePokemonId());

        pokemon2.takeDamage(pokemon2.getMaxHealth());
        assertEquals(-1, team.getFirstAlivePokemonId());
    }

    @Test
    void testGetActivePokemonInfo() {
        String[] info = team.getActivePokemonInfo();
        assertEquals("Pikachu", info[1]); // Nombre en posición 1
        assertEquals("50", info[4]); // Nivel en posición 4
    }

    // Clase de prueba para simular un entrenador máquina

    @Test
    void testGetMachineDecisionWithNonMachineTrainer() {
        assertThrows(POOBkemonException.class, () -> team.getMachineDecision(null));
    }
}