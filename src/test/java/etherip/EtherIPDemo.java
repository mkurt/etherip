/*******************************************************************************
 * Copyright (c) 2012-2021 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package etherip;

import etherip.data.CipException;
import etherip.protocol.ListServicesProtocol;
import etherip.types.CIPData;
import etherip.types.CIPData.Type;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Kay Kasemir, László Pataki
 */
@SuppressWarnings("nls")
public class EtherIPDemo {
    @Before
    public void setup() {
        TestSettings.logAll();
        Logger.getLogger("").setLevel(Level.ALL);
    }

    @Test
    public void testConnectTcp() throws Exception {
        try
                (
                        EtherNetIP plc = new EtherNetIP(TestSettings.get("plc"),
                                TestSettings.getInt("slot"));
                ) {
            plc.connectTcp();

            System.out.println("\n*\n* Connected:\n*\n");
            System.out.println(plc.getIdentity());
        } catch (final CipException e) {
            // It is possible to ask some individual field of the ExceptionCip, but these are included in the getMessage().
            // System.err.println(e.getStatusCode());
            // System.err.println(e.getStatusName());
            // System.err.println(e.getStatusDescription());
            System.err.println(e.getMessage());
            System.err.println("Failed with CipException");
            fail("Failed with CipException");
        }
    }

    @Test
    public void testConnectUdp() throws Exception {
        Logger.getLogger("").setLevel(Level.INFO);

        TestSettings.logAll();
        try
                (
                        EtherNetIP plc = new EtherNetIP(TestSettings.get("plc"),
                                TestSettings.getInt("slot"));
                ) {
            plc.connectUdp();

            System.out.println("\n*\n* UDP Socket established:\n*\n");
            System.out.println(plc);

            final ListServicesProtocol.Service[] listServices = plc.listServices();

            for (final ListServicesProtocol.Service service : listServices) {
                System.out.println(service);
            }
        } catch (final CipException e) {
            // It is possible to ask some individual field of the ExceptionCip, but these are included in the getMessage().
            // System.err.println(e.getStatusCode());
            // System.err.println(e.getStatusName());
            // System.err.println(e.getStatusDescription());
            System.err.println(e.getMessage());
            System.err.println("Failed with CipException");
            fail("Failed with CipException");
        }
    }

    @Test
    public void testFloat() throws Exception {
        try
                (
                        EtherNetIP plc = new EtherNetIP(TestSettings.get("plc"),
                                TestSettings.getInt("slot"));
                ) {
            plc.connectTcp();

            final String tag = TestSettings.get("float_tag");

            System.out.println("\n*\n* float '" + tag + "':\n*\n");
            CIPData value = plc.readTag(tag);
            System.out.println(value);
            assertThat(value, not(nullValue()));
            value.set(0, 47.11);
            plc.writeTag(tag, value);

            value = plc.readTag(tag);
            assertThat(value, not(nullValue()));
            System.out.println("Changed to " + value);
            assertEquals(47.11, value.getNumber(0).doubleValue(), 0.01);

            value.set(0, 48.12);
            plc.writeTag(tag, value);

            value = plc.readTag(tag);
            assertThat(value, not(nullValue()));
            System.out.println("Changed to " + value);
            assertEquals(48.12, value.getNumber(0).doubleValue(), 0.01);
        }
    }

    @Test
    public void testInt() throws Exception {
        try
                (
                        EtherNetIP plc = new EtherNetIP(TestSettings.get("plc"),
                                TestSettings.getInt("slot"));
                ) {
            plc.connectTcp();

            final String tag = TestSettings.get("int_tag");

            System.out.println("\n*\n* int '" + tag + "':\n*\n");
            CIPData value = plc.readTag(tag);
            System.out.println(value);
            assertThat(value, not(nullValue()));
            value.set(0, 42);
            plc.writeTag(tag, value);

            value = plc.readTag(tag);
            assertThat(value, not(nullValue()));
            System.out.println("Changed to " + value);
            assertThat(value.getNumber(0).intValue(), equalTo(42));

            value.set(0, 47);
            plc.writeTag(tag, value);

            value = plc.readTag(tag);
            assertThat(value, not(nullValue()));
            System.out.println("Changed to " + value);
            assertThat(value.getNumber(0).intValue(), equalTo(47));
        }
    }

    @Test
    public void testLong() throws Exception {
        try
                (
                        EtherNetIP plc = new EtherNetIP(TestSettings.get("plc"),
                                TestSettings.getInt("slot"));
                ) {
            plc.connectTcp();

            final String tag = TestSettings.get("long_tag");

            System.out.println("\n*\n* long '" + tag + "':\n*\n");
            CIPData value = plc.readTag(tag);
            System.out.println(value);
            assertThat(value, not(nullValue()));
            value.set(0, 421241231);
            plc.writeTag(tag, value);

            value = plc.readTag(tag);
            assertThat(value, not(nullValue()));
            System.out.println("Changed to " + value);
            assertThat(value.getNumber(0).intValue(), equalTo(421241231));

            value.set(0, 474346565);
            plc.writeTag(tag, value);

            value = plc.readTag(tag);
            assertThat(value, not(nullValue()));
            System.out.println("Changed to " + value);
            assertThat(value.getNumber(0).intValue(), equalTo(474346565));
        }
    }

