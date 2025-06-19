package tg.bots.bot_with_fics.bot;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import tg.bots.bot_with_fics.converter.JsonToFanficConverter;
import tg.bots.bot_with_fics.entity.Fanfic;
import tg.bots.bot_with_fics.entity.UserState;
import tg.bots.bot_with_fics.service.FanficService;
import tg.bots.bot_with_fics.service.OwnerService;
import tg.bots.bot_with_fics.service.UserStateService;


@Slf4j
@Component
@RequiredArgsConstructor
public class MyTelegramBot extends TelegramLongPollingBot {
    private final UserStateService userStateService;
    private final FanficService fanficService;
    private final OwnerService ownerService;
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.username}")
    private String botUsername;




    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    //обработчик команд
    public SendMessage commandHandler(String command, long chatId) {
        UserState userState = userStateService.findById(chatId);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        switch (command) {
            case "/start":
                userState.setState("MAIN_MENU");
                message.setText("Выберите действие:");
                message.setReplyMarkup(Keyboard.getMainMenu(ownerService.hasOwner(chatId)));
                break;
            case "/iwanttobreakfree":
                ownerService.createOwner(chatId);
                if (ownerService.hasOwner(chatId)) {
                    message.setText("Поздравляю, вы стали админом.");
                } else {
                    message.setText("Что-то пошло не так с регистрацией адмна");
                }
                message.setReplyMarkup(Keyboard.getMainMenu(ownerService.hasOwner(chatId)));
                break;
            default:
                userState.setState("MAIN_MENU");
                message.setText("Такой команды не существует.\nВыберите действие:");
                message.setReplyMarkup(Keyboard.getMainMenu(ownerService.hasOwner(chatId)));
        }
        userStateService.save(userState);
        return message;
    }


    //обработчик обратной связи
    public SendMessage feedbackHandler(String text, long chatId) {
        UserState userState = userStateService.findById(chatId);
        log.info("user state is " + userState.getState());
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        switch (userState.getState()) {
            case "ADD_FIC":
                log.info("user sand data for fic");
                for (long owners : ownerService.getOwners()) {
                    send("!добавить! " + text, chatId);
                }
                message.setText("Фанфик отправлен на рассмотрение.\nВыберите действие:");
                message.setReplyMarkup(Keyboard.getMainMenu(ownerService.hasOwner(chatId)));
                break;
            case "COMPLAINT":
                log.info("user send complaint");
                for (long owners : ownerService.getOwners()) {
                    send("!жалоба! " + text, chatId);
                }
                message.setText("Жалоба отправлена на рассмотрение.\nВыберите действие:");
                message.setReplyMarkup(Keyboard.getMainMenu(ownerService.hasOwner(chatId)));
                break;
            case "CREATE" :
                log.info("admin will create fic");
                Fanfic fanfic = JsonToFanficConverter.convert(text);
                int num =  fanficService.createFic(fanfic);
                if (num == 1) {
                    message.setText("Фанфик загружен");
                } else {
                    message.setText("Фанфик уже существует");
                }
                break;
            case "DELETE" :
                log.info("admin will delete fic");
                String[] data = text.split("_");
                int a = fanficService.delete(data[0], data[1]);
                if (a==1) {
                    message.setText("Фанфик удален");
                } else {
                    message.setText("Такого фанфика нет в базе.");
                }
                break;
            default:
                message.setText("Такой команды не существует.\nВыберите действие:");
                message.setReplyMarkup(Keyboard.getMainMenu(ownerService.hasOwner(chatId)));
        }
        userState.setState("MAIN_MENU");
        userStateService.save(userState);
        return message;
    }

    //обработчик кнопок
    public SendMessage buttonHandler(String callBackData, long chatId) {
        UserState userState = userStateService.findById(chatId);
        Fanfic fanfic = null;
        boolean validSearch = userState.getState().equals("FUN") || userState.getState().equals("ROMANTIC") ||
                userState.getState().equals("MELANCHOLIC") || userState.getState().equals("ALARMING") ||
                userState.getState().equals("PHILOSOPHICAL");
        boolean isRandom = userState.getState().equals("RANDOM");
        boolean isHot = userState.getState().equals("HOT");
        boolean isSearch = userState.getState().startsWith("FUN_") || userState.getState().startsWith("ROMANTIC_") ||
                userState.getState().startsWith("MELANCHOLIC_") || userState.getState().startsWith("ALARMING_") ||
                userState.getState().startsWith("PHILOSOPHICAL");
        SendMessage message = new SendMessage();
        String textForCheck = "Как вы относитесь к откровенным сценам?";
        message.setChatId(chatId);

        switch (callBackData) {
            case "createFic" :
                userState.setState("CREATE");
                message.setText("Введите информацию в JSON");
                break;
            case "deleteFic" :
                userState.setState("DELETE");
                message.setText("Введите информацию в JSON");
                break;
            case "search" :
                userState.setState("SEARCH");
                message.setText("Какое у вас сегодня настроение?");
                message.setReplyMarkup(Keyboard.getMoodMenu());
                break;
            case "random" :
                userState.setState("RANDOM");
                message.setText("Случайная работа:");
                fanfic = fanficService.getRandomFanfic();
                message = addFic(fanfic, chatId);
                break;
            case "addFic" :
                userState.setState("ADD_FIC");
                message.setText("Напишите о работе в сообщении:\nназвание,\nссылку,\nавтор/переводчик,\nрейтинг,\n" +
                        "пейринг (если есть или джен),\nописание,\nесть ли откровенные сцены,\n" +
                        "к какому настроению подходит (или жанр как у фильма).");
                break;
            case "complaint" :
                userState.setState("COMPLAINT");
                message.setText("Напишите название работы, автора и вашу претензию. Например, " +
                        "много грамматических ошибок или несоответствие настроению.");
                break;
            case "research":
                if (isRandom) {
                    fanfic = fanficService.getRandomFanfic();
                    message = addFic(fanfic, chatId);
                }
                else if (isHot) {
                    fanfic = fanficService.getRandomFanficByMood(userState.getState());
                    message = addFic(fanfic, chatId);
                }
                else if (isSearch) {
                    fanfic = fanficService.getRandomFanficByMoodAndCheck(userState.getState());
                    message = addFic(fanfic, chatId);
               } else {
                userState.setState("MAIN_MENU");
                message.setText("Данная кнопка устарела. Попробуйте еще раз:");
                message.setReplyMarkup(Keyboard.getMainMenu(ownerService.hasOwner(chatId)));
               }
                break;
            case "mainMenu":
                userState.setState("MAIN_MENU");
                message.setText("Выберите действие:");
                message.setReplyMarkup(Keyboard.getMainMenu(ownerService.hasOwner(chatId)));
                break;
            case "fun":
                userState.setState("FUN");
                message.setText(textForCheck);
                message.setReplyMarkup(Keyboard.getCheck());
                break;
            case "romantic":
                userState.setState("ROMANTIC");
                message.setText(textForCheck);
                message.setReplyMarkup(Keyboard.getCheck());
                break;
            case "melancholic":
                userState.setState("MELANCHOLIC");
                message.setText(textForCheck);
                message.setReplyMarkup(Keyboard.getCheck());
                break;
            case "alarming":
                userState.setState("ALARMING");
                message.setText(textForCheck);
                message.setReplyMarkup(Keyboard.getCheck());
                break;
            case "hot":
                userState.setState("HOT");
                fanfic = fanficService.getRandomFanficByMood(userState.getState());
                message = addFic(fanfic, chatId);
                break;
            case "philosophical":
                userState.setState("PHILOSOPHICAL");
                message.setText(textForCheck);
                message.setReplyMarkup(Keyboard.getCheck());
                break;
            case "no":
                if (validSearch) {
                    userState.setState(userState.getState()+"_FALSE");
                    fanfic = fanficService.getRandomFanficByMoodAndCheck(userState.getState());
                    message = addFic(fanfic, chatId);
                } else {
                    userState.setState("MAIN_MENU");
                    message.setText("Данная кнопка устарела. Попробуйте еще раз:");
                    message.setReplyMarkup(Keyboard.getMainMenu(ownerService.hasOwner(chatId)));
                }
                break;
            case "good":
                if (validSearch) {
                    userState.setState(userState.getState()+"_TRUE");
                    fanfic = fanficService.getRandomFanficByMoodAndCheck(userState.getState());
                    message = addFic(fanfic, chatId);
                } else {
                    userState.setState("MAIN_MENU");
                    message.setText("Данная кнопка устарела. Попробуйте еще раз:");
                    message.setReplyMarkup(Keyboard.getMainMenu(ownerService.hasOwner(chatId)));
                }
                break;
        }

        userStateService.save(userState);
        return message;
    }




    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = null;
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            if (messageText.startsWith("/")) {
                log.info("text message starts with /");
                message = commandHandler(messageText, chatId);
            } else {
                log.info("text message doesn't starts with с /");
                message = feedbackHandler(messageText, chatId);
            }

        } else if (update.hasCallbackQuery()) {
            log.info("user selected button ");
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String callbackData = update.getCallbackQuery().getData();
            message = buttonHandler(callbackData, chatId);
        }
        if (message != null) {
            log.info("send message");
            send(message);
        }
    }

    public SendMessage addFic(Fanfic fanfic, long chatId) {
       SendMessage message = new SendMessage();
       message.setChatId(chatId);
       if (fanfic != null) {
           message.setText("\uD83D\uDCAB  " + fanfic.getPost());
           message.setParseMode("MarkdownV2");
           message.disableWebPagePreview();
           message.setReplyMarkup(Keyboard.getChoiceMenu());
       } else {
           message.setText("В данном разделе пока нет работ.");
           message.setReplyMarkup(Keyboard.getMoodMenu());

       }
        return message;
    }


    private void send(String text, long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        }catch (TelegramApiException e) {
            log.info("message was not saved");
            e.printStackTrace();
        }
    }

    private void send(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        }catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
