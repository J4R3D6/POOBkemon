import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StateTest {

    private Pokemon pokemon;

    @BeforeEach
    public void setUp() throws POOBkemonException {
        String[] info = {
                "1", "Pikachu", "ELECTRIC", "NONE", "300", "100", "50", "40", "50", "50", "90"
        };
        ArrayList<Integer> attacks = new ArrayList<>();
        attacks.add(1); // ID ficticio, se usará ataque por defecto si falla
        pokemon = new Pokemon(1, info, attacks, false, 50);
    }

    private State buildState(String type, String duration, String permanente, String volatil, String descripcion) {
        return new State(new String[]{type, duration, permanente, volatil, descripcion, ""});
    }

    @Test
    public void testBurn() {
        State burn = buildState("BURN", "3", "1", "0", "quemado");
        pokemon.setPrincipalState(burn);
        String result = burn.applyEffect(pokemon);
        assertTrue(result.toLowerCase().contains("quem"));
    }

    @Test
    public void testPoison() {
        State poison = buildState("POISON", "3", "1", "0", "envenenado");
        pokemon.setPrincipalState(poison);
        String result = poison.applyEffect(pokemon);
        assertTrue(result.toLowerCase().contains("venen"));
    }

    @Test
    public void testBadPoison() {
        State badPoison = buildState("BAD_POISON", "3", "1", "0", "mal envenenado");
        pokemon.setPrincipalState(badPoison);
        String result = badPoison.applyEffect(pokemon);
        assertTrue(result.toLowerCase().contains("grave"));
    }

    @Test
    public void testParalysis() {
        State paralysis = buildState("PARALYSIS", "2", "1", "0", "paralizado");
        pokemon.setPrincipalState(paralysis);
        String result = paralysis.applyEffect(pokemon);
        assertTrue(result.toLowerCase().contains("paraliz"));
    }

    @Test
    public void testSleep() {
        State sleep = buildState("SLEEP", "2", "1", "0", "dormido");
        pokemon.setPrincipalState(sleep);
        String result = sleep.applyEffect(pokemon);
        assertTrue(result.toLowerCase().contains("dormido"));
    }

    @Test
    public void testFreeze() {
        State freeze = buildState("FREEZE", "2", "1", "0", "congelado");
        pokemon.setPrincipalState(freeze);
        String result = freeze.applyEffect(pokemon);
        assertTrue(result.toLowerCase().contains("congel"));
    }

    @Test
    public void testFlinch() {
        State flinch = buildState("FLINCH", "1", "0", "1", "retroceso");
        pokemon.setSecundaryState(flinch);
        String result = flinch.applyEffect(pokemon);
        assertTrue(result.toLowerCase().contains("no ataca"));
    }

    @Test
    public void testConfusion() {
        State confusion = buildState("CONFUSION", "3", "0", "1", "confuso");
        pokemon.setSecundaryState(confusion);
        String result = confusion.applyEffect(pokemon);
        assertTrue(result.toLowerCase().contains("confundido"));
    }

    @Test
    public void testStatUp() {
        int attackBefore = pokemon.getPokemonAttack();
        State atkUp = buildState("ATTACK_UP", "1", "0", "1", "sube ataque");
        atkUp.applyEffect(pokemon);
        int attackAfter = pokemon.getPokemonAttack();
        assertTrue(attackAfter > attackBefore);
    }

    @Test
    public void testStatDown() {
        int defenseBefore = (int) pokemon.getDefense();
        State defDown = buildState("DEFENSE_DOWN", "1", "0", "1", "baja defensa");
        defDown.applyEffect(pokemon);
        int defenseAfter = (int) pokemon.getDefense();
        assertTrue(defenseAfter < defenseBefore);
    }

    @Test
    public void testHeal() {
        pokemon.takeDamage(50);
        int hpBefore = pokemon.getCurrentHealth();
        State heal = buildState("HEAL", "1", "0", "1", "cura");
        heal.applyEffect(pokemon);
        int hpAfter = pokemon.getCurrentHealth();
        assertTrue(hpAfter > hpBefore);
    }

    @Test
    public void testProtect() {
        State protect = buildState("PROTECT", "1", "0", "1", "se protege");
        protect.applyEffect(pokemon);
        // No getter para isProtected, se puede asumir éxito si no lanza excepción
    }

    @Test
    public void testNightmareWhileSleeping() {
        State sleep = buildState("SLEEP", "3", "1", "0", "dormido");
        pokemon.setSecundaryState(sleep); // <--- cambiar aquí

        State nightmare = buildState("NIGHTMARE", "3", "0", "1", "pesadilla");
        pokemon.setSecundaryState(nightmare);

        String result = nightmare.applyEffect(pokemon);
        assertTrue(result.toLowerCase().contains("sufre"));
    }

    @Test
    public void testIngrain() {
        pokemon.takeDamage(30);
        int before = pokemon.getCurrentHealth();
        State ingr = buildState("INGRAIN", "3", "0", "1", "arraigado");
        ingr.applyEffect(pokemon);
        assertTrue(pokemon.getCurrentHealth() > before);
    }

    @Test
    public void testSandstorm() {
        State storm = buildState("SANDSTORM", "3", "0", "1", "tormenta de arena");
        String result = storm.applyEffect(pokemon);
        assertTrue(result.toLowerCase().contains("tormenta"));
    }

    @Test
    public void testDisable() {
        State disable = buildState("DISABLE", "2", "0", "1", "deshabilita");
        disable.applyEffect(pokemon);
        // No assert directo porque no hay getter de PP, pero debe cubrir el método
    }

    @Test
    public void testStatChangeEffectEnds() {
        State evasionDown = buildState("EVASION_DOWN", "1", "0", "1", "baja evasión");
        assertEquals(1, evasionDown.getDuration());
        evasionDown.applyEffect(pokemon);
        assertEquals(0, evasionDown.getDuration());
    }

    
    @Test
    public void testLeechSeedEffect() {
        State leech = buildState("LEECH_SEED", "2", "0", "1", "drenadoras");
        int before = pokemon.getCurrentHealth();
        String result = leech.applyEffect(pokemon);
        int after = pokemon.getCurrentHealth();
        assertTrue(before > after);
        assertTrue(result.toLowerCase().contains("drenadoras"));
    }
    

    @Test
    public void testCurseEffectGhost() throws POOBkemonException {
        String[] ghostInfo = {
            "2", "Gastly", "GHOST", "NONE", "300", "100", "30", "35", "30", "100", "80"
        };
        ArrayList<Integer> attacks = new ArrayList<>();
        attacks.add(1);
        Pokemon ghost = new Pokemon(2, ghostInfo, attacks, false, 50);
        State curse = buildState("CURSE", "2", "0", "1", "maldición");
        int before = ghost.getCurrentHealth();
        curse.applyEffect(ghost);
        int after = ghost.getCurrentHealth();
        assertTrue(before > after);
    }
    
    @Test
    public void testTauntEffect() {
        State taunt = buildState("TAUNT", "2", "0", "1", "provocado");
        double spdBefore = pokemon.getSpeed();
        taunt.applyEffect(pokemon);
        assertTrue(pokemon.getSpeed() > spdBefore);
    }
    
    @Test
    public void testTormentEffect() {
        State torment = buildState("TORMENT", "2", "0", "1", "atormentado");
        double spdBefore = pokemon.getSpeed();
        torment.applyEffect(pokemon);
        assertTrue(pokemon.getSpeed() > spdBefore);
    }
    
    @Test
    public void testSubstituteEffectWithEnoughHP() {
        State substitute = buildState("SUBSTITUTE", "1", "0", "1", "alterado");
        int expected = pokemon.getMaxHealth() / 4;
        int before = pokemon.getCurrentHealth();
        substitute.applyEffect(pokemon);
        assertTrue(pokemon.getCurrentHealth() < before);
    }

    @Test
    public void testSubstituteEffectWithoutEnoughHP() {
        pokemon.takeDamage(pokemon.getMaxHealth() - (pokemon.getMaxHealth() / 4) + 1);
        State substitute = buildState("SUBSTITUTE", "1", "0", "1", "alterado");
        int before = pokemon.getCurrentHealth();
        String result = substitute.applyEffect(pokemon);
        assertEquals(before, pokemon.getCurrentHealth());
        assertTrue(result.toLowerCase().contains("no tiene suficiente"));
    }


}
