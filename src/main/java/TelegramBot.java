import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.BotConfig;
import model.CurrencyEnum;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import service.ApiService;

public class TelegramBot extends TelegramLongPollingBot {

  @Override
  public String getBotUsername() {
    return BotConfig.botUsername;
  }

  @Override
  public String getBotToken() {
    return BotConfig.botToken;
  }

  @Override
  public void onUpdateReceived(Update update) {
    Message message = update.getMessage();

    if (message != null && message.hasText()) {
      try {
        handleMessage(message);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  private void handleMessage(Message message) throws IOException {

    if (message.getText().equals("/start")) {

      sendMessage(message, BotConfig.startMessage, false);

    } else if (CurrencyEnum.isExist(message.getText())) {

      String result = ApiService.getRateByCode(message.getText());
      sendMessage(message, result + " тг", true);

    } else {

      sendMessage(message, BotConfig.errorMessage, true);

    }
  }

  private void sendMessage(Message message, String text, Boolean isReplyToMessage) {

    SendMessage sendMsg = new SendMessage();
    sendMsg.enableMarkdown(true);
    sendMsg.setChatId(message.getChatId().toString());

    if (isReplyToMessage) {
      sendMsg.setReplyToMessageId(message.getMessageId());
    }

    sendMsg.setText(text);

    try {
      setButtons(sendMsg);
      execute(sendMsg);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  public void setButtons(SendMessage sendMsg) {

    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    sendMsg.setReplyMarkup(replyKeyboardMarkup);
    replyKeyboardMarkup.setSelective(true);
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(false);

    List<KeyboardRow> rowList = new ArrayList<>();
    KeyboardRow keyboard = new KeyboardRow();

    keyboard.add(new KeyboardButton(CurrencyEnum.EUR.name()));
    keyboard.add(new KeyboardButton(CurrencyEnum.USD.name()));
    keyboard.add(new KeyboardButton(CurrencyEnum.RUB.name()));

    rowList.add(keyboard);
    replyKeyboardMarkup.setKeyboard(rowList);

  }

  public static void main(String[] args) throws IOException {

    try {
      TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
      api.registerBot(new TelegramBot());
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }

  }

}
