package com.github.shalom938.intellijpluginfromtemplate.toolWindow

import org.cef.callback.CefCallback
import org.cef.handler.CefResourceHandler
import org.cef.misc.IntRef
import org.cef.misc.StringRef
import org.cef.network.CefRequest
import org.cef.network.CefResponse

class MyApiProxyHandler  : CefResourceHandler{
    override fun processRequest(
        request: CefRequest?,
        callback: CefCallback?
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getResponseHeaders(
        response: CefResponse?,
        responseLength: IntRef?,
        redirectUrl: StringRef?
    ) {
        TODO("Not yet implemented")
    }

    override fun readResponse(
        dataOut: ByteArray?,
        bytesToRead: Int,
        bytesRead: IntRef?,
        callback: CefCallback?
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }

}
