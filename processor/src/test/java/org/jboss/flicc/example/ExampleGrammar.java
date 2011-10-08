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

package org.jboss.flicc.example;

import org.jboss.flicc.Scanner;

import static org.jboss.flicc.Flicc.*;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
@SuppressWarnings("JavaDoc")
@LR(0)
@$$("Program")
public final class ExampleGrammar {
    // states
    private static final int INITIAL = 0;
    private static final int COMMENT = 1;

    @$P("[ \t\r\f]") @$S(INITIAL)
    public void ignoreWS() {}

    @$L("/*") @$S(INITIAL)
    public void startComment(Scanner scanner) {
        scanner.pushState(COMMENT);
    }

    @$P(".") @$S(COMMENT)
    public void ignoreComments() {}

    @$L("*/") @$S(COMMENT)
    public void endComment(Scanner scanner) {
        scanner.popState();
    }

    @$L("*") @$A(LEFT) @$S(INITIAL)
    public void and() {
    }

    @$L("+") @$A(LEFT) @$S(INITIAL)
    public void or() {
    }

    @$P(".") @$S(INITIAL)
    public void invalidChar(Scanner scanner) {
        // report error
    }

    @$$("\n") @$S(INITIAL)
    public void eol() {}

    @$$("Literal") @$P("[01]")
    public Literal literal(@$0 char text) {
        return Literal.valueOf(text == '1');
    }

    @$$("Expr") @$R("Literal")
    public Expr literalExpr(@$1 Literal literal) {
        return literal;
    }

    @$$("Expr") @$R("Expr * Literal")
    public Expr andExpr(@$1 Expr left, @$3 Literal right) {
        return new AndExpr(left, right);
    }

    @$$("Expr") @$R("Expr + Literal")
    public Expr orExpr(@$1 Expr left, @$3 Literal right) {
        return new OrExpr(left, right);
    }

    @$$("Line") @$R("\n")
    public void emptyLine() {}

    @$$("Line") @$R("Expr \n")
    public void exprLine(@$1 Expr expr) {
        System.out.println("The answer is: " + expr.getValue());
    }

    @$$("Program") @$R("Program Line")
    public void program1() {
    }

    @$$("Program") @$R("")
    public void program2() {
        System.out.println("No input!");
    }
}
