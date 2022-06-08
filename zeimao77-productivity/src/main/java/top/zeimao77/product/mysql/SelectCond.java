package top.zeimao77.product.mysql;

import top.zeimao77.product.model.ImmutablePair;

import java.util.ArrayList;
import java.util.List;

public class SelectCond extends PageSearch {

    protected List<SelectCondNode> searchCondNodeList = new ArrayList<>();
    ArrayList<ImmutablePair<String,String>> queryColumnList = new ArrayList();

    public SelectCond() {}

    public SelectCond(Integer pageNo, Integer pageSize) {
        super(pageNo, pageSize);
    }

    public static SelectCond select() {
        return new SelectCond();
    }

    public static SelectCond select(Integer pageNo, Integer pageSize) {
        return new SelectCond(pageNo,pageSize);
    }

    /**
     * 设置查询列
     * @param colomn 列名
     * @param alias 别名
     * @return this
     */
    public SelectCond putQueryColumnAsAlias(String colomn,String alias) {
        safe(colomn);
        safe(alias);
        queryColumnList.add(new ImmutablePair<>(colomn,alias));
        return this;
    }

    protected void setQueryColumn(String queryColumn) {
        if("*".equals(queryColumn)) {
            this.queryColumn = "*";
        }
        String[] split = queryColumn.split(",| ");
        for (String s : split) {
            safe(s);
        }
        this.queryColumn = queryColumn;
    }

    public SelectCond putQueryColumn(String... columns) {
        for (String column : columns) {
            safe(column);
            queryColumnList.add(new ImmutablePair<>(column,null));
        }
        return this;
    }

    public SelectCond from(String tableName) {
        if(tableName != null) {
            setTableName(tableName);
        }
        if(!queryColumnList.isEmpty()) {
            StringBuilder sBuiler = new StringBuilder();
            for (ImmutablePair<String, String> pair : queryColumnList) {
                if(sBuiler.length() > 0 && pair.getRight() == null) {
                    sBuiler.append(String.format(",%s",pair.getLeft()));
                } else if(sBuiler.length() > 0 && pair.getRight() != null) {
                    sBuiler.append(String.format(",%s AS %s",pair.getLeft(),pair.getRight()));
                } else if(sBuiler.length() == 0 && pair.getRight() == null){
                    sBuiler.append(String.format("%s",pair.getLeft()));
                } else {
                    sBuiler.append(String.format("%s AS %s",pair.getLeft(),pair.getRight()));
                }
            }
            setQueryColumn(sBuiler.toString());
        }
        return this;
    }

    public SelectCond noPage(){
        set_paging(false);
        return this;
    }

    public SelectCond orderBy(String... orderBy) {
        for (String order : orderBy) {
            safe(order);
        }
        super.setOrderBy(orderBy);
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

    public static class SelectCondNode {

        public static final String BIND_AND = SQL.BIND_AND;
        public static final String BIND_OR = SQL.BIND_OR;
        public static final String COND_QIN = SQL.COND_QIN;
        public static final String COND_QNIN = SQL.COND_QNIN;
        public static final String COND_QIS = SQL.COND_QIS;
        public static final String COND_QNE = SQL.COND_QNE;
        public static final String COND_QLIKE = SQL.COND_QLIKE;
        public static final String COND_QLLIKE = SQL.COND_QLLIKE;  // 左边添加百分号
        public static final String COND_QRLIKE = SQL.COND_QRLIKE;  // 右边添加百分号
        public static final String COND_QGT = SQL.COND_QGT;
        public static final String COND_QGTE = SQL.COND_QGTE;
        public static final String COND_QLT = SQL.COND_QLT;
        public static final String COND_QLTE = SQL.COND_QLTE;
        public static final String COND_QREGEXP = SQL.COND_QREGEXP;

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
