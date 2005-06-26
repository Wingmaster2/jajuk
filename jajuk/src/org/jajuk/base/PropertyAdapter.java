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

import java.io.Serializable;
import java.util.Iterator;

import org.jajuk.util.ITechnicalStrings;
import org.jajuk.util.SequentialMap;
import org.jajuk.util.Util;
import org.xml.sax.Attributes;

/**
 * Generic property handler
 * @author Bertrand Florat 
 * @created 17 oct. 2003
 */
abstract public class PropertyAdapter implements IPropertyable, ITechnicalStrings,Serializable {
	
	/** Item properties, singleton */
	private SequentialMap properties;
    
	/** ID. Ex:1,2,3... */
    protected String sId;
    /** Name */
    protected String sName;
    
    /**
     * Constructor
     * @param sId element ID
     * @param sName element name
     */
    PropertyAdapter(String sId,String sName){
        setId(sId);
        setName(sName);
    }
    
    /**
     * @return
     */
    public String getId() {
        return sId;
    }
    
    /**
     * @return
     */
    public String getName() {
        return sName;
    }
    
    /*
	 * (non-Javadoc)
	 * 
	 * @see org.jajuk.base.Propertyable#getProperties()
	 */
	public SequentialMap getProperties() {
		if ( properties == null){
			properties = new SequentialMap();
		}
		return properties;
	}
	
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jajuk.base.Propertyable#getProperty(java.lang.String)
	 */
	public String getValue(String sKey) {
		if ( sKey == null ){
			return null;
		}
		//get property singleton
        SequentialMap properties = getProperties();
		//must be a property
		if ( !properties.containsKey(sKey)){ //no more? return null
			return null;
		}
		return (String) properties.get(sKey); //return property value
	}
	
     
    
    /*
     * (non-Javadoc)
     * 
     * @see org.jajuk.base.Propertyable#containsKey(java.lang.String)
     */
    public boolean containsProperty(String sKey) {
        return properties.containsKey(sKey) 
            && properties.get(sKey) != null 
            && !properties.get(sKey).equals(""); //$NON-NLS-1$
    }
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jajuk.base.Propertyable#setProperty(java.lang.String, java.lang.String)
	 */
	public void setProperty(String sKey, String sValue) {
	    SequentialMap properties = getProperties();
		properties.put(sKey, sValue);
	}
	
	public void setDefaultProperty(String sKey, String sValue) {
        SequentialMap properties = getProperties();
		if ( properties.containsKey(sKey)){
			return;
		}
		setProperty(sKey,sValue);
	}
	
	
	
	/**
     * Return an XML representation of this item  
     * @return
     */
    public String toXml() {
        StringBuffer sb = new StringBuffer("\t\t<").append(getIdentifier()); //$NON-NLS-1$
        sb.append(getPropertiesXml());
        sb.append("/>\n"); //$NON-NLS-1$
        return sb.toString();
    }
    
    private String getPropertiesXml() {
        SequentialMap properties = getProperties();
		Iterator it = properties.keys().iterator();
     	StringBuffer sb = new StringBuffer(); //$NON-NLS-1$
		while (it.hasNext()) {
			String sKey = (String) it.next();
			String sValue = Util.formatXML(properties.getProperty(sKey));
			sb.append(" "+sKey + "='" + sValue + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return sb.toString();
	}
	
/**
     * Set all personnal properties of an XML file for an item
     * 
     * @param attributes :
     *                list of attributes for this XML item
     * @param index :
     *                index of the first non-standard attribute
     */
    public void populateProperties(Attributes attributes, int index) {
        if (attributes.getLength() >= index) { //found some properties
            for (int i = index; i < attributes.getLength(); i++) {
                setProperty(attributes.getQName(i), attributes.getValue(i));
            }
        }
    }
	
	/**
	 * @param properties The properties to set.
	 */
	public void setProperties(SequentialMap properties) {
		this.properties = properties;
	}
	
	/* (non-Javadoc)
	 * @see org.jajuk.base.IPropertyable#removeProperty(java.lang.String)
	 */
	public void removeProperty(String sKey) {
        SequentialMap properties = getProperties();
		if (properties.containsKey(sKey)){
			properties.remove(sKey);
        }
	}
	
	/* (non-Javadoc)
	 * @see org.jajuk.base.IPropertyable#displayProperty()
	 */
	public void displayProperties() {
	}
    
 /**
     * @param id The sId to set.
     */
    protected void setId(String id) {
        sId = id;
        setProperty(XML_ID,id);
    }

    /**
     * @param name The sName to set.
     */
    protected void setName(String name) {
        sName = name;
        setProperty(XML_NAME,name);
    }
	
    /* (non-Javadoc)
     * @see org.jajuk.base.IPropertyable#isPropertyEditable()
     */
    abstract public boolean isPropertyEditable(String sProperty);
	
    /**
     * Default implementation for this method, simply return standard value
     */
    public String getHumanValue(String sKey){
        return getValue(sKey);
    }
    
}
