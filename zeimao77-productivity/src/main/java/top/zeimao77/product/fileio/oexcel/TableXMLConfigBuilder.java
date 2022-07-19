package top.zeimao77.product.fileio.oexcel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import top.zeimao77.product.xml.AbstractXmlBuiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 示例配置
 * <table tableName="table001" id="97628f46ba4143058ad73510af8e4fdb">
 *   <property name="select">select * from demo</property>
 *   <columnList>
 *       <column index="0" field="id" title="唯一序号" width="22" format="@" />
 *       <column index="1" field="name" title="姓名" width="14" format="@" />
 *       <column index="2" field="type" title="类型" width="8" format="@">
 *           <converter converterId="ef27135ea48240519da968b2ab89567e" defaultValue="OO">
 *               <rule key="1" value="Y1"/>
 *               <rule key="2" value="O2"/>
 *           </converter>
 *       </column>
 *   </columnList>
 * </table>
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
                Table.Converter c = new Table.Converter();
                c.setConverterId(parseProperty(converterElement,"converterId"));
                c.setDefaultValue(parseProperty(converterElement,"defaultValue"));
                NodeList ruleList = converterElement.getElementsByTagName("rule");
                HashMap<String, Object> stringStringHashMap = parseMap(ruleList);
                c.setRule(stringStringHashMap);
                column.setConverter(c);
            }
            columnList.add(column);
        }
        table.setColumnList(columnList);
    }


}
