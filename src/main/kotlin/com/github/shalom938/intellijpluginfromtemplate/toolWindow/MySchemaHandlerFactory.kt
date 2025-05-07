package com.github.shalom938.intellijpluginfromtemplate.toolWindow

import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.callback.CefSchemeHandlerFactory
import org.cef.handler.CefResourceHandler
import org.cef.network.CefRequest
import java.net.URI

class MySchemaHandlerFactory : CefSchemeHandlerFactory {
    override fun create(
        browser: CefBrowser,
        frame: CefFrame,
        schemeName: String,
        request: CefRequest
    ): CefResourceHandler? {


        val url = URI(request.url).toURL()
        if(url.path.startsWith("/objects")){
            return MyApiProxyHandler()
        }

        return MyResourceHandler()
    }

    fun getSchema(): String = "https"

    fun getDomain(): String = "main"

}
