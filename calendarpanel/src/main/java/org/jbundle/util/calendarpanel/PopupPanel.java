/*
 * Copyright © 2011 jbundle.org. All rights reserved.
 */
package org.jbundle.util.calendarpanel;

import javax.swing.Popup;

public interface PopupPanel {
    /**
     * Set my popup parent, so I can hide when the user clicks a button.
     * @param popupParent
     */
    public void setPopupParent(Popup popupParent);

}
