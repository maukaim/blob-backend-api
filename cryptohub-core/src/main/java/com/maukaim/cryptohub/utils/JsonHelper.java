package com.maukaim.cryptohub.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JsonHelper {
    private static final ObjectMapper mapper = new BlobObjectMapper();

    public static <T> T deserialize(String json, Class<T> clazz){
        try{
            return mapper.readValue(json, clazz);
        } catch(IOException e){
            log.warn("Json deserialization failed for {}", json, e);
            return null;
        } catch (NullPointerException e){
            log.warn("Json deserialization failed, JSON is null");
            return null;
        }
    }

    public static<T> T deserialize(String json, TypeReference<T> typeReference){
        try{
            return mapper.readValue(json, typeReference);
        } catch(IOException e){
            log.warn("Json deserialization failed for {}", json, e);
            return null;
        } catch (NullPointerException e){
            log.warn("Json deserialization failed, JSON is null");
            return null;
        }
    }

    public static String serialize(Object obj){
        try{
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e){
            log.warn("Json serialization failed for {}", obj.getClass().getName(), e);
            return null;
        } catch (NullPointerException e){
            log.warn("Json serialization failed, JSON is null");
            return null;
        }
    }
}
