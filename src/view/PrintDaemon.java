package view;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author S. B. Wong
 * @version 1.0
 */

public class PrintDaemon {
    java.util.Stack<String> strStack = new java.util.Stack<String>();

    public PrintDaemon() {
        new Thread() {
            public void run() {
                while(true) {
                    if(!strStack.isEmpty()) {
                        System.out.println(strStack.remove(0));
                    }
                    try {
                        Thread.sleep(10);
                    }
                     catch (Exception ex) {
                    }
                }
            }
        }.start();
  }

  //public void println(String s)  {  }
}