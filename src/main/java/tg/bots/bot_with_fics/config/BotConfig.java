package tg.bots.bot_with_fics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import tg.bots.bot_with_fics.bot.MyTelegramBot;
import tg.bots.bot_with_fics.repository.UserStateRepository;
import tg.bots.bot_with_fics.service.UserStateService;

@Configuration
public class BotConfig {


    @Bean
    public TelegramBotsApi telegramBotsApi(MyTelegramBot telegramBot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(telegramBot);
        return botsApi;
    }

}


