package com.github.shalom938.intellijpluginfromtemplate.toolWindow

import com.intellij.openapi.diagnostic.thisLogger
import org.cef.CefApp
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.handler.CefLifeSpanHandler

class MyLifespanHandle: CefLifeSpanHandler {
    override fun onBeforePopup(
        browser: CefBrowser,
        frame: CefFrame,
        target_url: String,
        target_frame_name: String
    ): Boolean {
        thisLogger().info("in onBeforePopup")
        return true
    }

    override fun onAfterCreated(browser: CefBrowser) {
        thisLogger().info("in onAfterCreated")
        val schemeHandlerFactory = MySchemaHandlerFactory()
        CefApp.getInstance().registerSchemeHandlerFactory(
            schemeHandlerFactory.getSchema(), null, schemeHandlerFactory
        )
        browser.loadURL("https://main/index.html")
    }

    override fun onAfterParentChanged(browser: CefBrowser) {
        thisLogger().info("in onAfterParentChanged")
    }

    override fun doClose(browser: CefBrowser): Boolean {
        thisLogger().info("in doClose")
        return true
    }

    override fun onBeforeClose(browser: CefBrowser) {
        thisLogger().info("in onBeforeClose")
    }

}
