package demo.lynn.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by lynn on 2018/1/18.
 *
 * 其他进程的应用通过
 * bindService(new Intent("demo.lynn.aidldemo.AIDLService"),conection,Context.BIND_AUTO_CREATE);
 * 来获取IMyAidlInterface.Stub的实例
 * 然后根据拷贝的aidl文件来定义接口规范，实现进程间的通信。
 */

public class AIDLService extends Service implements OperationManager.OperationListener{

    private static OperationManager manager=null;


    //RemoteCallbackList是系统专门提供的用于删除跨进程listener的接口
    final RemoteCallbackList<IMyAidlInterfaceCallback> mCallbacks = new RemoteCallbackList<IMyAidlInterfaceCallback>();


    @Override
    public void onCreate() {

        super.onCreate();
        manager = OperationManager.getInstance();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return stub;//return IMyAidlInterface, 通过onbind提供给client端通过
    }

    private IMyAidlInterface.Stub stub = new IMyAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
            manager.basicTypes(anInt,aLong,aBoolean,aFloat,aDouble,aString);
        }

        @Override
        public byte[] onExportBytes(int id) throws RemoteException {
            return manager.onExportBytes(id);
        }

        @Override
        public String getAlVersion() throws RemoteException {
            onSomeState(111,222);
            return manager.getAlVersion();
        }

        @Override
        public int onImportTemplates(byte[] ptemplates) throws RemoteException {
            return manager.onImportTemplates(ptemplates);
        }

        @Override
        public void registerListener(IMyAidlInterfaceCallback cb) throws RemoteException {

                mCallbacks.register(cb);
        }

        @Override
        public void unregisterListener(IMyAidlInterfaceCallback cb) throws RemoteException {
            mCallbacks.unregister(cb);
        }


    } ;

    //实际代码运行后回调的callback，由operationsmanager->aidlservice->client其他应用实现的callback具体方法
    //我们在另外一个应用中可通过实现 aidlcallback 并注册，实现进程间的回调。
    //RemoteCallbackList是系统专门提供的用于删除跨进程listener的接口

    @Override
    public void onSomeState(int id, int state) {
        final int n=mCallbacks.beginBroadcast();
        for(int i=0;i<n;i++){
            try{
                mCallbacks.getBroadcastItem(i).onSomeState(id,state);
            }catch (RemoteException re){
                re.printStackTrace();
            }
        }
        mCallbacks.finishBroadcast();

    }

    @Override
    public void onResult(int id, int result) {
        final int n=mCallbacks.beginBroadcast();
        for(int i=0;i<n;i++){
            try{
                mCallbacks.getBroadcastItem(i).onResult(id,result);

            }catch (RemoteException re){
                re.printStackTrace();
            }
        }
        mCallbacks.finishBroadcast();
    }

    @Override
    public void onError(String msg) {
        final int n=mCallbacks.beginBroadcast();
        for(int i=0;i<n;i++){
            try{
                mCallbacks.getBroadcastItem(i).onError(msg);
            }catch (RemoteException re){
                re.printStackTrace();
            }
        }
        mCallbacks.finishBroadcast();
    }
}
