package sample.PDF;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import sample.Model.Customer;
import sample.Model.Order;
import sample.Model.OrderLine;

import java.awt.*;
import java.io.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PdfExporting {
    public  String FILE ;//"e:/FirstPdf.pdf";
    public static File fontFile = new File("E:/DragonDentalApp/PDF/Resource/open-sans/vuArial.ttf");
    BaseFont bf = BaseFont.createFont(fontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    Font vnFontNormal = new Font(bf,12);
    Font vnFontNormalSmall = new Font(bf,11);
    public static File fontFileBold = new File("E:/DragonDentalApp/PDF/Resource/open-sans/vuArialBold.ttf");
    BaseFont bf2 = BaseFont.createFont(fontFileBold.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    Font vnFontBold = new Font(bf2,12);
    NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);


    public PdfExporting(String file) throws IOException, DocumentException {
        this.FILE = file;
    }

    public void createPdf(Order order, float totalPrice, int promoteProduct, List<OrderLine> orderLines, Customer customer) throws IOException {

        Document document = new Document(PageSize.A4);
        try {
            LocalDate now =LocalDate.now();
            PdfWriter.getInstance(document, new FileOutputStream(FILE+"/"+order.getId()+" "+dateConverter(order.getCreateAt())+".pdf"));
            document.open();
            addTitlePage(document);
            addMetaData(document,order,totalPrice,promoteProduct,orderLines, customer);

            //addContent(document);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void addTitlePage(Document document) throws DocumentException, IOException {
        Paragraph preface = new Paragraph();
        Paragraph title = new Paragraph();

        String logourl = "E:/DragonDentalApp/PDF/logo.jpg";
        Image logo = Image.getInstance(logourl);
        logo.scaleAbsolute(90,80);
        logo.setAbsolutePosition(450,730);
        // We add one empty line
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph(
                "CÔNG TY TNHH DRAGON DENTAL TRADING " , vnFontBold));
        preface.add(new Paragraph(
                "Vp: 50/2 Bành Văn Trân, P7, Q.Tân Bình", vnFontBold));
        preface.add(new Paragraph(
                "ĐT: 0919 889 019 - 0366004446" , vnFontBold));
        addEmptyLine(preface, 1);

        title.add(new Paragraph(
                "ĐƠN ĐẶT HÀNG" , vnFontBold));
        addEmptyLine(title,1);

        title.setIndentationLeft(200);
        document.add(preface);
        document.add(title);
        document.add(logo);
    }

    private void addMetaData(Document document,Order order, float totalPrice, int promoteProduct, List<OrderLine> orderLines, Customer customer) throws DocumentException, UnsupportedEncodingException {
        Paragraph customerInfo = new Paragraph();
        customerInfo.add(new Paragraph("Tên khách: "+ order.getCustomer(),vnFontNormal));
        customerInfo.add(new Paragraph("Địa chỉ:      "+ customer.getAddress(),vnFontNormal));
        customerInfo.add(new Paragraph("ĐT:            "+ customer.getPhone(),vnFontNormal));
        customerInfo.add(new Paragraph("Ngày:         "+ order.getCreateAt(),vnFontNormal));
        addEmptyLine(customerInfo,1);
        document.add(customerInfo);
        float[] columnWidths = {4,2, 3,2,4};
        PdfPTable table = new PdfPTable(columnWidths);
        PdfPCell cell;
        table.addCell(new Paragraph("Sản Phẩm",vnFontBold));
        table.addCell(new Paragraph("Số Lượng",vnFontBold));
        table.addCell(new Paragraph("Đơn Giá (VND)",vnFontBold));
        table.addCell(new Paragraph("Khuyến Mãi %",vnFontBold));
        table.addCell(new Paragraph("Tổng",vnFontBold));
        table.getDefaultCell().setUseAscender(true);
        if(orderLines != null){
            if(orderLines.size() > 0){
                for (OrderLine orderLine: orderLines){
                    table.addCell(new Paragraph(orderLine.getProduct(),vnFontNormalSmall));
                    table.addCell(new Paragraph(String.valueOf(orderLine.getQuantity()),vnFontNormalSmall));
                    table.addCell(new Paragraph(currency.format((int)orderLine.getPrice()),vnFontNormalSmall));
                    table.addCell(new Paragraph(String.valueOf(orderLine.getDiscount()),vnFontNormalSmall));
                    table.addCell(new Paragraph(currency.format((int)orderLine.getTotalPrice()),vnFontNormalSmall));
                }

            }
            cell = new PdfPCell(new Phrase("Số lượng sản phẩm khuyến mãi",vnFontBold));
            cell.setColspan(4);
            table.addCell(cell);
            table.addCell(new Paragraph(String.valueOf(promoteProduct),vnFontNormalSmall));

            cell = new PdfPCell(new Phrase("Tổng giá tiền",vnFontBold));
            cell.setColspan(4);
            table.addCell(cell);
            table.addCell(new Paragraph(currency.format((int)totalPrice),vnFontNormalSmall));
        }
        document.add(table);

    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
    private String dateConverter(String oldDate)
    {
        return oldDate.replace('/','-');
    }
}
