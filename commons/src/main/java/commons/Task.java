package commons;


public class Task {
    private String task;
    public boolean status;

    public Task(String task) {
        this.task = task;
        status=false;
    }
    public void setTick(boolean ifTicks){
        status=ifTicks;
    }
}
