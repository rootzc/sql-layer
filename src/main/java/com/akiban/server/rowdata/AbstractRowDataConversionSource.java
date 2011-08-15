/**
 * Copyright (C) 2011 Akiban Technologies Inc.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 */

package com.akiban.server.rowdata;

import com.akiban.server.AkServerUtil;
import com.akiban.server.Quote;
import com.akiban.server.encoding.EncodingException;
import com.akiban.server.types.AkType;
import com.akiban.server.types.ConversionSource;
import com.akiban.server.types.Converters;
import com.akiban.server.types.SourceIsNullException;
import com.akiban.util.AkibanAppender;
import com.akiban.util.ByteSource;
import com.akiban.util.WrappingByteSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

abstract class AbstractRowDataConversionSource implements ConversionSource {

    // ConversionSource interface

    @Override
    public BigDecimal getDecimal() {
        AkibanAppender appender = AkibanAppender.of(new StringBuilder(fieldDef.getMaxStorageSize()));
        ConversionHelperBigDecimal.decodeToString(fieldDef, bytes, getRawOffsetAndWidth(), appender);
        String asString = appender.toString();
        assert ! asString.isEmpty();
        try {
            return new BigDecimal(asString);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(asString);
        }
    }

    @Override
    public BigInteger getUBigInt() {
        long offsetAndWidth = getRawOffsetAndWidth();
        if (offsetAndWidth == 0) {
            return null;
        }
        int offset = (int)offsetAndWidth;
        return AkServerUtil.getULong(bytes, offset);
    }

    @Override
    public ByteSource getVarBinary() {
        long offsetAndWidth = getRawOffsetAndWidth();
        if (offsetAndWidth == 0) {
            return null;
        }
        int offset = (int) offsetAndWidth + fieldDef.getPrefixSize();
        int size = (int) (offsetAndWidth >>> 32) - fieldDef.getPrefixSize();
        return byteSource.wrap(bytes, offset, size);
    }

    @Override
    public double getDouble() {
        long asLong = extractLong(Signage.SIGNED);
        return Double.longBitsToDouble(asLong);
    }

    @Override
    public double getUDouble() {
        return getDouble();
    }

    @Override
    public float getFloat() {
        long asLong = extractLong(Signage.SIGNED);
        int asInt = (int) asLong;
        return Float.intBitsToFloat(asInt);
    }

    @Override
    public float getUFloat() {
        return getFloat();
    }

    @Override
    public long getDate() {
        return extractLong(Signage.SIGNED);
    }

    @Override
    public long getDateTime() {
        return extractLong(Signage.SIGNED);
    }

    @Override
    public long getInt() {
        return extractLong(Signage.SIGNED);
    }

    @Override
    public long getLong() {
        return extractLong(Signage.SIGNED);
    }

    @Override
    public long getTime() {
        return extractLong(Signage.SIGNED);
    }

    @Override
    public long getTimestamp() {
        return extractLong(Signage.SIGNED);
    }

    @Override
    public long getUInt() {
        return extractLong(Signage.UNSIGNED);
    }

    @Override
    public long getYear() {
        return extractLong(Signage.SIGNED) & 0xFF;
    }

    @Override
    public String getString() {
        final long location = getRawOffsetAndWidth();
        return location == 0
                ? null
                : AkServerUtil.decodeMySQLString(bytes, (int)location, (int)(location >>> 32), fieldDef);
    }

    @Override
    public String getText() {
        return getString();
    }

    @Override
    public void appendAsString(AkibanAppender appender, Quote quote) {
        AkType type = getConversionType();
        quote.quote(appender, type);
        if (type == AkType.VARCHAR || type == AkType.TEXT) {
            appendStringField(appender, quote);
        }
        // TODO the rest of this method doesn't give Quote a crack at things.
        // (I think quoting should really be selected at the Appender level, not externally)
        else if (type == AkType.DECIMAL) {
            ConversionHelperBigDecimal.decodeToString(fieldDef, bytes, getRawOffsetAndWidth(), appender);
        } else {
            Converters.convert(this, appender.asConversionTarget());
        }
        quote.quote(appender, type);
    }

    // for subclasses
    protected abstract long getRawOffsetAndWidth();

    protected void bind(byte[] bytes, FieldDef fieldDef) {
        this.bytes = bytes;
        this.fieldDef = fieldDef;
    }

    // for use within this class
    // Stolen from the Encoding classes

    private void appendStringField(AkibanAppender appender, Quote quote) {
        try {
            final long location = getCheckedOffsetAndWidth();
            if (appender.canAppendBytes()) {
                ByteBuffer buff = location == 0
                        ? null
                        : AkServerUtil.byteBufferForMySQLString(bytes, (int)location, (int) (location >>> 32), fieldDef);
                quote.append(appender, buff, fieldDef.column().getCharsetAndCollation().charset());
            }
            else {
                String s = location == 0
                        ? null
                        : AkServerUtil.decodeMySQLString(bytes, (int)location, (int) (location >>> 32), fieldDef);
                quote.append(appender, s);
            }
        } catch (EncodingException e) {
            quote.append(appender, "<encoding exception! " + e.getMessage() + '>');
        }
    }
    
    private long extractLong(Signage signage) {
        long offsetAndWidth = getCheckedOffsetAndWidth();
        final int offset = (int)offsetAndWidth;
        final int width = (int)(offsetAndWidth >>> 32);
        if (signage == Signage.SIGNED) {
            return AkServerUtil.getSignedIntegerByWidth(bytes, offset, width);
        } else {
            assert signage == Signage.UNSIGNED;
            return AkServerUtil.getUnsignedIntegerByWidth(bytes, offset, width);
        }
    }

    private long getCheckedOffsetAndWidth() {
        long offsetAndWidth = getRawOffsetAndWidth();
        if (offsetAndWidth == 0) {
            throw new SourceIsNullException();
        }
        return offsetAndWidth;
    }

    // object state
    private final WrappingByteSource byteSource = new WrappingByteSource();
    private byte[] bytes;
    private FieldDef fieldDef;

    private enum Signage {
        SIGNED, UNSIGNED
    }
}
