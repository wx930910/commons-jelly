/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//jelly/jelly-tags/jms/src/java/org/apache/commons/jelly/tags/jms/MapMessageTag.java,v 1.2 2003/01/26 06:24:47 morgand Exp $
 * $Revision: 1.2 $
 * $Date: 2003/01/26 06:24:47 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 * 
 * $Id: MapMessageTag.java,v 1.2 2003/01/26 06:24:47 morgand Exp $
 */
package org.apache.commons.jelly.tags.jms;

import java.util.Iterator;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MapMessage;
import javax.jms.JMSException;

import org.apache.commons.jelly.JellyTagException;

/** Creates a JMS MapMessage
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 1.2 $
  */
public class MapMessageTag extends MessageTag {

    public MapMessageTag() {
    }

    public void addEntry(String name, Object value) throws JellyTagException {
        MapMessage message = (MapMessage) getMessage();
        try {
            message.setObject(name, value);
        } 
        catch (JMSException e) {
            throw new JellyTagException(e);
        }
    }
    
    // Properties
    //-------------------------------------------------------------------------                                
    
    /**
     * Sets the Map of entries to be used for this Map Message
     */
    public void setMap(Map map) throws JellyTagException {
        MapMessage message = (MapMessage) getMessage();
        for (Iterator iter = map.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            String name = entry.getKey().toString();
            Object value = entry.getValue();
            
            try {
                message.setObject(name, value);
            } 
            catch (JMSException e) {
                throw new JellyTagException(e);
            }
        }
    }
    
    // Implementation methods
    //-------------------------------------------------------------------------                            
    protected Message createMessage() throws JellyTagException {
        try {
            return getConnection().createMapMessage();
        } catch (JMSException e) {
            throw new JellyTagException(e);
        }
    }    
}    
