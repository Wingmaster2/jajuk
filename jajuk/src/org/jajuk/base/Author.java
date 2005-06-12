/*
 *  Jajuk
 *  Copyright (C) 2003 Bertrand Florat
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
package org.jajuk.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.jajuk.i18n.Messages;

/**
 *  An author
 **<p> Logical item
 * @author     Bertrand Florat
 * @created    17 oct. 2003
 */
public class Author extends PropertyAdapter implements Comparable{

	/**Albums for this author*/
	private ArrayList alAlbums = new ArrayList(10);
    
	/**
	 * Author constructor
	 * @param id
	 * @param sName
	 */
	public Author(String sId, String sName) {
        super(sId,sName);
	}
	
/* (non-Javadoc)
     * @see org.jajuk.base.IPropertyable#getIdentifier()
     */
    public String getIdentifier() {
        return XML_AUTHOR;
    }
    /**
	 * @return
	 */
	public String getName() {
		return sName;
	}
	
	/**
	 * Return author name, dealing with unkwnown for any language
	 * @return author name
	 */
	public String getName2() {
		String sOut = getName();
		if (sOut.equals(UNKNOWN_AUTHOR)){ 
			sOut = Messages.getString(UNKNOWN_AUTHOR);
		}
		return sOut;
	}

	
	/**
	 * toString method
	 */
	public String toString() {
		return "Author[ID="+sId+" Name=" + sName + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	/**
	 * @return
	 */
	public String getId() {
		return sId;
	}
	
	
	/**
	 * Equal method to check two authors are identical
	 * @param otherAuthor
	 * @return
	 */
	public boolean equals(Object otherAuthor){
		return this.getId().equals(((Author)otherAuthor).getId() );
	}	
	
	
	/**
	 * hashcode ( used by the equals method )
	 */
	public int hashCode(){
		return getId().hashCode();
	}
	
	
	/**
	 * @return
	 */
	public ArrayList getAlbums() {
		return alAlbums;
	}
	
	/**
	 * @param album
	 */
	public void addAlbum(Album album) {
		alAlbums.add(album);
	}
	
	/**
	 *Alphabetical comparator used to display ordered lists
	 *@param other item to be compared
	 *@return comparaison result 
	 */
	public int compareTo(Object o){
		Author otherAuthor = (Author)o;
		return  getName2().compareToIgnoreCase(otherAuthor.getName2());
	}
	
	/**
	 * return tracks associated with this item
	 * @return tracks associated with this item
	 */
	public ArrayList getTracks() {
		ArrayList alTracks = new ArrayList(100);
		Iterator it = TrackManager.getTracks().iterator();
		while ( it.hasNext()){
			Track track = (Track)it.next();
			if ( track != null && track.getAuthor().equals(this)){
				alTracks.add(track);
			}
		}
		Collections.sort(alTracks);
		return alTracks;
	}
	
	/**
	 * @return whether the author is Unknown or not
	 */
	public boolean isUnknown(){
	    return this.getName().equals(UNKNOWN_AUTHOR); 
   }
    

}
