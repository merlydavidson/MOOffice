package moffice.meta.com.molibrary.firebase;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.firebase.MOMesgReceiver;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

/**
 * Created by com.moffice.com.microoffice.app on 01/12/2017.
 */

public class FireBaseMesgReceiver  extends FirebaseMessagingService {

    static int num=0;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            LogWriter.writeLog("MesgRecvd","FireBaseMesg");
            if(remoteMessage.getFrom().contains("/topics/meta")){
                Map<String, String> values = remoteMessage.getData();
                String dataMesg = values.get("mesg");
                LogWriter.writeLogDevMesg("Meta",dataMesg);
                return;
            }
            Map<String, String> values = remoteMessage.getData();
            String dataMesg = values.get("mesg");
            JSONObject mesg = new JSONObject(dataMesg);
            LogWriter.writeLog("MesgRecvd-Content",mesg.toString());
            MOMesgReceiver momReceiver = new MOMesgReceiver();
            momReceiver.processMessage(getBaseContext(),mesg);

        }catch (Exception exp){
            LogWriter.writeLogException("MesgRecvd",exp);
        }
    }

    public void processMetaMesg(){

    }
}
