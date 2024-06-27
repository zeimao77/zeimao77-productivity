package com.zeimao77;
/**
 * 2024年5月 中百小业态分离报表需求修改
 * 相关的报表有:
 * 订单查询                   order.saleorder.listOrder
 * 支付方式销售汇总            omdmain.saleorderpaycount.getList
 * 门店销售日报 ok            order.saleorder.report.salesDaily
 * 支付明细表   ok            order.saleorder.report.searchPayReport
 * 线下收银员长短款日报        pos.payindiffdetail.searchcc
 * 门店对账报表               omdmain.order.report.salesDaily
 */
public class ErpQueryConverter {

    public static final ErpQueryConverter INSTANCE = new ErpQueryConverter();

    private String flag = null;
    // private static LocalDateTime flagTime = LocalDateTime.of(2024, 6, 1, 0, 0, 0);

    public String getNewErpCode(String startTime,String endTime,String oldErpCode) {
        if(flag == null)
            init();
        if(!"006".equals(oldErpCode) || flag.compareTo(startTime) <= 0)
            return oldErpCode;
        if(flag.compareTo(endTime) >= 0)
            return "002";
        throw new RuntimeException("小业态分离期间数据不允许跨时段查询");
    }

    public void init() {

            flag = "2024-06-20";
    }


}
