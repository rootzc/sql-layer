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

package com.akiban.server.types3.mcompat.mcasts;

import com.akiban.server.types3.TExecutionContext;

public final class CastUtils
{
    public static long round (long max, long min, double val, TExecutionContext context)
    {
        long rounded = Math.round(val);
        
        if (Double.compare(rounded, val) != 0)
            context.reportTruncate(Double.toString(val), Long.toString(rounded));
        return getInRange(max, min, rounded, context);
    }

    public static long getInRange (long max, long min, long val, TExecutionContext context)
    {
        if (val > max)
        {
            context.reportTruncate(Long.toString(val), Long.toString(max));
            return max;
        }
        else if (val < min)
        {
            context.reportTruncate(Long.toString(val), Long.toString(min));
            return min;
        }
        else
            return val;
    }
    
     
    public static long getInRange (long max, long min, long val, long defaultVal, TExecutionContext context)
    {
        if (val > max)
        {
            context.reportTruncate(Long.toString(val), Long.toString(defaultVal));
            return defaultVal;
        }
        else if (val < min)
        {
            context.reportTruncate(Long.toString(val), Long.toString(defaultVal));
            return defaultVal;
        }
        else
            return val;
    }
     
    /**
     * Truncate non-digits part
     * @param st
     * @return 
     */
    public static String truncateNonDigits(String st, TExecutionContext context)
    {
        int n = 0;
        st = st.trim();
        
        // if the string starts with non-digits, return 0
        if (st.isEmpty() || !Character.isDigit(st.charAt(n++)) 
                && (n == st.length() || n < st.length() && !Character.isDigit(st.charAt(n++))))
        {
            context.reportTruncate(st, "0");
            return "0";
        }
        
        char ch;
        boolean sawDot = false;
        for (; n < st.length(); ++n)
            if ((!Character.isDigit(ch = st.charAt(n)))
                    && (sawDot || !(sawDot = ch == '.')))
            {
                context.reportTruncate(st ,(st = st.substring(0, n)));
                return st;
            }
        
        return st;
    }
}
