/*
 This file is part of the NetBeans Code Outline plugin. 
 Copyright (C) 2014 Benno Markiewicz 

 This program is free software; you can redistribute it and/or 
 modify it under the terms of the GNU General Public License 
 as published by the Free Software Foundation; either version 2 
 of the License, or (at your option) any later version. 

 This program is distributed in the hope that it will be useful, 
 but WITHOUT ANY WARRANTY; without even the implied warranty of 
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 GNU General Public License for more details. 

 You should have received a copy of the GNU General Public License 
 along with this program; if not, write to the Free Software 
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. 
 */
package de.markiewb.netbeans.plugins.outline;

import bluej.editor.moe.NaviView;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.text.StyledDocument;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.cookies.EditorCookie;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;


@ConvertAsProperties(
        dtd = "-//de.markiewb.netbeans.plugins.outline//Outline//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "OutlineTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "de.markiewb.netbeans.plugins.outline.OutlineTopComponent")
@ActionReference(path = "Menu/Window/Tools" , position = 2000, separatorBefore = 1999 )
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_OutlineAction",
        preferredID = "OutlineTopComponent"
)
@Messages({
    "CTL_OutlineAction=Code Outline",
    "CTL_OutlineTopComponent=Code Outline",
    "HINT_OutlineTopComponent=Code Outline"
})
/**
 * Integrates the code outline component of BlueJ into the NetBeans.
 */
public final class OutlineTopComponent extends TopComponent implements LookupListener {

    private Lookup.Result<EditorCookie> lookupResult;
    private final static int NAVIVIEW_WIDTH = 90;       // width of the "naviview" (min-source) box

    public OutlineTopComponent() {
        initComponents();
        setName(Bundle.CTL_OutlineTopComponent());
        setToolTipText(Bundle.HINT_OutlineTopComponent());
    }

    @Override
    public void resultChanged(LookupEvent ev) {

        Collection<? extends EditorCookie> allInstances = lookupResult.allInstances();
        if (allInstances.size() == 1) {
            EditorCookie next = allInstances.iterator().next();
            StyledDocument sourceDocument = next.getDocument();
            JEditorPane[] openedPanes = next.getOpenedPanes();
            if (null == openedPanes || openedPanes.length < 1) {
                return;
            }
            JEditorPane editorPane = openedPanes[0];
            Container parent = editorPane.getParent();
            if (null == parent) {
                return;
            }
            parent = parent.getParent();
            if (null == parent) {
                return;
            }
            if (parent instanceof JScrollPane) {

                JScrollPane scrollPane = (JScrollPane) parent;
                scrollPane.getVerticalScrollBar().setUnitIncrement(16);

                NaviView naviView = new NaviView(sourceDocument, scrollPane.getVerticalScrollBar());
                naviView.setPreferredSize(new Dimension(NAVIVIEW_WIDTH, 0));
                naviView.setMaximumSize(new Dimension(NAVIVIEW_WIDTH, Integer.MAX_VALUE));
                naviView.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                jPanel1.removeAll();

                jPanel1.setLayout(new BorderLayout());
                jPanel1.add(naviView, BorderLayout.CENTER);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {

        Lookup actionsGlobalContext = Utilities.actionsGlobalContext();
        lookupResult = actionsGlobalContext.lookupResult(EditorCookie.class);
        lookupResult.addLookupListener(this);

    }

    @Override
    public void componentClosed() {
        lookupResult.removeLookupListener(this);
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
//        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
//        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
