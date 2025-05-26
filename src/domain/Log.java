package domain;    

import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.Level;

/**
 * Clase utilitaria para registrar errores del juego en un archivo de log.
 */
public class Log {
    public static String nombre = "POOBkemon"; // Nombre base del archivo de log

    /**
     * Registra una excepción en el archivo de log.
     * @param e Excepción a registrar
     */
    public static void record(Exception e) {
        try {
            Logger logger = Logger.getLogger(nombre);
            logger.setUseParentHandlers(false);
            FileHandler file = new FileHandler(nombre + ".log", true);
            file.setFormatter(new SimpleFormatter());
            logger.addHandler(file);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Mensaje de debug");
            }
            file.close();
        } catch (Exception oe) {
            oe.printStackTrace();
        }
    }
}
