package top.zeimao77.product.fileio;

import java.util.List;

public class JavaCodeFile extends CodeFile{

    protected String _packageName;
    protected String _className;

    public JavaCodeFile(String path, String fileName) {
        super(path, fileName);
    }

    public JavaCodeFile(String path, String fileName, List<String> lines) {
        super(path, fileName, lines);
    }

    @Override
    public void set_fileName(String _fileName) {
        super.set_fileName(_fileName);
        putKeys("_fileName",_className);
        if(this._className != null) {
            set_className(_fileName.replace(".java",""));
        }
    }

    /**
     * @return 包名
     */
    public String get_packageName() {
        return _packageName;
    }

    public void set_packageName(String _packageName) {
        this._packageName = _packageName;
        putKeys("_packageName",_packageName);
        if(this._fileName != null) {
            set_fileName(_packageName.concat(".java"));
        }
    }

    /**
     * @return 类名
     */
    public String get_className() {
        return _className;
    }

    public void set_className(String _className) {
        this._className = _className;
    }
}
