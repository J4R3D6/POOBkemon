package domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class DefensiveTest {
    private Defensive defensiveTrainer;
    private Pokemon umbreon;

    @BeforeEach
    void setUp() throws POOBkemonException {
        // Info básica del Pokémon (id, nombre, tipo1, tipo2, stats...)
        String[] umbreonInfo = {"197", "Umbreon", "Dark", "", "525", "95", "65", "110", "60", "130", "65"};

        // Umbreon tendrá dos ataques: uno defensivo (id=1), uno ofensivo (id=2)
        ArrayList<Integer> attackIds = new ArrayList<>(Arrays.asList(1, 2));
        umbreon = new Pokemon(1, umbreonInfo, attackIds, false, 50);

        // Defensive trainer con un solo Pokémon
        BagPack bagPack = new BagPack(new ArrayList<>());
        defensiveTrainer = new Defensive(1, bagPack, "TankBot", new ArrayList<>(Arrays.asList(umbreon)));
    }

    @Test
    void testDefensiveChoosesDefensiveAttackIfAvailable() throws POOBkemonException {
        // Asegurarse de que el primer ataque sea defensivo
        for (Attack attack : umbreon.getAttacks()) {
            if (attack instanceof StateAttack) {
            	((StateAttack) attack).getState().setType(State.StateType.DEFENSE_UP);

            }
        }

        String[] decision = defensiveTrainer.machineDecision(null);
        assertEquals("Attack", decision[0]);
        assertEquals(String.valueOf(umbreon.getId()), decision[2]); // Asegura que ataca el Pokémon correcto
        assertEquals(String.valueOf(defensiveTrainer.getId()), decision[3]); // Asegura que es la máquina correcta
    }

    @Test
    void testDefensiveFallsBackToFirstAvailableAttack() throws POOBkemonException {
        // Hacemos que ningún ataque sea defensivo
        for (Attack attack : umbreon.getAttacks()) {
            if (attack instanceof StateAttack) {
                ((StateAttack) attack).getState().setType(State.StateType.BURN); // No considerado defensivo
            }
        }

        String[] decision = defensiveTrainer.machineDecision(null);
        assertEquals("Attack", decision[0]);
        assertNotNull(decision[1]); // Tiene un ataque disponible
    }

    @Test
    void testDefensiveThrowsExceptionWhenNoAttacksLeft() throws POOBkemonException {
        for (Attack attack : umbreon.getAttacks()) {
            attack.setCurrentPP(0);
        }
        assertThrows(POOBkemonException.class, () -> defensiveTrainer.machineDecision(null));
    }
    
    @Test
    void testDefensiveRecognizesAllDefensiveTypes() throws POOBkemonException {
        State.StateType[] defensiveTypes = {
            State.StateType.DEFENSE_UP,
            State.StateType.PROTECT,
            State.StateType.SUBSTITUTE,
            State.StateType.MAGIC_COAT,
            State.StateType.SP_DEFENSE_UP,
            State.StateType.ATTACK_DOWN,
            State.StateType.SP_ATTACK_DOWN
        };

        for (State.StateType type : defensiveTypes) {
            // Crear un Pokémon con un solo ataque defensivo válido
            String[] info = {"198", "TestMon", "Psychic", "", "500", "100", "100", "100", "100", "100", "100"};
            ArrayList<Integer> attackIds = new ArrayList<>(Arrays.asList(1)); // Asegúrate que id=1 sea StateAttack
            Pokemon pkm = new Pokemon(1, info, attackIds, false, 50);

            for (Attack attack : pkm.getAttacks()) {
                if (attack instanceof StateAttack) {
                    ((StateAttack) attack).getState().setType(type);
                    attack.setCurrentPP(5);
                }
            }

            Defensive trainer = new Defensive(99, new BagPack(new ArrayList<>()), "Tester", new ArrayList<>(Arrays.asList(pkm)));
            String[] decision = trainer.machineDecision(null);

            assertEquals("Attack", decision[0], "Falló con tipo: " + type.name());
            assertEquals(String.valueOf(pkm.getId()), decision[2]);
            assertEquals(String.valueOf(trainer.getId()), decision[3]);
        }
    }
    
    @Test
    void testDefensiveChoosesDefenseUp() throws POOBkemonException {
        String[] info = {"199", "TestMon", "Psychic", "", "500", "100", "100", "100", "100", "100", "100"};
        ArrayList<Integer> attackIds = new ArrayList<>(Arrays.asList(1));
        Pokemon pkm = new Pokemon(1, info, attackIds, false, 50);

        for (Attack attack : pkm.getAttacks()) {
            if (attack instanceof StateAttack) {
                ((StateAttack) attack).getState().setType(State.StateType.DEFENSE_UP);
                attack.setCurrentPP(5);
            }
        }

        Defensive trainer = new Defensive(101, new BagPack(new ArrayList<>()), "Tester", new ArrayList<>(Arrays.asList(pkm)));
        String[] decision = trainer.machineDecision(null);

        assertEquals("Attack", decision[0]);
        assertEquals(String.valueOf(pkm.getId()), decision[2]);
        assertEquals(String.valueOf(trainer.getId()), decision[3]);
    }

    @Test
    void testDefensiveChoosesPROTECT() throws POOBkemonException {
        String[] info = {"200", "TestMon", "Psychic", "", "500", "100", "100", "100", "100", "100", "100"};
        ArrayList<Integer> attackIds = new ArrayList<>(Arrays.asList(1));
        Pokemon pkm = new Pokemon(1, info, attackIds, false, 50);

        for (Attack attack : pkm.getAttacks()) {
            if (attack instanceof StateAttack) {
                ((StateAttack) attack).getState().setType(State.StateType.PROTECT);
                attack.setCurrentPP(5);
            }
        }

        Defensive trainer = new Defensive(102, new BagPack(new ArrayList<>()), "Tester", new ArrayList<>(Arrays.asList(pkm)));
        String[] decision = trainer.machineDecision(null);

        assertEquals("Attack", decision[0]);
        assertEquals(String.valueOf(pkm.getId()), decision[2]);
        assertEquals(String.valueOf(trainer.getId()), decision[3]);
    }

    @Test
    void testDefensiveChoosesSUBSTITUTE() throws POOBkemonException {
        String[] info = {"201", "TestMon", "Psychic", "", "500", "100", "100", "100", "100", "100", "100"};
        ArrayList<Integer> attackIds = new ArrayList<>(Arrays.asList(1));
        Pokemon pkm = new Pokemon(1, info, attackIds, false, 50);

        for (Attack attack : pkm.getAttacks()) {
            if (attack instanceof StateAttack) {
                ((StateAttack) attack).getState().setType(State.StateType.SUBSTITUTE);
                attack.setCurrentPP(5);
            }
        }

        Defensive trainer = new Defensive(103, new BagPack(new ArrayList<>()), "Tester", new ArrayList<>(Arrays.asList(pkm)));
        String[] decision = trainer.machineDecision(null);

        assertEquals("Attack", decision[0]);
        assertEquals(String.valueOf(pkm.getId()), decision[2]);
        assertEquals(String.valueOf(trainer.getId()), decision[3]);
    }

    @Test
    void testDefensiveChoosesMagicCoat() throws POOBkemonException {
        String[] info = {"202", "TestMon", "Psychic", "", "500", "100", "100", "100", "100", "100", "100"};
        ArrayList<Integer> attackIds = new ArrayList<>(Arrays.asList(1));
        Pokemon pkm = new Pokemon(1, info, attackIds, false, 50);

        for (Attack attack : pkm.getAttacks()) {
            if (attack instanceof StateAttack) {
                ((StateAttack) attack).getState().setType(State.StateType.MAGIC_COAT);
                attack.setCurrentPP(5);
            }
        }

        Defensive trainer = new Defensive(104, new BagPack(new ArrayList<>()), "Tester", new ArrayList<>(Arrays.asList(pkm)));
        String[] decision = trainer.machineDecision(null);

        assertEquals("Attack", decision[0]);
        assertEquals(String.valueOf(pkm.getId()), decision[2]);
        assertEquals(String.valueOf(trainer.getId()), decision[3]);
    }

    @Test
    void testDefensiveChoosesSpDeffenseUp() throws POOBkemonException {
        String[] info = {"203", "TestMon", "Psychic", "", "500", "100", "100", "100", "100", "100", "100"};
        ArrayList<Integer> attackIds = new ArrayList<>(Arrays.asList(1));
        Pokemon pkm = new Pokemon(1, info, attackIds, false, 50);

        for (Attack attack : pkm.getAttacks()) {
            if (attack instanceof StateAttack) {
                ((StateAttack) attack).getState().setType(State.StateType.SP_DEFENSE_UP);
                attack.setCurrentPP(5);
            }
        }

        Defensive trainer = new Defensive(105, new BagPack(new ArrayList<>()), "Tester", new ArrayList<>(Arrays.asList(pkm)));
        String[] decision = trainer.machineDecision(null);

        assertEquals("Attack", decision[0]);
        assertEquals(String.valueOf(pkm.getId()), decision[2]);
        assertEquals(String.valueOf(trainer.getId()), decision[3]);
    }

    @Test
    void testDefensiveChoosesAttackDown() throws POOBkemonException {
        String[] info = {"204", "TestMon", "Psychic", "", "500", "100", "100", "100", "100", "100", "100"};
        ArrayList<Integer> attackIds = new ArrayList<>(Arrays.asList(1));
        Pokemon pkm = new Pokemon(1, info, attackIds, false, 50);

        for (Attack attack : pkm.getAttacks()) {
            if (attack instanceof StateAttack) {
                ((StateAttack) attack).getState().setType(State.StateType.ATTACK_DOWN);
                attack.setCurrentPP(5);
            }
        }

        Defensive trainer = new Defensive(106, new BagPack(new ArrayList<>()), "Tester", new ArrayList<>(Arrays.asList(pkm)));
        String[] decision = trainer.machineDecision(null);

        assertEquals("Attack", decision[0]);
        assertEquals(String.valueOf(pkm.getId()), decision[2]);
        assertEquals(String.valueOf(trainer.getId()), decision[3]);
    }

    @Test
    void testDefensiveChoosesSpAttackDown() throws POOBkemonException {
        String[] info = {"205", "TestMon", "Psychic", "", "500", "100", "100", "100", "100", "100", "100"};
        ArrayList<Integer> attackIds = new ArrayList<>(Arrays.asList(1));
        Pokemon pkm = new Pokemon(1, info, attackIds, false, 50);

        for (Attack attack : pkm.getAttacks()) {
            if (attack instanceof StateAttack) {
                ((StateAttack) attack).getState().setType(State.StateType.SP_ATTACK_DOWN);
                attack.setCurrentPP(5);
            }
        }

        Defensive trainer = new Defensive(107, new BagPack(new ArrayList<>()), "Tester", new ArrayList<>(Arrays.asList(pkm)));
        String[] decision = trainer.machineDecision(null);

        assertEquals("Attack", decision[0]);
        assertEquals(String.valueOf(pkm.getId()), decision[2]);
        assertEquals(String.valueOf(trainer.getId()), decision[3]);
    }


}
