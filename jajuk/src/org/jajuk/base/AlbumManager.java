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
 *
 */

package org.jajuk.base;

import java.util.ArrayList;
import java.util.Iterator;

import org.jajuk.util.MD5Processor;

/**
 *  Convenient class to manage Albums
 * @Author    Bertrand Florat
 * @created    17 oct. 2003
 */
public class AlbumManager extends ItemManager{
	/**Self instance*/
    private static AlbumManager singleton;

	/**
	 * No constructor available, only static access
	 */
	private AlbumManager() {
        super();
    }
    
  /**
     * @return singleton
     */
    public static AlbumManager getInstance(){
      if (singleton == null){
          singleton = new AlbumManager();
      }
        return singleton;
    }
    
	/**
	 * Register an Album
	 *@param sName
	 */
	public synchronized  Album registerAlbum(String sName) {
		String sId = MD5Processor.hash(sName.trim().toLowerCase());
		return registerAlbum(sId,sName);
	}
    
    /**
     * Register an Album with a known id
     * 
     * @param sName
     */
    public synchronized Album registerAlbum(String sId, String sName) {
        if (hmItems.containsKey(sId)) {
            return (Album)hmItems.get(sId);
        }
        Album album = new Album(sId, sName);
        hmItems.put(sId, album);
        postRegistering(album);
        return album;
    }
			
    /**
     * Chnage the item
     * @param old
     * @param sNewName
     * @return new album
     */
    public synchronized Album changeAlbumName(Album old,String sNewName){
        Album newItem = registerAlbum(sNewName);
        ArrayList alTracks = new ArrayList(TrackManager.getInstance().getItems()); //we need to create a new list to avoid concurrent exceptions
        Iterator it = alTracks.iterator();
        while (it.hasNext()){
            Track track = (Track)it.next();
            if (track.getAlbum().equals(old)){
                TrackManager.getInstance().changeTrackAlbum(track,sNewName);
            }
        }
        cleanup();//remove useless albums if no more tracks use it
        return newItem;
    }
    
	/**
		 * Format the album name to be normalized : 
		 * <p>-no underscores or other non-ascii characters
		 * <p>-no spaces at the begin and the end
		 * <p>-All in lower cas expect first letter of first word
		 * <p> exemple: "My album title" 
		 *  @param sName
		 * @return
		 */
		private synchronized String format(String sName){
			String sOut;
			sOut = sName.trim(); //supress spaces at the begin and the end
			sOut.replace('-',' ');  //move - to space
			sOut.replace('_',' '); //move _ to space
			char c = sOut.charAt(0);
			sOut = sOut.toLowerCase();
			StringBuffer sb = new StringBuffer(sOut);
			sb.setCharAt(0,Character.toUpperCase(c));
			return sb.toString();
		}

    /* (non-Javadoc)
     * @see org.jajuk.base.ItemManager#getIdentifier()
     */
    public String getIdentifier() {
        return XML_ALBUMS;
    }
   
}
