/*
 * $Header: /home/cvs/jakarta-commons-sandbox/jelly/src/java/org/apache/commons/jelly/tags/define/DynamicTag.java,v 1.10 2002/07/10 21:46:18 jvanzyl Exp $
 * $Revision: 1.10 $
 * $Date: 2002/07/10 21:46:18 $
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
 * $Id: DynamicTag.java,v 1.10 2002/07/10 21:46:18 jvanzyl Exp $
 */
package org.apache.commons.jelly.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import org.apache.commons.jelly.DynaTagSupport;
import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * <p><code>DynamicTag</code> is a tag that is created from
 * inside a Jelly script as a Jelly template and will invoke a 
 * given script, passing in its instantiation attributes 
 * as variables and will allow the template to invoke its instance body.</p>
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @version $Revision: 1.10 $
 */
public class DynamicTag extends DynaTagSupport {

    /** The Log to which logging calls will be made. */
    private static final Log log = LogFactory.getLog(DynamicTag.class);

    /** The template script */
    private Script template;

    /** The instance attributes */
    private Map attributes = new HashMap();

    public DynamicTag() {
    }

    public DynamicTag(Script template) {
        this.template = template;
    }


    // Tag interface
    //-------------------------------------------------------------------------                    
    public void doTag(XMLOutput output) throws JellyTagException {
        if ( log.isDebugEnabled() ) {
            log.debug("Invoking dynamic tag with attributes: " + attributes);
        }
        attributes.put("org.apache.commons.jelly.body", getBody());
        
        // create new context based on current attributes
        JellyContext newJellyContext = context.newJellyContext(attributes);
        Map attrMap = new HashMap();
        for ( Iterator keyIter = this.attributes.keySet().iterator();
              keyIter.hasNext();) {
            String key = (String) keyIter.next();
            if ( key.endsWith( "Attr" ) ) {
                Object value = this.attributes.get( key );
                attrMap.put( key, value );
                attrMap.put( key.substring( 0, key.length()-4 ), value );
            }  
        }
        newJellyContext.setVariable( "attrs", attrMap );
        getTemplate().run(newJellyContext, output);
    }

    // DynaTag interface
    //-------------------------------------------------------------------------                    
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
        attributes.put(name + "Attr", value);
    }

    // Properties
    //-------------------------------------------------------------------------                    
    /** The template to be executed by this tag which may well 
     * invoke this instances body from inside the template
     */
    public Script getTemplate() {
        return template;
    }

    public void setTemplate(Script template) {
        this.template = template;
    }
}
