package com.apartment.notification.core.enums;

public enum ParseMode {
    MARKDOWNV2("MarkdownV2"),
    HTML("HTML");

    private String type;

    ParseMode(String str) {
        type = str;
    }
    public String getType() {
        return type;
    }
    public static ParseMode getStatusByString(String value) {
        for (ParseMode status : ParseMode.values()) {
            if (status.type.equals(value)) {
                return status;
            }
        }
        throw new EnumConstantNotPresentException(ParseMode.class, "%s not found".formatted(value));
    }
}
