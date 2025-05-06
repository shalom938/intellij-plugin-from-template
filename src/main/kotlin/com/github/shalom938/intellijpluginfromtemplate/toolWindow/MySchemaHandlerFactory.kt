package com.github.shalom938.intellijpluginfromtemplate.toolWindow

import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.callback.CefSchemeHandlerFactory
import org.cef.handler.CefResourceHandler
import org.cef.network.CefRequest

class MySchemaHandlerFactory : CefSchemeHandlerFactory {
    override fun create(
        browser: CefBrowser?,
        frame: CefFrame?,
        schemeName: String?,
        request: CefRequest?
    ): CefResourceHandler? {
        return MyResourceHandler()
    }

    fun getSchema(): String = "https"

    fun getDomain(): String = "main"

}
