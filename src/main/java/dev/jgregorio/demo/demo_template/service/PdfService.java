package dev.jgregorio.demo.demo_template.service;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateException;
import net.sf.jooreports.templates.DocumentTemplateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.ConnectException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class PdfService {

    private static final Logger logger = LoggerFactory.getLogger(PdfService.class);
    //String host = "34.90.78.128"
    String openOfficeHost = "127.0.0.1";
    Integer openOfficePort = 8100;

    public void generateZip(String templatePath, List<Map<String, Object>> dataList, OutputStream os) throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(os)) {
            for (int i = 0; i < dataList.size(); i++) {
                System.out.println(String.format("Processing data %s/%s", i+1, dataList.size()));
                Map<String, Object> data = dataList.get(i);
                final String entryName = "document_" + i + ".pdf";
                final ZipEntry zipEntry = new ZipEntry(entryName);
                zipOutputStream.putNextEntry(zipEntry);
                NonClosingOutputStream nonClosingOutputStream =
                        new NonClosingOutputStream(zipOutputStream);
                generatePdf(templatePath, data, nonClosingOutputStream);
                zipOutputStream.closeEntry();
            }
        }
    }

    public void generatePdf(String templatePath, Map<String, Object> dataModel, OutputStream os) throws IOException {
        File tempFile = File.createTempFile("ODT_TO_PDF", ".odt");
        try (FileInputStream in = new FileInputStream(templatePath)) {
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                createDocument(in, dataModel, fos);
            }
        }
        try (InputStream inputStream = new FileInputStream(tempFile)) {
            convert(inputStream, os);
        }
        tempFile.delete();
    }

    private void convert(InputStream inputStream, OutputStream outputStream) throws ConnectException {
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(openOfficeHost, openOfficePort);
        try {
            connection.connect();
            StreamOpenOfficeDocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
            DefaultDocumentFormatRegistry defaultDocumentFormatRegistry = new DefaultDocumentFormatRegistry();
            DocumentFormat odtFormat = defaultDocumentFormatRegistry.getFormatByFileExtension("odt");
            DocumentFormat pdfFormat = defaultDocumentFormatRegistry.getFormatByFileExtension("pdf");
            converter.convert(inputStream, odtFormat, outputStream, pdfFormat);

        } finally {
            connection.disconnect();
        }
    }

    private void createDocument(InputStream templateIs, Map<String, Object> dataModel, OutputStream temporalOs) throws IOException {
        try {
            DocumentTemplateFactory templateFactory = new DocumentTemplateFactory();
            DocumentTemplate template = templateFactory.getTemplate(templateIs);
            template.createDocument(dataModel, temporalOs);
        } catch (DocumentTemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
