package domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class OffensiveTest {
    private Offensive offensiveTrainer;
    private Pokemon charizard;

    @BeforeEach
    void setUp() throws POOBkemonException {
        String[] charizardInfo = {"6","Charizard","Fire","Flying","534","78","84","78","109","85","100"};
        ArrayList<Integer> attacks = new ArrayList<>(Arrays.asList(1, 2)); // IDs de ataques
        charizard = new Pokemon(1, charizardInfo, attacks, false, 50);

        BagPack bagPack = new BagPack(new ArrayList<>());
        offensiveTrainer = new Offensive(1, bagPack, "Rival", new ArrayList<>(Arrays.asList(charizard)));
    }

    @Test
    void testOffensiveAlwaysAttacks() throws POOBkemonException {
        String[] decision = offensiveTrainer.machineDecision(null); // No necesita POOBkemon para esta prueba
        assertEquals("Attack", decision[0]);
    }

    @Test
    void testOffensiveChoosesFirstAvailableAttack() throws POOBkemonException {
        String[] decision = offensiveTrainer.machineDecision(null);
        assertNotNull(decision[1]); // ID del ataque
    }

    @Test
    void testOffensiveThrowsExceptionWhenNoAttacksLeft() throws POOBkemonException {
        for (Attack attack : charizard.getAttacks()) {
            attack.setCurrentPP(0);
        }
        assertThrows(POOBkemonException.class, () -> offensiveTrainer.machineDecision(null));
    }
}