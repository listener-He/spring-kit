package org.hehh.cloud.spring.mvc.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.cache.support.NullValue;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author : HeHui
 * @date : 2019-06-25 17:13
 * @describe : 对序列化@class的支持
 */
public class NullValueSerializer extends StdSerializer<NullValue> {
    private static final long serialVersionUID = 1999052150548658807L;
    private  String classIdentifier;

    public NullValueSerializer() {
        this(null);
    }

    public NullValueSerializer(@Nullable String classIdentifier) {
        super(NullValue.class);
        this.classIdentifier = StringUtils.hasText(classIdentifier) ? classIdentifier : "@class";
    }

    public void serialize(NullValue value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField(this.classIdentifier, NullValue.class.getName());
        jgen.writeEndObject();
    }
}
