package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SpecialTest {

    private Pokemon attacker;
    private Pokemon target;
    private Special specialAttack;

    @BeforeEach
    public void setUp() throws POOBkemonException {
        // Crear Pokémon de prueba con stats válidos
        String[] attackerInfo = {
            "25", "Pikachu", "ELECTRIC", "NONE", "320", "90", "55", "40", "100", "50", "90"
        };
        String[] targetInfo = {
            "6", "Charizard", "FIRE", "FLYING", "534", "78", "84", "78", "109", "85", "100"
        };

        ArrayList<Integer> fakeAttacks = new ArrayList<>();
        fakeAttacks.add(1); // Valor dummy, no se usa directamente

        attacker = new Pokemon(1, attackerInfo, fakeAttacks, false, 50);
        target = new Pokemon(2, targetInfo, fakeAttacks, false, 50);

        // Crear ataque especial con estructura correcta
        String[] attackInfo = {
            "1",                // id
            "Thunderbolt",      // name
            "Un potente rayo",  // description
            "ELECTRIC",         // type
            "special",          // damage_class
            "90",               // power
            "100",              // accuracy
            "15",               // pp
            "ENEMY",            // direccion
            "NONE",             // consequence
            "0"                 // probabilityStatus
        };

        specialAttack = new Special(1, attackInfo);
    }

    @Test
    public void testSpecialAttackCreation() {
        assertEquals("Thunderbolt", specialAttack.getName());
        assertEquals(90, specialAttack.getPower());
        assertEquals("ELECTRIC", specialAttack.getType());
    }

    @Test
    public void testTimeOverUsesPP() {
        int ppBefore = specialAttack.getCurrentPP();
        specialAttack.timeOver();
        int ppAfter = specialAttack.getCurrentPP();
        assertEquals(ppBefore - 1, ppAfter);
    }

    @Test
    public void testCalculateDamageReturnsPositiveValue() {
        int damage = specialAttack.calculateDamage(attacker, target);
        assertTrue(damage >= 1, "El daño debe ser al menos 1");
    }

    @Test
    public void testCalculateDamageConsidersStats() {
        int baseDamage = specialAttack.calculateDamage(attacker, target);

        // Reducir defensa especial para simular más daño
        target.modifyStat("SP_defense", 0.5);
        int increasedDamage = specialAttack.calculateDamage(attacker, target);

        assertTrue(increasedDamage > baseDamage, "El daño debería ser mayor cuando la defensa especial es menor");
    }
}
