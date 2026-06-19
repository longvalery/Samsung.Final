package rva.com.services;

public class CustomerTimer {
    private boolean isActive;
    private long duration;
    private long startTime;


    public CustomerTimer(long duration) { // seconds
        this.duration = duration;
        this.reset();
    }

    public void update(float delta) {
        if (this.isActive) {
            long now = System.currentTimeMillis();
            if (now - this.startTime >= this.duration) {
                this.deactivate(); }
        }
    }

    public void activate(long duration) {
        if (! this.isActive) {
            this.isActive = true;
            this.startTime = System.currentTimeMillis();
            this.duration = duration;
        }
    }

    private void deactivate() { this.reset(); }
    public boolean isActive() {  return this.isActive; }
    public void reset() { isActive = false; startTime = 0;  }

    public int  remainder () {
        return (int) ((this.startTime + this.duration - System.currentTimeMillis()) / 1000);
    }

    public void dispose() { }
}
