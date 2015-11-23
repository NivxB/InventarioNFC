package com.redes.proyecto.inventorynfc;

/**
 * Created by Alberto on 22/11/2015.
 */
public class Product {
    private int id;
    private String barcode;
    private String name;
    private double purchasePrice;
    private double sellPrice;
    private int categoryId;
    public Product(int id,String barcode,String name,double purchasePrice, double sellPrice, int categoryId){
        this.id=id;
        this.barcode=barcode;
        this.name=name;
        this.purchasePrice=purchasePrice;
        this.sellPrice=sellPrice;
        this.categoryId=categoryId;
    }
    public void setId(int id){
        this.id=id;
    }
    public void setBarcode(String barcode){
        this.barcode=barcode;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setPurchasePrice(double purchasePrice){
        this.purchasePrice=purchasePrice;
    }
    public void setSellPrice(double sellPrice){
        this.sellPrice=sellPrice;
    }
    public void setCategoryId(int categoryId){
        this.categoryId=categoryId;
    }
    public int getId(){
        return this.id;
    }
    public String getBarcode(){
        return this.barcode;
    }
    public String getName(){
        return this.name;
    }
    public double getPurchasePrice(){
        return this.purchasePrice;
    }
    public double getSellPrice(){
        return this.sellPrice;
    }
    public int getCategoryId(){
        return this.categoryId;
    }
    public String toString(){
        return "Product:{id: "+this.id+", barcode: "+this.barcode+
               ", name: "+this.name+", purchasePrice: "+this.purchasePrice+", sellPrice: "+this.sellPrice+
                ", categoryId: "+this.categoryId+"}";
    }

    public String toJson() {
        return "{'product_barcode':'"+this.barcode+"',"
                + "'product_name':'"+this.name+"',"
                + "'product_category':"+this.categoryId+","
                + "'purchase_price':"+this.purchasePrice+","
                + "'sell_price':"+this.sellPrice+"}";
    }
}
