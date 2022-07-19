package top.zeimao77.product.fileio.oexcel;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DefaultExcelZipStreamingView extends AbstractView {

    protected List<?> dataList;
    protected Table table;
    private static final String CONTENTTYPE = "application/zip;charset=UTF-8";

    public DefaultExcelZipStreamingView(Table table, List<?> dataList) {
        setContentType(CONTENTTYPE);
        this.table = table;
        this.dataList = dataList;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("content-disposition","attachment;filename="+table.getTableName()+".zip");
        ExcelXlsxDocumentBuilder builder = new ExcelXlsxDocumentBuilder(table,dataList);
        Workbook workbook = builder.build();
        renderZip(workbook,response);
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
