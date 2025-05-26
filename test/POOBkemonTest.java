
import domain.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class POOBkemonTest {
    private POOBkemon game;
    private File testSaveFile;

    @BeforeEach
    void setUp() throws POOBkemonException {
        game = POOBkemon.getInstance();
        testSaveFile = new File("test_save.dat");

        // Configuración básica del juego
        ArrayList<String> trainers = new ArrayList<>(Arrays.asList("Player1", "Player2"));

        HashMap<String, ArrayList<Integer>> pokemons = new HashMap<>();
        pokemons.put("Player1", new ArrayList<>(Arrays.asList(25,7))); // Pikachu
        pokemons.put("Player2", new ArrayList<>(Arrays.asList(7,25)));  // Squirtle

        HashMap<String, String[][]> items = new HashMap<>();
        items.put("Player1", new String[][]{{"Potion", "1", "20"}});
        items.put("Player2", new String[][]{{"Potion", "1", "20"}});

        HashMap<String, ArrayList<Integer>> attacks = new HashMap<>();
        attacks.put("Player1", new ArrayList<>(Arrays.asList(1, 2, 3, 4,5,6,7,8))); // 4 ataques
        attacks.put("Player2", new ArrayList<>(Arrays.asList(9,10,11,12,13,14,15,16)));  // 4 ataques

        game.initGame(trainers, pokemons, items, attacks, false,
                new int[]{1, 2}, new String[]{"Ash", "Gary"});
    }

    @AfterEach
    void tearDown() {
        game.resetGame();
        if (testSaveFile.exists()) {
            testSaveFile.delete();
        }
    }

    @Test
    void testSingletonPattern() {
        POOBkemon instance1 = POOBkemon.getInstance();
        POOBkemon instance2 = POOBkemon.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void testInitGameWithValidData() {
        assertEquals(2, game.getTeams().size());
        assertTrue(game.isOk());
    }

    @Test
    void testInitGameWithMissingData() {
        assertThrows(POOBkemonException.class, () -> {
            game.initGame(new ArrayList<>(), new HashMap<>(), new HashMap<>(),
                    new HashMap<>(), false, new int[]{}, new String[]{});
        });
    }

    @Test
    void testTakeDecisionInvalidAction() {
        String[] invalidDecision = {"InvalidAction"};
        assertThrows(POOBkemonException.class, () -> game.takeDecision(invalidDecision));
    }

    @Test
    void testCheckBattleStatus() {
        // Debilitar todos los Pokémon del primer equipo
        for (Pokemon p : game.getTeams().get(0).getTrainer().getPokemons()) {
            p.takeDamage(p.getMaxHealth());
        }

        game.checkBattleStatus();
        assertTrue(game.getFinishBattle());
    }

    @Test
    void testGetCurrentPokemons() throws POOBkemonException {
        HashMap<Integer, String[]> currentPokemons = null;
        try {
            currentPokemons = game.getCurrentPokemons();
        } catch (POOBkemonException e) {
            throw new POOBkemonException(e.getMessage());
        }
        assertEquals(2, currentPokemons.size());
        assertNotNull(currentPokemons.get(1));
    }

    @Test
    void testLoadNullGame() throws POOBkemonException {
        game.save(testSaveFile);
        try{
            POOBkemon loadedGame = game.open(testSaveFile);
        }catch (POOBkemonException e) {
            assertEquals( "Error al abrir: null", e.getMessage());
        }
    }

    @Test
    void testGetPokemonInfo() throws POOBkemonException {
        String[] info = game.getPokemonInfo(1, 0);
        assertNotNull(info);
        assertEquals("25", info[2]); // ID Pokedex de Pikachu
    }

    @Test
    void testGetPokemonInfoinCsv() throws POOBkemonException {
        ArrayList<String[]>  info = game.getPokemonsInfo();
        assertNotNull(info);
        assertEquals(386, info.size()); // ID Pokedex de Pikachu
    }

    @Test
    void testGetItemsInfoinCsv() throws POOBkemonException {
        ArrayList<ArrayList<String>>  info = game.getItemsInfo();
        assertNotNull(info);
        assertEquals(4, info.size()); // ID Pokedex de Pikachu
    }
    
    @Test
    void testGetAttackToChosee() throws POOBkemonException {
        String  info = game.getAttackForChoose(1);
        assertNotNull(info);
        assertEquals("1 | Pound\n" + "T.Normal | C.physical\n" + "PD 40 | PP 35", info); // ID Pokedex de Pikachu
    }
    
    @Test
    void testGetAttackType() throws POOBkemonException {
        String  info = game.getAttackType(1);
        assertNotNull(info);
        assertEquals("Normal", info); // ID Pokedex de Pikachu
    }
    @Test
    void testGetAttackToChoseeNull() throws POOBkemonException {
        String  info = game.getAttackForChoose(0);
        assertNull(info);
    }
    
    @Test
    void testGetAttackTypeNull() throws POOBkemonException {
        String  info = game.getAttackType(0);
        assertNull(info);
    }
    
    @Test
    void testGetAttackNull() throws POOBkemonException {
        String[]  info = game.getAttackId(0);
        assertEquals(0, info.length);
    }
    @Test
    void testCreatePokemon() throws POOBkemonException {
    	Pokemon pokemon = game.createPokemon(1, new ArrayList<>(Arrays.asList(1, 2, 3, 4)));
    	assertNotNull(pokemon);
    }
    @Test
    void testCreatePokemonNull() throws POOBkemonException {
        try {
            Pokemon pokemon = game.createPokemon(0, new ArrayList<>(Arrays.asList(1, 2, 3, 4)));
        }catch (POOBkemonException e){
            assertEquals("No se encontró un pokémon con ID: ",e.getMessage());
        }
    }
    @Test
    void testDeleteGame() {
        game.resetGame();
        assertNull(POOBkemon.getInstance().getTeams());
        assertFalse(POOBkemon.getInstance().isOk());
    }
    @Test
    void testCreatePotion() {
        String[] potionData = {"Potion", "1", "20"};
        Item potion = game.createItem(potionData);
        assertTrue(potion instanceof Potion);
    }

    @Test
    void testCreateRevive() {
        String[] reviveData = {"Revive", "1"};
        Item revive = game.createItem(reviveData);
        assertTrue(revive instanceof Revive);
    }

    @Test
    void testCreateInvalidItem() {
        String[] invalidData = {"Invalid", "1"};
        assertThrows(IllegalArgumentException.class, () -> game.createItem(invalidData));
    }
    @Test
    void testGetTeamByValidId() {
        Team team = game.getTeamByTrainerId(1);
        assertNotNull(team);
        assertEquals(1, team.getTrainer().getId());
    }

    @Test
    void testGetTeamByInvalidId() {
        Team team = game.getTeamByTrainerId(999);
        assertNull(team);
    }
    @Test
    void testSearchExistingPokemon() {
        Pokemon pokemon = game.searchPokemon(0); // Primer Pokémon creado
        assertNotNull(pokemon);
    }

    @Test
    void testSearchNonExistingPokemon() {
        Pokemon pokemon = game.searchPokemon(999);
        assertNull(pokemon);
    }
    @Test
    void testGetWinnerBeforeBattleEnds() {
        assertThrows(POOBkemonException.class, () -> game.getWinner());
    }

    @Test
    void testGetWinnerAfterBattleEnds() throws POOBkemonException {
        // Forzar fin de batalla
        game.run(1);
        assertNotNull(game.getWinner());
    }

    @Test
    void testMachineDecisionForInvalidTrainer() {
        assertThrows(POOBkemonException.class, () -> game.machineDecision(999));
    }

    @Test
    void testAutoChangePokemonWhenWeak() throws POOBkemonException {
        // Debilitar el Pokémon actual
        Pokemon current = game.getTeams().get(0).getTrainer().getActivePokemon();
        current.takeDamage(current.getMaxHealth());

        game.autoChangePokemon();
        // Verificar que se cambió el Pokémon
        assertNotEquals(current.getId(),
                game.getTeams().get(0).getTrainer().getActivePokemon().getId());
    }
    @Test
    void testGetPokemonsPerTrainer() {
        int[] pokemonIds = game.getPokemonsPerTrainer(1);
        assertNotNull(pokemonIds);
        assertTrue(pokemonIds.length > 0);
    }
    @Test
    void testGetInactivePokemons() {
        int[] inactiveIds = game.getInactivePokemons(1);
        assertNotNull(inactiveIds);
        // Debería haber 0 inactivos al inicio (solo 1 Pokémon)
        assertEquals(1, inactiveIds.length);
    }
    
    @Test
    void testInitGameWithOffensiveAndDefensiveTrainers() throws POOBkemonException {
        POOBkemon.getInstance().resetGame();
        POOBkemon game = POOBkemon.getInstance();

        ArrayList<String> trainers = new ArrayList<>(Arrays.asList("Offensive", "Defensive"));
        HashMap<String, ArrayList<Integer>> pokemons = new HashMap<>();
        pokemons.put("Offensive", new ArrayList<>(Arrays.asList(25, 7)));
        pokemons.put("Defensive", new ArrayList<>(Arrays.asList(7, 25)));

        HashMap<String, ArrayList<Integer>> attacks = new HashMap<>();
        attacks.put("Offensive", new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8)));
        attacks.put("Defensive", new ArrayList<>(Arrays.asList(9, 10, 11, 12, 13, 14, 15, 16)));

        HashMap<String, String[][]> items = new HashMap<>();
        items.put("Offensive", new String[0][0]);
        items.put("Defensive", new String[0][0]);

        game.initGame(trainers, pokemons, items, attacks, false,
                new int[]{10, 20}, new String[]{"Red", "Blue"});

        assertEquals(2, game.getTeams().size());
        assertEquals("Red", game.getTeamByTrainerId(10).getTrainer().getName());
    }
    
    @Test
    void testTakeDecisionAttack() throws POOBkemonException {
        int trainerId = 1;
        Team team = game.getTeamByTrainerId(trainerId);
        Pokemon attacker = team.getTrainer().getPokemonById(team.getTrainer().getCurrentPokemonId());

        Attack ataque = attacker.getAttacks().get(0); // obtiene el primer ataque real
        assertNotNull(ataque);

        ataque.setCurrentPP(10); // asegurar que tiene PP

        int attackId = ataque.getIdInGame();
        int pokemonId = attacker.getId();

        String[] decision = {"Attack", String.valueOf(attackId), String.valueOf(pokemonId), String.valueOf(trainerId)};
        game.takeDecision(decision);
    }


    
    @Test
    void testTakeDecisionUseItem() throws POOBkemonException {
        int trainerId = 1;
        int pokemonId = game.getTeamByTrainerId(trainerId).getTrainer().getCurrentPokemonId();
        String[] decision = {"UseItem", String.valueOf(trainerId), String.valueOf(pokemonId), "potion"};
        game.takeDecision(decision);
    }
    
    @Test
    void testTakeDecisionChangePokemon() throws POOBkemonException {
        int trainerId = 1;
        int currentId = game.getTeamByTrainerId(trainerId).getTrainer().getCurrentPokemonId();
        int newId = -1;
        for (Pokemon p : game.getTeamByTrainerId(trainerId).getTrainer().getPokemons()) {
            if (p.getId() != currentId) {
                newId = p.getId();
                break;
            }
        }
        if (newId == -1) fail("No hay segundo Pokémon para probar cambio.");
        String[] decision = {"ChangePokemon", String.valueOf(trainerId), String.valueOf(newId)};
        game.takeDecision(decision);
    }
    
    @Test
    void testTakeDecisionRun() throws POOBkemonException {
        String[] decision = {"Run", "1"};
        game.takeDecision(decision);
        assertTrue(game.getFinishBattle());
    }
    
    @Test
    void testTakeDecisionTimeOver() throws POOBkemonException {
        int trainerId = 1;
        int pokemonId = game.getTeamByTrainerId(trainerId).getTrainer().getCurrentPokemonId();
        String[] decision = {"timeOver", String.valueOf(trainerId), String.valueOf(pokemonId)};
        game.takeDecision(decision);
    }
    
    @Test
    void testAttackMethodDirectly() throws POOBkemonException {
        int trainerId = 1;
        Team team = game.getTeamByTrainerId(trainerId);
        Pokemon attacker = team.getTrainer().getPokemonById(team.getTrainer().getCurrentPokemonId());

        Attack ataque = attacker.getAttacks().get(0); // ataque real
        assertNotNull(ataque);

        ataque.setCurrentPP(10);

        int attackId = ataque.getIdInGame();
        int pokemonId = attacker.getId();

        game.attack(attackId, trainerId, pokemonId);
    }
    
    @Test
    void testGetInfoItemsCoversChain() throws POOBkemonException {
        POOBkemon.getInstance().resetGame();
        POOBkemon game = POOBkemon.getInstance();

        ArrayList<String> trainers = new ArrayList<>(List.of("Player1", "Player2"));

        HashMap<String, ArrayList<Integer>> pokemons = new HashMap<>();
        pokemons.put("Player1", new ArrayList<>(List.of(25, 7)));
        pokemons.put("Player2", new ArrayList<>(List.of(7, 25)));

        HashMap<String, ArrayList<Integer>> attacks = new HashMap<>();
        attacks.put("Player1", new ArrayList<>(List.of(1, 2, 3, 4, 1, 2, 3, 4)));
        attacks.put("Player2", new ArrayList<>(List.of(1, 2, 3, 4, 1, 2, 3, 4)));

        // Agregar una poción y un revive al jugador Player1
        String[][] items1 = {
            {"Potion", "1", "20"},
            {"Revive", "1"}
        };
        String[][] items2 = new String[0][0];

        HashMap<String, String[][]> items = new HashMap<>();
        items.put("Player1", items1);
        items.put("Player2", items2);

        game.initGame(trainers, pokemons, items, attacks, false, new int[]{1, 2}, new String[]{"Ash", "Gary"});

        String[][] result = game.getInfoItemsPerTrainer(1); // ID del Player1
        assertNotNull(result);
        assertTrue(result.length >= 1, "Debe devolver al menos un ítem");
        assertEquals("potion", result[0][0].toLowerCase());
    }
    
    @Test
    void testNumPokemonsAndMovements() {
        assertEquals(386, game.getNumPokemons());     // 2 por entrenador = 4
        assertEquals(356, game.getNumMovements());    // No se ha hecho ninguna acción aún
    } 
    
    @Test
    void testIsMachineFalse() {
        // Player1 no es Machine
        assertFalse(game.isMachine(1));
    }
    
    @Test
    void testGetActiveAttacks() {
        HashMap<Integer, String[][]> activeAttacks = game.getAttacksPerActivePokemon();
        assertNotNull(activeAttacks);
        assertEquals(2, activeAttacks.size()); // Dos entrenadores
        assertTrue(activeAttacks.containsKey(1));
        assertTrue(activeAttacks.get(1).length > 0);
    }
    
    @Test
    void testShinyProbability() {
        game.setProbShiny(42);
        assertEquals(42, game.getProbShiny());
    }
}