// IMyAidlInterfaceCallback.aidl
package demo.lynn.aidldemo;

// Declare any non-default types here with import statements

interface IMyAidlInterfaceCallback {
    void onSomeState(int id,int state);

    void onResult(int id,int result);

    void onError(String msg);
}
