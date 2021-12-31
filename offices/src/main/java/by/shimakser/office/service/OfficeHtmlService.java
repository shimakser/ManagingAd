package by.shimakser.office.service;

import by.shimakser.office.model.FileType;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@Service
public class OfficeHtmlService extends BaseOfficeService {

    private static final String PATH_TO_HTML = "offices/src/main/resources/templates/office.html";

    @Override
    public byte[] exportToFile(FileType fileType) throws IOException {
        File inputHtml = new File(PATH_TO_HTML);

        File file = null;
        try {
            file = Files.createTempFile(null, null).toFile();

            OutputStream os = new FileOutputStream(file);
            Document document = Jsoup.parse(inputHtml, "UTF-8");
            document.outputSettings()
                    .syntax(Document.OutputSettings.Syntax.xml);

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.toStream(os);
            builder.withW3cDocument(new W3CDom().fromJsoup(document), "/");
            builder.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Files.readAllBytes(file.toPath());
    }

    @Override
    public FileType getType() {
        return FileType.HTML;
    }
}
