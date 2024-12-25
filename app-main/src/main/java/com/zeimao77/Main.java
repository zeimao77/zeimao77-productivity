package com.zeimao77;
import top.zeimao77.product.factory.BeanFactory;
import top.zeimao77.product.factory.ComponentFactory;
import top.zeimao77.product.main.BaseMain;
import top.zeimao77.product.util.JsonBeanUtil;
import top.zeimao77.product.util.LongIdGenerator;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


public class Main extends BaseMain {

    private static String BODY = """
            {"body":{"order":{"asap":true,"bag_fee":{"currency_code":"HKD","fractional":0},"brand_id":"aeon-hk","cash_due":{"currency_code":"HKD","fractional":0},"customer":{"contact_access_code":"242666143","contact_number":"85230189333","first_name":"HK","loyalty":{"card_number":""},"order_frequency_at_site":"NEW CUSTOMER"},"cutlery_notes":"NO CUTLERY / 不需要餐具","display_id":"8102","fee_breakdown":[],"fulfillment_type":"deliveroo","id":"hk:401e92a9-b75e-464f-9c83-182b873eecf1","is_tabletless":false,"items":[{"discount_amount":{"currency_code":"HKD","fractional":0},"item_fees":[],"menu_unit_price":{"currency_code":"HKD","fractional":1000},"modifiers":[],"name":"菲律賓>單隻超甜香蕉","operational_name":"菲律賓單隻超甜香蕉","pos_item_id":"000945402-plu","quantity":1,"total_price":{"currency_code":"HKD","fractional":1000},"unit_price":{"currency_code":"HKD","fractional":1000}}],"location_id":"1000","meal_cards":[],"offer_discount":{"currency_code":"HKD","fractional":0},"order_notes":"","order_number":"10884168102","partner_order_subtotal":{"currency_code":"HKD","fractional":1000},"partner_order_total":{"currency_code":"HKD","fractional":1000},"prepare_for":"2024-12-10T09:22:04Z","start_preparing_at":"2024-12-10T09:18:04Z","status":"placed","status_log":[{"at":"2024-12-10T09:14:35Z","status":"placed"}],"subtotal":{"currency_code":"HKD","fractional":1000},"total_price":{"currency_code":"HKD","fractional":1000}}},"event":"order.new"}
            """;

    public static final String MYSQLBEAN = "mysql_top_zeimao77";

