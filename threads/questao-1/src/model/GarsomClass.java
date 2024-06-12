package model;

public class GarsomClass {
    
    public int id;
    public boolean isAvailable;
    public int capacity;
    public int currentCapacity;

    public GarsomClass(int id, int capacity) {
        this.id = id;
        this.isAvailable = true;
        this.capacity = capacity;
        currentCapacity = 0;
    }
}
