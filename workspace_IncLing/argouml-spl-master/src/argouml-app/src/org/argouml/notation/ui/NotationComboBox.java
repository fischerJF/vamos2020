// $Id: NotationComboBox.java 132 2010-09-26 23:32:33Z marcusvnac $
// Copyright (c) 1996-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.notation.ui;
import java.awt.Dimension;
import java.util.ListIterator;

import javax.swing.JComboBox;

//#if defined(LOGGING)
//@#$LPS-LOGGING:GranularityType:Import
import org.apache.log4j.Logger;
//#endif
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.events.ArgoNotationEventListener;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.specifications.Configuration;

/**
 *   This class provides a self-updating notation combo box.
 *
 *   @author Thierry Lach
 *   @since 0.9.4
 */
public class NotationComboBox
    extends JComboBox
    implements ArgoNotationEventListener {
    //#if defined(LOGGING)
    //@#$LPS-LOGGING:GranularityType:Field
    /**
     * Logger.
     */
//    private static Logger LOG = Logger.getLogger(NotationComboBox.class);
    private static Logger LOG = null;
    //#endif
    /**
     * The instance.
     */
    private static NotationComboBox singleton;

    /**
     * @return the singleton
     */
    public static NotationComboBox getInstance() {
        // Only instantiate when we need it.
        if (singleton == null) {
            singleton = new NotationComboBox();
        }
        if(Configuration.LOGGING) {
        LOG = Logger.getLogger(NotationComboBox.class);
        }        
        return singleton;
    }

    /**
     * The constructor.
     */
    public NotationComboBox() {
        super();
        setEditable(false);
        setMaximumRowCount(6);

        Dimension d = getPreferredSize();
        d.width = 200;
        setMaximumSize(d);

        ArgoEventPump.addListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
        refresh();
    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationChanged(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationChanged(ArgoNotationEvent event) {
    }
    
    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationAdded(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationAdded(ArgoNotationEvent event) {
        refresh();
    }
    
    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationRemoved(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationRemoved(ArgoNotationEvent event) {
    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderAdded(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationProviderAdded(ArgoNotationEvent event) {
    }

    /*
     * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderRemoved(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationProviderRemoved(ArgoNotationEvent event) {
    }

    /**
     * Refresh the combobox contents.
     */
    public void refresh() {
        removeAllItems();
        ListIterator iterator =
            Notation.getAvailableNotations().listIterator();
        while (iterator.hasNext()) {
            try {
                NotationName nn = (NotationName) iterator.next();
                addItem(nn);
            } catch (Exception e) {
                //#if defined(LOGGING)
                //@#$LPS-LOGGING:GranularityType:Statement
                //@#$LPS-LOGGING:Localization:NestedStatement
            	 if(Configuration.LOGGING) {
                 LOG.error("Unexpected exception", e);
            	 }//#endif
            }
        }
        setVisible(true);
        invalidate();
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 4059899784583789412L;
}
