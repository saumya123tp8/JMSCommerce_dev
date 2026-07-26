package com.example.JMSCommerce.Utility.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CurrencyType {

    INR("Indian Rupee", "₹"),
    USD("US Dollar", "$"),
    EUR("Euro", "€"),
    GBP("British Pound", "£"),
    JPY("Japanese Yen", "¥"),
    AUD("Australian Dollar", "A$"),
    CAD("Canadian Dollar", "C$"),
    CHF("Swiss Franc", "CHF"),
    CNY("Chinese Yuan", "¥"),
    SGD("Singapore Dollar", "S$"),
    AED("UAE Dirham", "د.إ"),
    SAR("Saudi Riyal", "﷼"),
    QAR("Qatari Riyal", "﷼"),
    KWD("Kuwaiti Dinar", "KD"),
    BHD("Bahraini Dinar", "BD"),
    OMR("Omani Rial", "OMR"),
    MYR("Malaysian Ringgit", "RM"),
    THB("Thai Baht", "฿"),
    KRW("South Korean Won", "₩"),
    NZD("New Zealand Dollar", "NZ$");

    private final String displayName;
    private final String symbol;
}
