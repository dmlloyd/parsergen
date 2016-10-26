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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jboss.flicc.Flicc;

import javax.annotation.processing.Completion;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import static javax.tools.Diagnostic.Kind.*;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class Processor implements javax.annotation.processing.Processor {

    private volatile ProcessingEnvironment processingEnv;

    public Set<String> getSupportedOptions() {
        return Collections.emptySet();
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_6;
    }

    public void init(final ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    public Iterable<? extends Completion> getCompletions(final Element element, final AnnotationMirror annotation, final ExecutableElement member, final String userText) {
        return Collections.emptySet();
    }

    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<String>(Arrays.asList(
            Flicc.LR.class.getName().replace('$', '.')
        ));
    }

    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Flicc.LR.class);
        for (Element element : elements) {
            processGrammar(element);
        }
        return true;
    }

    private void processGrammar(final Element element) {
        // iterate the methods
        final Flicc.LR lrAnnotation = element.getAnnotation(Flicc.LR.class);
        if (lrAnnotation == null) {
            processingEnv.getMessager().printMessage(ERROR, "Missing @LR() annotation", element);
            return;
        }
        final int lr = lrAnnotation.value();
        final Flicc.__ goalAnnotation = element.getAnnotation(Flicc.__.class);
        if (goalAnnotation == null) {
            processingEnv.getMessager().printMessage(ERROR, "Missing @_() (goal) annotation", element);
            return;
        }
        final String goal = goalAnnotation.value();
        processingEnv.getMessager().printMessage(NOTE, "Processing grammar for class '" + element.getSimpleName() + "' as LR(" + lr + ") goal '" + goal + "'", element);
        if (! (element instanceof TypeElement)) {
            processingEnv.getMessager().printMessage(ERROR, "Element is not a type", element);
            return;
        }
        TypeElement typeElement = (TypeElement) element;
        List<? extends Element> members = processingEnv.getElementUtils().getAllMembers(typeElement);
        Map<String, Symbol> symbolMap = new HashMap<String, Symbol>();
        for (Element member : ElementFilter.methodsIn(members)) {
            if (member instanceof ExecutableElement) {
                final ExecutableElement executableElement = (ExecutableElement) member;
                Element enclosingElement = executableElement.getEnclosingElement();
                if (enclosingElement instanceof TypeElement) {
                    TypeElement enclosingTypeElement = (TypeElement) enclosingElement;
                    if (enclosingTypeElement.getQualifiedName().contentEquals(Object.class.getName())) {
                        continue;
                    }
                }
                final Flicc.__ resultAnnotation = executableElement.getAnnotation(Flicc.__.class);
                String result = resultAnnotation == null ? null : resultAnnotation.value();
                final Flicc.Pattern patternAnnotation = executableElement.getAnnotation(Flicc.Pattern.class);
                final Flicc.Literal literalAnnotation = executableElement.getAnnotation(Flicc.Literal.class);
                final Flicc.IncludeState stateAnnotation = executableElement.getAnnotation(Flicc.IncludeState.class);
                final Flicc.Rule ruleAnnotation = executableElement.getAnnotation(Flicc.Rule.class);
                final Flicc.ExcludeState antiStateAnnotation = executableElement.getAnnotation(Flicc.ExcludeState.class);
                final Flicc.Assoc associativityAnnotation = executableElement.getAnnotation(Flicc.Assoc.class);
                if (ruleAnnotation != null) {
                    // Definitely a rule
                    if (patternAnnotation != null) {
                        processingEnv.getMessager().printMessage(ERROR, "Method cannot have a rule ($R) and a text pattern ($P) at the same time", executableElement);
                        // but keep going, treating it as a rule
                    }
                    if (literalAnnotation != null) {
                        processingEnv.getMessager().printMessage(ERROR, "Method cannot have a rule ($R) and a text literal ($L) at the same time", executableElement);
                        // but keep going, treating it as a rule
                    }
                    if (stateAnnotation != null) {
                        processingEnv.getMessager().printMessage(ERROR, "Method cannot have a rule ($R) and a state ($S) at the same time", executableElement);
                        // but keep going, treating it as a rule
                    }
                    if (antiStateAnnotation != null) {
                        processingEnv.getMessager().printMessage(ERROR, "Method cannot have a rule ($R) and an anti-state ($X) at the same time", executableElement);
                        // but keep going, treating it as a rule
                    }
                    if (resultAnnotation == null) {
                        processingEnv.getMessager().printMessage(ERROR, "Rule ($R) method must have a result ($$)", executableElement);
                        continue;
                    }
                    final Flicc.AssocType assocType = associativityAnnotation == null ? Flicc.AssocType.NONE : associativityAnnotation.value();
                    // it's a rule
                    Symbol symbol = symbolMap.get(result);
                    Nonterminal nonterminal;
                    if (symbol != null) {
                        if (symbol instanceof Symbol) {
                            processingEnv.getMessager().printMessage(ERROR, "Symbol '" + result + "' declared as terminal ($P/$L) and nonterminal ($R) at the same time", executableElement);
                            continue;
                        } else {
                            nonterminal = (Nonterminal) symbol;
                        }
                    } else {
                        symbolMap.put(result, nonterminal = new Nonterminal(result));
                    }
                } else if (patternAnnotation != null || literalAnnotation != null) {
                    if (patternAnnotation != null && literalAnnotation != null) {
                        processingEnv.getMessager().printMessage(ERROR, "Method cannot have $P and $L at the same time", executableElement);
                        continue;
                    }
                    boolean isPattern = patternAnnotation != null;
                    if (isPattern) {

                    } else {
                        String[] literalValues = literalAnnotation.value();
                        if (literalValues.length < 1) {
                            processingEnv.getMessager().printMessage(ERROR, "Literal string rule ($L) must have at least one value");
                            continue;
                        }
                    }
                    String[] values = isPattern ? new String[] { patternAnnotation.value() } : literalAnnotation.value();
                    for (String value : values) {
                        if (literalAnnotation != null && result == null) {
                            result = value;
                        }
                        // it's a pattern or literal
                        Symbol terminal;
                        if (result != null) {
                            Symbol existing = symbolMap.get(result);
                            if (existing != null) {
                                if (existing instanceof Nonterminal) {
                                    processingEnv.getMessager().printMessage(ERROR, "Symbol '" + result + "' declared as terminal ($P/$L) and nonterminal ($R) at the same time", executableElement);
                                    continue;
                                } else {
                                    terminal = (Symbol) existing;
                                }
                            } else {
                                symbolMap.put(result, terminal = new Symbol(result));
                            }
                        } else {
                            terminal = null;
                        }
                    }
                } else {
                    if (resultAnnotation == null) {
                        processingEnv.getMessager().printMessage(ERROR, "Method must either declare a rule or a terminal", executableElement);
                        continue;
                    }
                }
            }
        }
    }
}
