/*
 *  Jajuk
 *  Copyright (C) 2007 The Jajuk Team
 *
 *  Originally taken from:
 *   aTunes 1.6.0
 *   Copyright (C) 2006-2007 Alex Aranda (fleax) alex.aranda@gmail.com
 *
 *   http://www.atunes.org
 *   http://sourceforge.net/projects/atunes
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

package org.jajuk.services.lyrics;



import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jajuk.base.File;
import org.jajuk.services.lyrics.persisters.ILyricsPersister;
import org.jajuk.services.lyrics.providers.ILyricsProvider;
import org.jajuk.services.lyrics.providers.JajukLyricsProvider;
import org.jajuk.util.log.Log;



/**
 * Lyrics retrieval service. This service will retrieves lyrics from various
 * providers, querying all of them until one returns some valid input. (TODO:
 * user-selectable multi-input-sources. edit the LyricsService so that it will
 * notify about various valid sources, so we could propose various inputs to the
 * user)
 * 
 * For now the lyrics providers list is static and stored directly in this class
 */
public final class LyricsService {

  private static List<ILyricsProvider> providers = null;
  private static ILyricsProvider current = null;
  private static List<ILyricsPersister> persisters = null;

  /** Providers list */
  private static String[] providersClasses = new String[] {
      "org.jajuk.services.lyrics.providers.TagLyricsProvider",
      "org.jajuk.services.lyrics.providers.TxtLyricsProvider",
      "org.jajuk.services.lyrics.providers.LyricWikiWebLyricsProvider",
      "org.jajuk.services.lyrics.providers.FlyWebLyricsProvider",
      "org.jajuk.services.lyrics.providers.LyrcWebLyricsProvider"};
  
  /** Persisters list */
  private static String[] persisterClasses = new String[] {
    "org.jajuk.services.lyrics.persisters.TagPersister",
    "org.jajuk.services.lyrics.persisters.TxtPersister"
  };

  /**
   * Empty private constructor to avoid instantiating utility class
   */
  private LyricsService() {

  }

  /**
   * Loads the appropriate providers from the properties file. For now,
   * providers order is static and the providersClasses array reflect jajuk
   * authors service preferred ordering
   * 
   * @TODO this behavior could eventually be switched to a shuffle provider list
   *       for performance or better resources usage reasons
   * 
   * 
   */
  @SuppressWarnings("unchecked")
  public static void loadProviders() {
    providers = new ArrayList<ILyricsProvider>(2);
    try {
      for (String providerClass : providersClasses) {
        if (!StringUtils.isBlank(providerClass)) {
          Class<ILyricsProvider> clazz = (Class<ILyricsProvider>) Class.forName(providerClass);
          ILyricsProvider provider = clazz.newInstance();
          providers.add(provider);
          Log.debug("Added Lyrics provider " + providerClass);
        }
      }
    } catch (Exception e) {
      Log.error(e);
    }
  }

  /**
   */
  @SuppressWarnings("unchecked")
  public static void loadPersisters() {
    persisters = new ArrayList<ILyricsPersister>(2);
    try {
      for (String persisterClass : persisterClasses) {
        if (!StringUtils.isBlank(persisterClass)) {
          Class<ILyricsPersister> clazz = (Class<ILyricsPersister>) Class.forName(persisterClass);
          ILyricsPersister persister = clazz.newInstance();
          persisters.add(persister);
          Log.debug("Added Lyrics persister " + persisterClass);
        }
      }
    } catch (Exception e) {
      Log.error(e);
    }
  }

  /**
   * Cycles through lyrics providers to return the best matching lyrics.
   * 
   * @param audiofile
   *          the audio file to search lyrics for
   * 
   * @return the song's lyrics
   */
  public static String getLyrics(final File audioFile) {
    String lyrics = null;
    
    current = null;
    Log.debug("Retrieving lyrics for file {{" + audioFile + "}}");
    for (final ILyricsProvider provider : getProviders()) {
      lyrics = provider.getLyrics(audioFile);
      current = provider;
      if (lyrics != null) {
        break;
      }
    }
    return lyrics;
  }
  
  public static void commitLyrics(JajukLyricsProvider provider) {
    boolean commitOK = false;
    Log.debug("Commiting lyrics for file {{" + provider + "}}");
    for (final ILyricsPersister persister : getPersisters()) {    
      commitOK = persister.commitLyrics(provider);
      if (commitOK) {
        break;
      }
    }
    
    if (commitOK) {
      Log.warn("Lyrics successfully commited to " + provider.getFile().getName());
    }
    else {
      Log.warn("Lyrics could not be commited to " + provider.getFile().getName());
    }
  }
  
  public static void deleteLyrics(JajukLyricsProvider provider) {
    for (final ILyricsPersister persister : getPersisters()) {    
      persister.deleteLyrics(provider);
    }
  }

  /**
   * Returns the lazy-instantiated providers collection
   * 
   * @return the map of loaded providers
   */
  public static List<ILyricsProvider> getProviders() {
    if (providers == null) {
      loadProviders();
    }
    return providers;
  }

  public static ILyricsProvider getCurrentProvider() {
    return current;
  }
  
  public static List<ILyricsPersister> getPersisters() {
    if (persisters == null) {
      loadPersisters();
    }
    
    return persisters;
  }
}
