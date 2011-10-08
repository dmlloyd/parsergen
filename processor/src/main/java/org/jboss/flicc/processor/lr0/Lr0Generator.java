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

package org.jboss.flicc.processor.lr0;

import org.jboss.flicc.processor.Generator;
import org.jboss.flicc.processor.Grammar;
import org.jboss.flicc.processor.IntMap;
import org.jboss.flicc.processor.Symbol;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class Lr0Generator implements Generator {

    public void generate(final Grammar grammar, final ProcessingEnvironment env) {
        int tid = 0, nid = 0;
        final IntMap<Symbol> terminals = new IntMap<Symbol>();
        final IntMap<Nonterminal> nonterminals = new IntMap<Nonterminal>();
        for (Symbol terminal : grammar.getInputs()) {
            terminals.put(terminal, tid++);
        }
        for (Nonterminal nonterminal : grammar.getProducedBy().keySet()) {
            nonterminals.put(nonterminal, nid++);
        }
    }
}
