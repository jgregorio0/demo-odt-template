package dev.jgregorio.demo.demo_template.controller;

import dev.jgregorio.demo.demo_template.service.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    // @GetMapping
    // public ResponseEntity<byte[]> generatePdf() {
    // //byte[] pdfBytes = pdfService.generatePdf(data, outputStream);

    // HttpHeaders headers = new HttpHeaders();
    // headers.setContentType(MediaType.APPLICATION_PDF);
    // headers.setContentDispositionFormData("attachment", "generated.pdf");
    // headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

    // return ResponseEntity.ok()
    // .headers(headers)
    // .body(pdfBytes);
    // }
}
