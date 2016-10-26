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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class State {

    static final Indexer<GotoCondition, GotoCondition> INDEXER = new Indexer<GotoCondition, GotoCondition>() {
        public boolean accepts(final Object obj) {
            return obj instanceof GotoCondition;
        }

        public GotoCondition getKey(final GotoCondition value) {
            return value;
        }
    };

    static final Hasher<GotoCondition> HASHER = new Hasher<GotoCondition>() {
        public int hashCode(final GotoCondition obj) {
            return Arrays.hashCode(obj.terminals) * 13 + obj.nonterminal;
        }

        public boolean accepts(final Object obj) {
            return obj instanceof GotoCondition;
        }

        public boolean equals(final GotoCondition obj, final GotoCondition other) {
            return obj.nonterminal == other.nonterminal && Arrays.equals(obj.terminals, other.terminals);
        }
    };

    private final int id;
    // map of actions to take: shift, reduce, or error if missing
    // sequence can only contain terminals
    // sequence length is always equal to lookahead length; 1 for LR(0)
    // map of states to go to if returned to by a reduce, or error if missing
    // LR(0) and LR(1) parsers will only have Nonterminals for keys
    private final Map<Symbol, State> gotos = new HashMap<Symbol, State>();
    // A mapping of all possible terminal sequences which produce a given action.
    // Map<Action, List<SymbolSeq>> actions = ...

    // key = terminal
    private final Action[] actions;
    private final IndexMap<GotoCondition, GotoCondition> gotoConditions = new IndexHashMap<GotoCondition, GotoCondition>(INDEXER, HASHER, Equaller.DEFAULT);

    /**
     *
     * @param id the identifier of this state
     * @param cnt the # of possible terminal symbols
     */
    public State(final int id, final int cnt) {
        this.id = id;
        actions = new Action[cnt];
    }

    public int getId() {
        return id;
    }

    public static class GotoCondition {
        private final int nonterminal;
        private final int[] terminals;
        private final int targetState;

        /**
         * Construct a new instance.
         *
         * @param nonterminal the nonterminal to match
         * @param terminals the sequence of lookahead terminals which satisfy the condition
         * @param targetState the target state
         */
        public GotoCondition(final int nonterminal, final int[] terminals, final int targetState) {
            this.nonterminal = nonterminal;
            this.terminals = terminals;
            this.targetState = targetState;
        }

        public int hashCode() {
            return (Arrays.hashCode(terminals) * 13 + nonterminal) * 13 + targetState;
        }

        public boolean equals(final Object obj) {
            return obj instanceof GotoCondition && equals((GotoCondition) obj);
        }

        public boolean equals(final GotoCondition obj) {
            return this == obj || obj != null && nonterminal == obj.nonterminal && targetState == obj.targetState && Arrays.equals(terminals, obj.terminals);
        }
    }

    public abstract static class Action {
    }

    public static final class Shift {
        private final State nextState;

        public Shift(final State nextState) {
            this.nextState = nextState;
        }

        public State getNextState() {
            return nextState;
        }
    }

    public static final class Reduce {
        private final Rule rule;

        public Reduce(final Rule rule) {
            this.rule = rule;
        }

        public Rule getRule() {
            return rule;
        }
    }

    public Map<Symbol, State> getGotos() {
        return gotos;
    }
}
