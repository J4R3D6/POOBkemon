package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StateAttackTest {

    private Pokemon attacker;
    private Pokemon target;
    private StateAttack stateAttack;

    @BeforeEach
    public void setUp() throws POOBkemonException {
        // Pokémon básico
        String[] infoAttacker = {
            "25", "Pikachu", "ELECTRIC", "NONE", "320", "90", "55", "40", "100", "50", "90"
        };
        String[] infoTarget = {
            "6", "Bulbasaur", "GRASS", "NONE", "300", "80", "49", "49", "65", "65", "45"
        };

        ArrayList<Integer> dummyAttacks = new ArrayList<>();
        dummyAttacks.add(1);

        attacker = new Pokemon(1, infoAttacker, dummyAttacks, false, 50);
        target = new Pokemon(2, infoTarget, dummyAttacks, false, 50);
    }

    private StateAttack createBasicStateAttack(String direccion) throws POOBkemonException {
        String[] attackInfo = {
            "99", "Poison Spikes", "Espinas tóxicas", "POISON", "status", "0", "100", "10", direccion, "POISON", "100"
        };

        String[] stateInfo = {
            "POISON", // nombre del estado
            "3",      // duración
            "false"   // isPersistent
        };

        return new StateAttack(1, attackInfo, stateInfo);
    }

    @Test
    public void testCreation() throws POOBkemonException {
        stateAttack = createBasicStateAttack("ENEMY");
        stateAttack.applyEffect(attacker, target); // ahora sí se inicializa el state
        assertNotNull(stateAttack.getState());
        assertEquals("POISON", stateAttack.getState().getName());
    }

    @Test
    public void testApplyEffectToEnemy() throws POOBkemonException {
        stateAttack = createBasicStateAttack("ENEMY");

        String result = stateAttack.applyEffect(attacker, target);
        assertTrue(result.toLowerCase().contains("afectado") || result.toLowerCase().contains("es inmune"));
    }

    @Test
    public void testApplyEffectToSelf() throws POOBkemonException {
        stateAttack = createBasicStateAttack("ALLY");

        String result = stateAttack.applyEffect(attacker, target); // debería afectar a attacker
        assertTrue(result.contains(attacker.getName()));
    }

    @Test
    public void testGetInfoIncludesStateFields() throws POOBkemonException {
        stateAttack = createBasicStateAttack("ENEMY");
        String[] info = stateAttack.getInfo();

        assertTrue(info.length >= 12, "getInfo debe incluir campos del estado");
        assertEquals("POISON", info[info.length - 4]);
        assertEquals("3", info[info.length - 3]);
        assertEquals("false", info[info.length - 1]);
    }

    @Test
    public void testApplyEffectFailsIfMisses() throws POOBkemonException {
        // Precisión forzada a 0
        String[] attackInfo = {
            "99", "Poison Spikes", "Espinas tóxicas", "POISON", "status", "0", "0", "10", "ENEMY", "POISON", "100"
        };

        String[] stateInfo = {
            "POISON", "3", "false"
        };

        StateAttack missAttack = new StateAttack(1, attackInfo, stateInfo);
        String result = missAttack.applyEffect(attacker, target);

        assertTrue(result.toLowerCase().contains("falló"));
    }

    @Test
    public void testApplyEffectSkipsIfImmune() throws POOBkemonException {
        // BURN contra FIRE debería ser inmune
        String[] attackInfo = {
            "88", "Burn Touch", "Quema al objetivo", "FIRE", "status", "0", "100", "5", "ENEMY", "BURN", "100"
        };

        String[] stateInfo = {
            "BURN", "3", "false"
        };

        StateAttack burnAttack = new StateAttack(1, attackInfo, stateInfo);

        // Crear un Pokémon tipo FIRE
        String[] fireInfo = {
            "6", "Charizard", "FIRE", "FLYING", "534", "78", "84", "78", "109", "85", "100"
        };
        Pokemon firePokemon = new Pokemon(3, fireInfo, new ArrayList<>(), false, 50);

        String result = burnAttack.applyEffect(attacker, firePokemon);
        assertTrue(result.toLowerCase().contains("inmune"));
    }
}

