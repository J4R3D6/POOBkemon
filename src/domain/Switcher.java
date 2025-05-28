package domain;

import persistencia.StatsRepository;
import java.util.*;
import java.util.Random;

/**
 * Clase que representa una máquina inteligente (IA) que decide estratégicamente
 * si atacar o cambiar de Pokémon basándose en la efectividad de tipos y salud.
 */
public class Switcher extends Machine {

    private static final double BASE_SWITCH_PROBABILITY = 0.3; // Probabilidad base de cambiar de Pokémon
    private static final double HEALTH_SWITCH_THRESHOLD = 0.25; // Umbral de salud baja para cambio
    private static final double TYPE_DISADVANTAGE_THRESHOLD = 0.5; // Umbral de desventaja por tipo

    private static final double TYPE_SCORE_WEIGHT = 0.5; // Peso de efectividad de tipo en evaluación
    private static final double HEALTH_SCORE_WEIGHT = 0.3; // Peso de salud
    private static final double RANDOM_SCORE_WEIGHT = 0.2; // Peso del azar

    private final StatsRepository typeChart; // Referencia a la tabla de tipos
    private final Random random; // Generador de números aleatorios

    /**
     * Crea una nueva máquina del tipo Switcher.
     * @param id Identificador único
     * @param bagPack Mochila del entrenador
     * @param name Nombre de la máquina
     * @param pokemons Lista de Pokémon del entrenador
     * @throws POOBkemonException si ocurre un error al crear la máquina
     */
    public Switcher(int id, BagPack bagPack, String name, ArrayList<Pokemon> pokemons) throws POOBkemonException {
        super(id, bagPack, name, pokemons);
        this.typeChart = new StatsRepository();
        this.random = new Random();
    }

    /**
     * Decide la acción a tomar: atacar o cambiar de Pokémon, según análisis estratégico.
     * @param game Juego actual
     * @return Arreglo con decisión: atacar o cambiar
     * @throws POOBkemonException si no se puede tomar una decisión válida
     */
    @Override
    public String[] machineDecision(POOBkemon game) throws POOBkemonException {
        Pokemon active = getActivePokemon();
        Pokemon opponent = getOpponentActivePokemon(game);

        double typeEffectiveness = calculateTypeEffectiveness(active, opponent);

        if (shouldSwitchPokemon(active, typeEffectiveness)) {
            return createSwitchDecision(active, opponent);
        }
        return createAttackDecision(active, opponent);
    }

    /**
     * Obtiene el Pokémon activo del oponente.
     * @param game Instancia actual del juego
     * @return Pokémon activo del oponente
     * @throws POOBkemonException si no se encuentra el Pokémon oponente
     */
    private Pokemon getOpponentActivePokemon(POOBkemon game) throws POOBkemonException {
        for (Team team : game.getTeams()) {
            if (team.getTrainer().getId() != this.getId()) {
                int currentId = team.getTrainer().getCurrentPokemonId();
                return team.getTrainer().getPokemonById(currentId);
            }
        }
        throw new POOBkemonException(POOBkemonException.POKEMON_INACTIVE);
    }

    /**
     * Evalúa si debería cambiar de Pokémon basado en salud o desventaja de tipo.
     * @param myActive Pokémon activo actual
     * @param typeEffectiveness Efectividad de tipo contra el oponente
     * @return true si se debe cambiar de Pokémon
     */
    private boolean shouldSwitchPokemon(Pokemon myActive, double typeEffectiveness) {
        double healthRatio = (double) myActive.currentHealth / myActive.maxHealth;

        return healthRatio < HEALTH_SWITCH_THRESHOLD
                || typeEffectiveness < TYPE_DISADVANTAGE_THRESHOLD
                || random.nextDouble() < calculateDynamicSwitchProbability(typeEffectiveness);
    }

    /**
     * Calcula la probabilidad de cambiar de Pokémon ajustada dinámicamente según tipo.
     * @param typeEffectiveness Efectividad de tipo
     * @return Probabilidad entre 0.1 y 0.9
     */
    private double calculateDynamicSwitchProbability(double typeEffectiveness) {
        double prob = BASE_SWITCH_PROBABILITY;
        if (typeEffectiveness < 1.0) prob += (1.0 - typeEffectiveness) * 0.3;
        if (typeEffectiveness > 1.0) prob -= (typeEffectiveness - 1.0) * 0.2;
        return Math.max(0.1, Math.min(0.9, prob));
    }

