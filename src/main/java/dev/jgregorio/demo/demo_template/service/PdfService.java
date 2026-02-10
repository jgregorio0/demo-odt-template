package dev.jgregorio.demo.demo_template.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.springframework.stereotype.Service;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

@Service
public class PdfService {

    public void generatePdf(InputStream templateStream, Map<String, Object> data, OutputStream outputStream) {
        try {
            // 1. Cargar el archivo ODT (ahora viene como parámetro)

            // 2. Crear el reporte indicando que usaremos Velocity
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(templateStream,
                    TemplateEngineKind.Velocity);

            // 3. Crear el contexto de datos y asignar valores
            IContext context = report.createContext();
            if (data != null) {
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    context.put(entry.getKey(), entry.getValue());
                }
            }

            // 4. Configurar la salida a PDF
            Options options = Options.getTo(ConverterTypeTo.PDF);

            // 5. Fusionar datos y convertir
            report.convert(context, options, outputStream);

            // No cerramos el outputStream aquí, dejamos que quien llame al método lo
            // gestione
            System.out.println("PDF generado correctamente.");

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }
    }
}
