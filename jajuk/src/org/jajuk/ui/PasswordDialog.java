/*
 *  Jajuk
 *  Copyright (C) 2004 bflorat
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import org.jajuk.Main;
import org.jajuk.i18n.Messages;

/**
 *  Type description
 *
 * @author     Bertrand Florat
 * @created    2 nov. 2005
 */
public class PasswordDialog extends JDialog implements ActionListener{
    JPasswordField pf;
    JOptionPane optionPane;
    
    public PasswordDialog(String sMessage){
        super(Main.getWindow(), true);
        setTitle(Messages.getString("DownloadManager.1"));
        pf = new JPasswordField(20);
        //Create the JOptionPane.
        optionPane = new JOptionPane(new Object[]{Messages.getString("DownloadManager.0"),pf},
                                    JOptionPane.QUESTION_MESSAGE,
                                    JOptionPane.OK_CANCEL_OPTION);
        optionPane.addPropertyChangeListener(
                new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent e) {
                        String prop = e.getPropertyName();
                        if (prop.equals(JOptionPane.VALUE_PROPERTY)) {
                            String sPwd = new String(pf.getPassword());
                            if (sPwd == null || sPwd.trim().equals("")){
                                sPwd = "NOP";//set a string to password to avoid reasking
                            }
                            optionPane.setValue(sPwd);
                            dispose();
                        }
                    }
                });

        //Make this dialog display it.
        setContentPane(optionPane);
        
        //Handle window closing correctly.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
         //Register an event handler that puts the text into the option pane.
        pf.addActionListener(this);
        
        //Ensure the text field always gets the first focus.
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
                pf.requestFocusInWindow();
            }
        });
        setLocation(Main.getWindow().getX()+Main.getWindow().getWidth()/3,Main.getWindow().getY()+Main.getWindow().getHeight()/3);
        pack();
        setVisible(true);
    }

    /** This method handles events for the text field. */
    public void actionPerformed(ActionEvent e) {
        optionPane.setValue(new String(pf.getPassword()));
        dispose();
    }
    
    public JOptionPane getOptionPane(){
        return optionPane;
    }
    

}
