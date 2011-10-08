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

    private Flicc() {
    }

    /**
     * Get a parser instance.  Returned instances are not thread-safe.
     *
     * @param type the type upon which the parser annotations were specified
     * @param handler the parse handler instance
     * @param <T> the parser type
     * @return the parser instance
     */
    public static <T> Parser getParser(Class<T> type, T handler) {
        if (type == null) {
            throw new IllegalArgumentException("type is null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler is null");
        }
        try {
            return Class.forName(type.getName() + "$$flicc_generated", true, type.getClassLoader()).asSubclass(Parser.class).getConstructor(type).newInstance(handler);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to construct parser " + type + " with handler " + handler, e);
        }
    }

    /**
     * Get a scanner instance.  Returned instances are not thread-safe.
     *
     * @param type the type upon which the scanner annotations were specified
     * @param handler the scanner handler instance
     * @param <T> the scanner type
     * @return the scanner instance
     */
    public static <T> Scanner getScanner(Class<T> type, T handler) {
        if (type == null) {
            throw new IllegalArgumentException("type is null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler is null");
        }
        try {
            return Class.forName(type.getName() + "$$flicc_generated", true, type.getClassLoader()).asSubclass(Scanner.class).getConstructor(type).newInstance(handler);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to construct parser " + type + " with handler " + handler, e);
        }
    }

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
    public @interface $0 {
    }

    /**
     * A shorthand positional parameter indicator annotation for position 1.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $1 {
    }

    /**
     * A shorthand positional parameter indicator annotation for position 2.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $2 {
    }

    /**
     * A shorthand positional parameter indicator annotation for position 3.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $3 {
    }

    /**
     * A shorthand positional parameter indicator annotation for position 4.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $4 {
    }

    /**
     * A shorthand positional parameter indicator annotation for position 5.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $5 {
    }

    /**
     * A shorthand positional parameter indicator annotation for position 6.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $6 {
    }

    /**
     * A shorthand positional parameter indicator annotation for position 7.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $7 {
    }

    /**
     * A shorthand positional parameter indicator annotation for position 8.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $8 {
    }

    /**
     * A shorthand positional parameter indicator annotation for position 9.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    public @interface $9 {
    }

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
    public @interface EOF {
    }

    /**
     * A literal string to match.  If no result type is specified, the literal string itself is used.
     */
    @Retention(SOURCE)
    @Target(METHOD)
    public @interface $L {

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
    public @interface $P {

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
    public @interface $R {

        /**
         * The space-separated list of symbols which this rule accepts.
         *
         * @return the RHS items
         */
        String value();
    }

    /**
     * Define the states in which a pattern is valid.
     */
    @Retention(SOURCE)
    @Target(METHOD)
    public @interface $S {

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
    public @interface $X {

        /**
         * The states in which this pattern is <b>not</b> valid.
         *
         * @return the states
         */
        int[] value();
    }

    /**
     * The result of this rule, pattern, or grammar.
     */
    @Retention(SOURCE)
    @Target({METHOD, TYPE})
    @Inherited
    public @interface $$ {

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
    public @interface $A {

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
