package domain;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LogTest {

    @Test
    public void testRecordWritesToLogFile() throws IOException {
        String fileName = "POOBkemon.log";
        File logFile = new File(fileName);

        // Borra log anterior
        if (logFile.exists()) {
            logFile.delete();
        }

        Exception testException = new Exception("Error de prueba");
        Log.record(testException);

        assertTrue(logFile.exists(), "El archivo de log debe haberse creado");
    }
}
