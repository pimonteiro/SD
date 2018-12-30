package server;


import common.IDNotFoundException;

public class Timer implements Runnable{
    private Middleware m;

    public Timer(Middleware m){
        this.m = m;
    }


    @Override
    public void run() {
        while (true){
            for(Integer id : m.getIdContainner()){
             if(m.getAuction(id).getStart()>=1000){
                 try {
                     m.closeAuction(id);
                 } catch (IDNotFoundException e) {
                     e.printStackTrace();
                 }
             }
         }
        }
    }
}
