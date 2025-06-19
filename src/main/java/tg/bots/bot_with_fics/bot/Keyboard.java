package tg.bots.bot_with_fics.bot;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Data
public class Keyboard {


    public static InlineKeyboardMarkup getMainMenu(boolean isOwner) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        //ряд 1
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton search = new InlineKeyboardButton("Подобрать фик под настроение");
        search.setCallbackData("search");
        row1.add(search);
        InlineKeyboardButton random = new InlineKeyboardButton("Случайная работа");
        random.setCallbackData("random");
        row1.add(random);
        rowsInline.add(row1);
        //ряд 2
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton addFic = new InlineKeyboardButton("Добавить фанфик");
        addFic.setCallbackData("addFic");
        row2.add(addFic);
        InlineKeyboardButton complaint = new InlineKeyboardButton("Отправить жалобу");
        complaint.setCallbackData("complaint");
        row2.add(complaint);
        rowsInline.add(row2);
        //для Owner
        if (isOwner) {
            List<InlineKeyboardButton> row3 = new ArrayList<>();
            InlineKeyboardButton createFic = new InlineKeyboardButton("Сохранить фанфик");
            createFic.setCallbackData("createFic");
            row3.add(createFic);
            InlineKeyboardButton deleteFic = new InlineKeyboardButton("Удалить фанфик");
            deleteFic.setCallbackData("deleteFic");
            row3.add(deleteFic);
            rowsInline.add(row3);
        }
        keyboardMarkup.setKeyboard(rowsInline);
        return keyboardMarkup;
    }

    //меню настроения
    public static InlineKeyboardMarkup getMoodMenu() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        //1 ряд
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton fun = new InlineKeyboardButton("Весёлое");
        fun.setCallbackData("fun");
        row1.add(fun);
        InlineKeyboardButton romantic = new InlineKeyboardButton("Романтичное");
        romantic.setCallbackData("romantic");
        row1.add(romantic);
        rowsInline.add(row1);
        //2 ряд
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton melancholic = new InlineKeyboardButton("Меланхоличное");
        melancholic.setCallbackData("melancholic");
        row2.add(melancholic);
        InlineKeyboardButton alarming = new InlineKeyboardButton("Тревожное");
        alarming.setCallbackData("alarming");
        row2.add(alarming);
        rowsInline.add(row2);
        //3 ряд
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton hot = new InlineKeyboardButton("Страстное");
        hot.setCallbackData("hot");
        row3.add(hot);
        InlineKeyboardButton philosophical = new InlineKeyboardButton("Глубокомысленное");
        philosophical.setCallbackData("philosophical");
        row3.add(philosophical);
        rowsInline.add(row3);
        keyboardMarkup.setKeyboard(rowsInline);
        return keyboardMarkup;
    }

    //меню выйти или репоиск
    public static InlineKeyboardMarkup getChoiceMenu() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsList = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton mainMenu = new InlineKeyboardButton("Главное меню");
        mainMenu.setCallbackData("mainMenu");
        row1.add(mainMenu);
        InlineKeyboardButton research = new InlineKeyboardButton("Показать другой");
        research.setCallbackData("research");
        row1.add(research);
        rowsList.add(row1);
        keyboardMarkup.setKeyboard(rowsList);
        return keyboardMarkup;
    }

    //меню hot
    public static InlineKeyboardMarkup getCheck() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsList = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton good = new InlineKeyboardButton("Хорошо");
        good.setCallbackData("good");
        row1.add(good);
        InlineKeyboardButton no = new InlineKeyboardButton("Не надо");
        no.setCallbackData("no");
        row1.add(no);
        rowsList.add(row1);
        keyboardMarkup.setKeyboard(rowsList);
        return keyboardMarkup;
    }


}
