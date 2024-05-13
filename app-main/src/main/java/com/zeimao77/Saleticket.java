package com.zeimao77;

import java.math.BigDecimal;

public class Saleticket {

    private String stimktno;   // 商场编号
    private String stiposno;  // 款机号
    private Integer stirowno;  // 行号
    private Integer stiinvno;  // 小票流水号
    private String stioutinvno;  // 外部流水号(石基订单号）
    private String stigrantreason;  // 券类型
    private String stibatchno;  // 券编号
    private String stigdid;  // 管理码
    private String stibarcode;  // 条码
    private BigDecimal stisaleqnt;  // 数量
    private BigDecimal stiswapamt;  // 成交金额
    private BigDecimal stipopdisamt;  // 分摊金额
    private String stitrans;  // 是否已上传
    private String stidate;  // 销售日期
    private BigDecimal stisaleprice;  // 单价
    private String stisupplierno; // 操作员
    private String sticustomer; // 客商

    public String getStimktno() {
        return stimktno;
    }

    public void setStimktno(String stimktno) {
        this.stimktno = stimktno;
    }

    public String getStiposno() {
        return stiposno;
    }

    public void setStiposno(String stiposno) {
        this.stiposno = stiposno;
    }

    public Integer getStirowno() {
        return stirowno;
    }

    public void setStirowno(Integer stirowno) {
        this.stirowno = stirowno;
    }

    public Integer getStiinvno() {
        return stiinvno;
    }

    public void setStiinvno(Integer stiinvno) {
        this.stiinvno = stiinvno;
    }

    public String getStioutinvno() {
        return stioutinvno;
    }

    public void setStioutinvno(String stioutinvno) {
        this.stioutinvno = stioutinvno;
    }

    public String getStigrantreason() {
        return stigrantreason;
    }

    public void setStigrantreason(String stigrantreason) {
        this.stigrantreason = stigrantreason;
    }

    public String getStibatchno() {
        return stibatchno;
    }

    public void setStibatchno(String stibatchno) {
        this.stibatchno = stibatchno;
    }

    public String getStigdid() {
        return stigdid;
    }

    public void setStigdid(String stigdid) {
        this.stigdid = stigdid;
    }

    public String getStibarcode() {
        return stibarcode;
    }

    public void setStibarcode(String stibarcode) {
        this.stibarcode = stibarcode;
    }

    public BigDecimal getStisaleqnt() {
        return stisaleqnt;
    }

    public void setStisaleqnt(BigDecimal stisaleqnt) {
        this.stisaleqnt = stisaleqnt;
    }

    public BigDecimal getStiswapamt() {
        return stiswapamt;
    }

    public void setStiswapamt(BigDecimal stiswapamt) {
        this.stiswapamt = stiswapamt;
    }

    public BigDecimal getStipopdisamt() {
        return stipopdisamt;
    }

    public void setStipopdisamt(BigDecimal stipopdisamt) {
        this.stipopdisamt = stipopdisamt;
    }

    public String getStitrans() {
        return stitrans;
    }

    public void setStitrans(String stitrans) {
        this.stitrans = stitrans;
    }

    public String getStidate() {
        return stidate;
    }

    public void setStidate(String stidate) {
        this.stidate = stidate;
    }

    public BigDecimal getStisaleprice() {
        return stisaleprice;
    }

    public void setStisaleprice(BigDecimal stisaleprice) {
        this.stisaleprice = stisaleprice;
    }

    public String getStisupplierno() {
        return stisupplierno;
    }

    public void setStisupplierno(String stisupplierno) {
        this.stisupplierno = stisupplierno;
    }

    public String getSticustomer() {
        return sticustomer;
    }

    public void setSticustomer(String sticustomer) {
        this.sticustomer = sticustomer;
    }
}
