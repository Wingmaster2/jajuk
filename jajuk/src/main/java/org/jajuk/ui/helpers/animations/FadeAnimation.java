/*
 *  Jajuk
 *  Copyright (C) 2003-2009 The Jajuk Team
 *  http://jajuk.info
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

package org.jajuk.ui.helpers.animations;

import java.awt.Window;

import org.jajuk.util.log.Log;

/**
 * Fade animation implementation.
 */
public class FadeAnimation extends AbstractAnimation {
  
  /** DOCUMENT_ME. */
  private Direction opacity;

  /**
   * Instantiates a new fade animation.
   * 
   * @param window DOCUMENT_ME
   * @param opacity DOCUMENT_ME
   */
  public FadeAnimation(Window window, Direction opacity) {
    super(window);
    this.opacity = opacity;
  }

  /* (non-Javadoc)
   * @see org.jajuk.ui.helpers.animations.IAnimation#animate(int)
   */
  @Override
  public void animate(final int animationTime) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          for (int i = 0; i < 20; i++) {
            final float progress = i / 20.0f;
            java.awt.EventQueue.invokeLater(new Runnable() {
              @Override
              public void run() {
                AWTUtilities.setWindowOpacity(window, opacity.getOpacity(progress));
              }
            });
            Thread.sleep(animationTime / 20);
          }
        } catch (Exception ex) {
          Log.error(ex);
        }
        animationCompleted();
      }
    }).start();
  }

  /**
   * DOCUMENT_ME.
   */
  public interface Direction {
    
    /**
     * Gets the opacity.
     * 
     * @param progress DOCUMENT_ME
     * 
     * @return the opacity
     */
    float getOpacity(float progress);
  }

  /**
   * DOCUMENT_ME.
   */
  public enum Directions implements Direction {
    
    /** DOCUMENT_ME. */
    IN {
      @Override
      public float getOpacity(float progress) {
        return progress;
      }
    },
    
    /** DOCUMENT_ME. */
    OUT {
      @Override
      public float getOpacity(float progress) {
        return 1 - progress;
      }
    };
  }
}