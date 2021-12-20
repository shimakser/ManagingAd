package by.shimakser.office.service;

import by.shimakser.dto.OfficeRequest;
import by.shimakser.office.model.Office;
import by.shimakser.office.repository.OfficeRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class OfficeHtmlService implements OfficeService {

    private final OfficeRepository officeRepository;

    private static final String PATH_TO_HTML = "offices/src/main/resources/templates/office.html";

    @Autowired
    public OfficeHtmlService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    public List<Office> getOffices() {
        return officeRepository.findAll();
    }

    @Override
    public void importToFile(OfficeRequest officeRequest) {
        String importFileName = "/offices_import_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_HH_mm_ss")) + ".pdf";
        String outputPdf = officeRequest.getPathToFile() + importFileName;
        File inputHtml = new File(PATH_TO_HTML);

        try (OutputStream os = new FileOutputStream(outputPdf)) {
            Document document = Jsoup.parse(inputHtml, "UTF-8");
            document.outputSettings()
                    .syntax(Document.OutputSettings.Syntax.xml);

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withUri(outputPdf);
            builder.toStream(os);
            builder.withW3cDocument(new W3CDom().fromJsoup(document), "/");
            builder.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
