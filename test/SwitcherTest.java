import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SwitcherTest {

    private POOBkemon juego;
    private int machineId = 100;
    private int humanId = 200;

    @BeforeEach
    public void setUp() throws POOBkemonException {
        juego = POOBkemon.getInstance();
        juego.resetGame(); // limpiar singleton

        // Nombres de entrenadores (los tipos definen el tipo de IA)
        ArrayList<String> trainerTypes = new ArrayList<>();
        trainerTypes.add("Switcher1");
        trainerTypes.add("Player2");

        // IDs de los entrenadores
        int[] ids = {machineId, humanId};

        // Nombres visibles
        String[] names = {"Bot", "Jugador"};

        // Pokémon y ataques (4 por Pokémon)
        HashMap<String, ArrayList<Integer>> pokemons = new HashMap<>();
        HashMap<String, ArrayList<Integer>> ataques = new HashMap<>();

        ArrayList<Integer> idsP1 = new ArrayList<>(List.of(25, 7)); // Pikachu, Squirtle
        ArrayList<Integer> atksP1 = new ArrayList<>(List.of(1, 1, 1, 1, 2, 2, 2, 2)); // 4 para cada uno
        pokemons.put("Switcher1", idsP1);
        ataques.put("Switcher1", atksP1);

        ArrayList<Integer> idsP2 = new ArrayList<>(List.of(74)); // Geodude
        ArrayList<Integer> atksP2 = new ArrayList<>(List.of(3, 3, 3, 3));
        pokemons.put("Player2", idsP2);
        ataques.put("Player2", atksP2);

        // Ítems vacíos
        HashMap<String, String[][]> items = new HashMap<>();
        items.put("Switcher1", new String[0][0]);
        items.put("Player2", new String[0][0]);

        // Iniciar juego correctamente
        juego.initGame(trainerTypes, pokemons, items, ataques, false, ids, names);
    }

    @Test
    public void testSwitcherShouldSwitchWhenLowHealth() throws POOBkemonException {
        Team team = juego.getTeamByTrainerId(machineId);
        Pokemon activo = team.getTrainer().getPokemonById(team.getTrainer().getCurrentPokemonId());
        activo.takeDamage(activo.getMaxHealth() - 10); // lo deja con poca vida

        String[] decision = juego.machineDecision(machineId);
        assertEquals("ChangePokemon", decision[0]);
    }

    @Test
    public void testSwitcherShouldAttackWithGoodHealth() throws POOBkemonException {
        String[] decision = juego.machineDecision(machineId);
        assertEquals("Attack", decision[0]);
    }

    @Test
    public void testSwitcherShouldSwitchWithBadType() throws POOBkemonException {
        // Pikachu (ELECTRIC) vs Geodude (GROUND) = inmunidad
        String[] decision = juego.machineDecision(machineId);
        assertEquals("ChangePokemon", decision[0]);
    }

    @Test
    public void testSwitcherReturnsValidAttackFormat() throws POOBkemonException {
        String[] decision = juego.machineDecision(machineId);
        if (decision[0].equals("Attack")) {
            assertEquals(4, decision.length);
            assertDoesNotThrow(() -> Integer.parseInt(decision[1]));
        }
    }
}
