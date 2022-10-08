package top.zeimao77.product.fileio.oexcel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import top.zeimao77.product.converter.AbstractNonReFreshConverter;
import top.zeimao77.product.util.BeanUtil;
import top.zeimao77.product.xml.AbstractXmlBuiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 示例配置:
 * <pre>
 * &lt;table tableName="table001" id="97628f46ba4143058ad73510af8e4fdb"&gt;
 *   &lt;property name="select"&gt;select * from demo&lt;/property&gt;
 *   &lt;columnList&gt;
 *       &lt;column index="0" field="id" title="唯一序号" width="22" format="@" /&gt;
 *       &lt;column index="1" field="name" title="姓名" width="14" format="@" /&gt;
 *       &lt;column index="2" field="type" title="类型" width="8" format="@"&gt;
 *           &lt;converter converterId="ef27135ea48240519da968b2ab89567e" defaultValue="OO"&gt;
 *               &lt;rule key="1" value="Y1"/&gt;
 *               &lt;rule key="2" value="O2"/&gt;
 *           &lt;/converter&gt;
 *       &lt;/column&gt;
 *   &lt;/columnList&gt;
 * &lt;/table&gt;
 * </pre>
 *
 */
public class TableXMLConfigBuilder extends AbstractXmlBuiler<Table> {

    public TableXMLConfigBuilder(Document document) {
        super(document);
    }

    public TableXMLConfigBuilder(String path) {
        super(path);
    }


    @Override
    public Table build() {
        Table table = new Table();
        table.setTableName(parseProperty(document.getDocumentElement(),"tableName"));
        table.setId(parseProperty(document.getDocumentElement(),"id"));
        table.setSelect(parseProperty(document.getDocumentElement(),"select"));
        parseColumnList(table);
        return table;
    }

    public void parseColumnList(Table table) {
        List<Table.Column> columnList;
        Element columnElementList = (Element) document.getElementsByTagName("columnList").item(0);
        NodeList childNodes = columnElementList.getElementsByTagName("column");
        columnList = new ArrayList<>(childNodes.getLength());
        for (int i = 0; i < childNodes.getLength(); i++) {
            Element columnElement = (Element) childNodes.item(i);
            Table.Column column = new Table.Column();
            column.setIndex(Integer.valueOf(parseProperty(columnElement,"index")));
            column.setTitle(parseProperty(columnElement,"title"));
            column.setWidth(Integer.valueOf(parseProperty(columnElement,"width")));
            column.setFormat(parseProperty(columnElement,"format"));
            column.setField(parseProperty(columnElement,"field"));
            NodeList converter = columnElement.getElementsByTagName("converter");
            if(converter.getLength() > 0) {
                Element converterElement = (Element)converter.item(0);
                NodeList ruleList = converterElement.getElementsByTagName("rule");
                HashMap<String, Object> stringStringHashMap = parseMap(ruleList);
                Table.Converter converter1 = new Table.Converter(){

                    AbstractNonReFreshConverter objectAbstractNonReFreshConverter = new AbstractNonReFreshConverter<>(stringStringHashMap) {
                        @Override
                        protected void refresh() {}
                        @Override
                        public Object defaultName(String key) {
                            return parseProperty(converterElement,"defaultValue");
                        }
                    };
                    @Override
                    public Object getPrintValue(Table.Column column, Object line) {
                        Object property = BeanUtil.getProperty(line, column.getField());
                        if(property == null)
                            return null;
                        return objectAbstractNonReFreshConverter.get(property);
                    }
                };
                column.setConverter(converter1);
            }
            columnList.add(column);
        }
        table.setColumnList(columnList);
    }


}
