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
 * $Revision$
 */
package org.jajuk.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import layout.TableLayout;

import org.jajuk.base.FIFO;
import org.jajuk.base.FileManager;
import org.jajuk.base.History;
import org.jajuk.base.HistoryItem;
import org.jajuk.base.ITechnicalStrings;
import org.jajuk.base.SearchResult;
import org.jajuk.i18n.Messages;
import org.jajuk.util.ConfigurationManager;
import org.jajuk.util.Util;
import org.jajuk.util.log.Log;

import com.sun.SwingWorker;

/**
 *  Command panel ( static view )
 *
 * @author     bflorat
 * @created    3 oct. 2003
 */
public class CommandJPanel extends JPanel implements ITechnicalStrings,ActionListener,ListSelectionListener,ChangeListener,Observer{
	
	//singleton
	static private CommandJPanel command;
	
	//widgets declaration
	JToolBar jtbSearch;
	SearchBox  sbSearch;
	JToolBar jtbHistory;
	public SteppedComboBox jcbHistory;
	JToolBar jtbMode;
	JButton jbRepeat;
	JButton jbRandom;
	JButton jbContinue;
	JButton jbIntro;
	JToolBar jtbSpecial;
	JButton jbGlobalRandom;
	JButton jbBestof;
	JButton jbMute;
	JToolBar jtbPlay;
	JButton jbPrevious;
	JButton jbNext;
	JButton jbRew;
	JButton jbPlayPause;
	JButton jbStop;
	JButton jbFwd;
	JToolBar jtbVolume;
	JLabel jlVolume;
	JSlider jsVolume;
	JToolBar jtbPosition;
	JLabel jlPosition;
	JSlider jsPosition;
	
	//variables declaration
	/**Repeat mode flag*/
	static boolean bIsRepeatEnabled = false;
	/**Shuffle mode flag*/
	static boolean bIsShuffleEnabled = false;
	/**Continue mode flag*/
	static boolean bIsContinueEnabled = true;
	/**Intro mode flag*/
	static boolean bIsIntroEnabled = false;
	/**Forward or rewind jump size in track percentage*/
	static final float JUMP_SIZE = 0.1f;
	/**Slider move event filter*/
	private boolean bPositionChanging = false;
	
	
	
	public static synchronized CommandJPanel getInstance(){
		if (command == null){
			command = new CommandJPanel();
		}
		return command;
	}
	
