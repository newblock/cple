package com.qcx.mini.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by Smile on 17/5/26.
 */

public class GsonUtil {

    private static final String TAG = GsonUtil.class.getSimpleName();

    private GsonUtil() {
    }

    private static class init {
        static Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
    }

    public static Gson create() {
        return init.gson;
    }

    @SuppressWarnings("unchecked")
    private static class NullStringToEmptyAdapterFactory implements TypeAdapterFactory {

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType == String.class) {
                return (TypeAdapter<T>) new StringNullAdapter();
            }
            if (rawType == Integer.class) {
                return (TypeAdapter<T>) new IntegerNullAdapter();
            }
            if (rawType == Double.class) {
                return (TypeAdapter<T>) new DoubleNullAdapter();
            }
            if (rawType == Long.class) {
                return (TypeAdapter<T>) new LongNullAdapter();
            }
            if (rawType == Boolean.class) {
                return (TypeAdapter<T>) new BooleanNullAdapter();
            }
            return null;
        }
    }

    private static class StringNullAdapter extends TypeAdapter<String> {
        @Override
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
//                Log.e(TAG, "has a null value! ".concat(reader.toString()));
                return "";
            }
            return reader.nextString();
        }

        @Override
        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

    private static class IntegerNullAdapter extends TypeAdapter<Integer> {
        @Override
        public Integer read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
//                Log.e(TAG, "has a null value! ".concat(reader.toString()));
                return 0;
            }
            return reader.nextInt();
        }

        @Override
        public void write(JsonWriter writer, Integer value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

    private static class DoubleNullAdapter extends TypeAdapter<Double> {
        @Override
        public Double read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
//                Log.e(TAG, "has a null value! ".concat(reader.toString()));
                return 0.00;
            }
            return reader.nextDouble();
        }

        @Override
        public void write(JsonWriter writer, Double value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

    private static class LongNullAdapter extends TypeAdapter<Long> {
        @Override
        public Long read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
//                Log.e(TAG, "has a null value! ".concat(reader.toString()));
                return 0L;
            }
            return reader.nextLong();
        }

        @Override
        public void write(JsonWriter writer, Long value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

    private static class BooleanNullAdapter extends TypeAdapter<Boolean> {
        @Override
        public Boolean read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
//                Log.e(TAG, "has a null value! ".concat(reader.toString()));
                return false;
            }
            return reader.nextBoolean();
        }

        @Override
        public void write(JsonWriter writer, Boolean value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }
}
