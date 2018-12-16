package sample.PDF;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import sample.Model.Customer;
import sample.Model.Order;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class PdfMonthlyExporting {
    public  String FILE ;
    public static File fontFile = new File("E:/DragonDentalApp/PDF/Resource/open-sans/vuArial.ttf");
    BaseFont bf = BaseFont.createFont(fontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    Font vnFontNormal = new Font(bf,12);
    Font vnFontNormalSmall = new Font(bf,11);
    public static File fontFileBold = new File("E:/DragonDentalApp/PDF/Resource/open-sans/vuArialBold.ttf");
    BaseFont bf2 = BaseFont.createFont(fontFileBold.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    Font vnFontBold = new Font(bf2,12);
    NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
    LocalDate now =LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public PdfMonthlyExporting(String file) throws IOException, DocumentException {
        this.FILE =file;
    }

    public void createPdf(ArrayList<Order> orders, ArrayList<Float> totalPrices, Float debt, Customer customer,String month){
        Document document = new Document(PageSize.A4);
        try {

            PdfWriter.getInstance(document, new FileOutputStream(FILE+"/"+now.getMonth().toString()+" "+dateConverter(now.format(formatter))+".pdf"));
            document.open();
            addTitlePage(document,month);
            addMetaData(document,orders,totalPrices,debt, customer);

            //addContent(document);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addMetaData(Document document, ArrayList<Order> orders, ArrayList<Float> totalPrices, Float debt, Customer customer) throws IOException, DocumentException {
        Paragraph customerInfo = new Paragraph();
        customerInfo.add(new Paragraph("Tên khách: "+ customer.getName(),vnFontNormal));
        customerInfo.add(new Paragraph("Địa chỉ:      "+ customer.getAddress(),vnFontNormal));
        customerInfo.add(new Paragraph("ĐT:            "+ customer.getPhone(),vnFontNormal));
        customerInfo.add(new Paragraph("Ngày:         "+ now.format(formatter),vnFontNormal));
        addEmptyLine(customerInfo,1);
        document.add(customerInfo);
        float[] columnWidths = {1,3,4};
        PdfPTable table = new PdfPTable(columnWidths);
        PdfPCell cell;
        table.addCell(new Paragraph("ID",vnFontBold));
        table.addCell(new Paragraph("Ngày Giao",vnFontBold));
        table.addCell(new Paragraph("Giá Trị (VND)",vnFontBold));
        for(int i=0; i < orders.size();i++){
            table.addCell(new Paragraph(String.valueOf(orders.get(i).getId()),vnFontNormalSmall));
            table.addCell(new Paragraph(orders.get(i).getCreateAt(),vnFontNormalSmall));
            table.addCell(new Paragraph(currency.format(totalPrices.get(i)),vnFontNormalSmall));
        }
        cell = new PdfPCell(new Phrase("Tổng",vnFontBold));
        cell.setColspan(2);
        table.addCell(cell);
        table.addCell(new Paragraph(currency.format(getTotalPrices(totalPrices)),vnFontNormalSmall));
        cell = new PdfPCell(new Phrase("Nợ Cũ",vnFontBold));
        cell.setColspan(2);
        table.addCell(cell);
        float pay =0;
        if(debt > 0 ){
            debt = debt - getTotalPrices(totalPrices);
            pay = getTotalPrices(totalPrices)+ debt;
        }else{
            pay = getTotalPrices(totalPrices);
            debt =Float.valueOf(0);
        }
        table.addCell(new Paragraph(currency.format(debt),vnFontNormalSmall));
        cell = new PdfPCell(new Phrase("Tổng Cộng",vnFontBold));
        cell.setColspan(2);
        table.addCell(cell);
        table.addCell(new Paragraph(currency.format(pay),vnFontNormalSmall));
        document.add(table);
    }

    private void addTitlePage(Document document, String month) throws IOException, DocumentException {
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
                "\t \tTỔNG KẾT ĐƠN HÀNG THÁNG "+month , vnFontBold));
        addEmptyLine(title,1);

        title.setIndentationLeft(150);
        document.add(preface);
        document.add(title);
        document.add(logo);
    }

    private String dateConverter(String oldDate)
    {
        return oldDate.replace('/','-');
    }
    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
    private float getTotalPrices(ArrayList<Float> totalPrices){
        float total = 0;
        for(Float price: totalPrices){
            total = total + price;
        }
        return total;
    }
}