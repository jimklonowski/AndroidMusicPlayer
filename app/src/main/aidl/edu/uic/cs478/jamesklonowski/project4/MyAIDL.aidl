// MyAIDL.aidl
package edu.uic.cs478.jamesklonowski.project4;

// Declare any non-default types here with import statements

interface MyAIDL {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void completionToast();
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}
