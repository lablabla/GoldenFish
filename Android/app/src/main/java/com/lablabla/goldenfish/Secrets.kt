package com.lablabla.goldenfish

class Secrets {

    // Method calls will be added by gradle task hideSecret
    // Example : external fun getWellHiddenSecret(packageName: String): String

    companion object {
        init {
            System.loadLibrary("secrets")
        }
    }
    external fun getBrokerAddress(packageName: String): String

    external fun getBrokerUserName(packageName: String): String

    external fun getBrokerUPassword(packageName: String): String
}