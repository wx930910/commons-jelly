/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//jelly/src/java/org/apache/commons/jelly/TagSupport.java,v 1.2 2002/04/24 11:59:12 jstrachan Exp $
 * $Revision: 1.2 $
 * $Date: 2002/04/24 11:59:12 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2002 The Apache Software Foundation.  All rights
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
 * $Id: TagSupport.java,v 1.2 2002/04/24 11:59:12 jstrachan Exp $
 */
package org.apache.commons.jelly;

import java.io.StringWriter;
import java.io.Writer;

/** <p><code>TagSupport</code> an abstract base class which is useful to 
  * inherit from if developing your own tag.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 1.2 $
  */
public abstract class TagSupport implements Tag {

    /** the parent of this tag */
    private Tag parent;
    
    /** the body of the tag */
    private Script body;

    /** 
     * Searches up the parent hierarchy from the given tag 
     * for a Tag of the given type 
     *
     * @param from the tag to start searching from
     * @param tagClass the type of the tag to find
     * @return the tag of the given type or null if it could not be found
     */
    public static Tag findAncestorWithClass(Tag from, Class tagClass) {
        while ( from != null ) {
            if ( tagClass.isInstance( from ) ) {
                return from;
            }
            from = from.getParent();
        }
        return null;
    }

    /** @return the parent of this tag */
    public Tag getParent() {
        return parent;
    }
    
    /** Sets the parent of this tag */
    public void setParent(Tag parent) {
        this.parent = parent;
    }

    /** @return the body of the tag */
    public Script getBody() {
        return body;
    }
    
    /** Sets the body of the tag */
    public void setBody(Script body) {
        this.body = body; 
    }
    
    // Implementation methods
    //-------------------------------------------------------------------------                
    
    /** 
     * Searches up the parent hierarchy for a Tag of the given type 
     * @return the tag of the given type or null if it could not be found
     */
    protected Tag findAncestorWithClass(Class parentClass) {
        return findAncestorWithClass( getParent(), parentClass );
    }

    /**
     * Evaluates the given body using a buffer and returns the String 
     * of the result.
     *
     * @return the text evaluation of the body
     */
    protected String getBodyText( Context context ) throws Exception {
        // XXX: could maybe optimise this later on by having a pool of buffers
        StringWriter writer = new StringWriter();
        body.run( context, XMLOutput.createXMLOutput( writer ) );
        return writer.toString();
    }
    
}
