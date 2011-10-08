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

package org.jboss.flicc.generator;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class Rule {
    private final String name;
    private final Element element;
    private final String resultName;
    private final String[] ruleSpec;
    private final ActualParameter[] actualParameters;

    public Rule(final String name, final Element element, final String resultName, final String[] ruleSpec, final ActualParameter[] actualParameters) {
        this.name = name;
        this.element = element;
        this.resultName = resultName;
        this.ruleSpec = ruleSpec;
        this.actualParameters = actualParameters;
    }

    public static final class ActualParameter {
        private final VariableElement parameterElement;
        private final int index;

        public ActualParameter(final VariableElement parameterElement, final int index) {
            this.parameterElement = parameterElement;
            this.index = index;
        }

        public VariableElement getParameterElement() {
            return parameterElement;
        }

        public int getIndex() {
            return index;
        }
    }
}
