/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author senar
 */
public class Ingredient 
{
    private String itemID;
    private String name;
    private double unitPrice;
    private int availableQuantity;
    
    public Ingredient(String itemID,String name,double unitPrice,int availableQuantity)
    {
       this.itemID=itemID;
       this.name=name;
       this.unitPrice=unitPrice;
       this.availableQuantity=availableQuantity;
    }
    
    public void updateStock(int usedqty)
    {
       this.availableQuantity-=usedqty;
    }
    
    public void getDetails()
    {
        System.out.println(name +" - price:"+unitPrice);
    }
}
