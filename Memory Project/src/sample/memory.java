package sample;


import java.util.Vector;


public class memory  {
     int address;
     double size;
     boolean empty;
     process contained;
     boolean can_be_divided;

    public memory(process contained) {
        this.contained = contained;
    }


    public memory(int address, double size) {
        this.address = address;
        this.size = size;
        empty = true;
        can_be_divided = false;
    }
    public memory(){
        address = 0;size=0;empty = true;
    }


    public boolean isCan_be_divided() {
        return can_be_divided;
    }

    public void setCan_be_divided(boolean can_be_divided) {
        this.can_be_divided = can_be_divided;
    }

    public void set_paarameters(memory a){
        address=a.address;
        size=a.size;
        empty=a.empty;
        can_be_divided=a.can_be_divided;
    }


    public int getAddress() {
        return address;
    }

    public double getSize() {
        return size;
    }
    public void setAddress(int address){
        this.address=address;
    }
    public void setSize(double size){
        this.size=size;
    }
}
