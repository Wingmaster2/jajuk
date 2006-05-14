/*
 * Author: Bart Cremers (Real Software)
 * Date: 4-jan-2006
 * Time: 08:17:46
 */
package org.jajuk.ui.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import org.jajuk.base.FIFO;
import org.jajuk.base.FileManager;
import org.jajuk.i18n.Messages;
import org.jajuk.util.ConfigurationManager;
import org.jajuk.util.Util;
import org.jajuk.util.error.JajukException;

/**
 * @author Bart Cremers
 * @since 4-jan-2006
 */
public class GlobalRandomAction extends ActionBase {

    GlobalRandomAction() {
        super(Messages.getString("JajukWindow.6"), Util.getIcon(ICON_SHUFFLE_GLOBAL), true); //$NON-NLS-1$
        setShortDescription(Messages.getString("JajukWindow.23")); //$NON-NLS-1$
    }

    public void perform(ActionEvent evt) throws JajukException {
        ArrayList alToPlay = FileManager.getInstance().getGlobalShufflePlaylist();
        FIFO.getInstance().push(Util.createStackItems(alToPlay, ConfigurationManager.getBoolean(
            CONF_STATE_REPEAT), false), false);
    }
}
