package dev.jgregorio.demo.demo_template.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

/**
 * Service for generating PDF documents from ODT templates.
 * <p>
 * This service processes a list of data maps, applies them to a provided ODT
 * template,
 * converts the result to PDF, and packages all generated PDFs into a single ZIP
 * archive.
 * </p>
 */
@Service
public class PdfService {

    private static final Logger logger = LoggerFactory.getLogger(PdfService.class);

    private static final String PDF_EXTENSION = ".pdf";
    private static final String DEFAULT_FILE_NAME_PREFIX = "document_";
    private static final String DATA_FILE_NAME_KEY = "fileName";

    private static final Options PDF_OPTIONS = Options.getTo(ConverterTypeTo.PDF);

    /**
     * Generates a ZIP file containing PDF documents based on the provided template
     * and data list.
     *
     * @param templateStream The input stream of the ODT template. Must not be null.
     * @param dataList       A list of maps, where each map contains data for one
     *                       document. Must not be null or empty.
     * @param outputStream   The output stream where the ZIP file will be written.
     *                       Must not be null.
     * @throws RuntimeException If an error occurs during ZIP generation or report
     *                          processing.
     */
    public void generatePdf(final InputStream templateStream, final List<Map<String, Object>> dataList,
            final OutputStream outputStream) {
        validateInputs(templateStream, dataList, outputStream);

        logger.info("Starting PDF generation for {} entries.", dataList.size());

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            final IXDocReport report = loadReport(templateStream);

            int index = 1;
            for (final Map<String, Object> data : dataList) {
                final String fileName = resolveFileName(data, index);
                logger.debug("Processing entry {}/{}: {}", index, dataList.size(), fileName);

                addPdfToZip(zipOutputStream, fileName, report, data);
                index++;
            }

            logger.info("ZIP archive generated successfully with {} documents.", index - 1);

        } catch (final Exception e) {
            logger.error("Error generating PDF ZIP archive", e);
            throw new RuntimeException("Error generating PDF ZIP archive: " + e.getMessage(), e);
        }
    }

    private void validateInputs(final InputStream templateStream, final List<Map<String, Object>> dataList,
            final OutputStream outputStream) {
        if (templateStream == null) {
            throw new IllegalArgumentException("Template stream cannot be null");
        }
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("Data list cannot be null or empty");
        }
        if (outputStream == null) {
            throw new IllegalArgumentException("Output stream cannot be null");
        }
    }

    private IXDocReport loadReport(final InputStream templateStream) throws IOException, XDocReportException {
        // Load the report once and cache the template for this process
        return XDocReportRegistry.getRegistry().loadReport(templateStream, TemplateEngineKind.Velocity);
    }

    private String resolveFileName(final Map<String, Object> data, final int index) {
        String fileName = DEFAULT_FILE_NAME_PREFIX + index;

        if (data != null && data.containsKey(DATA_FILE_NAME_KEY)) {
            final Object fileNameObj = data.get(DATA_FILE_NAME_KEY);
            if (fileNameObj != null) {
                fileName = fileNameObj.toString();
            }
        }

        if (!fileName.toLowerCase().endsWith(PDF_EXTENSION)) {
            fileName += PDF_EXTENSION;
        }

        return fileName;
    }

    private void addPdfToZip(final ZipOutputStream zipOutputStream, final String fileName, final IXDocReport report,
            final Map<String, Object> data) throws IOException, XDocReportException {

        final ZipEntry zipEntry = new ZipEntry(fileName);
        zipOutputStream.putNextEntry(zipEntry);

        final IContext context = createContext(report, data);

        // Use NonClosingOutputStream to prevent XDocReport from closing the
        // ZipOutputStream
        report.convert(context, PDF_OPTIONS, new NonClosingOutputStream(zipOutputStream));
        zipOutputStream.closeEntry();
    }

    private IContext createContext(final IXDocReport report, final Map<String, Object> data)
            throws XDocReportException {
        final IContext context = report.createContext();
        if (data != null && !data.isEmpty()) {
            data.forEach(context::put);
        }
        return context;
    }

    /**
     * A FilterOutputStream that ignores the close() call.
     * Useful when passing a ZipOutputStream to a library that attempts to close the
     * stream.
     */
    private static class NonClosingOutputStream extends java.io.FilterOutputStream {
        public NonClosingOutputStream(final OutputStream out) {
            super(out);
        }

        @Override
        public void close() {
            // Do not close the underlying stream
        }
    }
}
