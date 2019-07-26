package siri_xlite.common;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.function.Consumer;

public interface JsonUtils {

    JsonFactory factory = new JsonFactory();

    default void writeStartDocument(JsonGenerator writer) {
        try {
            writer.writeStartObject();
            writer.writeObjectFieldStart("Siri");
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    default void writeEndDocument(JsonGenerator writer) {
        try {
            writer.writeEndObject();
            writer.writeEndObject();
            writer.flush();
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    default JsonGenerator createJsonWriter(final OutputStream out) {
        JsonGenerator result = null;
        try {
            result = factory.createGenerator(out);
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
        return result;
    }

    default void writeNaturalLanguageString(JsonGenerator writer, String name, String value) {
        try {
            writer.writeArrayFieldStart(name);
            writer.writeStartObject();
            writer.writeStringField("value", value);
            writer.writeEndObject();
            writer.writeEndArray();
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    default void write(JsonGenerator writer, String name, String value) {
        try {
            if (StringUtils.isNotEmpty(value)) {
                writer.writeStringField(name, value);
            }

        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    default void write(JsonGenerator writer, String name, Boolean value) {
        try {
            if (value != null) {
                writer.writeBooleanField(name, value);
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    default void write(JsonGenerator writer, String name, Integer value) {
        try {
            if (value != null) {
                writer.writeNumberField(name, value);
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    default void write(JsonGenerator writer, String name, Long value) {
        try {
            if (value != null) {
                writer.writeNumberField(name, value);
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    default void write(JsonGenerator writer, String name, Float value) {
        try {
            if (value != null) {
                writer.writeNumberField(name, value);
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    default void write(JsonGenerator writer, String name, Double value) {
        try {
            if (value != null) {
                writer.writeNumberField(name, value);
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    default void write(JsonGenerator writer, String name, BigDecimal value) {
        try {
            if (value != null) {
                writer.writeNumberField(name, value);
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    default <T> void write(JsonGenerator writer, String name, Collection<T> values) {
        try {
            if (CollectionUtils.isNotEmpty(values)) {
                writer.writeArrayFieldStart(name);
                for (Object value : values) {
                    writer.writeString(value.toString());
                }
                writer.writeEndArray();
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    default <T> void write(JsonGenerator writer, String name, Collection<T> values, Consumer<T> consumer) {
        try {
            if (CollectionUtils.isNotEmpty(values)) {
                writer.writeArrayFieldStart(name);
                values.forEach(consumer);
                writer.writeEndArray();
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    default void write(JsonGenerator writer, Runnable action) {
        try {
            writer.writeStartObject();
            action.run();
            writer.writeEndObject();
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    default void write(JsonGenerator writer, String name, Runnable action) {
        try {
            writer.writeObjectFieldStart(name);
            action.run();
            writer.writeEndObject();
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    @FunctionalInterface
    interface ConsumerWithException<T, E extends Throwable> {
        void accept(T t) throws E;
    }

    default <T, E extends Exception> Consumer<T> wrapper(ConsumerWithException<T, E> fn) {
        return t -> {
            try {
                fn.accept(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @FunctionalInterface
    interface RunnableWithException<E extends Throwable> {
        void run() throws E;
    }

    default <T, E extends Exception> Runnable wrapper(RunnableWithException<E> fn) {
        return () -> {
            try {
                fn.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

}
