/**
 * END USER LICENSE AGREEMENT (“EULA”)
 *
 * READ THIS AGREEMENT CAREFULLY (date: 9/13/2011):
 * http://www.akiban.com/licensing/20110913
 *
 * BY INSTALLING OR USING ALL OR ANY PORTION OF THE SOFTWARE, YOU ARE ACCEPTING
 * ALL OF THE TERMS AND CONDITIONS OF THIS AGREEMENT. YOU AGREE THAT THIS
 * AGREEMENT IS ENFORCEABLE LIKE ANY WRITTEN AGREEMENT SIGNED BY YOU.
 *
 * IF YOU HAVE PAID A LICENSE FEE FOR USE OF THE SOFTWARE AND DO NOT AGREE TO
 * THESE TERMS, YOU MAY RETURN THE SOFTWARE FOR A FULL REFUND PROVIDED YOU (A) DO
 * NOT USE THE SOFTWARE AND (B) RETURN THE SOFTWARE WITHIN THIRTY (30) DAYS OF
 * YOUR INITIAL PURCHASE.
 *
 * IF YOU WISH TO USE THE SOFTWARE AS AN EMPLOYEE, CONTRACTOR, OR AGENT OF A
 * CORPORATION, PARTNERSHIP OR SIMILAR ENTITY, THEN YOU MUST BE AUTHORIZED TO SIGN
 * FOR AND BIND THE ENTITY IN ORDER TO ACCEPT THE TERMS OF THIS AGREEMENT. THE
 * LICENSES GRANTED UNDER THIS AGREEMENT ARE EXPRESSLY CONDITIONED UPON ACCEPTANCE
 * BY SUCH AUTHORIZED PERSONNEL.
 *
 * IF YOU HAVE ENTERED INTO A SEPARATE WRITTEN LICENSE AGREEMENT WITH AKIBAN FOR
 * USE OF THE SOFTWARE, THE TERMS AND CONDITIONS OF SUCH OTHER AGREEMENT SHALL
 * PREVAIL OVER ANY CONFLICTING TERMS OR CONDITIONS IN THIS AGREEMENT.
 */
package com.akiban.server.types3.common;

import java.math.BigDecimal;

public interface BigDecimalWrapper extends Comparable<BigDecimalWrapper> {
    
    BigDecimalWrapper set(BigDecimalWrapper value);
    BigDecimalWrapper add(BigDecimalWrapper addend);
    BigDecimalWrapper subtract(BigDecimalWrapper subtrahend);
    BigDecimalWrapper multiply(BigDecimalWrapper multiplicand);
    BigDecimalWrapper divide(BigDecimalWrapper divisor);
    BigDecimalWrapper floor();
    BigDecimalWrapper ceil();
    BigDecimalWrapper truncate(int scale);
    BigDecimalWrapper round(int scale);
    BigDecimalWrapper divideToIntegralValue(BigDecimalWrapper divisor);
    BigDecimalWrapper divide(BigDecimalWrapper divisor, int scale);
    BigDecimalWrapper parseString(String num);
    BigDecimalWrapper round (int precision, int scale);
    BigDecimalWrapper negate();
    BigDecimalWrapper abs();
    BigDecimalWrapper mod(BigDecimalWrapper num);

    int compareTo (BigDecimalWrapper o);
    int getScale();
    int getPrecision();
    int getSign();
    boolean isZero();
    void reset();

    BigDecimal asBigDecimal();
}
