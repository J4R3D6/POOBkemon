package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class SurviveTest {

    private Survive survive;

    @BeforeEach
    void setUp() {
        survive = Survive.getInstance();
    }

    @Test
    void testSingletonInstanceIsSame() {
        Survive anotherInstance = Survive.getInstance();
        assertSame(survive, anotherInstance, "Survive debe seguir el patrón Singleton");
    }

    @Test
    void testCreatePokemonHasFixedLevel() throws POOBkemonException {
        ArrayList<Integer> attackIds = new ArrayList<>();
        attackIds.add(1);
        attackIds.add(2);

        Pokemon pokemon = survive.createPokemon(1, attackIds);

        assertNotNull(pokemon);
        assertEquals(100, pokemon.getLevel(), "El nivel del Pokémon debe estar fijo en 100");
    }

    @Test
    void testInitGameSetsFixedLevel() throws POOBkemonException {
        ArrayList<String> trainers = new ArrayList<>();
        trainers.add("Player1");
        trainers.add("Player2");

        ArrayList<Integer> pokemonsAsh = new ArrayList<>();
        pokemonsAsh.add(1);  // ID del Pokémon que esté definido en tu repositorio
        
        ArrayList<Integer> pokemonsAsh2 = new ArrayList<>();
        pokemonsAsh2.add(1);  // ID del Pokémon que esté definido en tu repositorio

        ArrayList<Integer> attacksAsh = new ArrayList<>();
        attacksAsh.add(1);
        attacksAsh.add(1);
        attacksAsh.add(1);
        attacksAsh.add(1);// Asegúrate de que este ataque exista
        
        ArrayList<Integer> attacksAsh2 = new ArrayList<>();
        attacksAsh2.add(1);
        attacksAsh2.add(1);
        attacksAsh2.add(1);
        attacksAsh2.add(1);// Asegúrate de que este ataque exista

        HashMap<String, ArrayList<Integer>> pokemons = new HashMap<>();
        pokemons.put("Player1", pokemonsAsh);
        pokemons.put("Player2", pokemonsAsh2);

        HashMap<String, ArrayList<Integer>> attacks = new HashMap<>();
        attacks.put("Player1", attacksAsh);
        attacks.put("Player2", attacksAsh2);


        HashMap<String, String[][]> items = new HashMap<>();
        items.put("Player1", new String[][]{{"Potion", "0","25"},{"Potion", "0","50"},{"Potion", "0","100"},{"Revive","0"}});
        items.put("Player2", new String[][]{{"Potion", "0","25"},{"Potion", "0","50"},{"Potion", "0","100"},{"Revive","0"}});

        int[] idTrainers = new int[]{1,3}; // Un ID para "Ash"
        String[] namesTrainers = new String[]{"Ash","Jared"};

        survive.initGame(trainers, pokemons, items, attacks,false, idTrainers, namesTrainers);

        // Si llegaste aquí sin excepción, el test pasa
        Pokemon p = survive.createPokemon(1, attacksAsh);
        assertEquals(100, p.getLevel());
    }


    @Test
    void testGetInfoItemsReturnsEmptyArray() {
        String[][] items = survive.getInfoItemsPerTrainer(0);
        assertNotNull(items);
        assertEquals(0, items.length, "Debe retornar una matriz vacía");
    }
}

