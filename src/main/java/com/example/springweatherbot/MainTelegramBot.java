package com.example.springweatherbot;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Slf4j
@Component
public class MainTelegramBot extends TelegramLongPollingBot {

    @org.springframework.beans.factory.annotation.Value("${telegram.bot.name}")
    private String name;

    @org.springframework.beans.factory.annotation.Value("${telegram.bot.token}")
    private String token;


    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    private String weather;
    private final SendMessage sendMessage = new SendMessage();


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
            final Message message = update.getMessage();
            if (message.hasText()){
                sendMessage.setChatId(message.getChatId().toString());

                try {
                    weather = Weather.getWeather(message.getText());
                    sendMessage.setText(weather);
                } catch (IOException e) {
                    sendMessage.setText("Нет такого города!");
                }
                catch (Throwable e) {
                    e.printStackTrace();
                    sendMessage.setText("Что то на сервере пошло не так, какие то не лады с этим городом... попробуйте еще раз =)");
                }

                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }
        }

    }




}