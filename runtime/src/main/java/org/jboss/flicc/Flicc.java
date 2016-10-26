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

package org.jboss.flicc;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Static holder class for all Flicc annotation types.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class Flicc {

    private Flicc() {}

    /**
     * A positional parameter indicator annotation.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $ {

        /**
         * The index, counting from 1, of the RHS argument to which this parameter applies.  For patterns,
         * 0 indicates the whole matched text, while 1+ indicate capture group numbers.  Capture groups are not
         * yet supported.
         *
         * @return the index
         */
        int value();
    }

    /**
     * A shorthand positional parameter indicator annotation for position 0.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $0 {}

    /**
     * A shorthand positional parameter indicator annotation for position 1.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $1 {}

    /**
     * A shorthand positional parameter indicator annotation for position 2.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $2 {}

    /**
     * A shorthand positional parameter indicator annotation for position 3.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $3 {}

    /**
     * A shorthand positional parameter indicator annotation for position 4.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $4 {}

    /**
     * A shorthand positional parameter indicator annotation for position 5.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $5 {}

    /**
     * A shorthand positional parameter indicator annotation for position 6.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $6 {}

    /**
     * A shorthand positional parameter indicator annotation for position 7.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $7 {}

    /**
     * A shorthand positional parameter indicator annotation for position 8.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $8 {}

    /**
     * A shorthand positional parameter indicator annotation for position 9.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $9 {}

    /**
     * Define the precedence of a terminal symbol or rule in terms of another terminal symbol.
     */
    @Retention(SOURCE)
    @Target(METHOD)
    public @interface Prec {

        /**
         * The terminal symbol whose precedence should be copied.
         *
         * @return the terminal symbol
         */
        String value();
    }

    /**
     * Match the end of an input stream.
     */
    @Retention(SOURCE)
    @Target(METHOD)
    public @interface EOF {}

    /**
     * A literal string to match.  If no result type is specified, the literal string itself is used.
     */
    @Retention(SOURCE)
    @Target(METHOD)
    public @interface Literal {

        /**
         * The literal string.
         *
         * @return the literal string
         */
        String[] value();
    }

    /**
     * A regular expression pattern to match.
     */
    @Retention(SOURCE)
    @Target(METHOD)
    public @interface Pattern {

        /**
         * The pattern string.
         *
         * @return the pattern string
         */
        String value();
    }

    /**
     * A rule, as a space-separated list of symbols.
     */
    @Retention(SOURCE)
    @Target(METHOD)
    public @interface Rule {

        /**
         * The space-separated list of symbols which this rule accepts.
         *
         * @return the RHS items
         */
        String value();
    }

    /**
     * Declare a constant field to be a scanner state value.
     */
    @Retention(SOURCE)
    @Target(FIELD)
    public @interface State {}

    /**
     * Declare a instance field of type {@code int} to contain the current line number.
     */
    @Retention(SOURCE)
    @Target(FIELD)
    public @interface Line {}

    /**
     * Declare a instance field of type {@code int} to contain the current column number.
     */
    @Retention(SOURCE)
    @Target(FIELD)
    public @interface Column {}

    /**
     * Declare a instance field of type {@code String} to contain the current file name.
     */
    @Retention(SOURCE)
    @Target(FIELD)
    public @interface File {}

    /**
     * Declare an abstract method to be a start method.  The following parameters are supported (the names must match
     * exactly):
     * <ul>
     *   <li>{@code String fileName} or {@code File file} - declare a file name to start processing of</li>
     *   <li>{@code InputString stream} or {@code Reader reader} - declare a stream to start processing</li>
     *   <li>{@code int line} - specify the line number to set</li>
     * </ul>
     */
    @Retention(SOURCE)
    @Target(METHOD)
    public @interface Start {}

    @Retention(SOURCE)
    @Target(METHOD)
    public @interface Push {}

    @Retention(SOURCE)
    @Target(METHOD)
    public @interface Pop {}

    /**
     * Declare the parse method.  Attach to an abstract method which returns the given result of the given accept rule.
     */
    @Retention(SOURCE)
    @Target(METHOD)
    public @interface Parse {

        /**
         * The name of the accept rule.
         *
         * @return the name of the accept rule
         */
        String value();
    }

    /**
     * Define the states in which a pattern is valid.
     */
    @Retention(SOURCE)
    @Target(METHOD)
    public @interface IncludeState {

        /**
         * The states in which this pattern is valid.
         *
         * @return the states
         */
        int[] value();
    }

    /**
     * Define the states in which a pattern is <b>not</b> valid.
     */
    @Retention(SOURCE)
    @Target(METHOD)
    public @interface ExcludeState {

        /**
         * The states in which this pattern is <b>not</b> valid.
         *
         * @return the states
         */
        int[] value();
    }

    /**
     * The result of this rule or pattern.
     */
    @Retention(SOURCE)
    @Target(METHOD)
    @Inherited
    public @interface __ {

        /**
         * The result string which should be a token or non-terminal name.
         *
         * @return the result string
         */
        String value();
    }

    /**
     * Define associativity of a terminal symbol as a binary operator.
     */
    public @interface Assoc {

        /**
         * The associativity type.
         *
         * @return the associativity type
         */
        AssocType value();
    }

    /**
     * Left-associativity.
     */
    public static final AssocType LEFT = AssocType.LEFT;
    /**
     * Right-associativity.
     */
    public static final AssocType RIGHT = AssocType.RIGHT;
    /**
     * Non-associativity.
     */
    public static final AssocType NONE = AssocType.NONE;

    /**
     * Associativity type.
     */
    public enum AssocType {
        /**
         * Left-associativity.
         */
        LEFT,
        /**
         * Right-associativity.
         */
        RIGHT,
        /**
         * Non-associativity.
         */
        NONE,
    }

    /**
     * Declare a class to be a LR(k) grammar.
     */
    @Retention(SOURCE)
    @Target(TYPE)
    @Inherited
    public @interface LR {

        /**
         * The number of tokens of look-ahead.
         *
         * @return the look-ahead length, usually 0 or 1.
         */
        int value() default 1;
    }
}
