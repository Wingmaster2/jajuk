/*
 *  Jajuk
 *  Copyright (C) 2003 The Jajuk Team
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
 *  $Revision$
 */
package org.jajuk.ui;

import org.jajuk.i18n.Messages;
import org.jajuk.util.ITechnicalStrings;
import org.jajuk.util.JajukFileFilter;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

/**
 * Music-oriented file chooser
 * <p>
 * decorator
 */
public class JajukFileChooser extends JFileChooser implements ITechnicalStrings {

	private static final long serialVersionUID = 1L;
	private JajukFileFilter filter;

	/**
	 * Constructor with specified file filter
	 * 
	 * @param jfilter
	 *            filter to use
	 */
	public JajukFileChooser(JajukFileFilter jfilter) {
		setDialogTitle(Messages.getString("JajukFileChooser.0"));
		this.filter = jfilter;
		for (int i=0;i<jfilter.getFilters().length;i++){
			addChoosableFileFilter(jfilter.getFilters()[i]);
		}
		setMultiSelectionEnabled(true);
		//don't hide hidden files
		setFileHidingEnabled(false);
		setAcceptAllFileFilterUsed(false);
		// Use default directory to store documents (My Documents under Windows
		// for ie)
		setCurrentDirectory(FileSystemView.getFileSystemView()
				.getDefaultDirectory());  
	}

	/**
	 * Default constructor
	 * 
	 */
	public JajukFileChooser() {
		this(null);
	}
	
	/**
	 * Force the filter to accept directories
	 * @param b
	 */
	public void setAcceptDirectories(boolean b) {
		filter.setAcceptDirectories(b);
		for (int i=0;i<filter.getFilters().length;i++){
			filter.getFilters()[i].setAcceptDirectories(b);
		}
	}

}
