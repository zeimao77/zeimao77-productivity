package top.zeimao77.product.fileio.oexcel;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class DefaultExcelStreamingView extends AbstractXlsxStreamingView {

    protected List<?> dataList;
    protected Table table;
    protected int rowNo;

    public DefaultExcelStreamingView(Table table, List<?> dataList,int rowNo) {
        this.table = table;
        this.dataList = dataList;
        this.rowNo = rowNo;
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExcelXlsxDocumentBuilder excelXlsxDocumentBuilder = new ExcelXlsxDocumentBuilder(table, dataList,rowNo);
        excelXlsxDocumentBuilder.build(workbook);
        response.setHeader("Content-disposition","attachment;filename="+table.getTableName()+".xlsx");
    }

    public ModelAndView mav() {
        return new ModelAndView(this);
    }

}
