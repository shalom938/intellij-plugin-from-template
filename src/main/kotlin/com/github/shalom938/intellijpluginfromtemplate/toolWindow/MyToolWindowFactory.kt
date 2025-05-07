package com.github.shalom938.intellijpluginfromtemplate.toolWindow

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.jcef.JBCefApp
import com.intellij.ui.jcef.JBCefBrowserBase
import com.intellij.ui.jcef.JBCefBrowserBuilder
import java.awt.BorderLayout
import javax.swing.SwingConstants


class MyToolWindowFactory : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MyToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }

    override fun shouldBeAvailable(project: Project) = true

    class MyToolWindow(toolWindow: ToolWindow) {

        fun getContent() = JBPanel<JBPanel<*>>().apply {

            layout = BorderLayout()

            if (!JBCefApp.isSupported()) {
                val label = JBLabel("JCef not supported")
                add(label,BorderLayout.CENTER)
                return@apply
            }

            val jcefRemoteEnabled = System.getProperty("jcef.remote.enabled")
            val jcefRemoteLabel = JBLabel("JCEf remote enable : $jcefRemoteEnabled")
            jcefRemoteLabel.isOpaque = true
            jcefRemoteLabel.background = JBColor.BLUE
            jcefRemoteLabel.horizontalAlignment = SwingConstants.CENTER
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
