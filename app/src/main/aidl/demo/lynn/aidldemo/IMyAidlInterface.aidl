// IMyAidlInterface.aidl
package demo.lynn.aidldemo;

//需要倒入自定义的aidl callback
import demo.lynn.aidldemo.IMyAidlInterfaceCallback;

// Declare any non-default types here with import statements
// 写完之后需要make project后生成对应的java文件

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    byte[] onExportBytes(int id);

    String getAlVersion();

    void registerListener(IMyAidlInterfaceCallback cb);

    void unregisterListener(IMyAidlInterfaceCallback cb);

    int onImportTemplates(inout byte[] ptemplates);

}
