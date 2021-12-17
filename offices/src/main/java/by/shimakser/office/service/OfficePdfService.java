package by.shimakser.office.service;

import by.shimakser.dto.OfficeRequest;
import by.shimakser.office.model.Office;
import by.shimakser.office.repository.OfficeRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service("officePdfService")
public class OfficePdfService implements OfficeService {

    private final OfficeRepository officeRepository;

    @Autowired
    public OfficePdfService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    @Override
    @Transactional
    public void importToFile(OfficeRequest officeRequest) {
        List<Office> offices = officeRepository.findAll();

        String importFileName = "/offices_import_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_HH_mm_ss")) + ".pdf";
        String path = officeRequest.getPathToFile() + importFileName;

        Document document = new Document();
        try (FileOutputStream out = new FileOutputStream(path)) {
            PdfWriter.getInstance(document, out);
            document.open();

            Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, BaseColor.BLACK);

            Paragraph title = new Paragraph("Offices import | " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss")), font);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            URL imageUrl = new URL("https://i.redd.it/fsal3ipywty21.png");
            Image image = Image.getInstance(imageUrl);
            image.scaleAbsoluteHeight(200);
            image.scaleAbsoluteWidth(200);
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);
            document.add(Chunk.NEWLINE);

            // Add PDF Table Header
            PdfPTable table = new PdfPTable(OFFICES_FIELDS.length);
            table.setWidthPercentage(100);
            float[] officeColumnWidths = new float[] {3f, 12f, 15f, 7f, 44f, 20f};
            table.setWidths(officeColumnWidths);
            Arrays.stream(OFFICES_FIELDS).forEach(headerTitle -> {
                PdfPCell header = new PdfPCell();
                Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase(headerTitle, headFont));
                table.addCell(header);
            });

            for (Office office : offices) {
                PdfPCell idCell = new PdfPCell(new Phrase(office.getId().
                        toString()));
                idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(idCell);

                PdfPCell titleCell = new PdfPCell(new Phrase(office.getOfficeTitle()));
                titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(titleCell);

                PdfPCell addressCell = new PdfPCell(new Phrase(office.getOfficeAddress()));
                addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(addressCell);

                PdfPCell priceCell = new PdfPCell(new Phrase(office.getOfficePrice().
                        toString()));
                priceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(priceCell);

                PdfPTable contactsTable = new PdfPTable(CONTACTS_FIELDS.length);
                float[] contactColumnWidths = new float[] {2f, 13f, 13f, 13f};
                contactsTable.setWidths(contactColumnWidths);
                office.getOfficeContacts().forEach(contact -> {
                    contactsTable.addCell(new PdfPCell(new Phrase(contact.getId().toString())));
                    contactsTable.addCell(new PdfPCell(new Phrase(contact.getContactPhoneNumber())));
                    contactsTable.addCell(new PdfPCell(new Phrase(contact.getContactEmail())));
                    contactsTable.addCell(new PdfPCell(new Phrase(contact.getContactSite())));
                });
               table.addCell(new PdfPCell(contactsTable));

                table.addCell(new PdfPCell(new Phrase(office.getOfficeDescription())));
            }
            document.add(table);

            document.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
}
