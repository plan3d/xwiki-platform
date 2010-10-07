/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.rendering.internal.renderer.xwiki20.link;

import org.apache.commons.lang.StringUtils;
import org.xwiki.component.annotation.Component;
import org.xwiki.rendering.internal.parser.reference.XWiki20ResourceReferenceParser;
import org.xwiki.rendering.listener.DocumentResourceReference;
import org.xwiki.rendering.listener.ResourceReference;
import org.xwiki.rendering.renderer.link.LinkTypeReferenceSerializer;

/**
 * Serialize a link reference pointing to a document using the format
 * {@code (document reference)(#anchor)(?query string)}.
 *  
 * @version $Id$
 * @since 2.5M2
 */
@Component("xwiki/2.0/doc")
public class DocumentLinkTypeReferenceSerializer implements LinkTypeReferenceSerializer
{
    /**
     * Escapes to add when rendering a link reference part.
     */
    private static final String[] ESCAPE_REPLACEMENTS_REFERENCE = new String[]{
        XWiki20ResourceReferenceParser.ESCAPE_CHAR + XWiki20ResourceReferenceParser.SEPARATOR_QUERYSTRING,
        XWiki20ResourceReferenceParser.ESCAPE_CHAR + XWiki20ResourceReferenceParser.SEPARATOR_INTERWIKI,
        XWiki20ResourceReferenceParser.ESCAPE_CHAR + XWiki20ResourceReferenceParser.SEPARATOR_ANCHOR };

    /**
     * Replacement chars for the escapes to add to the reference part.
     */
    private static final String[] ESCAPES_REFERENCE = new String[]{
        XWiki20ResourceReferenceParser.SEPARATOR_QUERYSTRING,
        XWiki20ResourceReferenceParser.SEPARATOR_INTERWIKI,
        XWiki20ResourceReferenceParser.SEPARATOR_ANCHOR };

    /**
     * Escapes to add when rendering a link query string, anchor or interwiki part.
     */
    private static final String[] ESCAPE_REPLACEMENTS_EXTRA = new String[]{
        XWiki20ResourceReferenceParser.ESCAPE_CHAR + XWiki20ResourceReferenceParser.SEPARATOR_QUERYSTRING,
        XWiki20ResourceReferenceParser.ESCAPE_CHAR + XWiki20ResourceReferenceParser.SEPARATOR_INTERWIKI,
        XWiki20ResourceReferenceParser.ESCAPE_CHAR + XWiki20ResourceReferenceParser.SEPARATOR_ANCHOR,
        "" + XWiki20ResourceReferenceParser.ESCAPE_CHAR + XWiki20ResourceReferenceParser.ESCAPE_CHAR };

    /**
     * Replacement chars for the escapes to add to the query string, anchor or interwiki part.
     */
    private static final String[] ESCAPES_EXTRA = new String[]{
        XWiki20ResourceReferenceParser.SEPARATOR_QUERYSTRING,
        XWiki20ResourceReferenceParser.SEPARATOR_INTERWIKI,
        XWiki20ResourceReferenceParser.SEPARATOR_ANCHOR,
        "" + XWiki20ResourceReferenceParser.ESCAPE_CHAR };

    /**
     * {@inheritDoc}
     *
     * @see LinkTypeReferenceSerializer#serialize(org.xwiki.rendering.listener.ResourceReference)
     */
    public String serialize(ResourceReference reference)
    {
        StringBuilder buffer = new StringBuilder();

        if (reference.getReference() != null) {
            // Make sure we escape special chars: # and ? as they have special meaning in links, but only for
            // links to documents. Also escape \ since it's the escape char.
            String normalizedReference = addEscapesToReferencePart(reference.getReference());
            buffer.append(normalizedReference);
        }

        String anchor = reference.getParameter(DocumentResourceReference.ANCHOR);
        if (anchor != null) {
            buffer.append('#');
            buffer.append(addEscapesToExtraParts(anchor));
        }
        String queryString = reference.getParameter(DocumentResourceReference.QUERY_STRING);
        if (queryString != null) {
            buffer.append('?');
            buffer.append(addEscapesToExtraParts(queryString));
        }

        return buffer.toString();
    }

    /**
     * @param text the reference to which to add escapes to
     * @return the modified text
     */
    protected String addEscapesToReferencePart(String text)
    {
        return StringUtils.replaceEach(text, ESCAPES_REFERENCE, ESCAPE_REPLACEMENTS_REFERENCE);
    }

    /**
     * @param text the query string and anchor parts to which to add escapes to
     * @return the modified text
     */
    protected String addEscapesToExtraParts(String text)
    {
        return StringUtils.replaceEach(text, ESCAPES_EXTRA, ESCAPE_REPLACEMENTS_EXTRA);
    }
}
