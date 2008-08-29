/*
 *  Jajuk
 *  Copyright (C) 2003-2008 The Jajuk Team
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *  $Revision: 3132 $
 */
package org.jajuk.ui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import org.jajuk.base.Directory;
import org.jajuk.base.File;
import org.jajuk.services.players.FIFO;
import org.jajuk.util.Conf;
import org.jajuk.util.IconLoader;
import org.jajuk.util.Messages;
import org.jajuk.util.UtilFeatures;

/**
 * Play directories for a selection of files. For now, jajuk only play the first
 * found full directory
 * <p>
 * Action emitter is responsible to ensure all items provided share the same
 * type
 * </p>
 * <p>
 * Selection data is provided using the swing properties DETAIL_SELECTION
 * </p>
 */
public class PlayDirectorySelectionAction extends SelectionAction {

  private static final long serialVersionUID = -8078402652430413821L;

  PlayDirectorySelectionAction() {
    super(Messages.getString("FilesTableView.15"), IconLoader.ICON_DIRECTORY_SYNCHRO, true);
    setShortDescription(Messages.getString("FilesTableView.15"));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jajuk.ui.actions.JajukAction#perform(java.awt.event.ActionEvent)
   */
  @SuppressWarnings("unchecked")
  @Override
  public void perform(ActionEvent e) throws Exception {
    super.perform(e);
    if (selection.size() == 0 || !(selection.get(0) instanceof File)) {
      return;
    }
    // Select all files from the first found directory
    Directory dir = ((File) selection.get(0)).getDirectory();
    List<File> files = UtilFeatures.getPlayableFiles(dir);
    FIFO.push(
        UtilFeatures.createStackItems(UtilFeatures.applyPlayOption(files), Conf
            .getBoolean(CONF_STATE_REPEAT), true), false);
  }

}
