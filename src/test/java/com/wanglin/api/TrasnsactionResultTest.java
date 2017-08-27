package com.wanglin.api;

import static io.dropwizard.testing.FixtureHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TrasnsactionResultTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serializesToJSON() throws Exception {
        final UserCoinInfo fromUserCoinInfo = new UserCoinInfo(1L, 1L);
        final UserCoinInfo toUserCoinInfo   = new UserCoinInfo(2L, 3L);
        final TransactionResult trasnsactionResult = new TransactionResult(fromUserCoinInfo, toUserCoinInfo);

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/trasnsactionResult.json"), TransactionResult.class));

        assertThat(MAPPER.writeValueAsString(trasnsactionResult)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        final UserCoinInfo fromUserCoinInfo = new UserCoinInfo(1L, 1L);
        final UserCoinInfo toUserCoinInfo   = new UserCoinInfo(2L, 3L);
        final TransactionResult trasnsactionResult = new TransactionResult(fromUserCoinInfo, toUserCoinInfo);
        final TransactionResult expected = MAPPER.readValue(fixture("fixtures/trasnsactionResult.json"), TransactionResult.class);

        assertThat(expected.getFromUser().getUserId()).isEqualTo(trasnsactionResult.getFromUser().getUserId());
        assertThat(expected.getFromUser().getCoins()).isEqualTo(trasnsactionResult.getFromUser().getCoins());
        assertThat(expected.getToUser().getUserId()).isEqualTo(trasnsactionResult.getToUser().getUserId());
        assertThat(expected.getToUser().getCoins()).isEqualTo(trasnsactionResult.getToUser().getCoins());
    }
}
