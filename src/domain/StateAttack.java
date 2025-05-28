package domain;

import persistencia.StatusRepository;
/**
 * Representa un ataque de estado que modifica
 * las estadísticas de un Pokémon. Puede tener efectos positivos (mejoras)
 * o negativos (estados alterados), y puede afectar tanto al usuario como al objetivo.
 */
public class StateAttack extends Attack{

    private State state;
    private String stateName;
    private int effectValue;
    private boolean affectsSelf;
    private boolean isPersistent;

    /**
     * Constructor para crear un ataque de estado.
     *
     * @param idInside ID interno del ataque en la partida.
     * @param infoAttack Información del ataque (nombre, tipo, poder, precisión, etc.).
     * @param infoState Información del estado (nombre, valor, persistencia).
     * @throws POOBkemonException Si hay error al construir el ataque o los datos del estado son inválidos.
     */
    public StateAttack(int idInside, String[] infoAttack, String[] infoState) throws POOBkemonException {
        super(idInside, infoAttack);

        if (infoState == null || infoState.length < 3) {
            if (infoState == null){
                throw new POOBkemonException("Informacion de estado nula");
            }
            throw new POOBkemonException("Información de estado inválida");
        }

        this.stateName = infoState[0];
        String[] info = new StatusRepository().getStatusByName(stateName);
        state = new State(info); // Se construye el estado con la info del repositorio
        this.effectValue = Integer.parseInt(infoState[1]);
        this.affectsSelf = infoAttack.length > 8 && infoAttack[8].equalsIgnoreCase("ally");
        this.isPersistent = Boolean.parseBoolean(infoState[2]);
    }

    /**
     * Devuelve el objeto `State` creado durante la aplicación del efecto.
     * @return El estado asociado al ataque.
     */
    public State getState() {
        return this.state;
    }

    /**
     * Retorna toda la información del ataque, incluyendo nombre de estado, efecto, 
     * si se aplica al propio atacante y si el estado es persistente.
     *
     * @return Arreglo de Strings con toda la información del ataque.
     */
    @Override
    public String[] getInfo() {
        String[] baseInfo = super.getInfo();
        String[] fullInfo = new String[baseInfo.length + 4];

        System.arraycopy(baseInfo, 0, fullInfo, 0, baseInfo.length);

        fullInfo[baseInfo.length]     = stateName;
        fullInfo[baseInfo.length + 1] = String.valueOf(this.effectValue);
        fullInfo[baseInfo.length + 2] = String.valueOf(this.affectsSelf);
        fullInfo[baseInfo.length + 3] = String.valueOf(this.isPersistent);

        return fullInfo;
    }

    /**
     * Aplica el efecto del ataque de estado sobre el objetivo o el atacante,
     * dependiendo de si `affectsSelf` es verdadero.
     *
     * @param attacker Pokémon que realiza el ataque.
     * @param target Pokémon objetivo del ataque.
     * @return Mensaje indicando el resultado del ataque (éxito, inmunidad o fallo).
     * @throws POOBkemonException Si ocurre un error al obtener el estado desde el repositorio.
     */
    @Override
    public String applyEffect(Pokemon attacker, Pokemon target){
        if (!this.probabilityToHits(attacker, target)) {
            return attacker.getName() + " falló el ataque de estado!";
        }

        Pokemon objetivo = this.affectsSelf ? attacker : target;

        if (!state.isImmune(objetivo)) {
            state.applyTo(objetivo);
            return objetivo.getName() + " fue afectado por " + this.name;
        } else {
            return objetivo.getName() + " es inmune a " + this.name;
        }
    }
}