    public static void main(String[] args) throws UnsupportedEncodingException, ParseException {

        ComponentFactory.initSimpleSqlClient(MYSQLBEAN, BeanFactory.DEFAULT);

        DeliverooBody bean = JsonBeanUtil.DEFAULT.toBean(BODY, DeliverooBody.class);

        Order order = new Order();
        DeliverooStoreConverter.ShopInfo shopInfo = DeliverooStoreConverter.INSTANCE.getShopInfo(bean.getBody().getOrder().getLocation_id());

        Date nowDate = new Date();
        SaleOrdersModel saleOrdersModel = new SaleOrdersModel();
        order.setSaleOrdersModel(saleOrdersModel);
        saleOrdersModel.setOid(LongIdGenerator.INSTANCE.generate());
        saleOrdersModel.setSheetNo(saleOrdersModel.getOid().toString());
        saleOrdersModel.setReDeliveryNum(null);
        saleOrdersModel.setPackageNum(null);
        saleOrdersModel.setEntId(0L);
        saleOrdersModel.setParentSheetNo(saleOrdersModel.getSheetNo());
        saleOrdersModel.setChannel(DeliverooConst.CHANNEL);
        saleOrdersModel.setChannelSheetNo(bean.getBody().getOrder().getId());
        saleOrdersModel.setBusCompany(null);
        saleOrdersModel.setUid(null);
        saleOrdersModel.setUserName(null);
        saleOrdersModel.setUserRemark(null);
        saleOrdersModel.setUserMobile(null);
        saleOrdersModel.setCid(null);
        saleOrdersModel.setCusClass(null);
        saleOrdersModel.setCusCode(null);
        saleOrdersModel.setTerminalNo(DeliverooConst.TERMINALNO);
        saleOrdersModel.setTerminalSno("2412120000002");
        saleOrdersModel.setTerminalOperator(DeliverooConst.TERMINALOPERATOR);
        saleOrdersModel.setSaleMarketId(shopInfo.getShopId());
        saleOrdersModel.setSaleMarketCode(shopInfo.getShopCode());
        saleOrdersModel.setSaleMarket(shopInfo.getShopName());
        saleOrdersModel.setLogisticsStoreId(0L);
        saleOrdersModel.setLogisticsStoreCode(null);
        saleOrdersModel.setLogisticsStore(null);
        saleOrdersModel.setPopDiscountValue(null);
        saleOrdersModel.setAdjustDiscountValue(BigDecimal.ZERO);
        saleOrdersModel.setCustomDiscountValue(BigDecimal.ZERO);
        saleOrdersModel.setMealDiscountValue(BigDecimal.ZERO);
        saleOrdersModel.setPayDiscountValue(BigDecimal.ZERO);
        saleOrdersModel.setTotalDiscountValue(BigDecimal.ZERO);
        saleOrdersModel.setTotalCouponValue(BigDecimal.ZERO);
        saleOrdersModel.setOverageValue(BigDecimal.ZERO);
        saleOrdersModel.setRoundUpOverageValue(BigDecimal.ZERO);
        saleOrdersModel.setChangeValue(BigDecimal.ZERO);
        saleOrdersModel.setOrderType("1");
        saleOrdersModel.setLogisticsMode(7);
        saleOrdersModel.setDeliveryMode(0);
        saleOrdersModel.setCodPay(false);
        saleOrdersModel.setWeight(0D);
        saleOrdersModel.setLogisticsFreight(BigDecimal.ZERO);
        saleOrdersModel.setFreightFact(BigDecimal.ZERO);
        saleOrdersModel.setOughtPay(bean.getBody().getOrder().getTotal_price().getAmount());
        saleOrdersModel.setFactPay(saleOrdersModel.getOughtPay());
        saleOrdersModel.setPayState(2);
        saleOrdersModel.setOrderState(7);
        saleOrdersModel.setLogisticsState(10);
        saleOrdersModel.setStockout(false);
        saleOrdersModel.setUploadErp(false);
        saleOrdersModel.setColdStorage(false);
        saleOrdersModel.setManulAudit(null);
        saleOrdersModel.setDepositSale(false);
        saleOrdersModel.setTailMoneyPay(null);
        saleOrdersModel.setStaffShopping(false);
        saleOrdersModel.setHasBackPrint(false);
        saleOrdersModel.setHasFastPay(false);
        saleOrdersModel.setAutoAuditDenyId(null);
        saleOrdersModel.setAutoAuditDenyReason(saleOrdersModel.getChannelSheetNo());
        saleOrdersModel.setCallerRemark(null);
        saleOrdersModel.setCancelReasonId(null);
        saleOrdersModel.setCancelReason(null);
        saleOrdersModel.setExceptionState(null);
        saleOrdersModel.setBuyerReturnReason(null);
        saleOrdersModel.setBuyerReturnBankcard("0");
        saleOrdersModel.setThsq(null);
        saleOrdersModel.setJfkh(null);
        saleOrdersModel.setGhsq(null);
        saleOrdersModel.setHysq(null);
        saleOrdersModel.setSqkh(null);
        saleOrdersModel.setCalcBillId(null);
        saleOrdersModel.setTrackNo(null);
        saleOrdersModel.setDeliveryKeyword(null);
        saleOrdersModel.setDeliveryStartTime(null);
        saleOrdersModel.setDeliveryEndTime(null);
        saleOrdersModel.setLang("ch");
        saleOrdersModel.setCreator(null);
        saleOrdersModel.setPayDate(bean.getBody().getOrder().getStart_preparing_at_date());
        saleOrdersModel.setSaleDate(saleOrdersModel.getPayDate());
        saleOrdersModel.setLogisticsDate(null);
        saleOrdersModel.setStoreOutDate(null);
        saleOrdersModel.setPrintDate(null);
        saleOrdersModel.setCreateDate(nowDate);
        saleOrdersModel.setReceiveDate(nowDate);
        saleOrdersModel.setLastDate(null);
        saleOrdersModel.setSigningDate(null);
        saleOrdersModel.setStaffNo(null);
        saleOrdersModel.setStaffCardNo(null);
        saleOrdersModel.setStaffCardType(null);
        saleOrdersModel.setOctopusDeviceId(null);
        saleOrdersModel.setOctopusCardno(null);
        saleOrdersModel.setOctopusBalance(BigDecimal.ZERO);
        saleOrdersModel.setOctopusIsSmart(false);
        saleOrdersModel.setOctopusRefNo(null);
        saleOrdersModel.setOctopusTranscationTime(null);
        saleOrdersModel.setBillDate(OrderStockAndProfitSyncUtil.computeBillDate(saleOrdersModel.getSaleDate()));
        saleOrdersModel.setJoinCusType(0);
        saleOrdersModel.setBonusPointExpireDate(null);
        saleOrdersModel.setMembershipExpireDate(null);
        saleOrdersModel.setMemberNameEnglish(null);
        saleOrdersModel.setMemberNameChinese(null);
        saleOrdersModel.setHasDuplFlag(false);
        saleOrdersModel.setStampBalance(BigDecimal.ZERO);
        saleOrdersModel.setBonusPointLastDay(BigDecimal.ZERO);
        saleOrdersModel.setBonusPointLastMonth(BigDecimal.ZERO);
        saleOrdersModel.setBonusPointUsed(BigDecimal.ZERO);
        saleOrdersModel.setBonusPointToBeExpired(BigDecimal.ZERO);
        saleOrdersModel.setBonusPointLastUpdateDate(null);
        saleOrdersModel.setLastUpdateTime(null);
        saleOrdersModel.setMembershipUntilDate(null);
        saleOrdersModel.setElectronicStamp(BigDecimal.ZERO);
        saleOrdersModel.setPhysicalStamp(BigDecimal.ZERO);
        saleOrdersModel.setMemberActionSno(null);
        saleOrdersModel.setOriginalPay(null);
        saleOrdersModel.setUploadErpType(2);

        SaleOrderPayModel saleOrderPayModel = new SaleOrderPayModel();
        saleOrderPayModel.setOctopusLastAddValDate(null);
        saleOrderPayModel.setOctopusLastAddValType(null);
        saleOrderPayModel.setOctopusTranscationTime(null);
        saleOrderPayModel.setOctopusIsSmart(null);
        saleOrderPayModel.setAuthCode(null);
        saleOrderPayModel.setOpId(LongIdGenerator.INSTANCE.generate());
        saleOrderPayModel.setEntId(saleOrdersModel.getEntId());
        saleOrderPayModel.setOid(saleOrdersModel.getOid());
        saleOrderPayModel.setRowNo(1L);
        saleOrderPayModel.setFlag(1);
        saleOrderPayModel.setPayType(DeliverooConst.PAYTYPE);
        saleOrderPayModel.setPayCode(DeliverooConst.PAYCODE);
        saleOrderPayModel.setPayName(DeliverooConst.PAYNAME);
        saleOrderPayModel.setCopType(null);
        saleOrderPayModel.setPayNo(null);
        saleOrderPayModel.setRowNoId(null);
        saleOrderPayModel.setPayMemo(null);
        saleOrderPayModel.setRate(1D);
        saleOrderPayModel.setAmount(saleOrdersModel.getOughtPay());
        saleOrderPayModel.setMoney(saleOrdersModel.getOughtPay());
        saleOrderPayModel.setOverage(BigDecimal.ZERO);
        saleOrderPayModel.setDxtype(null);
        saleOrderPayModel.setConsumersId(null);
        saleOrderPayModel.setCouponGroup(null);
        saleOrderPayModel.setCouponEventScd(null);
        saleOrderPayModel.setCouponEventId(null);
        saleOrderPayModel.setCouponPolicyId(null);
        saleOrderPayModel.setCouponTraceSeqno(null);
        saleOrderPayModel.setCouponBalance(null);
        saleOrderPayModel.setTrace(null);
        saleOrderPayModel.setRefCode(null);
        saleOrderPayModel.setTerminalId(null);
        saleOrderPayModel.setMerchantId(null);
        saleOrderPayModel.setBatchNo(null);
        saleOrderPayModel.setCouponIsClass(null);
        saleOrderPayModel.setCashCost(null);
        saleOrderPayModel.setLang(saleOrdersModel.getLang());
        saleOrderPayModel.setCreateDate(saleOrdersModel.getCreateDate());
        saleOrderPayModel.setRoundUpOverageValue(null);
        saleOrderPayModel.setCusName(null);
        saleOrderPayModel.setExpiryDate(null);
        saleOrderPayModel.setInstallmentTerms(null);
        saleOrderPayModel.setFirstInstallmentAmount(null);
        saleOrderPayModel.setFirstPaymentDueDate(null);
        saleOrderPayModel.setFinalPyamentDueDate(null);
        saleOrderPayModel.setPosEntryMode(null);
        saleOrderPayModel.setDeliveryMemoNumber(null);
        saleOrderPayModel.setTrackData(null);
        saleOrderPayModel.setDescription(null);
        saleOrderPayModel.setMonthlyInstallment(null);
        order.getSaleOrderPayModelList().add(saleOrderPayModel);



        List<DeliverooBody.Item> items = bean.getBody().getOrder().getItems();
        int rowNo = 0;

        SaleGoodsConverter saleGoodsConverter = new SaleGoodsConverter();
        for (DeliverooBody.Item item : items) {
            saleGoodsConverter.addGoodsCode(item.getGoodsCode());
        }
        for (DeliverooBody.Item item : items) {
            String goodsCode = item.getGoodsCode();
            SaleGoods saleGoods = saleGoodsConverter.getSaleGoods(goodsCode);

            SaleOrderDetailModel saleOrderDetailModel = new SaleOrderDetailModel();
            saleOrderDetailModel.setLicense(null);
            saleOrderDetailModel.setPrtDuplFlag(null);
            saleOrderDetailModel.setGoodsDesc(null);
            saleOrderDetailModel.setPrcutMode(null);
            saleOrderDetailModel.setControlGood(saleGoods.getControlFlag());
            saleOrderDetailModel.setEatWay(null);
            saleOrderDetailModel.setStallCode(null);
            saleOrderDetailModel.setOdId(LongIdGenerator.INSTANCE.generate());
            saleOrderDetailModel.setEntId(saleOrdersModel.getEntId());
            saleOrderDetailModel.setOid(saleOrdersModel.getOid());
            saleOrderDetailModel.setSheetNo(saleOrdersModel.getSheetNo());
            saleOrderDetailModel.setParentSheetNo(saleOrdersModel.getParentSheetNo());
            saleOrderDetailModel.setAssistantId(null);
            saleOrderDetailModel.setContractCode(null);
            saleOrderDetailModel.setCabinetGroup(null);
            saleOrderDetailModel.setWorkMode(null);
            saleOrderDetailModel.setSkuName(saleGoods.getGoodsName());
            saleOrderDetailModel.setEngName(saleGoods.getEnSname());
            saleOrderDetailModel.setRowNo(++rowNo);
            saleOrderDetailModel.setSpuCode(saleGoods.getParentGoodsCode());
            saleOrderDetailModel.setItemCode(goodsCode);
            saleOrderDetailModel.setBarCode(saleGoods.getBarNo());
            saleOrderDetailModel.setBrandCode(saleGoods.getBrandCode());
            saleOrderDetailModel.setCatCode(saleGoods.getCategoryCode());
            saleOrderDetailModel.setStyleCode("0");
            saleOrderDetailModel.setSmGoodsCode(null);
            saleOrderDetailModel.setSmGoodsSno(null);
            saleOrderDetailModel.setSmGoodProperty(null);
            saleOrderDetailModel.setKlm(null);
            saleOrderDetailModel.setGoodsId(null);
            saleOrderDetailModel.setGoodsFlag(null);
            saleOrderDetailModel.setGoodType(null);
            saleOrderDetailModel.setPopTag(null);
            saleOrderDetailModel.setGoodsProperty(null);
            saleOrderDetailModel.setListPrice(item.getUnit_price().getAmount());
            saleOrderDetailModel.setSalePrice(saleOrderDetailModel.getListPrice());
            saleOrderDetailModel.setFactor(null);
            saleOrderDetailModel.setQty(item.getQuantity().doubleValue());
            saleOrderDetailModel.setCopies(saleOrderDetailModel.getQty().intValue());
            saleOrderDetailModel.setUnitCode(saleGoods.getSaleUnit());
            saleOrderDetailModel.setSaleValue(saleOrderDetailModel.getSalePrice().multiply(BigDecimal.valueOf(saleOrderDetailModel.getQty())));
            saleOrderDetailModel.setCouponValue(BigDecimal.ZERO);
            saleOrderDetailModel.setPopDiscountValue(BigDecimal.ZERO);
            saleOrderDetailModel.setAdjustDiscountValue(BigDecimal.ZERO);
            saleOrderDetailModel.setCustomDiscountValue(BigDecimal.ZERO);
            saleOrderDetailModel.setPayDiscountValue(BigDecimal.ZERO);
            saleOrderDetailModel.setMealDiscountValue(BigDecimal.ZERO);
            saleOrderDetailModel.setTotalDiscountValue(BigDecimal.ZERO);
            saleOrderDetailModel.setSaleAmount(item.getTotal_price().getAmount());
            saleOrderDetailModel.setInvoiceMoney(null);
            saleOrderDetailModel.setVerderName(null);
            saleOrderDetailModel.setVenderCode(null);
            saleOrderDetailModel.setWeight(0D);
            saleOrderDetailModel.setLogisticsFreight(BigDecimal.ZERO);
            saleOrderDetailModel.setStockout(false);
            saleOrderDetailModel.setStockoutCopies(0);
            saleOrderDetailModel.setAllowReturnCopies(saleOrderDetailModel.getQty().intValue());
            saleOrderDetailModel.setWeighGood(saleGoods.getEscaleFlag());
            saleOrderDetailModel.setColdGood(saleGoods.getColdTransFlag());
            saleOrderDetailModel.setFishGoods(false);
            saleOrderDetailModel.setTreasureFlag(null);
            saleOrderDetailModel.setRemark(null);
            saleOrderDetailModel.setDeliveryDate(null);
            saleOrderDetailModel.setDeliveryTime(null);
            saleOrderDetailModel.setLang(saleOrdersModel.getLang());
            saleOrderDetailModel.setCreator(saleOrdersModel.getCreator());
            saleOrderDetailModel.setCreateDate(saleOrdersModel.getCreateDate());
            saleOrderDetailModel.setReceiveDate(saleOrdersModel.getReceiveDate());
            saleOrderDetailModel.setLastDate(null);
            saleOrderDetailModel.setProcessFlag(null);
            order.getSaleOrderDetailModels().add(saleOrderDetailModel);

        }

        OrderWriter writer = new OrderWriter();
        writer.writeOrder(order);
    }


}
