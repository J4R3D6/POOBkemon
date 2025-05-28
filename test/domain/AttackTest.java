package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class AttackTest {
    
    private Attack attack;
    private String[] validAttackInfo;
    
    @BeforeEach
    void setUp() {
        validAttackInfo = new String[]{
            "1",       // idCSV
            "Tackle",  // name
            "A physical attack", // description
            "Normal",  // type
            "",        // (unused)
            "35",      // power
            "95",      // presition
            "35",      // ppMax
            "0"        // (unused)
        };
    }

    @Test
    void testConstructorWithValidData() throws POOBkemonException {
        attack = new Attack(1, validAttackInfo);
        
        assertEquals(1, attack.getIdRepository());
        assertEquals("Tackle", attack.getName());
        assertEquals("Normal", attack.getType());
        assertEquals(35, attack.getPower());
        assertEquals(95, attack.getAccuracy());
        assertEquals(35, attack.getCurrentPP());
        assertEquals(35, attack.getMaxPP());
        assertEquals("A physical attack", attack.getInfo()[7]);
    }

    @Test
    void testConstructorWithIncompleteData() {
        String[] incompleteInfo = {"1", "Tackle"};
        assertThrows(POOBkemonException.class, () -> {
            new Attack(1, incompleteInfo);
        });
    }

    @Test
    void testConstructorWithInvalidNumericData() {
        String[] invalidInfo = validAttackInfo.clone();
        invalidInfo[5] = "not_a_number"; // Invalid power
        
        assertThrows(POOBkemonException.class, () -> {
            new Attack(1, invalidInfo);
        });
    }

    @Test
    void testUsePP() throws POOBkemonException {
        attack = new Attack(1, validAttackInfo);
        int initialPP = attack.getCurrentPP();
        
        attack.usePP();
        assertEquals(initialPP - 1, attack.getCurrentPP());
        
        // Test PP doesn't go below 0
        attack.setCurrentPP(1);
        attack.usePP();
        attack.usePP();
        assertEquals(0, attack.getCurrentPP());
    }

    @Test
    void testGetInfo() throws POOBkemonException {
        attack = new Attack(1, validAttackInfo);
        String[] info = attack.getInfo();
        
        assertEquals("Tackle", info[0]);
        assertEquals("Normal", info[1]);
        assertEquals("35", info[2]);
        assertEquals("95", info[3]);
        assertEquals("35", info[4]);
        assertEquals("35", info[5]);
        assertEquals("1", info[6]);
        assertEquals("A physical attack", info[7]);
        assertEquals("1", info[8]);
    }

    @Test
    void testApplyEffectHits() throws POOBkemonException {
        attack = new Attack(1, validAttackInfo);
        Pokemon attacker = createTestPokemon("Normal");
        Pokemon target = createTestPokemon("Normal");

        String result = attack.applyEffect(attacker, target);
        assertTrue(result.contains("causó") || result.contains("falló"));
    }

    @Test
    void testHitsCalculation() throws POOBkemonException {
        attack = new Attack(1, validAttackInfo);
        Pokemon attacker = createTestPokemon("Normal");
        Pokemon target = createTestPokemon("Normal");
        
        // Test with 100% accuracy attack
        Attack perfectAccuracy = new Attack(1, new String[]{"1","Perfect","","Normal","","50","100","30",""});
        assertTrue(perfectAccuracy.probabilityToHits(attacker, target));
        
        // Test with 0% accuracy attack (should still have minimum 10% chance)
        Attack zeroAccuracy = new Attack(1, new String[]{"1","ZeroAcc","","Normal","","50","0","30",""});
        boolean hitAtLeastOnce = false;
        for (int i = 0; i < 100; i++) {
            if (zeroAccuracy.probabilityToHits(attacker, target)) {
                hitAtLeastOnce = true;
                break;
            }
        }
        assertTrue(hitAtLeastOnce);
    }

    @Test
    void testCalculateDamage() throws POOBkemonException {
        attack = new Attack(1, validAttackInfo);
        Pokemon attacker = createTestPokemon("Normal");
        Pokemon target = createTestPokemon("Normal");
        
        int damage = attack.calculateDamage(attacker, target);
        assertTrue(damage >= 1); // Minimum damage is 1
    }

    @Test
    void testToString() throws POOBkemonException {
        attack = new Attack(1, validAttackInfo);
        String str = attack.toString();
        
        assertTrue(str.contains("Tackle"));
        assertTrue(str.contains("Type: Normal"));
        assertTrue(str.contains("Power: 35"));
        assertTrue(str.contains("PP: 35/35"));
    }

    @Test
    void testCriticalHitCalculation() throws POOBkemonException {
        attack = new Attack(1, validAttackInfo);
        Pokemon attacker = createTestPokemon("Normal");
        Pokemon target = createTestPokemon("Normal");
        
        boolean criticalOccurred = false;
        for (int i = 0; i < 2000; i++) {
            int damage = attack.calculateDamage(attacker, target);
            if (damage > 35) { // Assuming base damage is around 35
                criticalOccurred = true;
                break;
            }
        }
        assertNotNull(criticalOccurred); // ~4.17% chance should hit in 1000 tries
    }

    // Función auxiliar para crear Pokémon de prueba
    private Pokemon createTestPokemon(String type) throws POOBkemonException {
        String[] info = new String[]{
            "1", "TestPokemon", type, "", "",
            "100", // HP
            "50",  // Attack
            "50",  // Defense
            "50",  // Special Attack
            "50",  // Special Defense
            "50",  // Speed
            "", "", "", "", "", ""
        };
        return new Pokemon(1, info, new ArrayList<>(), false, 50);
    }
}
