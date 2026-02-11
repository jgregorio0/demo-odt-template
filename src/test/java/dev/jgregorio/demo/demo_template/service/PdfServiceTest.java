package dev.jgregorio.demo.demo_template.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.jupiter.api.Test;

class PdfServiceTest {

    @Test
    void testGeneratePdf() throws IOException {
        // Arrange
        PdfService pdfService = new PdfService();
        InputStream templateStream = getClass().getResourceAsStream("/templates/test.odt");
        assertNotNull(templateStream, "El archivo de plantilla test.odt no se encuentra en el classpath");

        Map<String, Object> data1 = new HashMap<>();
        data1.put("fileName", "juan_perez");
        data1.put("message", "Juan Pérez");
        data1.put("date", "10 de Febrero de 2026");

        Map<String, Object> data2 = new HashMap<>();
        data2.put("fileName", "maria_lopez");
        data2.put("message", "María López");
        data2.put("date", "11 de Febrero de 2026");

        List<Map<String, Object>> dataList = Arrays.asList(data1, data2);

        FileOutputStream fo = new FileOutputStream("test.zip");

        // Act
        pdfService.generatePdf(templateStream, dataList, fo);
        fo.close();

        // Assert
        File zipFile = new File("test.zip");
        assertTrue(zipFile.exists(), "El archivo ZIP generado no debería estar vacío");

        // Verificar el contenido del ZIP
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry = zis.getNextEntry();
            assertNotNull(entry, "El ZIP debería contener al menos una entrada");
            assertEquals("juan_perez.pdf", entry.getName());

            entry = zis.getNextEntry();
            assertNotNull(entry, "El ZIP debería contener una segunda entrada");
            assertEquals("maria_lopez.pdf", entry.getName());

            assertNull(zis.getNextEntry(), "No deberían haber más entradas en el ZIP");
        }
    }
}
