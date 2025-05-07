package com.github.shalom938.intellijpluginfromtemplate.toolWindow

import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.github.shalom938.intellijpluginfromtemplate.MyBundle
import com.github.shalom938.intellijpluginfromtemplate.services.MyProjectService
import com.intellij.ui.jcef.JBCefApp
import com.intellij.ui.jcef.JBCefBrowserBase
import com.intellij.ui.jcef.JBCefBrowserBuilder
import org.cef.CefApp
import java.awt.BorderLayout
import javax.swing.JButton


class MyToolWindowFactory : ToolWindowFactory {

    init {
        thisLogger().warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MyToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow(toolWindow: ToolWindow) {

        private val service = toolWindow.project.service<MyProjectService>()

        fun getContent() = JBPanel<JBPanel<*>>().apply {

            layout = BorderLayout()

            if (!JBCefApp.isSupported()) {
                val label = JBLabel("JCef not supported")
                add(label,BorderLayout.CENTER)
                return@apply
            }

            val jcefRemoteEnabled = System.getProperty("jcef.remote.enabled")
            val jcefRemoteLabel = JBLabel("JCEf remote enable : $jcefRemoteEnabled")
            add(jcefRemoteLabel, BorderLayout.NORTH)


            val jbCefBrowser = JBCefBrowserBuilder()
                .setEnableOpenDevToolsMenuItem(true)
                .build()

            jbCefBrowser.setErrorPage(JBCefBrowserBase.ErrorPage.DEFAULT)

            val lifespanHandler = MyLifespanHandle()
            jbCefBrowser.jbCefClient.addLifeSpanHandler(lifespanHandler, jbCefBrowser.cefBrowser)



            add(jbCefBrowser.component, BorderLayout.CENTER)


        }
    }
}