    /**
     * Crea la decisión de cambiar de Pokémon basada en candidatos válidos.
     * @param current Pokémon actual
     * @param opponent Pokémon enemigo
     * @return Arreglo que representa el cambio
     * @throws POOBkemonException si no hay ataques disponibles
     */
    private String[] createSwitchDecision(Pokemon current, Pokemon opponent) throws POOBkemonException {
        List<Pokemon> candidates = new ArrayList<>();
        for (Pokemon p : getPokemons()) {
            if (!p.getWeak() && p.getId() != current.getId()) candidates.add(p);
        }
        if (candidates.isEmpty()) return createAttackDecision(current, opponent);
        Pokemon best = selectBestSwitchCandidate(candidates, opponent);
        return new String[]{"ChangePokemon", String.valueOf(getId()), String.valueOf(best.getId())};
    }

    /**
     * Crea la decisión de ataque seleccionando el mejor ataque disponible.
     * @param attacker Pokémon que ataca
     * @param opponent Objetivo del ataque
     * @return Arreglo con decisión de ataque
     * @throws POOBkemonException si no hay ataques con PP disponibles
     */
    private String[] createAttackDecision(Pokemon attacker, Pokemon opponent) throws POOBkemonException {
        List<Attack> valid = new ArrayList<>();
        for (Attack atk : attacker.getAttacks()) {
            if (atk.getCurrentPP() > 0) valid.add(atk);
        }
        if (valid.isEmpty()) throw new POOBkemonException(POOBkemonException.ATTACK_NOT_FOUND);
        Attack best = selectBestAttack(valid, attacker, opponent);
        return new String[]{"Attack", String.valueOf(best.getIdInGame()), String.valueOf(attacker.getId()), String.valueOf(getId())};
    }

    /**
     * Selecciona el mejor Pokémon candidato para el cambio.
     * @param candidates Lista de Pokémon candidatos
     * @param opponent Pokémon enemigo
     * @return Pokémon que se considera mejor para entrar
     */
    private Pokemon selectBestSwitchCandidate(List<Pokemon> candidates, Pokemon opponent) {
        return candidates.stream()
                .max(Comparator.comparingDouble(p -> evaluateSwitchCandidate(p, opponent)))
                .orElse(candidates.get(0));
    }

    /**
     * Evalúa un Pokémon para determinar su idoneidad como reemplazo.
     * @param p Pokémon candidato
     * @param opponent Pokémon enemigo
     * @return Puntuación del candidato
     */
    private double evaluateSwitchCandidate(Pokemon p, Pokemon opponent) {
        double typeEff = calculateTypeEffectiveness(p, opponent);
        double health = (double) p.currentHealth / p.maxHealth;
        return typeEff * TYPE_SCORE_WEIGHT + health * HEALTH_SCORE_WEIGHT + random.nextDouble() * RANDOM_SCORE_WEIGHT;
    }

    /**
     * Selecciona el mejor ataque posible entre los disponibles.
     * @param attacks Lista de ataques disponibles
     * @param attacker Pokémon atacante
     * @param opponent Pokémon objetivo
     * @return El mejor ataque evaluado
     */
    private Attack selectBestAttack(List<Attack> attacks, Pokemon attacker, Pokemon opponent) {
        return attacks.stream()
                .max(Comparator.comparingDouble(a -> evaluateAttack(a, opponent)))
                .orElse(attacks.get(0));
    }

    /**
     * Evalúa un ataque para determinar su efectividad.
     * @param atk Ataque a evaluar
     * @param opponent Objetivo del ataque
     * @return Puntuación del ataque
     */
    private double evaluateAttack(Attack atk, Pokemon opponent) {
        try {
            double score = typeChart.getMultiplier(atk.getType(), opponent.getType()) * 0.5;
            score += (atk.getPower() / 150.0) * 0.3;
            score += (atk.getAccuracy() / 100.0) * 0.15;
            score += random.nextDouble() * 0.05;
            return score;
        } catch (Exception e) {
            return 0.5 + random.nextDouble() * 0.5;
        }
    }

    /**
     * Calcula la efectividad de tipo de un Pokémon contra otro.
     * @param attacker Pokémon atacante
     * @param defender Pokémon defensor
     * @return Multiplicador de efectividad (1.0 es neutro)
     */
    private double calculateTypeEffectiveness(Pokemon attacker, Pokemon defender) {
        try {
            return typeChart.getMultiplier(attacker.getType(), defender.getType());
        } catch (Exception e) {
            return 1.0;
        }
    }
}