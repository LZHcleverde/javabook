package com.augmentum.domain;

import com.augmentum.Utils.PriceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {
    private String oid;
    private Double ototal;
    private Integer oamount;
    private Integer ostatus;
    private Integer opaytype;
    private Integer uid;
    private String orealname;
    private String ophone;
    private String oaddress;
    private String odatetime;
    private Map<Integer, com.augmentum.domain.OrderItem> itemMap = new HashMap<Integer, com.augmentum.domain.OrderItem>();//（key,书籍id:value,item)
    private List<com.augmentum.domain.OrderItem> itemList = new ArrayList<com.augmentum.domain.OrderItem>();
    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Double getOtotal() {
        return ototal;
    }

    public void setOtotal(Double ototal) {
        this.ototal = ototal;
    }

    public String getOdatetime() {
        return odatetime;
    }

    public void setOdatetime(String odatetime) {
        this.odatetime = odatetime;
    }
    public Integer getOamount() {
        return oamount;
    }

    public void setOamount(Integer oamount) {
        this.oamount = oamount;
    }

    public Integer getOstatus() {
        return ostatus;
    }

    public void setOstatus(Integer ostatus) {
        this.ostatus = ostatus;
    }

    public Integer getOpaytype() {
        return opaytype;
    }

    public void setOpaytype(Integer opaytype) {
        this.opaytype = opaytype;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getOrealname() {
        return orealname;
    }

    public void setOrealname(String orealname) {
        this.orealname = orealname;
    }

    public String getOphone() {
        return ophone;
    }

    public void setOphone(String ophone) {
        this.ophone = ophone;
    }

    public String getOaddress() {
        return oaddress;
    }

    public void setOaddress(String oaddress) {
        this.oaddress = oaddress;
    }

    public List<com.augmentum.domain.OrderItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<com.augmentum.domain.OrderItem> itemList) {
        this.itemList = itemList;
    }

    public Map<Integer, com.augmentum.domain.OrderItem> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<Integer, com.augmentum.domain.OrderItem> itemMap) {
        this.itemMap = itemMap;
    }

    public void addGoods(Book book) {
        //是否包含此书
        if(itemMap.containsKey(book.getBid())) {
            com.augmentum.domain.OrderItem item = itemMap.get(book.getBid());
            item.setOiamount(item.getOiamount()+1);
        }else {
            com.augmentum.domain.OrderItem item = new com.augmentum.domain.OrderItem();
            item.setOiprice(book.getBprice());
            item.setOiamount(1);//第一次一本书
            item.setBook(book);
            item.setOrder(this);
            item.setBid(book.getBid());
            itemMap.put(book.getBid(), item);
        }
        oamount++;

        ototal = PriceUtil.add(ototal,book.getBprice());
    }

    public void lessen(int bid) {
        if(itemMap.containsKey(bid)) {
            com.augmentum.domain.OrderItem item = itemMap.get(bid);
            item.setOiamount(item.getOiamount()-1);
            oamount--;
            ototal = PriceUtil.subtract(ototal,item.getOiprice());
            if(item.getOiamount()<=0) {
                itemMap.remove(bid);
            }
        }
    }

    public void delete(int bid)
    {
        if(itemMap.containsKey(bid)) {
            com.augmentum.domain.OrderItem item = itemMap.get(bid);
            ototal = PriceUtil.subtract(ototal,item.getOiamount()*item.getOiprice());
            oamount-=item.getOiamount();
            itemMap.remove(bid);
        }
    }

    @Override
    public String toString() {
        return "Order{" +
                "oid='" + oid + '\'' +
                ", ototal=" + ototal +
                ", oamount=" + oamount +
                ", ostatus=" + ostatus +
                ", opaytype=" + opaytype +
                ", uid=" + uid +
                ", orealname='" + orealname + '\'' +
                ", ophone='" + ophone + '\'' +
                ", oaddress='" + oaddress + '\'' +
                ", odatetime='" + odatetime + '\'' +
                ", itemMap=" + itemMap +
                ", itemList=" + itemList +
                '}';
    }
}
