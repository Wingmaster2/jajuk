/*
 * Jajuk Copyright (C) 2003 bflorat
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA. $Log$
 * Place - Suite 330, Boston, MA 02111-1307, USA. Revision 1.3  2003/10/24 15:44:25  bflorat
 * Place - Suite 330, Boston, MA 02111-1307, USA. 24/10/2003
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * Revision 1.2 2003/10/23 22:07:40 bflorat 23/10/2003
 * 
 * Revision 1.1 2003/10/21 17:51:43 bflorat 21/10/2003
 *  
 */

package org.jajuk.base;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Convenient class to manage Tracks
 * 
 * @author bflorat @created 17 oct. 2003
 */
public class TrackManager {
	/** Tracks collection* */
	static HashMap hmTracks = new HashMap(100);

	/**
	 * No constructor available, only static access
	 */
	private TrackManager() {
		super();
	}

	/**
	 * Register an Track
	 * 
	 * @param sName
	 */
	public static Track registerTrack(String sName, Album album, Style style, Author author, long length, String sYear, Type type,  String sAdditionDate) {
		String sId = new Integer(hmTracks.size()).toString();
		Track track = new Track(sId, format(sName), album, style, author, length, sYear, type, sAdditionDate);
		hmTracks.put(sId, track);
		return track;
	}

	/** Return all registred Tracks */
	public static Collection getTracks() {
		return hmTracks.values();
	}

	/**
	 * Return Track by name
	 * 
	 * @param sName
	 * @return
	 */
	public static Track getTrack(String sName) {
		return (Track) hmTracks.get(sName);
	}

	/**
	 * Format the tracks names to be normalized :
	 * <p>
	 * -no underscores or other non-ascii characters
	 * <p>
	 * -no spaces at the begin and the end
	 * <p>
	 * -All in lower cas expect first letter of first word
	 * <p>
	 * exemple: "My track title"
	 * 
	 * @param sName
	 * @return
	 */
	private static String format(String sName) {
		String sOut;
		sOut = sName.trim(); //supress spaces at the begin and the end
		sOut.replace('-', ' '); //move - to space
		sOut.replace('_', ' '); //move _ to space
		char c = sOut.charAt(0);
		sOut = sOut.toLowerCase();
		StringBuffer sb = new StringBuffer(sOut);
		sb.setCharAt(0, Character.toUpperCase(c));
		return sb.toString();
	}

}
