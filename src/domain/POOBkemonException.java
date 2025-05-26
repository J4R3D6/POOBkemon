package domain;

/**
 * Representa las excepciones personalizadas del juego POOBkemon.
 * Esta clase define todos los posibles errores que pueden ocurrir durante el desarrollo
 * del juego, como problemas con datos incompletos, IDs no encontrados, errores de formato, etc.
 */
public class POOBkemonException extends Exception {
    /** Error cuando hay datos incompletos al iniciar el juego. */
    public static final String INCOMPLETE_DATA = "Información incompleta.";

    /** Error por formato inválido en los datos proporcionados. */
    public static final String INVALID_FORMAT = "Formato inválido en los datos.";

    /** Faltan datos del entrenador. */
    public static final String MISSING_TRAINER_DATA = "Datos del entrenador incompletos.";

    /** Faltan datos de los Pokémon. */
    public static final String MISSING_POKEMON_DATA = "Datos del Pokémon faltantes.";

    /** Faltan datos de los ítems. */
    public static final String MISSING_ITEMS_DATA = "Datos de ítems faltantes.";

    /** Información insuficiente para crear un Pokémon. */
    public static final String LESS_INFORMACION_POKEMON = "No se encuentra la información completa del pokemon.";

    /** La mochila es nula. */
    public static final String NULL_BAGPACK = "La mochila (BagPack) no puede ser nula.";

    /** No se puede cambiar a un Pokémon debilitado. */
    public static final String POKEMON_WEAK_CHANGE = "No se puede cambiar a un pokémon debilitado";

    /** No se encontró un Pokémon con el ID proporcionado. */
    public static final String POKEMON_ID_NOT_FOUND = "No se encontró un pokémon con ID: ";

    /** No se encontró un Pokémon activo. */
    public static final String POKEMON_ACTIVE_NO_FOUND = "No se encontró Pokémon activo";

    /** Error al tomar una decisión automática con la máquina. */
    public static final String MACHINE_ERROR = "No hay ataques disponibles";

    /** Tipo de ítem desconocido. */
    public static final String ITEM_ERROR = "Tipo de ítem desconocido: ";

    /** El entrenador no fue encontrado. */
    public static final String TRAINER_NOT_FOUND = "El entrenador no fue encontrado: ";

    /** El equipo no fue encontrado. */
    public static final String TEAM_NOT_FOUND = "El equipo no fue encontrado: ";

    /** Acción no reconocida. */
    public static final String ACTION_NOT_FOUND = "Acción no reconocida: ";

    /** Ataque no reconocido. */
    public static final String ATTACK_NOT_FOUND = "Ataque no reconocida: ";

    /** El Pokémon no está activo. */
    public static final String POKEMON_INACTIVE = "El pokeon no esta activo";

    /** No hay equipos cargados en el juego. */
    public static final String TEAMS_NULL = "No hay equipos";

    /** No se ha determinado un ganador. */
    public static final String WITHOUT_WINER = "No hay equipos";

    /** El entrenador no es una instancia de máquina. */
    public static final String TRAINER_IS_NOT_MACHINE = "El entrenador no es controlado por la máquina";

    /** Error al crear un estado (status). */
    public static final String STATE_ERROR = "Error al crear estado (enum) ";

    /** Error al crear un ataque. */
    public static final String ATTACK_ERROR = "Error al crear ataque: ";

    /**
     * Constructor de la excepción personalizada.
     * @param mensaje Mensaje descriptivo del error.
     */
    public POOBkemonException(String mensaje) {
        super(mensaje);
    }
}
