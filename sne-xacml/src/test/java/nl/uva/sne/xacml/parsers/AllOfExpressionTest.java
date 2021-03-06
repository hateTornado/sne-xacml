/*
 * SNE-XACML: A high performance XACML evaluation engine.
 *
 * Copyright (C) 2013-2014 Canh Ngo <canhnt@gmail.com>
 * System and Network Engineering Group, University of Amsterdam.
 * All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA
 */
package nl.uva.sne.xacml.parsers;

import nl.uva.sne.midd.MIDDException;
import nl.uva.sne.xacml.policy.parsers.AllOfExpression;
import nl.uva.sne.xacml.policy.parsers.AttributeInfo;
import nl.uva.sne.xacml.policy.parsers.XACMLParsingException;
import nl.uva.sne.xacml.util.XACMLUtil;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AllOfExpressionTest {

    private static final String SAMPLE_POLICY_FILE = "src/test/resources/xacml3-AllOfExpressionTest.xml";

    @Test
    public void testParse() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
        TargetType target = readTarget();
        assertNotNull(target);

        List<AnyOfType> lstAnyOf = target.getAnyOf();
        assertTrue(lstAnyOf != null && lstAnyOf.size() == 1);

        List<AllOfType> lstAllOf = lstAnyOf.get(0).getAllOf();
        assertTrue(lstAllOf != null && lstAllOf.size() == 1);

        AllOfType allOf = lstAllOf.get(0);
        assertTrue(allOf != null);


        AllOfExpression allOfExp = new AllOfExpression();
        try {
            allOfExp.parse(allOf);
            Map<String, AttributeInfo> mapIntervals = allOfExp.getAttributeInfos();
            Iterator<String> it = mapIntervals.keySet().iterator();

            while (it.hasNext()) {
                String varId = it.next();
                System.out.println("Variable '" + varId + "' has interval: " + mapIntervals.get(varId).getInterval());
            }

        } catch (XACMLParsingException | MIDDException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private TargetType readTarget() throws ParserConfigurationException,
            SAXException, IOException, FileNotFoundException {
        PolicyType p1 = XACMLUtil.unmarshalPolicyType(new FileInputStream(SAMPLE_POLICY_FILE));
        List<Object> objs = p1.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition();

        assertNotNull(objs);
        assertTrue(objs.size() == 1);
        assertTrue(objs.get(0) instanceof RuleType);

        RuleType r1 = (RuleType) objs.get(0);

        TargetType target = r1.getTarget();
        return target;
    }

}
