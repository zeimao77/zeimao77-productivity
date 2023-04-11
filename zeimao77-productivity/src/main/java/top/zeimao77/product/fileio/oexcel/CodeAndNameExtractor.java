package top.zeimao77.product.fileio.oexcel;

import top.zeimao77.product.util.BeanUtil;

public class CodeAndNameExtractor implements Table.Converter {

    private String codeField;
    private String nameField;

    public CodeAndNameExtractor(String codeField, String nameField) {
        this.codeField = codeField;
        this.nameField = nameField;
    }

    public String getCodeField() {
        return codeField;
    }

    public void setCodeField(String codeField) {
        this.codeField = codeField;
    }

    public String getNameField() {
        return nameField;
    }

    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    @Override
    public Object getPrintValue(Table.Column column, Object line) {
        Object c = BeanUtil.getProperty(line, codeField);
        Object n = BeanUtil.getProperty(line, nameField);
        return String.format("[%s]%s",c == null ? "" : c.toString(),n == null ? "" : n.toString());
    }
}