	private CommandJPanel(){
		//dimensions
		int height1 = 25;  //buttons, components
		//int height2 = 36; //slider ( at least this height in the gtk+ l&f ) 
		int iSeparator = 0;
		//set default layout and size
		double[][] size ={{0.20,iSeparator,272,iSeparator,0.13,iSeparator,0.10,iSeparator,0.21,iSeparator,0.16,iSeparator,0.16},
				{height1}}; //note we can't set a % for history combo box because of popup size
		setLayout(new TableLayout(size));
		setBorder(BorderFactory.createEtchedBorder());
		//search toolbar
		jtbSearch = new JToolBar();
		jtbSearch.setFloatable(false);
		sbSearch = new SearchBox(this);
		jtbSearch.add(Box.createHorizontalGlue());
		jtbSearch.add(sbSearch);
		jtbSearch.add(Box.createHorizontalGlue());
		
		//history toolbar
		jtbHistory = new JToolBar();
		jcbHistory = new SteppedComboBox(History.getInstance().getHistory().toArray());
		jtbHistory.setFloatable(false);
		jtbHistory.add(Box.createHorizontalGlue());;
		jcbHistory.setMinimumSize(new Dimension(100,20));
		jcbHistory.setPreferredSize(new Dimension(270,20));
		jcbHistory.setPopupWidth(1000);
		jcbHistory.setToolTipText(Messages.getString("CommandJPanel.0")); //$NON-NLS-1$
		jcbHistory.addActionListener(this);
		jtbHistory.add(jcbHistory);
		jtbHistory.add(Box.createHorizontalGlue());
		
		//Mode toolbar
		jtbMode = new JToolBar();
		jtbMode.setRollover(true);
		jtbMode.setFloatable(false);
		jtbMode.add(Box.createHorizontalGlue());
		ImageIcon ii = null;
		if ( ConfigurationManager.getBoolean(CONF_STATE_REPEAT)){
			ii = Util.getIcon(ICON_REPEAT_ON);
		}
		else{
			ii = Util.getIcon(ICON_REPEAT_OFF);
		}
		jbRepeat = new JButton(ii); 
		jbRepeat.setActionCommand(EVENT_REPEAT_MODE_STATUS_CHANGED);
		jbRepeat.setToolTipText(Messages.getString("CommandJPanel.1")); //$NON-NLS-1$
		jbRepeat.addActionListener(JajukListener.getInstance());
		jtbMode.add(jbRepeat);
		if ( ConfigurationManager.getBoolean(CONF_STATE_SHUFFLE)){
			ii = Util.getIcon(ICON_SHUFFLE_ON);
		}
		else{
			ii = Util.getIcon(ICON_SHUFFLE_OFF);
		}
		jbRandom = new JButton(ii);
		jbRandom.setToolTipText(Messages.getString("CommandJPanel.2")); //$NON-NLS-1$
		jbRandom.setActionCommand(EVENT_SHUFFLE_MODE_STATUS_CHANGED);
		jbRandom.addActionListener(JajukListener.getInstance());
		jtbMode.add(jbRandom);
		if ( ConfigurationManager.getBoolean(CONF_STATE_CONTINUE)){
			ii = Util.getIcon(ICON_CONTINUE_ON);
		}
		else{
			ii = Util.getIcon(ICON_CONTINUE_OFF);
		}
		jbContinue = new JButton(ii); 
		jbContinue.setToolTipText(Messages.getString("CommandJPanel.3")); //$NON-NLS-1$
		jbContinue.setActionCommand(EVENT_CONTINUE_MODE_STATUS_CHANGED);
		jbContinue.addActionListener(JajukListener.getInstance());
		jtbMode.add(jbContinue);
		if ( ConfigurationManager.getBoolean(CONF_STATE_INTRO)){
			ii = Util.getIcon(ICON_INTRO_ON);
		}
		else{
			ii = Util.getIcon(ICON_INTRO_OFF);
		}
		jbIntro = new JButton(ii); 
		jbIntro.setToolTipText(Messages.getString("CommandJPanel.4")); //$NON-NLS-1$
		jbIntro.setActionCommand(EVENT_INTRO_MODE_STATUS_CHANGED);
		jbIntro.addActionListener(JajukListener.getInstance());
		jtbMode.add(jbIntro);
		jtbMode.add(Box.createHorizontalGlue());
		
		//Special functions toolbar
		jtbSpecial = new JToolBar();
		jtbSpecial.add(Box.createHorizontalGlue());
		jtbSpecial.setFloatable(false);
		jtbSpecial.setRollover(true);
		jbGlobalRandom = new JButton(Util.getIcon(ICON_ROLL)); 
		jbGlobalRandom.addActionListener(this);
		jbGlobalRandom.setToolTipText(Messages.getString("CommandJPanel.5")); //$NON-NLS-1$
		jtbSpecial.add(jbGlobalRandom);
		jbBestof = new JButton(Util.getIcon(ICON_BESTOF)); 
		jbBestof.addActionListener(this);
		jbBestof.setToolTipText(Messages.getString("CommandJPanel.6")); //$NON-NLS-1$
		jtbSpecial.add(jbBestof);
		jbMute = new JButton(Util.getIcon(ICON_MUTE)); 
		jbMute.addActionListener(this);
		jbMute.setToolTipText(Messages.getString("CommandJPanel.7")); //$NON-NLS-1$
		jtbSpecial.add(jbMute);
		jtbSpecial.add(Box.createHorizontalGlue());
		
		//Play toolbar
		jtbPlay = new JToolBar();
		jtbPlay.setRollover(true);
		jtbPlay.setFloatable(false);
		jtbPlay.add(Box.createHorizontalGlue());
		jbPrevious = new JButton(Util.getIcon(ICON_PREVIOUS)); 
		jbPrevious.setToolTipText(Messages.getString("CommandJPanel.8")); //$NON-NLS-1$
		jbPrevious.addActionListener(this);
		jtbPlay.add(jbPrevious);
		jbNext = new JButton(Util.getIcon(ICON_NEXT)); 
		jbNext.setToolTipText(Messages.getString("CommandJPanel.9")); //$NON-NLS-1$
		jbNext.addActionListener(this);
		jtbPlay.add(jbNext);
		jtbPlay.addSeparator();
		jbRew = new JButton(Util.getIcon(ICON_REW)); 
		jbRew.setEnabled(false);
		jbRew.setToolTipText(Messages.getString("CommandJPanel.10")); //$NON-NLS-1$
		jbRew.addActionListener(this);
		jtbPlay.add(jbRew);
		jbPlayPause = new JButton(Util.getIcon(ICON_PAUSE)); 
		jbPlayPause.setToolTipText(Messages.getString("CommandJPanel.11")); //$NON-NLS-1$
		jbPlayPause.setEnabled(false);
		jbPlayPause.addActionListener(this);
		jtbPlay.add(jbPlayPause);
		jbStop = new JButton(Util.getIcon(ICON_STOP)); 
		jbStop.setToolTipText(Messages.getString("CommandJPanel.12")); //$NON-NLS-1$
		jbStop.addActionListener(this);
		jbStop.setEnabled(false);
		jtbPlay.add(jbStop);
		jbFwd = new JButton(Util.getIcon(ICON_FWD)); 
		jbFwd.setToolTipText(Messages.getString("CommandJPanel.13")); //$NON-NLS-1$
		jbFwd.setEnabled(false);
		jbFwd.addActionListener(this);
		jtbPlay.add(jbFwd);
		jtbPlay.add(Box.createHorizontalGlue());
		
		//Volume toolbar
		jtbVolume = new JToolBar();
		jtbVolume.setFloatable(false);
		jtbVolume.add(Box.createHorizontalGlue());
		jlVolume = new JLabel(Util.getIcon(ICON_VOLUME)); 
		jtbVolume.add(jlVolume);
		jsVolume = new JSlider(0,100,50);
		jsVolume.setToolTipText(Messages.getString("CommandJPanel.14")); //$NON-NLS-1$
		jsVolume.addChangeListener(this);
		jtbVolume.add(jsVolume);
		jtbVolume.add(Box.createHorizontalGlue());
		
		//Position toolbar
		jtbPosition = new JToolBar();
		jtbPosition.setFloatable(false);
		jtbPosition.add(Box.createHorizontalGlue());
		jlPosition = new JLabel(Util.getIcon(ICON_POSITION)); 
		jtbPosition.add(jlPosition);
		jsPosition = new JSlider(0,100,0);
		jsPosition.addChangeListener(this);
		jsPosition.setEnabled(false);
		jsPosition.setToolTipText(Messages.getString("CommandJPanel.15")); //$NON-NLS-1$
		jtbPosition.add(jsPosition);
		jtbPosition.add(Box.createHorizontalGlue());
		
		//add toolbars to main panel
		add(jtbSearch,"0,0"); //$NON-NLS-1$
		add(jtbHistory,"2,0"); //$NON-NLS-1$
		add(jtbMode,"4,0"); //$NON-NLS-1$
		add(jtbSpecial,"6,0"); //$NON-NLS-1$
		add(jtbPlay,"8,0"); //$NON-NLS-1$
		add(jtbVolume,"10,0"); //$NON-NLS-1$
		add(jtbPosition,"12,0"); //$NON-NLS-1$
		
		//register to player events
		ObservationManager.register(EVENT_PLAYER_PLAY,this);
		ObservationManager.register(EVENT_PLAYER_STOP,this);
		ObservationManager.register(EVENT_PLAYER_PAUSE,this);
		ObservationManager.register(EVENT_PLAYER_UNPAUSE,this);
		
	}	
	
	
	/** 
	 * Add an history item in the history combo box
	 * @param file
	 */
	public void addHistoryItem(HistoryItem hi){
		String sOut = hi.toString();
		if (sOut == null){
			return;
		}
		jcbHistory.removeActionListener(this); //stop listening this item when manupulating it
		jcbHistory.insertItemAt(sOut,0);
		jcbHistory.setSelectedIndex(0);
		jcbHistory.addActionListener(this);
	}
	
	
	
	
	/**
	 * Clear history bar
	 */
	public void clearHistoryBar(){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jcbHistory.removeAllItems();
			}
		});
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(final ActionEvent ae) {
		if ( ae.getSource() == jcbHistory){
			SwingWorker sw = new SwingWorker() {
				HistoryItem hi = null;
				public Object construct() {
					hi = History.getInstance().getHistoryItem(jcbHistory.getSelectedIndex());
					return null;
				}
				
				public void finished() {
					if (hi != null){
						org.jajuk.base.File file = FileManager.getFile(hi.getFileId());
						if (file!= null && !file.isScanned()){  //file must be on a mounted device not refreshing
							FIFO.getInstance().push(file,false);
						}
						else{
							Messages.showErrorMessage("120",file.getDirectory().getDevice().getName()); //$NON-NLS-1$
							jcbHistory.setSelectedItem(null);
						}
					}	
				}
			};
			sw.start();
			
		}
		if (ae.getSource() == jbGlobalRandom ){
			SwingWorker sw = new SwingWorker() {
				org.jajuk.base.File file = null;
				public Object construct() {
					file = FileManager.getShuffleFile();
					return null;
				}
				
				public void finished() {
					if (file != null){
						FIFO.getInstance().setBestof(false); //break best of mode if set
						FIFO.getInstance().setGlobalRandom(true);
						FIFO.getInstance().push(file,false,true);
					}
				}
			};
			sw.start();
		}
		if (ae.getSource() == jbBestof ){
			SwingWorker sw = new SwingWorker() {
				org.jajuk.base.File file = null;
				public Object construct() {
					file = FileManager.getBestOfFile();
					return null;
				}
				
				public void finished() {
					if (file != null){
						FIFO.getInstance().setGlobalRandom(false); //break global random mode if set
						FIFO.getInstance().setBestof(true);
						FIFO.getInstance().push(file,false,true);
					}
				}
			};
			sw.start();
		}
		else if (ae.getSource() == jbMute ){
			Util.setMute(!Util.getMute());
		}
		else if(ae.getSource() == jbStop){
			FIFO.getInstance().stopRequest();
		}
		else if(ae.getSource() == jbPlayPause){
			FIFO.getInstance().pauseRequest();
		}
		else if (ae.getSource() == jbPrevious){
			FIFO.getInstance().playPrevious();
		}
		else if (ae.getSource() == jbNext){
			FIFO.getInstance().playNext();
		}
		else if (ae.getSource() == jbRew){
			float fCurrentPosition = FIFO.getInstance().getCurrentPosition();
			FIFO.getInstance().setCurrentPosition(fCurrentPosition-JUMP_SIZE);
		}
		else if (ae.getSource() == jbFwd){
			float fCurrentPosition = FIFO.getInstance().getCurrentPosition();
			FIFO.getInstance().setCurrentPosition(fCurrentPosition+JUMP_SIZE);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(final ListSelectionEvent e) {
		SwingWorker sw = new SwingWorker() {
			public Object construct() {
				if (!e.getValueIsAdjusting()){
					SearchResult sr = (SearchResult)sbSearch.alResults.get(sbSearch.jlist.getSelectedIndex());
					FIFO.getInstance().push(sr.getFile(),false);
				}
				return null;
			}
			
			public void finished() {
				if (!e.getValueIsAdjusting()){
					sbSearch.popup.hide();
					requestFocus();	
				}	
			}
		};
		sw.start();
	}
	
	/*
	 *  @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		if ( e.getSource() == jsVolume && !bPositionChanging){
			bPositionChanging = true;
			new Thread(){  //set volume asynchonously and after a delay to take only one value when slider is moved
				public void run(){
					try{
						Thread.sleep(500);
					}
					catch(InterruptedException ie){
						Log.error(ie);
					}
					Util.setVolume((float)jsVolume.getValue()/100);
					bPositionChanging = false;
				}
			}.start();
		}
		else if (e.getSource() == jsPosition && !bPositionChanging){
			bPositionChanging = true;
			new Thread(){  //set position asynchonously and after a delay to take only one value when slider is moved
				public void run(){
					try{
						Thread.sleep(500);
					}
					catch(InterruptedException ie){
						Log.error(ie);
					}
					FIFO.getInstance().setCurrentPosition((float)jsPosition.getValue()/100);
					bPositionChanging = false;
				}
			}.start();
		}
	}
	
	/**
	 * Set Slider position
	 * @param i percentage of slider
	 */
	public void setCurrentPosition(final int i){
		if ( !bPositionChanging ){//don't move slider when user do it himself a	t the same time
			bPositionChanging = true;  //block events so player is not affected
			CommandJPanel.this.jsPosition.setValue(i);
			bPositionChanging = false;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jajuk.ui.Observer#update(java.lang.String)
	 */
	public void update(String subject) {
		if( subject.equals(EVENT_PLAYER_STOP)){
			jbRew.setEnabled(false);
			jbPlayPause.setEnabled(false);
			jbStop.setEnabled(false);
			jbFwd.setEnabled(false);
			jsPosition.setEnabled(false);
		}
		else if ( subject.equals(EVENT_PLAYER_PLAY)){
			jbRew.setEnabled(true);
			jbPlayPause.setEnabled(true);
			jbStop.setEnabled(true);
			jbFwd.setEnabled(true);
			jsPosition.setEnabled(true);
		}
		else if ( subject.equals(EVENT_PLAYER_PAUSE)){
			jbRew.setEnabled(false);
			jbFwd.setEnabled(false);
			jsPosition.setEnabled(false);
			jbPlayPause.setIcon(Util.getIcon(ICON_PLAY));
		}
		else if ( subject.equals(EVENT_PLAYER_UNPAUSE)){
			jbRew.setEnabled(true);
			jbFwd.setEnabled(true);
			jsPosition.setEnabled(true);
			jbPlayPause.setIcon(Util.getIcon(ICON_PAUSE));
		}
	}
	
}
