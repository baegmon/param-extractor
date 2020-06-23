package burp;

import java.awt.Component;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.Label;
import java.util.ArrayList;
import java.util.List;

public class BurpExtender implements IBurpExtender, ITab, IContextMenuFactory
{
    private IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;
    private JTabbedPane tabbedPane;

    private PrintWriter stdout;
    private PrintWriter stderr;
    
    //
    // implement IBurpExtender
    //
    
    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        // keep a reference to our callbacks object
        this.callbacks = callbacks;

        // set our extension name
        callbacks.setExtensionName(burp.Config.NAME);

        // register context menu factory
        callbacks.registerContextMenuFactory(this);

        // obtain an extension helpers object
        helpers = callbacks.getHelpers();
        
        // obtain our output stream
        stdout = new PrintWriter(callbacks.getStdout(), true);
        stderr = new PrintWriter(callbacks.getStderr(), true);

        stdout.println(burp.Config.INTRO);

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                // tabs with request/response viewers
                tabbedPane = new JTabbedPane();
                JPanel panel = new JPanel();

                panel.setLayout(null);

                tabbedPane.addTab("Tab 1", panel);

                // customize our UI components
                callbacks.customizeUiComponent(tabbedPane);

                // add the custom tab to Burp's UI
                callbacks.addSuiteTab(BurpExtender.this);
            }
        });
    }

    //
    // implement ITab
    //

    @Override
    public String getTabCaption()
    {
        return "Extractor";
    }

    @Override
    public Component getUiComponent()
    {
        return tabbedPane;
    }

    @Override
    public List<JMenuItem> createMenuItems(final IContextMenuInvocation invocationContext) {

        if (invocationContext.getInvocationContext() != IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST ||
                invocationContext.getInvocationContext() != IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_RESPONSE ||
                invocationContext.getInvocationContext() != IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_REQUEST ||
                invocationContext.getInvocationContext() != IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_RESPONSE)
            return null;

        /*
        if (invocationContext.getToolFlag() != IBurpExtenderCallbacks.TOOL_REPEATER
            || invocationContext.getToolFlag() != IBurpExtenderCallbacks.TOOL_PROXY)
            return null;

        if (invocationContext.getInvocationContext() != IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST)
            return null;
         */

        List<JMenuItem> menuItemList = new ArrayList<>();
        JMenuItem menuItem = new JMenuItem("Taint");

        menuItemList.add(menuItem);
        return menuItemList;

    }

}