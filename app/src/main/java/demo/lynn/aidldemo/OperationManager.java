package demo.lynn.aidldemo;

import android.content.Context;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by lynn on 2018/1/18.
 * 作为aidlservice和实际调用代码的桥梁和管理者。
 * 例如可以在此manager中加载jni库，声明native方法等,后续添加
 *
 */

public class OperationManager {
    private static OperationManager mInstance;

    private static OperationListener mListener;//leak?

    private static final Object mSyncObject= new Object();

    private OperationManager(){

    }
    public static OperationManager getInstance(){
        synchronized (mSyncObject){

            if(mInstance==null){
                mInstance=new OperationManager();

            }
            return mInstance;
        }
    }

    /**
     * aidlservice will implement this interface,and register itself
     * so Operation can
     * call callback to Aidlservice.
     */
    public interface OperationListener{
        void onSomeState(int id,int state);

        void onResult(int id,int result);

        void onError(String msg);
    }

    public void registerListener(OperationListener listener)
    {
        synchronized (mSyncObject){
            if(mListener!=null){
                Log.w("OprationManager","pre mListener isn't null");
            }
            mListener=listener;
        }
    }
    public void unregisterListener(OperationListener listener){
        if(mListener!=listener){
            Log.w("OprationManager","mListener!=listener unregisterListener failed");
        }else{
            mListener=null;
        }
    }

    //提供具体的一些接口，由aidlservice来访问

    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) {
        Log.i("OperationManager","anInt="+anInt+" along="+aLong+" aBoolean="+aBoolean+
        " aFloat="+aFloat+" aDouble="+aDouble+" aString="+aString);

    }


    public byte[] onExportBytes(int id)  {
        byte[] k={-128,1,1,10,127};
        return k;
    }


    public String getAlVersion()  {
        if(mListener!=null){//example for callback
            mListener.onSomeState(1,204);
        }

        return "operation version 1.0";
    }


    public int onImportTemplates(byte[] ptemplates)  {
        if (ptemplates!=null){
            Log.i("OperationManager","ptemplates="+ptemplates);
        }else{
            Log.w("OperationManager","ptemplates is null");
        }

        return 0;
    }

    //后续，定义给jni回调
    public static void onResult(int id,int result){
        if(mListener!=null){
            mListener.onResult(id,result);
        }
    }
    // TODO: onError onSomeState
}
