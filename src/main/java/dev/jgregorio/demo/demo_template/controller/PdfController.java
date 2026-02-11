package dev.jgregorio.demo.demo_template.controller;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import dev.jgregorio.demo.demo_template.service.PdfService;

/**
 * REST controller for PDF generation endpoints.
 * <p>
 * Provides endpoints to generate PDF documents from ODT templates and return
 * them as ZIP archives.
 * </p>
 */
@RestController
@RequestMapping("/pdf")
public class PdfController {

    private static final Logger logger = LoggerFactory.getLogger(PdfController.class);
    private static final String DEFAULT_TEMPLATE_PATH = "/templates/test.odt";
    private static final String ZIP_FILENAME = "generated-pdfs.zip";

    private final PdfService pdfService;

    public PdfController(final PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * Generates a ZIP file containing PDF documents based on the provided data
     * list.
     * <p>
     * Uses streaming to avoid loading the entire ZIP file into memory, making it
     * suitable for generating large numbers of PDFs.
     * </p>
     *
     * @param dataList A list of maps, where each map contains data for one PDF
     *                 document.
     *                 Each map can include a "fileName" key to specify the PDF
     *                 filename.
     * @return ResponseEntity with StreamingResponseBody for efficient streaming of
     *         the ZIP file.
     */
    @PostMapping("/generate")
    public ResponseEntity<StreamingResponseBody> generatePdf(@RequestBody final List<Map<String, Object>> dataList) {
        logger.info("Received request to generate {} PDF documents", dataList.size());

        final StreamingResponseBody stream = outputStream -> {
            try (InputStream templateStream = new ClassPathResource(DEFAULT_TEMPLATE_PATH).getInputStream()) {
                pdfService.generatePdf(templateStream, dataList, outputStream);
                logger.info("Successfully streamed ZIP file with {} documents", dataList.size());
            } catch (final Exception e) {
                logger.error("Error generating PDF ZIP file", e);
                throw e;
            }
        };

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", ZIP_FILENAME);

        return ResponseEntity.ok()
                .headers(headers)
                .body(stream);
    }


    @GetMapping("/test")
    public ResponseEntity<Void> test() {
     return ResponseEntity.ok().build();
    }
}
