package domain;

/**
 * Representa un ataque de tipo especial que utiliza estadísticas especiales
 * (ataque especial y defensa especial) para calcular el daño.
 */
public class Special extends Attack {

	/**
	 * Constructor del ataque especial.
	 * @param idInside ID interno del ataque.
	 * @param info Información del ataque proveniente del repositorio.
	 * @throws POOBkemonException Si hay error en los datos.
	 */
	public Special(int idInside, String[] info) throws POOBkemonException {
		super(idInside, info);
	}

	/**
	 * Acción a tomar cuando se acaba el tiempo del jugador:
	 * se gasta un punto de poder del ataque.
	 */
	@Override
	public void timeOver() {
		super.usePP();
	}

	/**
	 * Calcula el daño infligido al objetivo usando estadísticas especiales.
	 * @param attacker Pokémon que lanza el ataque.
	 * @param target Pokémon objetivo del ataque.
	 * @return Daño final como número entero.
	 */
	@Override
	public int calculateDamage(Pokemon attacker, Pokemon target) {
		int power = this.getPower();
		int level = attacker.getLevel();
		double randomFactor = 0.85 + (Math.random() * 0.15);
		double critical = (Math.random() < 0.0417) ? 2 : 1.0;

		double attackStat = attacker.getSpecialAttack();
		double defenseStat = target.getSpecialDefense();

		double damage = (((2.0 * level / 5 + 2) * power * attackStat / defenseStat) / 50 + 2);
		damage *= critical * randomFactor;

		return (int) Math.max(1, Math.round(damage));
	}
}
