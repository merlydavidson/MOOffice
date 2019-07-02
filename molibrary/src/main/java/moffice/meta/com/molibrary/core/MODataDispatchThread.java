package moffice.meta.com.molibrary.core;

import android.content.Context;

/**
 * Created by Administrator on 01/06/2018.
 */

public class MODataDispatchThread extends Thread {

   public static Object syncToken;
    private Context baseContext;
    private boolean terminate=false;
    public MODataDispatchThread(Object syncToken)
    {
        this.syncToken = syncToken;
    }
    @Override
    public void run() {
        while(true) // you will need to set some condition if you want to stop the thread in a certain time...
        {
            synchronized (syncToken)
            {
                try {
                    syncToken.wait(6000);
                    if(terminate) return;

                    MOAttenManager.sendAttnData(getBaseContext());
                    MOLeaveManager.sendLeaveData(getBaseContext());
                    MOVisitManager.sendVisData(getBaseContext());
                    MOVisitManager.sendVisMeetData(getBaseContext());
                    MOSupportManager.sendSupData(getBaseContext());
                    MOEnquiryManager.sendEnqData(getBaseContext());
                    MOMain.sendProdData(getBaseContext(),false);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("MODataDispatchThread: ");
        }
    }

    public Context getBaseContext() {
        return baseContext;
    }

    public void setBaseContext(Context baseContext) {
        this.baseContext = baseContext;
    }

    public boolean isTerminate() {
        return terminate;
    }

    public static void triggerDispatch() {
        if(syncToken==null) return;
        synchronized (syncToken) {
            syncToken.notify();
        }

    }
    public void setTerminate(boolean terminate) {
        syncToken.notify();
        this.terminate = terminate;
    }
}
