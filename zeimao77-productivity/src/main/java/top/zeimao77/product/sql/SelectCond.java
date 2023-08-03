package top.zeimao77.product.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zeimao77
 */
public class SelectCond extends PageSearch {

    protected List<SelectCondNode> searchCondNodeList = new ArrayList<>();
    public SelectCond() {}

    public static SelectCond select() {
        return new SelectCond();
    }

    private SelectCond(Integer pageNo, Integer pageSize) {
        super(pageNo, pageSize);
    }

    public static SelectCond select(List<String> queryFields) {
        SelectCond selectCond = new SelectCond();
        selectCond.setQueryFields(queryFields);
        return selectCond;
    }

    public SelectCond from(String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * 不分页
     * @return
     */
    public SelectCond noPage(){
        set_paging(false);
        return this;
    }

    public SelectCond page(Integer pageNo, Integer pageSize) {
        setPageNo(pageNo);
        setPageSize(pageSize);
        return this;
    }

    public SelectCond orderBy(String... orderBy) {
        setOrderBys(Arrays.asList(orderBy));
        return this;
    }

    public SelectCond is(String fieldName, Object content) {
        return where(SelectCondNode.BIND_AND,fieldName, SelectCondNode.COND_QIS,content);
    }

    public SelectCond where(String bind, String fieldName, String condition, Object content) {
        SelectCondNode searchCondNode = new SelectCondNode(this,bind, fieldName, condition, content);
        return where(searchCondNode);
    }

    public SelectCond where(SelectCondNode node) {
        this.searchCondNodeList.add(node);
        return this;
    }

    public List<SelectCondNode> getSearchCondNodeList() {
        return searchCondNodeList;
    }

    public void setSearchCondNodeList(List<SelectCondNode> searchCondNodeList) {
        this.searchCondNodeList = searchCondNodeList;
    }

    /**
     * 条件节点
     */
    public static class SelectCondNode implements IWhere {

        private String bind;
        private String fieldName;
        private String condition;
        private Object content;

        public SelectCondNode() {}

        public SelectCondNode(SelectCond cond,String bind, String fieldName, String condition, Object content) {
            this.bind = bind;
            this.condition = condition;
            this.content = content;
            setFieldName(fieldName,false,cond);
        }

        public String getBind() {
            return bind;
        }

        public void setBind(String bind) {
            this.bind = bind;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName,boolean safe,SelectCond cond) {
            if(!safe) {
                cond.safe(fieldName);
            }
            this.fieldName = fieldName;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }
    }

}