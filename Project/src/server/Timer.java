package server;



public class Timer implements Runnable{
    private CloseableAuction m;

    public Timer(CloseableAuction m){
        this.m = m;
    }


    @Override
    public void run() {
        while (true){
            m.closeAuctions();
        }
    }
}
