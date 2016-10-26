/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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

package org.jboss.flicc.processor;

import java.io.Serializable;

/**
 * @param <T> the upper bound of the type type of the key
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
interface Hasher<T> extends Equaller<T> {

    /**
     * Return the hash code of the given object.
     *
     * @param obj the object
     * @return the object's hash code
     */
    int hashCode(T obj);

    /**
     * A hasher which uses the standard {@code equals/hashCode} hashing mechanism.
     */
    Hasher<Object> DEFAULT = new DefaultHasher();

}

final class DefaultHasher extends DefaultEqualler implements Hasher<Object>, Serializable {

    private static final long serialVersionUID = 1382908942098071506L;

    public int hashCode(final Object obj) {
        return obj.hashCode();
    }

    protected Object readResolve() {
        return Hasher.DEFAULT;
    }
}

