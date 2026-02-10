package dev.jgregorio.demo.demo_template.service;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PdfServiceTest {

    @Test
    void testGeneratePdf() {
        // Arrange
        PdfService pdfService = new PdfService();
        InputStream templateStream = getClass().getResourceAsStream("/templates/test.odt");
        assertNotNull(templateStream, "El archivo de plantilla test.odt no se encuentra en el classpath");

        Map<String, Object> data = new HashMap<>();
        data.put("message", "Juan Pérez");
        data.put("date", "10 de Febrero de 2026");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act
        pdfService.generatePdf(templateStream, data, outputStream);

        // Assert
        assertTrue(outputStream.size() > 0, "El PDF generado no debería estar vacío");
    }
}
