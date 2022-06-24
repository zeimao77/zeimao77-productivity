package top.zeimao77.product.fileio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.zeimao77.product.exception.BaseServiceRunException;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码文件
 */
public class CodeFile {

    protected List<String> _lines;
    protected String _fileName;
    protected String _path;
    protected Map<String,String> keys = new HashMap<>();

    public CodeFile(String path,String fileName) {
        this._path = path;
        this._fileName = fileName;
    }

    public CodeFile(String path,String fileName,List<String> lines) {
        this._path = path;
        this._fileName = fileName;
        this._lines = lines;
    }

    public void putKeys(String key,String value) {
        keys.put(key,value);
    }

    public List<String> get_lines() {
        return _lines;
    }

    public void set_lines(List<String> _lines) {
        this._lines = _lines;
    }

    public String get_fileName() {
        return _fileName;
    }

    public void set_fileName(String _fileName) {
        this._fileName = _fileName;
        this.keys.put("_fileName",_fileName);
    }

    public String get_path() {
        return _path;
    }

    /**
     * 如果参数"@STDOUT",将会将输出结果定义到标准输出流
     * @param _path 文件路径
     */
    public void set_path(String _path) {
        this._path = _path;
        this.keys.put("_path",_path);
    }

    /**
     * 按模板并创生成文件
     * @param delete 如果文件存在是否删除
     */
    public void create(boolean delete) {
        PrintStream printStream = null;
        if(_path.equalsIgnoreCase("@stdout")) {
            printStream = System.out;
        } else {
            String newFilePath = String.format("%s/%s", _path, _fileName);
            File file = new File(newFilePath);
            if (!delete && file.exists()) {
                return;
            } else if(delete && file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
                printStream = new PrintStream(file);
            } catch (IOException e) {
                throw new BaseServiceRunException("IO异常",e);
            }
        }
        for (String line : _lines) {
            String newLine = line;
            for (Map.Entry<String, String> entry : keys.entrySet()) {
                String key = entry.getKey();
                String matchKey = String.format("<#%s#>", key);
                newLine = newLine.replaceAll(matchKey, entry.getValue());
            }
            System.out.println(newLine);
            printStream.println(newLine);
        }
        printStream.close();
    }
}
