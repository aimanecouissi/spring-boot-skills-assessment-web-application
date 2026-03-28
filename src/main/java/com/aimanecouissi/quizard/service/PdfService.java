package com.aimanecouissi.quizard.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Service
public class PdfService {
    private static final String PDF_RESOURCES = "/pdf-resources/";
    private final ResultService resultService;
    private final SpringTemplateEngine templateEngine;

    public PdfService(ResultService resultService, SpringTemplateEngine templateEngine) {
        this.resultService = resultService;
        this.templateEngine = templateEngine;
    }

    public File generatePdf(Long resultId) throws Exception {
        Context context = getContext(resultId);
        String html = loadAndFillTemplate(context);
        return renderPdf(html);
    }

    private File renderPdf(String html) throws Exception {
        File file = File.createTempFile("certificate", ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
        renderer.setDocumentFromString(html, new ClassPathResource(PDF_RESOURCES).getURL().toExternalForm());
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        file.deleteOnExit();
        return file;
    }

    private Context getContext(Long resultId) {
        Context context = new Context();
        context.setVariable("result", resultService.findById(resultId));
        return context;
    }

    private String loadAndFillTemplate(Context context) {
        return templateEngine.process("user/certificate", context);
    }
}
