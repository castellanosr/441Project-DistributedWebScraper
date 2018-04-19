import java.util.*;

class Thread1 extends Thread{
  public void run(){
    try{
      System.out.println("Running Task 2");
      task2 t = new task2();
      t.t2();
    }catch(Throwable t){
      t.printStackTrace();
    }
  }
}

class Thread2 extends Thread{
  public void run(){
    try{
      System.out.println("Running Task 3");
      task3 t = new task3();
      t.t3();
    }catch(Throwable t){
      t.printStackTrace();
    }
  }
}

class Thread3 extends Thread{
  public void run(){
    try{
      System.out.println("Running Task 4");
      task4 t = new task4();
      t.t4();
    }catch(Throwable t){
      t.printStackTrace();
    }
  }
}

class Thread4 extends Thread{
  public void run(){
    try{
      System.out.println("Running Task 5");
      task5 t = new task5();
      t.t5();
    }catch(Throwable t){
      t.printStackTrace();
    }
  }
}

public class Workflow{
    public static void main(String [] args){
      try{
        task1 t = new task1();
        t.t1();
        Thread [] threads = new Thread[4];
        Thread1 obj = new Thread1();
        threads[0] = obj;
        obj.start();
        obj.join();
        //obj.dumpStack();
        //obj.stop();
        Thread2 obj2 = new Thread2();
        threads[1] = obj2;
        obj2.start();
        obj2.join();
        Thread3 obj3 = new Thread3();
        threads[2] = obj3;
        obj3.start();
        //obj3.join();
        //Thread.sleep(1000);
        //obj3.stop();
        Thread4 obj4 = new Thread4();
        threads[3] = obj4;
        obj4.start();
        //obj4.join();
        //Thread.sleep(1000);
        //obj4.stop();
        Thread.sleep(10000);
        for(int i = 0; i < threads.length; i++){
          System.out.println(threads[i]);
          threads[i].join();
          System.out.println("Completed: "+ threads[i]);
          //threads[i].destroy();
        }
      }catch(Exception e){
        e.printStackTrace();
      }
      // while(obj.isAlive()){
      //   //System.out.println("I am in the while");
      // }

      task6 t6 = new task6();
      t6.t6(args[0]);

      String workflowName = "";
      int tasks= 0;
      int edges = 0;
      System.exit(0);
    }
}
