/*
 *  Jajuk
 *  Copyright (C) 2003 bflorat
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
 * $Log$
 * Revision 1.3  2003/10/24 15:44:25  bflorat
 * 24/10/2003
 *
 * Revision 1.2  2003/10/23 22:07:40  bflorat
 * 23/10/2003
 *
 * Revision 1.1  2003/10/21 20:37:54  bflorat
 * 21/10/2003
 *
 */

package org.jajuk.base;

import org.jajuk.util.Util;

/**
 *  File not managed in collection ( selected by file/open or in a playlist file )
 *
 * @author     bflorat
 * @created    21 oct. 2003
 */
public class BasicFile extends org.jajuk.base.File {

	/**Physical file*/
	java.io.File fio;
	
	public BasicFile(java.io.File fio){
		this.sId = "-1";
		this.sName = fio.getName();
		this.directory = null;
		this.lSize = fio.length();
		this.sQuality = null;
		this.fio = fio;
		//track creation
		String sTrackId = "1";
		String sTrackName = "";
		Album album = null;
		Style style = null;
		Author author = null;
		long length = 0;
		String sYear = ""; 
		Type type = TypeManager.getTypeByExtension(Util.getExtension(fio));
		String sAdditionDate ="";
		this.track = new Track(sTrackId,sTrackName,album,style,author,length,sYear,type,sAdditionDate);
	}
	
		/**
		 * Return full file path name
		 * @param file
		 * @return String
		 */
		public String getAbsolutePath(){
			return fio.getAbsolutePath();
		}

}
