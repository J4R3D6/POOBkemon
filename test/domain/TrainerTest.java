package domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class TrainerTest {
    private Trainer trainer;
    private Pokemon pikachu;
    private Pokemon squirtle;
    private Pokemon charizard;

    @BeforeEach
    void setUp() throws POOBkemonException {
        String[] pikachuInfo = {"25", "Pikachu", "Electric","", "534","78","84","78","109","85","100"};
        String[] squirtleInfo = {"7", "Squirtle", "Water","", "534","78","84","78","109","85","100"};
        String[] charizardInfo = {"6", "Charizard", "Fire","", "534","78","84","78","109","85","100"};

        pikachu = new Pokemon(1, pikachuInfo, new ArrayList<>(), false, 50);
        squirtle = new Pokemon(2, squirtleInfo, new ArrayList<>(), false, 50);
        charizard = new Pokemon(3, charizardInfo, new ArrayList<>(), false, 50);

        BagPack bagPack = new BagPack(new ArrayList<>());
        trainer = new Trainer(1, bagPack, "Ash", new ArrayList<>(Arrays.asList(pikachu, squirtle, charizard)));
    }

    @Test
    void testChangePokemonSuccess() throws POOBkemonException {
        trainer.changePokemon(squirtle.getId());
        assertEquals(squirtle.getId(), trainer.getCurrentPokemonId());
    }

    @Test
    void testChangeToFaintedPokemonThrowsException() {
        squirtle.takeDamage(squirtle.getMaxHealth());
        assertThrows(POOBkemonException.class, () -> trainer.changePokemon(squirtle.getId()));
    }

    @Test
    void testAllFaintedWhenAllPokemonNoWeak() {
        pikachu.takeDamage(pikachu.getMaxHealth());
        squirtle.takeDamage(squirtle.getMaxHealth());
        assertFalse(trainer.allFainted());
    }

    @Test
    void testUseItemPotion() throws POOBkemonException {
        Potion potion = new Potion(1, 20);
        trainer.getBagPack().addItem(potion);
        pikachu.takeDamage(30);
        trainer.useItem(pikachu, "potion");
        assertTrue(pikachu.getCurrentHealth() > 0);
    }

    @Test
    void testGetPokemonByIdReturnsCorrectPokemon() {
        assertEquals(pikachu, trainer.getPokemonById(pikachu.getId()));
        assertEquals(squirtle, trainer.getPokemonById(squirtle.getId()));
        assertNull(trainer.getPokemonById(999)); // ID inexistente
    }

    @Test
    void testGetActivePokemonReturnsFirstActive() {
        assertEquals(pikachu, trainer.getActivePokemon()); // Por defecto, el primero está activo
    }

    @Test
    void testGetInactivePokemonIds() throws POOBkemonException{
        try {
            trainer.changePokemon(squirtle.getId());
        } catch (POOBkemonException e) {
            throw new POOBkemonException("Error por: "+e.getMessage());
        }
        int[] inactiveIds = trainer.getInactivePokemonIds();
        assertEquals(2, inactiveIds.length); // pikachu y charizard inactivos
        assertTrue(Arrays.stream(inactiveIds).anyMatch(id -> id == pikachu.getId()));
    }

    @Test
    void testGetFirstAlivePokemonId() {
        // Todos vivos
        assertEquals(pikachu.getId(), trainer.getFirstAlivePokemonId());

        // Debilitar pikachu
        pikachu.takeDamage(pikachu.getMaxHealth());
        assertEquals(squirtle.getId(), trainer.getFirstAlivePokemonId());

        // Debilitar todos
        squirtle.takeDamage(squirtle.getMaxHealth());
        charizard.takeDamage(charizard.getMaxHealth());
        assertEquals(-1, trainer.getFirstAlivePokemonId()); // Ninguno vivo
    }

    @Test
    void testUseItemRevive() throws POOBkemonException {
        Revive revive = new Revive(1);
        trainer.getBagPack().addItem(revive);
        pikachu.takeDamage(pikachu.getMaxHealth());
        trainer.useItem(pikachu, "revive");
        assertEquals(pikachu.getMaxHealth() / 2, pikachu.getCurrentHealth());
        assertFalse(pikachu.getWeak());
    }

    @Test
    void testUseItemOnFaintedPokemonThrowsException() throws POOBkemonException {
        Potion potion = new Potion(1, 20);
        trainer.getBagPack().addItem(potion);
        pikachu.takeDamage(pikachu.getMaxHealth());
        assertThrows(POOBkemonException.class, () -> trainer.useItem(pikachu, "Potion"));
    }

    @Test
    void testUseNonexistentItemThrowsException() {
        assertThrows(POOBkemonException.class, () -> trainer.useItem(pikachu, "super_potion"));
    }

    @Test
    void testTimeOverOnPokemonWithNormalAttack() {
        int initialPP = pikachu.getAttacks().get(0).getCurrentPP();
        trainer.timeOver(pikachu.getId());
        assertFalse(pikachu.getAttacks().get(0).getCurrentPP() < initialPP); // PP reducido
    }

    @Test
    void testGetAllPokemonIds() {
        int[] ids = trainer.getAllPokemonIds();
        assertEquals(3, ids.length);
        assertArrayEquals(new int[]{1, 2, 3}, ids);
    }

    @Test
    void testGetActivePokemonInfo() {
        String[] info = trainer.getActivePokemonInfo();
        assertEquals("Pikachu", info[1]); // Nombre en posición 1 del array
        assertEquals("50", info[4]); // Nivel en posición 4
    }

    @Test
    void testGetActivePokemonAttacks() throws POOBkemonException{
        // Añadir ataques a pikachu (activo)
        ArrayList<Integer> attackIds = new ArrayList<>(Arrays.asList(1, 2,3,4));
        String[] pikachuInfo = new String[]{"25", "Pikachu", "Electric","", "534","78","84","78","109","85","100"};
        try {
            pikachu = new Pokemon(1, pikachuInfo, attackIds, false, 50);
            trainer = new Trainer(1, new BagPack(new ArrayList<>()), "Ash",
                    new ArrayList<>(Arrays.asList(pikachu, squirtle)));
        } catch (POOBkemonException e) {
        	throw new POOBkemonException("Error por: "+e.getMessage());
        }
        String[][] attacksInfo = trainer.getActivePokemonAttacks();
        assertEquals(4, attacksInfo.length); // 2 ataques
    }
    @Test
    void
    testPokemonActiveAttacksWithTwoIncompleteAttacks() throws POOBkemonException{
        // Añadir ataques a pikachu (activo)
        ArrayList<Integer> attackIds = new ArrayList<>(Arrays.asList(3,4));
        String[] pikachuInfo = new String[]{"25", "Pikachu", "Electric","", "534","78","84","78","109","85","100"};
        try {
            pikachu = new Pokemon(1, pikachuInfo, attackIds, false, 50);
            trainer = new Trainer(1, new BagPack(new ArrayList<>()), "Ash",
                    new ArrayList<>(Arrays.asList(pikachu, squirtle)));
        } catch (POOBkemonException e) {
        	throw new POOBkemonException("Error por: "+e.getMessage());
        }
        String[][] attacksInfo = trainer.getActivePokemonAttacks();
        assertEquals(2, attacksInfo.length); // 2 ataques
    }
}