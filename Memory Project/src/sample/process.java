package sample;


public class process {
    int id;
    double size;
    memory assigned_memory;
    boolean deallocated;
   public process(){
       id = 0;
        size=0;
        assigned_memory.setSize(0);
        assigned_memory.setAddress(-1);
        deallocated = false;
    }

    public process(boolean deallocated) {
        this.deallocated = deallocated;
    }

    public boolean isDeallocated() {
        return deallocated;
    }

    public void setDeallocated(boolean deallocated) {
        this.deallocated = deallocated;
    }

    public process(int id, double size) {
        this.id = id;
        this.size = size;
       // System.out.println("I reached here");
        assigned_memory=new memory(-1,-0);
        this.deallocated = false;
    }

    public process(int id, double size, memory assigned_memory, boolean deallocated) {
        this.id = id;
        this.size = size;
        this.assigned_memory = assigned_memory;
        this.deallocated = deallocated;
    }

    public process(int id, double size, memory assigned_memory) {
        this.id = id;
        this.size = size;
        this.assigned_memory = assigned_memory;
        deallocated = false;
    }


    public process(process a){
       id=a.id;
       size=a.size;
       assigned_memory=a.assigned_memory;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public memory getAssigned_memory() {
        return assigned_memory;
    }

    public void setAssigned_memory(memory assigned_memory) {
        this.assigned_memory = assigned_memory;
    }
}
