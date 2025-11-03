package com.apartment.data_provider.core.models.providers.onliner;
import java.util.List;

public record OnlinerResponse<T>(
        List<T> apartments,
        int total,
        Page page
        ) {
}
