package com.example.chatbotv1.API;

import java.util.Locale;

public interface ChatterBot {
    ChatterBotSession createSession(Locale... locales);
}
