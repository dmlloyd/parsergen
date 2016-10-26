/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.flicc.example;

import java.io.IOException;
import java.io.Reader;
import org.jboss.flicc.Flicc;

import static org.jboss.flicc.Flicc.*;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
@SuppressWarnings("JavaDoc")
@LR(0)
public abstract class ExampleGrammar {
    // states
    @State
    private static final int INITIAL = 0;
    @State
    private static final int COMMENT = 1;

    @Line
    protected int line;
    @File
    protected String fileName;

    @Pop
    protected abstract void popState();

    @Push
    protected abstract void pushState(int state);

    @Start
    public abstract void start(String fileName, Reader reader);

    @Parse("Program")
    public abstract void parse() throws IOException;

    @Pattern("[ \t\r\f]") @IncludeState(INITIAL)
    protected abstract void ignoreWS();

    @Flicc.Literal("/*") @IncludeState(INITIAL)
    protected void startComment() {
        pushState(COMMENT);
    }

    @Pattern(".") @IncludeState(COMMENT)
    protected abstract void ignoreComments();

    @Flicc.Literal("*/") @IncludeState(COMMENT)
    protected void endComment() {
        popState();
    }

    @Flicc.Literal("*") @Assoc(LEFT) @IncludeState(INITIAL)
    protected abstract void and();

    @Flicc.Literal("+") @Assoc(LEFT) @IncludeState(INITIAL)
    protected abstract void or();

    @Pattern(".") @IncludeState(INITIAL)
    protected void invalidChar() {
        // report error
    }

    @Flicc.Literal("\n") @IncludeState(INITIAL)
    protected abstract void eol();

    @__("Literal") @Pattern("[01]")
    protected Literal literal(@$0 char text) {
        return Literal.valueOf(text == '1');
    }

    @__("Expr") @Rule("Literal")
    protected abstract Literal literalExpr(@$1 Literal literal);

    @__("Expr") @Rule("Expr * Literal")
    protected abstract AndExpr andExpr(@$1 Expr left, @$3 Literal right);

    @__("Expr") @Rule("Expr + Literal")
    protected abstract OrExpr orExpr(@$1 Expr left, @$3 Literal right);

    @__("Line") @Rule("\n")
    protected abstract void emptyLine();

    @__("Line") @Rule("Expr \n")
    protected void exprLine(@$1 Expr expr) {
        System.out.println("The answer is: " + expr.getValue());
    }

    @__("Program") @Rule("Program Line")
    protected abstract void program1();

    @__("Program") @Rule("")
    protected void program2() {
        System.out.println("No input!");
    }
}
