package by.shimakser.office.service;

import by.shimakser.office.model.FileType;
import by.shimakser.office.model.Office;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Service("officePdfService")
public class OfficePdfService extends BaseOfficeService {

    private final String[] OFFICES_FIELDS = new String[]{"id", "title", "address", "price", "contacts", "description"};
    private final String[] CONTACTS_FIELDS = new String[]{"id", "phone", "email", "site"};

    private final float[] OFFICE_COLUMN_WIDTH = new float[]{5f, 12f, 15f, 7f, 44f, 20f};
    private final float[] CONTACTS_COLUMN_WIDTH = new float[]{5f, 13f, 13f, 13f};

    @Override
    public byte[] exportToFile() throws IOException {

        Document document = new Document();
        File file = null;

        try {
            file = Files.createTempFile(null, null).toFile();
            FileOutputStream out = new FileOutputStream(file);
            PdfWriter.getInstance(document, out);
            document.open();

            Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, BaseColor.BLACK);

            // Add title
            Paragraph title = new Paragraph(FILE_TITLE, font);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            insertImage(document);

            // Add Table Header
            PdfPTable table = new PdfPTable(OFFICES_FIELDS.length);
            table.setWidthPercentage(100);
            table.setWidths(OFFICE_COLUMN_WIDTH);

            insertColumnsHeader(table);

            // Write data in table
            insertDateInTable(table);

            document.add(table);
            document.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

        return Files.readAllBytes(file.toPath());
    }

    @Override
    public FileType name() {
        return FileType.PDF;
    }

    private void insertColumnsHeader(PdfPTable table) {
        Arrays.stream(OFFICES_FIELDS).forEach(headerTitle -> {
            PdfPCell header = new PdfPCell();
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);

            if (headerTitle.equals(OFFICES_FIELDS[4])) {
                header.addElement(insertContactsColumnsHeader(headerTitle));
            } else {
                header.setPhrase(new Phrase(headerTitle, headFont));
            }

            table.addCell(header);
        });
    }

    private void insertDateInTable(PdfPTable table) {
        List<Office> offices = getAll();

        for (Office office : offices) {
            PdfPCell idCell = new PdfPCell(new Phrase(office.getId().toString()));
            idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(idCell);

            PdfPCell titleCell = new PdfPCell(new Phrase(office.getOfficeTitle()));
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(titleCell);

            PdfPCell addressCell = new PdfPCell(new Phrase(office.getOfficeAddress()));
            addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(addressCell);

            PdfPCell priceCell = new PdfPCell(new Phrase(office.getOfficePrice().toString()));
            priceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(priceCell);

            table.addCell(new PdfPCell(insertContactsDataToTable(office)));

            table.addCell(new PdfPCell(new Phrase(office.getOfficeDescription())));
        }
    }

    private PdfPTable insertContactsColumnsHeader(String columnTitle) {
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        PdfPTable contactsCellTable = new PdfPTable(1);
        PdfPCell contactsCellTitle = new PdfPCell(new Phrase(columnTitle, headFont));
        contactsCellTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
        contactsCellTable.setWidthPercentage(100);
        contactsCellTable.addCell(contactsCellTitle);

        PdfPTable contactsColumns = new PdfPTable(4);
        contactsColumns.setWidthPercentage(100);

        try {
            contactsColumns.setWidths(CONTACTS_COLUMN_WIDTH);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Arrays.stream(CONTACTS_FIELDS).forEach(contactsField -> {
            PdfPCell cell = new PdfPCell(new Phrase(contactsField, headFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            contactsColumns.addCell(cell);
        });
        contactsCellTable.addCell(contactsColumns);

        return contactsCellTable;
    }

    private PdfPTable insertContactsDataToTable(Office office) {
        PdfPTable contactsTable = new PdfPTable(CONTACTS_FIELDS.length);

        try {
            contactsTable.setWidths(CONTACTS_COLUMN_WIDTH);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        office.getOfficeContacts().forEach(contact -> {
            contactsTable.addCell(new PdfPCell(new Phrase(contact.getId().toString())));
            contactsTable.addCell(new PdfPCell(new Phrase(contact.getContactPhoneNumber())));
            contactsTable.addCell(new PdfPCell(new Phrase(contact.getContactEmail())));
            contactsTable.addCell(new PdfPCell(new Phrase(contact.getContactSite())));
        });

        return contactsTable;
    }

    private void insertImage(Document document) {
        try {
            URL imageUrl = new URL(URL_TO_IMAGE);
            Image image = Image.getInstance(imageUrl);
            image.scaleAbsoluteHeight(200);
            image.scaleAbsoluteWidth(200);
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);
            document.add(Chunk.NEWLINE);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
}
