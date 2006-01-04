/*
 * Author: Bart Cremers (Real Software)
 * Date: 13-dec-2005
 * Time: 20:10:46
 */
package org.jajuk.ui.action;

import java.awt.event.ActionEvent;
import org.jajuk.ui.CommandJPanel;

/**
 * @author Bart Cremers(Real Software)
 * @since 13-dec-2005
 */
public class IncreaseVolumeAction extends ActionBase {

    IncreaseVolumeAction() {
        super("increase volume", "ctrl PLUS", true); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public void perform(ActionEvent evt) {
        int iOld = CommandJPanel.getInstance().getCurrentVolume();
        int iNew = iOld + 5;
        CommandJPanel.getInstance().setVolume(((float) iNew) / 100);
    }
}
