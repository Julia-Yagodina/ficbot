package tg.bots.bot_with_fics.converter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import tg.bots.bot_with_fics.entity.Fanfic;

public class JsonToFanficConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());

    public static Fanfic convert(String json) {
        try {
            return objectMapper.readValue(json, Fanfic.class);
        } catch (Exception e) {
            throw new RuntimeException("ошибка десериализации фанфика");
        }
    }
}
