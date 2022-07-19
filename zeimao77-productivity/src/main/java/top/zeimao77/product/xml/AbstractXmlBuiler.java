package top.zeimao77.product.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import top.zeimao77.product.exception.BaseServiceRunException;
import static top.zeimao77.product.exception.ExceptionCodeDefinition.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

/**
 * XML解析对象的通用抽象
 * @param <T> 解析的目标类型
 */
public abstract class AbstractXmlBuiler<T> {

    /**
     * XML Document文档
     */
    protected Document document;

    /**
     * 通过XML Document文档构造构建器
     * @param document XML Document文档
     */
    public AbstractXmlBuiler(Document document) {
        this.document = document;
    }

    /**
     * 通过输入流构造构建器
     * @param is 输入流
     */
    public AbstractXmlBuiler(InputStream is) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(is);
        } catch (ParserConfigurationException e) {
            throw new BaseServiceRunException(WRONG_SOURCE,"XML解析配置错误",e);
        } catch (IOException e) {
            throw new BaseServiceRunException(IOEXCEPTION,"IO错误",e);
        } catch (SAXException e) {
            throw new BaseServiceRunException("XML文件解析错误",e);
        }
    }

    /**
     * 通过文件构造构建器
     * @param file 文件
     */
    public AbstractXmlBuiler(File file) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(file);
        } catch (ParserConfigurationException e) {
            throw new BaseServiceRunException(WRONG_SOURCE,"XML解析配置错误",e);
        } catch (IOException e) {
            throw new BaseServiceRunException(IOEXCEPTION,"IO错误",e);
        } catch (SAXException e) {
            throw new BaseServiceRunException("XML文件解析错误",e);
        }
    }

    /**
     * 通过路径构造构建器
     * @param path 路径
     */
    public AbstractXmlBuiler(String path) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(path);
        } catch (ParserConfigurationException e) {
            throw new BaseServiceRunException(WRONG_SOURCE,"XML解析配置错误",e);
        } catch (IOException e) {
            throw new BaseServiceRunException(IOEXCEPTION,"IO错误",e);
        } catch (SAXException e) {
            throw new BaseServiceRunException("XML文件解析错误",e);
        }
    }

    /**
     * 将节点列表的 key 与 value 属性解析成MAP
     * @param nodeList 节点列表
     * @return 结果MAP
     */
    public static HashMap<String,Object> parseMap(NodeList nodeList) {
        HashMap<String,Object> resultMap = new HashMap<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element item = (Element) nodeList.item(i);
            String key = item.getAttribute("key");
            String value = item.getAttribute("value");
            resultMap.put(key,value);
        }
        return resultMap;
    }

    /**
     * 解析xml属性
     * @param element 元素节点
     * @param propertyName 属性名
     * @return 值
     */
    public static String parseProperty(Element element, String propertyName) {
        Attr attribute = element.getAttributeNode(propertyName);
        NodeList nodeList = element.getElementsByTagName("property");
        if(attribute != null) {
            return attribute.getValue();
        } else if(nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element propertyNode = (Element) nodeList.item(i);
                String name = propertyNode.getAttribute("name");
                if(name != null && propertyName.equals(name)) {
                    Attr nameAttr = propertyNode.getAttributeNode("value");
                    if(nameAttr != null) {
                        return nameAttr.getValue();
                    } else {
                        return propertyNode.getTextContent().trim();
                    }
                }
            }
        }
        return null;
    }

    public static final Function<Element,String> DEFAULT_FUNCTION_STRING_PARSE = o -> {
        String value = o.getAttribute("value");
        return value;
    };

    public static <T> ArrayList<T> parseArray(NodeList nodeList, Function<Element,T> fun) {
        ArrayList<T> arrayList = new ArrayList<>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element item = (Element) nodeList.item(i);
            T apply = fun.apply(item);
            arrayList.add(apply);
        }
        return arrayList;
    }

    /**
     * @return 构建结果
     */
    public abstract T build();


}
