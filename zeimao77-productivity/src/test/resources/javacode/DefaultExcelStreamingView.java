package top.zeimao77.product.fileio.oexcel;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


public class DefaultExcelStreamingView extends AbstractXlsxStreamingView {

    private XlsxDocumentBuilder xlsxDocumentBuilder;
    private String fileName;

    /**
     * 高扩展构建
     * @param fileName
     * @param xlsxDocumentBuilder
     */
    public DefaultExcelStreamingView(String fileName,XlsxDocumentBuilder xlsxDocumentBuilder) {
        this.fileName = fileName;
        this.xlsxDocumentBuilder = xlsxDocumentBuilder;
    }

    /**
     * 多sheet页的excel导出
     * @param fileName
     * @param xlsxDocumentBuilders
     */
    public DefaultExcelStreamingView(String fileName,ExcelXlsxDocumentBuilder... xlsxDocumentBuilders) {
        this.fileName = fileName;
        this.xlsxDocumentBuilder = new DelegatingXlsxDocumentBuilder(xlsxDocumentBuilders);
    }

    /**
     * 单sheet页的excel导出
     * @param table
     * @param dataList
     * @param rowNo
     */
    public DefaultExcelStreamingView(Table table, List<?> dataList, int rowNo) {
        this.fileName = table.getTableName();
        this.xlsxDocumentBuilder = new ExcelXlsxDocumentBuilder(table, dataList,rowNo);
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.xlsxDocumentBuilder.build(workbook);
        response.setHeader("Content-disposition","attachment;filename=" + URLEncoder.encode(fileName+".xlsx","UTF-8"));
    }

    public ModelAndView mav() {
        return new ModelAndView(this);
    }

}
