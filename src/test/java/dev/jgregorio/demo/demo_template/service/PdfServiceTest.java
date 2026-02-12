package dev.jgregorio.demo.demo_template.service;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PdfServiceTest {

    @Test
    void testGenerateSingleReport() throws IOException, URISyntaxException {
        // Arrange
        PdfService pdfService = new PdfService();
        URI templateUri = getClass().getResource("/templates/test.odt").toURI();
        File template = new File(templateUri);
        assertTrue(template.exists(), "El archivo de plantilla no existe");
        System.out.println(template.getPath());

        Map<String, Object> data1 = new HashMap<>();
        data1.put("fileName", "JG_Company_Centro_1.pdf");
        data1.put("fecha", "11/02/2026");
        data1.put("horaIni", "13:30");
        data1.put("horaFin", "14:00");
        data1.put("nombreEmpresa", "JG Company");
        data1.put("nombreCentro", "Centro 1");
        data1.put("spa", "Company S.A.");
        data1.put("nombreCurso", "Desarrollo con IA");
        data1.put("emptyActa", 1);
        data1.put("nombreFormador", "Pepito Pérez");

        // Act
        try (FileOutputStream fos = new FileOutputStream("test.pdf")) {
            pdfService.generatePdf(template.getPath(), data1, fos);
        }
        File file = new File("test.pdf");

        // Assert
        assertTrue(file.exists(), "El archivo PDF debería existir");
    }



    @Test
    void testGenerateMultipleReports() throws IOException, URISyntaxException {
        // Arrange
        PdfService pdfService = new PdfService();
        URI templateUri = getClass().getResource("/templates/test.odt").toURI();
        File template = new File(templateUri);
        assertTrue(template.exists(), "El archivo de plantilla no existe");
        System.out.println(template.getPath());

        Map<String, Object> data1 = new HashMap<>();
        data1.put("fileName", "JG_Company_Centro_1.pdf");
        data1.put("fecha", "11/02/2026");
        data1.put("horaIni", "13:30");
        data1.put("horaFin", "14:00");
        data1.put("nombreEmpresa", "JG Company");
        data1.put("nombreCentro", "Centro 1");
        data1.put("spa", "Company S.A.");
        data1.put("nombreCurso", "Desarrollo con IA");
        data1.put("emptyActa", 1);
        data1.put("nombreFormador", "Pepito Pérez");

        Map<String, Object> data2 = new HashMap<>();
        data2.put("fileName", "JG_Company_Centro_2.pdf");
        data2.put("fecha", "01/01/2026");
        data2.put("horaIni", "10:00");
        data2.put("horaFin", "11:00");
        data2.put("nombreEmpresa", "JG Company");
        data2.put("nombreCentro", "Centro 2");
        data2.put("spa", "Company S.A.");
        data2.put("nombreCurso", "Desarrollo con IA");
        data2.put("emptyActa", 1);
        data2.put("nombreFormador", "Pepito Pérez");

        List<Map<String, Object>> dataList = List.of(data1, data2);
        // Act
        try (FileOutputStream fos = new FileOutputStream("test.zip")) {
            pdfService.generateZip(template.getPath(), dataList, fos);
        }
        File file = new File("test.zip");

        // Assert
        assertTrue(file.exists(), "El archivo PDF debería existir");
    }


//    @Test
//    void testGenerateMultipleReports() throws IOException {
//        // Arrange
//        PdfService pdfService = new PdfService();
//        InputStream templateStream = getClass().getResourceAsStream("/templates/test.odt");
//        assertNotNull(templateStream, "El archivo de plantilla test.odt no se encuentra en el classpath");
//
//        Map<String, Object> data1 = new HashMap<>();
//        data1.put("fileName", "JG_Company_Centro_1.pdf");
//        data1.put("fecha", "11/02/2026");
//        data1.put("horaIni", "13:30");
//        data1.put("horaFin", "14:00");
//        data1.put("nombreEmpresa", "JG Company");
//        data1.put("nombreCentro", "Centro 1");
//        data1.put("spa", "Company S.A.");
//        data1.put("nombreCurso", "Desarrollo con IA");
//        data1.put("emptyActa", 1);
//        data1.put("nombreFormador", "Pepito Pérez");
//
//        Map<String, Object> data2 = new HashMap<>();
//        data2.put("fileName", "JG_Company_Centro_2.pdf");
//        data2.put("fecha", "01/01/2026");
//        data2.put("horaIni", "10:00");
//        data2.put("horaFin", "11:00");
//        data2.put("nombreEmpresa", "JG Company");
//        data2.put("nombreCentro", "Centro 2");
//        data2.put("spa", "Company S.A.");
//        data2.put("nombreCurso", "Desarrollo con IA");
//        data2.put("emptyActa", 1);
//        data2.put("nombreFormador", "Pepito Pérez");
//
//        List<Map<String, Object>> dataList = Arrays.asList(data1, data2);
//
//        FileOutputStream fo = new FileOutputStream("test.zip");
//
//        // Act
//        pdfService.generatePdf(templateStream, dataList, fo);
//        fo.close();
//
//        // Assert
//        File zipFile = new File("test.zip");
//        assertTrue(zipFile.exists(), "El archivo ZIP generado no debería estar vacío");
//
//        // Verificar el contenido del ZIP
//        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
//            ZipEntry entry = zis.getNextEntry();
//            assertNotNull(entry, "El ZIP debería contener al menos una entrada");
//            assertEquals("JG_Company_Centro_1.pdf", entry.getName());
//
//            entry = zis.getNextEntry();
//            assertNotNull(entry, "El ZIP debería contener una segunda entrada");
//            assertEquals("JG_Company_Centro_2.pdf", entry.getName());
//
//            assertNull(zis.getNextEntry(), "No deberían haber más entradas en el ZIP");
//        }
//    }
}
