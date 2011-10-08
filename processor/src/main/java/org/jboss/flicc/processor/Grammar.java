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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class Grammar {
    private final int lookahead;
    private final String goal;
    private final Map<SymbolSeq, RuleSet> rulesBySymbols = new HashMap<SymbolSeq, RuleSet>();
    private final Map<String, Symbol> symbolsByName = new HashMap<String, Symbol>();

    private static <K, V> List<V> getMapList(Map<K, List<V>> map, K key) {
        List<V> list = map.get(key);
        return list == null ? Collections.<V>emptyList() : list;
    }

    private static <K, V> List<V> addToMapList(Map<K, List<V>> map, K key, V value) {
        List<V> list = map.get(key);
        if (list == null) {
            map.put(key, list = new ArrayList<V>());
        }
        list.add(value);
        return list;
    }

    
}
