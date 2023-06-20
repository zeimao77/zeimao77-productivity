package top.zeimao77.product.fileio.oexcel;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DefaultExcelZipStreamingView extends AbstractView {

    private XlsxDocumentBuilder xlsxDocumentBuilder;
    private String fileName;

    private static final String CONTENTTYPE = "application/zip;charset=UTF-8";

    public DefaultExcelZipStreamingView(String fileName,XlsxDocumentBuilder xlsxDocumentBuilder) {
        this.fileName = fileName;
        this.xlsxDocumentBuilder = xlsxDocumentBuilder;
    }

    public DefaultExcelZipStreamingView(String fileName,XlsxDocumentBuilder... xlsxDocumentBuilders) {
        this.fileName = fileName;
        this.xlsxDocumentBuilder = new DelegatingXlsxDocumentBuilder(xlsxDocumentBuilders);
    }

    public DefaultExcelZipStreamingView(Table table, List<?> dataList,int rowNo) {
        this.fileName = table.getTableName();
        this.xlsxDocumentBuilder = new ExcelXlsxDocumentBuilder(table, dataList,rowNo);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        setContentType(CONTENTTYPE);
        response.setHeader("content-disposition","attachment;filename=" + URLEncoder.encode(this.fileName+".zip","UTF-8"));
        Workbook workbook = this.xlsxDocumentBuilder.build();
        renderZip(workbook,response);
        workbook.close();
    }

    protected void renderZip(Workbook workbook,HttpServletResponse response) throws IOException {
        try(ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
            ZipEntry zipEntry = new ZipEntry("data.xlsx");
            zipOutputStream.putNextEntry(zipEntry);
            workbook.write(zipOutputStream);
        }
    }

    public ModelAndView mav() {
        return new ModelAndView(this);
    }

}
