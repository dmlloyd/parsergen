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
public interface ExampleGrammarInterface {
    int COMMENT = 0;

    @$P("[ \t\r\f]")
    void ignoreWS();

    @$L("/*")
    void startComment(Scanner scanner);

    @$P(".") @$S(COMMENT)
    void ignoreComments();

    @$L("*/") @$S(COMMENT)
    void endComment(Scanner scanner);

    @$$("BINARY") @$A(LEFT)
    void binary();

    @$$("*") @$A(LEFT)
    void and();

    @$$("+") @$A(LEFT)
    void or();

    @$$("\n")
    void eol();

    @$$("Literal") @$P("[01]")
    Literal literal(@$0 char text);

    @$$("Expr") @$R("Literal")
    Expr literalExpr(@$1 Literal literal);

    @$$("Expr") @$R("Expr * Literal")
    Expr andExpr(@$1 Expr left, @$3 Literal right);

    @$$("Expr") @$R("Expr + Literal")
    Expr orExpr(@$1 Expr left, @$3 Literal right);

    @$$("Line") @$R("\n")
    void emptyLine();

    @$$("Line") @$R("Expr \n")
    void exprLine(@$1 Expr expr);

    @$$("Program") @$R("Program Line")
    void program1();

    @$$("Program") @$R("")
    void program2();
}
