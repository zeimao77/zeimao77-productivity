package top.zeimao77.product.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PageSearch extends BaseSearch {

    private static Logger logger = LogManager.getLogger(PageSearch.class);

    public static final Integer DEFAULT_PAGENO = 1;
    public static final Integer DEFAULT_PAGESIZE = 50;
    public static final Integer NO_PAGESIZE = 99999;

    protected boolean _paging = true;
    protected Integer _limit;
    protected Integer _offset;

    protected Integer pageNo;
    protected Integer pageSize;
    protected Long total;
    private List<String> orderBys;

    public PageSearch(){
        this.pageNo = DEFAULT_PAGENO;
        this.pageSize = DEFAULT_PAGESIZE;
    }

    public PageSearch(Integer pageNo,Integer pageSize){
        setPageNo(pageNo);
        setPageSize(pageSize);
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        if(pageNo > 0 ) {
            this.pageNo = pageNo;
        } else {
            this.pageNo = DEFAULT_PAGENO;
            logger.warn("页码不能为负，已经修改为：{}",this.pageNo);
        }
        if(pageNo != null && pageSize != null) {
            calcpage();
        }
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if(pageSize > 0) {
            this.pageSize = pageSize;
        } else {
            this.pageSize = DEFAULT_PAGESIZE;
            logger.warn("页长不能为负，已经修改为：{}",this.pageSize);
        }
        if(pageNo != null && pageSize != null) {
            calcpage();
        }
    }

    public Long getTotal() {
        return total;
    }

    public Long calcTotalPage() {
        return  total % pageSize > 0 ? (total / pageSize + 1) : total / pageSize;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<String> getOrderBys() {
        return orderBys;
    }

    public void setOrderBys(List<String> orderBys) {
        for (String orderBy : orderBys) {
            safe(orderBy);
        }
        this.orderBys = orderBys;
    }

    public static int[] parsePage(int pageNo, int pageSize) {
        int[] limit = new int[]{0,0};
        limit[0] = (pageNo - 1) * pageSize;
        limit[1] = pageSize;
        return limit;
    }

    /**
     * 计算分页 在设置页码之后手工调用
     */
    public void calcpage() {
        int[] cal = PageSearch.parsePage(pageNo,pageSize);
        _limit = cal[0];
        _offset =cal[1];
    }

    public boolean isPaging() {
        return this._paging;
    }

    public void set_paging(boolean paging) {
        if(!paging) {
            this._paging = false;
            this.pageNo = DEFAULT_PAGENO;
            this.pageSize = NO_PAGESIZE;
        } else {
            this._paging = true;
        }
    }

    public Integer get_limit() {
        return _limit;
    }

    public Integer get_offset() {
        return _offset;
    }
}