    @Test
    public void testBool() throws Exception {
        try
                (
                        EtherNetIP plc = new EtherNetIP(TestSettings.get("plc"),
                                TestSettings.getInt("slot"));
                ) {
            plc.connectTcp();

            final String tag = TestSettings.get("bool_tag");
            System.out.println("\n*\n* bool '" + tag + "':\n*\n");

            CIPData value = plc.readTag(tag);
            System.out.println("Original Value: " + value);
            System.out.flush();
            System.err.flush();

            value = new CIPData(Type.BOOL, 1);

            value.set(0, 1);
            plc.writeTag(tag, value);
            value = plc.readTag(tag);
            System.out.println("Wrote 1: " + value);
            assertThat(value, not(nullValue()));
            assertThat(value.getNumber(0).intValue(), not(equalTo(0)));

            value.set(0, 0);
            plc.writeTag(tag, value);
            value = plc.readTag(tag);
            System.out.println("Wrote 0: " + value);
            assertThat(value, not(nullValue()));
            assertThat(value.getNumber(0).intValue(), equalTo(0));

            value.set(0, 255);
            plc.writeTags(new String[]{tag}, new CIPData[]{value});
            value = plc.readTag(tag);
            System.out.println("Wrote 255: " + value);
            assertThat(value, not(nullValue()));
            assertThat(value.getNumber(0).intValue(), not(equalTo(0)));
        }
    }

    @Test
    public void testString() throws Exception {
        try
                (
                        EtherNetIP plc = new EtherNetIP(TestSettings.get("plc"),
                                TestSettings.getInt("slot"));
                ) {
            plc.connectTcp();

            final String tag = TestSettings.get("string_tag");

            System.out.println("\n*\n* string '" + tag + "':\n*\n");
            CIPData value = plc.readTag(tag);
            System.out.println(value);
            assertThat(value, not(nullValue()));
            assertThat(value.isNumeric(), equalTo(false));
            String new_value = value.getString();
            if (new_value.equals("test"))
                new_value = "Testing!";
            else
                new_value = "test";
            System.out.println("Writing '" + new_value + "'");
            value.setString(new_value);
            plc.writeTag(tag, value);

            value = plc.readTag(tag);
            System.out.println(value);
            assertThat(value.getString(), equalTo(new_value));
        }
    }

    @Test
    public void testString2() throws Exception {
        try
                (
                        EtherNetIP plc = new EtherNetIP(TestSettings.get("plc"),
                                TestSettings.getInt("slot"));
                ) {
            plc.connectTcp();

            final String tag = TestSettings.get("string_tag");

            String value = "Merhaba Asker!";
            byte[] data = new byte[Math.max((value.length() + 7), 90)]; // 0x13 Too short error if data is < 90 bytes
            CIPData cipData = new CIPData(CIPData.Type.STRUCT, data);
            cipData.setString(value);

            plc.writeTag(tag, cipData);

            System.out.println("\n*\n* string '" + tag + "':\n*\n");
            CIPData stringValue = plc.readTag(tag);

            System.out.println(stringValue);
            assertThat(stringValue, not(nullValue()));
            assertThat(stringValue.isNumeric(), equalTo(false));

            String new_value = stringValue.getString();

            if (new_value.equals("Merhaba Asker!"))
                new_value = "Sag ol!";

            System.out.println("Writing '" + new_value + "'");

            stringValue.setString(new_value);
            plc.writeTag(tag, stringValue);

            stringValue = plc.readTag(tag);
            System.out.println(value);
            assertThat(stringValue.getString(), equalTo(new_value));
        }
    }

    @Test
    public void testMultiRead() throws Exception {
        try
                (
                        EtherNetIP plc = new EtherNetIP(TestSettings.get("plc"),
                                TestSettings.getInt("slot"));
                ) {
            plc.connectTcp();

            System.out.println("\n*\n* Multi read:\n*\n");
            final String[] tags = new String[]{TestSettings.get("float_tag"),
                    TestSettings.get("bool_tag"),
                    TestSettings.get("int_tag"),
                    TestSettings.get("string_tag")};
            final CIPData[] results = plc.readTags(tags);
            assertThat(results, not(nullValue()));
            assertThat(results.length, equalTo(tags.length));

            for (int i = 0; i < results.length; ++i) {
                System.out.println(tags[i] + " = " + results[i]);
            }
        }
    }
}
