package dev.jgregorio.demo.demo_template.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class PdfServiceTest {

    @Test
    void testGeneratePdf() throws IOException {
        // Arrange
        PdfService pdfService = new PdfService();
        InputStream templateStream = getClass().getResourceAsStream("/templates/test.odt");
        assertNotNull(templateStream, "El archivo de plantilla test.odt no se encuentra en el classpath");

        Map<String, Object> data = new HashMap<>();
        data.put("message", "Juan Pérez");
        data.put("date", "10 de Febrero de 2026");

        FileOutputStream fo = new FileOutputStream("test.pdf");

        // Act
        pdfService.generatePdf(templateStream, data, fo);
        fo.close();
        // Assert
        assertTrue(new File("test.pdf").exists(), "El PDF generado no debería estar vacío");
    }
}
